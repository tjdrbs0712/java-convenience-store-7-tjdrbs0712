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

    /**
     * netPrice 프로모션 상품의 총금액
     *
     * @param price 프로모션 상품 가격
     */
    public void addNetPrice(int price) {
        netPrice += price;
    }

    public void resultPromotionDiscount(int promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    /**
     * 주문 상품 목록으로 총 금액 계산하는 메서드
     *
     * @param purchaseProducts 주문 상품 목록
     */
    public void calculateTotalPrice(List<PurchaseProduct> purchaseProducts) {
        totalPrice = purchaseProducts.stream()
                .mapToInt(p -> p.getQuantity() * p.getPrice())
                .sum();
    }

    /**
     * 멤버십 할인 금액을 계산하는 메서드
     */
    public void calculateMembershipDiscount() {
        membershipDiscount = (int) ((totalPrice - netPrice) * DIVISION_VALUE);
        membershipDiscount = Math.min(membershipDiscount, MAX_DISCOUNT);
    }

    /**
     * 최종 지불 금액
     */
    public void calculateFinalPay() {
        pay = totalPrice - promotionDiscount - membershipDiscount;
    }

}
