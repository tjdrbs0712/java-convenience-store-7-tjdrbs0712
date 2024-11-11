package store.domain.receipt;

import store.domain.store.Promotion;

public class PurchaseProduct {
    private String name;
    private int quantity;
    private int price;
    private Promotion promotion;

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

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

}
