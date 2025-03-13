package com.jian.service;

import com.jian.cache.CacheService;
import com.jian.common.model.Transaction;
import com.jian.common.util.Constants;
import com.jian.entity.TransactionEntity;
import com.jian.exception.BusinessException;
import com.jian.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CacheService cacheService;

    private final TransactionRepository transactionRepository;

    // try get from cache, if not exist in cache, get from DB
    public Transaction getByTransactionId(Long id) {

        var cache = cacheService.getCache(id);
        if (cache != null) {
            return convertToTransaction(cache);
        }
        var transactionEntity = findFromDB(id);
        return convertToTransaction(transactionEntity);
    }

    @CachePut(value = Constants.CACHE_TRANSACTION_KEY_PREFIX, key = "#result.id", unless = "#result == null")
    public TransactionEntity findFromDB(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new BusinessException("transaction not found"));
    }

    // add new record to DB and update cache
    @Caching(evict = {
            @CacheEvict(value = Constants.CACHE_TRANSACTION_KEY_PREFIX, key = "#result.id"),
            @CacheEvict(value = Constants.CACHE_TRANSACTIONS_KEY_PREFIX, allEntries = true)
    },
            put = {
            @CachePut(value = Constants.CACHE_TRANSACTION_KEY_PREFIX, key = "#result.id")
    })
    public TransactionEntity transfer(Transaction transaction) {
        var transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(transaction.getAmount());
        transactionEntity.setCurrency(transaction.getCurrency());
        transactionEntity.setRemittorAccount(transaction.getRemittorAccount());
        transactionEntity.setRecipientAccount(transaction.getRecipientAccount());
        transactionEntity.setMemo(transaction.getMemo());
        return transactionRepository.save(transactionEntity);
    }

    // delete db and cache
    @Caching(evict = {
            @CacheEvict(value = Constants.CACHE_TRANSACTION_KEY_PREFIX, key = "#id"),
            @CacheEvict(value = Constants.CACHE_TRANSACTIONS_KEY_PREFIX, allEntries = true)
    })
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new BusinessException("transaction not found");
        }
        transactionRepository.deleteById(id);
        log.info("delete transaction id {}", id);
    }

    // update record to DB and update cache
    @Caching(evict = {
            @CacheEvict(value = Constants.CACHE_TRANSACTION_KEY_PREFIX, key = "#result.id"),
            @CacheEvict(value = Constants.CACHE_TRANSACTIONS_KEY_PREFIX, allEntries = true)
    },
            put = {
            @CachePut(value = Constants.CACHE_TRANSACTION_KEY_PREFIX, key = "#result.id")
    })
    public TransactionEntity updateTransaction(Transaction transaction) {
        var id = transaction.getId();
        if (id == null) {
            throw new BusinessException("id is null");
        }
        var transactionInDB = findFromDB(id);
        transactionInDB.setAmount(transaction.getAmount());
        transactionInDB.setCurrency(transaction.getCurrency());
        transactionInDB.setRecipientAccount(transaction.getCurrency());
        transactionInDB.setRemittorAccount(transaction.getCurrency());
        transactionInDB.setCurrency(transaction.getCurrency());
        if (transaction.getMemo() != null) {
            transactionInDB.setMemo(transaction.getMemo());
        }
        return transactionRepository.save(transactionInDB);
    }

    @Cacheable(value = Constants.CACHE_TRANSACTIONS_KEY_PREFIX, key = "#p0.pageNumber + '_' + #p0.pageSize", unless = "#result == null")
    public Page<Transaction> getTransactions(Pageable pageable) {
        var all = transactionRepository.findAll(pageable);
        return all.map(this::convertToTransaction);
    }

    private Transaction convertToTransaction(TransactionEntity transactionEntity) {
        var transaction = new Transaction();
        transaction.setId(transactionEntity.getId());
        transaction.setAmount(transactionEntity.getAmount());
        transaction.setCurrency(transactionEntity.getCurrency());
        transaction.setRemittorAccount(transactionEntity.getRemittorAccount());
        transaction.setRecipientAccount(transactionEntity.getRecipientAccount());
        transaction.setMemo(transactionEntity.getMemo());
        transaction.setCreateTime(transactionEntity.getCreateTime());
        transaction.setUpdateTime(transactionEntity.getUpdateTime());
        return transaction;
    }


}
