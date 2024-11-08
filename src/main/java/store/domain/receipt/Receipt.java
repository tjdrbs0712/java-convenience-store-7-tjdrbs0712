package store.domain.receipt;

import java.util.List;

public class Receipt {
    
    private final List<PurchaseProduct> purchaseProducts;
    private final List<GiveAway> giveAway;
    private final PurchaseAmount purchaseAmount;

    public Receipt(List<PurchaseProduct> purchaseProducts, List<GiveAway> giveAway, PurchaseAmount purchaseAmount) {
        this.purchaseProducts = purchaseProducts;
        this.giveAway = giveAway;
        this.purchaseAmount = purchaseAmount;
    }

    public List<PurchaseProduct> getPurchaseProducts() {
        return purchaseProducts;
    }

    public List<GiveAway> getGiveAway() {
        return giveAway;
    }

    public PurchaseAmount getPurchaseAmount() {
        return purchaseAmount;
    }
}
