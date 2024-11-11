package store.domain.store;

import store.dto.ProductDto;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    public Product(ProductDto productDto) {
        this.name = productDto.getName();
        this.price = productDto.getPrice();
        this.quantity = productDto.getQuantity();
        this.promotion = productDto.getPromotion();
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

    public void updateQuantity(int purchaseQuantity) {
        quantity -= purchaseQuantity;
    }

}
