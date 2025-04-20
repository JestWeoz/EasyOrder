package com.example.easyoderba.Repository.ProductRespository;

import java.awt.print.Pageable;
import java.util.List;

import com.example.easyoderba.Utils.ConfigSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.easyoderba.Model.Entity.ProductEntity.CategoryEntity;
import com.example.easyoderba.Model.Entity.ProductEntity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategory(CategoryEntity category);

    @Query(ConfigSQL.Product.SEARCH_BY_QUERY)
    List<ProductEntity> findByCustom(@Param("keyword") String keyword);
}
