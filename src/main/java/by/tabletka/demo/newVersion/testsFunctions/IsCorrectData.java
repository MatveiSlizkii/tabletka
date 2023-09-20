package by.tabletka.demo.newVersion.testsFunctions;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IsCorrectData {
    public static void main(String[] args) throws IOException {
        String filePath = "table.xlsx";

        // Открываем файл
        FileInputStream inputStream = new FileInputStream(filePath);

        // Создаем объект рабочей книги
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        // Получаем первый лист
        XSSFSheet sheet = workbook.getSheetAt(0);
        // Итерируемся по строкам листа
        List<String> nameMed = new ArrayList<>();

        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            // Получаем строку
            XSSFRow row = sheet.getRow(rowNum);

            // Получаем ячейки в строке
            XSSFCell cellA = row.getCell(0);
            nameMed.add(cellA.getStringCellValue());
        }
        boolean isCorrect = true;
        List<String> incorrectData = new ArrayList<>();
        for (int i = 0; i < nameMed.size(); i++) {

            URL obj = new URL("https://tabletka.by/search?request=" + nameMed.get(i).replace(" ", "%C2%A0"));

            // Получаем ответ от сервера
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            String rawResponse = "";
            if (responseCode == HttpURLConnection.HTTP_OK) { // success

            } else {
                isCorrect = false;
                incorrectData.add(nameMed.get(i));
            }
        }
        System.out.println(isCorrect);

    }
}
