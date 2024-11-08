package store.domain.order;

public class CartItem {

    private final String name;
    private final int quantity;

    public CartItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

}
