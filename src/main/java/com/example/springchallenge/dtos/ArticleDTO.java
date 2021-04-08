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
    private Integer prestige;


    public void subtractQuantity(Integer quantity) {
        this.quantity -= quantity;
    }

    private String formatPrice() {
        return "$" + String.format("%,d", price).replace(",", ".");
    }

    private String formatShipping() {
        return freeShipping? "SI" : "NO";
    }

    private String formatPrestige() {

        String result = "";
        for (int i = 0; i < prestige; i++) {
            result += "*";
        }
        return result;
    }

    public String toCsvRow() {
        return String.join(",", name, category, brand, formatPrice(), quantity.toString(), formatShipping(), formatPrestige());
    }
}
