package com.mayank.springsecurity.auth.controller;

import com.mayank.springsecurity.auth.dto.AuthenticationRequest;
import com.mayank.springsecurity.auth.service.AuthenticationService;
import com.mayank.springsecurity.auth.dto.RegisterRequest;
import com.mayank.springsecurity.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(value = "register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest request, HttpServletResponse response) {
        service.register(request, response);

    }

    @PostMapping("authenticate")
    @ResponseStatus(HttpStatus.CREATED)
    public void authenticate(@RequestBody @Valid AuthenticationRequest request, HttpServletResponse response) {
        service.authenticate(request, response);
    }

    @PostMapping("refresh-token")
    @ResponseStatus(HttpStatus.CREATED)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        service.refreshToken(request, response);
    }

}
