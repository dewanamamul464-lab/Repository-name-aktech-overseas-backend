package com.aktech.overseas.controller;

import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.service.AdminEmployerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/employers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEmployerController {

    private final AdminEmployerService adminEmployerService;

    public AdminEmployerController(
            AdminEmployerService adminEmployerService) {

        this.adminEmployerService = adminEmployerService;
    }

    // =========================================================
    // GET ALL EMPLOYERS
    // =========================================================

    @GetMapping
    public List<Employer> getAllEmployers() {

        return adminEmployerService.getAllEmployers();
    }

    // =========================================================
    // GET PENDING EMPLOYERS
    // =========================================================

    @GetMapping("/pending")
    public List<Employer> getPendingEmployers() {

        return adminEmployerService.getPendingEmployers();
    }

    // =========================================================
    // GET EMPLOYER BY ID
    // =========================================================

    @GetMapping("/{id}")
    public Employer getEmployerById(
            @PathVariable Long id) {

        return adminEmployerService.getEmployerById(id);
    }

    // =========================================================
    // APPROVE EMPLOYER
    // =========================================================

    @PutMapping("/{id}/approve")
    public Employer approveEmployer(
            @PathVariable Long id) {

        return adminEmployerService.approveEmployer(id);
    }

    // =========================================================
    // REJECT EMPLOYER
    // =========================================================

    @PutMapping("/{id}/reject")
    public Employer rejectEmployer(
            @PathVariable Long id) {

        return adminEmployerService.rejectEmployer(id);
    }

    // =========================================================
    // DELETE EMPLOYER
    // =========================================================

    @DeleteMapping("/{id}")
    public String deleteEmployer(
            @PathVariable Long id) {

        adminEmployerService.deleteEmployer(id);

        return "Employer deleted successfully.";
    }
}