package store.controller;

import store.Service.ProductService;
import store.domain.Product;

import java.io.IOException;
import java.util.List;

import static store.constant.FileConstant.PRODUCTS_FILE_PATH;
import static store.constant.FileConstant.PROMOTIONS_FILE_PATH;

public class StoreController {

    private final ProductService productService;

    public StoreController(){
        this.productService = new ProductService();
    }

    public void run(){
        displayProducts();
    }

    private void displayProducts(){
        try {
            List<Product> products = productService.loadProducts(PROMOTIONS_FILE_PATH, PRODUCTS_FILE_PATH);
            productService.displayProducts(products);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
