package com.example.easyoderba.Model.Entity.ProductEntity;

import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryEntity extends BaseEntity {
    String name;

    @OneToMany
    Set<ProductEntity> products;
}
