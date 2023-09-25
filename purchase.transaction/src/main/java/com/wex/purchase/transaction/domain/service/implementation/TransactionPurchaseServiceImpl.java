package com.wex.purchase.transaction.domain.service.implementation;

import com.wex.purchase.transaction.domain.dto.ConvertedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.CountryExchangeRateDTO;
import com.wex.purchase.transaction.domain.dto.CreateTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.dto.RatesOfExchangeDTO;
import com.wex.purchase.transaction.domain.dto.SavedTransactionPurchaseDTO;
import com.wex.purchase.transaction.domain.mapper.TransactionMapper;
import com.wex.purchase.transaction.domain.service.CacheService;
import com.wex.purchase.transaction.domain.service.TransactionPurchaseService;
import com.wex.purchase.transaction.exception.ResourceBadRequestException;
import com.wex.purchase.transaction.exception.ResourceNotFoundException;
import com.wex.purchase.transaction.utils.Formatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.wex.purchase.transaction.utils.Constants.EXCHANGE_RATES_NOT_FOUND;
import static com.wex.purchase.transaction.utils.Constants.FILTER_API;
import static com.wex.purchase.transaction.utils.Constants.INVALID_FILTER;
import static com.wex.purchase.transaction.utils.Constants.INVALID_RATE_EXCHANGE;
import static com.wex.purchase.transaction.utils.Constants.TRANSACTION_NOT_FOUND;

@Service
public class TransactionPurchaseServiceImpl implements TransactionPurchaseService {

    private final TransactionMapper transactionMapper;

    @Value("${rates-of-exchange-api.url}")
    private String ratesOfExchangeApiBaseUrl;
    private final RestTemplate restTemplate;
    private final CacheService cacheService;

    public TransactionPurchaseServiceImpl(TransactionMapper transactionMapper, CacheService cacheService) {
        this.transactionMapper = transactionMapper;
        this.cacheService = cacheService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    @CachePut(value = "TransactionPurchases", key = "#result.transactionUuid")
    public SavedTransactionPurchaseDTO createTransactionPurchase(CreateTransactionPurchaseDTO createTransactionPurchaseDTO) {
        var transactionPurchaseEntity =
                transactionMapper.mapCreateTransactionPurchaseDTOToTransactionPurchase(createTransactionPurchaseDTO);
        return transactionMapper.mapTransactionPurchaseToSavedTransactionPurchaseDTO(transactionPurchaseEntity);
    }

    @Override
    public ConvertedTransactionPurchaseDTO getTransactionPurchaseByTransactionUuid(UUID transactionUuid, String countryCurrency) {

        var savedTransactionPurchaseDTO = findSavedTransactionPurchaseDTO(transactionUuid);

        var transactionPurchase =
                transactionMapper.mapSavedPurchaseResponseDTOToConvertedTransactionPurchaseDTO(savedTransactionPurchaseDTO);

        if (Objects.nonNull(countryCurrency)) {
            var ratesOfExchange = findRatesOfExchangeByCountryCurrencyFilter(countryCurrency);
            validateMonthsBetweenExchangeRateDateAndPurchaseDate(ratesOfExchange.getData(), transactionPurchase);
            return convertAmount(ratesOfExchange.getData(), transactionPurchase);
        }
        return transactionPurchase;
    }

    private SavedTransactionPurchaseDTO findSavedTransactionPurchaseDTO(UUID transactionUuid) {
        SavedTransactionPurchaseDTO savedTransactionPurchaseDTO = getTransactionsPurchaseCache(transactionUuid);
        if (Objects.isNull(savedTransactionPurchaseDTO)) {
            throw new ResourceNotFoundException(TRANSACTION_NOT_FOUND);
        }
        return savedTransactionPurchaseDTO;
    }

    private void validateMonthsBetweenExchangeRateDateAndPurchaseDate(List<CountryExchangeRateDTO> countryExchangeRateList,
                                                                      ConvertedTransactionPurchaseDTO convertedTransactionPurchaseDTO) {

        Date transactionPurchaseDate = convertedTransactionPurchaseDTO.getTransactionDate();
        countryExchangeRateList.forEach(countryExchangeRate -> {
            Date countryExchangeRateRecordDate = countryExchangeRate.getRecordDate();
            var diffDays = (int) (transactionPurchaseDate.getTime() - countryExchangeRateRecordDate.getTime());

            if (diffDays > 6) {
                throw new ResourceBadRequestException(INVALID_RATE_EXCHANGE);
            }
        });
    }

    private ConvertedTransactionPurchaseDTO convertAmount(
            List<CountryExchangeRateDTO> countryExchangeRateList, ConvertedTransactionPurchaseDTO transactionPurchase) {
        ConvertedTransactionPurchaseDTO convertedTransactionPurchaseDTO = new ConvertedTransactionPurchaseDTO();
        for (CountryExchangeRateDTO countryExchangeRate : countryExchangeRateList) {
            var exchangeRate = countryExchangeRate.getExchangeRate();
            double convertedAmount =
                    convertUsdPurchaseAmountUsingExchangeRate(transactionPurchase.getUsdPurchaseAmount(), exchangeRate);
            convertedTransactionPurchaseDTO = buildConvertedTransactionPurchase(transactionPurchase, countryExchangeRate);
            convertedTransactionPurchaseDTO.setExchangeRateUsed(exchangeRate);
            convertedTransactionPurchaseDTO.setConvertedAmount(roundDoubleValue(convertedAmount));
        }
        return convertedTransactionPurchaseDTO;
    }

    private Double convertUsdPurchaseAmountUsingExchangeRate(Double usdPurchaseAmount, Double exchangeRate) {
        return usdPurchaseAmount * exchangeRate;
    }

    private ConvertedTransactionPurchaseDTO buildConvertedTransactionPurchase(
            ConvertedTransactionPurchaseDTO transactionPurchase, CountryExchangeRateDTO countryExchangeRateDTO) {
        return ConvertedTransactionPurchaseDTO.builder()
                .transactionDate(transactionPurchase.getTransactionDate())
                .usdPurchaseAmount(roundDoubleValue(transactionPurchase.getUsdPurchaseAmount()))
                .description(transactionPurchase.getDescription())
                .transactionUuid(transactionPurchase.getTransactionUuid())
                .countryCurrencyDescription(countryExchangeRateDTO.getCountryCurrencyDescription())
                .exchangeRateRecordDate(countryExchangeRateDTO.getRecordDate().toString())
                .build();
    }

    private RatesOfExchangeDTO findRatesOfExchangeByCountryCurrencyFilter(String countryCurrency) {
        var ratesOfExchange = getRatesOfExchange(countryCurrency);
        if (Objects.nonNull(ratesOfExchange) && CollectionUtils.isEmpty(ratesOfExchange.getData())) {
            throw new ResourceBadRequestException(INVALID_FILTER);
        }
        if (Objects.nonNull(ratesOfExchange)) {
            return ratesOfExchange;
        }
        throw new ResourceNotFoundException(EXCHANGE_RATES_NOT_FOUND);
    }

    private Double roundDoubleValue(Double value) {
        return Formatter.formatTwoDecimalPlaces(value);
    }

    private RatesOfExchangeDTO getRatesOfExchange(String countryCurrency) {
        String ratesOfExchangeApiFilteredUrl = ratesOfExchangeApiBaseUrl + FILTER_API + countryCurrency;

        ResponseEntity<RatesOfExchangeDTO> responseEntity =
                restTemplate.getForEntity(ratesOfExchangeApiFilteredUrl, RatesOfExchangeDTO.class);
        return responseEntity.getBody();
    }

    private SavedTransactionPurchaseDTO getTransactionsPurchaseCache(UUID transactionUuid) {
        Cache cache = cacheService.getTransactionsPurchaseCache();

        return Objects.nonNull(cache)
                ? cache.get(transactionUuid, SavedTransactionPurchaseDTO.class)
                : null;
    }
}
