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

    /**
     * 프로모션 상품의 값을 계산하기 위한 메서드
     *
     * @param product           구매한 상품
     * @param requestedQuantity 구매 갯수
     * @param purchaseAmount    구매 가격 계산 값을 저장 위한 클래스
     * @return
     */
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
