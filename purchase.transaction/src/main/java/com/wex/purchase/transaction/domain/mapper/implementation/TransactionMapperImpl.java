package com.wex.purchase.transaction.domain.mapper.implementation;

import com.wex.purchase.transaction.domain.dto.CreateTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.SavedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.ConvertedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.entity.TransactionPurchase;
import com.wex.purchase.transaction.domain.mapper.TransactionMapper;
import com.wex.purchase.transaction.utils.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionMapperImpl implements TransactionMapper {
    @Override
    public TransactionPurchase mapCreateTransactionPurchaseDTOToTransactionPurchase(
            CreateTransactionPurchaseDTO createTransactionPurchaseDTO) {
        return TransactionPurchase.builder()
                .transactionDate(createTransactionPurchaseDTO.getTransactionDate())
                .description(createTransactionPurchaseDTO.getDescription())
                .usdPurchaseAmount(Formatter.formatTwoDecimalPlaces(createTransactionPurchaseDTO.getUsdPurchaseAmount()))
                .transactionUuid(UUID.randomUUID())
                .build();
    }

    @Override
    public SavedTransactionPurchaseDTO mapTransactionPurchaseToSavedTransactionPurchaseDTO(
            TransactionPurchase transactionPurchase) {
        return SavedTransactionPurchaseDTO.builder()
                .transactionUuid(transactionPurchase.getTransactionUuid())
                .usdPurchaseAmount(Formatter.formatTwoDecimalPlaces(transactionPurchase.getUsdPurchaseAmount()))
                .transactionDate(transactionPurchase.getTransactionDate())
                .description(transactionPurchase.getDescription())
                .build();
    }

    @Override
    public ConvertedTransactionPurchaseDTO mapSavedPurchaseResponseDTOToConvertedTransactionPurchaseDTO(
            SavedTransactionPurchaseDTO savedTransactionPurchaseDTO) {
        return ConvertedTransactionPurchaseDTO.builder()
                .transactionDate(savedTransactionPurchaseDTO.getTransactionDate())
                .transactionUuid(savedTransactionPurchaseDTO.getTransactionUuid())
                .description(savedTransactionPurchaseDTO.getDescription())
                .usdPurchaseAmount(Formatter.formatTwoDecimalPlaces(savedTransactionPurchaseDTO.getUsdPurchaseAmount()))
                .build();
    }
}
