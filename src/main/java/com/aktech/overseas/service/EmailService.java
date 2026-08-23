package com.aktech.overseas.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =========================================================
    // APPLICATION SUBMITTED
    // =========================================================

    public void sendApplicationSubmittedEmail(
            String applicantEmail,
            String applicantName,
            String companyName,
            String jobPosition) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(applicantEmail);
            message.setSubject(
                    "Application Submitted - AKTech Overseas"
            );

            message.setText(
                    "Hello " + applicantName + ",\n\n"
                            + "Your job application has been successfully "
                            + "submitted through AKTech Overseas.\n\n"
                            + "Company: " + companyName + "\n"
                            + "Position: " + jobPosition + "\n"
                            + "Status: PENDING\n\n"
                            + "Your application is now waiting for review.\n\n"
                            + "You can check your application status from "
                            + "the \"My Applications\" section of the "
                            + "AKTech Overseas app.\n\n"
                            + "Thank you for using AKTech Overseas.\n\n"
                            + "Regards,\n"
                            + "AKTech Overseas"
            );

            mailSender.send(message);

            System.out.println(
                    "Application submitted email sent to: "
                            + applicantEmail
            );

        } catch (Exception e) {
            System.out.println(
                    "Failed to send application submitted email: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // APPLICATION STATUS
    // =========================================================

    public void sendApplicationStatusEmail(
            String applicantEmail,
            String applicantName,
            String jobPosition,
            String status) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(applicantEmail);
            message.setSubject(
                    "Application Status Update - AKTech Overseas"
            );

            message.setText(
                    "Hello " + applicantName + ",\n\n"
                            + "There has been an update to your "
                            + "job application.\n\n"
                            + "Job Position: " + jobPosition + "\n"
                            + "Application Status: " + status + "\n\n"
                            + "Please open the AKTech Overseas app "
                            + "and visit the \"My Applications\" section "
                            + "to see your application.\n\n"
                            + "Thank you for using AKTech Overseas.\n\n"
                            + "Regards,\n"
                            + "AKTech Overseas"
            );

            mailSender.send(message);

            System.out.println(
                    "Application status email sent to: "
                            + applicantEmail
            );

        } catch (Exception e) {
            System.out.println(
                    "Failed to send application status email: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // EMPLOYER REGISTRATION
    // Sends notification to ADMIN
    // =========================================================

    public void sendEmployerRegistrationEmail(
            String adminEmail,
            String employerName,
            String employerEmail,
            String companyName) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(adminEmail);

            message.setSubject(
                    "New Employer Registration Request - AKTech Overseas"
            );

            message.setText(
                    "Hello Admin,\n\n"
                            + "A new employer has registered on "
                            + "AKTech Overseas and is waiting for "
                            + "administrator approval.\n\n"
                            + "Employer Name: " + employerName + "\n"
                            + "Employer Email: " + employerEmail + "\n"
                            + "Company Name: " + companyName + "\n\n"
                            + "Status: PENDING\n\n"
                            + "Please review this employer before "
                            + "granting employer permissions.\n\n"
                            + "The employer must not be allowed to "
                            + "publish jobs until approved by an "
                            + "administrator.\n\n"
                            + "Regards,\n"
                            + "AKTech Overseas"
            );

            mailSender.send(message);

            System.out.println(
                    "Employer registration email sent to admin: "
                            + adminEmail
            );

        } catch (Exception e) {
            System.out.println(
                    "Failed to send employer registration email: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // EMPLOYER APPROVED
    // =========================================================

    public void sendEmployerApprovalEmail(
            String employerEmail,
            String employerName,
            String companyName) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(employerEmail);

            message.setSubject(
                    "Employer Account Approved - AKTech Overseas"
            );

            message.setText(
                    "Hello " + employerName + ",\n\n"
                            + "Good news!\n\n"
                            + "Your employer account for "
                            + companyName
                            + " has been approved by the "
                            + "AKTech Overseas administrator.\n\n"
                            + "You can now log in to AKTech Overseas "
                            + "and use the employer features available "
                            + "to your account.\n\n"
                            + "Please make sure that all jobs and "
                            + "information you publish are genuine, "
                            + "accurate, and legitimate.\n\n"
                            + "Regards,\n"
                            + "AKTech Overseas"
            );

            mailSender.send(message);

            System.out.println(
                    "Employer approval email sent to: "
                            + employerEmail
            );

        } catch (Exception e) {
            System.out.println(
                    "Failed to send employer approval email: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // EMPLOYER REJECTED
    // =========================================================

    public void sendEmployerRejectionEmail(
            String employerEmail,
            String employerName,
            String companyName) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(employerEmail);

            message.setSubject(
                    "Employer Registration Update - AKTech Overseas"
            );

            message.setText(
                    "Hello " + employerName + ",\n\n"
                            + "We are sorry to inform you that your "
                            + "employer registration request for "
                            + companyName
                            + " was not approved by the "
                            + "AKTech Overseas administrator.\n\n"
                            + "If you believe this was a mistake, "
                            + "please contact AKTech Overseas "
                            + "administration.\n\n"
                            + "Regards,\n"
                            + "AKTech Overseas"
            );

            mailSender.send(message);

            System.out.println(
                    "Employer rejection email sent to: "
                            + employerEmail
            );

        } catch (Exception e) {
            System.out.println(
                    "Failed to send employer rejection email: "
                            + e.getMessage()
            );
        }
    }
}