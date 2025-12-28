package com.foodflow.payment.service;


import com.foodflow.payment.dto.PaymentLinkResponseDto;
import com.foodflow.payment.webhook.dto.RazorpayWebhookEvent;

public interface PaymentService {

    PaymentLinkResponseDto createPaymentLink(Long orderId);

    void handlePaymentSuccess(RazorpayWebhookEvent event);

//    void handlePaymentSuccess(RazorpayWebhookEvent event);
}

