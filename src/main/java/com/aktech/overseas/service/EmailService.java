
        package com.aktech.overseas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =========================================================
    // COMMON EMAIL METHOD
    // =========================================================

    public void sendEmail(
            String to,
            String subject,
            String text) {

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);

            System.out.println(
                    "EMAIL SENT SUCCESSFULLY"
            );

            System.out.println(
                    "TO: " + to
            );

        } catch (Exception e) {

            System.err.println(
                    "EMAIL SENDING FAILED"
            );

            System.err.println(
                    "TO: " + to
            );

            System.err.println(
                    "ERROR: " + e.getMessage()
            );

            /*
             * Do NOT throw the exception again.
             *
             * Registration / approval should not fail
             * just because an email could not be delivered.
             */
        }
    }

    // =========================================================
    // APPLICANT REGISTRATION EMAIL
    // =========================================================

    public void sendApplicantRegistrationEmail(
            String email,
            String fullName,
            String username) {

        String subject =
                "Welcome to AKTech Overseas";

        String text =
                "Dear " + fullName + ",\n\n"
                        + "Welcome to AKTech Overseas!\n\n"
                        + "Your applicant account has been "
                        + "successfully created.\n\n"
                        + "Username: " + username + "\n\n"
                        + "You can now log in to the AKTech Overseas "
                        + "mobile application and explore available "
                        + "job opportunities.\n\n"
                        + "Thank you for joining AKTech Overseas.\n\n"
                        + "Best regards,\n"
                        + "AKTech Overseas";

        sendEmail(
                email,
                subject,
                text
        );
    }

    // =========================================================
    // EMPLOYER REGISTRATION EMAIL
    // =========================================================

    public void sendEmployerRegistrationEmail(
            String adminEmail,
            String contactPerson,
            String employerEmail,
            String companyName) {

        String subject =
                "New Employer Registration - AKTech Overseas";

        String text =
                "Dear Admin,\n\n"
                        + "A new employer has registered on "
                        + "AKTech Overseas.\n\n"
                        + "Company Name: "
                        + companyName + "\n"
                        + "Contact Person: "
                        + contactPerson + "\n"
                        + "Employer Email: "
                        + employerEmail + "\n\n"
                        + "The employer account is currently "
                        + "PENDING approval.\n\n"
                        + "Please log in to the admin panel and "
                        + "review the employer registration.\n\n"
                        + "AKTech Overseas";

        sendEmail(
                adminEmail,
                subject,
                text
        );
    }

    // =========================================================
    // EMPLOYER APPROVAL EMAIL
    // =========================================================

    public void sendEmployerApprovalEmail(
            String employerEmail,
            String contactPerson,
            String companyName) {

        String subject =
                "Employer Account Approved - AKTech Overseas";

        String text =
                "Dear " + contactPerson + ",\n\n"
                        + "We are pleased to inform you that your "
                        + "employer account for "
                        + companyName
                        + " has been APPROVED.\n\n"
                        + "You can now log in to the AKTech Overseas "
                        + "application and use the employer features.\n\n"
                        + "Thank you for choosing AKTech Overseas.\n\n"
                        + "Best regards,\n"
                        + "AKTech Overseas";

        sendEmail(
                employerEmail,
                subject,
                text
        );
    }

    // =========================================================
    // EMPLOYER REJECTION EMAIL
    // =========================================================

    public void sendEmployerRejectionEmail(
            String employerEmail,
            String contactPerson,
            String companyName) {

        String subject =
                "Employer Account Rejected - AKTech Overseas";

        String text =
                "Dear " + contactPerson + ",\n\n"
                        + "We regret to inform you that the employer "
                        + "registration for "
                        + companyName
                        + " has been REJECTED.\n\n"
                        + "If you believe this decision was made in "
                        + "error or you would like more information, "
                        + "please contact AKTech Overseas administration.\n\n"
                        + "Best regards,\n"
                        + "AKTech Overseas";

        sendEmail(
                employerEmail,
                subject,
                text
        );
    }
}

