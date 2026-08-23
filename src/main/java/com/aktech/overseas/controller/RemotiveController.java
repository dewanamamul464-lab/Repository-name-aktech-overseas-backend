package com.aktech.overseas.controller;

import com.aktech.overseas.entity.Job;
import com.aktech.overseas.service.RemotiveJobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/remotive")
public class RemotiveController {

    private final RemotiveJobService remotiveJobService;

    public RemotiveController(
            RemotiveJobService remotiveJobService) {

        this.remotiveJobService = remotiveJobService;
    }

    @GetMapping("/import")
    public List<Job> importJobs() {

        return remotiveJobService.fetchAndSaveJobs();
    }
}