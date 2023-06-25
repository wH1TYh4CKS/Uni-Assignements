package group3.Store.Products;

import java.math.BigDecimal;

public class ProductInfo {
    private final String SKU;
    private final String name;
    private final String brand;
    private final String details;
    private final BigDecimal price;

    public ProductInfo(String SKU, String name, String brand, String price, String details) {
        this.SKU = SKU;
        this.name = name;
        this.brand = brand;
        this.price = new BigDecimal(price);
        this.details = details;
    }

    public String getSKU() {
        return SKU;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDetails() {
        return details;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getPriceText() {
        return "RM" + price;
    }
}
