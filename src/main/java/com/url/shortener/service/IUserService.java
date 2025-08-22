package com.url.shortener.service;

import com.url.shortener.dto.request.LoginRequest;
import com.url.shortener.model.User;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;

public interface IUserService {
    User register(User user);
    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
    User findByUsername(String username);
}
