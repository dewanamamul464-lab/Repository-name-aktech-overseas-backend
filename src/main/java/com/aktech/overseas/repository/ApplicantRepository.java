package com.aktech.overseas.repository;

import com.aktech.overseas.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantRepository
        extends JpaRepository<Applicant, Long> {

    Optional<Applicant> findByEmail(String email);

    boolean existsByEmail(String email);

    // Find applicant by username
    Optional<Applicant> findByUserUsername(String username);
}