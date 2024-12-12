package com.shop.ecommerce.services;

import com.shop.ecommerce.requests.GoogleUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleService {

    private static final String GOOGLE_TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";

    public GoogleUser verifyGoogleIdToken(String idToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GOOGLE_TOKEN_INFO_URL + "?id_token=" + idToken;

        try {
            ResponseEntity<GoogleUser> response = restTemplate.getForEntity(url, GoogleUser.class);
            return response.getBody();
        } catch (Exception e) {
            return null; // Token không hợp lệ
        }
    }
}
