package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.message.OutputMessage;

public class InputView {

    public String purchaseProducts() {
        System.out.println(OutputMessage.PURCHASE_PROMPT.getMessage());
        return Console.readLine();
    }

    public String addGiveAway(String productName) {
        System.out.printf((OutputMessage.ADD_GIVEAWAY.getMessage()) + "%n", productName);
        return Console.readLine();
    }

    public String nonPromotion(String productName, int quantity) {
        System.out.printf((OutputMessage.NON_PROMOTION_QUANTITY.getMessage()) + "%n", productName, quantity);
        return Console.readLine();
    }

}
