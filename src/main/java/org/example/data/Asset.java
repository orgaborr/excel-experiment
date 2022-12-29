package org.example.data;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Asset {
    @EqualsAndHashCode.Include
    private String assetName;
    private BigDecimal units;
    private boolean successful;
    private BigDecimal amountInUsd;
    private BigDecimal assetCostUsd;
}
