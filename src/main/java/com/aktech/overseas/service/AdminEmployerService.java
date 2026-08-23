package com.aktech.overseas.service;

import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.EmployerStatus;
import com.aktech.overseas.repository.EmployerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminEmployerService {

    private final EmployerRepository employerRepository;
    private final EmailService emailService;

    public AdminEmployerService(
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
    // GET EMPLOYER BY ID
    // =========================================================

    public Employer getEmployerById(Long id) {

        return employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + id
                        )
                );
    }

    // =========================================================
    // APPROVE EMPLOYER
    // =========================================================

    public Employer approveEmployer(Long id) {

        Employer employer = employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + id
                        )
                );

        // -----------------------------------------------------
        // Already approved
        // -----------------------------------------------------

        if (employer.getStatus()
                == EmployerStatus.APPROVED) {

            throw new RuntimeException(
                    "Employer account is already approved."
            );
        }

        // -----------------------------------------------------
        // Set approved
        // -----------------------------------------------------

        employer.setStatus(
                EmployerStatus.APPROVED
        );

        Employer savedEmployer =
                employerRepository.save(employer);

        // -----------------------------------------------------
        // Send approval email
        // -----------------------------------------------------

        emailService.sendEmployerApprovalEmail(
                employer.getEmail(),
                employer.getContactPerson(),
                employer.getCompanyName()
        );

        return savedEmployer;
    }

    // =========================================================
    // REJECT EMPLOYER
    // =========================================================

    public Employer rejectEmployer(Long id) {

        Employer employer = employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + id
                        )
                );

        // -----------------------------------------------------
        // Already rejected
        // -----------------------------------------------------

        if (employer.getStatus()
                == EmployerStatus.REJECTED) {

            throw new RuntimeException(
                    "Employer account is already rejected."
            );
        }

        // -----------------------------------------------------
        // Set rejected
        // -----------------------------------------------------

        employer.setStatus(
                EmployerStatus.REJECTED
        );

        Employer savedEmployer =
                employerRepository.save(employer);

        // -----------------------------------------------------
        // Send rejection email
        // -----------------------------------------------------

        emailService.sendEmployerRejectionEmail(
                employer.getEmail(),
                employer.getContactPerson(),
                employer.getCompanyName()
        );

        return savedEmployer;
    }

    // =========================================================
    // DELETE EMPLOYER
    // =========================================================

    public void deleteEmployer(Long id) {

        Employer employer = employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + id
                        )
                );

        employerRepository.delete(employer);
    }
}