package com.example.springchallenge.services;

import com.example.springchallenge.dtos.ArticleDTO;
import com.example.springchallenge.dtos.PurchaseRequestDTO;
import com.example.springchallenge.dtos.PurchaseResponseDTO;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    List<ArticleDTO> getUnfilteredArticles();

    List<ArticleDTO> getArticles(Map<String, String> allFilters) throws Exception;

    PurchaseResponseDTO PurchaseArticles(PurchaseRequestDTO articles) throws Exception;
}
