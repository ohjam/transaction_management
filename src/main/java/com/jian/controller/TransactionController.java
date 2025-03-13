package com.jian.controller;

import com.jian.common.http.response.JsonResult;
import com.jian.common.http.response.JsonResultWithPage;
import com.jian.common.model.Transaction;
import com.jian.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${api.urlPrefix}/transactions", produces = {"application/json"})
@RequiredArgsConstructor
public class TransactionController extends AbstractController {

    private final TransactionService transactionService;

    @Operation(summary = "get transaction by id", description = "get transaction by id")
    @GetMapping("/{transactionId}")
    public JsonResult<Transaction> getByTransactionId(@PathVariable Long transactionId) {
        return new JsonResult<Transaction>().setCode(0).setSuccess(true).setData(transactionService.getByTransactionId(transactionId));
    }

    @Operation(summary = "create transaction", description = "create transaction")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<Long> transfer(@Valid @RequestBody Transaction transaction) {
        return new JsonResult<Long>().setCode(0).setSuccess(true).setData(transactionService.transfer(transaction).getId());
    }

    @Operation(summary = "update transaction", description = "update transaction")
    @PatchMapping
    public JsonResult<Long> update(@RequestBody Transaction transaction) {
        return new JsonResult<Long>().setCode(0).setSuccess(true).setData(transactionService.updateTransaction(transaction).getId());
    }

    @Operation(summary = "delete transaction by id", description = "delete transaction by id")
    @DeleteMapping("/{id}")
    public JsonResult<?> delete(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return new JsonResult<Long>().setCode(0).setSuccess(true).setData(null);
    }

    @Operation(summary = "get all transaction", description = "get all transaction")
    @GetMapping
    public JsonResultWithPage<List<Transaction>> getAllTransactions(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") @Min(1) @Max(100) int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Transaction> transactions = transactionService.getTransactions(pageable);
        List<Transaction> transactionList = transactions.getContent();
        return new JsonResultWithPage<List<Transaction>>()
                .setCode(0)
                .setSuccess(true)
                .setPageNumber(pageable.getPageNumber())
                .setPageSize(pageable.getPageSize())
                .setTotalPages(transactions.getTotalPages())
                .setTotalRows(transactions.getTotalElements())
                .setData(transactionList);

    }

}
