package com.aktech.overseas.repository;

import com.aktech.overseas.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository
        extends JpaRepository<Applicant, Long> {

    Optional<Applicant> findByUserUsername(String username);

    Optional<Applicant> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Applicant> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    List<Applicant> findByCountryIgnoreCase(String country);
}