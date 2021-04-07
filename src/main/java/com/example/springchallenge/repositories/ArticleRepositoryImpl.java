package com.example.springchallenge.repositories;

import com.example.springchallenge.dtos.ArticleDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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

    @Override
    public void subtractStock(Long id, Integer quantity) {
        catalog.get(id.intValue() - 1).subtractQuantity(quantity);
    }

    @Override
    public void updateDatabase() {

        String recordAsCsv = catalog.stream()
                .map(ArticleDTO::toCsvRow)
                .collect(Collectors.joining(System.getProperty("line.separator")));

        try {
            FileWriter writer = new FileWriter("src/main/resources/dbProductos.csv");

            writer.append("name,category,brand,price,quantity,freeShipping,prestige\n");
            writer.append(recordAsCsv);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
