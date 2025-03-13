package com.jian.cache;

import com.jian.common.util.Constants;
import com.jian.entity.TransactionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    @Mock
    private CacheManager mockManager;

    private CacheService cacheServiceUnderTest;

    @BeforeEach
    void setUp() {
        cacheServiceUnderTest = new CacheService(mockManager);
    }

    @Test
    void testGetCache() {
        // Setup
        Cache cache = Mockito.mock(Cache.class);
        TransactionEntity entity = Mockito.mock(TransactionEntity.class);
        Mockito.when(cache.get(any(), any(Class.class))).thenReturn(entity);
        when(mockManager.getCache(Constants.CACHE_TRANSACTION_KEY_PREFIX)).thenReturn(cache);

        // Run the test
        final TransactionEntity result = cacheServiceUnderTest.getCache(0L);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testGetCache_CacheManagerReturnsNull() {
        // Setup
        when(mockManager.getCache(Constants.CACHE_TRANSACTION_KEY_PREFIX)).thenReturn(null);

        // Run the test
        final TransactionEntity result = cacheServiceUnderTest.getCache(0L);

        // Verify the results
        assertThat(result).isNull();
    }
}
