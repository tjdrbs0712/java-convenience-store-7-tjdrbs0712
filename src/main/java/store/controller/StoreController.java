package store.controller;

import store.Service.OrderService;
import store.Service.ProductService;
import store.repository.ProductRepository;
import store.view.InputView;
import store.view.OutputView;

import static store.constant.FileConstant.PRODUCTS_FILE_PATH;
import static store.constant.FileConstant.PROMOTIONS_FILE_PATH;

public class StoreController {

    private final ProductService productService;
    private final OrderService orderService;
    private final ProductRepository productRepository = new ProductRepository();
    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();

    public StoreController(){
        this.productService = new ProductService(productRepository, outputView);
        this.orderService = new OrderService(productRepository, inputView, outputView);
    }

    public void run(){
        displayProducts();
        orderService.orderProducts();
    }

    private void displayProducts(){
        productService.loadProducts(PROMOTIONS_FILE_PATH, PRODUCTS_FILE_PATH);
        productService.displayProducts();
    }

}
