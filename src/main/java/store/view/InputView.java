package store.view;

import store.message.OutputMessage;

public class InputView {

    public void purchaseProducts(){
        System.out.println(OutputMessage.PURCHASE_PROMPT.getMessage());
    }

}
