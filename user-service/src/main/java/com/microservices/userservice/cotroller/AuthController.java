package com.microservices.userservice.cotroller;

import com.microservices.userservice.dto.LoginRequestDto;
import com.microservices.userservice.dto.SignUpRequestDto;
import com.microservices.userservice.dto.UserDto;
import com.microservices.userservice.dto.ValidateTokenRequest;
import com.microservices.userservice.model.Session;
import com.microservices.userservice.model.SessionStatus;
import com.microservices.userservice.repository.SessionRepository;
import com.microservices.userservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final SessionRepository sessionRepository;
    private final AuthService authService;

    public AuthController(AuthService authService,
                          SessionRepository sessionRepository) {
        this.authService = authService;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request)
    {
        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword()).getBody();
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request)
    {

        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping(path = "/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequest request) throws UnsupportedEncodingException {
        SessionStatus status = authService.validate(request.getToken(), request.getUserId());

        return new ResponseEntity<>(status, HttpStatus.OK);
    }


        @PostMapping(path = "/logout/{id}")
    public ResponseEntity<Void> logout(@PathVariable("id") Long userId, @RequestHeader("token") String token){
        String response = authService.logout(token, userId);
        log.info(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/sessions")
    public ResponseEntity<List<Session>> getAllSessions(){
        return authService.getAllSessions();
    }

    @PostMapping(path = "/user")
    public Long getUser(String token){
       Long userId = authService.getUser(token).getBody().getId();
       return userId;
    }


}
