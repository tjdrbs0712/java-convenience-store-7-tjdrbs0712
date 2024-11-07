package store.util;

import store.domain.Product;
import store.domain.Promotion;

import java.util.Map;

public class FileParser {

    public static Promotion parsePromotion(String line) {
        String[] data = line.split(",");
        String name = data[0];
        int buy = Integer.parseInt(data[1]);
        int get = Integer.parseInt(data[2]);
        String startDate = data[3];
        String endDate = data[4];

        return new Promotion(name, buy, get, startDate, endDate);
    }

    public static Product parseProduct(String line, Map<String, Promotion> promotions) {
        String[] data = line.split(",");
        String name = data[0];
        int price = Integer.parseInt(data[1]);
        int quantity = Integer.parseInt(data[2]);
        String promotionName = data[3];

        Promotion promotion = promotions.getOrDefault(promotionName, null);
        return new Product(name, price, quantity, promotion);
    }

}
