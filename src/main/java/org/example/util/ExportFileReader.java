package org.example.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.data.Asset;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Pattern;

public final class ExportFileReader {
    private static final int ASSET_NAME_INDEX = 1;
    private static final int UNITS_INDEX = 4;
    private static final int SUCCESSFUL_INDEX = 6;
    private static final int AMOUNT_IN_USD_INDEX = 2;
    private static final String DELIMITER_SPACE = " ";
    private static final String SUCCESS_STATUS = "Success";

    private ExportFileReader() {}

    public static Collection<Asset> readExportFile(String filePath) throws IOException {
        FileInputStream inputFile = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(0);

        Set<Asset> assetSet = new HashSet<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Asset asset = new Asset();
            for (Cell cell : row) {
                if (isColumnUsed(cell.getColumnIndex())) {
                    String cellValue = cell.getRichStringCellValue().getString();
                    switch (cell.getColumnIndex()) {
                        case ASSET_NAME_INDEX -> asset.setAssetName(cellValue);
                        case UNITS_INDEX -> asset.setUnits(new BigDecimal(getPureValue(cellValue)));
                        case SUCCESSFUL_INDEX -> asset.setSuccessful(SUCCESS_STATUS.equals(cellValue));
                        case AMOUNT_IN_USD_INDEX -> asset.setAmountInUsd(new BigDecimal(getPureValue(cellValue)));
                        default -> System.out.printf("Column %s is not implemented!%n", cell.getColumnIndex());
                    }
                }
            }
            asset.setAssetCostUsd(asset.getAmountInUsd().divide(asset.getUnits(), 4, RoundingMode.HALF_EVEN));
            assetSet.add(asset);
        }

        return assetSet;
    }

    private static boolean isColumnUsed(int columnIndex) {
        return columnIndex == ASSET_NAME_INDEX ||
                columnIndex == UNITS_INDEX ||
                columnIndex == SUCCESSFUL_INDEX ||
                columnIndex == AMOUNT_IN_USD_INDEX;
    }

    private static String getPureValue(String cellValue) {
        Pattern pattern = Pattern.compile(DELIMITER_SPACE);
        return pattern.split(cellValue)[0];
    }

}
