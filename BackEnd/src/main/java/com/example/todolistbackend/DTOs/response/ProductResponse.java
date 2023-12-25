package com.example.todolistbackend.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    int productId;
    String productName;
    int productAmount;
    String productUnit;
    LocalDateTime createDate;
    LocalDateTime updateDate;
    String createdBy;
    String updatedBy;

    public List getData(){
        return List.of(
                productId,
                productName,
                productAmount,
                productUnit,
                createDate,
                updateDate,
                createdBy,
                updatedBy
        );
    }
}
