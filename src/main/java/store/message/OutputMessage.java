package store.message;

public enum OutputMessage {
    WELCOME("안녕하세요. W편의점입니다."),
    INVENTORY("현재 보유하고 있는 상품입니다.\n"),
    PURCHASE_PROMPT("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    ADD_GIVEAWAY("\n현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    NON_PROMOTION_QUANTITY("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    MEMBERSHIP_DISCOUNT("\n멤버십 할인을 받으시겠습니까? (Y/N)"),
    RETRY_PURCHASE("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"),
    STORE("\n==============W 편의점================"),
    PRESENTATION("=============증\t정==============="),
    EQUAL_SIGN("===================================="),
    PRODUCT_NAME("상품평"),
    QUANTITY("수량"),
    AMOUNT("금액"),
    TOTAL_PRICE("총구매액"),
    PROMOTION_DISCOUNT("행사할인"),
    MEMBERSHIP_DISCOUNT_PRICE("맴버십할인"),
    PAY("내실돈"),

    ;

    private final String message;

    OutputMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
