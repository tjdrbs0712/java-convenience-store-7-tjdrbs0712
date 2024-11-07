package store.validation;

import store.error.InputException;
import store.message.ErrorMessage;

import java.util.Map;

public class ProductParserValidator {
    private static final String DELIMITER_PREFIX = "[";
    private static final String DELIMITER_END = "]";

    public static void validateProductEntryFormat(String productEntry){
        if (!productEntry.startsWith(DELIMITER_PREFIX) || !productEntry.endsWith(DELIMITER_END)) {
            throw new InputException(ErrorMessage.INVALID_FORMAT);
        }
    }

    public static void validateProductDetailsFormat(String[] productDetails){
        if (productDetails.length != 2 || productDetails[0].isBlank() || productDetails[1].isBlank()) {
            throw new InputException(ErrorMessage.INVALID_FORMAT);
        }
    }

    public static void validateProductNameContains(Map<String, Integer> parsedProductInfo, String name){
        if(parsedProductInfo.containsKey(name)){
            throw new InputException(ErrorMessage.DUPLICATE_PRODUCT);
        }
    }

}
