package com.aktech.overseas.service;

import com.aktech.overseas.entity.Job;
import com.aktech.overseas.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // =========================================================
    // GET JOBS - PAGINATED
    // =========================================================

    public Page<Job> getJobs(
            int page,
            int size,
            String search,
            String jobType
    ) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 20;
        }

        if (size > 100) {
            size = 100;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "id"
                )
        );

        String cleanSearch =
                search == null
                        ? ""
                        : search.trim();

        String cleanJobType =
                jobType == null
                        ? ""
                        : jobType.trim();

        // No search + no filter
        if (cleanSearch.isEmpty()
                && cleanJobType.isEmpty()) {

            return jobRepository.findAllByOrderByIdDesc(
                    pageable
            );
        }

        // Job type only
        if (cleanSearch.isEmpty()) {

            return jobRepository.findJobsByType(
                    cleanJobType,
                    pageable
            );
        }

        // Search + optional job type
        return jobRepository.searchJobsWithType(
                cleanSearch,
                cleanJobType,
                pageable
        );
    }

    // =========================================================
    // GET JOB BY ID
    // =========================================================

    public Job getJobById(Long id) {

        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found with id: " + id
                        )
                );
    }

    // =========================================================
    // GET ALL JOBS
    // =========================================================

    public List<Job> getAllJobs() {

        return jobRepository.findAll(
                Sort.by(
                        Sort.Direction.DESC,
                        "id"
                )
        );
    }

    // =========================================================
    // GET VERIFIED JOBS
    // =========================================================

    public List<Job> getVerifiedJobs() {

        return jobRepository.findByVerifiedTrue();
    }

    // =========================================================
    // GET VERIFIED JOBS - PAGINATED
    // =========================================================

    public Page<Job> getVerifiedJobs(
            int page,
            int size
    ) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 20;
        }

        if (size > 100) {
            size = 100;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "id"
                )
        );

        return jobRepository.findByVerifiedTrue(
                pageable
        );
    }

    // =========================================================
    // GET FOREIGN JOBS
    // =========================================================

    public List<Job> getForeignJobs() {

        return jobRepository.findByJobTypeIgnoreCase(
                "FOREIGN"
        );
    }

    // =========================================================
    // GET FOREIGN JOBS - PAGINATED
    // =========================================================

    public Page<Job> getForeignJobs(
            int page,
            int size
    ) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 20;
        }

        if (size > 100) {
            size = 100;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "id"
                )
        );

        return jobRepository.findByJobTypeIgnoreCase(
                "FOREIGN",
                pageable
        );
    }

    // =========================================================
    // GET DOMESTIC JOBS
    // =========================================================

    public List<Job> getDomesticJobs() {

        return jobRepository.findByJobTypeIgnoreCase(
                "DOMESTIC"
        );
    }

    // =========================================================
    // GET DOMESTIC JOBS - PAGINATED
    // =========================================================

    public Page<Job> getDomesticJobs(
            int page,
            int size
    ) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 20;
        }

        if (size > 100) {
            size = 100;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "id"
                )
        );

        return jobRepository.findByJobTypeIgnoreCase(
                "DOMESTIC",
                pageable
        );
    }

    // =========================================================
    // GET JOBS BY TYPE
    // =========================================================

    public List<Job> getJobsByType(String jobType) {

        if (jobType == null
                || jobType.trim().isEmpty()) {

            return getAllJobs();
        }

        return jobRepository.findByJobTypeIgnoreCase(
                jobType.trim()
        );
    }

    // =========================================================
    // CREATE JOB
    // =========================================================

    public Job createJob(Job job) {

        return jobRepository.save(job);
    }

    // =========================================================
    // DELETE JOB
    // =========================================================

    public void deleteJob(Long id) {

        if (!jobRepository.existsById(id)) {

            throw new RuntimeException(
                    "Job not found with id: " + id
            );
        }

        jobRepository.deleteById(id);
    }
}