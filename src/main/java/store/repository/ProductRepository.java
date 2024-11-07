package store.repository;

import store.domain.Product;
import store.domain.Promotion;
import store.util.LoadFileParse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {
    public Map<String, Promotion> loadPromotions(String filePath) throws IOException {
        Map<String, Promotion> promotions = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                Promotion promotion = LoadFileParse.parsePromotion(line);
                promotions.put(promotion.getName(), promotion);
            }
        }
        return promotions;
    }


    public List<Product> loadProducts(String filePath, Map<String, Promotion> promotions) throws IOException{
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                Product product = LoadFileParse.parseProduct(line, promotions);
                products.add(product);
            }
        }
        return products;
    }

}
