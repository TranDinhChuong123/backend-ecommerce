//package com.shop.ecommerce.configs;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//
//  @Value("${application.security.jwt.secret-key}")
//  private String secretKey;
//  @Value("${application.security.jwt.expiration}")
//  private long jwtExpiration;
//  @Value("${application.security.jwt.refresh-token.expiration}")
//  private long refreshExpiration;
//
//  public String extractUsername(String token) {
//    return extractClaim(token, Claims::getSubject);
//  }
//
//  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//    final Claims claims = extractAllClaims(token);
//    return claimsResolver.apply(claims);
//  }
//
//  public String generateToken(UserDetails userDetails) {
//    return generateToken(new HashMap<>(), userDetails);
//  }
//
//  public String generateToken(
//      Map<String, Object> extraClaims,
//      UserDetails userDetails
//  ) {
//    return buildToken(extraClaims, userDetails, jwtExpiration);
//  }
//
//  public String generateRefreshToken(
//      UserDetails userDetails
//  ) {
//    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
//  }
//
//  private String buildToken(
//          Map<String, Object> extraClaims,
//          UserDetails userDetails,
//          long expiration
//  ) {
//    return Jwts
//            .builder()
//            .setClaims(extraClaims)
//            .setSubject(userDetails.getUsername())
//            .setIssuedAt(new Date(System.currentTimeMillis()))
//            .setExpiration(new Date(System.currentTimeMillis() + expiration))
//            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//            .compact();
//  }
//
//  public boolean isTokenValid(String token, UserDetails userDetails) {
//    final String username = extractUsername(token);
//    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//  }
//
//  private boolean isTokenExpired(String token) {
//    return extractExpiration(token).before(new Date());
//  }
//
//  private Date extractExpiration(String token) {
//    return extractClaim(token, Claims::getExpiration);
//  }
//
//  private Claims extractAllClaims(String token) {
//    return Jwts
//        .parserBuilder()
//        .setSigningKey(getSignInKey())
//        .build()
//        .parseClaimsJws(token)
//        .getBody();
//  }
//
//  private Key getSignInKey() {
//    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//    return Keys.hmacShaKeyFor(keyBytes);
//  }
//}

package com.shop.ecommerce.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  // Đọc giá trị secret key từ file cấu hình
  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  // Đọc thời gian hết hạn của access token từ file cấu hình
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;



  // Đọc thời gian hết hạn của refresh token từ file cấu hình
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  // Trích xuất tên người dùng từ token
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Trích xuất các claim từ token theo một hàm xử lý cụ thể
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }


  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  // Tạo access token từ thông tin người dùng
  public String generateAccessToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }
  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
    return Jwts
            .builder()
            .setClaims(extraClaims) // Thêm các claim bổ sung vào token
            .setSubject(userDetails.getUsername()) // Đặt tên người dùng là subject của token
            .claim("role", userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).findFirst().orElse("CUSTOMER"))
            .setIssuedAt(new Date(System.currentTimeMillis())) // Đặt thời gian phát hành token
            .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Đặt thời gian hết hạn của token
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký token với secret key
            .compact(); // Hoàn thành và trả về token dưới dạng chuỗi
  }

  // Kiểm tra xem token có hợp lệ không
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token); // Trích xuất tên người dùng từ token
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); // Kiểm tra tính hợp lệ của token
  }

  // Kiểm tra xem token đã hết hạn chưa
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date()); // So sánh thời gian hết hạn với thời gian hiện tại
  }

  // Trích xuất thời gian hết hạn từ token
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration); // Trích xuất claim thời gian hết hạn
  }

  // Trích xuất tất cả các claim từ token
  private Claims extractAllClaims(String token) {
    return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey()) // Đặt secret key để xác thực token
            .build()
            .parseClaimsJws(token) // Phân tích và trích xuất claims từ token
            .getBody(); // Trả về body của token chứa các claims
  }

  // Lấy secret key từ cấu hình và chuyển đổi nó thành Key đối tượng
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Giải mã secret key từ base64
    return Keys.hmacShaKeyFor(keyBytes); // Tạo đối tượng Key từ secret key
  }
}

