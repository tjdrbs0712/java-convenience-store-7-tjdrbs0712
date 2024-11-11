package store.parser;

import static store.constant.GlobalConstant.DELIMITER;
import static store.constant.GlobalConstant.ONE_INDEX;
import static store.constant.GlobalConstant.PRODUCT_QUANTITY_DELIMITER;
import static store.constant.GlobalConstant.ZERO_INDEX;

import java.util.List;
import store.domain.order.Cart;
import store.domain.order.CartItem;
import store.domain.store.Product;
import store.error.InputException;
import store.message.ErrorMessage;
import store.validation.ProductParserValidator;

public class OrderParser {

    /**
     * 주문 문자열을 입력 받아 파싱해주는 메서드
     *
     * @param order    주문 문자열
     * @param products 편의점 상품 목록
     * @return 주문한 상품 목록
     */
    public static Cart orderParser(String order, List<Product> products) {
        Cart cart = new Cart();
        String[] productEntries = order.split(DELIMITER, -1);

        for (String productEntry : productEntries) {
            productParser(cart, productEntry);
        }
        cart.checkCartItem(products);

        return cart;
    }

    /**
     * 구분자로 잘라진 상품들을 상품 이름과, 재고로 cart 클래스 필드에 저장
     *
     * @param cart         주문한 상품들을 담아줄 저장소
     * @param productEntry 구분자로 잘라진 상품 이름과 재고
     */
    private static void productParser(Cart cart, String productEntry) {
        ProductParserValidator.validateProductEntryFormat(productEntry);
        String[] productDetails = extractProductDetails(productEntry);

        String productName = productDetails[ZERO_INDEX];
        int quantity = parseQuantity(productDetails[ONE_INDEX]);

        ProductParserValidator.validateProductNameContains(cart, productName);

        cart.addCartItem(new CartItem(productName, quantity));
    }

    /**
     * "[", "]" 구분자 분리하기
     *
     * @param productEntry 주문한 상품의 이름과 갯수 ex) [콜라-2]
     * @return
     */
    private static String[] extractProductDetails(String productEntry) {
        String[] productDetails = productEntry.substring(ONE_INDEX, productEntry.length() - 1)
                .split(PRODUCT_QUANTITY_DELIMITER);
        ProductParserValidator.validateProductDetailsFormat(productDetails);
        return productDetails;
    }

    /**
     * 주문한 상품 갯수를 정수로 파싱
     *
     * @param quantityStr 문자열 숫자
     * @return 상품 갯수
     */
    private static int parseQuantity(String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr);
            ProductParserValidator.validateProductQuantityMinus(quantity);
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new InputException(ErrorMessage.INVALID_FORMAT);
        }
    }

}
