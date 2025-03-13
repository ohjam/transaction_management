package com.jian.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String CACHE_GLOBAL_KEY_PREFIX = "transaction_management:";
    public static final String CACHE_TRANSACTION_KEY_PREFIX = CACHE_GLOBAL_KEY_PREFIX + "transaction:";
    public static final String CACHE_TRANSACTIONS_KEY_PREFIX = CACHE_GLOBAL_KEY_PREFIX + "transactions";

}
