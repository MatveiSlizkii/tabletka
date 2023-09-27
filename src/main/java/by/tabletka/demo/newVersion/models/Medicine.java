package by.tabletka.demo.newVersion.models;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
    private String name;
    private String completeness;
    private String fabricator;
    private String countryFabricator;
    private List<WidePrice> prices;
    private String defaultURL;

    public Medicine(String name, String completeness, String fabricator,
                    String countryFabricator, List<WidePrice> prices) {
        this.name = name;
        this.completeness = completeness;
        this.fabricator = fabricator;
        this.countryFabricator = countryFabricator;
        this.prices = prices;
    }

    @Override
    public String toString() {
        return "{" + name +
                ", " + completeness +
                ", " + fabricator +
                ", " + countryFabricator +
                ", " + prices +
                '}';
    }
}
