package store.domain.receipt;

import java.util.List;

public class PurchaseAmount {
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

    public void totalPriceCalculator(List<PurchaseProduct> purchaseProducts) {
        for (PurchaseProduct purchaseProduct : purchaseProducts) {
            int purchaseQuantity = purchaseProduct.getQuantity();
            int purchasePrice = purchaseProduct.getPrice();
            this.totalPrice += purchaseQuantity * purchasePrice;
        }
    }

    public void membershipDiscountCal() {
        membershipDiscount = (int) ((totalPrice - netPrice) * 0.3f);
        if (membershipDiscount > 8000) {
            membershipDiscount = 8000;
        }
    }

    public void resultPay() {
        pay = totalPrice - promotionDiscount - membershipDiscount;
    }

}
