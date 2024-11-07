package store.parser;

import store.error.InputException;
import store.message.ErrorMessage;
import store.validation.ProductParserValidator;

import java.util.HashMap;
import java.util.Map;

public class OrderParser {
    private static final String DELIMITER = ",";
    public static final String PRODUCT_QUANTITY_DELIMITER = "-";

    public static Map<String, Integer> orderParser(String order){
        Map<String, Integer> parsedProductInfo = new HashMap<>();
        String[] productEntries = order.split(DELIMITER, -1);

        for (String productEntry : productEntries) {
            productParser(parsedProductInfo, productEntry);
        }

        return parsedProductInfo;
    }

    private static void productParser(Map<String, Integer> parsedProductInfo, String productEntry){
        ProductParserValidator.validateProductEntryFormat(productEntry);
        String[] productDetails = extractProductDetails(productEntry);

        String productName = productDetails[0];
        int quantity = parseQuantity(productDetails[1]);

        ProductParserValidator.validateProductNameContains(parsedProductInfo, productName);
        parsedProductInfo.put(productName, quantity);
    }

    private static String[] extractProductDetails(String productEntry) {
        String[] productDetails = productEntry.substring(1, productEntry.length() - 1)
                .split(PRODUCT_QUANTITY_DELIMITER);
        ProductParserValidator.validateProductDetailsFormat(productDetails);
        return productDetails;
    }

    private static int parseQuantity(String quantityStr){
        try{
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e){
            throw new InputException(ErrorMessage.INVALID_FORMAT);
        }
    }

}
