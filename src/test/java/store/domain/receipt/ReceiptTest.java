package store.domain.receipt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReceiptTest {
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
    }

    @Test
    @DisplayName("총 구매 금액 및 프로모션 할인이 올바르게 계산되는지 확인")
    void testResultReceipt() {
        PurchaseProduct product1 = new PurchaseProduct("콜라", 3, 1000, null);
        PurchaseProduct product2 = new PurchaseProduct("사이다", 2, 1200, null);
        GiveAway giveAway = new GiveAway("사은품", 1, 500);

        receipt.addPurchaseProducts(product1);
        receipt.addPurchaseProducts(product2);
        receipt.addGiveAway(giveAway);

        receipt.ResultReceipt();

        int totalPrice = 3 * 1000 + 2 * 1200;
        int promotionDiscount = 500;

        assertThat(receipt.getPurchaseAmount().getTotalPrice()).isEqualTo(totalPrice);
        assertThat(receipt.getPurchaseAmount().getPromotionDiscount()).isEqualTo(promotionDiscount);
    }

    @Test
    @DisplayName("멤버십 할인이 올바르게 적용되는지 확인")
    void testResultMembershipDiscount() {
        PurchaseProduct product = new PurchaseProduct("콜라", 10, 1000, null);

        receipt.addPurchaseProducts(product);
        receipt.ResultReceipt();
        receipt.resultMembershipDiscount();

        int discount = Math.min((int) (receipt.getPurchaseAmount().getTotalPrice() * 0.3f), 8000);

        assertThat(receipt.getPurchaseAmount().getMembershipDiscount()).isEqualTo(discount);
    }

    @Test
    @DisplayName("최종 결제 금액이 올바르게 계산되는지 확인")
    void testResultPay() {
        PurchaseProduct purchaseProduct = new PurchaseProduct("콜라", 5, 1000, null);
        GiveAway giveAway = new GiveAway("사은품", 1, 500);

        receipt.addPurchaseProducts(purchaseProduct);
        receipt.addGiveAway(giveAway);
        receipt.ResultReceipt();
        receipt.resultMembershipDiscount();
        receipt.resultPay();

        int finalPay = receipt.getPurchaseAmount().getTotalPrice()
                - receipt.getPurchaseAmount().getPromotionDiscount()
                - receipt.getPurchaseAmount().getMembershipDiscount();

        assertThat(receipt.getPurchaseAmount().getPay()).isEqualTo(finalPay);
    }
}