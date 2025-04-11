package com.example.easyoderba.Model.Entity.ProductEntity;

import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")

@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryEntity extends BaseEntity {
    String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}
            , orphanRemoval = true)
    List<ProductEntity> products = new ArrayList<>();
}
