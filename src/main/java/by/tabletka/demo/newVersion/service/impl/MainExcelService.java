package by.tabletka.demo.newVersion.service.impl;

import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.service.ExcelService;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class MainExcelService implements ExcelService {
    @Override
    public List<String> reader(String path) {
        String filePath = path;

        // Открываем файл
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Программа не смогла найти запрашиваемый файл");
        }

        // Создаем объект рабочей книги
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Программа не смогла считать файл возможно вы указали не Excel файл");
        }

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
        return nameMed;
    }


    @Override
    public String createExcelTable(List<List<Medicine>> medicineLists) {

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
        //List<List<Medicine>> medicineLists = new ArrayList<>();

        //заполняем данными
        int x = 0;
        for (int i = 0; i < medicineLists.size(); i++) {
            List<Medicine> medicineList = medicineLists.get(i);
            for (int j = 0; j < medicineList.size(); j++) {
                Medicine medicine = medicineList.get(j);
                XSSFRow row1 = sheet.createRow(i+x);

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

                XSSFRow row2 = sheet.createRow(1 + i+x);
                XSSFCell cell511 = row2.createCell(4);
                XSSFCell cell611 = row2.createCell(5);

                cell511.setCellValue("max");
                cell611.setCellValue(medicine.getMaxPrice());
                x+=2;
            }


        }
        // Сохраняем книгу Excel
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("tableFinal.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Не удалось сохранить конечную таблицу");
        }
        try {
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить конечную таблицу");
        }

        return "Беги проверять";
    }
}
