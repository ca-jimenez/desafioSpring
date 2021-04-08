package com.example.springchallenge.services;

import com.example.springchallenge.dtos.ArticleDTO;
import com.example.springchallenge.dtos.PurchaseRequestDTO;
import com.example.springchallenge.dtos.PurchaseResponseDTO;

import java.util.List;
import java.util.Map;

public interface MarketService {

    List<ArticleDTO> getUnfilteredArticles();

    List<ArticleDTO> getArticles(Map<String, String> allFilters) throws Exception;

    PurchaseResponseDTO PurchaseArticles(Long cartId, PurchaseRequestDTO articles) throws Exception;
}
