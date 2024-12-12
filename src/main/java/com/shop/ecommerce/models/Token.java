package com.shop.ecommerce.models;

import com.shop.ecommerce.models.enums.Role;
import com.shop.ecommerce.models.enums.TokenType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private String id;

    @Field(name = "access_token")
    private String accessToken;

    @Field(name = "refresh_token")
    private String refreshToken;

    @Field(name = "access_token_type")
    private TokenType tokenType;

    public boolean expired;
    private boolean revoked;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "refresh_tokens_used")
    private List<ArrayList> refreshTokensUsed  = new ArrayList<>();


//    @Field(name = "expiration_date_access_token")
//    private LocalDateTime expirationDateAccessToken; // Trường lưu trữ thời điểm hết hạn của access token.
//
//    @Field(name = "expiration_date_refresh_token")
//    private LocalDateTime expirationDateRefreshToken; // Trường lưu trữ thời điểm hết hạn của refresh token.

}
