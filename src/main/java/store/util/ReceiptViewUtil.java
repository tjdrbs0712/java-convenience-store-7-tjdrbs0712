package store.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import store.domain.receipt.GiveAway;
import store.domain.receipt.PurchaseProduct;
import store.domain.receipt.PurchaseProductSummary;
import store.domain.receipt.Receipt;
import store.message.OutputMessage;

public class ReceiptViewUtil {

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###");
    private static final String STRING_PRODUCT_FORMAT = "%-10s\t%-5s\t%-10s";
    private static final String STRING_PRESENT_FORMAT = "%-10s\t%-5s";
    private static final String STRING_PRICE_FORMAT = "%-10s\t\t\t%-10s";

    public static List<String> formatReceipt(Receipt receipt) {
        List<PurchaseProductSummary> productSummaries = aggregateProductSummaries(receipt.getPurchaseProducts());

        List<String> receiptDisplays = new ArrayList<>();
        addHeader(receiptDisplays);
        addProductSummaries(receiptDisplays, productSummaries);
        addGiveAways(receiptDisplays, receipt.getGiveAway());
        addTotals(receiptDisplays, receipt);

        return receiptDisplays;
    }

    private static void addHeader(List<String> receiptDisplays) {
        receiptDisplays.add(OutputMessage.STORE.getMessage());
        receiptDisplays.add(String.format(STRING_PRODUCT_FORMAT, "상품명", "수량", "금액"));
    }

    private static List<PurchaseProductSummary> aggregateProductSummaries(List<PurchaseProduct> purchaseProducts) {
        List<PurchaseProductSummary> productSummaries = new ArrayList<>();
        for (PurchaseProduct product : purchaseProducts) {
            addOrUpdateProductSummary(productSummaries, product);
        }
        return productSummaries;
    }

    private static void addProductSummaries(List<String> receiptDisplays,
                                            List<PurchaseProductSummary> productSummaries) {
        for (PurchaseProductSummary summary : productSummaries) {
            receiptDisplays.add(String.format(STRING_PRODUCT_FORMAT, summary.getName(), summary.getQuantity(),
                    summary.getTotalPrice()));
        }
    }

    private static void addGiveAways(List<String> receiptDisplays, List<GiveAway> giveAways) {
        if (giveAways.isEmpty()) {
            return;
        }
        receiptDisplays.add(OutputMessage.PRESENTATION.getMessage());
        for (GiveAway giveAway : giveAways) {
            receiptDisplays.add(String.format(STRING_PRESENT_FORMAT, giveAway.getName(), giveAway.getQuantity()));
        }
    }

    private static void addOrUpdateProductSummary(List<PurchaseProductSummary> summaries, PurchaseProduct product) {
        PurchaseProductSummary existingSummary = findProductSummary(summaries, product.getName());

        if (existingSummary != null) {
            existingSummary.addQuantity(product.getQuantity());
            existingSummary.addTotalPrice(product.getPrice() * product.getQuantity());
        } else {
            summaries.add(new PurchaseProductSummary(product.getName(), product.getQuantity(),
                    product.getPrice() * product.getQuantity()));
        }
    }

    private static PurchaseProductSummary findProductSummary(List<PurchaseProductSummary> summaries,
                                                             String productName) {
        for (PurchaseProductSummary summary : summaries) {
            if (summary.getName().equals(productName)) {
                return summary;
            }
        }
        return null;
    }

    private static void addTotals(List<String> receiptDisplays, Receipt receipt) {
        receiptDisplays.add(OutputMessage.EQUAL_SIGN.getMessage());
        receiptDisplays.add(formatTotalPrice(receipt));
        receiptDisplays.add(formatPromotionDiscount(receipt));
        receiptDisplays.add(formatMembershipDiscount(receipt));
        receiptDisplays.add(formatPayAmount(receipt));
    }

    private static String formatTotalPrice(Receipt receipt) {
        int totalQuantity = receipt.getPurchaseProducts().stream().mapToInt(PurchaseProduct::getQuantity).sum();
        String formattedTotalPrice = PRICE_FORMAT.format(receipt.getPurchaseAmount().getTotalPrice());
        return String.format(STRING_PRODUCT_FORMAT, OutputMessage.TOTAL_PRICE.getMessage(), totalQuantity,
                formattedTotalPrice);
    }

    private static String formatPromotionDiscount(Receipt receipt) {
        String formattedPromotionDiscount = "-" + PRICE_FORMAT.format(
                receipt.getPurchaseAmount().getPromotionDiscount());
        return String.format(STRING_PRICE_FORMAT, OutputMessage.PROMOTION_DISCOUNT.getMessage(),
                formattedPromotionDiscount);
    }

    private static String formatMembershipDiscount(Receipt receipt) {
        String formattedMembershipDiscount = "-" + PRICE_FORMAT.format(
                receipt.getPurchaseAmount().getMembershipDiscount());
        return String.format(STRING_PRICE_FORMAT, OutputMessage.MEMBERSHIP_DISCOUNT_PRICE.getMessage(),
                formattedMembershipDiscount);
    }

    private static String formatPayAmount(Receipt receipt) {
        String formattedPayAmount = PRICE_FORMAT.format(receipt.getPurchaseAmount().getPay());
        return String.format(STRING_PRICE_FORMAT, OutputMessage.PAY.getMessage(), formattedPayAmount);
    }
}