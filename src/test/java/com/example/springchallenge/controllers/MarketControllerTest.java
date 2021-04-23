package com.example.springchallenge.controllers;

import com.example.springchallenge.dtos.ArticleDTO;
import com.example.springchallenge.repositories.ArticleRepository;
import com.example.springchallenge.repositories.ArticleRepositoryImpl;
import com.example.springchallenge.services.MarketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MarketController.class)
class MarketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private MarketService marketService;

    @Test
    public void shouldReturnArticles() throws Exception {

        Map<String, String> filters = new HashMap<>();

        List<ArticleDTO> articles = new ArrayList<>();

        articles.add(new ArticleDTO(1L, "remera", "Indumentaria", "Nike", 2300, 10, true, 4));
        articles.add(new ArticleDTO(2L, "short", "Indumentaria", "Adidas", 1500, 20, false, 3));
        articles.add(new ArticleDTO(2L, "medias", "Indumentaria", "Adidas", 500, 20, false, 3));


        when(marketService.getArticles(filters)).thenReturn(articles);
        this.mockMvc.perform(get("/api/v1/articles")).andDo(print()).andExpect(status().isOk())
                .andExpect((content().json(articles.toString())));
    }

    @Test
    public void shouldReturnFilteredArticles() throws Exception {


        Map<String, String> filters = new HashMap<>();
        filters.put("category", "Herramientas");

        List<ArticleDTO> articles = new ArrayList<>();

        ArticleDTO destornillador = new ArticleDTO(1L, "destornillador", "Herramientas", "Black & Decker", 1500, 20, false, 3);
        ArticleDTO martillo = new ArticleDTO(2L, "martillo", "Herramientas", "Black & Decker", 500, 20, false, 3);
        ArticleDTO remera = new ArticleDTO(3L, "remera", "Indumentaria", "Nike", 2300, 10, true, 4);
        ArticleDTO medias = new ArticleDTO(4L, "medias", "Indumentaria", "Adidas", 500, 20, false, 3);

        articles.add(destornillador);
        articles.add(martillo);
        articles.add(remera);
        articles.add(medias);

        List<ArticleDTO> sortedArticles = new ArrayList<>();

        sortedArticles.add(destornillador);
        sortedArticles.add(martillo);

//        when(articleRepository.getArticleList()).thenReturn(articles);
        when(marketService.getArticles(filters)).thenReturn(sortedArticles);
        this.mockMvc.perform(get("/api/v1/articles").param("category", "Herramientas")).andDo(print()).andExpect(status().isOk())
                .andExpect((content().json(sortedArticles.toString())));
    }

}