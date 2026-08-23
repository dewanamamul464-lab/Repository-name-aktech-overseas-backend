package com.aktech.overseas.service;

import com.aktech.overseas.dto.DashboardDTO;
import com.aktech.overseas.entity.ApplicationStatus;
import com.aktech.overseas.entity.Role;
import com.aktech.overseas.repository.ApplicantRepository;
import com.aktech.overseas.repository.JobRepository;
import com.aktech.overseas.repository.UserRepository;
import com.aktech.overseas.repository.JobApplicationRepository;

import org.springframework.stereotype.Service;


@Service
public class DashboardService {


    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;



    public DashboardService(
            ApplicantRepository applicantRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            JobApplicationRepository jobApplicationRepository) {


        this.applicantRepository = applicantRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }



    public DashboardDTO getDashboard() {


        long totalApplicants =
                applicantRepository.count();



        long totalEmployers =
                userRepository.countByRole(Role.EMPLOYER);



        long totalJobs =
                jobRepository.count();



        long totalApplications =
                jobApplicationRepository.count();



        long pendingApplications =
                jobApplicationRepository
                        .countByStatus(ApplicationStatus.PENDING);



        long acceptedApplications =
                jobApplicationRepository
                        .countByStatus(ApplicationStatus.APPROVED);



        long rejectedApplications =
                jobApplicationRepository
                        .countByStatus(ApplicationStatus.REJECTED);



        return new DashboardDTO(
                totalApplicants,
                totalEmployers,
                totalJobs,
                totalApplications,
                pendingApplications,
                acceptedApplications,
                rejectedApplications
        );
    }
}