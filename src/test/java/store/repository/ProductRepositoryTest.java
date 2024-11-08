package store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.FileConstant;
import store.domain.store.Product;
import store.domain.store.Promotion;

class ProductRepositoryTest {

    private ProductRepository productRepository;
    Map<String, Promotion> promotions;

    @BeforeEach
    void setUp() throws IOException {
        productRepository = new ProductRepository();

        String filePath = FileConstant.PROMOTIONS_FILE_PATH;
        promotions = productRepository.loadPromotions(filePath);
    }

    @Test
    @DisplayName("promotions.md 파일을 제대로 읽어오는지 테스트")
    void testLoadPromotions() throws IOException {
        String testPromotion1 = "탄산2+1";
        String testPromotion2 = "MD추천상품";
        String testPromotion3 = "반짝할인";

        assertThat(promotions).containsKeys(testPromotion1, testPromotion2, testPromotion3);
    }

    @Test
    @DisplayName("products.md 파일을 제대로 읽어오는지 테스트")
    void testLoadProducts() throws IOException {
        String filePath = FileConstant.PRODUCTS_FILE_PATH;
        String name = "콜라";
        int price = 1000;
        String promotion = "탄산2+1";

        productRepository.loadProducts(filePath, promotions);
        List<Product> products = productRepository.getProducts();
        Product product = products.getFirst();

        assertThat(name).isEqualTo(product.getName());
        assertThat(price).isEqualTo(product.getPrice());
        assertThat(promotion).isEqualTo(promotions.get(promotion).getName());
    }

}