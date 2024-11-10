package store.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import store.domain.store.Promotion;
import store.repository.ProductRepository;
import store.repository.StoreRepository;
import store.util.ProductsViewUtil;
import store.view.OutputView;

public class ProductService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final OutputView outputView;

    public ProductService(StoreRepository storeRepository, ProductRepository productRepository, OutputView outputView) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.outputView = outputView;
    }

    public void loadProducts(String PromotionsFilePath, String ProductsFilePath) {
        try {
            Map<String, Promotion> promotions = productRepository.loadPromotions(PromotionsFilePath);
            productRepository.loadProducts(ProductsFilePath, promotions);
            storeRepository.StoreProductUpdate(productRepository.getProducts());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayProducts() {
        outputView.welcomeStoreView();
        List<String> productDisplays = ProductsViewUtil.ProductsViewFormatter(productRepository.getProducts());
        outputView.productsView(productDisplays);
    }

}
