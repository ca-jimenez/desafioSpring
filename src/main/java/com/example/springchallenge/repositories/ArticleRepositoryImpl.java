package com.example.springchallenge.repositories;

import com.example.springchallenge.dtos.ArticleDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository {

    private List<ArticleDTO> catalog;

    private AtomicLong idCounter = new AtomicLong(1);

    public ArticleRepositoryImpl() {
        catalog = loadDatabase();
    }

    @Override
    public List<ArticleDTO> getArticleList() {
        return catalog;
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
       return catalog.stream().filter(a -> a.getProductId().equals(id)).findFirst().orElse(null);
    }

    private List<ArticleDTO> loadDatabase() {

        List<ArticleDTO> records = new ArrayList<>();

        try {

            Resource resource = new ClassPathResource("dbProductos.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String row;

            reader.readLine();

            while ((row = reader.readLine()) != null) {
                String[] data = row.split(",");

                String name = data[0];
                String category = data[1];
                String brand = data[2];
                Integer price = Integer.parseInt(data[3]
                        .replace("$", "")
                        .replace(".", ""));

                Integer quantity = Integer.parseInt(data[4]);
                Boolean freeShipping = data[5].equals("SI");
//                String prestige = data[6];
                Integer prestige = data[6].length();

                records.add(new ArticleDTO(idCounter.getAndIncrement(), name, category, brand, price, quantity, freeShipping, prestige));

            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}
