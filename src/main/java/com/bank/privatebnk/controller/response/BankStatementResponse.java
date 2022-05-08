package com.bank.privatebnk.controller.response;

import com.bank.privatebnk.controller.dto.AdminTransactionDTO;
import lombok.Data;

import java.util.List;

@Data
public class BankStatementResponse {

    private List<AdminTransactionDTO> list;
    private double totalBalance;


}