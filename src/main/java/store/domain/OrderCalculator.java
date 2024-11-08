package store.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import store.domain.order.CartItem;
import store.dto.OrderProductDto;
import store.validation.ProductValidator;

public class OrderCalculator {
    private final List<Product> products;
    private final List<OrderProductDto> orderProductDtos = new ArrayList<>();

    public OrderCalculator(List<Product> products) {
        this.products = products;
    }

    public void addPurchaseProduct(List<CartItem> productDetails) {
        for (CartItem cartItem : productDetails) {
            ProductValidator.validateProductContains(products, cartItem.getName());
        }
    }

    public void calculator(List<CartItem> productDetails) {

        for (CartItem cartItem : productDetails) {
            String productName = cartItem.getName();
            int quantity = cartItem.getQuantity();
            orderProductDtos.add(asd(productName, quantity));
        }
    }

    private OrderProductDto asd(String productName, int quantity) {

        return null;
    }


    private List<Product> findProductsByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .sorted(Comparator.comparing(product -> product.getPromotion() == null))
                .toList();
    }

//    private int calculateTotalCostForProduct(String productName, int quantity) {
//        List<Product> matchingProducts = findProductsByName(productName);
//
//        int totalCost = 0;
//        int remainingQuantity = quantity;
//
//        // 프로모션이 있는 상품을 우선 처리
//        totalCost += processProducts(matchingProducts, remainingQuantity, true);
//
//        // 프로모션이 없는 상품을 처리
//        remainingQuantity -= quantity;
//        totalCost += processProducts(matchingProducts, remainingQuantity, false);
//
//        if (remainingQuantity > 0) {
//            throw new InputException(ErrorMessage.EXCEEDS_STOCK);
//        }
//
//        return totalCost;
//    }
//
//    private int processProducts(List<Product> products, int remainingQuantity, boolean hasPromotion) {
//        int totalCost = 0;
//
//        for (Product product : products) {
//            if ((product.getPromotion() != null) == hasPromotion) {
//                int quantityToBuy = Math.min(product.getQuantity(), remainingQuantity);
//                totalCost += product.getPrice() * quantityToBuy;
//                product.updateQuantity(product.getQuantity() - quantityToBuy);
//                remainingQuantity -= quantityToBuy;
//
//                if (remainingQuantity == 0) {
//                    break;
//                }
//            }
//        }
//
//        return totalCost;
//    }

}
