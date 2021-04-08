package com.example.springchallenge.services;

import com.example.springchallenge.dtos.*;
import com.example.springchallenge.exceptions.InsufficientStockException;
import com.example.springchallenge.exceptions.InvalidArticleException;
import com.example.springchallenge.exceptions.InvalidFilterException;
import com.example.springchallenge.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;

    private AtomicLong ticketIdCounter = new AtomicLong(1);

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticleDTO> getUnfilteredArticles() {
        return articleRepository.getArticleList();
    }

    @Override
    public List<ArticleDTO> getArticles(Map<String, String> allFilters) throws Exception {

        List<ArticleDTO> catalog = getUnfilteredArticles();

        if (allFilters.size() < 1) {

            return catalog;

        } else if (allFilters.size() <= 2
                || (allFilters.size() == 3 && allFilters.get("order") != null)) {

            validateFilters(allFilters);

            return filterArticles(catalog, allFilters);

        } else {
            throw new InvalidFilterException("A maximum of 2 filters can be applied at the same time");
        }
    }

    @Override
    public PurchaseResponseDTO PurchaseArticles(PurchaseRequestDTO articles) throws Exception {

        List<PurchaseArticleDTO> articleList = articles.getArticles();

        long total = 0L;

        for (PurchaseArticleDTO article : articleList) {

            if (article.getQuantity() < 1) {
                throw new InvalidArticleException("Quantity for article with product id " + article.getProductId() + " is invalid");
            }

            ArticleDTO itemInStock = articleRepository.getArticleById(article.getProductId());

            if (itemInStock == null) {
                throw new InvalidArticleException("Article with product id " + article.getProductId() + " is invalid");
            }

            validateArticle(article, itemInStock);

            if (article.getQuantity() > itemInStock.getQuantity()) {
                throw new InsufficientStockException("Stock available for article with product id " + itemInStock.getProductId() + " is " + itemInStock.getQuantity());
            }

            total += (article.getQuantity() * (long) itemInStock.getPrice());
        }

        for (PurchaseArticleDTO article : articleList) {
            articleRepository.subtractStock(article.getProductId(), article.getQuantity());
        }

        articleRepository.updateDatabase();

        TicketDTO ticket = new TicketDTO(ticketIdCounter.getAndIncrement(), articleList, total);
        StatusDTO status = new StatusDTO(200, "La solicitud de compra se completó con éxito");

        return new PurchaseResponseDTO(ticket, status);
    }

    private void validateArticle(PurchaseArticleDTO reqArticle, ArticleDTO catalogArticle) throws InvalidArticleException {

        if (!reqArticle.getName().equals(catalogArticle.getName())
                || !reqArticle.getBrand().equals(catalogArticle.getBrand())) {
            throw new InvalidArticleException("Invalid article data. Product id: " + reqArticle.getProductId());
        }
    }

    private void validateFilters(Map<String, String> filterList) throws Exception {

        for (Map.Entry<String, String> filter : filterList.entrySet()) {

            String key = filter.getKey();

            if (!key.equals("name")
                    && !key.equals("category")
                    && !key.equals("price")
                    && !key.equals("brand")
                    && !key.equals("freeShipping")
                    && !key.equals("prestige")
                    && !key.equals("order")) {

                throw new InvalidFilterException("Accepted filters are: name, category, price, brand, freeShipping, prestige and order");
            }

            if (key.equals("price") || key.equals("prestige") || key.equals("order")) {
                try {
                    Integer.parseInt(filter.getValue());

                } catch (NumberFormatException e) {
                    throw new InvalidFilterException(key + " filter accepts only numeric values");
                }
            }

            if (key.equals("freeShipping")
                    && !filter.getValue().equals("true")
                    && !filter.getValue().equals("false")) {

                throw new InvalidFilterException("FreeShipping must be boolean");
            }
        }
    }

    private List<ArticleDTO> filterArticles(List<ArticleDTO> catalog, Map<String, String> filters) throws Exception {

        String name = filters.get("name");
        String category = filters.get("category");
        String brand = filters.get("brand");
        Integer price = filters.get("price") != null ? Integer.parseInt(filters.get("price")) : null;
        Boolean freeShipping = filters.get("freeShipping") != null ? filters.get("freeShipping").equals("true") : null;
        Integer prestige = filters.get("prestige") != null ? Integer.parseInt(filters.get("prestige")) : null;
        Integer order = filters.get("order") != null ? Integer.parseInt(filters.get("order")) : null;

        if (name != null) {
            catalog = filterByName(catalog, name);
        }

        if (category != null) {
            catalog = filterByCategory(catalog, category);
        }

        if (brand != null) {
            catalog = filterByBrand(catalog, brand);
        }

        if (price != null) {
            catalog = filterByPrice(catalog, price);
        }

        if (freeShipping != null) {
            catalog = filterByShipping(catalog, freeShipping);
        }

        if (prestige != null) {
            catalog = filterByPrestige(catalog, prestige);
        }

        if (order != null) {
            catalog = sortArticles(catalog, order);
        }

        return catalog;
    }

    private List<ArticleDTO> filterByName(List<ArticleDTO> catalog, String filter) {

        return catalog.stream().filter(item -> item.getName()
                .equalsIgnoreCase(filter))
                .collect(Collectors.toList());
    }

    private List<ArticleDTO> filterByCategory(List<ArticleDTO> catalog, String filter) {

        return catalog.stream().filter(item -> item.getCategory()
                .equalsIgnoreCase(filter))
                .collect(Collectors.toList());
    }

    private List<ArticleDTO> filterByBrand(List<ArticleDTO> catalog, String filter) {

        return catalog.stream().filter(item -> item.getBrand()
                .equalsIgnoreCase(filter))
                .collect(Collectors.toList());
    }

    private List<ArticleDTO> filterByPrice(List<ArticleDTO> catalog, Integer filter) {

        return catalog.stream().filter(item -> item.getPrice()
                .equals(filter))
                .collect(Collectors.toList());
    }

    private List<ArticleDTO> filterByShipping(List<ArticleDTO> catalog, Boolean filter) {

        return catalog.stream().filter(item -> item.getFreeShipping()
                .equals(filter))
                .collect(Collectors.toList());
    }

    private List<ArticleDTO> filterByPrestige(List<ArticleDTO> catalog, Integer filter) {

        return catalog.stream().filter(item -> item.getPrestige()
                .equals(filter))
                .collect(Collectors.toList());
    }

    private List<ArticleDTO> sortArticles(List<ArticleDTO> catalog, Integer order) throws InvalidFilterException {

        List<ArticleDTO> sorted;

        switch (order) {
            case 0:
                sorted = catalog.stream().sorted(Comparator.comparing(ArticleDTO::getName)).collect(Collectors.toList());
                break;
            case 1:
                sorted = catalog.stream().sorted((a, b) -> b.getName().compareToIgnoreCase(a.getName())).collect(Collectors.toList());
                break;
            case 2:
                sorted = catalog.stream().sorted((a, b) -> b.getPrice() - a.getPrice()).collect(Collectors.toList());
                break;
            case 3:
                sorted = catalog.stream().sorted(Comparator.comparingInt(ArticleDTO::getPrice)).collect(Collectors.toList());
                break;
            default:
                throw new InvalidFilterException("Order filter accepted values: 0-3");
        }
        return sorted;
    }
}
