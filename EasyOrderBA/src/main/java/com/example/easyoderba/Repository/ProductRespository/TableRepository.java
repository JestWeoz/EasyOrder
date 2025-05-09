package com.example.easyoderba.Repository.ProductRespository;

import com.example.easyoderba.Model.Entity.ProductEntity.TableEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
}
