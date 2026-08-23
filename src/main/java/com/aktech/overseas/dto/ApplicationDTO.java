package com.aktech.overseas.dto;

import com.aktech.overseas.entity.ApplicationStatus;

public class ApplicationDTO {

    private Long id;
    private Long applicantId;
    private String applicantName;
    private Long jobId;
    private String company;
    private String position;
    private ApplicationStatus status;

    // Default Constructor
    public ApplicationDTO() {
    }

    // Parameterized Constructor
    public ApplicationDTO(Long id,
                          Long applicantId,
                          String applicantName,
                          Long jobId,
                          String company,
                          String position,
                          ApplicationStatus status) {

        this.id = id;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.jobId = jobId;
        this.company = company;
        this.position = position;
        this.status = status;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}