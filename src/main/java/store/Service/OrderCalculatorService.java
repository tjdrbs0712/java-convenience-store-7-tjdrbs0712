package store.Service;

import java.util.List;
import store.domain.order.CartItem;
import store.domain.receipt.PurchaseProduct;
import store.domain.receipt.Receipt;
import store.domain.store.Product;
import store.domain.store.Promotion;
import store.repository.ProductRepository;
import store.repository.StoreRepository;
import store.validation.ProductValidator;
import store.view.InputView;

public class OrderCalculatorService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InputView inputView;
    private final Receipt receipt = new Receipt();

    public OrderCalculatorService(StoreRepository storeRepository, ProductRepository productRepository,
                                  InputView inputView) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inputView = inputView;
    }

    public void addCartItem(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            ProductValidator.validateProductContains(productRepository.getProducts(), cartItem.getName());
        }
    }

    public Receipt calculateTotal(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            createPurchaseProductDto(cartItem.getName(), cartItem.getQuantity());
        }
        updateProductQuantity(receipt.getPurchaseProducts());
        return receipt;
    }

    private void updateProductQuantity(List<PurchaseProduct> purchaseProducts) {
        for (PurchaseProduct dto : purchaseProducts) {
            Product product = storeRepository.findProduct(dto.getName(), dto.getPromotion());
            product.updateQuantity(dto.getQuantity());
        }
    }

    private void createPurchaseProductDto(String productName, int requestedQuantity) {
        List<Product> products = productRepository.findProductsByName(productName);
        ProductValidator.validateStockAvailability(products, requestedQuantity);

        int remainingQuantity = requestedQuantity;
        for (Product product : products) {
            remainingQuantity = processProduct(product, remainingQuantity);
            if (remainingQuantity == 0) {
                break;
            }
        }
    }

    private int processProduct(Product product, int requestQuantity) {
        Promotion promotion = product.getPromotion();
        int productQuantity = product.getQuantity();

        if (promotion != null) {
            requestQuantity = applyPromotionNeed(product, promotion, requestQuantity, productQuantity);
            receipt.addGiveAway(promotion.calculateGiveAway(product, Math.min(requestQuantity, productQuantity)));
        }

        int purchaseQuantity = Math.min(productQuantity, requestQuantity);
        receipt.addPurchaseProducts(
                new PurchaseProduct(product.getName(), purchaseQuantity, product.getPrice(), promotion));

        return requestQuantity - purchaseQuantity;
    }

    private int applyPromotionNeed(Product product, Promotion promotion, int requestQuantity, int productQuantity) {
        if (requestQuantity < productQuantity) {
            return applyPromotion(product, promotion, requestQuantity);
        }
        return requestQuantity;
    }

    private int applyPromotion(Product product, Promotion promotion, int requestQuantity) {
        int buy = promotion.getBuy();
        int promotionQuantity = buy + promotion.getGet();
        if (requestQuantity % promotionQuantity == buy) {
            return addPromotion(product, requestQuantity);
        }
        return requestQuantity;
    }


    private int addPromotion(Product product, int requestQuantity) {
        try {
            String input = inputView.addGiveAway(product.getName());
            ProductValidator.validateInputYesOrNo(input);
            if (input.equals("Y")) {
                return requestQuantity++;
            }
            return requestQuantity;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return addPromotion(product, requestQuantity);
        }
    }
}
