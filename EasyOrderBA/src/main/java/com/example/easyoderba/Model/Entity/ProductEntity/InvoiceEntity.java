package com.example.easyoderba.Model.Entity.ProductEntity;

import com.example.easyoderba.Model.Entity.AuthEntity.User;
import com.example.easyoderba.Model.Entity.BaseEntity;
import com.example.easyoderba.Utils.Enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "invoice")
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceEntity extends BaseEntity {

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Column(name = "total_amount")
    Long totalAmount;

    @OneToMany(mappedBy = "invoice")
    private List<OrderEntity> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;


}
