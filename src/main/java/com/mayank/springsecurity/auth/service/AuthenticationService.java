package com.mayank.springsecurity.auth.service;

import com.mayank.springsecurity.auth.dto.RegisterRequest;
import com.mayank.springsecurity.auth.dto.AuthenticationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    void register(RegisterRequest request, HttpServletResponse response);

    void authenticate(AuthenticationRequest request, HttpServletResponse response);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
