package store.parser;

import static store.constant.GlobalConstant.DELIMITER;
import static store.constant.GlobalConstant.FOUR_DATE_INDEX;
import static store.constant.GlobalConstant.ONE_INDEX;
import static store.constant.GlobalConstant.STRING_NULL;
import static store.constant.GlobalConstant.THREE_DATE_INDEX;
import static store.constant.GlobalConstant.TWO_INDEX;
import static store.constant.GlobalConstant.ZERO_INDEX;

import java.util.Map;
import store.domain.store.Product;
import store.domain.store.Promotion;

public class FileParser {

    public static Promotion parsePromotion(String line) {
        String[] data = line.split(DELIMITER);
        String name = data[ZERO_INDEX];
        int buy = Integer.parseInt(data[ONE_INDEX]);
        int get = Integer.parseInt(data[TWO_INDEX]);
        String startDate = data[THREE_DATE_INDEX];
        String endDate = data[FOUR_DATE_INDEX];

        return new Promotion(name, buy, get, startDate, endDate);
    }

    public static Product parseProduct(String line, Map<String, Promotion> promotions) {
        String[] data = line.split(DELIMITER);
        String name = data[ZERO_INDEX];
        int price = Integer.parseInt(data[ONE_INDEX]);
        int quantity = Integer.parseInt(data[TWO_INDEX]);
        String promotionName = data[THREE_DATE_INDEX];
        if (!promotionName.equals(STRING_NULL) && !promotions.containsKey(promotionName)) {
            return null;
        }
        Promotion promotion = promotions.getOrDefault(promotionName, null);
        return new Product(name, price, quantity, promotion);
    }

}
