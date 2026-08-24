package com.aktech.overseas.repository;

import com.aktech.overseas.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // =========================================================
    // APPLICANT + JOB
    // =========================================================

    Optional<JobApplication> findByApplicantIdAndJobId(
            Long applicantId,
            Long jobId
    );

    // =========================================================
    // APPLICANT APPLICATIONS
    // =========================================================

    List<JobApplication> findByApplicantId(
            Long applicantId
    );

    // =========================================================
    // JOB APPLICATIONS
    // =========================================================

    List<JobApplication> findByJobId(
            Long jobId
    );

    // =========================================================
    // EMPLOYER'S JOB APPLICATIONS
    // =========================================================

    List<JobApplication> findByJobEmployerId(
            Long employerId
    );

    // =========================================================
    // DELETE APPLICANT APPLICATIONS
    // =========================================================

    void deleteByApplicantId(
            Long applicantId
    );

    // =========================================================
    // DELETE JOB APPLICATIONS
    // =========================================================

    void deleteByJobId(
            Long jobId
    );

    // =========================================================
    // STATUS
    // =========================================================

    long countByStatus(
            com.aktech.overseas.entity.ApplicationStatus status
    );
}