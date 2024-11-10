package store.Service;

import static store.repository.StoreRepository.PROMOTION_NAME;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import store.domain.order.CartItem;
import store.domain.receipt.GiveAway;
import store.domain.receipt.PurchaseProduct;
import store.domain.receipt.Receipt;
import store.domain.store.Product;
import store.domain.store.Promotion;
import store.dto.PurchaseProductDto;
import store.error.InputException;
import store.message.ErrorMessage;
import store.repository.ProductRepository;
import store.repository.StoreRepository;
import store.validation.ProductValidator;

public class OrderCalculatorService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final List<PurchaseProductDto> purchaseProductDtos = new ArrayList<>();
    private final Receipt receipt = new Receipt();

    public OrderCalculatorService(StoreRepository storeRepository, ProductRepository productRepository) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    public void addCartItem(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            ProductValidator.validateProductContains(productRepository.getProducts(), cartItem.getName());
        }
    }

    public Receipt calculator(List<CartItem> cartItems) {

        for (CartItem cartItem : cartItems) {
            String productName = cartItem.getName();
            int quantity = cartItem.getQuantity();

            //어떤 상품을 몇개 구매했는지 계산
            purchaseProductDtos.add(createPurchaseProductDto(productName, quantity));
        }
        addPurchaseProduct(cartItems);
        updateProducts(purchaseProductDtos);
        for (PurchaseProductDto purchaseProductDto : purchaseProductDtos) {
            System.out.println(purchaseProductDto.getName() + " = " + purchaseProductDto.getQuantity());
        }
        return null;
    }

    // 구매한 상품의 갯수만큼 차감
    private void updateProducts(List<PurchaseProductDto> purchaseProductDtos) {
        for (PurchaseProductDto purchaseProductDto : purchaseProductDtos) {
            Product product = storeRepository.findProduct(purchaseProductDto.getName(),
                    purchaseProductDto.getPromotion());
            product.updateQuantity(purchaseProductDto.getQuantity());
        }
    }

    //구매한 상품 목록 = 영수증
    private void addPurchaseProduct(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product product = storeRepository.getProducts().get(cartItem.getName() + PROMOTION_NAME);

            int totalPrice = product.getPrice() * cartItem.getQuantity();
            receipt.addPurchaseProducts(new PurchaseProduct(cartItem.getName(), cartItem.getQuantity(), totalPrice));
        }
    }

    private PurchaseProductDto createPurchaseProductDto(String productName, int requestQuantity) {
        List<Product> matchingProducts = findProductsByName(productName);
        int availableQuantity = matchingProducts.stream().mapToInt(Product::getQuantity).sum();

        if (availableQuantity < requestQuantity) {
            throw new InputException(ErrorMessage.EXCEEDS_STOCK); // 재고 부족 예외
        }

        for (Product product : matchingProducts) {
            int productQuantity = product.getQuantity();
            Promotion promotion = product.getPromotion();

            if (promotion != null) {
                if (productQuantity >= requestQuantity) {
                    requestQuantity = applyPromotion(product, requestQuantity);
                }
                addGiveProduct(product, requestQuantity);
            }

            if (productQuantity >= requestQuantity) {
                return new PurchaseProductDto(product.getName(), requestQuantity, product.getPrice(), promotion);
            }
            requestQuantity -= productQuantity;
            purchaseProductDtos.add(
                    new PurchaseProductDto(product.getName(), productQuantity, product.getPrice(), promotion));
        }

        throw new InputException(ErrorMessage.INVALID_INPUT);
    }

    private int applyPromotion(Product product, int requestQuantity) {
        Promotion promotion = product.getPromotion();
        int buy = promotion.getBuy();
        int promotionQuantity = buy + 1;

        if (requestQuantity % promotionQuantity == buy) {
            System.out.println("현재 " + product.getName() + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
            String response = Console.readLine();
            if ("Y".equalsIgnoreCase(response)) {
                requestQuantity++;
            }
        }
        return requestQuantity;
    }


    private List<Product> findProductsByName(String productName) {
        return productRepository.getProducts().stream()
                .filter(product -> product.getName().equals(productName))
                .sorted(Comparator.comparing(product -> product.getPromotion() == null))
                .toList();
    }

    private void addGiveProduct(Product product, int requestQuantity) {
        Promotion promotion = product.getPromotion();
        int buyQuantity = promotion.getBuy();
        int freeItems = requestQuantity / (buyQuantity + 1); // 증정품 수량 계산
        receipt.addGiveAway(new GiveAway(product.getName(), requestQuantity + freeItems));
    }

}
