package org.example.data;

import java.math.BigDecimal;

public class Asset {
    private String assetName;
    private BigDecimal units;
    private boolean successful;
    private BigDecimal amountInUsd;
    private BigDecimal assetCostUsd;

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public BigDecimal getAmountInUsd() {
        return amountInUsd;
    }

    public void setAmountInUsd(BigDecimal amountInUsd) {
        this.amountInUsd = amountInUsd;
    }

    public BigDecimal getAssetCostUsd() {
        return assetCostUsd;
    }

    public void setAssetCostUsd(BigDecimal assetCostUsd) {
        this.assetCostUsd = assetCostUsd;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "assetName='" + assetName + '\'' +
                ", units=" + units +
                ", successful=" + successful +
                ", amountInUsd=" + amountInUsd +
                ", assetCostUsd=" + assetCostUsd +
                '}';
    }
}
