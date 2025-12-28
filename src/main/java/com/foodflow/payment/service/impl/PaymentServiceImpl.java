package com.foodflow.payment.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.order.entity.Order;
import com.foodflow.order.service.OrderCommandService;
import com.foodflow.order.service.OrderQueryService;
import com.foodflow.payment.dto.PaymentLinkResponseDto;
import com.foodflow.payment.entity.Payment;
import com.foodflow.payment.enums.PaymentStatus;
import com.foodflow.payment.repository.PaymentRepository;
import com.foodflow.payment.service.PaymentService;
import com.foodflow.payment.webhook.dto.RazorpayWebhookEvent;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    @Value("${razorpay.api.key}")
    private String razorpayKey;

    @Value("${razorpay.api.secret}")
    private String razorpaySecret;

    @Override
    @Transactional
    public PaymentLinkResponseDto createPaymentLink(Long orderId) {

        Order order = orderQueryService.getOrderById(orderId);

        Payment payment = Payment.builder()
                .orderId(order.getId())
                .amount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);
        try {
            RazorpayClient client =
                    new RazorpayClient(razorpayKey, razorpaySecret);

            JSONObject request = new JSONObject();
            request.put("amount", order.getTotalAmount().multiply(BigDecimal.valueOf(100)));
            request.put("currency", "INR");
            request.put("reference_id", order.getId().toString());
            request.put("description", "FoodFlow Order #" + order.getId());

            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getName());
            customer.put("contact", order.getUser().getPhone());
            request.put("customer", customer);

            request.put("callback_method", "get");

            PaymentLink link = client.paymentLink.create(request);
            payment.setGatewayOrderId(link.get("id"));
            paymentRepository.save(payment);

            return new PaymentLinkResponseDto(
                    link.get("id"),
                    link.get("short_url")
            );
        }
        catch (RazorpayException ex){
            throw new BadRequestException(ex.toString());
        }



    }

    @Override
    public void handlePaymentSuccess(RazorpayWebhookEvent event) {

        if (!event.isPaymentLinkPaid()) return;

        Payment payment = paymentRepository
                .findByGatewayOrderId(event.getPaymentLinkId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) return;

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(event.getPaymentId());
        paymentRepository.save(payment);

        orderCommandService.updateOrderStatusAfterPayment(payment.getOrderId());
    }

}

