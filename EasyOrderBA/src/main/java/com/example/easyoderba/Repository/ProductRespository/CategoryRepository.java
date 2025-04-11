package com.example.easyoderba.Repository.ProductRespository;

import com.example.easyoderba.Model.Entity.ProductEntity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
