package com.app.MyIBC.Authentification.config.handler;

import com.app.MyIBC.Authentification.config.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final String redirectUri;

    public CustomOAuth2SuccessHandler(JwtUtils jwtUtils, String redirectUri) {
        this.jwtUtils = jwtUtils;
        this.redirectUri = redirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 1. Récupérer l'utilisateur authentifié via Google
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // 2. Extraire les infos nécessaires (email par exemple)
        String email = oauthUser.getAttribute("email");

        // 3. Générer le token JWT
        String token = jwtUtils.generateToken(email);

        // 4. Construire l'URL de redirection avec le token en paramètre
        String redirectWithToken = redirectUri + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        // 5. Rediriger vers le frontend
    }
}
