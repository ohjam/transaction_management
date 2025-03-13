package com.jian.service;

import com.jian.cache.CacheService;
import com.jian.common.model.Transaction;
import com.jian.entity.TransactionEntity;
import com.jian.exception.BusinessException;
import com.jian.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CacheService mockCacheService;
    @Mock
    private TransactionRepository mockTransactionRepository;
    private TransactionService transactionServiceUnderTest;

    @BeforeEach
    void setUp() {
        transactionServiceUnderTest = new TransactionService(mockCacheService, mockTransactionRepository);
    }


    @Test
    void testGetByTransactionId() {
        // Setup
        final Transaction expectedResult = new Transaction();
        expectedResult.setId(0L);
        expectedResult.setAmount(new BigDecimal("1"));
        expectedResult.setCurrency("CNY");
        expectedResult.setRemittorAccount("123");
        expectedResult.setRecipientAccount("456");
        expectedResult.setMemo("memo");

        // Configure CacheService.getCache(...).
        final TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(0L);
        transactionEntity.setAmount(new BigDecimal("1"));
        transactionEntity.setCurrency("CNY");
        transactionEntity.setRemittorAccount("123");
        transactionEntity.setRecipientAccount("456");
        transactionEntity.setMemo("memo");
        when(mockCacheService.getCache(0L)).thenReturn(transactionEntity);

        // Run the test
        final Transaction result = transactionServiceUnderTest.getByTransactionId(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetByTransactionId_CacheServiceReturnsNull() {
        // Setup
        final Transaction expectedResult = new Transaction();
        expectedResult.setId(0L);
        expectedResult.setAmount(new BigDecimal("1"));
        expectedResult.setCurrency("CNY");
        expectedResult.setRemittorAccount("123");
        expectedResult.setRecipientAccount("456");
        expectedResult.setMemo("memo");

        when(mockCacheService.getCache(0L)).thenReturn(null);

        // Configure TransactionRepository.findById(...).
        final TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(0L);
        transactionEntity.setAmount(new BigDecimal("1"));
        transactionEntity.setCurrency("CNY");
        transactionEntity.setRemittorAccount("123");
        transactionEntity.setRecipientAccount("456");
        transactionEntity.setMemo("memo");
        final Optional<TransactionEntity> optionalTransactionEntity = Optional.of(transactionEntity);
        when(mockTransactionRepository.findById(0L)).thenReturn(optionalTransactionEntity);

        // Run the test
        final Transaction result = transactionServiceUnderTest.getByTransactionId(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetByTransactionId_TransactionRepositoryReturnsAbsent() {
        // Setup
        when(mockCacheService.getCache(0L)).thenReturn(null);
        when(mockTransactionRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.getByTransactionId(0L))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void testFindFromDB() {
        // Setup
        // Configure TransactionRepository.findById(...).
        final TransactionEntity transactionEntity1 = new TransactionEntity();
        transactionEntity1.setId(0L);
        transactionEntity1.setAmount(new BigDecimal("0.00"));
        transactionEntity1.setCurrency("currency");
        transactionEntity1.setRemittorAccount("currency");
        transactionEntity1.setRecipientAccount("currency");
        transactionEntity1.setMemo("memo");
        final Optional<TransactionEntity> transactionEntity = Optional.of(transactionEntity1);
        when(mockTransactionRepository.findById(0L)).thenReturn(transactionEntity);

        // Run the test
        final TransactionEntity result = transactionServiceUnderTest.findFromDB(0L);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testFindFromDB_TransactionRepositoryReturnsAbsent() {
        // Setup
        when(mockTransactionRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.findFromDB(0L)).isInstanceOf(BusinessException.class);
    }

    @Test
    void testTransfer() {
        // Setup
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("0.00"));
        transaction.setCurrency("currency");
        transaction.setRemittorAccount("currency");
        transaction.setRecipientAccount("currency");
        transaction.setMemo("memo");

        // Configure TransactionRepository.save(...).
        final TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(0L);
        transactionEntity.setAmount(new BigDecimal("0.00"));
        transactionEntity.setCurrency("currency");
        transactionEntity.setRemittorAccount("currency");
        transactionEntity.setRecipientAccount("currency");
        transactionEntity.setMemo("memo");
        when(mockTransactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        // Run the test
        final TransactionEntity result = transactionServiceUnderTest.transfer(transaction);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testDeleteTransaction() {
        // Setup
        when(mockTransactionRepository.existsById(0L)).thenReturn(true);

        // Run the test
        transactionServiceUnderTest.deleteTransaction(0L);

        // Verify the results
        verify(mockTransactionRepository).deleteById(0L);
    }

    @Test
    void testDeleteTransaction_TransactionRepositoryExistsByIdReturnsFalse() {
        // Setup
        when(mockTransactionRepository.existsById(0L)).thenReturn(false);

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.deleteTransaction(0L))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void testUpdateTransaction() {
        // Setup
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("0.00"));
        transaction.setCurrency("currency");
        transaction.setRemittorAccount("currency");
        transaction.setRecipientAccount("currency");
        transaction.setMemo("memo");

        // Configure TransactionRepository.findById(...).
        final TransactionEntity transactionEntity1 = new TransactionEntity();
        transactionEntity1.setId(0L);
        transactionEntity1.setAmount(new BigDecimal("0.00"));
        transactionEntity1.setCurrency("currency");
        transactionEntity1.setRemittorAccount("currency");
        transactionEntity1.setRecipientAccount("currency");
        transactionEntity1.setMemo("memo");
        final Optional<TransactionEntity> transactionEntity = Optional.of(transactionEntity1);
        when(mockTransactionRepository.findById(0L)).thenReturn(transactionEntity);

        // Configure TransactionRepository.save(...).
        final TransactionEntity transactionEntity2 = new TransactionEntity();
        transactionEntity2.setId(0L);
        transactionEntity2.setAmount(new BigDecimal("0.00"));
        transactionEntity2.setCurrency("currency");
        transactionEntity2.setRemittorAccount("currency");
        transactionEntity2.setRecipientAccount("currency");
        transactionEntity2.setMemo("memo");
        when(mockTransactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity2);

        // Run the test
        final TransactionEntity result = transactionServiceUnderTest.updateTransaction(transaction);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testUpdateTransaction_TransactionRepositoryFindByIdReturnsAbsent() {
        // Setup
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("0.00"));
        transaction.setCurrency("currency");
        transaction.setRemittorAccount("currency");
        transaction.setRecipientAccount("currency");
        transaction.setMemo("memo");

        when(mockTransactionRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.updateTransaction(transaction))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void testUpdateTransaction_null_id() {
        // Setup
        final Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("0.00"));
        transaction.setCurrency("currency");
        transaction.setRemittorAccount("currency");
        transaction.setRecipientAccount("currency");
        transaction.setMemo("memo");

        // Run the test
        assertThatThrownBy(() -> transactionServiceUnderTest.updateTransaction(transaction))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void testGetTransactions() {
        // Setup
        // Configure TransactionRepository.findAll(...).
        final TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(0L);
        transactionEntity.setAmount(new BigDecimal("0.00"));
        transactionEntity.setCurrency("currency");
        transactionEntity.setRemittorAccount("currency");
        transactionEntity.setRecipientAccount("currency");
        transactionEntity.setMemo("memo");
        final Page<TransactionEntity> transactionEntities = new PageImpl<>(List.of(transactionEntity));
        when(mockTransactionRepository.findAll(any(Pageable.class))).thenReturn(transactionEntities);

        // Run the test
        final Page<Transaction> result = transactionServiceUnderTest.getTransactions(PageRequest.of(0, 1));

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testGetTransactions_TransactionRepositoryReturnsNoItems() {
        // Setup
        when(mockTransactionRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Run the test
        final Page<Transaction> result = transactionServiceUnderTest.getTransactions(PageRequest.of(0, 1));

        // Verify the results
        assertNotNull(result);
    }
}
