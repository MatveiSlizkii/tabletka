package by.tabletka.demo.newVersion.service.impl;

import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.service.ExcelService;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MainExcelService implements ExcelService {
    @Override
    public List<Medicine> reader(String path) {

        List<Medicine> medicineList = new ArrayList<>();

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
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            // Получаем строку
            XSSFRow row = sheet.getRow(rowNum);

            // Получаем ячейки в строке
            XSSFCell cellB = row.getCell(1);
            XSSFCell cellC = row.getCell(2);
            XSSFCell cellD = row.getCell(3);
            XSSFCell cellE = row.getCell(4);
            XSSFCell cellG = row.getCell(6);

            String regex = "\\s{2,}";
            Medicine medicine = Medicine.builder()
                    .name(cellB.getStringCellValue().replaceAll(regex, " "))
                    .completeness(cellC.getStringCellValue().replaceAll(regex, " "))
                    .fabricator(cellD.getStringCellValue().replaceAll(regex, " "))
                    .countryFabricator(cellE.getStringCellValue().replaceAll(regex, " "))
                    .defaultURL(cellG.getStringCellValue())
                    .build();

            medicineList.add(medicine);

        }
        return medicineList;
    }

    @Override
    public void checkURL(String path) {

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

        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            // Получаем строку
            XSSFRow row = sheet.getRow(rowNum);

            // Получаем ячейки в строке
            XSSFCell cellB = row.getCell(1);
            String defaultURL;

            try {
                XSSFCell cellG = row.getCell(6);
                defaultURL = cellG.getStringCellValue();

            } catch (NullPointerException e) {
                defaultURL = null;

            }

            if (defaultURL == null || defaultURL.isEmpty()) {
                String nameMedicine = cellB.getStringCellValue();
                nameMedicine = nameMedicine.replaceAll("\\s{2,}", " ");

                if (nameMedicine.charAt(0) == ' ') nameMedicine = nameMedicine.substring(1, nameMedicine.length());
                if (nameMedicine.charAt(nameMedicine.length() - 1) == ' ')
                    nameMedicine = nameMedicine.substring(0, nameMedicine.length() - 1);



                String mainURL = "https://tabletka.by/search?request=" +
                        nameMedicine.replace(" ", "%C2%A0");

                Hyperlink hyperlink = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
                hyperlink.setAddress(mainURL);

                CellStyle cellStyle = workbook.createCellStyle();

                // Установка цвета текста
                Font font = workbook.createFont();
                font.setColor(IndexedColors.BLUE.index);
                cellStyle.setFont(font);
                cellStyle.setBorderBottom(BorderStyle.MEDIUM);

                XSSFCell cellG = row.getCell(6);
                cellG.setCellValue(mainURL);
                cellG.setHyperlink(hyperlink);
                cellG.setCellStyle(cellStyle);

                System.out.println("программа добавила ссылку на препарат " + rowNum + " " + nameMedicine);
                System.out.println("для подстраховки прошу проверить является ли добавленная ссылка рабочей");
                System.out.println("если нет исправьте, пожалуйста, вручную");

            } else System.out.println("программа проверила успешно препарат c индексом " + rowNum);

        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException("Программе не удалось сохранить изменения в исходный ексель," +
                    " возможно он у Вас является открытым");
        }

    }

    @Override
    public String createExcelTable(List<Medicine> medicineList) {

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
        int x = 1;

        for (int j = 0; j < medicineList.size(); j++) {
            Medicine medicine = medicineList.get(j);
            XSSFRow row1 = sheet.createRow(x + j);

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
            cell61.setCellValue(medicine.getPrices().get(0).getMinPrice());

            XSSFRow row2 = sheet.createRow(x + 1 + j);
            XSSFCell cell511 = row2.createCell(4);
            XSSFCell cell611 = row2.createCell(5);

            cell511.setCellValue("max");
            cell611.setCellValue(medicine.getPrices().get(0).getMaxPrice());
            x++;
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

    @Override
    public String createExcelWithRegion(List<Medicine> medicineList) {
        // Создаем книгу Excel
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Создаем лист в книге
        XSSFSheet sheet = workbook.createSheet("Таблица");

        //стили для границ

        XSSFCellStyle styleAutoWrapText = workbook.createCellStyle();
        styleAutoWrapText.setBorderTop(BorderStyle.THIN);
        styleAutoWrapText.setWrapText(true);

        XSSFCellStyle styleTop = workbook.createCellStyle();
        styleTop.setBorderTop(BorderStyle.THIN);

        XSSFCellStyle styleBot = workbook.createCellStyle();
        styleBot.setBorderBottom(BorderStyle.THIN);

        XSSFCellStyle styleRight = workbook.createCellStyle();
        styleRight.setBorderRight(BorderStyle.THICK);

        XSSFCellStyle styleLeft = workbook.createCellStyle();
        styleLeft.setBorderLeft(BorderStyle.THIN);

        XSSFCellStyle styleLeftBot = workbook.createCellStyle();
        styleLeftBot.setBorderLeft(BorderStyle.THIN);
        styleLeftBot.setBorderBottom(BorderStyle.THIN);

        XSSFCellStyle styleRightBot = workbook.createCellStyle();
        styleRightBot.setBorderRight(BorderStyle.THICK);
        styleRightBot.setBorderBottom(BorderStyle.THIN);

        XSSFCellStyle styleBoldBot = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        styleBoldBot.setFont(font);
        styleBoldBot.setBorderBottom(BorderStyle.THICK);
        styleBoldBot.setWrapText(true);


        XSSFCellStyle styleBoldBotRight = workbook.createCellStyle();
        styleBoldBotRight.setFont(font);
        styleBoldBotRight.setBorderBottom(BorderStyle.THICK);
        styleBoldBotRight.setBorderRight(BorderStyle.THICK);


        // Создаем строку в листе
        XSSFRow row = sheet.createRow(0);
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(12, 3000);


        // Добавляем ячейки в строку
        XSSFCell cell1 = row.createCell(0);
        XSSFCell cell2 = row.createCell(1);
        XSSFCell cell3 = row.createCell(2);
        XSSFCell cell4 = row.createCell(3);
        XSSFCell cell5 = row.createCell(4);

        XSSFCell brest = row.createCell(5);
        XSSFCell vtb01 = row.createCell(6);
        XSSFCell gom03 = row.createCell(7);
        XSSFCell gr04 = row.createCell(8);
        XSSFCell mnsk05 = row.createCell(9);
        XSSFCell mog06 = row.createCell(10);
        XSSFCell minobl07 = row.createCell(11);
        XSSFCell allR00 = row.createCell(12);
        XSSFCell allR01 = row.createCell(13);
        // Задаем значения ячейкам
        cell1.setCellValue("Торговое наименование");
        cell2.setCellValue("Комплект");
        cell3.setCellValue("Производитель");
        cell4.setCellValue("Страна производителя");
        cell5.setCellValue("Цена");
        brest.setCellValue("Брест");
        vtb01.setCellValue("Витебск");
        gom03.setCellValue("Гомель");
        gr04.setCellValue("Гродно");
        mnsk05.setCellValue("Минск");
        mog06.setCellValue("Могилев");
        minobl07.setCellValue("Минская область");
        allR00.setCellValue("мин");
        allR01.setCellValue("макс");

        // Задаем формат ячейкам

        cell1.setCellStyle(styleBoldBot);
        cell2.setCellStyle(styleBoldBot);
        cell3.setCellStyle(styleBoldBot);
        cell4.setCellStyle(styleBoldBot);
        cell5.setCellStyle(styleBoldBotRight);
        brest.setCellStyle(styleBoldBot);
        vtb01.setCellStyle(styleBoldBot);
        gom03.setCellStyle(styleBoldBot);
        gr04.setCellStyle(styleBoldBot);
        mnsk05.setCellStyle(styleBoldBot);
        mog06.setCellStyle(styleBoldBot);
        minobl07.setCellStyle(styleBoldBot);
        allR00.setCellStyle(styleBoldBotRight);


        //это была чисто шапка

        // Создаем объект XSSFCellStyle
        XSSFCellStyle styleYellow = workbook.createCellStyle();
        styleYellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styleYellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleYellow.setBorderBottom(BorderStyle.THIN);

        XSSFCellStyle styleYellowLeft = workbook.createCellStyle();
        styleYellowLeft.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styleYellowLeft.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleYellowLeft.setBorderBottom(BorderStyle.THIN);
        styleYellowLeft.setBorderLeft(BorderStyle.THICK);

        XSSFCellStyle styleYellowRight = workbook.createCellStyle();
        styleYellowRight.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styleYellowRight.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleYellowRight.setBorderBottom(BorderStyle.THIN);
        styleYellowRight.setBorderRight(BorderStyle.THICK);

        XSSFCellStyle styleGreen = workbook.createCellStyle();
        styleGreen.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        styleGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleGreen.setBorderBottom(BorderStyle.THIN);
        styleGreen.setBorderRight(BorderStyle.THICK);


        XSSFCellStyle styleRed = workbook.createCellStyle();
        styleRed.setFillForegroundColor(IndexedColors.RED.getIndex());
        styleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleRed.setBorderRight(BorderStyle.THICK);


        //заполняем данными
        int x = 1;

        for (int j = 0; j < medicineList.size(); j++) {
            Medicine medicine = medicineList.get(j);
            XSSFRow row1 = sheet.createRow(x + j);

            XSSFCell cell11 = row1.createCell(0);
            XSSFCell cell21 = row1.createCell(1);
            XSSFCell cell31 = row1.createCell(2);
            XSSFCell cell41 = row1.createCell(3);
            XSSFCell cell51 = row1.createCell(4);
            XSSFCell brestCell = row1.createCell(5);
            XSSFCell vitebskCell = row1.createCell(6);
            XSSFCell gomelCell = row1.createCell(7);
            XSSFCell grodnoCell = row1.createCell(8);
            XSSFCell minskCell = row1.createCell(9);
            XSSFCell mogilevCell = row1.createCell(10);
            XSSFCell minskayaOblCell = row1.createCell(11);
            XSSFCell allRegionCell = row1.createCell(12);


            cell11.setCellValue(medicine.getName());
            cell11.setCellStyle(styleAutoWrapText);
            cell21.setCellValue(medicine.getCompleteness());
            cell21.setCellStyle(styleAutoWrapText);
            cell31.setCellValue(medicine.getFabricator());
            cell31.setCellStyle(styleAutoWrapText);
            cell41.setCellValue(medicine.getCountryFabricator());
            cell41.setCellStyle(styleAutoWrapText);
            cell51.setCellValue("min");
            cell51.setCellStyle(styleGreen);
//            brestCell.setCellValue(medicine.getPrices().get(1).getMinPrice());
//            vitebskCell.setCellValue(medicine.getPrices().get(2).getMinPrice());
//            gomelCell.setCellValue(medicine.getPrices().get(3).getMinPrice());
//            grodnoCell.setCellValue(medicine.getPrices().get(4).getMinPrice());
//            minskCell.setCellValue(medicine.getPrices().get(5).getMinPrice());
//            mogilevCell.setCellValue(medicine.getPrices().get(6).getMinPrice());
//            minskayaOblCell.setCellValue(medicine.getPrices().get(7).getMinPrice());
//            minskayaOblCell.setCellStyle(styleRight);
            allRegionCell.setCellValue(medicine.getPrices().get(0).getMinPrice());
            allRegionCell.setCellStyle(styleRight);


            XSSFRow row2 = sheet.createRow(x + 1 + j);

            //чисто чтобы подчеркнуть
            XSSFCell cell011 = row2.createCell(0);
            XSSFCell cell111 = row2.createCell(1);
            XSSFCell cell211 = row2.createCell(2);
            XSSFCell cell311 = row2.createCell(3);
            cell011.setCellStyle(styleBot);
            cell111.setCellStyle(styleBot);
            cell211.setCellStyle(styleBot);
            cell311.setCellStyle(styleBot);

            XSSFCell cell511 = row2.createCell(4);
            XSSFCell brestCell1 = row2.createCell(5);
            XSSFCell vitebskCe1ll = row2.createCell(6);
            XSSFCell gomelCell1 = row2.createCell(7);
            XSSFCell grodnoCel1l = row2.createCell(8);
            XSSFCell minskCell1 = row2.createCell(9);
            XSSFCell mogilevCe1ll = row2.createCell(10);
            XSSFCell minskayaO1blCell = row2.createCell(11);
            XSSFCell allRegion1Cell = row2.createCell(12);


            cell511.setCellValue("max");
            cell511.setCellStyle(styleRed);
//            brestCell1.setCellValue(medicine.getPrices().get(1).getMaxPrice());
//            brestCell1.setCellStyle(styleBot);
//            vitebskCe1ll.setCellValue(medicine.getPrices().get(2).getMaxPrice());
//            vitebskCe1ll.setCellStyle(styleBot);
//            gomelCell1.setCellValue(medicine.getPrices().get(3).getMaxPrice());
//            gomelCell1.setCellStyle(styleBot);
//            grodnoCel1l.setCellValue(medicine.getPrices().get(4).getMaxPrice());
//            grodnoCel1l.setCellStyle(styleBot);
//            minskCell1.setCellValue(medicine.getPrices().get(5).getMaxPrice());
//            minskCell1.setCellStyle(styleBot);
//            mogilevCe1ll.setCellValue(medicine.getPrices().get(6).getMaxPrice());
//            mogilevCe1ll.setCellStyle(styleBot);
//            minskayaO1blCell.setCellValue(medicine.getPrices().get(7).getMaxPrice());
//            minskayaO1blCell.setCellStyle(styleRightBot);
            allRegion1Cell.setCellValue(medicine.getPrices().get(0).getMaxPrice());
            allRegion1Cell.setCellStyle(styleRightBot);

            //перекраска максимальных значений
//            if (medicine.getPrices().get(1).getMaxPrice().equals(medicine.getPrices().get(0).getMaxPrice()))
//                brestCell1.setCellStyle(styleYellowLeft);
//            if (medicine.getPrices().get(2).getMaxPrice().equals(medicine.getPrices().get(0).getMaxPrice()))
//                vitebskCe1ll.setCellStyle(styleYellow);
//            if (medicine.getPrices().get(3).getMaxPrice().equals(medicine.getPrices().get(0).getMaxPrice()))
//                gomelCell1.setCellStyle(styleYellow);
//            if (medicine.getPrices().get(4).getMaxPrice().equals(medicine.getPrices().get(0).getMaxPrice()))
//                grodnoCel1l.setCellStyle(styleYellow);
//            if (medicine.getPrices().get(5).getMaxPrice().equals(medicine.getPrices().get(0).getMaxPrice()))
//                minskCell1.setCellStyle(styleYellow);
//            if (medicine.getPrices().get(6).getMaxPrice().equals(medicine.getPrices().get(0).getMaxPrice()))
//                mogilevCe1ll.setCellStyle(styleYellow);
//            if (medicine.getPrices().get(7).getMaxPrice().equals(medicine.getPrices().get(0).getMaxPrice()))
//                minskayaO1blCell.setCellStyle(styleYellowRight);

            x++;
        }

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH_mm");
        String dateNow = ldt.format(formatter).toString();
        // Сохраняем книгу Excel
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("table " + dateNow + ".xlsx");
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
