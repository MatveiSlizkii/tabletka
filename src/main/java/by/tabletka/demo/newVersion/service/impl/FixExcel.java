package by.tabletka.demo.newVersion.service.impl;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FixExcel {
    public static void main(String[] args) {


        String filePath = "tabletest1.xlsx";

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

        removeColumn(sheet, 4);
        removeColumn(sheet, 4);
        removeColumn(sheet, 4);
        removeColumn(sheet, 4);
        removeColumn(sheet, 4);
        removeColumn(sheet, 4);
        removeColumn(sheet, 4);
        removeColumn(sheet, 4);

        int sourceRowIdx = 2;
        int sourceColIdx = 4;
        int targetRowIdx = 1;
        int targetColIdx = 5;

        // Перемещаем значение из ячейки E3 (индекс строки 2, индекс столбца 4) в ячейку F2 (индекс строки 1, индекс столбца 5)
        try {
            for (int i = 0; i < 250; i++) {
                moveCellValue(sheet, sourceRowIdx, sourceColIdx, targetRowIdx, targetColIdx);
                sourceRowIdx = sourceRowIdx + 2;
                targetRowIdx = targetRowIdx + 2;
            }
        } catch (NullPointerException e) {
        }


        int rowIndex = 500;
        while (rowIndex >= 2) {
            removeRow(sheet, rowIndex);
            rowIndex = rowIndex - 2;
        }


        // Записываем изменения в файл
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private static void removeColumn(Sheet sheet, int columnToDelete) {
        for (Row row : sheet) {
            for (int colNum = columnToDelete; colNum < row.getLastCellNum(); colNum++) {
                Cell cell = row.getCell(colNum);
                if (cell != null) {
                    row.removeCell(cell);
                }
                Cell nextCell = row.getCell(colNum + 1);
                if (nextCell != null) {
                    Cell newCell = row.createCell(colNum, nextCell.getCellType());
                    cloneCell(newCell, nextCell);
                }
            }
        }
    }

    private static void cloneCell(Cell newCell, Cell oldCell) {
        newCell.setCellStyle(oldCell.getCellStyle());
        switch (oldCell.getCellType()) {
            case STRING:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            case ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case BLANK:
                newCell.setBlank();
                break;
            default:
                break;
        }
    }

    private static void moveCellValue(Sheet sheet, int sourceRowIdx, int sourceColIdx, int targetRowIdx, int targetColIdx) {
        Row sourceRow = sheet.getRow(sourceRowIdx);
        Row targetRow = sheet.getRow(targetRowIdx);
        if (targetRow == null) {
            targetRow = sheet.createRow(targetRowIdx);
        }

        Cell sourceCell = sourceRow.getCell(sourceColIdx);
        if (sourceCell == null) {
            return; // Если исходная ячейка пуста, ничего не делаем
        }

        Cell targetCell = targetRow.getCell(targetColIdx);
        if (targetCell == null) {
            targetCell = targetRow.createCell(targetColIdx, sourceCell.getCellType());
        }

        // Копируем содержимое и стили ячейки
        cloneCell(targetCell, sourceCell);

        // Очищаем исходную ячейку
        sourceRow.removeCell(sourceCell);
    }

    private static void removeRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }
}
