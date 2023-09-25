package com.wex.purchase.transaction.domain.service;

import com.wex.purchase.transaction.domain.dto.ConvertedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.CreateTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.SavedTransactionPurchaseDTO;

import java.util.UUID;

public interface TransactionPurchaseService {
    SavedTransactionPurchaseDTO createTransactionPurchase(CreateTransactionPurchaseDTO createTransactionPurchaseDTO);

    ConvertedTransactionPurchaseDTO getTransactionPurchaseByTransactionUuid(UUID transactionUuid, String countryCurrency);
}
