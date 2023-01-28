package org.example.util;

import lombok.experimental.UtilityClass;
import org.example.data.AssetPurchase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class AssetCalculator {

    public static Collection<AssetPurchase> mergeSameAssetPurchases(List<AssetPurchase> assetPurchaseList) {
        Map<String, List<AssetPurchase>> assetNameAssetPurchaseListMap = assetPurchaseList.stream().collect(Collectors.groupingBy(AssetPurchase::getAssetName));
        Set<AssetPurchase> assetPurchaseSet = new HashSet<>();
        assetNameAssetPurchaseListMap.forEach((assetName, purchases) -> {
            if (purchases.size() == 1) {
                assetPurchaseSet.add(purchases.get(0));
            } else {
                assetPurchaseSet.add(summarizeAssetPurchases(assetName, purchases));
            }
        });
        return assetPurchaseSet;
    }

    private static AssetPurchase summarizeAssetPurchases(String assetName, List<AssetPurchase> purchases) {
        AssetPurchase combinedAssetPurchase = new AssetPurchase();
        combinedAssetPurchase.setAssetName(assetName);

        BigDecimal units = BigDecimal.ZERO;
        BigDecimal amountInUsd = BigDecimal.ZERO;

        for (AssetPurchase assetPurchase : purchases) {
            if (!assetPurchase.isSuccessful()) {
                System.out.printf("A purchase for %s was unsuccessful and was ignored from calculation! Please check the import file.", assetName);
            } else {
                units = units.add(assetPurchase.getUnits());
                amountInUsd = amountInUsd.add(assetPurchase.getAmountInUsd());
            }
        }

        if (!BigDecimal.ZERO.equals(units) && !BigDecimal.ZERO.equals(amountInUsd)) {
            combinedAssetPurchase.setSuccessful(true);
            combinedAssetPurchase.setUnits(units);
            combinedAssetPurchase.setAmountInUsd(amountInUsd);
            combinedAssetPurchase.setAssetCostUsd(calculateAssetCostUsd(amountInUsd, units));
        }

        return combinedAssetPurchase;
    }

    public static BigDecimal calculateAssetCostUsd(BigDecimal amountInUsd, BigDecimal units) {
        return amountInUsd.divide(units, 4, RoundingMode.HALF_EVEN);
    }
}
