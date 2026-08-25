
        package com.aktech.overseas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =========================================================
    // APPLICANT REGISTRATION EMAIL
    // =========================================================

    public void sendApplicantRegistrationEmail(
            String email,
            String fullName,
            String username) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(senderEmail);

            message.setTo(email);

            message.setSubject(
                    "Welcome to AKTech Overseas"
            );

            message.setText(
                    "Dear " + fullName + ",\n\n"
                            + "Welcome to AKTech Overseas!\n\n"
                            + "Your applicant account has been "
                            + "successfully created.\n\n"
                            + "Username: " + username + "\n\n"
                            + "You can now log in to the "
                            + "AKTech Overseas mobile application "
                            + "and explore available jobs.\n\n"
                            + "Thank you for choosing "
                            + "AKTech Overseas.\n\n"
                            + "Best regards,\n"
                            + "AKTech Overseas Team"
            );

            mailSender.send(message);

            System.out.println(
                    "Applicant registration email sent to: "
                            + email
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send applicant registration email "
                            + "to: " + email
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // EMPLOYER REGISTRATION EMAIL
    // =========================================================

    public void sendEmployerRegistrationEmail(
            String adminEmail,
            String contactPerson,
            String employerEmail,
            String companyName) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(senderEmail);

            message.setTo(adminEmail);

            message.setSubject(
                    "New Employer Registration - "
                            + companyName
            );

            message.setText(
                    "Dear Admin,\n\n"
                            + "A new employer has registered "
                            + "on AKTech Overseas.\n\n"
                            + "Company Name: "
                            + companyName + "\n"
                            + "Contact Person: "
                            + contactPerson + "\n"
                            + "Employer Email: "
                            + employerEmail + "\n\n"
                            + "The employer account is currently "
                            + "PENDING approval.\n\n"
                            + "Please log in to the admin account "
                            + "and review the employer registration.\n\n"
                            + "Best regards,\n"
                            + "AKTech Overseas System"
            );

            mailSender.send(message);

            System.out.println(
                    "Employer registration notification sent "
                            + "to admin: " + adminEmail
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send employer registration "
                            + "notification to admin: "
                            + adminEmail
            );

            e.printStackTrace();
        }
    }
}

