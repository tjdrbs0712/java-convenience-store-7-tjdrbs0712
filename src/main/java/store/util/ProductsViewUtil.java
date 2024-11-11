package store.util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.StockStatus;
import store.domain.store.Product;
import store.domain.store.Promotion;

public class ProductsViewUtil {

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###");

    public static List<String> ProductsViewFormatter(List<Product> products) {
        LocalDate today = LocalDate.now();
        List<String> productDisplays = new ArrayList<>();
        for (Product product : products) {
            String line = "";
            line = formatProducts(product, today);
            productDisplays.add(line);
        }
        return productDisplays;
    }

    private static String formatProducts(Product product, LocalDate today) {
        String promotionInfo = getPromotionInfo(product.getPromotion(), today);
        String stockInfo = product.getQuantity() + StockStatus.IN_STOCK.getStatus();
        String price = PRICE_FORMAT.format(product.getPrice());
        if (product.getQuantity() == 0) {
            stockInfo = StockStatus.OUT_OF_STOCK.getStatus();
        }

        String display = String.format("- %s %s원 %s %s",
                product.getName(), price, stockInfo, promotionInfo);
        return display.trim();
    }

    private static String getPromotionInfo(Promotion promotion, LocalDate today) {
        if (promotion != null) {
            try {
                return promotion.getName();
            } catch (IllegalArgumentException e) {
                return promotion.getName();
            }
        }
        return "";
    }
}
