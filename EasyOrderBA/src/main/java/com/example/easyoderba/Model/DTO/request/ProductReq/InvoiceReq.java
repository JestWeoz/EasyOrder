package com.example.easyoderba.Model.DTO.request.ProductReq;

import com.example.easyoderba.Utils.Enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Or;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceReq {
    Long InvoiceID;
    Long userID;
    List<Long> orderIDs;
    String customerName;
    String customerPhone;
    PaymentMethod paymentMethod;


}
