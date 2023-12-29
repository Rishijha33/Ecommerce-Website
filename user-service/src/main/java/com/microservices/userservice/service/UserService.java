package com.microservices.userservice.service;

import com.microservices.userservice.dto.UserDto;
import com.microservices.userservice.mapper.UserMapper;
import com.microservices.userservice.model.Role;
import com.microservices.userservice.model.User;
import com.microservices.userservice.repository.RoleRepository;
import com.microservices.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User createUser(User user)
    {
//        User user = UserMapper.dtoToUserMapper(user);
        userRepository.save(user);
        return user;

    }

    public UserDto getUserDetails(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){return null;}

        return UserDto.from(optionalUser.get());
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        if(optionalUser.isEmpty()){return null;}
        User user = optionalUser.get();
        user.setRoles(Set.copyOf(roles));

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }

    public List<UserDto> getAllUsers(){
        List<User> users= userRepository.findAll();
        List<UserDto> dtos = users.stream().map(user -> {
            return UserMapper.userToDtoMapper(user);
        }).toList();

        return dtos;
    }
}
