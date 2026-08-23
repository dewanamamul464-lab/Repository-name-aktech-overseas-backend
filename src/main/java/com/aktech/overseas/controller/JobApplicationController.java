package com.aktech.overseas.controller;

import com.aktech.overseas.dto.ApplicationDTO;
import com.aktech.overseas.service.JobApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;


    public JobApplicationController(
            JobApplicationService jobApplicationService) {

        this.jobApplicationService = jobApplicationService;
    }


    // ==========================
    // Applicant: Apply for Job
    // ==========================
    @PostMapping
    @PreAuthorize("hasRole('APPLICANT')")
    public ApplicationDTO apply(
            @RequestBody ApplicationDTO dto) {

        return jobApplicationService.apply(dto);
    }



    // ==========================
    // Applicant: View Own Applications
    // ==========================
    @GetMapping("/my")
    @PreAuthorize("hasRole('APPLICANT')")
    public List<ApplicationDTO> getMyApplications() {

        return jobApplicationService.getMyApplications();
    }



    // ==========================
    // Employer/Admin: View All
    // ==========================
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public List<ApplicationDTO> getAllApplications() {

        return jobApplicationService.getAllApplications();
    }



    // ==========================
    // Employer/Admin: View By ID
    // ==========================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ApplicationDTO getApplicationById(
            @PathVariable Long id) {

        return jobApplicationService.getApplicationById(id);
    }



    // ==========================
    // Admin Approve
    // ==========================
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ApplicationDTO approveApplication(
            @PathVariable Long id) {

        return jobApplicationService.approveApplication(id);
    }



    // ==========================
    // Admin Reject
    // ==========================
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ApplicationDTO rejectApplication(
            @PathVariable Long id) {

        return jobApplicationService.rejectApplication(id);
    }



    // ==========================
    // Admin Delete
    // ==========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteApplication(
            @PathVariable Long id) {

        jobApplicationService.deleteApplication(id);

        return "Application deleted successfully.";
    }
}