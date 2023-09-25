package com.wex.purchase.transaction.rest;

import com.wex.purchase.transaction.domain.dto.ConvertedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.CreateTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.SavedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.service.TransactionPurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController()
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionPurchaseService transactionPurchaseService;

    @Operation(summary = "Create a transaction purchase", responses = {
            @ApiResponse(responseCode = "201", description = "The transaction was successfully created",
                    content = @Content(schema = @Schema(implementation = SavedTransactionPurchaseDTO.class))),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SavedTransactionPurchaseDTO> createTransactionPurchase(
            @RequestBody @Valid CreateTransactionPurchaseDTO createTransactionPurchaseDTO) {
        return new ResponseEntity<>(transactionPurchaseService.createTransactionPurchase(createTransactionPurchaseDTO),
                HttpStatus.CREATED);

    }

    @Operation(summary = "Retrieve the transaction by its UUID", responses = {
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "The operation threw an exception"),
            @ApiResponse(responseCode = "400", description = "The difference between the transaction date and the exchange rate is bigger than 6 months"),
            @ApiResponse(responseCode = "400", description = "The inputted Country-Currency value isn't valid"),

    })
    @GetMapping("/{transactionUuid}")
    public ConvertedTransactionPurchaseDTO getPurchaseTransactions(@PathVariable(value = "transactionUuid") UUID transactionUuid,
                                                                   @RequestParam(value = "country-currency", required = false) String countryCurrency) {
        return transactionPurchaseService.getTransactionPurchaseByTransactionUuid(transactionUuid, countryCurrency);
    }
}
