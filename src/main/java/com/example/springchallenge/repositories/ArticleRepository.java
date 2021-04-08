package com.example.springchallenge.repositories;

import com.example.springchallenge.dtos.ArticleDTO;

import java.util.List;

public interface ArticleRepository {

    List<ArticleDTO> getArticleList();

    ArticleDTO getArticleById(Long id);

    void subtractStock(Long id, Integer quantity);

    void updateDatabase();
}