package store.domain.receipt;

public class PurchaseAmount {

    private final int totalPrice;
    private final int PromotionDiscount;
    private final int membershipDiscount;
    private final int pay;

    public PurchaseAmount(int totalPrice, int promotionDiscount, int membershipDiscount, int pay) {
        this.totalPrice = totalPrice;
        PromotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.pay = pay;
    }
}
