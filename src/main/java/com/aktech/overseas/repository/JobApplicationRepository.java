package com.aktech.overseas.repository;

import com.aktech.overseas.entity.ApplicationStatus;
import com.aktech.overseas.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // =========================================================
    // CHECK IF APPLICANT ALREADY APPLIED FOR JOB
    // =========================================================

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM JobApplication a
            WHERE a.applicant.id = :applicantId
              AND a.job.id = :jobId
            """)
    boolean existsByApplicantIdAndJobId(
            @Param("applicantId") Long applicantId,
            @Param("jobId") Long jobId
    );

    // =========================================================
    // FIND APPLICATION BY APPLICANT AND JOB
    // =========================================================

    @Query("""
            SELECT a
            FROM JobApplication a
            WHERE a.applicant.id = :applicantId
              AND a.job.id = :jobId
            """)
    Optional<JobApplication> findByApplicantIdAndJobId(
            @Param("applicantId") Long applicantId,
            @Param("jobId") Long jobId
    );

    // =========================================================
    // GET ALL APPLICATIONS OF AN APPLICANT
    // =========================================================

    @Query("""
            SELECT a
            FROM JobApplication a
            WHERE a.applicant.id = :applicantId
            ORDER BY a.id DESC
            """)
    List<JobApplication> findByApplicantId(
            @Param("applicantId") Long applicantId
    );

    // =========================================================
    // GET APPLICATIONS FOR EMPLOYER'S JOBS
    // =========================================================

    @Query("""
            SELECT a
            FROM JobApplication a
            WHERE a.job.employer.id = :employerId
            ORDER BY a.id DESC
            """)
    List<JobApplication> findByJobEmployerId(
            @Param("employerId") Long employerId
    );

    // =========================================================
    // GET APPLICATIONS BY STATUS
    // =========================================================

    List<JobApplication> findByStatus(
            ApplicationStatus status
    );

    // =========================================================
    // COUNT APPLICATIONS BY STATUS
    // =========================================================

    long countByStatus(
            ApplicationStatus status
    );

    // =========================================================
    // COUNT APPLICANT APPLICATIONS
    // =========================================================

    @Query("""
            SELECT COUNT(a)
            FROM JobApplication a
            WHERE a.applicant.id = :applicantId
            """)
    long countByApplicantId(
            @Param("applicantId") Long applicantId
    );

    // =========================================================
    // COUNT EMPLOYER APPLICATIONS
    // =========================================================

    @Query("""
            SELECT COUNT(a)
            FROM JobApplication a
            WHERE a.job.employer.id = :employerId
            """)
    long countByJobEmployerId(
            @Param("employerId") Long employerId
    );
}