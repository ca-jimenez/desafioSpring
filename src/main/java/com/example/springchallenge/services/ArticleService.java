package com.example.springchallenge.services;

import com.example.springchallenge.dtos.ArticleDTO;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    List<ArticleDTO> getUnfilteredArticles();
    List<ArticleDTO> getArticles(Map<String, String> allFilters, String name, String category, String brand, Integer price, Boolean freeShipping, String prestige) throws Exception;
}
