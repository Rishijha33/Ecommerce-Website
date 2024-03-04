package com.microservices.paymentservice.paymentgateway;

public class RazorpayPaymentGateway implements PaymentGateway{
    @Override
    public String generatePaymentLink(Long orderId, Long amount) {
        return null;
    }
}
