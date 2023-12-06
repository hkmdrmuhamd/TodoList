package com.example.todolistbackend.DTOs.request;

import lombok.Data;

@Data
public class LoginRequest {

    String userName;
    String password;

}
