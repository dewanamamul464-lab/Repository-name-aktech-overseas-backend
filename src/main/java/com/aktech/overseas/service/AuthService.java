package com.aktech.overseas.service;

import com.aktech.overseas.dto.EmployerRegisterRequest;
import com.aktech.overseas.dto.LoginRequest;
import com.aktech.overseas.dto.LoginResponse;
import com.aktech.overseas.dto.RegisterRequest;
import com.aktech.overseas.dto.UserDTO;
import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.entity.Employer;
import com.aktech.overseas.entity.EmployerStatus;
import com.aktech.overseas.entity.Role;
import com.aktech.overseas.entity.User;
import com.aktech.overseas.repository.ApplicantRepository;
import com.aktech.overseas.repository.EmployerRepository;
import com.aktech.overseas.repository.UserRepository;
import com.aktech.overseas.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;
    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Value("${app.admin.email}")
    private String adminEmail;

    public AuthService(
            UserRepository userRepository,
            ApplicantRepository applicantRepository,
            EmployerRepository employerRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.applicantRepository = applicantRepository;
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    // =========================================================
    // REGISTER APPLICANT
    // =========================================================

    @Transactional
    public String register(RegisterRequest request) {

        // -----------------------------------------------------
        // CHECK USERNAME
        // -----------------------------------------------------

        if (userRepository
                .findByUsername(request.getUsername())
                .isPresent()) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        // -----------------------------------------------------
        // CHECK APPLICANT EMAIL
        // -----------------------------------------------------

        if (applicantRepository
                .existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // -----------------------------------------------------
        // CHECK EMPLOYER EMAIL
        // -----------------------------------------------------

        if (employerRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // -----------------------------------------------------
        // CREATE USER
        // -----------------------------------------------------

        User user = new User();

        user.setUsername(
                request.getUsername()
        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(
                Role.APPLICANT
        );

        user = userRepository.save(user);

        // -----------------------------------------------------
        // CREATE APPLICANT PROFILE
        // -----------------------------------------------------

        Applicant applicant = new Applicant();

        applicant.setFullName(
                request.getFullName()
        );

        applicant.setEmail(
                request.getEmail()
        );

        applicant.setPhone(
                request.getPhone()
        );

        applicant.setCountry("");

        applicant.setExperience("");

        applicant.setSkills("");

        applicant.setUser(user);

        applicantRepository.save(applicant);

        // -----------------------------------------------------
        // SEND WELCOME EMAIL ASYNCHRONOUSLY
        //
        // IMPORTANT:
        // This no longer blocks registration.
        // The applicant account is already saved.
        // -----------------------------------------------------

        emailService.sendApplicantRegistrationEmail(
                applicant.getEmail(),
                applicant.getFullName(),
                user.getUsername()
        );

        // -----------------------------------------------------
        // RETURN SUCCESS IMMEDIATELY
        // -----------------------------------------------------

        return "Applicant registered successfully.";
    }

    // =========================================================
    // REGISTER EMPLOYER
    // =========================================================

    @Transactional
    public String registerEmployer(
            EmployerRegisterRequest request) {

        // -----------------------------------------------------
        // CHECK USERNAME
        // -----------------------------------------------------

        if (userRepository
                .findByUsername(request.getUsername())
                .isPresent()) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        // -----------------------------------------------------
        // CHECK EMPLOYER EMAIL
        // -----------------------------------------------------

        if (employerRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Employer email already exists"
            );
        }

        // -----------------------------------------------------
        // CHECK APPLICANT EMAIL
        // -----------------------------------------------------

        if (applicantRepository
                .existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "This email is already registered"
            );
        }

        // -----------------------------------------------------
        // CREATE USER
        // -----------------------------------------------------

        User user = new User();

        user.setUsername(
                request.getUsername()
        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(
                Role.EMPLOYER
        );

        user = userRepository.save(user);

        // -----------------------------------------------------
        // CREATE EMPLOYER PROFILE
        // -----------------------------------------------------

        Employer employer = new Employer();

        employer.setUser(user);

        employer.setCompanyName(
                request.getCompanyName()
        );

        employer.setContactPerson(
                request.getContactPerson()
        );

        employer.setEmail(
                request.getEmail()
        );

        employer.setPhone(
                request.getPhone()
        );

        employer.setCountry(
                request.getCountry()
        );

        employer.setAddress(
                request.getAddress()
        );

        employer.setRegistrationNumber(
                request.getRegistrationNumber()
        );

        employer.setDescription(
                request.getDescription()
        );

        // -----------------------------------------------------
        // EMPLOYER ALWAYS STARTS AS PENDING
        // -----------------------------------------------------

        employer.setStatus(
                EmployerStatus.PENDING
        );

        employerRepository.save(employer);

        // -----------------------------------------------------
        // NOTIFY ADMIN ASYNCHRONOUSLY
        // -----------------------------------------------------

        emailService.sendEmployerRegistrationEmail(
                adminEmail,
                employer.getContactPerson(),
                employer.getEmail(),
                employer.getCompanyName()
        );

        // -----------------------------------------------------
        // RETURN SUCCESS IMMEDIATELY
        // -----------------------------------------------------

        return "Employer registration submitted successfully. "
                + "Your account is waiting for admin approval.";
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public LoginResponse login(
            LoginRequest request) {

        // -----------------------------------------------------
        // FIND USER
        // -----------------------------------------------------

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"
                        )
                );

        // -----------------------------------------------------
        // CHECK PASSWORD
        // -----------------------------------------------------

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        // =====================================================
        // EMPLOYER APPROVAL CHECK
        // =====================================================

        if (user.getRole() == Role.EMPLOYER) {

            Employer employer =
                    employerRepository
                            .findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Employer profile not found"
                                    )
                            );

            // -------------------------------------------------
            // PENDING
            // -------------------------------------------------

            if (employer.getStatus()
                    == EmployerStatus.PENDING) {

                throw new RuntimeException(
                        "Your employer account is waiting "
                                + "for admin approval."
                );
            }

            // -------------------------------------------------
            // REJECTED
            // -------------------------------------------------

            if (employer.getStatus()
                    == EmployerStatus.REJECTED) {

                throw new RuntimeException(
                        "Your employer account has been rejected."
                );
            }

            // -------------------------------------------------
            // APPROVED
            // -------------------------------------------------

            if (employer.getStatus()
                    != EmployerStatus.APPROVED) {

                throw new RuntimeException(
                        "Your employer account is not approved."
                );
            }
        }

        // =====================================================
        // GENERATE JWT
        // =====================================================

        String token = jwtUtil.generateToken(
                user.getUsername(),
                "ROLE_" + user.getRole().name()
        );

        // =====================================================
        // CREATE USER DTO
        // =====================================================

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        // =====================================================
        // RETURN LOGIN RESPONSE
        // =====================================================

        return new LoginResponse(
                token,
                userDTO
        );
    }
}