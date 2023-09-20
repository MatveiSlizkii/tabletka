package by.tabletka.demo.newVersion;

import by.tabletka.demo.ExcelWork;
import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.service.DataService;
import by.tabletka.demo.newVersion.service.ExcelService;
import by.tabletka.demo.newVersion.service.impl.MainExcelService;
import by.tabletka.demo.newVersion.service.impl.TabletkaDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class mainClass {
    public static void main(String[] args) {
         MainExcelService excelService = new MainExcelService();
         TabletkaDataService dataService = new TabletkaDataService();
        //забрали с екселя имена препаратов
        List<String> nameMedicine = excelService.reader("table.xlsx");
        System.out.println(nameMedicine);
        //проверяем все ли они подходят под ссылку
        Map<Boolean, List<String>> isCorrect = dataService.isCorrectData(nameMedicine);
        isCorrect.forEach((bool,list)-> {
            if (bool == true) {
                System.out.println("Проверка вносимых препаратов прошла успешно");
            } else System.out.println("Есть проблемы с индентификацией некоторых препаратов: " + list.toString());
        });
        //начинаем наполнять данными все препараты
        long startAdd = System.currentTimeMillis();
        List<List<Medicine>> listsMedicine = new ArrayList<>();
        for (int i = 0; i < nameMedicine.size(); i++) {
            listsMedicine.add(dataService.addData(nameMedicine.get(i)));
        }
        long finishAdd = System.currentTimeMillis();
        long delta = finishAdd - startAdd;
        System.out.println("Время на внесение " + nameMedicine.size() + " препаратов заняло " + delta + " миллисекунд");
        //заполняем таблицы конечную
        excelService.createExcelTable(listsMedicine);

    }

}
