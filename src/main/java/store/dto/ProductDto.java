package store.dto;

import store.domain.store.Promotion;

public class ProductDto {
    private String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    public ProductDto(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
