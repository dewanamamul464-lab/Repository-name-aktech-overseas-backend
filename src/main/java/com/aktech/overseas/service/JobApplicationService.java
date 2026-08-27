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
    private final EmailService emailService;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            ApplicantRepository applicantRepository,
            JobRepository jobRepository,
            EmailService emailService) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.applicantRepository = applicantRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
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

        // =====================================================
        // FIND CURRENT APPLICANT
        // =====================================================

        String username =
                authentication.getName();

        Applicant applicant =
                applicantRepository
                        .findByUserUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Applicant not found for username: "
                                                + username
                                )
                        );

        // =====================================================
        // FIND JOB
        // =====================================================

        Job job =
                jobRepository
                        .findById(dto.getJobId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found with id: "
                                                + dto.getJobId()
                                )
                        );

        // =====================================================
        // PREVENT DUPLICATE APPLICATION
        // =====================================================

        if (jobApplicationRepository
                .existsByApplicantIdAndJobId(
                        applicant.getId(),
                        job.getId())) {

            throw new RuntimeException(
                    "You have already applied for this job."
            );
        }

        // =====================================================
        // CREATE APPLICATION
        // =====================================================

        JobApplication application =
                new JobApplication();

        application.setApplicant(applicant);
        application.setJob(job);

        application.setStatus(
                ApplicationStatus.PENDING
        );

        JobApplication saved =
                jobApplicationRepository.save(application);

        // =====================================================
        // SEND APPLICATION CONFIRMATION EMAIL
        // =====================================================

        try {

            emailService.sendApplicationSubmittedEmail(
                    applicant.getEmail(),
                    applicant.getFullName(),
                    job.getCompany(),
                    saved.getId()
            );

        } catch (Exception e) {

            System.err.println(
                    "APPLICATION EMAIL FAILED: "
                            + e.getMessage()
            );
        }

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

        String username =
                authentication.getName();

        Applicant applicant =
                applicantRepository
                        .findByUserUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Applicant not found for username: "
                                                + username
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

        if (id == null) {
            throw new RuntimeException(
                    "Application ID is required."
            );
        }

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

        if (id == null) {
            throw new RuntimeException(
                    "Application ID is required."
            );
        }

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found with id: "
                                                + id
                                )
                        );

        // =====================================================
        // CHANGE STATUS
        // =====================================================

        application.setStatus(
                ApplicationStatus.APPROVED
        );

        JobApplication updated =
                jobApplicationRepository.save(application);

        // =====================================================
        // SEND APPROVAL EMAIL TO APPLICANT
        // =====================================================

        Applicant applicant =
                updated.getApplicant();

        Job job =
                updated.getJob();

        if (applicant != null
                && applicant.getEmail() != null
                && !applicant.getEmail().isBlank()) {

            try {

                emailService.sendApplicationApprovedEmail(
                        applicant.getEmail(),
                        applicant.getFullName(),
                        job != null
                                ? job.getCompany()
                                : "the company",
                        updated.getId()
                );

            } catch (Exception e) {

                System.err.println(
                        "APPLICATION APPROVAL EMAIL FAILED: "
                                + e.getMessage()
                );
            }
        }

        return convertToDTO(updated);
    }

    // =========================================================
    // REJECT APPLICATION
    // =========================================================

    public ApplicationDTO rejectApplication(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Application ID is required."
            );
        }

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found with id: "
                                                + id
                                )
                        );

        // =====================================================
        // CHANGE STATUS
        // =====================================================

        application.setStatus(
                ApplicationStatus.REJECTED
        );

        JobApplication updated =
                jobApplicationRepository.save(application);

        // =====================================================
        // SEND REJECTION EMAIL TO APPLICANT
        // =====================================================

        Applicant applicant =
                updated.getApplicant();

        Job job =
                updated.getJob();

        if (applicant != null
                && applicant.getEmail() != null
                && !applicant.getEmail().isBlank()) {

            try {

                emailService.sendApplicationRejectedEmail(
                        applicant.getEmail(),
                        applicant.getFullName(),
                        job != null
                                ? job.getCompany()
                                : "the company",
                        updated.getId()
                );

            } catch (Exception e) {

                System.err.println(
                        "APPLICATION REJECTION EMAIL FAILED: "
                                + e.getMessage()
                );
            }
        }

        return convertToDTO(updated);
    }

    // =========================================================
    // DELETE APPLICATION
    // =========================================================

    public void deleteApplication(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Application ID is required."
            );
        }

        if (!jobApplicationRepository.existsById(id)) {

            throw new RuntimeException(
                    "Application not found with id: "
                            + id
            );
        }

        jobApplicationRepository.deleteById(id);
    }

    // =========================================================
    // ENTITY -> DTO
    // =========================================================

    private ApplicationDTO convertToDTO(
            JobApplication application) {

        ApplicationDTO dto =
                new ApplicationDTO();

        // =====================================================
        // APPLICATION ID
        // =====================================================

        dto.setId(
                application.getId()
        );

        // =====================================================
        // APPLICANT
        // =====================================================

        if (application.getApplicant() != null) {

            dto.setApplicantId(
                    application
                            .getApplicant()
                            .getId()
            );
        }

        // =====================================================
        // JOB
        // =====================================================

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

        // =====================================================
        // STATUS
        // =====================================================

        if (application.getStatus() != null) {

            dto.setStatus(
                    application.getStatus()
            );
        }

        return dto;
    }
}