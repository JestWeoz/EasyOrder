package com.example.easyoderba.Utils.Enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderStatus {
        PENDING("PENDING"),
        CONFIRMED("CONFIRMED"),
        PREPARING("PREPARING"),
        READY("READY"),
        COMPLETED("COMPLETED"),
        CANCELLED("CANCELLED");

        String status;

}
