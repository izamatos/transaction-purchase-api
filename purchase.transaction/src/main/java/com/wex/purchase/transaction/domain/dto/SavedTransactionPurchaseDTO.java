package com.wex.purchase.transaction.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
public class SavedTransactionPurchaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4724881127320257072L;

    private UUID transactionUuid;
    private String description;
    private Date transactionDate;
    private Double usdPurchaseAmount;
}
