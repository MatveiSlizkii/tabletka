package by.tabletka.demo.newVersion.service.impl;

import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.models.Region;
import by.tabletka.demo.newVersion.models.TripleRawData;
import by.tabletka.demo.newVersion.service.DataService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static by.tabletka.demo.newVersion.service.parseHandler.*;

@Service
public class TabletkaDataService implements DataService {


    @Override
    public Map<Boolean, List<String>> isCorrectData(List<String> nameMedicine) {

        boolean isCorrect = true;
        List<String> incorrectData = new ArrayList<>();

        for (int i = 0; i < nameMedicine.size(); i++) {

            URL obj = null;
            try {
                obj = new URL("https://tabletka.by/search?request=" + nameMedicine.get(i).replace(" ", "%C2%A0"));
            } catch (MalformedURLException e) {
                throw new RuntimeException("Ошибка при формировании ссылки");
            }

            // Получаем ответ от сервера
            HttpURLConnection con = null;
            int responseCode = 0;
            try {
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                responseCode = con.getResponseCode();
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при переходе по ссылке. Проверьте подключение к Интернету");
            }

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                System.out.println(i + 1 + " " + nameMedicine.get(i) + " Успешно проверено");

            } else {
                isCorrect = false;
                incorrectData.add(nameMedicine.get(i));
                System.out.println(i + 1 + " " + nameMedicine.get(i) + " Успешно проверено");

            }
        }
        Map<Boolean, List<String>> result = new HashMap<>();
        result.put(isCorrect, incorrectData);
        return result;
    }

    @Override
    public String createDefaultURL(String nameMedicine) {
        nameMedicine = nameMedicine.replaceAll("\\s{2,}", " ");

        if (nameMedicine.charAt(0) == ' ') nameMedicine = nameMedicine.substring(1, nameMedicine.length());
        if (nameMedicine.charAt(nameMedicine.length() - 1) == ' ')
            nameMedicine = nameMedicine.substring(0, nameMedicine.length() - 1);

        return "https://tabletka.by/search?request=" + nameMedicine.replace(" ", "%C2%A0");
    }

    @Override
    public Medicine addMainData(Medicine rawMedicine, Region region) {

        int indexRegion = 0;
        switch (region) {
            case ALL_REGIONS -> indexRegion = 0;
            case BREST -> indexRegion = 1;
            case VITEBSK -> indexRegion = 2;
            case GOMEL -> indexRegion = 3;
            case GRODNO -> indexRegion = 4;
            case MINSK -> indexRegion = 5;
            case MOGILEV -> indexRegion = 6;
            case MINSKAYA_OBL -> indexRegion = 7;

        }

        String rawResponse = getRawResponseNotFound(rawMedicine, region);

        if (rawResponse.indexOf("Препарат отсутствует в продаже ") != -1) {

            rawMedicine.getPrices().get(indexRegion).setMaxPrice("Отсутствует в продаже");
            rawMedicine.getPrices().get(indexRegion).setMinPrice("Отсутствует в продаже");

            int index = rawResponse.indexOf("class=\"table-border\"");

            rawResponse = rawResponse.substring(index);
            int indexEnd = rawResponse.indexOf("</table>");
            rawResponse = rawResponse.substring(0, indexEnd);

            String m1 = "<tr>        <td class=\"name tooltip-info\">            <div class=\"content-table\">      " +
                    "          <div class=\"text-wrap\" style=\"pointer-events: auto\">             " +
                    "       <div class=\"tooltip-info-header\">                        <a href=\"";
            while (rawResponse.indexOf(m1) != -1) {
                TripleRawData tripleRawDataName = findNameNotFound(rawResponse, rawMedicine);
                rawResponse = tripleRawDataName.getRawHTML();
                if (!tripleRawDataName.isFind()) continue;

                TripleRawData tripleRawDataCompleteness = findCompletenessNotFound(rawResponse, rawMedicine);
                rawResponse = tripleRawDataCompleteness.getRawHTML();
                if (!tripleRawDataCompleteness.isFind()) continue;

                TripleRawData tripleRawDataFabricator = findFabricatorNotFound(rawResponse, rawMedicine);
                rawResponse = tripleRawDataFabricator.getRawHTML();
                if (!tripleRawDataFabricator.isFind()) continue;

                TripleRawData tripleRawDataCountryFabricator = findCountryFabricatorNotFound(rawResponse, rawMedicine);
                rawResponse = tripleRawDataCountryFabricator.getRawHTML();
                if (!tripleRawDataCountryFabricator.isFind()) continue;

                TripleRawData tripleRawInfo = findInfoNotFound(rawResponse, rawMedicine);
                rawResponse = tripleRawInfo.getRawHTML();
                if (!tripleRawInfo.isFind()) continue;
                rawMedicine.getPrices().get(indexRegion).setMaxPrice(tripleRawInfo.getData());
                rawMedicine.getPrices().get(indexRegion).setMinPrice(tripleRawInfo.getData());

                break;
            }

        } else {


            int index = rawResponse.indexOf("class=\"tbody-base-tbl\"");
            rawMedicine.getPrices().get(indexRegion).setMaxPrice("Не найдено");
            rawMedicine.getPrices().get(indexRegion).setMinPrice("Не найдено");
            if (index == -1) {
                return rawMedicine;
            }
            rawResponse = rawResponse.substring(index);
            int indexEnd = rawResponse.indexOf("</table>");
            rawResponse = rawResponse.substring(0, indexEnd);

            // System.out.println(rawResponse);
            String m = "<td class=\"name tooltip-info\">            <div class=\"content-table\">             " +
                    "   <div class=\"text-wrap\">                    <div class=\"tooltip-info-header\">        " +
                    "                <a href=\"";
            while (rawResponse.indexOf(m) != -1) {
                TripleRawData tripleRawDataName = findName(rawResponse, rawMedicine);
                rawResponse = tripleRawDataName.getRawHTML();
                if (!tripleRawDataName.isFind()) continue;

                TripleRawData tripleRawDataCompleteness = findCompleteness(rawResponse, rawMedicine);
                rawResponse = tripleRawDataCompleteness.getRawHTML();
                if (!tripleRawDataCompleteness.isFind()) continue;

                TripleRawData tripleRawDataFabricator = findFabricator(rawResponse, rawMedicine);
                rawResponse = tripleRawDataFabricator.getRawHTML();
                if (!tripleRawDataFabricator.isFind()) continue;

                TripleRawData tripleRawDataCountryFabricator = findCountryFabricator(rawResponse, rawMedicine);
                rawResponse = tripleRawDataCountryFabricator.getRawHTML();
                if (!tripleRawDataCountryFabricator.isFind()) continue;

                TripleRawData tripleRawDataMaxPrice = findMaxPrice(rawResponse, rawMedicine);
                rawResponse = tripleRawDataMaxPrice.getRawHTML();
                if (!tripleRawDataMaxPrice.isFind()) continue;
                rawMedicine.getPrices().get(indexRegion).setMaxPrice(tripleRawDataMaxPrice.getData());

                TripleRawData tripleRawDataMinPrice = findMinPrice(rawResponse, rawMedicine);
                rawResponse = tripleRawDataMinPrice.getRawHTML();
                if (!tripleRawDataMinPrice.isFind()) continue;
                rawMedicine.getPrices().get(indexRegion).setMinPrice(tripleRawDataMinPrice.getData());
                break;
            }


        }


        return rawMedicine;
    }


}
