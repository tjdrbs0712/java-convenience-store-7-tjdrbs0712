package store.domain.receipt;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

    private final List<PurchaseProduct> purchaseProducts = new ArrayList<>();
    private final List<GiveAway> giveAway = new ArrayList<>();
    private PurchaseAmount purchaseAmount;

    public List<PurchaseProduct> getPurchaseProducts() {
        return purchaseProducts;
    }

    public List<GiveAway> getGiveAway() {
        return giveAway;
    }

    public PurchaseAmount getPurchaseAmount() {
        return purchaseAmount;
    }

    public void addPurchaseProducts(PurchaseProduct product) {
        purchaseProducts.add(product);
    }

    public void addGiveAway(GiveAway giveAway) {
        this.giveAway.add(giveAway);
    }

    public void resultPurchaseAmount(PurchaseAmount purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
}
