
        package com.aktech.overseas.controller;

import com.aktech.overseas.entity.Job;
import com.aktech.overseas.service.JobService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // =========================================================
    // GET JOBS
    // =========================================================
    //
    // GET /api/jobs
    // GET /api/jobs?page=0&size=20
    // GET /api/jobs?search=engineer&page=0&size=20
    // GET /api/jobs?jobType=FOREIGN&page=0&size=20
    // GET /api/jobs?search=engineer&jobType=FOREIGN&page=0&size=20
    //
    // =========================================================

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Job>> getJobs(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            String jobType) {

        Page<Job> jobs = jobService.getJobs(
                page,
                size,
                search,
                jobType
        );

        return ResponseEntity.ok(jobs);
    }

    // =========================================================
    // GET SINGLE JOB BY ID
    // =========================================================
    //
    // GET /api/jobs/{id}
    //
    // Example:
    // GET /api/jobs/3496
    //
    // Used by Flutter AI Recommendations when the user
    // taps "View Job".
    //
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Job> getJobById(
            @PathVariable Long id) {

        Job job = jobService.getJobById(id);

        return ResponseEntity.ok(job);
    }

    // =========================================================
    // FOREIGN JOBS
    // =========================================================

    @GetMapping("/foreign")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Job>> getForeignJobs(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<Job> jobs = jobService.getForeignJobs(
                page,
                size
        );

        return ResponseEntity.ok(jobs);
    }

    // =========================================================
    // DOMESTIC JOBS
    // =========================================================

    @GetMapping("/domestic")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Job>> getDomesticJobs(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<Job> jobs = jobService.getDomesticJobs(
                page,
                size
        );

        return ResponseEntity.ok(jobs);
    }

    // =========================================================
    // VERIFIED JOBS
    // =========================================================

    @GetMapping("/verified")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Job>> getVerifiedJobs(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Page<Job> jobs = jobService.getVerifiedJobs(
                page,
                size
        );

        return ResponseEntity.ok(jobs);
    }
}

