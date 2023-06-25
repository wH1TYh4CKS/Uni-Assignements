package group3.Store;

public enum UserType {
    ADMIN,
    DELIVERY_STAFF,
    CUSTOMER,
    ORDER_STATUS,
    ORDERS,
    PAYMENTS;

    public String getType() {
        return this.name();
    }
}
