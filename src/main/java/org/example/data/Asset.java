package org.example.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Asset {
    private String assetName;
    private BigDecimal units;
    private boolean successful;
    private BigDecimal amountInUsd;
    private BigDecimal assetCostUsd;
}
