package store.domain.receipt;

public class PurchaseAmount {
    private int totalPrice;
    private int PromotionDiscount;
    private int membershipDiscount;
    private int pay;


    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return PromotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getPay() {
        return pay;
    }

    public void addTotalPrice(int price) {
        totalPrice += price;
    }

}
