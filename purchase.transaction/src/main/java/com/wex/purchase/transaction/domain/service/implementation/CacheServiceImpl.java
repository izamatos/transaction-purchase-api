package com.wex.purchase.transaction.domain.service.implementation;

import com.wex.purchase.transaction.domain.service.CacheService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import static com.wex.purchase.transaction.utils.Constants.CACHE_NAME;

@Service
public class CacheServiceImpl implements CacheService {

    private final CacheManager cacheManager;

    public CacheServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Cache getTransactionsPurchaseCache() {
        return cacheManager.getCache(CACHE_NAME);
    }
}
