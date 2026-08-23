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
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public AdminEmployerService(
            EmployerRepository employerRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            EmailService emailService) {

        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
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
    //
    // Deletes the COMPLETE employer account:
    //
    // 1. Employer's job applications
    // 2. Employer's jobs
    // 3. Employer profile
    // 4. Employer User/login account
    //
    // After deletion:
    //
    // - Employer disappears from admin
    // - Username becomes available again
    // - Email becomes available again
    // - Employer can register again
    // =========================================================

    @Transactional
    public void deleteEmployer(Long id) {

        // -----------------------------------------------------
        // Find employer
        // -----------------------------------------------------

        Employer employer = employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + id
                        )
                );

        // -----------------------------------------------------
        // Save associated User before deleting Employer
        // -----------------------------------------------------

        User user = employer.getUser();

        // -----------------------------------------------------
        // Find all jobs belonging to this employer
        // -----------------------------------------------------

        List<Job> jobs =
                jobRepository.findByEmployerId(id);

        // -----------------------------------------------------
        // Delete employer's jobs
        //
        // Job has:
        //
        // @OneToMany(
        //     mappedBy = "job",
        //     cascade = CascadeType.ALL,
        //     orphanRemoval = true
        // )
        //
        // Therefore the applications belonging to these jobs
        // are deleted together with the jobs.
        // -----------------------------------------------------

        if (!jobs.isEmpty()) {

            jobRepository.deleteAll(jobs);
        }

        // -----------------------------------------------------
        // Delete employer profile
        // -----------------------------------------------------

        employerRepository.delete(employer);

        // -----------------------------------------------------
        // Delete User/login account
        //
        // This is required so the old username can be reused.
        // -----------------------------------------------------

        if (user != null) {

            userRepository.delete(user);
        }
    }
}