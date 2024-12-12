package com.shop.ecommerce.controllers;

import com.shop.ecommerce.requests.EmailRequest;
import com.shop.ecommerce.requests.RegisterRequest;
import com.shop.ecommerce.responses.ApiResponse;
import com.shop.ecommerce.responses.AuthenticationResponse;
import com.shop.ecommerce.requests.LoginRequest;
import com.shop.ecommerce.services.AuthenticationService;
import com.shop.ecommerce.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    private final AuthenticationService service;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Registration successful",
                service.register(request)
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Login successful",    // Message
                service.login(request)           // Data
        ));
    }

    @PostMapping("/login-provider")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> loginWithProvider(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Login successful",
                service.loginWithProvider(request)
        ));
    }




    @PostMapping("/userExists")
    public ResponseEntity<ApiResponse<Boolean>> userExistsByEmail(
            @RequestBody EmailRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Email existence check successful",
                service.ExistsByEmail(request.getEmail())
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            HttpServletRequest request
    ) throws IOException {
        return ResponseEntity.ok(new ApiResponse<>(
                "Refresh token successful",
                service.refreshToken(request)
        ));

    }
    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse<Boolean>> verifyCode(
            @RequestParam String email,
            @RequestParam String code
    ) {
            return ResponseEntity.ok(new ApiResponse<>(
                    emailService.verifyCode(email, code)
                            ? 200
                            : 400,
                    emailService.verifyCode(email, code)
                            ? "Verification successful"
                            : "Verification failed",
                    emailService.verifyCode(email, code)
            ));


    }


    @PostMapping("/send-verification-code/{email}")
    public ApiResponse<Boolean> sendVerificationCode(@PathVariable String email) {
        boolean isSent = emailService.sendVerificationEmail(email);
        return new ApiResponse<>(
                "Verification successful",
                isSent
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Boolean>> forgotPassword(@RequestBody EmailRequest request) {
        boolean isSent = emailService.sendResetPasswordEmail(request.getEmail());
        if (isSent) {
            return ResponseEntity.ok(new ApiResponse<>(
                    "Password reset email sent successfully",
                    true
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    "Failed to send password reset email",
                    false
            ));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword
    ) {
            boolean isReset = service.resetPassword(email, newPassword);
            if (isReset) {
                return ResponseEntity.ok(new ApiResponse<>(
                        "Password reset successful",
                        true
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                        "Failed to reset password",
                        false
                ));
            }

    }

    @PostMapping("/verify-password")
    public ResponseEntity<ApiResponse<Boolean>> authenticateWithoutToken(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "authenticateWithoutToken successful",
                service.authenticateWithoutToken(request)
        ));
    }

    @PostMapping("/validate-email")
    public ResponseEntity<ApiResponse<Boolean>> ExistsByEmail(
            @RequestBody EmailRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "ExistsByEmail",
                service.ExistsByEmail(request.getEmail())
        ));
    }






}
