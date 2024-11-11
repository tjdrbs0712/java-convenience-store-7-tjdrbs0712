package store.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import store.domain.StockStatus;
import store.domain.store.Product;
import store.domain.store.Promotion;

public class ProductsViewUtil {

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###");

    /**
     * 시작 상품 목록을 출력해기 위한 파싱 메서드
     */
    public static List<String> ProductsViewFormatter(List<Product> products) {
        List<String> productDisplays = new ArrayList<>();
        for (Product product : products) {
            String line = "";
            line = formatProducts(product);
            productDisplays.add(line);
        }
        return productDisplays;
    }

    /**
     *
     */
    private static String formatProducts(Product product) {
        String promotionInfo = getPromotionInfo(product.getPromotion());
        String stockInfo = product.getQuantity() + StockStatus.IN_STOCK.getStatus();
        String price = PRICE_FORMAT.format(product.getPrice());
        if (product.getQuantity() == 0) {
            stockInfo = StockStatus.OUT_OF_STOCK.getStatus();
        }

        String display = String.format("- %s %s원 %s %s",
                product.getName(), price, stockInfo, promotionInfo);
        return display.trim();
    }

    private static String getPromotionInfo(Promotion promotion) {
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
