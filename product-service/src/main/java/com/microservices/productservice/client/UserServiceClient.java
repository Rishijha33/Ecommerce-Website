package com.microservices.productservice.client;


import com.microservices.productservice.dto.ValidateTokenDto;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {
    private RestTemplateBuilder restTemplateBuilder;


    private String userServiceUrl;



    private String userServiceValidateUrl;

    public UserServiceClient(RestTemplateBuilder restTemplateBuilder){
        this.restTemplateBuilder = restTemplateBuilder;
    }

    public String validateToken(String token){
        ValidateTokenDto validateTokenDto = new ValidateTokenDto();
        validateTokenDto.setToken(token);
        String validateTokenUrl = userServiceUrl+ userServiceValidateUrl;
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> validateResponse = restTemplate.postForEntity(validateTokenUrl, validateTokenDto, String.class);
        return validateResponse.getBody();
    }




}
