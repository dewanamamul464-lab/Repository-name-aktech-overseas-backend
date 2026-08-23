package com.aktech.overseas.service;

import com.aktech.overseas.dto.ApplicationDTO;
import com.aktech.overseas.entity.ApplicationStatus;
import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.Job;
import com.aktech.overseas.entity.JobApplication;
import com.aktech.overseas.repository.ApplicantRepository;
import com.aktech.overseas.repository.EmployerRepository;
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
    private final EmployerRepository employerRepository;
    private final EmailService emailService;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            ApplicantRepository applicantRepository,
            JobRepository jobRepository,
            EmployerRepository employerRepository,
            EmailService emailService) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.applicantRepository = applicantRepository;
        this.jobRepository = jobRepository;
        this.employerRepository = employerRepository;
        this.emailService = emailService;
    }

    // =========================================================
    // APPLY FOR JOB
    // =========================================================

    public ApplicationDTO apply(ApplicationDTO dto) {

        if (dto.getJobId() == null) {
            throw new RuntimeException("Job ID is required.");
        }

        // Get currently logged-in username
        String username = getCurrentUsername();

        // Find applicant using logged-in user
        Applicant applicant = applicantRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Applicant profile not found."));

        // Find job
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() ->
                        new RuntimeException("Job not found."));

        // Prevent duplicate application
        if (jobApplicationRepository
                .findByApplicantIdAndJobId(
                        applicant.getId(),
                        job.getId()
                )
                .isPresent()) {

            throw new RuntimeException(
                    "You have already applied for this job."
            );
        }

        // Create application
        JobApplication application = new JobApplication();

        application.setApplicant(applicant);
        application.setJob(job);
        application.setStatus(ApplicationStatus.PENDING);

        application = jobApplicationRepository.save(application);

        // Send email notification
        emailService.sendApplicationSubmittedEmail(
                applicant.getEmail(),
                applicant.getFullName(),
                job.getCompany(),
                job.getPosition()
        );

        return convertToDTO(application);
    }

    // =========================================================
    // APPLICANT: VIEW OWN APPLICATIONS
    // =========================================================

    public List<ApplicationDTO> getMyApplications() {

        String username = getCurrentUsername();

        Applicant applicant = applicantRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Applicant profile not found."));

        return jobApplicationRepository
                .findByApplicantId(applicant.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // EMPLOYER / ADMIN: VIEW ALL APPLICATIONS
    // =========================================================

    public List<ApplicationDTO> getAllApplications() {

        return jobApplicationRepository
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // EMPLOYER / ADMIN: VIEW APPLICATION BY ID
    // =========================================================

    public ApplicationDTO getApplicationById(Long id) {

        JobApplication application = jobApplicationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found."));

        return convertToDTO(application);
    }

    // =========================================================
    // EMPLOYER / ADMIN: APPROVE APPLICATION
    // =========================================================

    public ApplicationDTO approveApplication(Long id) {

        JobApplication application = jobApplicationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found."));

        // Check whether current user is allowed to approve
        validateEmployerAccess(application);

        application.setStatus(ApplicationStatus.APPROVED);

        application = jobApplicationRepository.save(application);

        // Send status email
        sendStatusEmail(application);

        return convertToDTO(application);
    }

    // =========================================================
    // EMPLOYER / ADMIN: REJECT APPLICATION
    // =========================================================

    public ApplicationDTO rejectApplication(Long id) {

        JobApplication application = jobApplicationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found."));

        // Check whether current user is allowed to reject
        validateEmployerAccess(application);

        application.setStatus(ApplicationStatus.REJECTED);

        application = jobApplicationRepository.save(application);

        // Send status email
        sendStatusEmail(application);

        return convertToDTO(application);
    }

    // =========================================================
    // ADMIN: DELETE APPLICATION
    // =========================================================

    public void deleteApplication(Long id) {

        JobApplication application = jobApplicationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found."));

        jobApplicationRepository.delete(application);
    }

    // =========================================================
    // VALIDATE EMPLOYER ACCESS
    // =========================================================

    private void validateEmployerAccess(JobApplication application) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated.");
        }

        // ADMIN can approve/reject any application
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return;
        }

        // Get currently logged-in employer
        String username = authentication.getName();

        Employer employer = employerRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Employer profile not found."));

        // Get the job associated with this application
        Job job = application.getJob();

        if (job == null) {
            throw new RuntimeException(
                    "Application is not associated with a job."
            );
        }

        // Check whether this employer owns the job
        Employer jobEmployer = job.getEmployer();

        if (jobEmployer == null ||
                !jobEmployer.getId().equals(employer.getId())) {

            throw new RuntimeException(
                    "You are not authorized to manage this application."
            );
        }
    }

    // =========================================================
    // SEND APPLICATION STATUS EMAIL
    // =========================================================

    private void sendStatusEmail(JobApplication application) {

        Applicant applicant = application.getApplicant();
        Job job = application.getJob();

        emailService.sendApplicationStatusEmail(
                applicant.getEmail(),
                applicant.getFullName(),
                job.getPosition(),
                application.getStatus().name()
        );
    }

    // =========================================================
    // CONVERT ENTITY -> DTO
    // =========================================================

    private ApplicationDTO convertToDTO(JobApplication application) {

        Applicant applicant = application.getApplicant();
        Job job = application.getJob();

        return new ApplicationDTO(
                application.getId(),
                applicant.getId(),
                applicant.getFullName(),
                job.getId(),
                job.getCompany(),
                job.getPosition(),
                application.getStatus()
        );
    }

    // =========================================================
    // GET CURRENT LOGGED-IN USERNAME
    // =========================================================

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated.");
        }

        return authentication.getName();
    }
}