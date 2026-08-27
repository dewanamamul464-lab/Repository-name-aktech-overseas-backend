package com.aktech.overseas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "applicants")
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private String country;

    private String experience;

    private String skills;

    @Column(unique = true)
    private String passportNumber;

    // =========================================================
    // CV INFORMATION
    // =========================================================

    private String cvFileName;

    @Column(length = 1000)
    private String cvUrl;

    private LocalDateTime cvUploadedAt;

    // =========================================================
    // PROFILE IMAGE
    // =========================================================

    @Column(length = 1000)
    private String profileImage;

    // =========================================================
    // USER ACCOUNT
    // =========================================================

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "user_id",
            unique = true
    )
    private User user;

    // =========================================================
    // JOB APPLICATIONS
    // =========================================================

    @JsonIgnore
    @OneToMany(
            mappedBy = "applicant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JobApplication> applications;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Applicant() {
    }

    // =========================================================
    // GETTERS & SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getCvFileName() {
        return cvFileName;
    }

    public void setCvFileName(String cvFileName) {
        this.cvFileName = cvFileName;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public LocalDateTime getCvUploadedAt() {
        return cvUploadedAt;
    }

    public void setCvUploadedAt(LocalDateTime cvUploadedAt) {
        this.cvUploadedAt = cvUploadedAt;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<JobApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<JobApplication> applications) {
        this.applications = applications;
    }
}