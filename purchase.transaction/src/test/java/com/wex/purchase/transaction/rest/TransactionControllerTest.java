package com.wex.purchase.transaction.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.purchase.transaction.config.TestConfig;
import com.wex.purchase.transaction.domain.dto.ConvertedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.CreateTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.SavedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.service.TransactionPurchaseService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc httpRequest;

    @MockBean
    private TransactionPurchaseService transactionPurchaseService;

    @Autowired
    private EasyRandom easyRandom;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateTransactionPurchaseDTO createTransactionPurchaseDTO;
    private SavedTransactionPurchaseDTO savedTransactionPurchaseDTO;
    private ConvertedTransactionPurchaseDTO convertedTransactionPurchaseDTO;

    @BeforeEach
    void setUp() {
        createTransactionPurchaseDTO = easyRandom.nextObject(CreateTransactionPurchaseDTO.class);
        savedTransactionPurchaseDTO = easyRandom.nextObject(SavedTransactionPurchaseDTO.class);
        convertedTransactionPurchaseDTO = easyRandom.nextObject(ConvertedTransactionPurchaseDTO.class);
    }

    @Test
    void itShouldReturnSavedTransactionPurchase_WhenCreateTransactionPurchase_WithValidFields() throws Exception {

        String body = objectMapper.writeValueAsString(createTransactionPurchaseDTO);

        when(transactionPurchaseService.createTransactionPurchase(any())).thenReturn(savedTransactionPurchaseDTO);

        httpRequest.perform(post("/v1/transactions").accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        verify(transactionPurchaseService, times(1)).createTransactionPurchase(any());
    }

    @Test
    void itShouldReturnTransactionPurchaseResponseList_WhenGetPurchaseTransactions_WithValidParams() throws Exception {
        var countryCurrency = easyRandom.nextObject(String.class);
        UUID transactionUuid = UUID.randomUUID();

        when(transactionPurchaseService.getTransactionPurchaseByTransactionUuid(transactionUuid, countryCurrency))
                .thenReturn(convertedTransactionPurchaseDTO);

        httpRequest.perform(get("/v1/transactions/" + transactionUuid).accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("country-currency", countryCurrency))
                .andExpect(status().isOk());

        verify(transactionPurchaseService, times(1)).getTransactionPurchaseByTransactionUuid(transactionUuid, countryCurrency);
    }
}