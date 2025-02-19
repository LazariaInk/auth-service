package com.lazar.auth_service.controller;

import com.lazar.auth_service.model.RefreshToken;
import com.lazar.auth_service.model.User;
import com.lazar.auth_service.service.OAuth2UserService;
import com.lazar.auth_service.service.RefreshTokenService;
import com.lazar.auth_service.util.CustomJwtUtil;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2LoginController {

    private final CustomJwtUtil jwtUtil;
    private final OAuth2UserService oAuth2UserService;
    private final RefreshTokenService refreshTokenService;

    public OAuth2LoginController(CustomJwtUtil jwtUtil, OAuth2UserService oAuth2UserService, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.oAuth2UserService = oAuth2UserService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/login/success")
    public Map<String, String> getJwtAfterGoogleLogin(OAuth2AuthenticationToken authToken) {
        if (authToken == null) {
            throw new RuntimeException("Authentication token is null");
        }
        String email = authToken.getPrincipal().getAttribute("email");
        if (email == null) {
            throw new RuntimeException("Email not found in OAuth2 authentication response");
        }
        User user = oAuth2UserService.processOAuth2User(authToken.getPrincipal());
        String accessToken = jwtUtil.generateAccessToken(email);
        RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(user);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken.getToken());
        return tokens;
    }
}
