package com.quan.controller;

import com.quan.model.User;
import com.quan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String jwt) {

        User user = userService.findUserByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @PostMapping("/profile/update")
    public ResponseEntity<User> updateProfile(@RequestHeader("Authorization") String jwt,
                                              @RequestBody User req) {

        User user = userService.updateUser(req);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

}
