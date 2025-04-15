package com.quan.service.impl;

import com.quan.config.JwtProvider;
import com.quan.model.User;
import com.quan.repository.UserRepository;
import com.quan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByJwt(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(User req) {
        User user = userRepository.findByEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setGender(req.getGender());
        user.setPhone(req.getPhone());
        user.setDob(req.getDob());
        return userRepository.save(user);
    }
}
