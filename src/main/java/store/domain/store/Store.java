package store.domain.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {

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

    private String generateKey(String name, Promotion promotion) {
        String promotionName = "NO_PROMOTION";
        if (promotion != null) {
            promotionName = promotion.getName();
        }
        return name + "_" + promotionName;
    }
}
