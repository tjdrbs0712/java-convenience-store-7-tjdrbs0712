package store.validation;

import java.util.List;
import store.domain.store.Product;
import store.error.InputException;
import store.message.ErrorMessage;

public class ProductValidator {

    //구매하려는 상품이 있는지 검증
    public static void validateProductContains(List<Product> products, String productName) {
        boolean exists = products.stream()
                .anyMatch(product -> product.getName().equals(productName));

        if (!exists) {
            throw new InputException(ErrorMessage.PRODUCT_NOT_FOUND);
        }
    }

    //재고 부족
    public static void validateStockAvailability(List<Product> products, int requestedQuantity) {
        int availableQuantity = products.stream().mapToInt(Product::getQuantity).sum();
        if (availableQuantity < requestedQuantity) {
            throw new InputException(ErrorMessage.EXCEEDS_STOCK);
        }
    }

    public static void validateInputYesOrNo(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new InputException(ErrorMessage.INVALID_INPUT);
        }
    }
}
