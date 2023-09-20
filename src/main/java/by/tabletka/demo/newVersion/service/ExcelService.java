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
    List<String> reader (String path);



    /**
     * Сохраняет все полученные данные в Excel таблицу
     * @param medicineLists лист препаратов с уже заполненными датами
     * @return возвращает отчет об операции
     */
    String createExcelTable (List<List<Medicine>> medicineLists);
}
