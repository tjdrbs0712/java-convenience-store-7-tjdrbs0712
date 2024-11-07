package store.validation;

import store.domain.Product;
import store.error.InputException;
import store.message.ErrorMessage;

import java.util.List;

public class ProductValidator {

    public static void validateProductContains(List<Product> products, String productName){
        boolean exists = products.stream()
                .anyMatch(product -> product.getName().equals(productName));

        if (!exists) {
            throw new InputException(ErrorMessage.PRODUCT_NOT_FOUND);
        }
    }
}
