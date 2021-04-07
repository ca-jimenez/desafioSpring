package com.example.springchallenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseArticleDTO {

    private Long productId;
    private String name;
    private String brand;
    private Integer quantity;
}
