package com.example.todolistbackend.DTOs.request;

import lombok.Data;

@Data
public class CreateProductRequest {

        private String productName;
        private int productAmount;
        private String productUnit;
}
