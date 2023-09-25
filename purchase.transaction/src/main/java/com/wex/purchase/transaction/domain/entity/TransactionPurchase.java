package com.wex.purchase.transaction.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Builder
public class TransactionPurchase implements Serializable {

    @Serial
    private static final long serialVersionUID = -8482144600377160857L;

    private UUID transactionUuid;

    private String description;

    private Date transactionDate;

    private Double usdPurchaseAmount;
}
