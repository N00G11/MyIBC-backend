package com.app.MyIBC.Authentification.controller;

import com.app.MyIBC.Authentification.config.JwtUtils;
import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.Authentification.service.UserService;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionDesUtilisateur.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UtilisateurService utilisateurService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Vérifier si le username existe déjà
            if (userRepository.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Le nom d'utilisateur '" + user.getUsername() + "' est déjà utilisé.");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Utilisateur newUser = utilisateurService.saveUtilisateur(user);

            return ResponseEntity.ok(
                    buildAuthResponse(
                            newUser.getUsername(),
                            String.valueOf(newUser.getRole()),
                            newUser.getCode()
                    )
            );
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur pendant l'enregistrement : " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User u = userRepository.findByEmail(user.getEmail());

            if (u == null) {
                return unauthorized("Email ou mot de passe invalide.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(u.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok(buildAuthResponse(u.getUsername(), String.valueOf(u.getRole()), u.getCode()));
            }

            return unauthorized("Email ou mot de passe invalide.");

        } catch (AuthenticationException e) {
            log.warn("Échec de l'authentification : {}", e.getMessage());
            return unauthorized("Email ou mot de passe invalide.");
        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'authentification : {}", e.getMessage());
            return serverError("Erreur interne : " + e.getMessage());
        }
    }

    @PostMapping("/login/{code}")
    public ResponseEntity<?> loginByCode(@PathVariable String code) {
        try {
            User u = userService.getUserByCode(code);

            if (u == null || u.getUsername() == null) {
                return unauthorized("Code invalide.");
            }

            return ResponseEntity.ok(buildAuthResponse(u.getUsername(), String.valueOf(u.getRole()), u.getCode()));

        } catch (Exception e) {
            log.error("Erreur lors de l'authentification avec code : {}", e.getMessage());
            return serverError("Erreur serveur : " + e.getMessage());
        }
    }

    // ---------- Méthodes utilitaires ----------

    private Map<String, Object> buildAuthResponse(String username, String role, String code) {
        Map<String, Object> authData = new HashMap<>();
        authData.put("token", jwtUtils.generateToken(username));
        authData.put("type", "Bearer");
        authData.put("role", role);
        if (code != null) authData.put("code", code);
        return authData;
    }

    private ResponseEntity<String> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    private ResponseEntity<String> serverError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
