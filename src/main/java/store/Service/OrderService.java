package store.Service;

import store.domain.order.Cart;
import store.domain.receipt.Receipt;
import store.parser.OrderParser;
import store.repository.ProductRepository;
import store.repository.StoreRepository;
import store.view.InputView;
import store.view.OutputView;

public class OrderService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InputView inputView;
    private final OutputView outputView;

    public OrderService(StoreRepository storeRepository, ProductRepository productRepository, InputView inputView,
                        OutputView outputView) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void orderProducts() {
        try {
            String order = inputView.purchaseProducts();
            Cart cart = OrderParser.orderParser(order);
            OrderCalculatorService orderCalculatorService = new OrderCalculatorService(storeRepository,
                    productRepository);
            orderCalculatorService.addCartItem(cart.getCartItems());
            Receipt receipt = orderCalculatorService.calculator(cart.getCartItems());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            orderProducts();
        }
    }


}
