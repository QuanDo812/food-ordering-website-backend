package com.quan.response;

import com.quan.enums.ROLE_USER;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthReponse {

    private String jwt;
    private String message;
    private ROLE_USER role;

}
