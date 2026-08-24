package com.aktech.overseas.service;

import com.aktech.overseas.dto.ApplicationDTO;
import com.aktech.overseas.entity.ApplicationStatus;
import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.entity.Job;
import com.aktech.overseas.entity.JobApplication;
import com.aktech.overseas.repository.ApplicantRepository;
import com.aktech.overseas.repository.JobApplicationRepository;
import com.aktech.overseas.repository.JobRepository;
import com.aktech.overseas.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final UserRepository userRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            ApplicantRepository applicantRepository,
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.applicantRepository = applicantRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // APPLY FOR JOB
    // =========================================================

    @Transactional
    public ApplicationDTO apply(ApplicationDTO dto) {

        if (dto == null) {
            throw new RuntimeException("Application data is required");
        }

        if (dto.getJobId() == null) {
            throw new RuntimeException("Job ID is required");
        }

        // -----------------------------------------------------
        // GET CURRENT LOGGED-IN USER
        // -----------------------------------------------------

        String username = getCurrentUsername();

        Applicant applicant = applicantRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Applicant profile not found"
                        )
                );

        // -----------------------------------------------------
        // FIND JOB
        // -----------------------------------------------------

        Job job = jobRepository
                .findById(dto.getJobId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found"
                        )
                );

        // -----------------------------------------------------
        // PREVENT DUPLICATE APPLICATION
        // -----------------------------------------------------

        if (jobApplicationRepository
                .findByApplicantIdAndJobId(
                        applicant.getId(),
                        job.getId()
                )
                .isPresent()) {

            throw new RuntimeException(
                    "You have already applied for this job"
            );
        }

        // -----------------------------------------------------
        // CREATE APPLICATION
        // -----------------------------------------------------

        JobApplication application = new JobApplication();

        application.setApplicant(applicant);
        application.setJob(job);

        // New applications start as PENDING
        application.setStatus(ApplicationStatus.PENDING);

        application = jobApplicationRepository.save(application);

        return convertToDTO(application);
    }

    // =========================================================
    // GET MY APPLICATIONS
    // =========================================================

    public List<ApplicationDTO> getMyApplications() {

        String username = getCurrentUsername();

        Applicant applicant = applicantRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Applicant profile not found"
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
                    "Application ID is required"
            );
        }

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"
                                )
                        );

        return convertToDTO(application);
    }

    // =========================================================
    // APPROVE APPLICATION
    // =========================================================

    @Transactional
    public ApplicationDTO approveApplication(Long id) {

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"
                                )
                        );

        application.setStatus(
                ApplicationStatus.APPROVED
        );

        application =
                jobApplicationRepository.save(application);

        return convertToDTO(application);
    }

    // =========================================================
    // REJECT APPLICATION
    // =========================================================

    @Transactional
    public ApplicationDTO rejectApplication(Long id) {

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"
                                )
                        );

        application.setStatus(
                ApplicationStatus.REJECTED
        );

        application =
                jobApplicationRepository.save(application);

        return convertToDTO(application);
    }

    // =========================================================
    // DELETE APPLICATION
    // =========================================================

    @Transactional
    public void deleteApplication(Long id) {

        JobApplication application =
                jobApplicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"
                                )
                        );

        jobApplicationRepository.delete(application);
    }

    // =========================================================
    // GET APPLICATIONS BY JOB
    // =========================================================

    public List<ApplicationDTO> getApplicationsByJob(
            Long jobId) {

        return jobApplicationRepository
                .findByJobId(jobId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // DELETE APPLICATIONS BY APPLICANT
    // =========================================================

    @Transactional
    public void deleteApplicationsByApplicant(
            Long applicantId) {

        jobApplicationRepository
                .deleteByApplicantId(applicantId);
    }

    // =========================================================
    // DELETE APPLICATIONS BY JOB
    // =========================================================

    @Transactional
    public void deleteApplicationsByJob(
            Long jobId) {

        jobApplicationRepository
                .deleteByJobId(jobId);
    }

    // =========================================================
    // CONVERT ENTITY -> DTO
    // =========================================================

    private ApplicationDTO convertToDTO(
            JobApplication application) {

        ApplicationDTO dto = new ApplicationDTO();

        dto.setId(application.getId());

        // -----------------------------------------------------
        // APPLICANT
        // -----------------------------------------------------

        if (application.getApplicant() != null) {

            dto.setApplicantId(
                    application.getApplicant().getId()
            );

            dto.setApplicantName(
                    application.getApplicant().getFullName()
            );
        }

        // -----------------------------------------------------
        // JOB
        // -----------------------------------------------------

        if (application.getJob() != null) {

            dto.setJobId(
                    application.getJob().getId()
            );

            dto.setCompany(
                    application.getJob().getCompany()
            );

            dto.setPosition(
                    application.getJob().getPosition()
            );
        }

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        dto.setStatus(
                application.getStatus()
        );

        return dto;
    }

    // =========================================================
    // GET CURRENT USERNAME FROM JWT / SECURITY CONTEXT
    // =========================================================

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        String username =
                authentication.getName();

        if (username == null ||
                username.isBlank()) {

            throw new RuntimeException(
                    "Unable to identify logged-in user"
            );
        }

        return username;
    }
}