package com.example.payment.Repository;

import com.example.payment.DTO.UserDTO;
import com.example.payment.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUid(String uid);
    boolean existsByUid(String uid);
    boolean existsByNickname(String nickname);
}
