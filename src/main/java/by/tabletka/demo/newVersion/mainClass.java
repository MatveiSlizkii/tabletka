package by.tabletka.demo.newVersion;

import by.tabletka.demo.ExcelWork;
import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.models.Region;
import by.tabletka.demo.newVersion.models.WidePrice;
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
        List<Medicine> medicineList = excelService.reader("table.xlsx");
        System.out.println(medicineList);
        //проверяем все ли они подходят под ссылку
        List<String> nameMedicine = new ArrayList<>();
        medicineList.forEach((o)-> nameMedicine.add(o.getName()));
        Map<Boolean, List<String>> isCorrect = dataService.isCorrectData(nameMedicine);
        isCorrect.forEach((bool, list) -> {
            if (bool == true) {
                System.out.println("Проверка вносимых препаратов прошла успешно");
            } else System.out.println("Есть проблемы с индентификацией некоторых препаратов: " + list.toString());
        });

        //Наполняем в препараты пустые значения регионов
        List<WidePrice> widePrices = new ArrayList<>();
        widePrices.add(0, new WidePrice(Region.ALL_REGIONS));
        widePrices.add(1, new WidePrice(Region.BREST));
        widePrices.add(2, new WidePrice(Region.VITEBSK));
        widePrices.add(3, new WidePrice(Region.GOMEL));
        widePrices.add(4, new WidePrice(Region.GRODNO));
        widePrices.add(5, new WidePrice(Region.MINSK));
        widePrices.add(6, new WidePrice(Region.MOGILEV));
        widePrices.add(7, new WidePrice(Region.MINSKAYA_OBL));
        medicineList.forEach((o)-> o.setPrices(widePrices));


        //начинаем наполнять данными все препараты
        long startAdd = System.currentTimeMillis();
        for (int i = 0; i < medicineList.size(); i++) {
            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.ALL_REGIONS));
        }
        long finishAdd = System.currentTimeMillis();
        long delta = finishAdd - startAdd;
        System.out.println("Время на внесение " + nameMedicine.size() + " препаратов заняло " + delta + " миллисекунд");
        //заполняем таблицы конечную
        excelService.createExcelTable(medicineList);

    }

}
