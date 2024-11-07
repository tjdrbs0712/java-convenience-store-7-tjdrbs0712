package store.Service;

import store.domain.Product;
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

    public ProductService(){
        this.productRepository = new ProductRepository();
        this.outputView = new OutputView();
    }


    public List<Product> loadProducts(String PromotionsFilePath, String ProductsFilePath) throws IOException {
        Map<String, Promotion> promotions = productRepository.loadPromotions(PromotionsFilePath);
        return productRepository.loadProducts(ProductsFilePath, promotions);
    }

    public void displayProducts(List<Product> products) {
        outputView.welcomeStoreView();
        List<String> productDisplays = ProductsViewUtil.ProductsViewFormatter(products);
        outputView.productsView(productDisplays);
    }

}
