package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Token;
import com.shop.ecommerce.models.User;

import java.util.List;

public interface TokenService {

    void revokeAllUserTokens(User user);
    void saveUserToken(User user, String accessToken,String refreshToken);

    List<Token> findAllValidTokenByUser(String userId);

    List<Token> saveAll(List<Token> listToken);

}
