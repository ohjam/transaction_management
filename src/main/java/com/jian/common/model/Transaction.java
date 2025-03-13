package com.jian.common.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Transaction {

    private Long id;

    @NotNull(message = "amount must not be null")
    @Positive(message = "amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "currency must not be blank")
    private String currency;

    @NotBlank(message = "remittor account must not be blank")
    private String remittorAccount;

    @NotBlank(message = "recipient account must not be blank")
    private String recipientAccount;

    private String memo;

    private Timestamp createTime;

    private Timestamp updateTime;

}
