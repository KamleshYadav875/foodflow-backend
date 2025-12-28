package com.foodflow.payment.controller;

import com.foodflow.payment.service.PaymentService;
import com.foodflow.payment.webhook.dto.RazorpayWebhookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Void> handle(@RequestBody RazorpayWebhookEvent event) {
        paymentService.handlePaymentSuccess(event);
        return ResponseEntity.ok().build();
    }
}
