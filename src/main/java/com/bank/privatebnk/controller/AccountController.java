package com.bank.privatebnk.controller;

import com.bank.privatebnk.controller.dto.RecipientDTO;
import com.bank.privatebnk.controller.request.RecipientRequest;
import com.bank.privatebnk.controller.request.TransactionRequest;
import com.bank.privatebnk.controller.request.TransferRequest;
import com.bank.privatebnk.controller.response.BankStatementResponse;
import com.bank.privatebnk.controller.response.DashBoardInfoResponse;
import com.bank.privatebnk.controller.response.RecipientListResponse;
import com.bank.privatebnk.controller.response.Response;
import com.bank.privatebnk.domain.Account;
import com.bank.privatebnk.domain.Recipient;
import com.bank.privatebnk.domain.User;
import com.bank.privatebnk.security.service.UserDetailsImpl;
import com.bank.privatebnk.service.AccountService;
import com.bank.privatebnk.service.RecipientService;
import com.bank.privatebnk.service.TransactionService;
import com.bank.privatebnk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private RecipientService recipientService;
   @Autowired
   private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @PostMapping("/recipient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Response> addRecipient(
            @Valid @RequestBody RecipientRequest recipientRequest) {

        UserDetailsImpl userDetail =
                (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication().getPrincipal();
        User user = userService.findById(userDetail.getId());
        recipientService.addRecipient(recipientRequest, user);

        Response response = new Response();
        response.setMessage("Recipient added successfully");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/recipient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<RecipientListResponse> getRecipient(){
        UserDetailsImpl userDetails=
                (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        User user=userService.findById(userDetails.getId());
        List<Recipient> recipients = user.getRecipients();

        List<RecipientDTO> recipientDTOList =
                         recipients
                        .stream()
                        .map(this::convertoDTO)
                        .collect(Collectors.toList());

        RecipientListResponse response =
                new RecipientListResponse(recipientDTOList);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/recipient/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Response>deleteRecipient(@PathVariable Long id){
        UserDetailsImpl userDetails=
                        (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        User user=userService.findById(userDetails.getId());
        recipientService.removeRecipient(user, id);

        Response response =new Response();
        response.setMessage("Recipient deleted successfully");
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Response> deposit(@Valid @RequestBody TransactionRequest transactionRequest){
        UserDetailsImpl userDetails=(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userService.findById(userDetails.getId());
        accountService.deposit(transactionRequest, user);

        Response response=new Response();

        response.setMessage("Amount successfully deposited");
        response.setSuccess(true);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('CUSTOMER')")

    public ResponseEntity<Response> withdraw(@Valid @RequestBody TransactionRequest transactionRequest){
        UserDetailsImpl userDetails=(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userService.findById(userDetails.getId());
        accountService.withdraw(transactionRequest, user);

        Response response=new Response();

        response.setMessage("Amount successfully withdraw");
        response.setSuccess(true);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('CUSTOMER')")

    public ResponseEntity<Response> transfer(@Valid @RequestBody TransferRequest transactionRequest){
        UserDetailsImpl userDetails=(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userService.findById(userDetails.getId());
        accountService.transfer(transactionRequest, user);

        Response response=new Response();

        response.setMessage("Amount successfully transferred");
        response.setSuccess(true);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping("/customerstatement")
    @PreAuthorize("hasRole('CUSTOMER')")

    public ResponseEntity<DashBoardInfoResponse> getCustomerStatement(
            @RequestParam(value="startDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value="endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate){


        UserDetailsImpl userDetails=(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userService.findById(userDetails.getId());

        Account account =accountService.getAccount(user);
        DashBoardInfoResponse response = transactionService.calculateCustomerStatement(account.getId(), startDate, endDate);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/bankstatement")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BankStatementResponse> getBankStatement(
            @RequestParam(value="startDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value="endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate){

        BankStatementResponse bankStatement =
                transactionService.calculateBankStatement(startDate, endDate);

        return ResponseEntity.ok(bankStatement);
    }


    private RecipientDTO convertoDTO(Recipient recipient) {
        RecipientDTO recipientDTO=new RecipientDTO();
        User user=recipient.getAccount().getUser();
        recipientDTO.setId(recipient.getId());
        recipientDTO.setFirstName(user.getFirstName());
        recipientDTO.setLastName(user.getLastName());
        recipientDTO.setPhoneNumber(user.getPhoneNumber());
        recipientDTO.setEmail(user.getEmail());
        recipientDTO.setAccountNumber(recipient.getAccount().getAccountNumber());
        return recipientDTO;
    }
}
