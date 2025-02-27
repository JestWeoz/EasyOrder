package org.easyoder.Model.DTO;

import lombok.Data;

import java.time.LocalDate;


@Data
public class UserCreationReq {
    private String username;
    private String password;
    private String email;
}
