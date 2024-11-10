package store.view;

import java.util.List;
import store.message.OutputMessage;

public class OutputView {
    public void welcomeStoreView() {
        System.out.println(OutputMessage.WELCOME.getMessage());
        System.out.println(OutputMessage.INVENTORY.getMessage());
    }

    public void productsView(List<String> productDisplays) {
        productDisplays.forEach(System.out::println);
    }

}
