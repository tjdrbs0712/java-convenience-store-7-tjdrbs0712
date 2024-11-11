package store.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.store.Product;
import store.domain.store.Promotion;
import store.parser.FileParser;
import store.util.DateUtil;

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
                addPromotion(line, promotions);
            }
        }
        return promotions;
    }

    public void addPromotion(String line, Map<String, Promotion> promotions) {
        Promotion promotion = FileParser.parsePromotion(line);

        if (DateUtil.isPromotionActive(promotion)) {
            promotions.put(promotion.getName(), promotion);
        }
    }


    public void loadProducts(String filePath, Map<String, Promotion> promotions) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                addProduct(line, promotions);
            }
        }
    }

    private void addProduct(String line, Map<String, Promotion> promotions) {
        Product product = FileParser.parseProduct(line, promotions);
        if (product != null) {
            products.add(product);
        }
    }

    public List<Product> findProductsByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .sorted(Comparator.comparing(product -> product.getPromotion() == null))
                .toList();
    }
}