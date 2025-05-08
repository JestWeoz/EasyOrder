package com.example.easyoderba.Utils.Enums;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PaymentMethod {
    CASH("CASH"),
    CARD("CARD"),
    TRANSFER("TRANSFER"),
    OTHER("OTHER");

    String value;

}
