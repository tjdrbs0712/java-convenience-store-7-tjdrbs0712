package store.controller;

import static store.constant.FileConstant.PRODUCTS_FILE_PATH;
import static store.constant.FileConstant.PROMOTIONS_FILE_PATH;

import store.Service.OrderCalculatorService;
import store.Service.OrderService;
import store.Service.ProductService;
import store.domain.order.Cart;
import store.domain.receipt.Receipt;
import store.repository.ProductRepository;
import store.repository.StoreRepository;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final ProductService productService;
    private final OrderService orderService;
    private final OrderCalculatorService orderCalculatorService;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController() {
        this.productRepository = new ProductRepository();
        this.storeRepository = new StoreRepository();
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.productService = new ProductService(storeRepository, productRepository, outputView);
        this.orderService = new OrderService(productRepository, inputView, outputView);
        this.orderCalculatorService = new OrderCalculatorService(storeRepository, productRepository, inputView);
    }

    public void run() {
        loadInitialData();
        processPurchases();
    }

    /**
     * md 파일을 읽어 오는 메서드
     */
    private void loadInitialData() {
        productService.loadProducts(PROMOTIONS_FILE_PATH, PRODUCTS_FILE_PATH);
    }

    /**
     * 읽어온 데이터를 출력하는 메서드
     */
    private void processPurchases() {
        do {
            productService.displayProducts();
            Receipt receipt = inputCartItems();
            orderService.receiptView(receipt);
        } while (orderService.processOrderAndCheckRetry());
    }

    /**
     * 상품을 주문하는 메서드
     */
    private Receipt inputCartItems() {
        try {
            Cart cart = orderService.orderProducts();
            return orderCalculatorService.calculateTotal(cart.getCartItems());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return inputCartItems();
        }
    }

}
