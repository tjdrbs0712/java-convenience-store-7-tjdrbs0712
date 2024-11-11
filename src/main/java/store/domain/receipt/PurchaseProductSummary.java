package store.domain.receipt;

public class PurchaseProductSummary {
    private final String name;
    private int quantity;
    private int totalPrice;

    public PurchaseProductSummary(String name, int quantity, int totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public void addQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
    }

    public void addTotalPrice(int additionalPrice) {
        this.totalPrice += additionalPrice;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}

