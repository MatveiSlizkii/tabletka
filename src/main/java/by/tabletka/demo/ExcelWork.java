package by.tabletka.demo;

import by.tabletka.demo.newVersion.models.Medicine;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelWork {
    public List<Medicine> excelReader(String pathToFile) throws IOException {

        String filePath = pathToFile;

        // Открываем файл
        FileInputStream inputStream = new FileInputStream(filePath);

        // Создаем объект рабочей книги
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        // Получаем первый лист
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<Medicine> medicineList = new ArrayList<>();
        // Итерируемся по строкам листа
        for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum++) {
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
//            Medicine medicine = Medicine.builder()
//                    .name(name.replaceAll(regex, " "))
//                    .completeness(cellB.getStringCellValue().replaceAll(regex, " "))
//                    .dosage(cellC.getStringCellValue().replaceAll(regex, " "))
//                    .url(cellD.getStringCellValue())
//                    .build();
//            medicineList.add(medicine);
            //medicineList.
        }
        // Закрываем файл
        inputStream.close();
        System.out.println("Считка данных с Excel файла завершена успешно");
        return medicineList;
    }

    public void createExcelTable(List<Medicine> medicineList) throws IOException {
        // Создаем книгу Excel
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Создаем лист в книге
        XSSFSheet sheet = workbook.createSheet("Таблица");

        // Создаем строку в листе
        XSSFRow row = sheet.createRow(0);

        // Добавляем ячейки в строку
        XSSFCell cell1 = row.createCell(0);
        XSSFCell cell2 = row.createCell(1);
        XSSFCell cell3 = row.createCell(2);
        XSSFCell cell4 = row.createCell(3);
        // Задаем значения ячейкам
        cell1.setCellValue("Торговое наименование");
        cell2.setCellValue("Комплект");
        cell3.setCellValue("Дозировка");
        cell4.setCellValue("Цена");

        // Задаем формат ячейкам
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell1.setCellStyle(style);
        cell2.setCellStyle(style);
        cell3.setCellStyle(style);
        cell4.setCellStyle(style);

        //заполняем данными
        int x = 0;
        for (int i = 0; i < medicineList.size()*2; i+=2) {
            Medicine medicine = medicineList.get(x);
            XSSFRow row1 = sheet.createRow(1 + i);

            XSSFCell cell11 = row1.createCell(0);
            XSSFCell cell21 = row1.createCell(1);
            XSSFCell cell31 = row1.createCell(2);
            XSSFCell cell41 = row1.createCell(3);
            cell11.setCellValue(medicine.getName());
            cell21.setCellValue(medicine.getCompleteness());
            //cell31.setCellValue(medicine.getDosage());
            cell41.setCellValue("max");

            XSSFRow row2 = sheet.createRow(2 + i);
            XSSFCell cell411 = row2.createCell(3);
            cell411.setCellValue("min");
            x++;

        }


        // Сохраняем книгу Excel
        FileOutputStream outputStream = new FileOutputStream("table.xlsx");
        workbook.write(outputStream);
        outputStream.close();
    }
}

