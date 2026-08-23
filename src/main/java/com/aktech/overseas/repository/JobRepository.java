package com.aktech.overseas.repository;

import com.aktech.overseas.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    boolean existsBySourceAndExternalJobId(
            String source,
            String externalJobId
    );

    List<Job> findByEmployerId(Long employerId);

    List<Job> findByVerifiedTrue();

    Page<Job> findByVerifiedTrue(
            Pageable pageable
    );

    List<Job> findByJobTypeIgnoreCase(
            String jobType
    );

    Page<Job> findByJobTypeIgnoreCase(
            String jobType,
            Pageable pageable
    );

    Page<Job> findAllByOrderByIdDesc(
            Pageable pageable
    );

    @Query("""
        SELECT j
        FROM Job j
        WHERE
            LOWER(COALESCE(j.position, '')) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(COALESCE(j.company, '')) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(COALESCE(j.country, '')) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(COALESCE(j.description, '')) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(COALESCE(j.requirements, '')) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(COALESCE(j.source, '')) LIKE LOWER(CONCAT('%', :search, '%'))
        """)
    Page<Job> searchJobs(
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
        SELECT j
        FROM Job j
        WHERE
            LOWER(COALESCE(j.jobType, '')) = LOWER(:jobType)
        ORDER BY j.id DESC
        """)
    Page<Job> findJobsByType(
            @Param("jobType") String jobType,
            Pageable pageable
    );

    @Query("""
        SELECT j
        FROM Job j
        WHERE
            (
                LOWER(COALESCE(j.position, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(COALESCE(j.company, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(COALESCE(j.country, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(COALESCE(j.description, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(COALESCE(j.requirements, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(COALESCE(j.source, '')) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            AND (
                :jobType = ''
                OR LOWER(COALESCE(j.jobType, '')) = LOWER(:jobType)
            )
        """)
    Page<Job> searchJobsWithType(
            @Param("search") String search,
            @Param("jobType") String jobType,
            Pageable pageable
    );
}