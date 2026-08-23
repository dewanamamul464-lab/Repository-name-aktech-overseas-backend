package com.aktech.overseas.service;

import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.EmployerStatus;
import com.aktech.overseas.repository.EmployerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final EmployerRepository employerRepository;
    private final EmailService emailService;

    public AdminService(
            EmployerRepository employerRepository,
            EmailService emailService) {

        this.employerRepository = employerRepository;
        this.emailService = emailService;
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

    @Transactional
    public Employer approveEmployer(Long employerId) {

        Employer employer = employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: "
                                        + employerId
                        )
                );

        // -----------------------------------------------------
        // Update approval status
        // -----------------------------------------------------

        employer.setStatus(EmployerStatus.APPROVED);

        Employer savedEmployer =
                employerRepository.save(employer);

        // -----------------------------------------------------
        // Send approval email AFTER successful database save
        // -----------------------------------------------------

        try {

            emailService.sendEmployerApprovalEmail(
                    savedEmployer.getEmail(),
                    savedEmployer.getContactPerson(),
                    savedEmployer.getCompanyName()
            );

        } catch (Exception e) {

            System.out.println(
                    "Employer approved, but approval email "
                            + "could not be sent: "
                            + e.getMessage()
            );
        }

        return savedEmployer;
    }

    // =========================================================
    // REJECT EMPLOYER
    // =========================================================

    @Transactional
    public Employer rejectEmployer(Long employerId) {

        Employer employer = employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: "
                                        + employerId
                        )
                );

        // -----------------------------------------------------
        // Update rejection status
        // -----------------------------------------------------

        employer.setStatus(EmployerStatus.REJECTED);

        Employer savedEmployer =
                employerRepository.save(employer);

        // -----------------------------------------------------
        // Send rejection email AFTER successful database save
        // -----------------------------------------------------

        try {

            emailService.sendEmployerRejectionEmail(
                    savedEmployer.getEmail(),
                    savedEmployer.getContactPerson(),
                    savedEmployer.getCompanyName()
            );

        } catch (Exception e) {

            System.out.println(
                    "Employer rejected, but rejection email "
                            + "could not be sent: "
                            + e.getMessage()
            );
        }

        return savedEmployer;
    }
}