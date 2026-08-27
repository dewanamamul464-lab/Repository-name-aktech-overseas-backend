package com.aktech.overseas.controller;

import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.service.AdminApplicantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/applicants")
@PreAuthorize("hasRole('ADMIN')")
public class AdminApplicantController {

    private final AdminApplicantService adminApplicantService;

    public AdminApplicantController(
            AdminApplicantService adminApplicantService) {

        this.adminApplicantService =
                adminApplicantService;
    }

    // =========================================================
    // GET ALL APPLICANTS
    // =========================================================

    @GetMapping
    public List<Applicant> getAllApplicants() {

        return adminApplicantService
                .getAllApplicants();
    }

    // =========================================================
    // GET APPLICANT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public Applicant getApplicantById(
            @PathVariable Long id) {

        return adminApplicantService
                .getApplicantById(id);
    }

    // =========================================================
    // DELETE APPLICANT
    // =========================================================

    @DeleteMapping("/{id}")
    public String deleteApplicant(
            @PathVariable Long id) {

        adminApplicantService
                .deleteApplicant(id);

        return "Applicant deleted successfully.";
    }
}