package com.wex.purchase.transaction.domain.service.implementation;

import com.wex.purchase.transaction.config.TestConfig;
import com.wex.purchase.transaction.domain.dto.ConvertedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.CreateTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.RatesOfExchangeDTO;
import com.wex.purchase.transaction.domain.dto.SavedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.entity.TransactionPurchase;
import com.wex.purchase.transaction.domain.mapper.TransactionMapper;
import com.wex.purchase.transaction.domain.service.CacheService;
import com.wex.purchase.transaction.exception.ResourceBadRequestException;
import com.wex.purchase.transaction.exception.ResourceNotFoundException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class TransactionPurchaseServiceImplTest {

    @InjectMocks
    private TransactionPurchaseServiceImpl transactionPurchaseService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CacheService cacheService;

    @Autowired
    private EasyRandom easyRandom;

    private CreateTransactionPurchaseDTO createTransactionPurchaseDTO;
    private TransactionPurchase transactionPurchase;
    private SavedTransactionPurchaseDTO savedTransactionPurchaseDTO;
    private ConvertedTransactionPurchaseDTO convertedTransactionPurchaseDTO;
    private RatesOfExchangeDTO ratesOfExchange;
    private String countryCurrency;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transactionPurchaseService, "ratesOfExchangeApiBaseUrl", "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?sort=-record_date&page[size]=1");
        createTransactionPurchaseDTO = easyRandom.nextObject(CreateTransactionPurchaseDTO.class);
        transactionPurchase = easyRandom.nextObject(TransactionPurchase.class);
        savedTransactionPurchaseDTO = easyRandom.nextObject(SavedTransactionPurchaseDTO.class);
        convertedTransactionPurchaseDTO = easyRandom.nextObject(ConvertedTransactionPurchaseDTO.class);
        ratesOfExchange = easyRandom.nextObject(RatesOfExchangeDTO.class);
        countryCurrency = "Brazil-Real";

    }

    @Test
    void itShouldReturnSavedTransactionPurchase_WhenCreateTransactionPurchase_WithValidCreateTransactionPurchaseRequest() {

        when(transactionMapper.mapCreateTransactionPurchaseDTOToTransactionPurchase(createTransactionPurchaseDTO))
                .thenReturn(transactionPurchase);

        when(transactionMapper.mapTransactionPurchaseToSavedTransactionPurchaseDTO(transactionPurchase))
                .thenReturn(savedTransactionPurchaseDTO);

        var response = transactionPurchaseService.createTransactionPurchase(createTransactionPurchaseDTO);

        assertNotNull(response);
        verify(transactionMapper, times(1))
                .mapCreateTransactionPurchaseDTOToTransactionPurchase(createTransactionPurchaseDTO);
        verify(transactionMapper, times(1))
                .mapTransactionPurchaseToSavedTransactionPurchaseDTO(transactionPurchase);
    }

    @Test
    void itShouldReturnConvertedTransactionPurchase_WhenGetTransactionPurchaseByValidTransactionUuid() {
        UUID transactionUuid = UUID.randomUUID();
        convertedTransactionPurchaseDTO.setTransactionDate(new Date());

        when(cacheService.getTransactionsPurchaseCache()).thenReturn(mock(Cache.class));
        when(cacheService.getTransactionsPurchaseCache().get(transactionUuid, SavedTransactionPurchaseDTO.class))
                .thenReturn(savedTransactionPurchaseDTO);
        when(transactionMapper.mapSavedPurchaseResponseDTOToConvertedTransactionPurchaseDTO(savedTransactionPurchaseDTO))
                .thenReturn(convertedTransactionPurchaseDTO);
        when(restTemplate.getForEntity(anyString(), eq(RatesOfExchangeDTO.class)))
                .thenReturn(new ResponseEntity<>(ratesOfExchange, HttpStatus.OK));

        var response = transactionPurchaseService.getTransactionPurchaseByTransactionUuid(transactionUuid, countryCurrency);

        assertNotNull(response);
        verify(cacheService, times(2)).getTransactionsPurchaseCache();
        verify(transactionMapper, times(1))
                .mapSavedPurchaseResponseDTOToConvertedTransactionPurchaseDTO(savedTransactionPurchaseDTO);
    }

    @Test
    void itShouldReturnResourceNotFoundException_WhenGetTransactionPurchaseByInvalidTransactionUuid() {
        UUID transactionUuid = UUID.randomUUID();
        var countryCurrency = "Brazil-Real";
        convertedTransactionPurchaseDTO.setTransactionDate(new Date(2019, Calendar.FEBRUARY, 1));

        when(cacheService.getTransactionsPurchaseCache()).thenReturn(mock(Cache.class));
        when(cacheService.getTransactionsPurchaseCache().get(transactionUuid, SavedTransactionPurchaseDTO.class))
                .thenReturn(null);

        Exception exception = assertThrows((ResourceNotFoundException.class), () ->
                transactionPurchaseService.getTransactionPurchaseByTransactionUuid(transactionUuid, countryCurrency));

        assertTrue(exception.getMessage().contains("Transaction purchase not found"));
        verify(cacheService, times(2)).getTransactionsPurchaseCache();
    }

    @Test
    void itShouldReturnResourceBadRequestException_WhenGetTransactionPurchaseByValidTransactionUuidWithRecordDateGreaterThanSixMonths() {
        UUID transactionUuid = UUID.randomUUID();
        var countryCurrency = "Brazil-Real";

        when(cacheService.getTransactionsPurchaseCache()).thenReturn(mock(Cache.class));
        when(cacheService.getTransactionsPurchaseCache().get(transactionUuid, SavedTransactionPurchaseDTO.class))
                .thenReturn(savedTransactionPurchaseDTO);
        when(transactionMapper.mapSavedPurchaseResponseDTOToConvertedTransactionPurchaseDTO(savedTransactionPurchaseDTO))
                .thenReturn(convertedTransactionPurchaseDTO);
        when(restTemplate.getForEntity(anyString(), eq(RatesOfExchangeDTO.class)))
                .thenReturn(new ResponseEntity<>(ratesOfExchange, HttpStatus.OK));

        Exception exception = assertThrows((ResourceBadRequestException.class), () ->
                transactionPurchaseService.getTransactionPurchaseByTransactionUuid(transactionUuid, countryCurrency));

        assertTrue(exception.getMessage()
                .contains("No currency conversion rate is available within 6 months equal to or before the purchase date, the purchase cannot be converted to the target currency."));
        verify(cacheService, times(2)).getTransactionsPurchaseCache();
        verify(transactionMapper, times(1))
                .mapSavedPurchaseResponseDTOToConvertedTransactionPurchaseDTO(savedTransactionPurchaseDTO);
    }
}