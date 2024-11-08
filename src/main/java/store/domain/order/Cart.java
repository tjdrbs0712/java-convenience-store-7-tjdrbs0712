package store.domain.order;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }

}
