package com.app.MyIBC.Authentification.filter;


import com.app.MyIBC.Authentification.config.JwtUtils;
import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        User existingUser = userRepository.findByEmail(email);
        boolean isNew = false;

        if (existingUser == null) {
            Participant p = null;
            p.setEmail(email);
            p.setUsername(name);
            p.setRole(Role.valueOf("ROLE_PARTICIPANT"));
            participantRepository.save(p);
            isNew = true;
        }

        // Génère un token JWT si tu veux
        String token = jwtUtils.generateToken(name);

        // Redirection vers le frontend avec paramètres dans l'URL
        String redirectUrl = isNew
                ? "http://localhost:3000?token=" + token
                : "http://localhost:3000/?token=" + token;

        response.sendRedirect(redirectUrl);
    }
}

