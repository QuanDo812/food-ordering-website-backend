package com.quan.controller;

import com.quan.config.JwtProvider;
import com.quan.enums.ROLE_USER;
import com.quan.model.Cart;
import com.quan.model.User;
import com.quan.repository.CartRepository;
import com.quan.repository.UserRepository;
import com.quan.request.LoginRequest;
import com.quan.request.OtpRequest;
import com.quan.response.AuthReponse;
import com.quan.service.AuthService;
import com.quan.service.CustomerUserDetailsService;
import com.quan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;


    @Autowired
    private CustomerUserDetailsService customUserDetails;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<AuthReponse> createUserHandler(@RequestBody User user) throws Exception{

        User isExistedUser = userRepository.findByEmail(user.getEmail());
        if(isExistedUser != null){
            log.warn("user already exist: ", isExistedUser);
            throw new Exception("email is already used in another user");
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setFullName(user.getFullName());
        newUser.setRole(user.getRole());
        newUser.setPhone(user.getPhone());
        newUser.setDob(user.getDob());
        newUser.setGender(user.getGender());

        User savedUser = userRepository.save(newUser);

        Cart newCart = new Cart();
        newCart.setCustomer(savedUser);
        cartRepository.save(newCart);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtProvider.generateToken(auth);

        AuthReponse authReponse = new AuthReponse();
        authReponse.setJwt(jwt);
        authReponse.setMessage("Register successfully!");
        authReponse.setRole(savedUser.getRole());

        return new ResponseEntity<>(authReponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthReponse> signInHandler(@RequestBody LoginRequest loginRequest) throws Exception{

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication auth = authenticate(username, password);
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(auth);

        AuthReponse authReponse = new AuthReponse();
        authReponse.setJwt(jwt);
        authReponse.setMessage("Login successfully!");

        return new ResponseEntity<>(authReponse, HttpStatus.OK);

    }



    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwt(jwt);
        String email = user.getEmail();
        authService.sendOtp(email);
        return ResponseEntity.ok("OTP đã được gửi.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestHeader("Authorization") String jwt,@RequestBody OtpRequest otp) {
        User user = userService.findUserByJwt(jwt);
        String email = user.getEmail();
        boolean isValid = authService.validateOtp(email, otp.getOtp());
        return isValid ? ResponseEntity.ok("Xác nhận thành công.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP không hợp lệ.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestHeader("Authorization") String jwt, @RequestBody String newPassword) {
        User user = userService.findUserByJwt(jwt);
        String email = user.getEmail();
        authService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Đổi mật khẩu thành công.");
    }


    private Authentication authenticate(String username, String password) throws Exception {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException("Username is incorrect");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Password is incorrect");
        }
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
}
