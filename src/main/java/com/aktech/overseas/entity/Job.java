package com.aktech.overseas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;

    private String company;

    private String position;

    private String salary;

    // FOREIGN or DOMESTIC
    private String jobType;

    // Job description
    @Column(columnDefinition = "TEXT")
    private String description;

    // Job requirements
    @Column(columnDefinition = "TEXT")
    private String requirements;

    private String experience;

    private Integer vacancies;

    private LocalDate expiryDate;

    private Boolean verified = false;

    // Example:
    // Recruitment Agency / Company / External Source
    private String source;

    // External job ID from Remotive or another source
    @Column(name = "external_job_id")
    private String externalJobId;

    // Original external job URL
    @Column(columnDefinition = "TEXT")
    private String sourceUrl;

    // =========================================================
    // EMPLOYER
    // =========================================================

    @ManyToOne
    @JoinColumn(name = "employer_id")
    @JsonIgnore
    private Employer employer;

    // =========================================================
    // APPLICATIONS
    // =========================================================

    @OneToMany(
            mappedBy = "job",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<JobApplication> applications;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Job() {
    }

    // =========================================================
    // ID
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // =========================================================
    // COUNTRY
    // =========================================================

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // =========================================================
    // COMPANY
    // =========================================================

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    // =========================================================
    // POSITION
    // =========================================================

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    // =========================================================
    // SALARY
    // =========================================================

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    // =========================================================
    // JOB TYPE
    // =========================================================

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // =========================================================
    // REQUIREMENTS
    // =========================================================

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    // =========================================================
    // EXPERIENCE
    // =========================================================

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    // =========================================================
    // VACANCIES
    // =========================================================

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    // =========================================================
    // EXPIRY DATE
    // =========================================================

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    // =========================================================
    // VERIFIED
    // =========================================================

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    // =========================================================
    // SOURCE
    // =========================================================

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    // =========================================================
    // EXTERNAL JOB ID
    // =========================================================

    public String getExternalJobId() {
        return externalJobId;
    }

    public void setExternalJobId(String externalJobId) {
        this.externalJobId = externalJobId;
    }

    // =========================================================
    // SOURCE URL
    // =========================================================

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    // =========================================================
    // EMPLOYER
    // =========================================================

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    // =========================================================
    // EMPLOYER ID
    // =========================================================
    //
    // Employer itself remains hidden because of @JsonIgnore.
    //
    // Flutter receives only:
    //
    // "employerId": 7
    //
    // for employer-created jobs.
    //
    // External jobs return:
    //
    // "employerId": null
    //
    // =========================================================

    @JsonProperty("employerId")
    public Long getEmployerId() {

        if (employer == null) {
            return null;
        }

        return employer.getId();
    }

    // =========================================================
    // APPLICATIONS
    // =========================================================

    public List<JobApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<JobApplication> applications) {
        this.applications = applications;
    }
}