package com.quan.service;

import com.quan.model.User;

public interface UserService {

    User findUserByEmail(String email);
    User findUserByJwt(String jwt);

    User updateUser(User req);

}
