package store.Service;

import store.domain.OrderCalculator;
import store.parser.OrderParser;
import store.repository.ProductRepository;
import store.view.InputView;
import store.view.OutputView;

import java.util.Map;

public class OrderService {
    private final ProductRepository productRepository;
    private final InputView inputView;
    private final OutputView outputView;

    public OrderService(ProductRepository productRepository, InputView inputView, OutputView outputView){
        this.productRepository = productRepository;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void orderProducts(){
        try {
            String order = inputView.purchaseProducts();
            Map<String, Integer> productDetails = OrderParser.orderParser(order);
            OrderCalculator orderCalculator = new OrderCalculator(productRepository.getProducts());
            orderCalculator.addPurchaseProduct(productDetails);

        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            orderProducts();
        }
    }


}
