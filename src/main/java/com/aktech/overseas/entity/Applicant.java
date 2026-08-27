package com.aktech.overseas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "applicants")
public class Applicant {

    // =========================================================
    // PRIMARY KEY
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // APPLICANT INFORMATION
    // =========================================================

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
    //
    // @JsonIgnore prevents infinite JSON recursion:
    //
    // Applicant
    //    -> applications
    //       -> applicant
    //          -> applications
    //             -> applicant
    //                -> ...
    //
    // Database relationship is NOT affected.
    // Applications are still deleted through cascade/orphanRemoval.
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
    // GET ID
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // =========================================================
    // GET FULL NAME
    // =========================================================

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // =========================================================
    // GET EMAIL
    // =========================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // =========================================================
    // GET PHONE
    // =========================================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // =========================================================
    // GET COUNTRY
    // =========================================================

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // =========================================================
    // GET EXPERIENCE
    // =========================================================

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    // =========================================================
    // GET SKILLS
    // =========================================================

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    // =========================================================
    // GET PASSPORT NUMBER
    // =========================================================

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    // =========================================================
    // GET CV FILE NAME
    // =========================================================

    public String getCvFileName() {
        return cvFileName;
    }

    public void setCvFileName(String cvFileName) {
        this.cvFileName = cvFileName;
    }

    // =========================================================
    // GET CV URL
    // =========================================================

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    // =========================================================
    // GET CV UPLOADED DATE
    // =========================================================

    public LocalDateTime getCvUploadedAt() {
        return cvUploadedAt;
    }

    public void setCvUploadedAt(LocalDateTime cvUploadedAt) {
        this.cvUploadedAt = cvUploadedAt;
    }

    // =========================================================
    // GET PROFILE IMAGE
    // =========================================================

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // =========================================================
    // GET USER
    // =========================================================

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // =========================================================
    // GET APPLICATIONS
    // =========================================================

    public List<JobApplication> getApplications() {
        return applications;
    }

    public void setApplications(
            List<JobApplication> applications) {

        this.applications = applications;
    }
}