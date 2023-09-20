package by.tabletka.demo.newVersion.models;

import lombok.*;

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
    private String minPrice;
    private String maxPrice;
}
