package com.example.board.repository;

import com.example.board.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByUserEmail(String userEmail);
    public Optional<UserEntity> findByUserEmailAndUserPassword(String userEmail, String userPassword);

}
