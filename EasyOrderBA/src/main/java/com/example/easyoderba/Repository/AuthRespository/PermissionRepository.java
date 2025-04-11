package com.example.easyoderba.Repository.AuthRespository;

import com.example.easyoderba.Model.Entity.AuthEntity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    void deletePermissionByName(String name);
}
