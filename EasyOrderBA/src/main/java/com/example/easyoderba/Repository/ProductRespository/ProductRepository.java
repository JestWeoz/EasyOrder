package com.example.easyoderba.Repository.ProductRespository;

import com.example.easyoderba.Model.Entity.ProductEntity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
