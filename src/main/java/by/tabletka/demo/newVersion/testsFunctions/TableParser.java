package by.tabletka.demo.newVersion.testsFunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class TableParser {

    public static void main(String[] args) throws IOException {
        // Создаем URL для получения данных таблицы
        URL obj = new URL("https://tabletka.by/search?request=Гропримед");

        // Получаем ответ от сервера
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
       // con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        String rawResponse = "";
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

// print result
            //System.out.println(response.toString());
            rawResponse = response.toString();
        } else {
            System.out.println("GET request did not work.");
        }
        //System.out.println(rawResponse);
        int index = rawResponse.indexOf("class=\"tbody-base-tbl\"");
        rawResponse = rawResponse.substring(index);
        int indexEnd = rawResponse.indexOf("</table>");
        rawResponse = rawResponse.substring(0, indexEnd);
       // System.out.println(rawResponse);


        while (rawResponse.indexOf("<div class=\"tooltip-info-header\">") != -1){
            rawResponse = rawResponse.substring(rawResponse.indexOf("<div class=\"tooltip-info-header\">") + 34, rawResponse.length());
            rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
            String name = rawResponse.substring(1, rawResponse.indexOf("<"));
            //rawResponse = rawResponse.substring(1, rawResponse.indexOf("<"));
            System.out.println(name);
            rawResponse = rawResponse.substring(rawResponse.indexOf("<div class=\"tooltip-info-header\">") + 34, rawResponse.length());
            rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
            String form = rawResponse.substring(1, rawResponse.indexOf("<"));
            System.out.println(form);
            rawResponse = rawResponse.substring(rawResponse.indexOf("<div class=\"tooltip-info-header\">") + 34, rawResponse.length());
            rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
            String regex = "\\s{2,}";
            String proiz = rawResponse.substring(1, rawResponse.indexOf("<")).replaceAll(regex, " ");
            String proiz1 = proiz.substring(1, proiz.length()-1);
            System.out.println(proiz1);
            rawResponse = rawResponse.substring(rawResponse.indexOf("<span class=\"capture\"><span>") + 29, rawResponse.length());
            String country = rawResponse.substring(0, rawResponse.indexOf("<")).replaceAll(regex, " ");
            System.out.println(country);
//        rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
            rawResponse = rawResponse.substring(rawResponse.indexOf("<span class=\"price-value\">") + 26, rawResponse.length());
            String prices = rawResponse.substring(0, rawResponse.indexOf(" р.</span>")).replaceAll(regex, " ");
            String[] values = prices.split(" ... ");
            String min = values[0];
            String max = values[1];
            System.out.println(min);
            System.out.println(max);



        }
        //System.out.println(rawResponse);


    }
}