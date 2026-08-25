package com.aktech.overseas.service;

import com.aktech.overseas.dto.ApplicationDTO;
import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.entity.ApplicationStatus;
import com.aktech.overseas.entity.Job;
import com.aktech.overseas.entity.JobApplication;
import com.aktech.overseas.repository.ApplicantRepository;
import com.aktech.overseas.repository.JobApplicationRepository;
import com.aktech.overseas.repository.JobRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final ApplicantRepository applicantRepository;
    private final JobRepository jobRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            ApplicantRepository applicantRepository,
            JobRepository jobRepository) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.applicantRepository = applicantRepository;
        this.jobRepository = jobRepository;
    }

    // =========================================================
    // APPLY FOR JOB
    // =========================================================

    public ApplicationDTO apply(ApplicationDTO dto) {

        if (dto == null) {
            throw new RuntimeException(
                    "Application data is required."
            );
        }

        if (dto.getJobId() == null) {
            throw new RuntimeException(
                    "Job ID is required."
            );
        }

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated."
            );
        }

        String email = authentication.getName();

        Applicant applicant =
                applicantRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Applicant not found."
                                )
                        );

        Job job =
                jobRepository
                        .findById(dto.getJobId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found with id: "
                                                + dto.getJobId()
                                )
                        );

        // Prevent duplicate application
        if (jobApplicationRepository
                .existsByApplicantIdAndJobId(
                        applicant.getId(),
                        job.getId())) {

            throw new RuntimeException(
                    "You have already applied for this job."
            );
        }

        JobApplication application =
                new JobApplication();

        application.setApplicant(applicant);
        application.setJob(job);

        // ApplicationStatus is an enum
        application.setStatus(
                ApplicationStatus.PENDING
        );

        JobApplication saved =
                jobApplicationRepository.save(application);

        return convertToDTO(saved);
    }

    // =========================================================
    // GET CURRENT APPLICANT APPLICATIONS
    // =========================================================

    public List<ApplicationDTO> getMyApplications() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated."
            );
        }

        String email = authentication.getName();

        Applicant applicant =
                applicantRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Applicant not found."
                                )
                        );

        return jobApplicationRepository
                .findByApplicantId(applicant.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET ALL APPLICATIONS
    // =========================================================

    public List<ApplicationDTO> getAllApplications() {

        return jobApplicationRepository
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET APPLICATION BY ID
    // =========================================================

    public ApplicationDTO getApplicationById(Long id) {

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found with id: "
                                                + id
                                )
                        );

        return convertToDTO(application);
    }

    // =========================================================
    // APPROVE APPLICATION
    // =========================================================

    public ApplicationDTO approveApplication(Long id) {

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found with id: "
                                                + id
                                )
                        );

        application.setStatus(
                ApplicationStatus.APPROVED
        );

        JobApplication updated =
                jobApplicationRepository.save(application);

        return convertToDTO(updated);
    }

    // =========================================================
    // REJECT APPLICATION
    // =========================================================

    public ApplicationDTO rejectApplication(Long id) {

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found with id: "
                                                + id
                                )
                        );

        application.setStatus(
                ApplicationStatus.REJECTED
        );

        JobApplication updated =
                jobApplicationRepository.save(application);

        return convertToDTO(updated);
    }

    // =========================================================
    // DELETE APPLICATION
    // =========================================================

    public void deleteApplication(Long id) {

        if (!jobApplicationRepository.existsById(id)) {

            throw new RuntimeException(
                    "Application not found with id: "
                            + id
            );
        }

        jobApplicationRepository.deleteById(id);
    }

    // =========================================================
    // ENTITY → DTO
    // =========================================================

    private ApplicationDTO convertToDTO(
            JobApplication application) {

        ApplicationDTO dto =
                new ApplicationDTO();

        dto.setId(application.getId());

        // -----------------------------------------------------
        // Applicant
        // -----------------------------------------------------

        if (application.getApplicant() != null) {

            dto.setApplicantId(
                    application
                            .getApplicant()
                            .getId()
            );
        }

        // -----------------------------------------------------
        // Job
        // -----------------------------------------------------

        if (application.getJob() != null) {

            dto.setJobId(
                    application
                            .getJob()
                            .getId()
            );

            dto.setCompany(
                    application
                            .getJob()
                            .getCompany()
            );
        }

        // -----------------------------------------------------
        // Status
        // -----------------------------------------------------

        if (application.getStatus() != null) {
            dto.setStatus(application.getStatus());
        }

        return dto;
    }
}