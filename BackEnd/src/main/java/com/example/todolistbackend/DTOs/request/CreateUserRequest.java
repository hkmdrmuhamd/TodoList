package com.example.todolistbackend.DTOs.request;

import lombok.Data;



@Data
public class CreateUserRequest {

    private String userName;
    private String password;
    private String role;
}
