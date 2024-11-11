package store.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.order.Cart;
import store.domain.store.Product;
import store.dto.ProductDto;
import store.error.InputException;
import store.message.ErrorMessage;

class OrderParserTest {
    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = List.of(
                new Product(new ProductDto("콜라", 1000, 10, null)),
                new Product(new ProductDto("사이다", 1200, 8, null)),
                new Product(new ProductDto("감자칩", 1500, 5, null))
        );
    }

    @Test
    @DisplayName("올바른 주문 문자열을 파싱하는지 확인")
    void testOrderParser() {
        String order = "[콜라-2],[사이다-3]";

        Cart cart = OrderParser.orderParser(order, products);

        assertThat(cart.getCartItems().getFirst().getName()).isEqualTo("콜라");
        assertThat(cart.getCartItems().getFirst().getQuantity()).isEqualTo(2);
        assertThat(cart.getCartItems().get(1).getName()).isEqualTo("사이다");
        assertThat(cart.getCartItems().get(1).getQuantity()).isEqualTo(3);
    }

    @Test
    @DisplayName("잘못된 형식의 주문 문자열이 예외를 발생시키는지 확인")
    void testOrderParserNumberFormatException1() {
        String order = "[콜라-2],[사이다-abc]";

        assertThatThrownBy(() -> OrderParser.orderParser(order, products))
                .isInstanceOf(InputException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }

    @Test
    @DisplayName("잘못된 형식의 주문 문자열이 예외를 발생시키는지 확인")
    void testOrderParserNumberFormatException2() {
        String order = "[콜라-2],";

        assertThatThrownBy(() -> OrderParser.orderParser(order, products))
                .isInstanceOf(InputException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }

    @Test
    @DisplayName("잘못된 형식의 주문 문자열이 예외를 발생시키는지 확인")
    void testOrderParserNumberFormatException3() {
        String order = "[콜라-2[]";

        assertThatThrownBy(() -> OrderParser.orderParser(order, products))
                .isInstanceOf(InputException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }


    @Test
    @DisplayName("잘못된 형식의 주문 문자열이 예외를 발생시키는지 확인")
    void testOrderParserNumberFormatException4() {
        String order = ",[콜라-2]";

        assertThatThrownBy(() -> OrderParser.orderParser(order, products))
                .isInstanceOf(InputException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }


    @Test
    @DisplayName("잘못된 형식의 주문 문자열이 예외를 발생시키는지 확인")
    void testOrderParserNumberFormatException5() {
        String order = "[콜라-2],[사이다:5]";

        assertThatThrownBy(() -> OrderParser.orderParser(order, products))
                .isInstanceOf(InputException.class)
                .hasMessageContaining(ErrorMessage.INVALID_FORMAT.getMessage());
    }


    @Test
    @DisplayName("주문 문자열에 존재하지 않는 상품 이름이 포함된 경우 예외 발생")
    void testOrderParserHasNotProduct() {
        String order = "[초코파이-2]"; // 존재하지 않는 상품

        assertThatThrownBy(() -> OrderParser.orderParser(order, products))
                .isInstanceOf(InputException.class)
                .hasMessageContaining(ErrorMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("주문 문자열에 중복된 주문을 할 경우")
    void testOrderParserDuplicationException() {
        String order = "[콜라-2],[콜라-2]"; // 존재하지 않는 상품

        assertThatThrownBy(() -> OrderParser.orderParser(order, products))
                .isInstanceOf(InputException.class)
                .hasMessageContaining(ErrorMessage.DUPLICATE_PRODUCT.getMessage());
    }


}