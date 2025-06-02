package com.example.easyoderba.Repository.ProductRespository;

import com.example.easyoderba.Model.Entity.ProductEntity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
}
