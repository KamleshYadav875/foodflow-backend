package com.foodflow.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentLinkResponseDto {

    private String paymentId;
    private String paymentUrl;
}
