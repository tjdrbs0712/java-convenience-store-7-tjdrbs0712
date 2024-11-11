package store.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.store.Product;
import store.domain.store.Promotion;
import store.dto.PromotionDto;

class FileParserTest {

    @Test
    @DisplayName("읽어온 프로모션을 제대로 파싱하는지 테스트")
    void testParsePromotion() {
        String line = "탄산2+1,2,1,2024-01-01,2024-12-31";
        Promotion promotion = FileParser.parsePromotion(line);

        assertThat(promotion.getName()).isEqualTo("탄산2+1");
        assertThat(promotion.getBuy()).isEqualTo(2);
        assertThat(promotion.getGet()).isEqualTo(1);
        assertThat(promotion.getStartDate()).isEqualTo("2024-01-01");
        assertThat(promotion.getEndDate()).isEqualTo("2024-12-31");
    }

    @Test
    @DisplayName("읽어온 상품 정보를 제대로 파싱하는지 테스트")
    void testParseProduct() {
        PromotionDto promotionDto = new PromotionDto(
                "탄산2+1", 2, 1, "2024-01-01", "2024-12-31");
        Map<String, Promotion> promotions = Map.of("탄산2+1", new Promotion(promotionDto));
        String line = "콜라,1000,10,탄산2+1";
        Product product = FileParser.parseProduct(line, promotions);

        assertThat(product.getName()).isEqualTo("콜라");
        assertThat(product.getPrice()).isEqualTo(1000);
        assertThat(product.getQuantity()).isEqualTo(10);
        assertThat(product.getPromotion()).isNotNull();
        assertThat(product.getPromotion().getName()).isEqualTo("탄산2+1");
    }

}