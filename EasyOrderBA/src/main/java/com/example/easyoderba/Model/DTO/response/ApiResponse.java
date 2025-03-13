package com.example.easyoderba.Model.DTO.response;


import lombok.Data;

@Data
public class ApiResponse <T> {
    private int code;
    private String message;
    private T response;
}
