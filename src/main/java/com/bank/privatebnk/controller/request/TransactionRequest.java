package com.bank.privatebnk.config.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TransactionRequest {

    @NotNull
    private Double amount;

    @NotNull
    @NotBlank
    @Size(min=1,max=100,message="Please provide a comment")
    private String comment;

}

