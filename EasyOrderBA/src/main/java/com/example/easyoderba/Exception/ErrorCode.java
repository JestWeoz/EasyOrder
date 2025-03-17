package com.example.easyoderba.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNCAUGHT_EXCEPTION(9999, "Uncaught exception"),
    USER_EXISTED(1001, "User already existed"),
    USER_NOT_FOUND(1002, "User not found"),
    USERNAME_EXISTED(1003, "Username already existed"),
    USERNAME_INVALID(1004, "Username is invalid"),
    PASSWORD_INVALID(1005, "Password is invalid"),
    WRONG_PASSWORD(1006, "Wrong password"),
    EMAIL_EXISTED(1007, "Email already existed"),
    EMAIL_INVALID(1008, "Email is invalid"),

    ;


    private int code;
    private String message;

}
