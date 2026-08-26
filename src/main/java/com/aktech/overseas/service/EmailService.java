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

    // =========================================================
    // EMPLOYER APPROVAL EMAIL
    // =========================================================

    public void sendEmployerApprovalEmail(
            String employerEmail,
            String contactPerson,
            String companyName) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(senderEmail);
            message.setTo(employerEmail);

            message.setSubject(
                    "Employer Account Approved - "
                            + "AKTech Overseas"
            );

            message.setText(
                    "Dear " + contactPerson + ",\n\n"
                            + "Congratulations!\n\n"
                            + "Your employer account for "
                            + companyName
                            + " has been approved by the "
                            + "AKTech Overseas administration team.\n\n"
                            + "You can now log in to the "
                            + "AKTech Overseas application and "
                            + "access your employer account.\n\n"
                            + "You can create and manage job "
                            + "vacancies from your employer account.\n\n"
                            + "Thank you for choosing "
                            + "AKTech Overseas.\n\n"
                            + "Best regards,\n"
                            + "AKTech Overseas Admin Team"
            );

            mailSender.send(message);

            System.out.println(
                    "Employer approval email sent to: "
                            + employerEmail
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send employer approval email "
                            + "to: " + employerEmail
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // EMPLOYER REJECTION EMAIL
    // =========================================================

    public void sendEmployerRejectionEmail(
            String employerEmail,
            String contactPerson,
            String companyName) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(senderEmail);
            message.setTo(employerEmail);

            message.setSubject(
                    "Employer Account Rejected - "
                            + "AKTech Overseas"
            );

            message.setText(
                    "Dear " + contactPerson + ",\n\n"
                            + "We regret to inform you that your "
                            + "employer registration for "
                            + companyName
                            + " has been rejected by the "
                            + "AKTech Overseas administration team.\n\n"
                            + "If you believe this decision was "
                            + "made in error or you would like "
                            + "further information, please contact "
                            + "the AKTech Overseas administration team.\n\n"
                            + "Thank you for your interest in "
                            + "AKTech Overseas.\n\n"
                            + "Best regards,\n"
                            + "AKTech Overseas Admin Team"
            );

            mailSender.send(message);

            System.out.println(
                    "Employer rejection email sent to: "
                            + employerEmail
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send employer rejection email "
                            + "to: " + employerEmail
            );

            e.printStackTrace();
        }
    }
}