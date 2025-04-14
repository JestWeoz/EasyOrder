package com.example.easyoderba.Repository.ProductRespository;

import com.example.easyoderba.Model.Entity.ProductEntity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
