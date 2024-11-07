package store.message;

public enum PromotionType {
    TWO_PLUS_ONE("탄산2+1"),
    MD_RECOMMENDED("MD추천상품"),
    LIMITED_DISCOUNT("반짝할인");

    private final String promotionName;

    PromotionType(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionName() {
        return promotionName;
    }
}
