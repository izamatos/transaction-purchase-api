package com.wex.purchase.transaction.domain.service;

import org.springframework.cache.Cache;

public interface CacheService {

    public Cache getTransactionsPurchaseCache();
}
