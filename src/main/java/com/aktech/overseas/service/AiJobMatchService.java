package com.aktech.overseas.service;

import com.aktech.overseas.dto.AiJobMatchDTO;
import com.aktech.overseas.entity.Applicant;
import com.aktech.overseas.entity.Job;
import com.aktech.overseas.repository.ApplicantRepository;
import com.aktech.overseas.repository.JobRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class AiJobMatchService {

    private final ApplicantRepository applicantRepository;
    private final JobRepository jobRepository;

    public AiJobMatchService(
            ApplicantRepository applicantRepository,
            JobRepository jobRepository
    ) {
        this.applicantRepository = applicantRepository;
        this.jobRepository = jobRepository;
    }

    public List<AiJobMatchDTO> getRecommendedJobs(
            Long applicantId
    ) {
        Applicant applicant = getCurrentApplicant(applicantId);

        List<Job> activeJobs =
                jobRepository
                        .findByVerifiedTrueAndExpiryDateGreaterThanEqualOrderByIdDesc(
                                LocalDate.now()
                        );

        List<AiJobMatchDTO> recommendations =
                new ArrayList<>();

        for (Job job : activeJobs) {
            AiJobMatchDTO match = createMatch(
                    applicant,
                    job
            );

            // Show jobs with at least a small match.
            if (match.getMatchPercentage() >= 20) {
                recommendations.add(match);
            }
        }

        recommendations.sort(
                Comparator.comparing(
                        AiJobMatchDTO::getMatchPercentage
                ).reversed()
        );

        // Return the best 30 recommendations.
        if (recommendations.size() > 30) {
            return recommendations.subList(0, 30);
        }

        return recommendations;
    }

    private Applicant getCurrentApplicant(
            Long requestedApplicantId
    ) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Applicant applicant = applicantRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Applicant profile not found."
                        )
                );

        if (!applicant.getId().equals(requestedApplicantId)) {
            throw new AccessDeniedException(
                    "You can only view your own job recommendations."
            );
        }

        return applicant;
    }

    private AiJobMatchDTO createMatch(
            Applicant applicant,
            Job job
    ) {
        List<String> applicantSkills = splitValues(
                applicant.getSkills()
        );

        String jobText = normalize(
                join(
                        job.getPosition(),
                        job.getRequirements(),
                        job.getDescription()
                )
        );

        List<String> matchedSkills = new ArrayList<>();

        for (String skill : applicantSkills) {
            if (jobText.contains(
                    normalize(skill)
            )) {
                matchedSkills.add(skill);
            }
        }

        List<String> jobRequirements = splitValues(
                job.getRequirements()
        );

        List<String> missingSkills =
                findMissingRequirements(
                        applicantSkills,
                        jobRequirements
                );

        int skillScore = calculateSkillScore(
                applicantSkills,
                matchedSkills
        );

        int experienceScore = calculateExperienceScore(
                applicant.getExperience(),
                job.getExperience()
        );

        int matchPercentage = Math.min(
                100,
                skillScore + experienceScore
        );

        String reason = buildReason(
                applicant,
                job,
                matchedSkills,
                missingSkills,
                matchPercentage
        );

        return new AiJobMatchDTO(
                job.getId(),
                valueOrDefault(
                        job.getPosition(),
                        "Job Position"
                ),
                valueOrDefault(
                        job.getCompany(),
                        "Company"
                ),
                matchPercentage,
                matchedSkills,
                missingSkills,
                reason
        );
    }

    private int calculateSkillScore(
            List<String> applicantSkills,
            List<String> matchedSkills
    ) {
        if (applicantSkills.isEmpty()) {
            return 0;
        }

        double ratio =
                (double) matchedSkills.size()
                        / applicantSkills.size();

        return (int) Math.round(ratio * 70);
    }

    private int calculateExperienceScore(
            String applicantExperience,
            String jobExperience
    ) {
        int applicantYears = extractYears(
                applicantExperience
        );

        int requiredYears = extractYears(
                jobExperience
        );

        // If the job has no stated experience requirement,
        // award the experience portion.
        if (requiredYears == 0) {
            return 30;
        }

        if (applicantYears >= requiredYears) {
            return 30;
        }

        if (applicantYears > 0) {
            return 15;
        }

        return 0;
    }

    private List<String> findMissingRequirements(
            List<String> applicantSkills,
            List<String> jobRequirements
    ) {
        Set<String> missing = new LinkedHashSet<>();

        for (String requirement : jobRequirements) {
            String normalizedRequirement =
                    normalize(requirement);

            if (normalizedRequirement.isBlank()) {
                continue;
            }

            boolean found = applicantSkills.stream()
                    .map(this::normalize)
                    .anyMatch(skill ->
                            skill.contains(normalizedRequirement)
                                    || normalizedRequirement.contains(skill)
                    );

            if (!found) {
                missing.add(requirement);
            }
        }

        return new ArrayList<>(missing);
    }

    private String buildReason(
            Applicant applicant,
            Job job,
            List<String> matchedSkills,
            List<String> missingSkills,
            int score
    ) {
        if (score >= 80) {
            return "Strong match for your profile. "
                    + "Your skills and experience align well with "
                    + valueOrDefault(
                    job.getPosition(),
                    "this job"
            )
                    + ".";
        }

        if (score >= 60) {
            return "Good match based on your listed skills and experience. "
                    + "Review the job requirements before applying.";
        }

        if (!matchedSkills.isEmpty()) {
            return "Partial match. You have relevant skills for this role, "
                    + "but review the missing requirements before applying.";
        }

        if (!missingSkills.isEmpty()) {
            return "This role may need additional skills or experience "
                    + "before applying.";
        }

        return "This job is suggested based on your profile. "
                + "Please review the full requirements.";
    }

    private List<String> splitValues(
            String value
    ) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        String[] parts = value.split(
                "[,;\\n|/]"
        );

        Set<String> uniqueValues =
                new LinkedHashSet<>();

        for (String part : parts) {
            String cleaned = part.trim();

            if (!cleaned.isBlank()) {
                uniqueValues.add(cleaned);
            }
        }

        return new ArrayList<>(uniqueValues);
    }

    private int extractYears(
            String experience
    ) {
        if (experience == null || experience.isBlank()) {
            return 0;
        }

        String digits = experience.replaceAll(
                "[^0-9]",
                ""
        );

        if (digits.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private String join(
            String... values
    ) {
        StringBuilder builder = new StringBuilder();

        for (String value : values) {
            if (value != null && !value.isBlank()) {
                builder.append(value).append(" ");
            }
        }

        return builder.toString();
    }

    private String normalize(
            String value
    ) {
        if (value == null) {
            return "";
        }

        return value
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("\\s+", " ");
    }

    private String valueOrDefault(
            String value,
            String defaultValue
    ) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    }
}