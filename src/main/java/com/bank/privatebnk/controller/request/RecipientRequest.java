package com.bank.privatebnk.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RecipientRequest {

    @NotBlank(message="Please provide not blank  name")
    @NotNull(message="Please provide your  name")
    @Size(min=1, max=100, message="Your name '${validatedValue}' must be between {min} and {max} chars long")
    private String name;

    @NotNull
    private Long accountNumber;

}
