package org.example.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AssetCalculatorTest {

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
 }
