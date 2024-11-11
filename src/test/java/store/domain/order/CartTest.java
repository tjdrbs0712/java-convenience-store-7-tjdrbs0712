package store.domain.order;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.FileConstant;
import store.domain.store.Product;
import store.domain.store.Promotion;
import store.message.ErrorMessage;
import store.repository.ProductRepository;

class CartTest {

    private ProductRepository productRepository;
    Map<String, Promotion> promotions;
    List<Product> products;

    @BeforeEach
    void setup() throws IOException {
        productRepository = new ProductRepository();

        String promotionFilePath = FileConstant.PROMOTIONS_FILE_PATH;
        String ProductFilePath = FileConstant.PRODUCTS_FILE_PATH;
        promotions = productRepository.loadPromotions(promotionFilePath);
        productRepository.loadProducts(ProductFilePath, promotions);
        products = productRepository.getProducts();
    }

    @Test
    @DisplayName("카트에 담은 상품이 존재하는 경우")
    void testCartItemExist() {
        CartItem cartItem1 = new CartItem("콜라", 3);
        CartItem cartItem2 = new CartItem("사이다", 3);
        CartItem cartItem3 = new CartItem("감자칩", 3);

        Cart cart = new Cart();

        cart.addCartItem(cartItem1);
        cart.addCartItem(cartItem2);
        cart.addCartItem(cartItem3);

        assertThatCode(() -> cart.checkCartItem(products))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("카트에 담은 상품이 존재하지 않을 때 예외 발생")
    void testCartItemNotExistThrowsException() {

        CartItem cartItem1 = new CartItem("콜라", 3);
        CartItem cartItem2 = new CartItem("없는 상품", 3); // 존재하지 않는 상품

        Cart cart = new Cart();

        cart.addCartItem(cartItem1);
        cart.addCartItem(cartItem2);

        // when & then
        assertThatThrownBy(() -> cart.checkCartItem(products))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.PRODUCT_NOT_FOUND.getMessage());
    }

}