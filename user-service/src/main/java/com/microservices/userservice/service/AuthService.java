package com.microservices.userservice.service;

import com.microservices.userservice.repository.RoleRepository;
import com.microservices.userservice.repository.UserRepositorry;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepositorry userRepositorry;
    private final RoleRepository roleRepository;

    public AuthService(UserRepositorry userRepositorry, RoleRepository roleRepository) {
        this.userRepositorry = userRepositorry;
        this.roleRepository = roleRepository;
    }

}
