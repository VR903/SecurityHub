package com.mayank.springsecurity.auth.service;

import com.google.common.cache.LoadingCache;
import com.mayank.springsecurity.auth.dto.AuthenticationRequest;
import com.mayank.springsecurity.auth.dto.RegisterRequest;
import com.mayank.springsecurity.auth.exception.LoginAttemptsException;
import com.mayank.springsecurity.auth.exception.UserAlreadyExistsException;
import com.mayank.springsecurity.config.JwtService;
import com.mayank.springsecurity.token.Token;
import com.mayank.springsecurity.token.TokenRepository;
import com.mayank.springsecurity.token.TokenType;
import com.mayank.springsecurity.user.User;
import com.mayank.springsecurity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoadingCache<String, Integer> loginAttemptCache;

    public void register(RegisterRequest request, HttpServletResponse response) {

        if (repository.findByEmail(request.getEmail()).isPresent())
            throw new UserAlreadyExistsException("Email already registered!");
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = repository.save(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, accessToken);

        response.setHeader(ACCESS_TOKEN, accessToken);
        response.setHeader(REFRESH_TOKEN, refreshToken);
    }

    public void authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Optional<User> optionalUser = repository.findByEmail(request.getEmail());

        if (isBlocked(request.getEmail())) {
            throw new LoginAttemptsException("Blocked due to too many login attempts. Try again later.");
        }

        optionalUser.ifPresent(user -> {
                    String accessToken = jwtService.generateToken(user);
                    String refreshToken = jwtService.generateRefreshToken(user);
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);

                    response.setHeader(ACCESS_TOKEN, accessToken);
                    response.setHeader(REFRESH_TOKEN, refreshToken);
                }
        );
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = this.repository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                response.setHeader(ACCESS_TOKEN, accessToken);
                response.setHeader(REFRESH_TOKEN, refreshToken);
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .accessToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private boolean isBlocked(String username) {
        try {
            int attempts = loginAttemptCache.get(username);
            return attempts >= 3;  // Allow only 3 login attempts within the time window
        } catch (ExecutionException e) {
            return false;
        }
    }
}
