package com.example.springchallenge.controllers;

import com.example.springchallenge.dtos.ArticleDTO;
import com.example.springchallenge.dtos.PurchaseRequestDTO;
import com.example.springchallenge.dtos.PurchaseResponseDTO;
import com.example.springchallenge.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ArticleController {

    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

//    @GetMapping("/articles")
//    public ResponseEntity<List<ArticleDTO>> getArticles(
//            @RequestParam Map<String, String> allFilters,
//            @RequestParam(required = false, name = "name") String name,
//            @RequestParam(required = false, name = "category") String category,
//            @RequestParam(required = false, name = "brand") String brand,
//            @RequestParam(required = false, name = "price") Integer price,
//            @RequestParam(required = false, name = "freeShipping") Boolean freeShipping,
//            @RequestParam(required = false, name = "prestige") Integer prestige) throws Exception {
//
//            return new ResponseEntity<>(articleService.getArticles(allFilters, name, category, brand, price, freeShipping, prestige), HttpStatus.OK);
//    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getArticles(@RequestParam Map<String, String> allFilters) throws Exception {

        return new ResponseEntity<>(articleService.getArticles(allFilters), HttpStatus.OK);
    }

    @PostMapping("/purchase-request")
    public ResponseEntity<PurchaseResponseDTO> makePurchase(@RequestBody PurchaseRequestDTO articles) throws Exception {

        return new ResponseEntity<>(articleService.PurchaseArticles(articles), HttpStatus.OK);
    }
}