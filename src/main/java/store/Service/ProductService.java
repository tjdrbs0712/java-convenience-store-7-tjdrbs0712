package store.Service;

import store.domain.Promotion;
import store.repository.ProductRepository;
import store.util.ProductsViewUtil;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ProductService {

    private final ProductRepository productRepository;
    private final OutputView outputView;

    public ProductService(ProductRepository productRepository, OutputView outputView){
        this.productRepository = productRepository;
        this.outputView = outputView;
    }

    public void loadProducts(String PromotionsFilePath, String ProductsFilePath) {
        try {
            Map<String, Promotion> promotions = productRepository.loadPromotions(PromotionsFilePath);
            productRepository.loadProducts(ProductsFilePath, promotions);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void displayProducts() {
        outputView.welcomeStoreView();
        List<String> productDisplays = ProductsViewUtil.ProductsViewFormatter(productRepository.getProductManager().getProducts());
        outputView.productsView(productDisplays);
    }

}
