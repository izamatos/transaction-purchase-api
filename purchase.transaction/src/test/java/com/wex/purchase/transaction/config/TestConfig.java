package com.wex.purchase.transaction.config;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.context.annotation.Bean;

public class TestConfig {
    @Bean
    public EasyRandom easyRandom() {

        EasyRandomParameters parameters = new EasyRandomParameters()
                .stringLengthRange(3, 3)
                .collectionSizeRange(1, 2);

        return new EasyRandom(parameters);
    }
}
