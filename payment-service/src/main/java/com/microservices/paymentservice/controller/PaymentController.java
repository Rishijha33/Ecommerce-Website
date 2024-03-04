package com.microservices.paymentservice.controller;

import com.microservices.paymentservice.dto.InitiatePaymentDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping("/")
    public String initiatePayments(@RequestBody InitiatePaymentDto paymentDto){

        return null;
    }
}
