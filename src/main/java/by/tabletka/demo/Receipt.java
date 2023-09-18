package by.tabletka.demo;

public class Receipt {
    private String name;
    private String url;
    private String maxPrice;

    public Receipt(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                '}';
    }
}
