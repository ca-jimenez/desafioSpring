package com.example.springchallenge.services;

import com.example.springchallenge.dtos.*;
import com.example.springchallenge.exceptions.InvalidFilterException;
import com.example.springchallenge.exceptions.NoMatchesException;
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
    //public List<ArticleDTO> getArticles(Map<String, String> allFilters, String name, String category, String brand, Integer price, Boolean freeShipping, Integer prestige) throws Exception {
    public List<ArticleDTO> getArticles(Map<String, String> allFilters) throws Exception {

        List<ArticleDTO> catalog = getUnfilteredArticles();

//        if (name != null
//                || category != null
//                || brand != null
//                || price != null
//                || freeShipping != null
//                || prestige != null) {
        if (allFilters.size() > 0 && allFilters.size() < 3) {

            validateFilters(allFilters);
//            return filterArticles(catalog, name, category, brand, price, freeShipping, prestige);
            return filterArticles(catalog, allFilters);

        } else if (allFilters.size() > 2) {

            throw new InvalidFilterException("A maximum of 2 filters can be applied at the same time");
        } else {
            return catalog;
        }
    }

    @Override
    public PurchaseResponseDTO PurchaseArticles(PurchaseRequestDTO articles) {

        List<PurchaseArticleDTO> articleList = articles.getArticles();

        long total = 0L;

        //ToDo validate received product data
        //ToDo article not found
        for (PurchaseArticleDTO article : articleList) {

            ArticleDTO itemInStock = articleRepository.getArticleById(article.getProductId());

            if (article.getQuantity() > itemInStock.getQuantity()) {
                //ToDo not enough stock
            }

            total += (article.getQuantity() * (long) itemInStock.getPrice());
        }

        TicketDTO ticket = new TicketDTO(ticketIdCounter.getAndIncrement(), articleList, total);
        StatusDTO status = new StatusDTO(200, "La solicitud de compra se completó con éxito");

        return new PurchaseResponseDTO(ticket, status);
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
                    throw new InvalidFilterException("Price, prestige and order filters accept only numeric values");
                }
            }

            if (key.equals("freeShipping")
                    && !filter.getValue().equals("true")
                    && !filter.getValue().equals("false")) {

                throw new InvalidFilterException("FreeShipping must be boolean");
            }


        }

    }

    //    private List<ArticleDTO> filterArticles(List<ArticleDTO> catalog, String name, String category, String brand, Integer price, Boolean freeShipping, String prestige) throws NoMatchesException {
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


//        if (catalog.size() < 1) {
//            throw new NoMatchesException("No articles match the query");
//        }

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

//    private List<ArticleDTO> filterByPrestige(List<ArticleDTO> catalog, String filter) {
//
//        return catalog.stream().filter(item -> item.getPrestige()
//                .equalsIgnoreCase(filter))
//                .collect(Collectors.toList());
//    }

    private List<ArticleDTO> sortArticles(List<ArticleDTO> catalog, Integer order) throws InvalidFilterException {

        List<ArticleDTO> sorted;

        switch (order) {
            case 0:
//                Comparator<String> stringComparatorAsc = String::compareToIgnoreCase;
//                sorted = sortByName(catalog, stringComparatorAsc);
                sorted = catalog.stream().sorted(Comparator.comparing(ArticleDTO::getName)).collect(Collectors.toList());
                break;
            case 1:
//                Comparator<String> stringComparatorDesc = (a, b) -> b.compareToIgnoreCase(a);
//                sorted = sortByName(catalog, stringComparatorDesc);
                sorted = catalog.stream().sorted((a, b) -> b.getName().compareToIgnoreCase(a.getName())).collect(Collectors.toList());
                break;
            case 2:
//                Comparator<Integer> intComparatorDesc = (a, b) -> b - a;
//                sorted = sortByPrice(catalog, intComparatorDesc);
                sorted = catalog.stream().sorted((a, b) -> b.getPrice() - a.getPrice()).collect(Collectors.toList());
                break;
            case 3:
//                Comparator<Integer> intComparatorAsc = Comparator.comparingInt(a -> a);
//                sorted = sortByPrice(catalog, intComparatorAsc);
                sorted = catalog.stream().sorted(Comparator.comparingInt(ArticleDTO::getPrice)).collect(Collectors.toList());
                break;
            default:
                throw new InvalidFilterException("Order filter accepted values: 0-3");

        }
        return sorted;
    }
}
