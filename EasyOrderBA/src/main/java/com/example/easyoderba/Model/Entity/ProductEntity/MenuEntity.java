package com.example.easyoderba.Model.Entity.ProductEntity;

import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Entity
@Table(name = "menu")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuEntity extends BaseEntity {
    @OneToMany
    Set<CategoryEntity> categories;
}
