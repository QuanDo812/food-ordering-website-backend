package com.quan.service.impl;

import com.quan.model.User;
import com.quan.repository.UserRepository;
import com.quan.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final JavaMailSender mailSender;

    public AuthServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        otpStorage.put(email, otp);

        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP xác nhận");
        message.setText("Mã OTP của bạn là: " + otp);
        mailSender.send(message);

    }

    @Override
    public boolean validateOtp(String email, String otp) {
        return otp.equals(otpStorage.get(email));

    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpStorage.remove(email);

    }
}
