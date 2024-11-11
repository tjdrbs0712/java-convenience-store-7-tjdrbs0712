package store.domain.receipt;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

    private final List<PurchaseProduct> purchaseProducts = new ArrayList<>();
    private final List<GiveAway> giveAway = new ArrayList<>();
    private final PurchaseAmount purchaseAmount;

    public Receipt() {
        purchaseAmount = new PurchaseAmount();
    }

    public List<PurchaseProduct> getPurchaseProducts() {
        return purchaseProducts;
    }

    public List<GiveAway> getGiveAway() {
        return giveAway;
    }

    public PurchaseAmount getPurchaseAmount() {
        return purchaseAmount;
    }

    public void addPurchaseProducts(PurchaseProduct purchaseProduct) {
        purchaseProducts.add(purchaseProduct);
    }

    public void addGiveAway(GiveAway giveAway) {
        if (giveAway != null) {
            this.giveAway.add(giveAway);
        }
    }

    /**
     * 주문 상품 최종 금액, 프로모션 상품의 총 금액을 계산하는 메서드
     */
    public void ResultReceipt() {
        purchaseAmount.calculateTotalPrice(purchaseProducts);
        int promotionTotalPrice = giveAway.stream().mapToInt(GiveAway::getTotalPrice).sum();
        purchaseAmount.resultPromotionDiscount(promotionTotalPrice);
    }

    public void resultMembershipDiscount() {
        purchaseAmount.calculateMembershipDiscount();
    }

    public void resultPay() {
        purchaseAmount.calculateFinalPay();
    }

}
