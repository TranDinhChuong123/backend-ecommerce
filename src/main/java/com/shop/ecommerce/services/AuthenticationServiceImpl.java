package com.shop.ecommerce.services;

import com.shop.ecommerce.configs.JwtService;
import com.shop.ecommerce.exception.NotFoundException;
import com.shop.ecommerce.models.*;
import com.shop.ecommerce.repositories.UserRepository;
import com.shop.ecommerce.requests.*;
import com.shop.ecommerce.responses.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> existingUserOptional = userRepository.findUserByUsername(request.getUsername());
        if (existingUserOptional.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        // Đăng ký User
        var user = User.builder()
                .name(request.getName())
                .image(request.getImage())
                .email(request.getEmail())
                .username(request.getUsername())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .authProvider(request.getAuthProvider())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.saveUserToken(savedUser, accessToken, refreshToken);
        // Phương thức này cần có khả năng xử lý cả User và Shop
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean resetPassword(String email, String newPassword) {
        // Tìm user theo email
        User user = userRepository.findUserByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Mã hóa mật khẩu mới và lưu
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            if (!authentication.isAuthenticated()) {
                logger.error("Authentication failed for user: {}", request.getUsername());
                throw new BadCredentialsException("Authentication failed");
            }


        User foundUser = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        tokenService.revokeAllUserTokens(foundUser);
        var accessToken = jwtService.generateAccessToken(foundUser); // Tạo token cho người dùng
        var refreshToken = jwtService.generateRefreshToken(foundUser); // Tạo refresh token cho người dùng

        // Lưu token của người dùng
        tokenService.saveUserToken(foundUser, accessToken, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();


    }

    @Override
    public AuthenticationResponse loginWithProvider(LoginRequest request) {

        User foundUser = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        tokenService.revokeAllUserTokens(foundUser);
        var accessToken = jwtService.generateAccessToken(foundUser); // Tạo token cho người dùng
        var refreshToken = jwtService.generateRefreshToken(foundUser); // Tạo refresh token cho người dùng

        // Lưu token của người dùng
        tokenService.saveUserToken(foundUser, accessToken, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


//    @Override
//    public AuthenticationResponse register(RegisterRequest request) {
//        if (request.getAuthProvider() != null
//                && (request.getAuthProvider() == AuthProvider.GOOGLE
//                || request.getAuthProvider() == AuthProvider.FACEBOOK
//        )) {
//            Optional<User> existingUserOptional = userRepository.findUserByEmail(request.getEmail());
//
//            if (existingUserOptional.isPresent()) {
//                throw new RuntimeException("User already exists with this email");
//            }
//
//            var user = User.builder()
//                    .name(request.getName())
//                    .image(request.getImage())
//                    .email(request.getEmail())
//                    .username(request.getUsername())  // Có thể dùng email hoặc username (nếu muốn)
//                    .role(request.getRole())
//                    .authProvider(request.getAuthProvider())  // Đặt provider là Google hoặc Facebook
//                    .createdAt(LocalDateTime.now())
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//            User savedUser = userRepository.save(user);
//            String accessToken = jwtService.generateAccessToken(user);
//            String refreshToken = jwtService.generateRefreshToken(user);
//            tokenService.saveUserToken(savedUser, accessToken, refreshToken);
//
//            return AuthenticationResponse.builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshToken)
//                    .build();
//        }
//
//        // Nếu là đăng ký bằng username/password
//        Optional<User> existingUserOptional = userRepository.findUserByUsername(request.getUsername());
//        if (existingUserOptional.isPresent()) {
//            throw new RuntimeException("User already exists with this username");
//        }
//
//        // Đăng ký User với username và password
//        var user = User.builder()
//                .name(request.getName())
//                .image(request.getImage())
//                .email(request.getEmail())
//                .username(request.getUsername())
//                .role(request.getRole())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .authProvider(AuthProvider.EMAIL)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        User savedUser = userRepository.save(user);
//        String accessToken = jwtService.generateAccessToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//        tokenService.saveUserToken(savedUser, accessToken, refreshToken);
//
//        return AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//
//
//    @Override
//    public AuthenticationResponse login(LoginRequest request) {
//        if (request.isFacebookLogin()) {
//            return handleFacebookLogin(request.getFacebookAccessToken());
//        } else if (request.isGoogleLogin()) {
//            return handleGoogleLogin(request.getGoogleIdToken());
//        } else {
//            return handleUsernamePasswordLogin(request);
//        }
//    }
//
//    private AuthenticationResponse handleFacebookLogin(String facebookAccessToken) {
//        FacebookUser facebookUser = facebookService.verifyFacebookAccessToken(facebookAccessToken);
//
//        if (facebookUser == null) {
//            throw new BadCredentialsException("Invalid Facebook token");
//        }
//
//        User user = userRepository.findUserByEmail(facebookUser.getEmail())
//                .orElseGet(() -> createUserFromFacebookData(facebookUser));
//
//        return generateAuthenticationResponse(user);
//    }
//
//    private AuthenticationResponse handleGoogleLogin(String googleIdToken) {
//        GoogleUser googleUser = googleService.verifyGoogleIdToken(googleIdToken);
//
//        if (googleUser == null) {
//            logger.error("Invalid Google ID token: {}", googleIdToken);
//            throw new BadCredentialsException("Invalid Google token");
//        }
//
//        User user = userRepository.findUserByEmail(googleUser.getEmail())
//                .orElseGet(() -> createUserFromGoogleData(googleUser));
//
//        return generateAuthenticationResponse(user);
//    }
//
//    private AuthenticationResponse handleUsernamePasswordLogin(LoginRequest request) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//
//        if (!authentication.isAuthenticated()) {
//            logger.error("Authentication failed for user: {}", request.getUsername());
//            throw new BadCredentialsException("Authentication failed");
//        }
//
//        User foundUser = userRepository.findUserByUsername(request.getUsername())
//                .orElseThrow(() -> new NotFoundException("User not found"));
//
//        return generateAuthenticationResponse(foundUser);
//    }
//
//    private AuthenticationResponse generateAuthenticationResponse(User user) {
//        tokenService.revokeAllUserTokens(user);
//        String accessToken = jwtService.generateAccessToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//
//        tokenService.saveUserToken(user, accessToken, refreshToken);
//        return AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    private User createUserFromGoogleData(GoogleUser googleUser) {
//        User newUser = new User();
//        newUser.setEmail(googleUser.getEmail());
//        newUser.setName(googleUser.getName());
//        newUser.setUsername(googleUser.getId());
//        newUser.setRole(Role.CUSTOMER);
//        newUser.setImage(googleUser.getImage());
//        userRepository.save(newUser);
//        return newUser;
//    }



    @Override
    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userUsername;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid refresh token");
        }
        refreshToken = authHeader.substring(7);
        userUsername = jwtService.extractUsername(refreshToken);
        if (userUsername != null) {
            var user = this.userRepository.findUserByUsername(userUsername)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateAccessToken(user);
                tokenService.revokeAllUserTokens(user);
                tokenService.saveUserToken(user, accessToken, refreshToken);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

            }

        }
        throw new RuntimeException("Invalid token");
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return jwtService.isTokenValid(token, userDetails);
    }

    @Override
    public boolean ExistsByEmail(String email) {
        return userService.userExistsByEmail(email);
    }

    @Override
    public boolean authenticateWithoutToken(LoginRequest request) {
        try {
            // Thực hiện xác thực với AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Invalid username or password");
            }

            userRepository.findUserByUsername(request.getUsername())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            return  true;
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}


//        authenticationManager chuyển đối tượng token đến authenticationProvider.
//        Nếu thành công, trả về một Authentication object.
//        Cập Nhật SecurityContext: UsernamePasswordAuthenticationFilter
//        nhận kết quả từ authenticationManager và thiết lập thông tin xác thực vào
//        SecurityContext thông qua SecurityContextHolder.
