package com.aktech.overseas.service;

import com.aktech.overseas.entity.Job;
import com.aktech.overseas.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JobicyJobService {

    private static final String API =
            "https://jobicy.com/api/v2/remote-jobs?count=100";

    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

    public JobicyJobService(
            JobRepository jobRepository) {

        this.jobRepository = jobRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Job> fetchAndSaveJobs() {

        List<Job> savedJobs =
                new ArrayList<>();

        try {

            Map<String, Object> response =
                    restTemplate.getForObject(
                            API,
                            Map.class
                    );

            if (response == null) {
                return savedJobs;
            }

            Object jobsObject =
                    response.get("jobs");

            if (!(jobsObject instanceof List<?> jobs)) {
                return savedJobs;
            }

            for (Object object : jobs) {

                if (!(object instanceof Map<?, ?> remote)) {
                    continue;
                }

                String externalId =
                        getString(
                                remote,
                                "id"
                        );

                String sourceUrl =
                        getString(
                                remote,
                                "url"
                        );

                if (externalId.isBlank()) {
                    externalId = sourceUrl;
                }

                if (externalId.isBlank()) {
                    continue;
                }

                if (jobRepository
                        .existsBySourceAndExternalJobId(
                                "Jobicy",
                                externalId)) {
                    continue;
                }

                Job job =
                        new Job();

                job.setExternalJobId(
                        externalId
                );

                job.setPosition(
                        getString(
                                remote,
                                "jobTitle"
                        )
                );

                job.setCompany(
                        getString(
                                remote,
                                "companyName"
                        )
                );

                job.setCountry(
                        getString(
                                remote,
                                "jobGeo"
                        )
                );

                job.setJobType(
                        "FOREIGN - REMOTE"
                );

                job.setDescription(
                        getString(
                                remote,
                                "jobDescription"
                        )
                );

                job.setRequirements(
                        joinList(
                                remote.get(
                                        "jobIndustry"
                                )
                        )
                );

                job.setExperience(
                        getString(
                                remote,
                                "jobLevel"
                        )
                );

                job.setSalary(
                        buildSalary(remote)
                );

                job.setVacancies(1);

                job.setVerified(false);

                job.setSource(
                        "Jobicy"
                );

                job.setSourceUrl(
                        sourceUrl
                );

                job.setExpiryDate(
                        LocalDate.now()
                                .plusDays(30)
                );

                Job saved =
                        jobRepository.save(job);

                savedJobs.add(saved);
            }

        } catch (Exception e) {

            System.out.println(
                    "JOBICY IMPORT ERROR: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "JOBICY NEW JOBS: "
                        + savedJobs.size()
        );

        return savedJobs;
    }

    private String buildSalary(
            Map<?, ?> remote) {

        Object min =
                remote.get("salaryMin");

        Object max =
                remote.get("salaryMax");

        String currency =
                getString(
                        remote,
                        "salaryCurrency"
                );

        if (min == null && max == null) {
            return "";
        }

        if (min != null && max != null) {

            return min
                    + " - "
                    + max
                    + " "
                    + currency;
        }

        return String.valueOf(
                min != null ? min : max
        ) + " " + currency;
    }

    private String joinList(
            Object value) {

        if (!(value instanceof List<?> list)) {
            return "";
        }

        return list.stream()
                .map(String::valueOf)
                .reduce(
                        (a, b) -> a + ", " + b
                )
                .orElse("");
    }

    private String getString(
            Map<?, ?> map,
            String key) {

        Object value =
                map.get(key);

        return value == null
                ? ""
                : value.toString();
    }
}