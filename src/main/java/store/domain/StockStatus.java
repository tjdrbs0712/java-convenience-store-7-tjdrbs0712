package store.domain;

public enum StockStatus {
    IN_STOCK("개"),
    OUT_OF_STOCK("재고 없음");

    private final String status;

    StockStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
