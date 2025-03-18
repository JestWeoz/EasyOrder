package com.example.easyoderba.Repository;

import com.example.easyoderba.Model.Entity.AuthEntity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityByUsername(String username);
    boolean existsByUsername(String username);
    List<UserEntity> findAllByOrderByIdDesc();

}
