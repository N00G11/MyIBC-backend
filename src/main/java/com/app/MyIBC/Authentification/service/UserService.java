package com.app.MyIBC.Authentification.service;


import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByCode(String code){
        List<User> users = userRepository.findAll();

        for(User u:users){
            if (code.equals(u.getCode())){
                return u;
            }
        }
        return null;
    }

    public User getUserByTelephone(String telephone){
        List<User> users = userRepository.findAll();

        for(User u:users){
            if (telephone.equals(u.getTelephone())){
                return u;
            }
        }
        return null;
    }
}
