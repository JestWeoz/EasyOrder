package com.example.easyoderba.Repository;

import com.example.easyoderba.Model.Entity.AuthEntity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

}
