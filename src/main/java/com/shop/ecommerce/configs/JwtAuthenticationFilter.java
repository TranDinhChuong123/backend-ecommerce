package com.shop.ecommerce.configs;

import com.shop.ecommerce.repositories.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Đánh dấu lớp này là một bean Spring và sẽ được quản lý bởi container Spring.
@RequiredArgsConstructor // Tạo constructor tự động với tất cả các trường được đánh dấu bằng final.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Inject các dependency cần thiết thông qua constructor
    private final JwtService jwtService; // Service để xử lý JWT
    private final UserDetailsService userDetailsService; // Service để lấy thông tin người dùng
    private final TokenRepository tokenRepository; // Repository để lấy thông tin token

    // Override phương thức doFilterInternal từ OncePerRequestFilter
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Kiểm tra nếu yêu cầu gửi đến các endpoint đăng nhập, thì không thực hiện xác thực và cho phép tiếp tục lọc
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy token từ header "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userUserName;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Nếu không có token hoặc token không đúng định dạng, tiếp tục lọc
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7); // bỏ đến vị trí thứ 7
        userUserName = jwtService.extractUsername(jwt);

        // Kiểm tra và xác thực người dùng nếu cần
        if (userUserName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userUserName); // tải thông tin người dùng từ cơ sở dữ liệu bằng cách sử dụng email của người dùng nhận được từ JWT.
            var isTokenValid = tokenRepository.findByAccessToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
//            if (jwtService.isTokenValid(jwt, userDetails)) {
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                // Tạo authentication token và đặt nó vào SecurityContextHolder để xác thực người dùng
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
//                request.setAttribute("username", userUserName);
            }
        }
        // Tiếp tục lọc yêu cầu và chuyển nó đến bộ lọc tiếp theo trong chuỗi bộ lọc
        filterChain.doFilter(request, response);
    }
}
