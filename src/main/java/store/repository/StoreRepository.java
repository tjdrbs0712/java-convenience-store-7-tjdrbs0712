package store.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.store.Product;
import store.domain.store.Promotion;

public class StoreRepository {

    public static final String PROMOTION_NAME = "_NO_PROMOTION";

    private final Map<String, Product> products = new HashMap<>();

    public Map<String, Product> getProducts() {
        return products;
    }

    public void StoreProductUpdate(List<Product> products) {
        for (Product product : products) {
            String key = generateKey(product.getName(), product.getPromotion());
            this.products.put(key, product);
        }
    }

    public Product findProduct(String name, Promotion promotion) {
        String key = generateKey(name, promotion);
        return products.get(key);
    }

    public List<Product> findProducts(String name) {
        return products.entrySet().stream()
                .filter(entry -> entry.getKey().contains(name))
                .map(Map.Entry::getValue)
                .toList();
    }

    private String generateKey(String name, Promotion promotion) {
        String promotionName = PROMOTION_NAME;
        if (promotion != null) {
            promotionName = promotion.getName();
        }
        return name + promotionName;
    }
}
