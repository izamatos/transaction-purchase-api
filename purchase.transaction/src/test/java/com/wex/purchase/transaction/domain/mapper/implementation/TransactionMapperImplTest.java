package com.wex.purchase.transaction.domain.mapper.implementation;

import com.wex.purchase.transaction.config.TestConfig;
import com.wex.purchase.transaction.domain.dto.CreateTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.SavedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.entity.TransactionPurchase;
import com.wex.purchase.transaction.utils.Formatter;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class TransactionMapperImplTest {

    @InjectMocks
    private TransactionMapperImpl transactionMapper;

    @Autowired
    private EasyRandom easyRandom;

    private CreateTransactionPurchaseDTO createTransactionPurchaseDTO;

    private TransactionPurchase transactionPurchase;

    private SavedTransactionPurchaseDTO savedTransactionPurchaseDTO;

    @BeforeEach
    void setUp() {
        createTransactionPurchaseDTO = easyRandom.nextObject(CreateTransactionPurchaseDTO.class);
        transactionPurchase = easyRandom.nextObject(TransactionPurchase.class);
        savedTransactionPurchaseDTO = easyRandom.nextObject(SavedTransactionPurchaseDTO.class);
    }

    @Test
    void itShouldReturnTransactionPurchase_WhenMapWithCreateTransactionPurchaseDTO() {

        var response = transactionMapper
                .mapCreateTransactionPurchaseDTOToTransactionPurchase(createTransactionPurchaseDTO);

        assertNotNull(response);
        assertThat(response)
                .hasFieldOrPropertyWithValue("transactionDate", createTransactionPurchaseDTO.getTransactionDate())
                .hasFieldOrPropertyWithValue("description", createTransactionPurchaseDTO.getDescription())
                .hasFieldOrPropertyWithValue("usdPurchaseAmount", Formatter.formatTwoDecimalPlaces(createTransactionPurchaseDTO.getUsdPurchaseAmount()));
    }

    @Test
    void itShouldReturnSavedTransactionPurchaseDTO_WhenMapWithTransactionPurchase() {

        var response = transactionMapper
                .mapTransactionPurchaseToSavedTransactionPurchaseDTO(transactionPurchase);

        assertNotNull(response);
        assertThat(response)
                .hasFieldOrPropertyWithValue("transactionDate", transactionPurchase.getTransactionDate())
                .hasFieldOrPropertyWithValue("description", transactionPurchase.getDescription())
                .hasFieldOrPropertyWithValue("usdPurchaseAmount", Formatter.formatTwoDecimalPlaces(transactionPurchase.getUsdPurchaseAmount()));
    }

    @Test
    void itShouldReturnConvertedTransactionPurchaseDTO_WhenMapWithSavedTransactionPurchaseDTO() {

        var response = transactionMapper
                .mapSavedPurchaseResponseDTOToConvertedTransactionPurchaseDTO(savedTransactionPurchaseDTO);

        assertNotNull(response);
        assertThat(response)
                .hasFieldOrPropertyWithValue("transactionDate", savedTransactionPurchaseDTO.getTransactionDate())
                .hasFieldOrPropertyWithValue("description", savedTransactionPurchaseDTO.getDescription())
                .hasFieldOrPropertyWithValue("usdPurchaseAmount", Formatter.formatTwoDecimalPlaces(savedTransactionPurchaseDTO.getUsdPurchaseAmount()));
    }
}