package com.aktech.overseas.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employers")
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // USER ACCOUNT
    // =========================================================

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    // =========================================================
    // COMPANY INFORMATION
    // =========================================================

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String contactPerson;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String country;

    private String address;

    private String registrationNumber;

    @Column(length = 2000)
    private String description;

    // =========================================================
    // APPROVAL STATUS
    // =========================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployerStatus status =
            EmployerStatus.PENDING;

    // =========================================================
    // DATE
    // =========================================================

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // =========================================================
    // JOBS
    // =========================================================

    @OneToMany(
            mappedBy = "employer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Job> jobs;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Employer() {

        this.createdAt =
                LocalDateTime.now();

        this.status =
                EmployerStatus.PENDING;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(
            String companyName) {

        this.companyName = companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(
            String contactPerson) {

        this.contactPerson = contactPerson;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(
            String registrationNumber) {

        this.registrationNumber =
                registrationNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description) {

        this.description = description;
    }

    public EmployerStatus getStatus() {
        return status;
    }

    public void setStatus(
            EmployerStatus status) {

        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}