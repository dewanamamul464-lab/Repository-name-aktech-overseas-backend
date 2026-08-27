package com.aktech.overseas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    // =========================================================
    // BREVO CONFIGURATION
    // =========================================================

    @Value("${BREVO_API_KEY:}")
    private String brevoApiKey;

    @Value("${BREVO_SENDER_EMAIL:dakudrs@gmail.com}")
    private String senderEmail;

    @Value("${BREVO_SENDER_NAME:AKTech Overseas}")
    private String senderName;

    // =========================================================
    // HTTP CLIENT
    // =========================================================

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    public EmailService(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;

        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    // =========================================================
    // COMMON EMAIL METHOD
    // =========================================================

    public void sendEmail(
            String to,
            String subject,
            String text) {

        try {

            // -------------------------------------------------
            // CHECK API KEY
            // -------------------------------------------------

            if (brevoApiKey == null
                    || brevoApiKey.isBlank()) {

                System.err.println(
                        "BREVO EMAIL FAILED"
                );

                System.err.println(
                        "ERROR: BREVO_API_KEY is not configured"
                );

                return;
            }

            // -------------------------------------------------
            // BREVO REQUEST BODY
            // -------------------------------------------------

            Map<String, Object> sender =
                    Map.of(
                            "name",
                            senderName,

                            "email",
                            senderEmail
                    );

            Map<String, String> recipient =
                    Map.of(
                            "email",
                            to
                    );

            Map<String, Object> requestBody =
                    Map.of(
                            "sender",
                            sender,

                            "to",
                            List.of(recipient),

                            "subject",
                            subject,

                            "textContent",
                            text
                    );

            String json =
                    objectMapper.writeValueAsString(
                            requestBody
                    );

            // -------------------------------------------------
            // CREATE BREVO HTTP REQUEST
            // -------------------------------------------------

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            "https://api.brevo.com/v3/smtp/email"
                                    )
                            )
                            .header(
                                    "accept",
                                    "application/json"
                            )
                            .header(
                                    "api-key",
                                    brevoApiKey
                            )
                            .header(
                                    "content-type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(json)
                            )
                            .build();

            // -------------------------------------------------
            // SEND EMAIL
            // -------------------------------------------------

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            // -------------------------------------------------
            // CHECK RESPONSE
            // -------------------------------------------------

            if (response.statusCode() >= 200
                    && response.statusCode() < 300) {

                System.out.println(
                        "EMAIL SENT SUCCESSFULLY"
                );

                System.out.println(
                        "TO: " + to
                );

                System.out.println(
                        "BREVO RESPONSE: "
                                + response.body()
                );

            } else {

                System.err.println(
                        "EMAIL SENDING FAILED"
                );

                System.err.println(
                        "TO: " + to
                );

                System.err.println(
                        "BREVO HTTP STATUS: "
                                + response.statusCode()
                );

                System.err.println(
                        "BREVO RESPONSE: "
                                + response.body()
                );
            }

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

    // =========================================================
// APPLICATION SUBMITTED EMAIL
// =========================================================

    public void sendApplicationSubmittedEmail(
            String applicantEmail,
            String applicantName,
            String companyName,
            Long applicationId) {

        String subject =
                "Job Application Submitted - AKTech Overseas";

        String text =
                "Dear " + applicantName + ",\n\n"
                        + "Your job application has been "
                        + "successfully submitted.\n\n"
                        + "Company: " + companyName + "\n"
                        + "Application ID: " + applicationId + "\n"
                        + "Status: PENDING\n\n"
                        + "The employer will review your application. "
                        + "You will receive another email when the "
                        + "application status is updated.\n\n"
                        + "Thank you for using AKTech Overseas.\n\n"
                        + "Best regards,\n"
                        + "AKTech Overseas";

        sendEmail(
                applicantEmail,
                subject,
                text
        );
    }


// =========================================================
// APPLICATION APPROVED EMAIL
// =========================================================

    public void sendApplicationApprovedEmail(
            String applicantEmail,
            String applicantName,
            String companyName,
            Long applicationId) {

        String subject =
                "Job Application Approved - AKTech Overseas";

        String text =
                "Dear " + applicantName + ",\n\n"
                        + "We are pleased to inform you that your "
                        + "job application has been APPROVED.\n\n"
                        + "Company: " + companyName + "\n"
                        + "Application ID: " + applicationId + "\n"
                        + "Status: APPROVED\n\n"
                        + "Please log in to the AKTech Overseas "
                        + "application for more information.\n\n"
                        + "Congratulations!\n\n"
                        + "Best regards,\n"
                        + "AKTech Overseas";

        sendEmail(
                applicantEmail,
                subject,
                text
        );
    }


// =========================================================
// APPLICATION REJECTED EMAIL
// =========================================================

    public void sendApplicationRejectedEmail(
            String applicantEmail,
            String applicantName,
            String companyName,
            Long applicationId) {

        String subject =
                "Job Application Status Update - AKTech Overseas";

        String text =
                "Dear " + applicantName + ",\n\n"
                        + "We regret to inform you that your "
                        + "job application was not selected by the "
                        + "employer at this time.\n\n"
                        + "Company: " + companyName + "\n"
                        + "Application ID: " + applicationId + "\n"
                        + "Status: REJECTED\n\n"
                        + "We encourage you to continue exploring "
                        + "other job opportunities available on "
                        + "AKTech Overseas.\n\n"
                        + "Thank you for using AKTech Overseas.\n\n"
                        + "Best regards,\n"
                        + "AKTech Overseas";

        sendEmail(
                applicantEmail,
                subject,
                text
        );
    }
}