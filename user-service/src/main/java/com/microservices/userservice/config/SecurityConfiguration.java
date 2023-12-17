package com.microservices.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration  {

    @Order(1)   // We can set that which filter has the highest priority
    @Bean
    public SecurityFilterChain filteringCriteria(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().disable();
        httpSecurity.csrf().disable();
//        httpSecurity.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/auth/*").permitAll());      //sets a pattern for the pattern to permit the requests
//        httpSecurity.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/order/*").permitAll());
        httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()); //permit any request that is coming into the app
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}