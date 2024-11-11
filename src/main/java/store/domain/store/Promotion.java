package store.domain.store;

import store.domain.receipt.GiveAway;
import store.domain.receipt.PurchaseAmount;

public class Promotion {
    private String name;
    private int buy;
    private int get;
    private String startDate;
    private String endDate;

    public Promotion(String name, int buy, int get, String startDate, String endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public GiveAway calculateGiveAway(Product product, int requestedQuantity, PurchaseAmount purchaseAmount) {
        int totalItems = buy + get;
        int freeItems = requestedQuantity / totalItems;
        int promotionPrice = freeItems * product.getPrice();
        purchaseAmount.addNetPrice(product.getPrice() * freeItems * totalItems);

        if (freeItems == 0) {
            return null;
        }

        return new GiveAway(product.getName(), freeItems, promotionPrice);
    }
}
