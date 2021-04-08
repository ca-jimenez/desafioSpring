package com.example.springchallenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {

    private Long cartId;
    private Long total;
    private List<PurchaseArticleDTO> contents;

    public void addItems(List<PurchaseArticleDTO> articles, Long price) {
        contents.addAll(articles);
        total += price;
    }
}
