package com.wex.purchase.transaction.domain.service.implementation;

import com.wex.purchase.transaction.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Import(TestConfig.class)
@ExtendWith(SpringExtension.class)
class CacheServiceImplTest {

    @InjectMocks
    private CacheServiceImpl cacheService;

    @Mock
    private CacheManager cacheManager;

    @Test
    void itShouldReturnCache_WhenGetTransactionsPurchaseCache() {

        when(cacheManager.getCache("TransactionPurchases")).thenReturn(mock(Cache.class));

        var response = cacheService.getTransactionsPurchaseCache();

        assertNotNull(response);
    }

}