package com.aktech.overseas.repository;

import com.aktech.overseas.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployerRepository
        extends JpaRepository<Employer, Long> {

    Optional<Employer> findByEmail(String email);

    Optional<Employer> findByUserId(Long userId);

    Optional<Employer> findByUserUsername(String username);

    List<Employer> findByStatus(
            com.aktech.overseas.entity.EmployerStatus status
    );

    boolean existsByEmail(String email);
}