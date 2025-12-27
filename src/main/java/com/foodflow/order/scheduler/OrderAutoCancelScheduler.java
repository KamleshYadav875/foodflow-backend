package com.foodflow.order.scheduler;

import com.foodflow.order.entity.Order;
import com.foodflow.order.enums.CancelReason;
import com.foodflow.order.enums.OrderStatus;
import com.foodflow.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAutoCancelScheduler {

    @Value("${order.auto-cancel.created-minutes}")
    private long cancelAfterMinutes;

    private final OrderRepository orderRepository;

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void autoCancelUnacceptedOrders(){
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(cancelAfterMinutes);
        List<Order> expiredOrders = orderRepository.findExpiredOrders(
                        OrderStatus.CREATED,
                        expiryTime
                );

        if (expiredOrders.isEmpty()) {
            return;
        }

        for (Order order : expiredOrders) {
            try {
                order.setStatus(OrderStatus.CANCELLED);
                order.setCancelReason(CancelReason.SYSTEM_TIMEOUT);
                order.setCancelledAt(LocalDateTime.now());
            } catch (Exception e) {
                log.error(
                        "Failed to auto-cancel order {}",
                        order.getId(),
                        e
                );
            }
        }

        orderRepository.saveAll(expiredOrders);

        log.info("Auto-cancelled {} orders", expiredOrders.size());
    }
}
