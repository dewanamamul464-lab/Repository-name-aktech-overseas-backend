package com.aktech.overseas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ApplicantDTO {

    private String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
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
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @NotBlank(message = "Experience is required")
    @Size(max = 100, message = "Experience cannot exceed 100 characters")
    private String experience;

    @NotBlank(message = "Skills are required")
    @Size(max = 255, message = "Skills cannot exceed 255 characters")
    private String skills;

    @Size(max = 50, message = "Passport number cannot exceed 50 characters")
    private String passportNumber;

    // ==========================
    // CV Information
    // ==========================

    private String cvFileName;

    private String cvUrl;

    private LocalDateTime cvUploadedAt;

    public ApplicantDTO() {
    }

    public ApplicantDTO(Long id,
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

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getExperience() {
        return experience;
    }

    public String getSkills() {
        return skills;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getCvFileName() {
        return cvFileName;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public LocalDateTime getCvUploadedAt() {
        return cvUploadedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setCvFileName(String cvFileName) {
        this.cvFileName = cvFileName;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public void setCvUploadedAt(LocalDateTime cvUploadedAt) {
        this.cvUploadedAt = cvUploadedAt;
    }
}