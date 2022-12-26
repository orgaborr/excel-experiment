package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            FileInputStream inputFile = new FileInputStream(new File("C:/Users/user/Downloads/export_experiment.xlsx"));
            Workbook workbook = new XSSFWorkbook(inputFile);
            Sheet sheet = workbook.getSheetAt(0);

            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            for (Row row : sheet) {
                data.put(i, new ArrayList<>());
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING -> data.get(i).add(cell.getRichStringCellValue().getString());
                        case NUMERIC -> {
                            String cellValue = String.valueOf(DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue());
                            data.get(i).add(cellValue);
                        }
                        case BOOLEAN -> data.get(i).add(String.valueOf(cell.getBooleanCellValue()));
                        case FORMULA -> data.get(i).add(String.valueOf(cell.getCellFormula()));
                        default -> data.get(i).add("Cell type could not be identified!");
                    }
                }
                i++;
            }

            List<String> headers = data.get(0);
            data.remove(0);

            for (List<String> s : data.values()) {
                for (String header : headers) {
                    System.out.println(header + ": " + s.get(headers.indexOf(header)));
                }
                System.out.println("********************************************");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Input file was not found!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}