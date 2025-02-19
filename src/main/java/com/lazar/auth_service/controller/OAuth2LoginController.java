package com.lazar.auth_service.controller;

import com.lazar.auth_service.model.User;
import com.lazar.auth_service.service.OAuth2UserService;
import com.lazar.auth_service.util.CustomJwtUtil;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OAuth2LoginController {

    private final CustomJwtUtil jwtUtil;
    private final OAuth2UserService oAuth2UserService;

    public OAuth2LoginController(CustomJwtUtil jwtUtil, OAuth2UserService oAuth2UserService) {
        this.jwtUtil = jwtUtil;
        this.oAuth2UserService = oAuth2UserService;
    }

    @GetMapping("/login/success")
    public String getJwtAfterGoogleLogin(OAuth2AuthenticationToken authToken) {
        if (authToken == null) {
            throw new RuntimeException("Authentication token is null");
        }

        // 1. Extragem email-ul utilizatorului
        String email = authToken.getPrincipal().getAttribute("email");
        if (email == null) {
            throw new RuntimeException("Email not found in OAuth2 authentication response");
        }

        // 2. Salvăm utilizatorul în baza de date dacă nu există
        User user = oAuth2UserService.processOAuth2User(authToken.getPrincipal());

        // 3. Generăm un JWT pentru utilizator
        String accessToken = jwtUtil.generateAccessToken(email);

        return "Access Token: " + accessToken;
    }
}
