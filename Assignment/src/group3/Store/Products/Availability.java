package group3.Store.Products;

public enum Availability {
    IN_STOCK,
    OUT_OF_STOCK;

    @Override
    public String toString() {
        return name().replaceAll("_", " ");
    }
}
