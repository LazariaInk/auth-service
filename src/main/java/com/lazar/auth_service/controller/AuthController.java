package com.lazar.auth_service.controller;

import com.lazar.auth_service.model.RefreshToken;
import com.lazar.auth_service.model.User;
import com.lazar.auth_service.service.OAuth2UserService;
import com.lazar.auth_service.service.RefreshTokenService;
import com.lazar.auth_service.service.UserService;
import com.lazar.auth_service.util.CustomJwtUtil;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomJwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public AuthController(CustomJwtUtil jwtUtil, RefreshTokenService refreshTokenService, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email) {
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String accessToken = jwtUtil.generateAccessToken(email);
            //RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            //return "Access Token: " + accessToken + "\nRefresh Token: " + refreshToken.getToken();
            return "";
        }

        throw new RuntimeException("User not found");
    }

    @PostMapping("/refresh")
    public String refreshAccessToken(@RequestParam String refreshToken) {
        Optional<RefreshToken> tokenOpt = refreshTokenService.findByToken(refreshToken);

        if (tokenOpt.isPresent() && jwtUtil.validateRefreshToken(refreshToken)) {
            return jwtUtil.generateAccessToken(tokenOpt.get().getUser().getEmail());
        } else {
            throw new RuntimeException("Invalid or expired refresh token");
        }
    }

}
