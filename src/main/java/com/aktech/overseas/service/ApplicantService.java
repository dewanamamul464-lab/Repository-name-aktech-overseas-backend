package com.aktech.overseas.service;

import com.aktech.overseas.dto.ApplicantDTO;
import com.aktech.overseas.dto.FileUploadResponse;
import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.entity.User;
import com.aktech.overseas.repository.ApplicantRepository;
import com.aktech.overseas.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public ApplicantService(
            ApplicantRepository applicantRepository,
            UserRepository userRepository,
            CloudinaryService cloudinaryService) {

        this.applicantRepository = applicantRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // ==========================================
    // Create Applicant Profile
    // ==========================================
    public ApplicantDTO saveApplicant(ApplicantDTO dto) {

        if (applicantRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Applicant applicant = new Applicant();

        applicant.setFullName(dto.getFullName());
        applicant.setEmail(dto.getEmail());
        applicant.setPhone(dto.getPhone());
        applicant.setCountry(dto.getCountry());
        applicant.setExperience(dto.getExperience());
        applicant.setSkills(dto.getSkills());
        applicant.setPassportNumber(dto.getPassportNumber());
        applicant.setUser(user);

        Applicant saved = applicantRepository.save(applicant);

        return convertToDTO(saved);
    }

    // ==========================================
    // Upload CV
    // ==========================================
    public FileUploadResponse uploadCV(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please select a CV file.");
        }

        try {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            Applicant applicant = applicantRepository
                    .findByUserUsername(username)
                    .orElseThrow(() ->
                            new RuntimeException("Applicant profile not found"));

            String cvUrl = cloudinaryService.uploadCv(file);

            applicant.setCvFileName(file.getOriginalFilename());
            applicant.setCvUrl(cvUrl);
            applicant.setCvUploadedAt(LocalDateTime.now());

            applicantRepository.save(applicant);

            return new FileUploadResponse(
                    applicant.getCvFileName(),
                    applicant.getCvUrl(),
                    applicant.getCvUploadedAt()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "CV upload failed: " + e.getMessage(), e);

        }
    }

    // ==========================================
// Upload Profile Image
// ==========================================
    public String uploadProfileImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please select an image.");
        }

        try {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            Applicant applicant = applicantRepository
                    .findByUserUsername(username)
                    .orElseThrow(() ->
                            new RuntimeException("Applicant profile not found"));

            String imageUrl = cloudinaryService.uploadProfileImage(file);

            applicant.setProfileImage(imageUrl);

            applicantRepository.save(applicant);

            return imageUrl;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Profile image upload failed: " + e.getMessage(), e);

        }
    }

    // ==========================================
    // Applicant View Own CV
    // ==========================================
    public String getMyCV() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Applicant applicant = applicantRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Applicant profile not found"));

        if (applicant.getCvUrl() == null ||
                applicant.getCvUrl().isBlank()) {

            throw new RuntimeException("No CV uploaded.");

        }

        return applicant.getCvUrl();
    }

    // ==========================================
    // View CV By Applicant ID
    // ==========================================
    public String downloadCV(Long applicantId) {

        Applicant applicant =
                applicantRepository.findById(applicantId)
                        .orElseThrow(() ->
                                new RuntimeException("Applicant not found"));

        if (applicant.getCvUrl() == null ||
                applicant.getCvUrl().isBlank()) {

            throw new RuntimeException("CV has not been uploaded.");

        }

        return applicant.getCvUrl();
    }

    // ==========================================
    // Get My Profile
    // ==========================================
    public ApplicantDTO getMyProfile() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Applicant applicant =
                applicantRepository
                        .findByUserUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("Applicant profile not found"));

        return convertToDTO(applicant);
    }

    // ==========================================
    // Update My Profile
    // ==========================================
    public ApplicantDTO updateMyProfile(ApplicantDTO dto) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Applicant applicant = applicantRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Applicant profile not found"));

        updateFields(applicant, dto);

        Applicant saved = applicantRepository.save(applicant);

        return convertToDTO(saved);
    }

    // ==========================================
    // Get All Applicants
    // ==========================================
    public List<ApplicantDTO> getAllApplicants() {

        return applicantRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ==========================================
    // Get Applicant By ID
    // ==========================================
    public ApplicantDTO getApplicantById(Long id) {

        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Applicant not found"));

        return convertToDTO(applicant);
    }

    // ==========================================
    // Update Applicant (Admin)
    // ==========================================
    public ApplicantDTO updateApplicant(Long id,
                                        ApplicantDTO dto) {

        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Applicant not found"));

        updateFields(applicant, dto);

        Applicant saved = applicantRepository.save(applicant);

        return convertToDTO(saved);
    }

    // ==========================================
    // Delete Applicant
    // ==========================================
    public void deleteApplicant(Long id) {

        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Applicant not found"));

        applicantRepository.delete(applicant);
    }

    // ==========================================
    // Update Common Fields
    // ==========================================
    private void updateFields(Applicant applicant,
                              ApplicantDTO dto) {

        applicant.setFullName(dto.getFullName());
        applicant.setEmail(dto.getEmail());
        applicant.setPhone(dto.getPhone());
        applicant.setCountry(dto.getCountry());
        applicant.setExperience(dto.getExperience());
        applicant.setSkills(dto.getSkills());
        applicant.setPassportNumber(dto.getPassportNumber());

        if (dto.getCvFileName() != null) {
            applicant.setCvFileName(dto.getCvFileName());
        }

        if (dto.getCvUrl() != null) {
            applicant.setCvUrl(dto.getCvUrl());
        }

        if (dto.getCvUploadedAt() != null) {
            applicant.setCvUploadedAt(dto.getCvUploadedAt());
        }

        if (dto.getProfileImage() != null) {
            applicant.setProfileImage(dto.getProfileImage());
        }
    }
    // ==========================================
// Convert Entity -> DTO
// ==========================================
    private ApplicantDTO convertToDTO(Applicant applicant) {

        ApplicantDTO dto = new ApplicantDTO();

        dto.setId(applicant.getId());
        dto.setFullName(applicant.getFullName());
        dto.setEmail(applicant.getEmail());
        dto.setPhone(applicant.getPhone());
        dto.setCountry(applicant.getCountry());
        dto.setExperience(applicant.getExperience());
        dto.setSkills(applicant.getSkills());
        dto.setPassportNumber(applicant.getPassportNumber());

        dto.setCvFileName(applicant.getCvFileName());
        dto.setCvUrl(applicant.getCvUrl());
        dto.setCvUploadedAt(applicant.getCvUploadedAt());

        dto.setProfileImage(applicant.getProfileImage());

        return dto;
    }}