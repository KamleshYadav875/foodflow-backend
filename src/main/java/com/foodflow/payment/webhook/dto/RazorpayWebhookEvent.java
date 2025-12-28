package com.foodflow.payment.webhook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RazorpayWebhookEvent {

    private String event;
    private Payload payload;

    public boolean isPaymentLinkPaid() {
        return "payment_link.paid".equals(event);
    }

    public boolean isPaymentFailed() {
        return "payment.failed".equals(event);
    }

    public boolean isPaymentLinkExpired() {
        return "payment_link.expired".equals(event);
    }

    public String getPaymentLinkId() {
        if (payload != null
                && payload.paymentLink != null
                && payload.paymentLink.entity != null) {
            return payload.paymentLink.entity.id;
        }
        return null;
    }

    public String getPaymentId() {
        if (payload != null
                && payload.payment != null
                && payload.payment.entity != null) {
            return payload.payment.entity.id;
        }
        return null;
    }

    // ===================== NESTED CLASSES =====================

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {

        @JsonProperty("payment_link")
        private PaymentLink paymentLink;

        private Payment payment;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentLink {

        private Entity entity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payment {

        private Entity entity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entity {

        private String id;
    }
}
