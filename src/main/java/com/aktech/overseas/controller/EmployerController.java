package com.aktech.overseas.controller;

import com.aktech.overseas.dto.ApplicationDTO;
import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.Job;
import com.aktech.overseas.service.EmployerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(
            EmployerService employerService) {

        this.employerService =
                employerService;
    }

    // =========================================================
    // GET CURRENT EMPLOYER PROFILE
    // GET /api/employer/me
    // =========================================================

    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYER')")
    public Employer getMyProfile(
            Authentication authentication) {

        String username =
                authentication.getName();

        return employerService
                .getMyProfile(username);
    }

    // =========================================================
    // EMPLOYER POSTS A JOB
    // POST /api/employer/{employerId}/jobs
    // =========================================================

    @PostMapping("/{employerId}/jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public Job postJob(
            @PathVariable Long employerId,
            @RequestBody Job job) {

        return employerService.postJob(
                employerId,
                job
        );
    }

    // =========================================================
    // GET EMPLOYER'S JOBS
    // GET /api/employer/{employerId}/jobs
    // =========================================================

    @GetMapping("/{employerId}/jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<Job> getMyJobs(
            @PathVariable Long employerId) {

        return employerService
                .getMyJobs(employerId);
    }

    // =========================================================
    // GET EMPLOYER'S APPLICATIONS
    // GET /api/employer/{employerId}/applications
    // =========================================================

    @GetMapping("/{employerId}/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    public List<ApplicationDTO> getApplications(
            @PathVariable Long employerId) {

        return employerService
                .getApplications(employerId);
    }

    // =========================================================
    // GET FULL APPLICANT PROFILE
    //
    // GET
    // /api/employer/{employerId}/applications/{applicationId}/applicant
    //
    // READ ONLY
    // =========================================================

    @GetMapping(
            "/{employerId}/applications/{applicationId}/applicant"
    )
    @PreAuthorize("hasRole('EMPLOYER')")
    public Map<String, Object> getApplicantDetails(
            @PathVariable Long employerId,
            @PathVariable Long applicationId) {

        return employerService
                .getApplicantDetails(
                        employerId,
                        applicationId
                );
    }

    // =========================================================
    // APPROVE APPLICATION
    //
    // PUT
    // /api/employer/{employerId}/applications/{applicationId}/approve
    // =========================================================

    @PutMapping(
            "/{employerId}/applications/{applicationId}/approve"
    )
    @PreAuthorize("hasRole('EMPLOYER')")
    public ApplicationDTO approveApplication(
            @PathVariable Long employerId,
            @PathVariable Long applicationId) {

        return employerService
                .approveApplication(
                        employerId,
                        applicationId
                );
    }

    // =========================================================
    // REJECT APPLICATION
    //
    // PUT
    // /api/employer/{employerId}/applications/{applicationId}/reject
    // =========================================================

    @PutMapping(
            "/{employerId}/applications/{applicationId}/reject"
    )
    @PreAuthorize("hasRole('EMPLOYER')")
    public ApplicationDTO rejectApplication(
            @PathVariable Long employerId,
            @PathVariable Long applicationId) {

        return employerService
                .rejectApplication(
                        employerId,
                        applicationId
                );
    }
}