package com.aktech.overseas.repository;

import com.aktech.overseas.entity.ApplicationStatus;
import com.aktech.overseas.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {


    Optional<JobApplication> findByApplicantIdAndJobId(
            Long applicantId,
            Long jobId
    );


    List<JobApplication> findByApplicantId(
            Long applicantId
    );


    List<JobApplication> findByJobId(
            Long jobId
    );


    // Employer view applicants for their jobs
    List<JobApplication> findByJobEmployerId(
            Long employerId
    );


    long countByStatus(
            ApplicationStatus status
    );
}