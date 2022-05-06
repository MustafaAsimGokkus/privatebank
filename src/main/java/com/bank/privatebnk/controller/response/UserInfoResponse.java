package com.bank.privatebnk.controller.response;
import com.bank.privatebnk.controller.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private UserDTO user;
}
