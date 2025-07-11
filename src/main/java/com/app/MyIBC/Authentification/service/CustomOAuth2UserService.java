package com.app.MyIBC.Authentification.service;

import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        return new DefaultOAuth2UserService().loadUser(request);
    }
}
