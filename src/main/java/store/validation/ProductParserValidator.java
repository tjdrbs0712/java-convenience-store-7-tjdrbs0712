package store.validation;

import store.domain.order.Cart;
import store.domain.order.CartItem;
import store.error.InputException;
import store.message.ErrorMessage;

public class ProductParserValidator {
    private static final String DELIMITER_PREFIX = "[";
    private static final String DELIMITER_END = "]";

    public static void validateProductEntryFormat(String productEntry) {
        if (!productEntry.startsWith(DELIMITER_PREFIX) || !productEntry.endsWith(DELIMITER_END)) {
            throw new InputException(ErrorMessage.INVALID_FORMAT);
        }
    }

    public static void validateProductDetailsFormat(String[] productDetails) {
        if (productDetails.length != 2 || productDetails[0].isBlank() || productDetails[1].isBlank()) {
            throw new InputException(ErrorMessage.INVALID_FORMAT);
        }
    }

    public static void validateProductNameContains(Cart cart, String name) {
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getName().equals(name)) {
                throw new InputException(ErrorMessage.DUPLICATE_PRODUCT);
            }
        }
    }

    public static void validateProductQuantityMinus(int quantity) {
        if (quantity < 1) {
            throw new InputException(ErrorMessage.INVALID_INPUT);
        }
    }

}
