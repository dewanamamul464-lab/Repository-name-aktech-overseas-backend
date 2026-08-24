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

    public Employer approveEmployer(Long id) {

        Employer employer = employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + id
                        )
                );

        if (employer.getStatus()
                == EmployerStatus.APPROVED) {

            throw new RuntimeException(
                    "Employer account is already approved."
            );
        }

        employer.setStatus(
                EmployerStatus.APPROVED
        );

        Employer savedEmployer =
                employerRepository.save(employer);

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

        if (employer.getStatus()
                == EmployerStatus.REJECTED) {

            throw new RuntimeException(
                    "Employer account is already rejected."
            );
        }

        employer.setStatus(
                EmployerStatus.REJECTED
        );

        Employer savedEmployer =
                employerRepository.save(employer);

        emailService.sendEmployerRejectionEmail(
                employer.getEmail(),
                employer.getContactPerson(),
                employer.getCompanyName()
        );

        return savedEmployer;
    }

    // =========================================================
    // DELETE EMPLOYER - COMPLETE ACCOUNT DELETION
    // =========================================================

    @Transactional
    public void deleteEmployer(Long id) {

        Employer employer = employerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found with id: " + id
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
        // @OneToMany(cascade = CascadeType.ALL,
        //            orphanRemoval = true)
        // for JobApplication.
        //
        // Therefore deleting the jobs also removes their
        // applications.
        // -----------------------------------------------------

        List<Job> jobs =
                jobRepository.findByEmployerId(id);

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
        // This is the important part that was missing before.
        // Without this, the old username remains in users.
        // -----------------------------------------------------

        if (user != null) {

            userRepository.delete(user);
        }
    }
}