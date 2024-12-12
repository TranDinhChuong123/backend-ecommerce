package com.shop.ecommerce.services;

import com.shop.ecommerce.requests.AuthenticationRequest;
import com.shop.ecommerce.requests.RegisterRequest;
import com.shop.ecommerce.responses.AuthenticationResponse;
import com.shop.ecommerce.requests.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    boolean authenticateWithoutToken(LoginRequest request);

    boolean resetPassword(String email, String newPassword);

    AuthenticationResponse login(LoginRequest request);
    AuthenticationResponse loginWithProvider(LoginRequest request);

    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) throws IOException;
    boolean ExistsByEmail(String email);
}
