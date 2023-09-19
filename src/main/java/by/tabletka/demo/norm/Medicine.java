package by.tabletka.demo.norm;

import lombok.Builder;

@Builder
public class Medicine {
    private String name;
    private String completeness;
    private String dosage;
    private String url;
    private double minPrice;
    private double maxPrice;

    public Medicine(String name, String completeness, String dosage, String url, double minPrice, double maxPrice) {
        this.name = name;
        this.completeness = completeness;
        this.dosage = dosage;
        this.url = url;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompleteness() {
        return completeness;
    }

    public void setCompleteness(String completeness) {
        this.completeness = completeness;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return
                name  +
                        ", " + completeness  +
                        ", " + dosage  +
                        ", min=" + minPrice +
                        ", max=" + maxPrice +
                        ", " + url +
                        '}';
    }
}
