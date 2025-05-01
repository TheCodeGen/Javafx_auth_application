package com.example.odyssey.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignupRequest {
    public String username;
    public String email;
    public String password;
}
