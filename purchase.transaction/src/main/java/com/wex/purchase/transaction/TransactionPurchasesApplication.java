package com.wex.purchase.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TransactionPurchasesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionPurchasesApplication.class, args);
	}
}
