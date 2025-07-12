package com.app.MyIBC.Authentification.controller;

import com.app.MyIBC.Authentification.config.JwtUtils;
import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/success")
    public ResponseEntity<?> success(Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non authentifié");
        }

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Vérifier si le user existe déjà
        User existingUser = userRepository.findByEmail(email);
        Boolean isNew = false;
        if (existingUser == null) {
            isNew = true;
            Participant p = new Participant(); // NE PAS oublier new
            p.setEmail(email);
            p.setUsername(name);
            p.setRole(Role.ROLE_PARTICIPANT);
            participantRepository.save(p);
        }

        // Générer un token (si besoin)
        String token = jwtUtils.generateToken(name); // ou email selon ton JWT config

        return ResponseEntity.ok(Map.of(
                "email", email,
                "name", name,
                "token", token,
                "role", String.valueOf(existingUser.getRole()),
                "isNew", isNew
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpServletResponse response) {
        // Vérification d'unicité
        if (userRepository.findByUsername(user.getUsername()) != null || userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Username or Email is already in use");
        }

        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Créer un Participant
        Participant participant = new Participant();
        participant.setUsername(user.getUsername());
        participant.setEmail(user.getEmail());
        participant.setPassword(user.getPassword());
        participant.setRole(Role.valueOf("ROLE_PARTICIPANT")); // tu peux adapter selon ton besoin

        participantRepository.save(participant);

        // Générer le token
        String token = jwtUtils.generateToken(participant.getUsername());

        // Cookies sécurisés
        ResponseCookie tokenCookie = ResponseCookie.from("token", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();

        ResponseCookie roleCookie = ResponseCookie.from("role", String.valueOf(participant.getRole()))
                .path("/")
                .httpOnly(false)
                .secure(true)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, roleCookie.toString());

        Map<String, Object> authData = new HashMap<>();
        authData.put("token", token);
        authData.put("type", "Bearer");
        authData.put("role", participant.getRole());

        return ResponseEntity.ok(authData);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        try {
            User u = userRepository.findByEmail(user.getEmail());
            if (u == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(u.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String jwtToken = jwtUtils.generateToken(u.getUsername());

                ResponseCookie tokenCookie = ResponseCookie.from("token", jwtToken)
                        .path("/")
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("Strict")
                        .build();

                ResponseCookie roleCookie = ResponseCookie.from("role", String.valueOf(u.getRole()))
                        .path("/")
                        .httpOnly(false)
                        .secure(true)
                        .sameSite("Strict")
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, roleCookie.toString());

                Map<String, Object> authData = new HashMap<>();
                authData.put("token", jwtToken);
                authData.put("type", "Bearer ");
                authData.put("role", u.getRole());

                if (u instanceof Participant) {
                    Participant p = (Participant) u;
                    if (p.getQrCode() == true) {
                        authData.put("badge", "true");
                    }
                }

                return ResponseEntity.ok(authData);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (AuthenticationException e) {
            log.error("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password: " + e.getMessage());
        }
    }
}
