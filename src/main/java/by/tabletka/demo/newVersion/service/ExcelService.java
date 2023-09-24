package by.tabletka.demo.newVersion.service;

import by.tabletka.demo.newVersion.models.Medicine;

import java.util.List;

public interface ExcelService {
    /**
     *
     * @param path путь к файлу ексель (если он лежит в корневой папки программы то указывает "table.xlsx",
     *             если нет то указывает полный пусть к файлу включая сам файл и его расширение)
     * @return
     */
    List<Medicine> reader (String path);



    /**
     * Сохраняет все полученные данные в Excel таблицу
     * @param medicineList лист препаратов с уже заполненными датами ПО ПАРАМЕТРУ "ВСЕ РЕГИОНЫ"
     * @return возвращает отчет об операции
     */
    String createExcelTable (List<Medicine> medicineList);

    /**
     *
     * @param medicineList лист препаратов с уже заполненными датами с разбивкой по всем регионам
     * @return возвращает отчет об операции
     */
    String createExcelWithRegion (List<Medicine> medicineList);
}
