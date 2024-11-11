package store.Service;

import java.util.List;
import store.domain.order.Cart;
import store.domain.receipt.Receipt;
import store.parser.OrderParser;
import store.repository.ProductRepository;
import store.util.ReceiptViewUtil;
import store.validation.ProductValidator;
import store.view.InputView;
import store.view.OutputView;

public class OrderService {

    private final ProductRepository productRepository;
    private final InputView inputView;
    private final OutputView outputView;

    public OrderService(ProductRepository productRepository, InputView inputView,
                        OutputView outputView) {
        this.productRepository = productRepository;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public boolean processOrderAndCheckRetry() {
        try {
            String input = inputView.retryPurchase();
            ProductValidator.validateInputYesOrNo(input);
            return input.equals("Y");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return processOrderAndCheckRetry();
        }
    }

    public void receiptView(Receipt receipt) {
        List<String> receiptDisplays = ReceiptViewUtil.formatReceipt(receipt);
        outputView.receiptView(receiptDisplays);
    }

    public Cart orderProducts() {
        try {
            String order = inputView.purchaseProducts();
            return OrderParser.orderParser(order, productRepository.getProducts());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return orderProducts();
        }
    }
}
