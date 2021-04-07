package com.example.springchallenge.services;

import com.example.springchallenge.dtos.ArticleDTO;
import com.example.springchallenge.exceptions.InvalidFilterException;
import com.example.springchallenge.exceptions.NoMatchesException;
import com.example.springchallenge.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticleDTO> getUnfilteredArticles() {
        return articleRepository.getProductList();
    }

    @Override
    public List<ArticleDTO> getArticles(Map<String, String> allFilters, String name, String category, String brand, Integer price, Boolean freeShipping, String prestige) throws Exception {

        List<ArticleDTO> catalog = getUnfilteredArticles();

//        if (name != null
//                || category != null
//                || brand != null
//                || price != null
//                || freeShipping != null
//                || prestige != null) {
        if (allFilters.size() > 0 && allFilters.size() < 3) {

            validateFilters(allFilters);
            return filterArticles(catalog, name, category, brand, price, freeShipping, prestige);

        } else if (allFilters.size() > 2) {

            throw new InvalidFilterException("A maximum of 2 filters can be applied at the same time");
        } else {
            return catalog;
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
                    && !key.equals("prestige")) {

                throw new InvalidFilterException("Accepted filters are: name, category, price, brand, freeShipping and prestige");
            }
        }

    }

    private List<ArticleDTO> filterArticles(List<ArticleDTO> catalog, String name, String category, String brand, Integer price, Boolean freeShipping, String prestige) throws NoMatchesException {

//        int activeFilters = 0;
//
//        ArrayList<Object> filters = new ArrayList<>();
//        filters.add(name);
//        filters.add(category);
//        filters.add(brand);
//        filters.add(price);
//        filters.add(freeShipping);
//        filters.add(prestige);
//
//        for (Object filter : filters) {
//            if (filter != null) {
//                activeFilters++;
//
//                if (activeFilters > 2) {
//
//                }
//            }
//        }

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


        if (catalog.size() < 1) {
            throw new NoMatchesException("No articles match the query");
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

    private List<ArticleDTO> filterByPrestige(List<ArticleDTO> catalog, String filter) {

        return catalog.stream().filter(item -> item.getPrestige()
                .equalsIgnoreCase(filter))
                .collect(Collectors.toList());
    }

}
