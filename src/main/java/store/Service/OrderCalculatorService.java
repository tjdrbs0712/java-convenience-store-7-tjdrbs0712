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
    private Receipt receipt;

    public OrderCalculatorService(StoreRepository storeRepository, ProductRepository productRepository,
                                  InputView inputView) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inputView = inputView;
    }

    public Receipt calculateTotal(List<CartItem> cartItems) {
        receipt = new Receipt();
        for (CartItem cartItem : cartItems) {
            createPurchaseProductDto(cartItem.getName(), cartItem.getQuantity());
        }
        result();
        return receipt;
    }

    private void result() {
        receipt.ResultReceipt();
        membershipDiscount();
        receipt.resultPay();
        updateProductQuantity(receipt.getPurchaseProducts());
    }

    private void updateProductQuantity(List<PurchaseProduct> purchaseProducts) {
        for (PurchaseProduct purchaseProduct : purchaseProducts) {
            Product product = storeRepository.findProduct(purchaseProduct.getName(), purchaseProduct.getPromotion());
            product.updateQuantity(purchaseProduct.getQuantity());
        }
    }

    private void createPurchaseProductDto(String productName, int requestedQuantity) {
        List<Product> products = productRepository.findProductsByName(productName);
        ProductValidator.validateStockAvailability(products, requestedQuantity);

        int remainingQuantity = requestedQuantity;

        for (Product product : products) {
            remainingQuantity = processProduct(product, remainingQuantity);
            if (!alertNonPromotionQuantity(product, remainingQuantity, requestedQuantity)) {
                break;
            }
        }
    }

    private void membershipDiscount() {
        try {
            String input = inputView.membershipDiscountView();
            ProductValidator.validateInputYesOrNo(input);
            if (input.equals("Y")) {
                receipt.resultMembershipDiscount();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            membershipDiscount();
        }
    }

    private int processProduct(Product product, int requestQuantity) {
        int remainingQuantity = applyPromotionIfNeeded(product, requestQuantity);
        int purchaseQuantity = calculatePurchaseQuantity(product, remainingQuantity);
        addProductToReceipt(product, purchaseQuantity);
        return remainingQuantity - purchaseQuantity;
    }

    private int applyPromotionIfNeeded(Product product, int requestQuantity) {
        Promotion promotion = product.getPromotion();
        int productQuantity = product.getQuantity();

        if (promotion != null) {
            requestQuantity = applyPromotionNeed(product, promotion, requestQuantity, productQuantity);
            receipt.addGiveAway(promotion.calculateGiveAway(product, Math.min(requestQuantity, productQuantity),
                    receipt.getPurchaseAmount()));
        }
        return requestQuantity;
    }

    private int calculatePurchaseQuantity(Product product, int requestQuantity) {
        return Math.min(product.getQuantity(), requestQuantity);
    }

    private void addProductToReceipt(Product product, int purchaseQuantity) {
        Promotion promotion = product.getPromotion();
        receipt.addPurchaseProducts(
                new PurchaseProduct(product.getName(), purchaseQuantity, product.getPrice(), promotion));
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
        String input = getValidatedInput(product);
        return inputYesOrNo(input, requestQuantity);
    }

    private String getValidatedInput(Product product) {
        try {
            String input = inputView.addGiveAway(product.getName());
            ProductValidator.validateInputYesOrNo(input);
            return input;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getValidatedInput(product);
        }
    }

    private int inputYesOrNo(String input, int requestQuantity) {
        if (input.equals("Y")) {
            return requestQuantity + 1;
        }
        return requestQuantity;
    }

    private boolean alertNonPromotionQuantity(Product product, int remainingQuantity, int requestedQuantity) {
        int nonPromotionQuantity = 0;
        if (product.getPromotion() != null && remainingQuantity > 0) {
            nonPromotionQuantity = remainingQuantity + (product.getQuantity() % (product.getPromotion().getGet()
                    + product.getPromotion().getBuy()));
        }
        if (nonPromotionQuantity > 0 && !retryPromotionQuantity(product, nonPromotionQuantity)) {
            receipt.getPurchaseProducts().getLast().updateQuantity(requestedQuantity - nonPromotionQuantity);
            return false;
        }
        return true;
    }

    private boolean retryPromotionQuantity(Product product, int nonPromotionQuantity) {
        try {
            String input = inputView.nonPromotion(product.getName(), nonPromotionQuantity);
            ProductValidator.validateInputYesOrNo(input);
            return !input.equals("N");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return retryPromotionQuantity(product, nonPromotionQuantity);
        }
    }
}
