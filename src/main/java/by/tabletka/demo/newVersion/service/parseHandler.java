package by.tabletka.demo.newVersion.service;

import by.tabletka.demo.TestNotFound;
import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.models.Region;
import by.tabletka.demo.newVersion.models.TripleRawData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class parseHandler {

    public String getRawResponse(Medicine rawMedicine, Region region) {

        String mainURL = rawMedicine.getDefaultURL();
        if (region != Region.ALL_REGIONS) {
            String regionURL = "";
            switch (region) {
                case BREST -> regionURL = "41";
                case VITEBSK -> regionURL = "19";
                case GOMEL -> regionURL = "36";
                case GRODNO -> regionURL = "38";
                case MINSK -> regionURL = "1001";
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
            return new by.tabletka.demo.newVersion.models.TripleRawData(false, rawHTML, "");
        }
        rawHTML = rawHTML.substring(rawHTML.indexOf(m) + 194, rawHTML.length());
        rawHTML = rawHTML.substring(rawHTML.indexOf(">"), rawHTML.length());
        String name = rawHTML.substring(1, rawHTML.indexOf("<")).replaceAll(regex, " ");
        if (name.charAt(0) == ' ') name = name.substring(1);
        if (name.charAt(name.length() - 1) == ' ') {
            name = name.substring(0, name.length() - 1);
        }
        if (!name.toLowerCase().contains(medicine.getName().toLowerCase())) {
            return new by.tabletka.demo.newVersion.models.TripleRawData(false, rawHTML, name);
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
        if (completeness.charAt(completeness.length() - 1) == ' ') {
            completeness = completeness.substring(0, completeness.length() - 1);
        }

        if (!completeness.contains(medicine.getCompleteness())) {
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
        if (fabricator.charAt(fabricator.length() - 1) == ' ') {
            fabricator = fabricator.substring(0, fabricator.length() - 1);
        }

        if (!fabricator.contains(medicine.getFabricator())) {
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

        if (!countryFabricator.contains(medicine.getCountryFabricator())) {
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
        return new TripleRawData(true, rawHTML, values[0].replace(".", ","));
    }

    public static TripleRawData findMaxPrice(String rawHTML, Medicine medicine) {
        if (rawHTML.indexOf("<span class=\"price-value\">") == -1) {
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

        return new TripleRawData(true, rawHTML, max);
    }
    public static String getRawResponseNotFound(Medicine rawMedicine, Region region) {

        String mainURL = rawMedicine.getDefaultURL();
        if (region != Region.ALL_REGIONS) {
            String regionURL = "";
            switch (region) {
                case BREST -> regionURL = "41";
                case VITEBSK -> regionURL = "19";
                case GOMEL -> regionURL = "36";
                case GRODNO -> regionURL = "38";
                case MINSK -> regionURL = "1001";
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
    public static TripleRawData findNameNotFound(String rawHTML, Medicine medicine) {
        String regex = "\\s{2,}";
        String m = "<tr>        <td class=\"name tooltip-info\">            <div class=\"content-table\">      " +
                "          <div class=\"text-wrap\" style=\"pointer-events: auto\">             " +
                "       <div class=\"tooltip-info-header\">                        <a href=\"";

        if (rawHTML.indexOf(m) == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        rawHTML = rawHTML.substring(rawHTML.indexOf(m) + 233, rawHTML.length());
        rawHTML = rawHTML.substring(rawHTML.indexOf(">"), rawHTML.length());
        String name = rawHTML.substring(1, rawHTML.indexOf("<")).replaceAll(regex, " ");
        if (name.charAt(0) == ' ') name = name.substring(1);
        if (name.charAt(name.length() - 1) == ' ') {
            name = name.substring(0, name.length() - 1);
        }
        if (!name.toLowerCase().contains(medicine.getName().toLowerCase())) {
            return new TripleRawData(false, rawHTML, name);
        }
        //System.out.println(name);
        return new TripleRawData(true, rawHTML, name);
    }
    public static TripleRawData findCompletenessNotFound(String rawHTML, Medicine medicine) {
        String regex = "\\s{2,}";
        String tab = "class=\"form\">            <span class=\"form-title\">";
        if (rawHTML.indexOf(tab) == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        rawHTML = rawHTML.substring(rawHTML.indexOf(tab) + tab.length(), rawHTML.length());
        //rawHTML = rawHTML.substring(rawHTML.indexOf(">"), rawHTML.length());
        String completeness = rawHTML.substring(1, rawHTML.indexOf("<"));
        completeness = completeness.replaceAll(regex, " ");
        if (completeness.charAt(0) == ' ') completeness = completeness.substring(1);
        if (completeness.charAt(completeness.length() - 1) == ' ') {
            completeness = completeness.substring(0, completeness.length() - 1);
        }

        if (!completeness.contains(medicine.getCompleteness())) {
            return new TripleRawData(false, rawHTML, completeness);
        }

        //System.out.println(completeness);
        return new TripleRawData(true, rawHTML, completeness);
    }
    public static TripleRawData findFabricatorNotFound(String rawHTML, Medicine medicine) {
        String regex = "\\s{2,}";

        String proizv = "</td>        <td class=\"produce\">            <div class=\"content-table\">     " +
                "           <div class=\"text-wrap\">                    <span>";

        if (rawHTML.indexOf(proizv) == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        rawHTML = rawHTML.substring(rawHTML.indexOf(proizv) + proizv.length(), rawHTML.length());
        //rawHTML = rawHTML.substring(rawHTML.indexOf(">"), rawHTML.length());
        String fabricator = rawHTML.substring(0, rawHTML.indexOf("<")).replaceAll(regex, " ");
        //String fabricator = rawFabricator.substring(1, rawFabricator.length() - 1);
        if (fabricator.charAt(0) == ' ') fabricator = fabricator.substring(1);
        if (fabricator.charAt(fabricator.length() - 1) == ' ') {
            fabricator = fabricator.substring(0, fabricator.length() - 1);
        }

        if (!fabricator.contains(medicine.getFabricator())) {
            return new TripleRawData(false, rawHTML, fabricator);
        }

        //System.out.println(fabricator);
        return new TripleRawData(true, rawHTML, fabricator);

    }
    public static TripleRawData findCountryFabricatorNotFound(String rawHTML, Medicine medicine) {
        String v ="</div>                <div class=\"text-wrap\">                    <span class=\"capture\">";
        if (rawHTML.indexOf(v) == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        String regex = "\\s{2,}";
        rawHTML = rawHTML.substring(rawHTML.indexOf(v) + v.length(), rawHTML.length());
        String countryFabricator = rawHTML.substring(0, rawHTML.indexOf("<")).replaceAll(regex, " ");

        if (!countryFabricator.contains(medicine.getCountryFabricator())) {
            return new TripleRawData(false, rawHTML, countryFabricator);
        }

        //System.out.println(countryFabricator);
        return new TripleRawData(true, rawHTML, countryFabricator);
    }

    public static TripleRawData findInfoNotFound(String rawHTML, Medicine medicine) {
        String v ="</span>                </div>            </div>        " +
                "    </td>        <td class=\"info\">            <div class=\"text-wrap\">";
        if (rawHTML.indexOf(v) == -1) {
            return new TripleRawData(false, rawHTML, "");
        }
        String regex = "\\s{2,}";
        rawHTML = rawHTML.substring(rawHTML.indexOf(v) + v.length(), rawHTML.length());
        String info = rawHTML.substring(0, rawHTML.indexOf("<")).replaceAll(regex, " ");

        if (info.charAt(0) == ' ') info = info.substring(1);
        if (info.charAt(info.length() - 1) == ' ') {
            info = info.substring(0, info.length() - 1);
        }

//        if (!info.contains(medicine.getCountryFabricator())) {
//            return new TripleRawData(false, rawHTML, info);
//        }

        //System.out.println(countryFabricator);
        return new TripleRawData(true, rawHTML, info);
    }
}
