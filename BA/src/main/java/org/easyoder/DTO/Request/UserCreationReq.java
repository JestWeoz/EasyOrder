package org.easyoder.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;


@Getter
public class UserCreationReq {
    private String username;
    @Size(min = 6, max = 128)
    private String password;
    private String email;
}
