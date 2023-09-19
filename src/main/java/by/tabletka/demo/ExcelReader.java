package by.tabletka.demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.tabletka.demo.norm.Medicine;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    public static void main(String[] args) throws IOException {
        // Путь к Excel-файлу
        String filePath = "ЛЕКИ.xlsx";

        // Открываем файл
        FileInputStream inputStream = new FileInputStream(filePath);

        // Создаем объект рабочей книги
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        // Получаем первый лист
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<Medicine> medicineList = new ArrayList<>();
        // Итерируемся по строкам листа
        int j = 1;
        for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum ++) {
            // Получаем строку
            XSSFRow row = sheet.getRow(rowNum);

            // Получаем ячейки в строке
            XSSFCell cellA = row.getCell(0);
            XSSFCell cellB = row.getCell(1);
            XSSFCell cellC = row.getCell(2);
            XSSFCell cellD = row.getCell(3);

            String regex = "\\s{2,}";


            String name = "";


            if (cellA.getStringCellValue().isEmpty()) {
                name = medicineList.get(medicineList.size() - 1).getName();
            } else name = cellA.getStringCellValue();
            if (cellB.getStringCellValue().isEmpty() && cellC.getStringCellValue().isEmpty()) {
                continue;
            }
            Medicine medicine = Medicine.builder()
                    .name(name.replaceAll(regex, " "))
                    .completeness(cellB.getStringCellValue().replaceAll(regex, " "))
                    .dosage(cellC.getStringCellValue().replaceAll(regex, " "))
                    .url(cellD.getStringCellValue())
                    .build();
            medicineList.add(medicine);
            System.out.println(j + " " + medicine);
            j++;
            //medicineList.
        }
        // Закрываем файл
        inputStream.close();

    }
}