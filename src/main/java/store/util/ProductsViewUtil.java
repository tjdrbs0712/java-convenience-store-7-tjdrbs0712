package store.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.StockStatus;
import store.domain.store.Product;
import store.domain.store.Promotion;

public class ProductsViewUtil {


    public static List<String> ProductsViewFormatter(List<Product> products) {
        LocalDate today = LocalDate.now();
        List<String> productDisplays = new ArrayList<>();

        for (Product product : products) {
            String line = "";
            if (isValidPromotion(product.getPromotion(), today) || product.getPromotion() == null) {
                line = formatProducts(product, today);
            }
            productDisplays.add(line);
        }
        return productDisplays;
    }

    private static String formatProducts(Product product, LocalDate today) {
        String promotionInfo = getPromotionInfo(product.getPromotion(), today);
        String stockInfo = product.getQuantity() + StockStatus.IN_STOCK.getStatus();
        if (product.getQuantity() == 0) {
            stockInfo = StockStatus.OUT_OF_STOCK.getStatus();
        }

        String display = String.format("- %s %d원 %s %s",
                product.getName(), product.getPrice(), stockInfo, promotionInfo);

        return display.trim();
    }

    private static boolean isValidPromotion(Promotion promotion, LocalDate today) {
        if (promotion == null) {
            return false;
        }

        LocalDate startDate = LocalDate.parse(promotion.getStartDate());
        LocalDate endDate = LocalDate.parse(promotion.getEndDate());

        return (today.isEqual(startDate) || today.isAfter(startDate)) &&
                (today.isEqual(endDate) || today.isBefore(endDate));
    }

    private static String getPromotionInfo(Promotion promotion, LocalDate today) {
        if (isValidPromotion(promotion, today)) {
            try {
                return promotion.getName();
            } catch (IllegalArgumentException e) {
                return promotion.getName();
            }
        }
        return "";
    }
}
