package com.jian.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jian.common.util.Constants;
import com.jian.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        var listCacheBuilder = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES);
        var singleCacheBuilder = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES);
        var listCache = new CaffeineCache(Constants.CACHE_TRANSACTION_KEY_PREFIX, listCacheBuilder.build());
        var singleCache = new CaffeineCache(Constants.CACHE_TRANSACTIONS_KEY_PREFIX, singleCacheBuilder.build());
        cacheManager.setCaches(Arrays.asList(listCache, singleCache));

        return cacheManager;
    }


}
