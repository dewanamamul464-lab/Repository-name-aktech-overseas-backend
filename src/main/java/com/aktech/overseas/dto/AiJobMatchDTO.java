package com.aktech.overseas.dto;

import java.util.ArrayList;
import java.util.List;

public class AiJobMatchDTO {

    private Long jobId;

    private String title;

    private String company;

    private Integer matchPercentage;

    private List<String> matchedSkills = new ArrayList<>();

    private List<String> missingSkills = new ArrayList<>();

    private String reason;

    public AiJobMatchDTO() {
    }

    public AiJobMatchDTO(
            Long jobId,
            String title,
            String company,
            Integer matchPercentage,
            List<String> matchedSkills,
            List<String> missingSkills,
            String reason
    ) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.matchPercentage = matchPercentage;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.reason = reason;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(Integer matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}