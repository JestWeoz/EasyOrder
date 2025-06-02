package com.example.easyoderba.Model.DTO.request.ProductReq;

import com.example.easyoderba.Utils.Enums.OrderStatus;
import com.example.easyoderba.Utils.Enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderReq {
    Long orderId;
    Long tableId;
    List<OrderItemReq> orderItems;
    BigDecimal totalAmount;
    OrderStatus status;
    String note;
    Boolean isPaid;

}
