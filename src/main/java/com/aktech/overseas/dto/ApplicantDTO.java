
        package com.aktech.overseas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ApplicantDTO {

    // =========================================================
    // PROFILE IMAGE
    // =========================================================

    private String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // =========================================================
    // PRIMARY KEY
    // =========================================================

    private Long id;

    // =========================================================
    // APPLICANT INFORMATION
    // =========================================================

    @NotBlank(message = "Full name is required")
    @Size(
            max = 100,
            message = "Full name cannot exceed 100 characters"
    )
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "Phone number must contain 10 to 15 digits"
    )
    private String phone;

    @NotBlank(message = "Country is required")
    @Size(
            max = 100,
            message = "Country cannot exceed 100 characters"
    )
    private String country;

    @NotBlank(message = "Experience is required")
    @Size(
            max = 255,
            message = "Experience cannot exceed 255 characters"
    )
    private String experience;

    @NotBlank(message = "Skills are required")
    @Size(
            max = 255,
            message = "Skills cannot exceed 255 characters"
    )
    private String skills;

    @Size(
            max = 50,
            message = "Passport number cannot exceed 50 characters"
    )
    private String passportNumber;

    // =========================================================
    // CV INFORMATION
    // =========================================================

    private String cvFileName;

    private String cvUrl;

    private LocalDateTime cvUploadedAt;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ApplicantDTO() {
    }

    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public ApplicantDTO(
            Long id,
            String fullName,
            String email,
            String phone,
            String country,
            String experience,
            String skills,
            String passportNumber,
            String cvFileName,
            String cvUrl,
            LocalDateTime cvUploadedAt) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.experience = experience;
        this.skills = skills;
        this.passportNumber = passportNumber;
        this.cvFileName = cvFileName;
        this.cvUrl = cvUrl;
        this.cvUploadedAt = cvUploadedAt;
    }

    // =========================================================
    // GET ID
    // =========================================================

    public Long getId() {
        return id;
    }

    // =========================================================
    // GET FULL NAME
    // =========================================================

    public String getFullName() {
        return fullName;
    }

    // =========================================================
    // GET EMAIL
    // =========================================================

    public String getEmail() {
        return email;
    }

    // =========================================================
    // GET PHONE
    // =========================================================

    public String getPhone() {
        return phone;
    }

    // =========================================================
    // GET COUNTRY
    // =========================================================

    public String getCountry() {
        return country;
    }

    // =========================================================
    // GET EXPERIENCE
    // =========================================================

    public String getExperience() {
        return experience;
    }

    // =========================================================
    // GET SKILLS
    // =========================================================

    public String getSkills() {
        return skills;
    }

    // =========================================================
    // GET PASSPORT NUMBER
    // =========================================================

    public String getPassportNumber() {
        return passportNumber;
    }

    // =========================================================
    // GET CV FILE NAME
    // =========================================================

    public String getCvFileName() {
        return cvFileName;
    }

    // =========================================================
    // GET CV URL
    // =========================================================

    public String getCvUrl() {
        return cvUrl;
    }

    // =========================================================
    // GET CV UPLOADED DATE
    // =========================================================

    public LocalDateTime getCvUploadedAt() {
        return cvUploadedAt;
    }

    // =========================================================
    // SET ID
    // =========================================================

    public void setId(Long id) {
        this.id = id;
    }

    // =========================================================
    // SET FULL NAME
    // =========================================================

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // =========================================================
    // SET EMAIL
    // =========================================================

    public void setEmail(String email) {
        this.email = email;
    }

    // =========================================================
    // SET PHONE
    // =========================================================

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // =========================================================
    // SET COUNTRY
    // =========================================================

    public void setCountry(String country) {
        this.country = country;
    }

    // =========================================================
    // SET EXPERIENCE
    // =========================================================

    public void setExperience(String experience) {
        this.experience = experience;
    }

    // =========================================================
    // SET SKILLS
    // =========================================================

    public void setSkills(String skills) {
        this.skills = skills;
    }

    // =========================================================
    // SET PASSPORT NUMBER
    // =========================================================

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    // =========================================================
    // SET CV FILE NAME
    // =========================================================

    public void setCvFileName(String cvFileName) {
        this.cvFileName = cvFileName;
    }

    // =========================================================
    // SET CV URL
    // =========================================================

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    // =========================================================
    // SET CV UPLOADED DATE
    // =========================================================

    public void setCvUploadedAt(LocalDateTime cvUploadedAt) {
        this.cvUploadedAt = cvUploadedAt;
    }
}

