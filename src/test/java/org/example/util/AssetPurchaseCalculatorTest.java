package org.example.util;

import org.example.data.AssetPurchase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssetPurchaseCalculatorTest {

    @Test
    void testCalculateAssetCostUsd() {
        assertEquals(new BigDecimal("20.0000"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), new BigDecimal("5")));
        assertEquals(new BigDecimal("0.0200"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("0.1"), new BigDecimal("5")));
    }

    @Test
    void testCalculateAssetCostUsdWithRounding() {
        assertEquals(new BigDecimal("16.6667"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), new BigDecimal("6")));
        assertEquals(new BigDecimal("14.2857"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), new BigDecimal("7")));
        assertNotEquals(new BigDecimal("20.4081"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), new BigDecimal("4.9")));
    }

    @Test
    void testCalculateAssetCostUsdScale() {
        assertEquals(new BigDecimal("20.0000"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), new BigDecimal("5")));
        assertEquals(new BigDecimal("0.0000"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("1"), new BigDecimal("100000")));
        assertNotEquals(new BigDecimal("20"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), new BigDecimal("5")));
        assertNotEquals(new BigDecimal("20.0"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), new BigDecimal("5")));
        assertNotEquals(new BigDecimal("20.00000"), AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), new BigDecimal("5")));
    }

    @Test
    void testCalculateAssetCostUsdWithZero() {
        assertEquals(new BigDecimal("0.0000"), AssetCalculator.calculateAssetCostUsd(BigDecimal.ZERO, new BigDecimal("5")));
        assertThrows(ArithmeticException.class, () -> AssetCalculator.calculateAssetCostUsd(new BigDecimal("100"), BigDecimal.ZERO));
    }

    @Test
    void testMergeSameAssetPurchasesWithSinglePurchase() {
        AssetPurchase testPurchase = new AssetPurchase();
        testPurchase.setAssetName("testAsset");
        testPurchase.setSuccessful(true);
        testPurchase.setAmountInUsd(new BigDecimal("10"));
        testPurchase.setUnits(new BigDecimal("5"));

        List<AssetPurchase> purchases = new ArrayList<>();
        purchases.add(testPurchase);

        Collection<AssetPurchase> combinedPurchases = AssetCalculator.mergeSameAssetPurchases(purchases);
        combinedPurchases.forEach((AssetPurchase ap) -> {
            assertEquals(testPurchase.getAssetCostUsd(), ap.getAssetCostUsd());
            assertEquals(testPurchase.getUnits(), ap.getUnits());
        });
    }

    @Test
    void testMergeSameAssetPurchasesWithMultiplePurchases() {
        AssetPurchase testPurchase1 = new AssetPurchase();
        testPurchase1.setAssetName("testAsset");
        testPurchase1.setSuccessful(true);
        testPurchase1.setAmountInUsd(new BigDecimal("10"));
        testPurchase1.setUnits(new BigDecimal("5"));

        AssetPurchase testPurchase2 = new AssetPurchase();
        testPurchase2.setAssetName("testAsset");
        testPurchase2.setSuccessful(true);
        testPurchase2.setAmountInUsd(new BigDecimal("10"));
        testPurchase2.setUnits(new BigDecimal("10"));

        List<AssetPurchase> purchases = new ArrayList<>();
        purchases.add(testPurchase1);
        purchases.add(testPurchase2);

        Collection<AssetPurchase> combinedPurchases = AssetCalculator.mergeSameAssetPurchases(purchases);
        assertEquals(1, combinedPurchases.stream().distinct().count());

        combinedPurchases.forEach((AssetPurchase ap) -> {
            assertEquals(new BigDecimal("20"), ap.getAmountInUsd());
            assertEquals(new BigDecimal("15"), ap.getUnits());
            assertEquals(new BigDecimal("1.3333"), ap.getAssetCostUsd());
        });
    }

    @Test
    void testMergeSameAssetPurchasesWithMultipleDifferentAssetPurchases() {
        AssetPurchase testPurchase1 = new AssetPurchase();
        testPurchase1.setAssetName("testAsset");
        testPurchase1.setSuccessful(true);
        testPurchase1.setAmountInUsd(new BigDecimal("10"));
        testPurchase1.setUnits(new BigDecimal("5"));

        AssetPurchase testPurchase2 = new AssetPurchase();
        testPurchase2.setAssetName("testAsset");
        testPurchase2.setSuccessful(true);
        testPurchase2.setAmountInUsd(new BigDecimal("10"));
        testPurchase2.setUnits(new BigDecimal("10"));

        AssetPurchase testPurchase3 = new AssetPurchase();
        testPurchase3.setAssetName("differentAsset");
        testPurchase3.setSuccessful(true);
        testPurchase3.setAmountInUsd(new BigDecimal("10"));
        testPurchase3.setUnits(new BigDecimal("10"));

        List<AssetPurchase> purchases = new ArrayList<>();
        purchases.add(testPurchase1);
        purchases.add(testPurchase2);
        purchases.add(testPurchase3);

        Collection<AssetPurchase> combinedPurchases = AssetCalculator.mergeSameAssetPurchases(purchases);
        assertEquals(2, combinedPurchases.stream().distinct().count());
    }

    @Test
    void testMergeSameAssetPurchasesWithMultiplePurchasesWithOneUnsuccessful() {
        AssetPurchase testPurchase1 = new AssetPurchase();
        testPurchase1.setAssetName("testAsset");
        testPurchase1.setSuccessful(true);
        testPurchase1.setAmountInUsd(new BigDecimal("10"));
        testPurchase1.setUnits(new BigDecimal("5"));

        AssetPurchase testPurchase2 = new AssetPurchase();
        testPurchase2.setAssetName("testAsset");
        testPurchase2.setSuccessful(false);
        testPurchase2.setAmountInUsd(new BigDecimal("10"));
        testPurchase2.setUnits(new BigDecimal("10"));

        List<AssetPurchase> purchases = new ArrayList<>();
        purchases.add(testPurchase1);
        purchases.add(testPurchase2);

        Collection<AssetPurchase> combinedPurchases = AssetCalculator.mergeSameAssetPurchases(purchases);
        assertEquals(1, combinedPurchases.stream().distinct().count());

        combinedPurchases.forEach((AssetPurchase ap) -> {
            assertEquals(new BigDecimal("10"), ap.getAmountInUsd());
            assertEquals(new BigDecimal("5"), ap.getUnits());
            assertEquals(new BigDecimal("2.0000"), ap.getAssetCostUsd());
        });
    }

    @Test
    void testMergeSameAssetPurchasesWithMultiplePurchasesWithBothUnsuccessful() {
        AssetPurchase testPurchase1 = new AssetPurchase();
        testPurchase1.setAssetName("testAsset");
        testPurchase1.setSuccessful(false);
        testPurchase1.setAmountInUsd(new BigDecimal("10"));
        testPurchase1.setUnits(new BigDecimal("5"));

        AssetPurchase testPurchase2 = new AssetPurchase();
        testPurchase2.setAssetName("testAsset");
        testPurchase2.setSuccessful(false);
        testPurchase2.setAmountInUsd(new BigDecimal("10"));
        testPurchase2.setUnits(new BigDecimal("10"));

        List<AssetPurchase> purchases = new ArrayList<>();
        purchases.add(testPurchase1);
        purchases.add(testPurchase2);

        Collection<AssetPurchase> combinedPurchases = AssetCalculator.mergeSameAssetPurchases(purchases);
        assertEquals(1, combinedPurchases.stream().distinct().count());

        combinedPurchases.forEach((AssetPurchase ap) -> {
            assertFalse(ap.isSuccessful());
            assertNull(ap.getAmountInUsd());
            assertNull(ap.getUnits());
            assertNull(ap.getAssetCostUsd());
        });
    }
 }
