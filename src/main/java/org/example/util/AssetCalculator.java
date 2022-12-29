package org.example.util;

import lombok.experimental.UtilityClass;
import org.example.data.Asset;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class AssetCalculator {

    public static Collection<Asset> mergeSameAssetPurchases(List<Asset> assetList) {
        Map<String, List<Asset>> assetNameAssetListMap = assetList.stream().collect(Collectors.groupingBy(Asset::getAssetName));
        Set<Asset> assetSet = new HashSet<>();
        assetNameAssetListMap.forEach((assetName, purchases) -> {
            if (purchases.size() == 1) {
                assetSet.add(purchases.get(0));
            } else {
                assetSet.add(summarizeAssetPurchases(assetName, purchases));
            }
        });
        return assetSet;
    }

    private static Asset summarizeAssetPurchases(String assetName, List<Asset> purchases) {
        Asset finalAsset = new Asset();
        finalAsset.setAssetName(assetName);

        BigDecimal units = BigDecimal.ZERO;
        BigDecimal amountInUsd = BigDecimal.ZERO;

        for (Asset asset : purchases) {
            if (!asset.isSuccessful()) {
                System.out.printf("A purchase for %s was unsuccessful and was ignored from calculation! Please check the import file.", assetName);
            } else {
                units = units.add(asset.getUnits());
                amountInUsd = amountInUsd.add(asset.getAmountInUsd());
            }
        }

        if (!BigDecimal.ZERO.equals(units) && !BigDecimal.ZERO.equals(amountInUsd)) {
            finalAsset.setSuccessful(true);
            finalAsset.setUnits(units);
            finalAsset.setAmountInUsd(amountInUsd);
            finalAsset.setAssetCostUsd(calculateAssetCostUsd(amountInUsd, units));
        }

        return finalAsset;
    }

    public static BigDecimal calculateAssetCostUsd(BigDecimal amountInUsd, BigDecimal units) {
        return amountInUsd.divide(units, 4, RoundingMode.HALF_EVEN);
    }
}
