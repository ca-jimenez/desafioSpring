package com.example.springchallenge.repositories;

import com.example.springchallenge.dtos.ArticleDTO;

import java.util.List;

public interface ArticleRepository {

    List<ArticleDTO> getProductList();
}
