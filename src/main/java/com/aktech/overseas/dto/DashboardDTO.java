package com.aktech.overseas.dto;

public class DashboardDTO {

    private long totalApplicants;
    private long totalEmployers;
    private long totalJobs;
    private long totalApplications;
    private long pendingApplications;
    private long acceptedApplications;
    private long rejectedApplications;

    public DashboardDTO() {
    }

    public DashboardDTO(long totalApplicants,
                        long totalEmployers,
                        long totalJobs,
                        long totalApplications,
                        long pendingApplications,
                        long acceptedApplications,
                        long rejectedApplications) {

        this.totalApplicants = totalApplicants;
        this.totalEmployers = totalEmployers;
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
        this.pendingApplications = pendingApplications;
        this.acceptedApplications = acceptedApplications;
        this.rejectedApplications = rejectedApplications;
    }

    public long getTotalApplicants() {
        return totalApplicants;
    }

    public void setTotalApplicants(long totalApplicants) {
        this.totalApplicants = totalApplicants;
    }

    public long getTotalEmployers() {
        return totalEmployers;
    }

    public void setTotalEmployers(long totalEmployers) {
        this.totalEmployers = totalEmployers;
    }

    public long getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(long totalJobs) {
        this.totalJobs = totalJobs;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getPendingApplications() {
        return pendingApplications;
    }

    public void setPendingApplications(long pendingApplications) {
        this.pendingApplications = pendingApplications;
    }

    public long getAcceptedApplications() {
        return acceptedApplications;
    }

    public void setAcceptedApplications(long acceptedApplications) {
        this.acceptedApplications = acceptedApplications;
    }

    public long getRejectedApplications() {
        return rejectedApplications;
    }

    public void setRejectedApplications(long rejectedApplications) {
        this.rejectedApplications = rejectedApplications;
    }
}