package com.aktech.overseas.controller;

import com.aktech.overseas.dto.ApplicantDTO;
import com.aktech.overseas.dto.FileUploadResponse;
import com.aktech.overseas.service.ApplicantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    // ==========================
    // Applicant: Create Profile
    // ==========================
    @PostMapping
    @PreAuthorize("hasRole('APPLICANT')")
    public ApplicantDTO saveApplicant(
            @Valid @RequestBody ApplicantDTO applicantDTO) {

        return applicantService.saveApplicant(applicantDTO);
    }

    // ==========================
    // Applicant: Upload CV
    // ==========================
    @PostMapping(value = "/upload-cv", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<Map<String, Object>> uploadCV(
            @RequestParam("file") MultipartFile file) {

        FileUploadResponse uploadResponse =
                applicantService.uploadCV(file);

        Map<String, Object> response = new HashMap<>();

        response.put("message", "CV uploaded successfully");
        response.put("fileName", uploadResponse.getFileName());
        response.put("cvUrl", uploadResponse.getFilePath());
        response.put("uploadedAt", uploadResponse.getUploadedAt());

        return ResponseEntity.ok(response);
    }

    // ==========================
    // Applicant: Upload Profile Image
    // ==========================
    @PostMapping(value = "/me/profile-image", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {

        String imageUrl = applicantService.uploadProfileImage(file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile image uploaded successfully");
        response.put("profileImage", imageUrl);

        return ResponseEntity.ok(response);
    }

    // ==========================
    // Applicant / Employer / Admin: View CV By Applicant ID
    // ==========================
    @GetMapping("/{id}/cv")
    @PreAuthorize("hasAnyRole('APPLICANT','EMPLOYER','ADMIN')")
    public ResponseEntity<Map<String, String>> viewCV(
            @PathVariable Long id) {

        String cvUrl = applicantService.downloadCV(id);

        Map<String, String> response = new HashMap<>();
        response.put("cvUrl", cvUrl);

        return ResponseEntity.ok(response);
    }

    // ==========================
    // Applicant: View My Own CV
    // ==========================
    @GetMapping("/me/cv")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<Map<String, String>> getMyCV() {

        String cvUrl = applicantService.getMyCV();

        Map<String, String> response = new HashMap<>();
        response.put("cvUrl", cvUrl);

        return ResponseEntity.ok(response);
    }

    // ==========================
    // Applicant: Get Own Profile
    // ==========================
    @GetMapping("/me")
    @PreAuthorize("hasRole('APPLICANT')")
    public ApplicantDTO getMyProfile() {

        return applicantService.getMyProfile();
    }

    // ==========================
    // Applicant: Update Own Profile
    // ==========================
    @PutMapping("/me")
    @PreAuthorize("hasRole('APPLICANT')")
    public ApplicantDTO updateMyProfile(
            @Valid @RequestBody ApplicantDTO applicantDTO) {

        return applicantService.updateMyProfile(applicantDTO);
    }

    // ==========================
    // Admin: Get All Applicants
    // ==========================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ApplicantDTO> getAllApplicants() {

        return applicantService.getAllApplicants();
    }

    // ==========================
    // Admin: Get Applicant By ID
    // ==========================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApplicantDTO getApplicantById(
            @PathVariable Long id) {

        return applicantService.getApplicantById(id);
    }

    // ==========================
    // Admin: Update Applicant
    // ==========================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApplicantDTO updateApplicant(
            @PathVariable Long id,
            @Valid @RequestBody ApplicantDTO applicantDTO) {

        return applicantService.updateApplicant(id, applicantDTO);
    }

    // ==========================
    // Admin: Delete Applicant
    // ==========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteApplicant(
            @PathVariable Long id) {

        applicantService.deleteApplicant(id);

        return ResponseEntity.ok("Applicant deleted successfully.");
    }
}