package com.aktech.overseas.repository;

import com.aktech.overseas.entity.Role;
import com.aktech.overseas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    long countByRole(Role role);
}