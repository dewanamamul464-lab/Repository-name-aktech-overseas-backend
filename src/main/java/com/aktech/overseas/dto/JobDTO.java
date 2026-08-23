package com.aktech.overseas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class JobDTO {

    private Long id;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @NotBlank(message = "Company is required")
    @Size(max = 100, message = "Company cannot exceed 100 characters")
    private String company;

    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position cannot exceed 100 characters")
    private String position;

    @NotBlank(message = "Salary is required")
    @Size(max = 50, message = "Salary cannot exceed 50 characters")
    private String salary;

    @NotBlank(message = "Job type is required")
    private String jobType;

    @NotBlank(message = "Description is required")
    @Size(max = 2000)
    private String description;

    @NotBlank(message = "Requirements are required")
    @Size(max = 2000)
    private String requirements;

    @NotBlank(message = "Experience is required")
    @Size(max = 100)
    private String experience;

    @NotNull(message = "Vacancies are required")
    private Integer vacancies;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    private Boolean verified = false;

    @NotBlank(message = "Source is required")
    @Size(max = 150)
    private String source;

    // External application/source URL
    private String sourceUrl;

    public JobDTO() {
    }

    public JobDTO(
            Long id,
            String country,
            String company,
            String position,
            String salary,
            String jobType,
            String description,
            String requirements,
            String experience,
            Integer vacancies,
            LocalDate expiryDate,
            Boolean verified,
            String source,
            String sourceUrl) {

        this.id = id;
        this.country = country;
        this.company = company;
        this.position = position;
        this.salary = salary;
        this.jobType = jobType;
        this.description = description;
        this.requirements = requirements;
        this.experience = experience;
        this.vacancies = vacancies;
        this.expiryDate = expiryDate;
        this.verified = verified;
        this.source = source;
        this.sourceUrl = sourceUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}