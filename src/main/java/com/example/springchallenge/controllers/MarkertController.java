package com.example.springchallenge.controllers;

import com.example.springchallenge.dtos.ArticleDTO;
import com.example.springchallenge.dtos.PurchaseRequestDTO;
import com.example.springchallenge.dtos.PurchaseResponseDTO;
import com.example.springchallenge.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MarkertController {

    private final MarketService marketService;

    @Autowired
    public MarkertController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getArticles(@RequestParam Map<String, String> allFilters) throws Exception {

        return new ResponseEntity<>(marketService.getArticles(allFilters), HttpStatus.OK);
    }

    @PostMapping("/purchase-request")
    public ResponseEntity<PurchaseResponseDTO> makePurchase(
            @RequestParam(value = "shoppingCartId", required = false) Long shoppingCartId,
            @RequestBody PurchaseRequestDTO articles) throws Exception {

        return new ResponseEntity<>(marketService.PurchaseArticles(shoppingCartId, articles), HttpStatus.OK);
    }
}

//ToDo improve validations
