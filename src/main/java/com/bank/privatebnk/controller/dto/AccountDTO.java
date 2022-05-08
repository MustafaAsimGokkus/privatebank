package com.bank.privatebnk.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {

    private Long accountNumber;
    private BigDecimal accountBalance;


}
