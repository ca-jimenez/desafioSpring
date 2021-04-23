package com.example.springchallenge.services;

import com.example.springchallenge.controllers.MarketController;
import com.example.springchallenge.dtos.ArticleDTO;
import com.example.springchallenge.repositories.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(MarketService.class)
class MarketServiceImplTest {

    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private MarketServiceImpl marketService;

    List<ArticleDTO> articles;

    @BeforeEach
    void setUp() {

        articles = new ArrayList<>();

        ArticleDTO destornillador = new ArticleDTO(1L, "destornillador", "Herramientas", "Black & Decker", 1500, 20, false, 3);
        ArticleDTO martillo = new ArticleDTO(2L, "martillo", "Herramientas", "Black & Decker", 500, 20, true, 3);
        ArticleDTO remera = new ArticleDTO(3L, "remera", "Indumentaria", "Nike", 2300, 10, true, 4);
        ArticleDTO medias = new ArticleDTO(4L, "medias", "Indumentaria", "Adidas", 4000, 20, false, 3);

        when(articleRepository.getArticleList()).thenReturn(articles);
    }

    @Test
    @DisplayName("Get article list from repository")
    void getUnfilteredArticles() {

        List<ArticleDTO> returnedArticles = marketService.getUnfilteredArticles();

        assertEquals(articles, returnedArticles);
    }

    @Test
    @DisplayName("1. Get articles without filters")
    void getArticlesNoFilter() throws Exception {

        Map<String, String> filters = new HashMap<>();

        List<ArticleDTO> returnedArticles = marketService.getArticles(filters);

        assertEquals(articles, returnedArticles);
    }

    @Test
    @DisplayName("2. Get articles with one filter")
    void getArticlesOneFilter() throws Exception {

        Map<String, String> filters = new HashMap<>();
        filters.put("category", "Herramientas");

        List<ArticleDTO> returnedArticles = marketService.getArticles(filters);

        articles = articles.stream().filter(item -> item.getCategory()
                .equalsIgnoreCase(filters.get("category")))
                .collect(Collectors.toList());

        assertEquals(articles, returnedArticles);
    }

    @Test
    @DisplayName("3. Get articles with two filters")
    void getArticlesTwoFilters() throws Exception {

        Map<String, String> filters = new HashMap<>();
        filters.put("category", "Herramientas");
        filters.put("freeShipping", "true");

        List<ArticleDTO> returnedArticles = marketService.getArticles(filters);

        articles = articles.stream().filter(item -> item.getCategory()
                .equalsIgnoreCase(filters.get("category")))
                .filter(item -> item.getFreeShipping()
                .equals(filters.get("freeShipping").equals("true")))
                .collect(Collectors.toList());

        assertEquals(articles, returnedArticles);
    }

    @Test
    @DisplayName("4-a. Get ordered articles: Alphabetic Asc")
    void getArticlesAlpAscOrder() throws Exception {

        Map<String, String> filters = new HashMap<>();
        filters.put("order", "0");

        List<ArticleDTO> returnedArticles = marketService.getArticles(filters);

        articles = articles.stream()
                .sorted(Comparator.comparing(ArticleDTO::getName))
                .collect(Collectors.toList());

        assertEquals(articles, returnedArticles);
    }

    @Test
    @DisplayName("4-b. Get ordered articles: Alphabetic Desc")
    void getArticlesAlpDescOrder() throws Exception {

        Map<String, String> filters = new HashMap<>();
        filters.put("order", "1");

        List<ArticleDTO> returnedArticles = marketService.getArticles(filters);

        articles = articles.stream()
                .sorted((a, b) -> b.getName().compareToIgnoreCase(a.getName()))
                .collect(Collectors.toList());

        assertEquals(articles, returnedArticles);
    }

    @Test
    @DisplayName("5. Get ordered articles: Price Desc")
    void getArticlesPriceDescOrder() throws Exception {

        Map<String, String> filters = new HashMap<>();
        filters.put("order", "2");

        List<ArticleDTO> returnedArticles = marketService.getArticles(filters);

        articles = articles.stream()
                .sorted((a, b) -> b.getPrice() - a.getPrice())
                .collect(Collectors.toList());

        assertEquals(articles, returnedArticles);
    }

    @Test
    @DisplayName("5. Get ordered articles: Price Asc")
    void getArticlesPriceAscOrder() throws Exception {

        Map<String, String> filters = new HashMap<>();
        filters.put("order", "3");

        List<ArticleDTO> returnedArticles = marketService.getArticles(filters);

        articles = articles.stream()
                .sorted(Comparator.comparingInt(ArticleDTO::getPrice))
                .collect(Collectors.toList());

        assertEquals(articles, returnedArticles);
    }

    @Test
    void purchaseArticles() {
    }

}