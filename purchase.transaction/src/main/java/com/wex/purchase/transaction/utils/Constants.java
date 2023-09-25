package com.wex.purchase.transaction.utils;

public class Constants {
    private Constants() {
    }

    public static final String INVALID_RATE_EXCHANGE = "No currency conversion rate is available within 6 months equal to or before the purchase date, the purchase cannot be converted to the target currency.";
    public static final String INVALID_FILTER = "Filter not found between the currency and the country entered";
    public static final String CACHE_NAME = "TransactionPurchases";
    public static final String FILTER_API = "&filter=country_currency_desc:eq:";
    public static final String TRANSACTION_NOT_FOUND = "Transaction purchase not found";
    public static final String EXCHANGE_RATES_NOT_FOUND = "Couldn't find any exchange rates";
}
