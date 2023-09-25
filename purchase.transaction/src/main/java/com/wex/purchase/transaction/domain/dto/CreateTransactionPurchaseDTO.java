package com.wex.purchase.transaction.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Validated
public class CreateTransactionPurchaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4895012923439528260L;

    @NotEmpty(message = "description must not be empty")
    @Size(max = 50, message = "description must not exceed 50 characters")
    private String description;

    @NotNull(message = "transactionDate must not be null")
    private Date transactionDate;

    @NotNull(message = "usdPurchaseAmount must not be null")
    private Double usdPurchaseAmount;

}
