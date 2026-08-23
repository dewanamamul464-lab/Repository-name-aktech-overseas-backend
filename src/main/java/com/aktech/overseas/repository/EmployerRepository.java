package com.aktech.overseas.repository;

import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.EmployerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployerRepository
        extends JpaRepository<Employer, Long> {

    Optional<Employer> findByUserId(Long userId);

    Optional<Employer> findByUserUsername(String username);

    Optional<Employer> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Employer> findByStatus(EmployerStatus status);
}