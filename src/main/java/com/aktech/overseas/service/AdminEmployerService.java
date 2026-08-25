package com.aktech.overseas.service;

import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.EmployerStatus;
import com.aktech.overseas.entity.Job;
import com.aktech.overseas.entity.User;
import com.aktech.overseas.repository.EmployerRepository;
import com.aktech.overseas.repository.JobRepository;
import com.aktech.overseas.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminEmployerService {

    private final EmployerRepository employerRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public AdminEmployerService(
            EmployerRepository employerRepository,
            JobRepository jobRepository,
            UserRepository userRepository,
            EmailService emailService) {

        this.employerRepository = employerRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public Employer approveEmployer(Long id) {

        // -----------------------------------------------------
        // Find employer
        // -----------------------------------------------------

        Employer employer =
                employerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employer not found with id: "
                                                + id
                                )
                        );

        // -----------------------------------------------------
        // Check current status
        // -----------------------------------------------------

        if (employer.getStatus()
                == EmployerStatus.APPROVED) {

            throw new RuntimeException(
                    "Employer account is already approved."
            );
        }

        // -----------------------------------------------------
        // Approve employer
        // -----------------------------------------------------

        employer.setStatus(
                EmployerStatus.APPROVED
        );

        // -----------------------------------------------------
        // Save approval to database
        // -----------------------------------------------------

        Employer savedEmployer =
                employerRepository.save(employer);

        // -----------------------------------------------------
        // Send approval email
        //
        // IMPORTANT:
        // Email failure must NOT make the employer approval
        // operation fail.
        // -----------------------------------------------------

        try {

            emailService.sendEmployerApprovalEmail(
                    savedEmployer.getEmail(),
                    savedEmployer.getContactPerson(),
                    savedEmployer.getCompanyName()
            );

            System.out.println(
                    "Employer approval email sent successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Employer approved successfully, "
                            + "but approval email could not be sent."
            );

            System.out.println(
                    "Email error: " + e.getMessage()
            );
        }

        // -----------------------------------------------------
        // Return approved employer
        // -----------------------------------------------------

        return savedEmployer;
    }

    // =========================================================
    // REJECT EMPLOYER
    // =========================================================

    @Transactional
    public Employer rejectEmployer(Long id) {

        // -----------------------------------------------------
        // Find employer
        // -----------------------------------------------------

        Employer employer =
                employerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employer not found with id: "
                                                + id
                                )
                        );

        // -----------------------------------------------------
        // Check current status
        // -----------------------------------------------------

        if (employer.getStatus()
                == EmployerStatus.REJECTED) {

            throw new RuntimeException(
                    "Employer account is already rejected."
            );
        }

        // -----------------------------------------------------
        // Reject employer
        // -----------------------------------------------------

        employer.setStatus(
                EmployerStatus.REJECTED
        );

        // -----------------------------------------------------
        // Save rejection
        // -----------------------------------------------------

        Employer savedEmployer =
                employerRepository.save(employer);

        // -----------------------------------------------------
        // Send rejection email
        //
        // Email failure must NOT make the rejection operation
        // fail.
        // -----------------------------------------------------

        try {

            emailService.sendEmployerRejectionEmail(
                    savedEmployer.getEmail(),
                    savedEmployer.getContactPerson(),
                    savedEmployer.getCompanyName()
            );

            System.out.println(
                    "Employer rejection email sent successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Employer rejected successfully, "
                            + "but rejection email could not be sent."
            );

            System.out.println(
                    "Email error: " + e.getMessage()
            );
        }

        // -----------------------------------------------------
        // Return rejected employer
        // -----------------------------------------------------

        return savedEmployer;
    }

    // =========================================================
    // DELETE EMPLOYER - COMPLETE ACCOUNT DELETION
    // =========================================================

    @Transactional
    public void deleteEmployer(Long id) {

        // -----------------------------------------------------
        // Find employer
        // -----------------------------------------------------

        Employer employer =
                employerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employer not found with id: "
                                                + id
                                )
                        );

        // -----------------------------------------------------
        // Get linked User before deleting Employer
        // -----------------------------------------------------

        User user = employer.getUser();

        // -----------------------------------------------------
        // Delete all jobs belonging to this employer
        //
        // Job has:
        //
        // @OneToMany(
        //     cascade = CascadeType.ALL,
        //     orphanRemoval = true
        // )
        //
        // Therefore deleting the jobs also removes their
        // applications.
        // -----------------------------------------------------

        List<Job> jobs =
                jobRepository.findByEmployer_Id(id);

        if (jobs != null && !jobs.isEmpty()) {

            jobRepository.deleteAll(jobs);
        }

        // -----------------------------------------------------
        // Delete Employer profile
        // -----------------------------------------------------

        employerRepository.delete(employer);

        // -----------------------------------------------------
        // Delete linked User account
        //
        // This removes the old username from users as well.
        // -----------------------------------------------------

        if (user != null) {

            userRepository.delete(user);
        }
    }
}