package com.example.easyoderba.Repository.ProductRespository;

import com.example.easyoderba.Model.DTO.response.ProductRes.ImageResponse;
import com.example.easyoderba.Model.Entity.ProductEntity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByProductId(Long productId);
}
