package store.Service;

import java.util.List;
import store.domain.order.Cart;
import store.domain.receipt.Receipt;
import store.parser.OrderParser;
import store.repository.ProductRepository;
import store.repository.StoreRepository;
import store.util.ReceiptViewUtil;
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

    public boolean processOrderAndCheckRetry() {
        String input = inputView.retryPurchase();
        return input.equals("Y");
    }

    public void receiptView(Receipt receipt) {
        List<String> receiptDisplays = ReceiptViewUtil.formatReceipt(receipt);
        outputView.receiptView(receiptDisplays);
    }

    public Receipt orderProducts() {
        try {
            String order = inputView.purchaseProducts();
            Cart cart = OrderParser.orderParser(order);
            OrderCalculatorService orderCalculatorService = new OrderCalculatorService(storeRepository,
                    productRepository, inputView);
            orderCalculatorService.addCartItem(cart.getCartItems());
            return orderCalculatorService.calculateTotal(cart.getCartItems());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return orderProducts();
        }
    }
}
