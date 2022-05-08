package com.bank.privatebnk.controller.response;

import com.bank.privatebnk.controller.dto.RecipientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class RecipientListResponse {
    private List<RecipientDTO> recipients=new ArrayList<>();


}

