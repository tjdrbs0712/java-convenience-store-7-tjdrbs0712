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

    /**
     * 주문한 상품을 계산해주는 메서드
     *
     * @param cartItems 주문한 상품 리스트
     * @return 최종 결과 영수증
     */
    public Receipt calculateTotal(List<CartItem> cartItems) {
        receipt = new Receipt();
        for (CartItem cartItem : cartItems) {
            createPurchaseProductDto(cartItem.getName(), cartItem.getQuantity());
        }
        result();
        return receipt;
    }

    /**
     * 주문한 상품들의 최종 계산을 시작하는 메서드
     */
    private void result() {
        receipt.ResultReceipt();
        membershipDiscount();
        receipt.resultPay();
        updateProductQuantity(receipt.getPurchaseProducts());
    }

    /**
     * 주문 상품 재고 없데이드
     *
     * @param purchaseProducts 주문 목록
     */
    private void updateProductQuantity(List<PurchaseProduct> purchaseProducts) {
        for (PurchaseProduct purchaseProduct : purchaseProducts) {
            Product product = storeRepository.findProduct(purchaseProduct.getName(), purchaseProduct.getPromotion());
            product.updateQuantity(purchaseProduct.getQuantity());
        }
    }

    /**
     * 주문한 상품의 이름과 개수로 프로모션 상품과 일반 상품을 차례대로 계산
     *
     * @param productName       주문 상품
     * @param requestedQuantity 상품 개수
     */
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

    /**
     * 멤버십 할인 질문
     */
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

    /**
     * 프로모션 상품을 먼저 계산하고 남은 구매 개수는 일반 상품에서 계산하기 위한 메서드
     *
     * @param product         상품
     * @param requestQuantity 주문 상품 개수
     * @return
     */
    private int processProduct(Product product, int requestQuantity) {
        int remainingQuantity = applyPromotionIfNeeded(product, requestQuantity);
        int purchaseQuantity = calculatePurchaseQuantity(product, remainingQuantity);
        addProductToReceipt(product, purchaseQuantity);
        return remainingQuantity - purchaseQuantity;
    }

    /**
     * 현재 product가 프로모션 상품이라면 프로모션 계산
     *
     * @param product         현재 상품
     * @param requestQuantity 구매할 개수
     * @return 더 구매하야될 개수
     */
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

    /**
     * 구매 목록 클래스에 상품을 추가
     *
     * @param product          구매할 상품
     * @param purchaseQuantity 상품 개수
     * @return
     */
    private void addProductToReceipt(Product product, int purchaseQuantity) {
        Promotion promotion = product.getPromotion();
        receipt.addPurchaseProducts(
                new PurchaseProduct(product.getName(), purchaseQuantity, product.getPrice(), promotion));
    }

    /**
     * 구매 상품의 재고가 충분하다면 몇개의 프로모션 혜택을 받는지 계산하기 위한 메서드
     *
     * @param product         상품
     * @param promotion       프로모션
     * @param requestQuantity 구매할 개수
     * @param productQuantity 남은 재고
     * @return 구매할 재고
     */
    private int applyPromotionNeed(Product product, Promotion promotion, int requestQuantity, int productQuantity) {
        if (requestQuantity < productQuantity) {
            return applyPromotion(product, promotion, requestQuantity);
        }
        return requestQuantity;
    }

    /**
     * 실질적인 혜택 계산 메서드
     *
     * @param product         상품
     * @param promotion       프로모션
     * @param requestQuantity 구매할 개수
     */
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

    /**
     * 몇개의 상품을 혜택 없이 구매 해야되는지 계산 하는 메서드
     *
     * @param product           구매할 상품
     * @param remainingQuantity 구매할 상품의 개수
     * @param requestedQuantity 전체 구매할 상품의 개수
     * @return 프로모션 혜택 없이 계산을 할 건지 에 대한 값
     */
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

    /**
     * 프로모션 혜택을 받지 않고 상품을 구매할건지 입력 받는 메서드
     *
     * @param product              구매할 상품
     * @param nonPromotionQuantity 구매할 상품의 개수
     * @return
     */
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
