package store.repository;

import store.domain.Product;
import store.domain.Promotion;

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
                Promotion promotion = parsePromotion(line);
                promotions.put(promotion.getName(), promotion);
            }
        }
        return promotions;
    }

    private Promotion parsePromotion(String line) {
        String[] data = line.split(",");
        String name = data[0];
        int buy = Integer.parseInt(data[1]);
        int get = Integer.parseInt(data[2]);
        String startDate = data[3];
        String endDate = data[4];

        return new Promotion(name, buy, get, startDate, endDate);
    }

    public List<Product> loadProducts(String filePath, Map<String, Promotion> promotions) throws IOException{
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                Product product = parseProduct(line, promotions);
                products.add(product);
            }
        }
        return products;
    }

    private Product parseProduct(String line, Map<String, Promotion> promotions) {
        String[] data = line.split(",");
        String name = data[0];
        int price = Integer.parseInt(data[1]);
        int quantity = Integer.parseInt(data[2]);
        String promotionName = data[3];

        Promotion promotion = promotions.getOrDefault(promotionName, null);
        return new Product(name, price, quantity, promotion);
    }
}
