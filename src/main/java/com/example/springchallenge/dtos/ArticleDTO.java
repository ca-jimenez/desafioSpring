package com.example.springchallenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    private Long productId;
    private String name;
    private String category;
    private String brand;
    private Integer price;
    private Integer quantity;
    private Boolean freeShipping;
    //    private String prestige;
    private Integer prestige;
}
