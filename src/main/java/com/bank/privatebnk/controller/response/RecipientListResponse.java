package com.bank.privatebnk.config.controller.response;

import com.bank.privatebnk.config.controller.dto.RecipientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class RecipientListResponse {
    private List<RecipientDTO> recipients=new ArrayList<>();


}

