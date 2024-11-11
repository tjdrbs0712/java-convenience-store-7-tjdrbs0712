package store.domain.receipt;

public class GiveAway {

    private final String name;
    private final int quantity;
    private final int totalPrice;

    public GiveAway(String name, int quantity, int totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
