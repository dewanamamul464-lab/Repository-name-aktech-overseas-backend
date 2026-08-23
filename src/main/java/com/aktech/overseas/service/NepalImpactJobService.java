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
public class NepalImpactJobService {

    private static final String API =
            "https://nepalimpact.org/api/v1/opportunities";

    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

    public NepalImpactJobService(
            JobRepository jobRepository) {

        this.jobRepository = jobRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Job> fetchAndSaveJobs() {

        List<Job> savedJobs =
                new ArrayList<>();

        try {

            String url =
                    API
                            + "?limit=200"
                            + "&location=Nepal";

            Map<String, Object> response =
                    restTemplate.getForObject(
                            url,
                            Map.class
                    );

            if (response == null) {
                return savedJobs;
            }

            Object data =
                    response.get("data");

            if (!(data instanceof List<?> opportunities)) {
                return savedJobs;
            }

            for (Object object : opportunities) {

                if (!(object instanceof Map<?, ?> opportunity)) {
                    continue;
                }

                String type =
                        getString(
                                opportunity,
                                "type"
                        );

                // Only import actual jobs.
                // Do not import tenders.
                if (!type.isBlank()
                        && !type.equalsIgnoreCase("job")) {
                    continue;
                }

                String externalId =
                        getString(
                                opportunity,
                                "id"
                        );

                String sourceUrl =
                        getString(
                                opportunity,
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
                                "Nepal Impact",
                                externalId)) {
                    continue;
                }

                Job job =
                        new Job();

                job.setExternalJobId(
                        externalId
                );

                job.setPosition(
                        firstNonBlank(
                                opportunity,
                                "title",
                                "name",
                                "position"
                        )
                );

                job.setCompany(
                        firstNonBlank(
                                opportunity,
                                "organization",
                                "organization_name",
                                "company",
                                "employer"
                        )
                );

                job.setCountry("Nepal");

                job.setJobType("DOMESTIC");

                job.setDescription(
                        firstNonBlank(
                                opportunity,
                                "description",
                                "summary",
                                "excerpt"
                        )
                );

                job.setRequirements(
                        firstNonBlank(
                                opportunity,
                                "requirements",
                                "category"
                        )
                );

                job.setExperience("");

                job.setSalary(
                        firstNonBlank(
                                opportunity,
                                "salary",
                                "compensation"
                        )
                );

                job.setVacancies(1);

                job.setVerified(false);

                job.setSource(
                        "Nepal Impact"
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
                    "NEPAL IMPACT IMPORT ERROR: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "NEPAL IMPACT NEW JOBS: "
                        + savedJobs.size()
        );

        return savedJobs;
    }

    private String firstNonBlank(
            Map<?, ?> map,
            String... keys) {

        for (String key : keys) {

            String value =
                    getString(map, key);

            if (!value.isBlank()) {
                return value;
            }
        }

        return "";
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