package com.bank.privatebnk.controller;


import com.bank.privatebnk.controller.request.TransactionRequest;
import com.bank.privatebnk.controller.response.Response;
import com.bank.privatebnk.domain.User;
import com.bank.privatebnk.security.service.UserDetailsImpl;
import com.bank.privatebnk.service.AccountService;
import com.bank.privatebnk.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    AccountService mockAccountService;


    @Mock
    UserService mockUserService;


    @InjectMocks // our AccountController is underTest class. we injected above 2 mocks
    AccountController underTest;



    @Test
    void create_shouldReturnResponseWhenDeposit() {

        TransactionRequest tr=new TransactionRequest();
        tr.setAmount(200.0);
        tr.setComment("It is a deposit of 200");


        User user =new User();
        user.setId(1L);

        //we need to mock UserDetailsImpl
        UserDetailsImpl currentUser=Mockito.mock(UserDetailsImpl.class);
        currentUser.setId(1L);


        Authentication authentication=mock(Authentication.class);
        SecurityContext securityContext=mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .thenReturn(currentUser);

        when(mockUserService.findById(currentUser.getId())).thenReturn(user);
       //in postman we didn't call deposit.
        doNothing().when(mockAccountService).deposit(tr, user);

        ResponseEntity<Response> depositResponse = underTest.deposit(tr);
        Response actual = depositResponse.getBody();

        assertAll(
                ()->assertNotNull(actual),
                ()->assertEquals(HttpStatus.CREATED, depositResponse.getStatusCode()),
                ()->assertEquals(true,actual.isSuccess())
        );
    }
}