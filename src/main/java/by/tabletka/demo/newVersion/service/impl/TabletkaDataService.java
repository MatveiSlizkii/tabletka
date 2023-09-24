package by.tabletka.demo.newVersion.service.impl;

import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.models.Region;
import by.tabletka.demo.newVersion.service.DataService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

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

            } else {
                isCorrect = false;
                incorrectData.add(nameMedicine.get(i));
            }
        }
        Map<Boolean, List<String>> result = new HashMap<>();
        result.put(isCorrect, incorrectData);
        return result;
    }

    @Override
    public Medicine addMainData(Medicine rawMedicine, Region region) {

        int indexRegion = 0;
        switch (region){
            case ALL_REGIONS -> indexRegion = 0;
            case BREST -> indexRegion = 1;
            case VITEBSK -> indexRegion = 2;
            case GOMEL -> indexRegion = 3;
            case GRODNO -> indexRegion = 4;
            case MINSK -> indexRegion = 5;
            case MOGILEV -> indexRegion = 6;
            case MINSKAYA_OBL -> indexRegion = 7;

        }

        String rawResponse = getRawResponse(rawMedicine, region);
//System.out.println(rawResponse);
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
            if (!tripleRawDataName.isFind) continue;
            rawMedicine.setName(tripleRawDataName.getData());

            TripleRawData tripleRawDataCompleteness = findCompleteness(rawResponse, rawMedicine);
            rawResponse = tripleRawDataCompleteness.getRawHTML();
            if (!tripleRawDataCompleteness.isFind) continue;

            TripleRawData tripleRawDataFabricator = findFabricator(rawResponse, rawMedicine);
            rawResponse = tripleRawDataFabricator.getRawHTML();
            if (!tripleRawDataFabricator.isFind) continue;

            TripleRawData tripleRawDataCountryFabricator = findCountryFabricator(rawResponse, rawMedicine);
            rawResponse = tripleRawDataCountryFabricator.getRawHTML();
            if (!tripleRawDataCountryFabricator.isFind) continue;

            TripleRawData tripleRawDataMaxPrice = findMaxPrice(rawResponse, rawMedicine);
            rawResponse = tripleRawDataMaxPrice.getRawHTML();
            if (!tripleRawDataMaxPrice.isFind) continue;
            rawMedicine.getPrices().get(indexRegion).setMaxPrice(tripleRawDataMaxPrice.getData());

            TripleRawData tripleRawDataMinPrice = findMinPrice(rawResponse, rawMedicine);
            rawResponse = tripleRawDataMinPrice.getRawHTML();
            if (!tripleRawDataMinPrice.isFind) continue;
            rawMedicine.getPrices().get(indexRegion).setMinPrice(tripleRawDataMinPrice.getData());


            break;
        }


        return rawMedicine;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
     static class TripleRawData {
        private boolean isFind;
        private String rawHTML;
        private String data;

    }
    public String getRawResponse (Medicine rawMedicine, Region region) {

        String mainURL ="https://tabletka.by/search?request=" + rawMedicine.getName().replace(" ", "%C2%A0");
        if (region != Region.ALL_REGIONS){
            String regionURL ="";
            switch (region){
                case BREST -> regionURL = "41";
                case VITEBSK -> regionURL = "19";
                case GOMEL -> regionURL = "36";
                case GRODNO -> regionURL = "38";
                case MINSK -> regionURL =  "1001";
                case MOGILEV -> regionURL = "40";
                case MINSKAYA_OBL -> regionURL = "1008";
            }
            mainURL = mainURL + "&region=" + regionURL;
        }

        String rawResponse = "";
        // Создаем URL для получения данных таблицы
        URL obj = null;
        try {
            obj = new URL(mainURL);
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
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException("Программа не смогла вытянуть HTML ");
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            while (true) {
                try {
                    if (!((inputLine = in.readLine()) != null)) break;
                } catch (IOException e) {
                    throw new RuntimeException("Вытянуло HTML но не смогла преобразовать");
                }
                response.append(inputLine);
            }
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException("Не смог закрыть задачу по начальной обработке HTML");
            }

            //System.out.println(response.toString());
            rawResponse = response.toString();
        } else {
            throw new RuntimeException("GET request did not work.");
        }

        return rawResponse;
    }

    public static TripleRawData findName(String rawHTML, Medicine medicine) {
        String regex = "\\s{2,}";
        String m = "<td class=\"name tooltip-info\">            <div class=\"content-table\">             " +
                "   <div class=\"text-wrap\">                    <div class=\"tooltip-info-header\">        " +
                "                <a href=\"";

        if (rawHTML.indexOf(m) == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        rawHTML = rawHTML.substring(rawHTML.indexOf(m) + 194, rawHTML.length());
        rawHTML = rawHTML.substring(rawHTML.indexOf(">"), rawHTML.length());
        String name = rawHTML.substring(1, rawHTML.indexOf("<")).replaceAll(regex, " ");
        if (name.charAt(0) == ' ') name = name.substring(1);
        if (name.charAt(name.length()-1) == ' ') {
            name = name.substring(0, name.length()-1);
        }
        //System.out.println(name);
        return new TripleRawData(true, rawHTML, name);
    }

    public static TripleRawData findCompleteness(String rawHTML, Medicine medicine) {
        String regex = "\\s{2,}";
        String tab = "</span>                " +
                "</div>            </div>        </td>        <td class=\"form tooltip-info\">   " +
                "         <div class=\"content-table\">                <div class=\"text-wrap\">        " +
                "        \t<div class=\"tooltip-info-header\">                        <a href=";
        if (rawHTML.indexOf(tab) == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        rawHTML = rawHTML.substring(rawHTML.indexOf(tab) + tab.length(), rawHTML.length());
        rawHTML = rawHTML.substring(rawHTML.indexOf(">"), rawHTML.length());
        String completeness = rawHTML.substring(1, rawHTML.indexOf("<")).replaceAll(regex, " ");
        if (completeness.charAt(0) == ' ') completeness = completeness.substring(1);
        if (completeness.charAt(completeness.length()-1) == ' ') {
            completeness = completeness.substring(0, completeness.length()-1);
        }

        if (!completeness.contains(medicine.getCompleteness())){
            return new TripleRawData(false, rawHTML, completeness);
        }

        //System.out.println(completeness);
        return new TripleRawData(true, rawHTML, completeness);
    }

    public static TripleRawData findFabricator(String rawHTML, Medicine medicine) {
        String regex = "\\s{2,}";

        String proizv = "<td class=\"produce tooltip-info\">           " +
                " <div class=\"content-table\">                " +
                "<div class=\"text-wrap\">                   " +
                " <div class=\"tooltip-info-header\">         " +
                "                   <a href=\"";

        if (rawHTML.indexOf(proizv) == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        rawHTML = rawHTML.substring(rawHTML.indexOf(proizv) + 201, rawHTML.length());
        rawHTML = rawHTML.substring(rawHTML.indexOf(">"), rawHTML.length());
        String rawFabricator = rawHTML.substring(1, rawHTML.indexOf("<")).replaceAll(regex, " ");
        String fabricator = rawFabricator.substring(1, rawFabricator.length() - 1);
        if (fabricator.charAt(0) == ' ') fabricator = fabricator.substring(1);
        if (fabricator.charAt(fabricator.length()-1) == ' ') {
            fabricator = fabricator.substring(0, fabricator.length()-1);
        }

        if (!fabricator.contains(medicine.getFabricator())){
            return new TripleRawData(false, rawHTML, fabricator);
        }

        //System.out.println(fabricator);
        return new TripleRawData(true, rawHTML, fabricator);

    }

    public static TripleRawData findCountryFabricator(String rawHTML, Medicine medicine) {
        if (rawHTML.indexOf("<span class=\"capture\"><span>") == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        String regex = "\\s{2,}";
        rawHTML = rawHTML.substring(rawHTML.indexOf("<span class=\"capture\"><span>") + 29, rawHTML.length());
        String countryFabricator = rawHTML.substring(0, rawHTML.indexOf("<")).replaceAll(regex, " ");

        if (!countryFabricator.contains(medicine.getCountryFabricator())){
            return new TripleRawData(false, rawHTML, countryFabricator);
        }

        //System.out.println(countryFabricator);
        return new TripleRawData(true, rawHTML, countryFabricator);
    }
    public static TripleRawData findMinPrice(String rawHTML, Medicine medicine) {
        String regex = "\\s{2,}";
        //rawHTML = rawHTML.substring(rawHTML.indexOf("<span class=\"price-value\">") + 26, rawHTML.length());
        String prices = rawHTML.substring(0, rawHTML.indexOf(" р.</span>")).replaceAll(regex, " ");
        String[] values = prices.split(" ... ");
        //System.out.println(values[0].replace(".", ","));
        return new TripleRawData(true, rawHTML,values[0].replace(".", ","));
    }
    public static TripleRawData findMaxPrice(String rawHTML, Medicine medicine) {
        if (rawHTML.indexOf("<span class=\"price-value\">") == -1){
            return new TripleRawData(false, rawHTML, "");
        }

        String regex = "\\s{2,}";
        rawHTML = rawHTML.substring(rawHTML.indexOf("<span class=\"price-value\">") + 26, rawHTML.length());
        String prices = rawHTML.substring(0, rawHTML.indexOf(" р.</span>")).replaceAll(regex, " ");
        String[] values = prices.split(" ... ");

        String max = "";
        if (values.length == 2) {
            max = values[1].replace(".", ",");
        } else {
            max = values[0].replace(".", ",");
        }
        //System.out.println(max);

        return new TripleRawData(true, rawHTML,max);
    }

    @Override
    public Medicine addDataRegion(Medicine rawMedicine) {





        return null;
    }
}
