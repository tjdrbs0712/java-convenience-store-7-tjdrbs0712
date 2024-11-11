package store.message;

public enum ErrorMessage {
    INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    EXCEEDS_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요."),
    DUPLICATE_PRODUCT("중복된 상품이 입력되었습니다."),

    ;

    private final String message;
    private static final String ERROR_PREFIX = "[ERROR] ";

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_PREFIX + message;
    }
}
