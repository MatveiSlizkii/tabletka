package by.tabletka.demo;

import by.tabletka.demo.newVersion.models.Medicine;

import java.io.IOException;
import java.util.List;

public class TestExcel {
    public static void main(String[] args) throws IOException {
        ExcelWork excelWork = new ExcelWork();
        List<Medicine> medicineList = excelWork.excelReader("ЛЕКИ.xlsx");
        excelWork.createExcelTable(medicineList);
    }
}
