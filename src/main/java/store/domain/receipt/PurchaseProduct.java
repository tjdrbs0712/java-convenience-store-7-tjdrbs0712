package store.domain.receipt;

import store.domain.store.Promotion;

public class PurchaseProduct {
    private final String name;
    private final int quantity;
    private final int price;
    private final Promotion promotion;

    public PurchaseProduct(String name, int quantity, int price, Promotion promotion) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }

}
