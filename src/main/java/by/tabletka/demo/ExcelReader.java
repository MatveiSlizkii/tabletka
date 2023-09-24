package by.tabletka.demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.tabletka.demo.newVersion.models.Medicine;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    public static void main(String[] args) throws IOException {
        // Путь к Excel-файлу
        String filePath = "table.xlsx";

        // Открываем файл
        FileInputStream inputStream = new FileInputStream(filePath);

        // Создаем объект рабочей книги
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        // Получаем первый лист
        XSSFSheet sheet = workbook.getSheetAt(0);
        // Итерируемся по строкам листа

        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum ++) {
            // Получаем строку
            XSSFRow row = sheet.getRow(rowNum);

            // Получаем ячейки в строке
            XSSFCell cellA = row.getCell(0);
            System.out.println(cellA.getStringCellValue());

        }
        // Закрываем файл
        inputStream.close();

    }
}