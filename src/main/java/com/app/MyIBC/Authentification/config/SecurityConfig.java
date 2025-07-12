package com.app.MyIBC.Authentification.config;

import com.app.MyIBC.Authentification.filter.JwtFilter;
import com.app.MyIBC.Authentification.config.handler.CustomOAuth2SuccessHandler;
import com.app.MyIBC.Authentification.service.CustomOAuth2UserService;
import com.app.MyIBC.Authentification.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.web.filter.ForwardedHeaderFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    // 🔐 Handler personnalisé pour rediriger avec JWT vers le frontend
    @Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler() {
        // URL de redirection vers le frontend Next.js avec token
        String redirectUri = "https://my-ibc-frontend-8imw2guov-n00g11s-projects.vercel.app/auth/callback";
        return new CustomOAuth2SuccessHandler(jwtUtils, redirectUri);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/success").permitAll()
                            .requestMatchers("/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**").permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2Login(withDefaults())
                .addFilterBefore(new JwtFilter(userDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new CustomOAuth2UserService();
    }

    // ✅ Essentiel pour Railway et autres proxys
    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new ForwardedHeaderFilter());
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegBean;
    }
}
