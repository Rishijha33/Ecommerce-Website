package com.microservices.userservice.service;

import com.microservices.userservice.dto.UserDto;
import com.microservices.userservice.model.Role;
import com.microservices.userservice.model.User;
import com.microservices.userservice.repository.RoleRepository;
import com.microservices.userservice.repository.UserRepositorry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepositorry userRepositorry;
    private final RoleRepository roleRepository;

    public UserService(UserRepositorry userRepositorry, RoleRepository roleRepository) {
        this.userRepositorry = userRepositorry;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId){
        Optional<User> optionalUser = userRepositorry.findById(userId);
        if (optionalUser.isEmpty()){return null;}

        return UserDto.from(optionalUser.get());
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds)
    {
        Optional<User> optionalUser = userRepositorry.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        if(optionalUser.isEmpty()){return null;}
        User user = optionalUser.get();
        user.setRoles(Set.copyOf(roles));

        User savedUser = userRepositorry.save(user);

        return UserDto.from(savedUser);
    }
}
