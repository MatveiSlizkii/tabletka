package by.tabletka.demo;

import by.tabletka.demo.newVersion.service.impl.MainExcelService;

public class checkURL {
    public static void main(String[] args) {
        MainExcelService excelService = new MainExcelService();
        //excelService.reader("мое.xlsx");
        excelService.checkURL("мое900 (с правками апрель 2024).xlsx");

    }
}
