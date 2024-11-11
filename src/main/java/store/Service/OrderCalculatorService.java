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

        receipt.ResultReceipt();
        membershipDiscount();
        receipt.resultPay();
        System.out.println("총 " + receipt.getPurchaseAmount().getTotalPrice());
        System.out.println("행사 " + receipt.getPurchaseAmount().getPromotionDiscount());
        System.out.println("맴버 " + receipt.getPurchaseAmount().getMembershipDiscount());
        System.out.println("돈 " + receipt.getPurchaseAmount().getPay());

        updateProductQuantity(receipt.getPurchaseProducts());
        return receipt;
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
            alertNonPromotionQuantity(product, remainingQuantity);
        }
    }

    private void membershipDiscount() {
        String input = inputView.membershipDiscountView();
        ProductValidator.validateInputYesOrNo(input);
        if (input.equals("Y")) {
            receipt.resultMembershipDiscount();
        }
    }

    private int processProduct(Product product, int requestQuantity) {
        Promotion promotion = product.getPromotion();
        int productQuantity = product.getQuantity();

        if (promotion != null) {
            requestQuantity = applyPromotionNeed(product, promotion, requestQuantity, productQuantity);
            receipt.addGiveAway(promotion.calculateGiveAway(product, Math.min(requestQuantity, productQuantity),
                    receipt.getPurchaseAmount()));
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

    private void alertNonPromotionQuantity(Product product, int remainingQuantity) {
        int nonPromotionQuantity = 0;
        if (product.getPromotion() != null && remainingQuantity > 0) {
            nonPromotionQuantity = remainingQuantity + (product.getQuantity() % (product.getPromotion().getGet()
                    + product.getPromotion().getBuy()));
        }
        if (nonPromotionQuantity > 0) {
            retryPromotionQuantity(product, nonPromotionQuantity);
        }
    }

    private void retryPromotionQuantity(Product product, int nonPromotionQuantity) {
        try {
            String input = inputView.nonPromotion(product.getName(), nonPromotionQuantity);
            ProductValidator.validateInputYesOrNo(input);
            if (input.equals("N")) {
                receipt.deleteLastPurchaseProduct();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            retryPromotionQuantity(product, nonPromotionQuantity);
        }
    }
}
