package com.microservices.paymentservice.paymentgateway;

public interface PaymentGateway {
    String generatePaymentLink(Long orderId, Long amount);
}
