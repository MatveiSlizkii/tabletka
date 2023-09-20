package by.tabletka.demo.newVersion.service;

import by.tabletka.demo.newVersion.models.Medicine;

import java.util.List;
import java.util.Map;

public interface DataService {
    /**
     * Проверяет все ли переданные посредством ексель файла наименования лекарств имеют рабочую ссылку
     * @param nameMedicine лист с названиями препаратов
     * @return Boolean - true если все наименования успешно прошли проверку
     *                  false если некоторые наименования не прошли проверку
     *         List<String> возвращает наименования препаратов, которые не смогли пройти проверку
     *                      (в случае с успешным прохождения проверки лист будет пустой)
     */
    Map<Boolean, List<String>> isCorrectData (List<String> nameMedicine);

    /**
     * Заполняет все данные через tabletka.by
     * @param nameMedicine название препарата
     * @return лист с данными всех товарных позиций с данным наименованием
     */
    List<Medicine> addData (String nameMedicine);
}
