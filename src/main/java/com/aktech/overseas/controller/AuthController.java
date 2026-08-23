package com.aktech.overseas.controller;

import com.aktech.overseas.dto.EmployerRegisterRequest;
import com.aktech.overseas.dto.LoginRequest;
import com.aktech.overseas.dto.LoginResponse;
import com.aktech.overseas.dto.RegisterRequest;
import com.aktech.overseas.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================================================
    // REGISTER APPLICANT
    // =========================================================

    @PostMapping("/register")
    public String register(
            @Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    // =========================================================
    // REGISTER EMPLOYER
    // =========================================================

    @PostMapping("/register-employer")
    public String registerEmployer(
            @Valid @RequestBody EmployerRegisterRequest request) {

        return authService.registerEmployer(request);
    }

    // =========================================================
    // LOGIN
    // =========================================================

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}