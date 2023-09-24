package by.tabletka.demo.newVersion.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WidePrice {
    private Region nameRegion;
    private String minPrice;
    private String maxPrice;

    public WidePrice(Region nameRegion) {
        this.nameRegion = nameRegion;
    }

    @Override
    public String toString() {
        return "{" +
                "nameRegion=" + nameRegion +
                " =min:" + minPrice + '\'' +
                ", max:'" + maxPrice + '\'' +
                '}';
    }
}
