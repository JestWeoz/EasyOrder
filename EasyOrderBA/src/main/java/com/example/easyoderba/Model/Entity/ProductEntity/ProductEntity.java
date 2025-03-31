package com.example.easyoderba.Model.Entity.ProductEntity;


import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
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

}
