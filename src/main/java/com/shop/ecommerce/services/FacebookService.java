package com.shop.ecommerce.services;

import com.shop.ecommerce.requests.FacebookUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookService {

    private static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/me";

    public FacebookUser verifyFacebookAccessToken(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = FACEBOOK_GRAPH_API_URL + "?fields=id,name,email&access_token=" + accessToken;

        try {
            ResponseEntity<FacebookUser> response = restTemplate.getForEntity(url, FacebookUser.class);
            return response.getBody();
        } catch (Exception e) {
            return null; // Token không hợp lệ
        }
    }
}
