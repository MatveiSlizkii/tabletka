package by.tabletka.demo;

import by.tabletka.demo.newVersion.models.Medicine;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateExcelTableRawMain {

    public static void main(String[] args) throws IOException {
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
        XSSFCell cell5 = row.createCell(4);

        // Задаем значения ячейкам
        cell1.setCellValue("Торговое наименование");
        cell2.setCellValue("Комплект");
        cell3.setCellValue("Производитель");
        cell4.setCellValue("Страна производителя");
        cell5.setCellValue("Цена");

        // Задаем формат ячейкам
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell1.setCellStyle(style);
        cell2.setCellStyle(style);
        cell3.setCellStyle(style);
        cell4.setCellStyle(style);
        cell5.setCellStyle(style);

        //это была чисто шапка
        List<List<Medicine>> medicineLists = new ArrayList<>();

        //заполняем данными
        int x = 0;
        for (int i = 0; i < medicineLists.size(); i++) {
            List<Medicine> medicineList = medicineLists.get(i);
            for (int j = 0; j < medicineList.size(); j++) {
                Medicine medicine = medicineList.get(x);
                XSSFRow row1 = sheet.createRow(1 + i);

                XSSFCell cell11 = row1.createCell(0);
                XSSFCell cell21 = row1.createCell(1);
                XSSFCell cell31 = row1.createCell(2);
                XSSFCell cell41 = row1.createCell(3);
                XSSFCell cell51 = row1.createCell(4);
                XSSFCell cell61 = row1.createCell(5);


                cell11.setCellValue(medicine.getName());
                cell21.setCellValue(medicine.getCompleteness());
                cell31.setCellValue(medicine.getFabricator());
                cell41.setCellValue(medicine.getCountryFabricator());
                cell51.setCellValue("min");
                cell61.setCellValue(medicine.getMinPrice());

                XSSFRow row2 = sheet.createRow(2 + i);
                XSSFCell cell511 = row2.createCell(3);
                XSSFCell cell611 = row2.createCell(3);

                cell511.setCellValue("max");
                cell611.setCellValue(medicine.getMaxPrice());
                x++;
            }


        }
        // Сохраняем книгу Excel
        FileOutputStream outputStream = new FileOutputStream("tableFinal.xlsx");
        workbook.write(outputStream);
        outputStream.close();
    }
}
