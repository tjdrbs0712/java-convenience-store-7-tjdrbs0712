package store.domain.store;

import store.domain.receipt.GiveAway;
import store.domain.receipt.PurchaseAmount;
import store.dto.PromotionDto;

public class Promotion {
    private String name;
    private int buy;
    private int get;
    private String startDate;
    private String endDate;

    public Promotion(PromotionDto promotionDto) {
        this.name = promotionDto.getName();
        this.buy = promotionDto.getBuy();
        this.get = promotionDto.getGet();
        this.startDate = promotionDto.getStartDate();
        this.endDate = promotionDto.getEndDate();
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
