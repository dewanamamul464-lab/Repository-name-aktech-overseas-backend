package com.aktech.overseas.controller;

import com.aktech.overseas.dto.DashboardDTO;
import com.aktech.overseas.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Admin Dashboard Statistics
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public DashboardDTO getDashboard() {

        return dashboardService.getDashboard();
    }
}