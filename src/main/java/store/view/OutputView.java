package store.view;

import store.message.OutputMessage;

import java.util.List;

public class OutputView {
    public void welcomeStoreView(){
        System.out.println(OutputMessage.WELCOME.getMessage());
        System.out.println(OutputMessage.INVENTORY.getMessage());
    }

    public void productsView(List<String> productDisplays){
        productDisplays.forEach(System.out::println);
        System.out.println(OutputMessage.PURCHASE_PROMPT.getMessage());
    }

}
