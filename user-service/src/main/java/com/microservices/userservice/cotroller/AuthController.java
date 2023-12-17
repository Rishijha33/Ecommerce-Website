package com.microservices.userservice.cotroller;

import com.microservices.userservice.dto.LoginRequestDto;
import com.microservices.userservice.dto.SignUpRequestDto;
import com.microservices.userservice.dto.UserDto;
import com.microservices.userservice.model.Session;
import com.microservices.userservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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

    @GetMapping(path = "/sessions")
    public ResponseEntity<List<Session>> getAllSessions(){
        return authService.getAllSessions();
    }


}
