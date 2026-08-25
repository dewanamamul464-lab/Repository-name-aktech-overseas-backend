package com.aktech.overseas.repository;

import com.aktech.overseas.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // =========================================================
    // FIND JOBS BY EMPLOYER
    // =========================================================

    List<Job> findByEmployer_Id(Long employerId);

    Page<Job> findByEmployer_Id(
            Long employerId,
            Pageable pageable
    );

    // =========================================================
    // ALL JOBS - NEWEST FIRST
    // =========================================================

    Page<Job> findAllByOrderByIdDesc(
            Pageable pageable
    );

    // =========================================================
    // JOBS BY JOB TYPE
    // =========================================================

    List<Job> findByJobTypeIgnoreCase(
            String jobType
    );

    Page<Job> findByJobTypeIgnoreCase(
            String jobType,
            Pageable pageable
    );

    // =========================================================
    // JOBS BY TYPE - PAGINATED
    // Used by JobService.getJobs()
    // =========================================================

    @Query("""
            SELECT j
            FROM Job j
            WHERE LOWER(j.jobType) = LOWER(:jobType)
            """)
    Page<Job> findJobsByType(
            @Param("jobType") String jobType,
            Pageable pageable
    );

    // =========================================================
    // SEARCH + OPTIONAL JOB TYPE
    // Used by JobService.getJobs()
    // =========================================================

    @Query("""
            SELECT j
            FROM Job j
            WHERE
                (
                    LOWER(j.position) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(j.company) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(j.country) LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND (
                    :jobType = ''
                    OR LOWER(j.jobType) = LOWER(:jobType)
                )
            """)
    Page<Job> searchJobsWithType(
            @Param("search") String search,
            @Param("jobType") String jobType,
            Pageable pageable
    );

    // =========================================================
    // VERIFIED JOBS
    // =========================================================

    List<Job> findByVerifiedTrue();

    Page<Job> findByVerifiedTrue(
            Pageable pageable
    );

    // =========================================================
    // VERIFIED JOBS BY TYPE
    // =========================================================

    List<Job> findByVerifiedTrueAndJobTypeIgnoreCase(
            String jobType
    );

    Page<Job> findByVerifiedTrueAndJobTypeIgnoreCase(
            String jobType,
            Pageable pageable
    );

    // =========================================================
    // COUNTRY
    // =========================================================

    List<Job> findByCountryIgnoreCase(
            String country
    );

    // =========================================================
    // COMPANY
    // =========================================================

    List<Job> findByCompanyIgnoreCase(
            String company
    );

    // =========================================================
    // POSITION
    // =========================================================

    List<Job> findByPositionIgnoreCase(
            String position
    );

    // =========================================================
    // SEARCH JOBS
    // =========================================================

    @Query("""
            SELECT j
            FROM Job j
            WHERE
                LOWER(j.position) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.company) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.country) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    List<Job> searchJobs(
            @Param("search") String search
    );

    // =========================================================
    // SEARCH JOBS - PAGINATED
    // =========================================================

    @Query("""
            SELECT j
            FROM Job j
            WHERE
                LOWER(j.position) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.company) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.country) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<Job> searchJobs(
            @Param("search") String search,
            Pageable pageable
    );

    // =========================================================
    // SEARCH + JOB TYPE
    // =========================================================

    @Query("""
            SELECT j
            FROM Job j
            WHERE
                (
                    LOWER(j.position) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(j.company) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(j.country) LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND LOWER(j.jobType) = LOWER(:jobType)
            """)
    List<Job> searchJobsByJobType(
            @Param("search") String search,
            @Param("jobType") String jobType
    );

    // =========================================================
    // SEARCH + JOB TYPE - PAGINATED
    // =========================================================

    @Query("""
            SELECT j
            FROM Job j
            WHERE
                (
                    LOWER(j.position) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(j.company) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(j.country) LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND LOWER(j.jobType) = LOWER(:jobType)
            """)
    Page<Job> searchJobsByJobType(
            @Param("search") String search,
            @Param("jobType") String jobType,
            Pageable pageable
    );

    // =========================================================
    // EXTERNAL JOB DUPLICATE CHECK
    // Used by JobicyJobService
    // =========================================================

    boolean existsBySourceAndExternalJobId(
            String source,
            String externalJobId
    );
}