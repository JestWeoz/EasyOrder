package com.example.easyoderba.Model.Entity.ProductEntity;


import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductEntity extends BaseEntity {
    String name;
    Long price;
    String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}
            , orphanRemoval = true)
    List<FileEntity> fileEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    CategoryEntity category;

}
