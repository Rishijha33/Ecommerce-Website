package com.microservices.userservice.service;

import com.microservices.userservice.dto.UserDto;
import com.microservices.userservice.exception.InvalidCredentialsException;
import com.microservices.userservice.exception.InvalidTokenException;
import com.microservices.userservice.exception.UserAlreadyExistsException;
import com.microservices.userservice.exception.UserNotFoundException;
import com.microservices.userservice.mapper.UserMapper;
import com.microservices.userservice.model.Session;
import com.microservices.userservice.model.SessionStatus;
import com.microservices.userservice.model.User;
import com.microservices.userservice.repository.RoleRepository;
import com.microservices.userservice.repository.SessionRepository;
import com.microservices.userservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
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

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

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
        //String token = RandomStringUtils.randomAlphanumeric(30);
        MacAlgorithm alg = Jwts.SIG.HS256;  // HS256 is a hashing algorithm for jwt
        SecretKey secretKey =  alg.key().build(); //Generating the secret key

        // Start adding the claims
        Map<String, Object> jsonForJWT = new HashMap<>();
        jsonForJWT.put("userId", user.getId());
        jsonForJWT.put("roles", user.getRoles());
        jsonForJWT.put("createdAt", new Date());
        jsonForJWT.put("expiryAt" ,new Date(LocalDate.now().plusDays(3).toEpochDay()));

        // Create the token
        String token = Jwts.builder()
                .claims(jsonForJWT)                  // Adding the claims to the token
                .signWith(secretKey, alg)            //  Sending the key and algo
                .compact();                         //  Finishing the builder methd
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

    public SessionStatus validate(String token, Long userId) throws UnsupportedEncodingException {

        // check if the token is expired or not
        String[] chunks = token.split("\\.");
        String b64payload = chunks[1];
        String jsonString = new String(Base64.getDecoder().decode(b64payload), "UTF-8");
        log.info(jsonString);

        // Check if the session is there in the db and is active
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty() || sessionOptional.get().getSessionStatus() == SessionStatus.ENDED){
            throw new InvalidTokenException("The token in invalid");
        }

        return SessionStatus.ACTIVE;
    }

    public String logout(String token, Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty() || sessionOptional.get().getSessionStatus() == SessionStatus.ENDED){
            throw new InvalidTokenException("The token in invalid");
        }

        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);

        return "Session ended";
    }

    public ResponseEntity<List<Session>> getAllSessions()
    {
         return new ResponseEntity<>(sessionRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<User> getUser(String token){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndSessionStatus(token, SessionStatus.ACTIVE);
        if(sessionOptional.isEmpty()){
            throw new RuntimeException("Session not present");
        }

        User user = sessionOptional.get().getUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }




}
