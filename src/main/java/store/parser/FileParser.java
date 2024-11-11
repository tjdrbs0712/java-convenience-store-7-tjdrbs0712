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
import store.dto.ProductDto;
import store.dto.PromotionDto;

public class FileParser {

    /**
     * md 파일에서 프로모션을 읽어와 프로모션 클래스에 저장하는 메서드
     *
     * @param line 읽어온 데이터의 한줄
     * @return 프로모션 리턴
     */
    public static Promotion parsePromotion(String line) {
        String[] data = line.split(DELIMITER);
        String name = data[ZERO_INDEX];
        int buy = Integer.parseInt(data[ONE_INDEX]);
        int get = Integer.parseInt(data[TWO_INDEX]);
        String startDate = data[THREE_DATE_INDEX];
        String endDate = data[FOUR_DATE_INDEX];

        return new Promotion(new PromotionDto(name, buy, get, startDate, endDate));
    }

    /**
     * md 파일에서 상품을 읽어와 상품 클래스에 저장하는 메서드
     *
     * @param line 읽어온 데이터의 한줄
     * @return 상품 리턴
     */
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
        return new Product(new ProductDto(name, price, quantity, promotion));
    }

}
