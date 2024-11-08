package store.parser;

import store.domain.order.Cart;
import store.domain.order.CartItem;
import store.error.InputException;
import store.message.ErrorMessage;
import store.validation.ProductParserValidator;

public class OrderParser {
    private static final String DELIMITER = ",";
    public static final String PRODUCT_QUANTITY_DELIMITER = "-";

    public static Cart orderParser(String order) {

        Cart cart = new Cart();
        String[] productEntries = order.split(DELIMITER, -1);

        for (String productEntry : productEntries) {
            productParser(cart, productEntry);
        }

        return cart;
    }

    private static void productParser(Cart cart, String productEntry) {
        ProductParserValidator.validateProductEntryFormat(productEntry);
        String[] productDetails = extractProductDetails(productEntry);

        String productName = productDetails[0];
        int quantity = parseQuantity(productDetails[1]);

        ProductParserValidator.validateProductNameContains(cart, productName);

        cart.addCartItem(new CartItem(productName, quantity));
    }

    private static String[] extractProductDetails(String productEntry) {
        String[] productDetails = productEntry.substring(1, productEntry.length() - 1)
                .split(PRODUCT_QUANTITY_DELIMITER);
        ProductParserValidator.validateProductDetailsFormat(productDetails);
        return productDetails;
    }

    private static int parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new InputException(ErrorMessage.INVALID_FORMAT);
        }
    }

}
