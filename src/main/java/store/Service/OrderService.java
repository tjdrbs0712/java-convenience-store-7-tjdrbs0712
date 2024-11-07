package store.Service;

import store.domain.Product;
import store.view.InputView;

import java.util.List;

public class OrderService {
    private final InputView inputView;

    public OrderService(){
        inputView = new InputView();
    }

    public void OrderProducts(List<Product> products){
        inputView.purchaseProducts();
    }
}
