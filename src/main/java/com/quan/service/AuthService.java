package com.quan.service;

public interface AuthService {

    public void sendOtp(String email);

    public boolean validateOtp(String email, String otp);

    public void resetPassword(String email, String newPassword);

}
