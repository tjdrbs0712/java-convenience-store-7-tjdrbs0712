package store.domain.receipt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PurchaseAmountTest {

    private PurchaseAmount purchaseAmount;

    @BeforeEach
    void setUp() {
        purchaseAmount = new PurchaseAmount();
    }

    @Test
    @DisplayName("총 금액을 올바르게 계산하는지 확인")
    void testCalculateTotalPrice() {
        List<PurchaseProduct> purchaseProducts = List.of(
                new PurchaseProduct("Product1", 2, 1000, null),
                new PurchaseProduct("Product2", 1, 2000, null)
        );

        purchaseAmount.calculateTotalPrice(purchaseProducts);

        assertThat(purchaseAmount.getTotalPrice()).isEqualTo(4000);
    }

    @Test
    @DisplayName("멤버십 할인을 올바르게 계산하는지 확인")
    void testCalculateMembershipDiscount1() {
        purchaseAmount.addNetPrice(3000);
        purchaseAmount.calculateTotalPrice(List.of(
                new PurchaseProduct("Product1", 10, 1000, null)
        ));

        purchaseAmount.calculateMembershipDiscount();

        assertThat(purchaseAmount.getMembershipDiscount()).isEqualTo(2100);
    }

    @Test
    @DisplayName("멤버십 할인이 최대 할인보다 큰 경우 8000으로 고정")
    void testCalculateMembershipDiscount2() {
        purchaseAmount.addNetPrice(5000);
        purchaseAmount.calculateTotalPrice(List.of(
                new PurchaseProduct("Product1", 5, 100000, null)
        ));

        purchaseAmount.calculateMembershipDiscount();

        assertThat(purchaseAmount.getMembershipDiscount()).isEqualTo(8000);
    }

    @Test
    @DisplayName("최종 결제 금액이 올바르게 계산되는지 확인")
    void testCalculateFinalPay() {
        purchaseAmount.calculateTotalPrice(List.of(
                new PurchaseProduct("Product1", 2, 1000, null),
                new PurchaseProduct("Product2", 1, 2000, null)
        ));
        purchaseAmount.resultPromotionDiscount(500);
        purchaseAmount.addNetPrice(3000);
        purchaseAmount.calculateMembershipDiscount();
        purchaseAmount.calculateFinalPay();

        assertThat(purchaseAmount.getPay()).isEqualTo(
                purchaseAmount.getTotalPrice() - purchaseAmount.getPromotionDiscount()
                        - purchaseAmount.getMembershipDiscount()
        );
    }

}