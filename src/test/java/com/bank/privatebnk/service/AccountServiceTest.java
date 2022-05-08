package com.bank.privatebnk.service;

import com.bank.privatebnk.domain.Account;
import com.bank.privatebnk.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    AccountRepository mockAccountRespository;

    @InjectMocks
    AccountService underTest;

    @Test
    void create_shouldCreateSuccessfuly() {

        Account expected=new Account();
        expected.setAccountBalance(BigDecimal.valueOf(400.0));
        expected.setAccountNumber(515112588677L);
        expected.setId(1L);


        when(mockAccountRespository.save(any())).thenReturn(expected);
        Optional<Account> optionalExpected = Optional.of(expected);

        when(mockAccountRespository.findByAccountNumber(any()))
                                   .thenReturn(optionalExpected);

        Account actual=underTest.createAccount(any());

        assertAll(
                ()->assertNotNull(actual),
                ()->assertEquals(expected.getId(), actual.getId())
        );
    }
}