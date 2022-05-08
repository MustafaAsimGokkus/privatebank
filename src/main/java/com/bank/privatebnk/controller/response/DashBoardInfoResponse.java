package com.bank.privatebnk.controller.response;

import com.bank.privatebnk.controller.dto.AccountDTO;
import com.bank.privatebnk.controller.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashBoardInfoResponse {
    List<TransactionDTO> list;
    double totalDeposit;
    double totalWithdraw;
    double totalTransfer;

    AccountDTO account;

}
