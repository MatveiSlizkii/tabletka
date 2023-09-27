package by.tabletka.demo;

import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.models.Region;
import by.tabletka.demo.newVersion.models.TripleRawData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static by.tabletka.demo.newVersion.service.parseHandler.*;


public class TestNotFound {
    public static void main(String[] args) {
        Medicine rawMedicine = new Medicine();
        rawMedicine = Medicine.builder()
                .name("БИКАРД-ЛФ")
                .completeness("таблетки покрытые оболочкой 10мг N50")
                .fabricator("Лекфарм")
                .countryFabricator("Беларусь")
                .defaultURL("https://tabletka.by/search?request=БИКАРД-ЛФ")
                .build();

        Region region = Region.ALL_REGIONS;

        String rawResponse = getRawResponseNotFound(rawMedicine, region);

        int index = rawResponse.indexOf("class=\"table-border\"");

        rawResponse = rawResponse.substring(index);
        int indexEnd = rawResponse.indexOf("</table>");
        rawResponse = rawResponse.substring(0, indexEnd);



        //System.out.println(rawResponse);

        String m1 = "class=\"form\">            <span class=\"form-title\">";
        System.out.println(m1.length());

       // System.out.println(findName(rawResponse, rawMedicine));
        TripleRawData na = findNameNotFound(rawResponse,rawMedicine);
        rawResponse = na.getRawHTML();
        //System.out.println(rawResponse);

        TripleRawData co = findCompletenessNotFound(rawResponse, rawMedicine);
        System.out.println(co.getData());
        rawResponse = co.getRawHTML();
        //System.out.println(rawResponse);

        TripleRawData fa = findFabricatorNotFound(rawResponse, rawMedicine);
        System.out.println(fa.getData());
        rawResponse = fa.getRawHTML();
        //System.out.println(rawResponse);

        TripleRawData nfa = findCountryFabricatorNotFound(rawResponse, rawMedicine);
        System.out.println(nfa.getData());
        rawResponse = nfa.getRawHTML();
        //System.out.println(rawResponse);

        TripleRawData inf = findInfoNotFound(rawResponse, rawMedicine);
        System.out.println(inf.getData());
        rawResponse = inf.getRawHTML();
        //System.out.println(rawResponse);






    }



}
