package com.example.easyoderba.Model.Entity.ProductEntity;

import com.example.easyoderba.Model.Entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_table")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TableEntity extends BaseEntity {
    private String URL;


}
