package com.aktech.overseas.service;

import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.entity.JobApplication;
import com.aktech.overseas.entity.User;
import com.aktech.overseas.repository.ApplicantRepository;
import com.aktech.overseas.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminApplicantService {

    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;

    public AdminApplicantService(
            ApplicantRepository applicantRepository,
            UserRepository userRepository) {

        this.applicantRepository = applicantRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // GET ALL APPLICANTS
    // =========================================================

    public List<Applicant> getAllApplicants() {

        return applicantRepository.findAll();
    }

    // =========================================================
    // GET APPLICANT BY ID
    // =========================================================

    public Applicant getApplicantById(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Applicant ID is required."
            );
        }

        return applicantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Applicant not found with id: " + id
                        )
                );
    }

    // =========================================================
    // DELETE APPLICANT
    // =========================================================
    //
    // Deletes:
    // 1. Applicant profile
    // 2. Applicant's job applications
    // 3. Linked User account
    //
    // No approval/rejection is required for applicants.
    // =========================================================

    @Transactional
    public void deleteApplicant(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Applicant ID is required."
            );
        }

        // -----------------------------------------------------
        // Find applicant
        // -----------------------------------------------------

        Applicant applicant =
                applicantRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Applicant not found with id: "
                                                + id
                                )
                        );

        // -----------------------------------------------------
        // Get linked User before deleting applicant
        // -----------------------------------------------------

        User user = applicant.getUser();

        // -----------------------------------------------------
        // Delete applicant
        //
        // Applicant has:
        //
        // @OneToMany(
        //     mappedBy = "applicant",
        //     cascade = CascadeType.ALL,
        //     orphanRemoval = true
        // )
        //
        // Therefore the applicant's JobApplication records
        // are removed together with the applicant.
        // -----------------------------------------------------

        applicantRepository.delete(applicant);

        // -----------------------------------------------------
        // Delete linked User account
        // -----------------------------------------------------

        if (user != null) {

            userRepository.delete(user);
        }
    }
}