package com.example.easyoderba.Model.Entity.ProductEntity;

import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "tbl")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableEntity extends BaseEntity {
    String name;
}
