package store.domain.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.receipt.GiveAway;
import store.domain.receipt.PurchaseAmount;
import store.dto.ProductDto;
import store.dto.PromotionDto;

class PromotionTest {

    private Promotion promotion;
    private PurchaseAmount purchaseAmount;

    @BeforeEach
    void setUp() {
        PromotionDto promotionDto = new PromotionDto("2+1", 2, 1, "2024-01-01", "2024-12-31");
        promotion = new Promotion(promotionDto);
        purchaseAmount = new PurchaseAmount();
    }

    @Test
    @DisplayName("요청 수량이 프로모션 조건에 맞을 때 무료 상품을 정확히 계산하는지 확인")
    void testCalculateGiveAway1() {
        Product product = new Product(new ProductDto("콜라", 1000, 10, promotion));
        int requestedQuantity = 6;

        GiveAway giveAway = promotion.calculateGiveAway(product, requestedQuantity, purchaseAmount);

        assertThat(giveAway).isNotNull();
        assertThat(giveAway.getName()).isEqualTo("콜라");
        assertThat(giveAway.getQuantity()).isEqualTo(2);
        assertThat(giveAway.getTotalPrice()).isEqualTo(2000);
    }

    @Test
    @DisplayName("요청 수량이 프로모션 조건에 미달할 때 무료 상품이 없는지 확인")
    void testcalculateGiveAway2() {
        Product product = new Product(new ProductDto("사이다", 1000, 10, promotion));
        int requestedQuantity = 1;

        GiveAway giveAway = promotion.calculateGiveAway(product, requestedQuantity, purchaseAmount);

        assertThat(giveAway).isNull();
    }


}