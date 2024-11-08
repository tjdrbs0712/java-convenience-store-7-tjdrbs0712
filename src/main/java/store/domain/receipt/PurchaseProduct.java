package store.domain.receipt;

public class PurchaseProduct {

    private final String name;
    private final int quantity;
    private final int price;

    public PurchaseProduct(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

}
