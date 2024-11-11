package store.domain.receipt;

import java.util.List;

public class PurchaseAmount {

    final int MAX_DISCOUNT = 8000;
    final double DIVISION_VALUE = 0.3f;

    private int totalPrice;
    private int netPrice;
    private int promotionDiscount;
    private int membershipDiscount;
    private int pay;


    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getPay() {
        return pay;
    }

    public void addNetPrice(int price) {
        netPrice += price;
    }

    public void resultPromotionDiscount(int promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public void calculateTotalPrice(List<PurchaseProduct> purchaseProducts) {
        totalPrice = purchaseProducts.stream()
                .mapToInt(p -> p.getQuantity() * p.getPrice())
                .sum();
    }

    public void calculateMembershipDiscount() {
        membershipDiscount = (int) ((totalPrice - netPrice) * DIVISION_VALUE);
        membershipDiscount = Math.min(membershipDiscount, MAX_DISCOUNT);
    }

    public void calculateFinalPay() {
        pay = totalPrice - promotionDiscount - membershipDiscount;
    }

}
