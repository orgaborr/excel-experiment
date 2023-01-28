package org.example.util;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.data.AssetPurchase;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@UtilityClass
public final class ExportFileReader {
    private static final int ASSET_NAME_INDEX = 1;
    private static final int UNITS_INDEX = 4;
    private static final int SUCCESS_INDEX = 6;
    private static final int AMOUNT_IN_USD_INDEX = 2;
    private static final String DELIMITER_SPACE = " ";
    private static final String SUCCESS_STATUS = "Success";
    private static final List<Integer> USED_COLUMN_INDEX_LIST = Arrays.asList(ASSET_NAME_INDEX,
                                                                              UNITS_INDEX,
                                                                              SUCCESS_INDEX,
                                                                              AMOUNT_IN_USD_INDEX);

    public static Collection<AssetPurchase> readExportFile(String filePath) throws IOException {
        FileInputStream inputFile = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(0);

        List<AssetPurchase> assetPurchaseList = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            AssetPurchase assetPurchase = new AssetPurchase();
            for (Cell cell : row) {
                int columnIndex = cell.getColumnIndex();
                if (USED_COLUMN_INDEX_LIST.contains(columnIndex)) {
                    String cellValue = cell.getRichStringCellValue().getString();
                    switch (columnIndex) {
                        case ASSET_NAME_INDEX -> assetPurchase.setAssetName(cellValue);
                        case UNITS_INDEX -> assetPurchase.setUnits(new BigDecimal(getPureValue(cellValue)));
                        case SUCCESS_INDEX -> assetPurchase.setSuccessful(SUCCESS_STATUS.equals(cellValue));
                        case AMOUNT_IN_USD_INDEX -> assetPurchase.setAmountInUsd(new BigDecimal(getPureValue(cellValue)));
                        default -> System.out.printf("Column %s is not implemented!%n", columnIndex);
                    }
                }
            }
            assetPurchase.setAssetCostUsd(AssetCalculator.calculateAssetCostUsd(assetPurchase.getAmountInUsd(), assetPurchase.getUnits()));
            assetPurchaseList.add(assetPurchase);
        }

        return AssetCalculator.mergeSameAssetPurchases(assetPurchaseList);
    }

    private static String getPureValue(String cellValue) {
        return cellValue.split(DELIMITER_SPACE)[0];
    }
}
