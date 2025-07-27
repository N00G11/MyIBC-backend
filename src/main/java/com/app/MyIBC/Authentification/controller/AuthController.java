package com.app.MyIBC.Authentification.controller;

import com.app.MyIBC.Authentification.config.JwtUtils;
import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.Authentification.service.UserService;
import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesUtilisateur.entity.Admin;
import com.app.MyIBC.GestionDesUtilisateur.entity.Tresorier;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionDesUtilisateur.repository.AdminRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.TresorierRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.UtilisateurRepository;
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
    private final UtilisateurRepository utilisateurRepository;
    private final TresorierRepository tresorierRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UtilisateurService utilisateurService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Vérifier si le username existe déjà
            if (userRepository.findByTelephone(user.getTelephone()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Le numero de telephone '" + user.getTelephone() + "' est déjà utilisé.");
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


    @PostMapping("/login/code")
    public ResponseEntity<?> loginByCode(@RequestBody User user) {
        try {
            User u = userService.getUserByCode(user.getCode());

            if (u == null) {
                return unauthorized("code ou mot de passe invalide.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(u.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok(buildAuthResponse(u.getUsername(), String.valueOf(u.getRole()), u.getCode()));
            }

            return unauthorized("code ou mot de passe invalide.");

        } catch (AuthenticationException e) {
            log.warn("Échec de l'authentification : {}", e.getMessage());
            return unauthorized("Email ou mot de passe invalide.");
        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'authentification : {}", e.getMessage());
            return serverError("Erreur interne : " + e.getMessage());
        }
    }


    @PostMapping("/login/telephone")
    public ResponseEntity<?> loginByTelephone(@RequestBody User user) {
        try {
            User u = userService.getUserByTelephone(user.getTelephone());

            if (u == null) {
                return unauthorized("numero de telephone ou mot de passe invalide.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(u.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok(buildAuthResponse(u.getUsername(), String.valueOf(u.getRole()), u.getCode()));
            }

            return unauthorized("numero de telephone ou mot de passe invalide.");

        } catch (AuthenticationException e) {
            log.warn("Échec de l'authentification : {}", e.getMessage());
            return unauthorized("Email ou mot de passe invalide.");
        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'authentification : {}", e.getMessage());
            return serverError("Erreur interne : " + e.getMessage());
        }
    }

    @PostMapping("/verify-identity")
    public ResponseEntity<?> verifyIdentify(@RequestBody User user){
        if (!(userService.getUserByTelephone(user.getTelephone()) == userRepository.findByUsername(user.getUsername()))){
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody User user) {
        User u = userRepository.findByTelephone(user.getTelephone());

        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }

        String newPassword = passwordEncoder.encode(user.getPassword());
        u.setPassword(newPassword);

        if (u instanceof Admin) {
            adminRepository.save((Admin) u);
        } else if (u instanceof Tresorier) {
           tresorierRepository.save((Tresorier) u);
        } else if (u instanceof Utilisateur) {
           utilisateurRepository.save((Utilisateur) u);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Type d'utilisateur non supporté");
        }

        return ResponseEntity.ok(true);
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
