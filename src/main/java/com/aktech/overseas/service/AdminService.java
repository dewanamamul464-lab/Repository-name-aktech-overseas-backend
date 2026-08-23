package com.aktech.overseas.service;

import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.EmployerStatus;
import com.aktech.overseas.repository.EmployerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final EmployerRepository employerRepository;

    public AdminService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    // =========================================================
    // GET ALL EMPLOYERS
    // =========================================================

    public List<Employer> getAllEmployers() {
        return employerRepository.findAll();
    }

    // =========================================================
    // GET PENDING EMPLOYERS
    // =========================================================

    public List<Employer> getPendingEmployers() {

        return employerRepository.findByStatus(
                EmployerStatus.PENDING
        );
    }

    // =========================================================
    // APPROVE EMPLOYER
    // =========================================================

    public Employer approveEmployer(Long employerId) {

        Employer employer = employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + employerId
                        )
                );

        employer.setStatus(EmployerStatus.APPROVED);

        return employerRepository.save(employer);
    }

    // =========================================================
    // REJECT EMPLOYER
    // =========================================================

    public Employer rejectEmployer(Long employerId) {

        Employer employer = employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + employerId
                        )
                );

        employer.setStatus(EmployerStatus.REJECTED);

        return employerRepository.save(employer);
    }
}