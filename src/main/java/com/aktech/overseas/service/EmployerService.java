package com.aktech.overseas.service;

import com.aktech.overseas.dto.ApplicationDTO;
import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.entity.ApplicationStatus;
import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.Job;
import com.aktech.overseas.entity.JobApplication;
import com.aktech.overseas.repository.EmployerRepository;
import com.aktech.overseas.repository.JobApplicationRepository;
import com.aktech.overseas.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public EmployerService(
            EmployerRepository employerRepository,
            JobRepository jobRepository,
            JobApplicationRepository jobApplicationRepository) {

        this.employerRepository = employerRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    // =========================================================
    // GET CURRENT EMPLOYER PROFILE
    // =========================================================

    public Employer getMyProfile(String username) {

        return employerRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer profile not found"
                        )
                );
    }

    // =========================================================
    // EMPLOYER POSTS A JOB
    // =========================================================

    public Job postJob(
            Long employerId,
            Job job) {

        Employer employer = employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found"
                        )
                );

        job.setEmployer(employer);

        return jobRepository.save(job);
    }

    // =========================================================
    // GET EMPLOYER'S JOBS
    // =========================================================

    public List<Job> getMyJobs(
            Long employerId) {

        employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found"
                        )
                );

        return jobRepository.findByEmployer_Id(employerId);
    }

    // =========================================================
    // GET EMPLOYER'S APPLICATIONS
    // =========================================================

    public List<ApplicationDTO> getApplications(Long employerId) {

        employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException("Employer not found")
                );

        return jobApplicationRepository
                .findByJobEmployerId(employerId)
                .stream()
                .map(application ->
                        new ApplicationDTO(
                                application.getId(),
                                application.getApplicant().getId(),
                                application.getApplicant().getFullName(),
                                application.getJob().getId(),
                                application.getJob().getCompany(),
                                application.getJob().getPosition(),
                                application.getStatus()
                        )
                )
                .toList();
    }

    // =========================================================
    // GET FULL APPLICANT DETAILS
    //
    // Employer can VIEW the applicant.
    // Employer cannot modify applicant information.
    // =========================================================

    public Map<String, Object> getApplicantDetails(
            Long employerId,
            Long applicationId) {

        // -----------------------------------------------------
        // Verify employer exists
        // -----------------------------------------------------

        employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found"
                        )
                );

        // -----------------------------------------------------
        // Find application
        // -----------------------------------------------------

        JobApplication application =
                jobApplicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"
                                )
                        );

        // -----------------------------------------------------
        // SECURITY CHECK
        //
        // Application must belong to a job posted
        // by this employer.
        // -----------------------------------------------------

        if (application.getJob() == null) {
            throw new RuntimeException(
                    "Job not found for application"
            );
        }

        if (application.getJob().getEmployer() == null) {
            throw new RuntimeException(
                    "Employer not found for job"
            );
        }

        if (!application
                .getJob()
                .getEmployer()
                .getId()
                .equals(employerId)) {

            throw new RuntimeException(
                    "Application does not belong to this employer"
            );
        }

        // -----------------------------------------------------
        // Get applicant
        // -----------------------------------------------------

        Applicant applicant =
                application.getApplicant();

        if (applicant == null) {
            throw new RuntimeException(
                    "Applicant not found"
            );
        }

        // -----------------------------------------------------
        // Build READ-ONLY response
        // -----------------------------------------------------

        Map<String, Object> result =
                new LinkedHashMap<>();

        // =====================================================
        // APPLICATION INFORMATION
        // =====================================================

        result.put(
                "applicationId",
                application.getId()
        );

        result.put(
                "applicationStatus",
                application.getStatus()
        );

        // =====================================================
        // JOB INFORMATION
        // =====================================================

        result.put(
                "jobId",
                application.getJob().getId()
        );

        result.put(
                "company",
                application.getJob().getCompany()
        );

        result.put(
                "position",
                application.getJob().getPosition()
        );

        // =====================================================
        // APPLICANT INFORMATION
        // =====================================================

        result.put(
                "id",
                applicant.getId()
        );

        result.put(
                "applicantId",
                applicant.getId()
        );

        result.put(
                "fullName",
                applicant.getFullName()
        );

        result.put(
                "email",
                applicant.getEmail()
        );

        result.put(
                "phone",
                applicant.getPhone()
        );

        result.put(
                "country",
                applicant.getCountry()
        );

        result.put(
                "experience",
                applicant.getExperience()
        );

        result.put(
                "skills",
                applicant.getSkills()
        );

        result.put(
                "passportNumber",
                applicant.getPassportNumber()
        );

        // =====================================================
        // CV
        // =====================================================

        result.put(
                "cvFileName",
                applicant.getCvFileName()
        );

        result.put(
                "cvUrl",
                applicant.getCvUrl()
        );

        result.put(
                "cvUploadedAt",
                applicant.getCvUploadedAt()
        );

        // =====================================================
        // PROFILE IMAGE
        // =====================================================

        result.put(
                "profileImage",
                applicant.getProfileImage()
        );

        return result;
    }

    // =========================================================
    // APPROVE APPLICATION
    // =========================================================

    public ApplicationDTO approveApplication(
            Long employerId,
            Long applicationId) {

        return updateApplicationStatus(
                employerId,
                applicationId,
                ApplicationStatus.APPROVED
        );
    }

    // =========================================================
    // REJECT APPLICATION
    // =========================================================

    public ApplicationDTO rejectApplication(
            Long employerId,
            Long applicationId) {

        return updateApplicationStatus(
                employerId,
                applicationId,
                ApplicationStatus.REJECTED
        );
    }

    // =========================================================
    // UPDATE APPLICATION STATUS
    // =========================================================

    private ApplicationDTO updateApplicationStatus(
            Long employerId,
            Long applicationId,
            ApplicationStatus status) {

        // -----------------------------------------------------
        // Verify employer
        // -----------------------------------------------------

        employerRepository
                .findById(employerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employer not found"
                        )
                );

        // -----------------------------------------------------
        // Find application
        // -----------------------------------------------------

        JobApplication application =
                jobApplicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"
                                )
                        );

        // -----------------------------------------------------
        // SECURITY CHECK
        // -----------------------------------------------------

        if (application.getJob() == null ||
                application.getJob().getEmployer() == null ||
                !application
                        .getJob()
                        .getEmployer()
                        .getId()
                        .equals(employerId)) {

            throw new RuntimeException(
                    "Application does not belong to this employer"
            );
        }

        // -----------------------------------------------------
        // Update status
        // -----------------------------------------------------

        application.setStatus(status);

        JobApplication saved =
                jobApplicationRepository.save(application);

        // -----------------------------------------------------
        // Return DTO
        // -----------------------------------------------------

        return new ApplicationDTO(
                saved.getId(),

                saved
                        .getApplicant()
                        .getId(),

                saved
                        .getApplicant()
                        .getFullName(),

                saved
                        .getJob()
                        .getId(),

                saved
                        .getJob()
                        .getCompany(),

                saved
                        .getJob()
                        .getPosition(),

                saved.getStatus()
        );
    }
}