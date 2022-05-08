package com.bank.privatebnk.config.controller.response;
import com.bank.privatebnk.config.controller.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private UserDTO user;
}
