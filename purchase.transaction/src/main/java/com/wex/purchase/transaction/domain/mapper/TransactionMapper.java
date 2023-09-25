package com.wex.purchase.transaction.domain.mapper;

import com.wex.purchase.transaction.domain.dto.CreateTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.SavedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.ConvertedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.entity.TransactionPurchase;

public interface TransactionMapper {
    TransactionPurchase mapCreateTransactionPurchaseDTOToTransactionPurchase(
            CreateTransactionPurchaseDTO createTransactionPurchaseDTO);

    SavedTransactionPurchaseDTO mapTransactionPurchaseToSavedTransactionPurchaseDTO(
            TransactionPurchase transactionPurchase);

    ConvertedTransactionPurchaseDTO mapSavedPurchaseResponseDTOToConvertedTransactionPurchaseDTO(
            SavedTransactionPurchaseDTO savedTransactionPurchaseDTO);


}
