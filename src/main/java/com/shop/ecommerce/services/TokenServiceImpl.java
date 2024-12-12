package com.shop.ecommerce.services;

import com.shop.ecommerce.models.Token;
import com.shop.ecommerce.models.User;
import com.shop.ecommerce.models.enums.Role;
import com.shop.ecommerce.models.enums.TokenType;
import com.shop.ecommerce.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    private final TokenRepository tokenRepository;
    @Override
    public void saveUserToken(User user, String accessToken,String refreshToken) {
        var userToken = Token.builder()
                .userId(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(userToken);
    }

    @Override
    public List<Token> findAllValidTokenByUser(String userId) {
        return tokenRepository.findAllValidTokenByUser(userId);
    }

    @Override
    public List<Token> saveAll(List<Token> listToken) {
        return tokenRepository.saveAll(listToken);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUsername());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


}
