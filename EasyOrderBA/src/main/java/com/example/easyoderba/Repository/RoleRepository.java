package com.example.easyoderba.Repository;

import com.example.easyoderba.Model.Entity.AuthEntity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
