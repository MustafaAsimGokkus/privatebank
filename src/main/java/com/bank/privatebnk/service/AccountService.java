package com.bank.privatebnk.service;

import com.bank.privatebnk.controller.request.TransactionRequest;
import com.bank.privatebnk.controller.request.TransferRequest;
import com.bank.privatebnk.domain.Account;
import com.bank.privatebnk.domain.Transaction;
import com.bank.privatebnk.domain.User;
import com.bank.privatebnk.domain.enumeration.TransactionType;
import com.bank.privatebnk.exception.BalanceNotAvailableException;
import com.bank.privatebnk.exception.ResourceNotFoundException;
import com.bank.privatebnk.exception.message.ExceptionMessage;
import com.bank.privatebnk.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionService transactionService;

    public Account createAccount(User user) {
        Account account=new Account();
        Long accountNumber = getAccountNumber();
        account.setAccountBalance(BigDecimal.valueOf(0.0));
        account.setAccountNumber(accountNumber);
        account.setUser(user);

        accountRepository.save(account);
        return accountRepository.findByAccountNumber(accountNumber).
           orElseThrow(()->new ResourceNotFoundException(
              String.format(ExceptionMessage.ACCOUNT_NOT_FOUND_MESSAGE, accountNumber)));
    }
    private Long getAccountNumber() {
        long smallest=1000_0000_0000_0000L;
        long biggest=9999_9999_9999_9999L;

        return ThreadLocalRandom.current().nextLong(smallest,biggest);
    }

    public Account findByAccountNumber(Long number) {
        return accountRepository.findByAccountNumber(number)
                .orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessage.ACCOUNT_NOT_FOUND_MESSAGE, number)));
    }
    public Account getAccount(User user) {
        Account account=accountRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException
                (String.format(ExceptionMessage.USERACCOUNT_NOT_FOUND_MESSAGE,user.getId())));

        return account;
    }
    public void deposit(TransactionRequest request,User user) {
        Account account= getAccount(user);

        double amount=request.getAmount();

        account.setAccountBalance(account.getAccountBalance()
                .add(BigDecimal.valueOf(amount)));

        Transaction transaction= new Transaction(LocalDateTime.now(),
                request.getComment(),
                amount,
                account.getAccountBalance(),
                TransactionType.DEPOSIT,
                account);

        transactionService.saveTransaction(transaction);   //1.transaction
        accountRepository.save(account); //2.transaction
    }


    public void withdraw(TransactionRequest request, User user) {
        Account account= getAccount(user);

        double amount=request.getAmount();

        if(account.getAccountBalance().longValue()<request.getAmount()) {
            throw new BalanceNotAvailableException(ExceptionMessage.BALANCE_NOT_AVAILABLE_MESSAGE);
        }


        account.setAccountBalance(account.getAccountBalance().subtract(BigDecimal.valueOf(amount)));

        Transaction transaction= new Transaction(LocalDateTime.now(),
                                 request.getComment(),
                                 amount,
                                 account.getAccountBalance(),
                                 TransactionType.WITHDRAW,
                                 account);

        transactionService.saveTransaction(transaction);//1.
        accountRepository.save(account);//2.
    }

    public void transfer(TransferRequest request, User user) {
        Account account= getAccount(user);

        if(account.getAccountBalance().longValue()<request.getAmount()) {
            throw new BalanceNotAvailableException(ExceptionMessage.BALANCE_NOT_AVAILABLE_MESSAGE);
        }


        Account toAccount = accountRepository.findByAccountNumber(request.getRecipientNumber()).
                orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessage.RECIPIENT_ACCOUNT_NOT_FOUND_MESSAGE, request.getRecipientNumber())));


        double amount=request.getAmount();

        // sender
        account.setAccountBalance(account.getAccountBalance().subtract(BigDecimal.valueOf(amount)));

        // receiver
        toAccount.setAccountBalance(toAccount.getAccountBalance().add(BigDecimal.valueOf(amount)));

        LocalDateTime now  = LocalDateTime.now();

        //sender transaction
        Transaction transactionFrom= new Transaction(now,
                request.getComment()+" Transferred to "+toAccount.getAccountNumber(),
                amount,
                account.getAccountBalance(),
                TransactionType.TRANSFER,
                account);

        //receiver transaction
        Transaction transactionTo= new Transaction(now,
                "Transferred from "+account.getAccountNumber(),
                amount,
                toAccount.getAccountBalance(),
                TransactionType.TRANSFER,
                toAccount);


        transactionService.saveTransaction(transactionFrom);
        transactionService.saveTransaction(transactionTo);

        accountRepository.save(account);
        accountRepository.save(toAccount);




    }

}
