package by.tabletka.demo;

import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.models.Region;
import by.tabletka.demo.newVersion.service.impl.TabletkaDataService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TestExcel {
    public static void main(String[] args) throws IOException {
        TabletkaDataService dataService = new TabletkaDataService();
        Medicine medicine = new Medicine();
        medicine.setName("Линекс");
        medicine.setCompleteness("капсулы N16");
        medicine.setFabricator("Лек");
        medicine.setCountryFabricator("Словения");
       dataService.addMainData(medicine, Region.ALL_REGIONS);
       //Линекс	капсулы N16	Лек	Словения

//        String rawMedicine = "гроприносин";
//        String rawResponse = "";
//        // Создаем URL для получения данных таблицы
//        URL obj = null;
//        try {
//            obj = new URL("https://tabletka.by/search?request=" + rawMedicine.replace(" ", "%C2%A0"));
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Ошибка при формировании ссылки");
//        }
//
//        // Получаем ответ от сервера
//        HttpURLConnection con = null;
//        int responseCode = 0;
//        try {
//            con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("GET");
//            responseCode = con.getResponseCode();
//        } catch (IOException e) {
//            throw new RuntimeException("Ошибка при переходе по ссылке. Проверьте подключение к Интернету");
//        }
//
//        if (responseCode == HttpURLConnection.HTTP_OK) { // success
//            BufferedReader in = null;
//            try {
//                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            } catch (IOException e) {
//                throw new RuntimeException("Программа не смогла вытянуть HTML ");
//            }
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while (true) {
//                try {
//                    if (!((inputLine = in.readLine()) != null)) break;
//                } catch (IOException e) {
//                    throw new RuntimeException("Вытянуло HTML но не смогла преобразовать");
//                }
//                response.append(inputLine);
//            }
//            try {
//                in.close();
//            } catch (IOException e) {
//                throw new RuntimeException("Не смог закрыть задачу по начальной обработке HTML");
//            }
//
//            //System.out.println(response.toString());
//            rawResponse = response.toString();
//        } else {
//            System.out.println("GET request did not work.");
//        }
//        //System.out.println(rawResponse);
//        int index = rawResponse.indexOf("class=\"tbody-base-tbl\"");
//
//        rawResponse = rawResponse.substring(index);
//        int indexEnd = rawResponse.indexOf("</table>");
//        rawResponse = rawResponse.substring(0, indexEnd);
//
//
//
//        String m = "<td class=\"name tooltip-info\">            <div class=\"content-table\">             " +
//                "   <div class=\"text-wrap\">                    <div class=\"tooltip-info-header\">        " +
//                "                <a href=\"";
//
//        String proizv = "<td class=\"produce tooltip-info\">           " +
//                " <div class=\"content-table\">                " +
//                "<div class=\"text-wrap\">                   " +
//                " <div class=\"tooltip-info-header\">         " +
//                "                   <a href=\"";
//        String tab = "<span class=\"capture\">                                                   " +
//                " <a href=\"/search/mnn?mnn_id=633\">Инозин пранобекс                         " +
//                "   </a>                                            </span>                " +
//                "</div>            </div>        </td>        <td class=\"form tooltip-info\">   " +
//                "         <div class=\"content-table\">                <div class=\"text-wrap\">        " +
//                "        \t<div class=\"tooltip-info-header\">                        <a href=";
//
//        System.out.println(m.length());
//        System.out.println(rawResponse.contains(m));
//        index = rawResponse.indexOf(m);
//        rawResponse = rawResponse.substring(index+194);
//        rawResponse = rawResponse.substring(rawResponse.indexOf(">"), rawResponse.length());
//        String name = rawResponse.substring(1, rawResponse.indexOf("<"));
//        System.out.println(name);
//        System.out.println(tab.length());
//        index = rawResponse.indexOf(tab);
//        rawResponse = rawResponse.substring(index+457);
//
//
//        //        System.out.println(proizv.length());
////        index = rawResponse.indexOf(proizv);
////        rawResponse = rawResponse.substring(index+201);
//
//         System.out.println(rawResponse);

    }
}
