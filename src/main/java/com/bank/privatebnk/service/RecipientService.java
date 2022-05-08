package com.bank.privatebnk.service;

import com.bank.privatebnk.controller.request.RecipientRequest;
import com.bank.privatebnk.domain.Account;
import com.bank.privatebnk.domain.Recipient;
import com.bank.privatebnk.domain.User;
import com.bank.privatebnk.exception.ConflictException;
import com.bank.privatebnk.exception.ResourceNotFoundException;
import com.bank.privatebnk.exception.message.ExceptionMessage;
import com.bank.privatebnk.repository.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipientService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RecipientRepository recipientRepository;

    public void addRecipient(RecipientRequest recipientRequest, User user) {
        Account account=
                accountService.findByAccountNumber(recipientRequest.getAccountNumber());
        //we check if recipient account belongs the current user
        if(user.getId().equals(account.getUser().getId())) {
            throw new ConflictException(ExceptionMessage.RECIPIENT_ADD_ERROR_MESSAGE);
        }


//			Boolean isExist = recipientRepository.existsByUserAndAccount(user, account);
//
//			if(isExist) {
//				throw new ConflictException(ExceptionMessage.RECIPIENT_DUPLICATE_MESSAGE);
//			}


        Optional<Recipient> foundRecipient =
                recipientRepository.findRecipientByUserAndAccountId(
                        user.getId(), account.getId());

        if(foundRecipient.isPresent()) {
            throw new ConflictException(ExceptionMessage.RECIPIENT_DUPLICATE_MESSAGE);
        }


        validateRecipient(recipientRequest,account);

        Recipient recipient=new Recipient();
        recipient.setUser(user);
        recipient.setAccount(account);

        recipientRepository.save(recipient);
    }

    private void validateRecipient(RecipientRequest recipientRequest,Account account) {
        //in front end how recipient should be entered
        String name=account
                .getUser()
                .getFirstName()+" "+account.getUser().getLastName();

        if(!name.equalsIgnoreCase(recipientRequest.getName())) {
            throw new ResourceNotFoundException(
                    ExceptionMessage.RECIPIENT_VALIDATION_ERROR_MESSAGE);
        }
    }

    public void removeRecipient(User user,Long id) {
        Recipient recipient=recipientRepository.findById(id).
           orElseThrow(()-> new ResourceNotFoundException(
               String.format(ExceptionMessage.RECIPIENT_NOT_FOUND_MESSAGE,id) ));

        if(user.getId().equals(recipient.getUser().getId())) {
            recipientRepository.deleteById(recipient.getId());
        }else {
            throw new ConflictException(
                    ExceptionMessage.RECIPIENT_DELETE_PERMISSON_MESSAGE);
        }

    }

}
