package com.jian.config;

import com.jian.common.util.Constants;
import com.jian.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class Configs {

    @Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        var logFilterFilterRegistrationBean = new FilterRegistrationBean<LogFilter>();
        logFilterFilterRegistrationBean.setFilter(new LogFilter());
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        logFilterFilterRegistrationBean.setUrlPatterns(urlPatterns);
        logFilterFilterRegistrationBean.setOrder(1);
        return logFilterFilterRegistrationBean;
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Cache transactionsCache = new ConcurrentMapCache(Constants.CACHE_TRANSACTION_KEY_PREFIX);
        Cache transactionCache = new ConcurrentMapCache(Constants.CACHE_TRANSACTIONS_KEY_PREFIX);
        cacheManager.setCaches(Arrays.asList(transactionsCache, transactionCache));

        return cacheManager;
    }


}
