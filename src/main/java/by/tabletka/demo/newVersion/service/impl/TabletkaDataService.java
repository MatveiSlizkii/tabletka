package by.tabletka.demo.newVersion.service.impl;

import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.service.DataService;
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
    public List<Medicine> addData(String nameMedicine) {
        List<Medicine> medicineList = new ArrayList<>();
        String rawResponse = "";
        // Создаем URL для получения данных таблицы
        URL obj = null;
        try {
            obj = new URL("https://tabletka.by/search?request=" + nameMedicine.replace(" ", "%C2%A0"));
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
            System.out.println("GET request did not work.");
        }
        //System.out.println(rawResponse);
        int index = rawResponse.indexOf("class=\"tbody-base-tbl\"");
        if (index != -1){
            rawResponse = rawResponse.substring(index);
            int indexEnd = rawResponse.indexOf("</table>");
            rawResponse = rawResponse.substring(0, indexEnd);
            // System.out.println(rawResponse);




            while (rawResponse.indexOf("<div class=\"tooltip-info-header\">") != -1) {
                rawResponse = rawResponse.substring(rawResponse.indexOf("<div class=\"tooltip-info-header\">") + 34, rawResponse.length());
                rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
                String name = rawResponse.substring(1, rawResponse.indexOf("<"));
                //rawResponse = rawResponse.substring(1, rawResponse.indexOf("<"));
                System.out.println(name);
                rawResponse = rawResponse.substring(rawResponse.indexOf("<div class=\"tooltip-info-header\">") + 34, rawResponse.length());
                rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
                String completeness = rawResponse.substring(1, rawResponse.indexOf("<"));
                System.out.println(completeness);
                rawResponse = rawResponse.substring(rawResponse.indexOf("<div class=\"tooltip-info-header\">") + 34, rawResponse.length());
                rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
                String regex = "\\s{2,}";
                String rawFabricator = rawResponse.substring(1, rawResponse.indexOf("<")).replaceAll(regex, " ");
                String fabricator = rawFabricator.substring(1, rawFabricator.length() - 1);
                System.out.println(fabricator);
                rawResponse = rawResponse.substring(rawResponse.indexOf("<span class=\"capture\"><span>") + 29, rawResponse.length());
                String countryFabricator = rawResponse.substring(0, rawResponse.indexOf("<")).replaceAll(regex, " ");
                System.out.println(countryFabricator);
//        rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
                rawResponse = rawResponse.substring(rawResponse.indexOf("<span class=\"price-value\">") + 26, rawResponse.length());
                String prices = rawResponse.substring(0, rawResponse.indexOf(" р.</span>")).replaceAll(regex, " ");
                String[] values = prices.split(" ... ");
                String min = "";
                String max = "";
                if (values.length == 2){
                    min = values[0].replace(".", ",");
                    max = values[1].replace(".", ",");
                } else {
                    min = values[0].replace(".", ",");
                    max = values[0].replace(".", ",");
                }

                System.out.println(min);
                System.out.println(max);
                System.out.println();
                System.out.println();
                Medicine medicine = Medicine.builder()
                        .name(name)
                        .completeness(completeness)
                        .fabricator(fabricator)
                        .countryFabricator(countryFabricator)
                        .maxPrice(max)
                        .minPrice(min)
                        .build();

                medicineList.add(medicine);

            }
        } else {
            Medicine emptyMedicine = Medicine.builder()
                    .name(nameMedicine)
                    .completeness("Нет в продаже")
                    .fabricator("Нет в продаже")
                    .countryFabricator("Нет в продаже")
                    .minPrice("Нет в продаже")
                    .maxPrice("Нет в продаже")
                    .build();
            medicineList.add(emptyMedicine);
        }




        return medicineList;
    }
}
