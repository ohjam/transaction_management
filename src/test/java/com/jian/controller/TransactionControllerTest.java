package com.jian.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jian.common.model.Transaction;
import com.jian.entity.TransactionEntity;
import com.jian.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService mockTransactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetByTransactionId() throws Exception {
        // Setup
        // Configure TransactionService.getByTransactionId(...).
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("1"));
        transaction.setCurrency("CNY");
        transaction.setRemittorAccount("213");
        transaction.setRecipientAccount("543");
        when(mockTransactionService.getByTransactionId(0L)).thenReturn(transaction);

        // Run the test and verify the results
        mockMvc.perform(get("/api/v1/transactions/{transactionId}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testTransfer() throws Exception {
        // Setup
        // Configure TransactionService.transfer(...).
        final TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(0L);
        transactionEntity.setAmount(new BigDecimal("1"));
        transactionEntity.setCurrency("CNY");
        transactionEntity.setRemittorAccount("123");
        transactionEntity.setRecipientAccount("456");
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("1"));
        transaction.setCurrency("CNY");
        transaction.setRemittorAccount("123");
        transaction.setRecipientAccount("456");
        String content = objectMapper.writeValueAsString(transaction);
        when(mockTransactionService.transfer(transaction)).thenReturn(transactionEntity);

        // Run the test and verify the results
        mockMvc.perform(post("/api/v1/transactions")
                        .content(content).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testTransfer_badRequest() throws Exception {
        // Setup
        // Configure TransactionService.transfer(...).
        final TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(0L);
        transactionEntity.setAmount(new BigDecimal("1"));
        transactionEntity.setCurrency("CNY");
        transactionEntity.setRemittorAccount("123");
        transactionEntity.setRecipientAccount("456");
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("1"));
        transaction.setCurrency("CNY");
        transaction.setRecipientAccount("456");
        String content = objectMapper.writeValueAsString(transaction);
        when(mockTransactionService.transfer(transaction)).thenReturn(transactionEntity);

        // Run the test and verify the results
        mockMvc.perform(post("/api/v1/transactions")
                        .content(content).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate() throws Exception {
        // Setup
        // Configure TransactionService.updateTransaction(...).
        final TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(0L);
        transactionEntity.setAmount(new BigDecimal("1"));
        transactionEntity.setCurrency("CNY");
        transactionEntity.setRemittorAccount("123");
        transactionEntity.setRecipientAccount("123");
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("1"));
        transaction.setCurrency("CNY");
        transaction.setRemittorAccount("123");
        transaction.setRecipientAccount("123");
        when(mockTransactionService.updateTransaction(any())).thenReturn(transactionEntity);
        String content = objectMapper.writeValueAsString(transaction);
        // Run the test and verify the results
        mockMvc.perform(patch("/api/v1/transactions")
                        .content(content).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        // Setup
        // Run the test and verify the results
        mockMvc.perform(delete("/api/v1/transactions/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(mockTransactionService).deleteTransaction(0L);
    }

    @Test
    void testGetAllTransactions() throws Exception {
        // Setup
        // Configure TransactionService.getTransactions(...).
        final Transaction transaction = new Transaction();
        transaction.setId(0L);
        transaction.setAmount(new BigDecimal("1"));
        transaction.setCurrency("CNY");
        transaction.setRemittorAccount("123");
        transaction.setRecipientAccount("123");
        final Page<Transaction> transactions = new PageImpl<>(List.of(transaction));
        when(mockTransactionService.getTransactions(any(Pageable.class))).thenReturn(transactions);

        // Run the test and verify the results
        mockMvc.perform(get("/api/v1/transactions")
                        .param("pageNo", "0")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
