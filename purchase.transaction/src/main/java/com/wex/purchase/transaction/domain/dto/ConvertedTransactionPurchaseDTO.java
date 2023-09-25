package com.wex.purchase.transaction.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertedTransactionPurchaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3693444159399364541L;

    private UUID transactionUuid;
    private String description;
    private Date transactionDate;
    private Double usdPurchaseAmount;
    private Double exchangeRateUsed;
    private Double convertedAmount;
    private String countryCurrencyDescription;
    private String exchangeRateRecordDate;
}
