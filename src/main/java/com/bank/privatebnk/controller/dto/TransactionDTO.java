package com.bank.privatebnk.controller.dto;

import com.bank.privatebnk.domain.enumeration.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private LocalDateTime date;
    private String description;
    private TransactionType type;
    private double amount;
    private BigDecimal availableBalance;
}
