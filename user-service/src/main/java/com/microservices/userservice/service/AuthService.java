package com.microservices.userservice.service;

import com.microservices.userservice.dto.UserDto;
import com.microservices.userservice.exception.InvalidCredentialsException;
import com.microservices.userservice.exception.UserAlreadyExistsException;
import com.microservices.userservice.exception.UserNotFoundException;
import com.microservices.userservice.mapper.UserMapper;
import com.microservices.userservice.model.Session;
import com.microservices.userservice.model.SessionStatus;
import com.microservices.userservice.model.User;
import com.microservices.userservice.repository.RoleRepository;
import com.microservices.userservice.repository.SessionRepository;
import com.microservices.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    public AuthService(UserRepository userRepository, RoleRepository roleRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserDto> login(String email, String password)
    {
        //Get the user details from the DB
        Optional<User> userOptional = userRepository.findByEmail(email);

        //Check if user exists
        if (userOptional.isEmpty()){
            throw new UserNotFoundException("User for the given email Id does not exists. please sign-up to continue");
        }

        User user = userOptional.get();
        //Check if the credentials match
        if(!bCryptPasswordEncoder.matches(password, user.getPassword()))
        {
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        // Token generation
        String token = RandomStringUtils.randomAlphanumeric(30);

        //First check if they already have a session and then cancel it
        Optional<List<Session>> checkSession = sessionRepository.findByUserIdAndSessionStatus(user.getId(), SessionStatus.ACTIVE);
        if(!checkSession.isEmpty()){
            checkSession.get().forEach(session -> session.setSessionStatus(SessionStatus.ENDED));
        }
        log.info("Token session ended");

        //Session Creation
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        //Generating the response
        UserDto userDto = UserMapper.userToDtoMapper(user);

        //Setting up the headers
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);
        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);

    }

    public ResponseEntity<UserDto> signUp(String email, String password){
        // check if the email already exists
        Optional<User> checkUser = userRepository.findByEmail(email);
        if(!checkUser.isEmpty()){
            throw new UserAlreadyExistsException("User already exists with this email, please login");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);

        return new ResponseEntity<>(UserMapper.userToDtoMapper(user),HttpStatus.OK);
    }

    public ResponseEntity<List<Session>> getAllSessions()
    {
         return new ResponseEntity<>(sessionRepository.findAll(), HttpStatus.OK);
    }

}
