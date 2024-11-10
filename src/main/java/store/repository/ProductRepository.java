package store.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.store.Product;
import store.domain.store.Promotion;
import store.parser.FileParser;

public class ProductRepository {

    private final List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public Map<String, Promotion> loadPromotions(String filePath) throws IOException {
        Map<String, Promotion> promotions = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                Promotion promotion = FileParser.parsePromotion(line);
                promotions.put(promotion.getName(), promotion);
            }
        }
        return promotions;
    }

    public void loadProducts(String filePath, Map<String, Promotion> promotions) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                Product product = FileParser.parseProduct(line, promotions);
                products.add(product);
            }
        }
    }

}
