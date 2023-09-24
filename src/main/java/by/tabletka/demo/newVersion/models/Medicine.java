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
    @Override
    public String toString() {
        return  "{"+name +
                ", " + completeness +
                ", " + fabricator +
                ", " + countryFabricator +
                ", " + prices +
                '}';
    }
}
