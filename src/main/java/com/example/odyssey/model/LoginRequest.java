package com.example.odyssey.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {
    public String userInfo;
    public String password;
}