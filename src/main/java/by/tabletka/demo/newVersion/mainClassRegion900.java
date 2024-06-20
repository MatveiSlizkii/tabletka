package by.tabletka.demo.newVersion;

import by.tabletka.demo.newVersion.models.Medicine;
import by.tabletka.demo.newVersion.models.Region;
import by.tabletka.demo.newVersion.models.WidePrice;
import by.tabletka.demo.newVersion.service.impl.MainExcelService;
import by.tabletka.demo.newVersion.service.impl.TabletkaDataService;

import java.util.ArrayList;
import java.util.List;

public class mainClassRegion900 {
    public static void main(String[] args) {
        MainExcelService excelService = new MainExcelService();
        TabletkaDataService dataService = new TabletkaDataService();
        //забрали с екселя имена препаратов
        //115 - мое12
        //ЛП без контроля - ЛП без контроля мое900 (с правками апрель 2024).xlsx

        List<Medicine> medicineList = excelService.reader("мое12.xlsx");
        System.out.println(medicineList);
        //проверяем все ли они подходят под ссылку
//        List<String> nameMedicine = new ArrayList<>();
//        medicineList.forEach((o)-> nameMedicine.add(o.getName()));
//        Map<Boolean, List<String>> isCorrect = dataService.isCorrectData(nameMedicine);
//        isCorrect.forEach((bool, list) -> {
//            if (bool == true) {
//                System.out.println("Проверка вносимых препаратов прошла успешно");
//            } else System.out.println("Есть проблемы с индентификацией некоторых препаратов: " + list.toString());
//        });

        //Наполняем в препараты пустые значения регионов



        //начинаем наполнять данными все препараты
        long startAdd = System.currentTimeMillis();
        for (int i = 0; i < medicineList.size(); i++) {
            List<WidePrice> widePrices = new ArrayList<>();
            widePrices.add(0, new WidePrice(Region.ALL_REGIONS));
//            widePrices.add(1, new WidePrice(Region.BREST));
//            widePrices.add(2, new WidePrice(Region.VITEBSK));
//            widePrices.add(3, new WidePrice(Region.GOMEL));
//            widePrices.add(4, new WidePrice(Region.GRODNO));
//            widePrices.add(5, new WidePrice(Region.MINSK));
//            widePrices.add(6, new WidePrice(Region.MOGILEV));
//            widePrices.add(7, new WidePrice(Region.MINSKAYA_OBL));
            medicineList.get(i).setPrices(widePrices);



            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.ALL_REGIONS));
//            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.BREST));
//            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.VITEBSK));
//            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.GOMEL));
//            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.GRODNO));
//            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.MINSK));
//            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.MOGILEV));
//            medicineList.set(i, dataService.addMainData(medicineList.get(i), Region.MINSKAYA_OBL));
            System.out.println( i + 1 + " " +medicineList.get(i).getName() + " обработано");

        }
        long finishAdd = System.currentTimeMillis();
        long delta = finishAdd - startAdd;
        System.out.println("Время на внесение " + medicineList.size() + " препаратов заняло " + delta + " миллисекунд");
        System.out.println(medicineList);
        //заполняем таблицы конечную
        excelService.createExcelWithRegion(medicineList);

    }

}
