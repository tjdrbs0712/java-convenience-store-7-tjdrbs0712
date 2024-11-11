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

    public void checkCartItem(List<Product> products) {
        for (CartItem cartItem : cartItems) {
            ProductValidator.validateProductContains(products, cartItem.getName());
        }
    }

}
