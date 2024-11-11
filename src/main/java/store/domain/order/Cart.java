package store.domain.order;

import java.util.ArrayList;
import java.util.List;
import store.domain.store.Product;
import store.validation.ProductValidator;

public class Cart {

    private final List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }

    /**
     * 구매하려는 상품이 존재하는지 검증
     *
     * @param products 전체 상품 리스트
     */
    public void checkCartItem(List<Product> products) {
        for (CartItem cartItem : cartItems) {
            ProductValidator.validateProductContains(products, cartItem.getName());
        }
    }

}
