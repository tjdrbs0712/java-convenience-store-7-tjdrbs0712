package store.controller;

import store.Service.OrderService;
import store.Service.ProductService;

import java.io.IOException;

import static store.constant.FileConstant.PRODUCTS_FILE_PATH;
import static store.constant.FileConstant.PROMOTIONS_FILE_PATH;

public class StoreController {

    private final ProductService productService;
    private final OrderService orderService;

    public StoreController(){
        this.productService = new ProductService();
        this.orderService = new OrderService();
    }

    public void run(){
        displayProducts();
    }

    private void displayProducts(){
        try {
            productService.loadProducts(PROMOTIONS_FILE_PATH, PRODUCTS_FILE_PATH);
            productService.displayProducts();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
