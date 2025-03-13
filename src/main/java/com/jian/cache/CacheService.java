package com.jian.cache;

import com.jian.common.util.Constants;
import com.jian.entity.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager manager;

    public TransactionEntity getCache(Long id) {
        if (isCached(id)) {
            var cache = manager.getCache(Constants.CACHE_TRANSACTION_KEY_PREFIX);
            return cache.get(id, TransactionEntity.class);
        }
        return null;
    }

    private boolean isCached(Long id) {
        var cache = manager.getCache(Constants.CACHE_TRANSACTION_KEY_PREFIX);
        if (cache != null) {
            return Optional.ofNullable(cache.get(id, TransactionEntity.class)).isPresent();
        }
        return false;
    }
}
