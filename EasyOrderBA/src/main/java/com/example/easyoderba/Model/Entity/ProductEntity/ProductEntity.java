package com.example.easyoderba.Model.Entity.ProductEntity;


import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_product")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductEntity extends BaseEntity {
}
