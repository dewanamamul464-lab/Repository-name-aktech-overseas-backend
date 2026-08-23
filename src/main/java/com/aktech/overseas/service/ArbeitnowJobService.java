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
public class ArbeitnowJobService {

    private static final String API =
            "https://www.arbeitnow.com/api/job-board-api";

    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

    public ArbeitnowJobService(
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

            Object data =
                    response.get("data");

            if (!(data instanceof List<?> jobs)) {
                return savedJobs;
            }

            for (Object object : jobs) {

                if (!(object instanceof Map<?, ?> remote)) {
                    continue;
                }

                String sourceUrl =
                        getString(
                                remote,
                                "url"
                        );

                String externalId =
                        getString(
                                remote,
                                "slug"
                        );

                if (externalId.isBlank()) {
                    externalId = sourceUrl;
                }

                if (externalId.isBlank()) {
                    continue;
                }

                if (jobRepository
                        .existsBySourceAndExternalJobId(
                                "Arbeitnow",
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
                                "title"
                        )
                );

                job.setCompany(
                        getString(
                                remote,
                                "company_name"
                        )
                );

                job.setCountry(
                        getString(
                                remote,
                                "location"
                        )
                );

                job.setJobType(
                        isRemote(remote)
                                ? "FOREIGN - REMOTE"
                                : "FOREIGN"
                );

                job.setDescription(
                        getString(
                                remote,
                                "description"
                        )
                );

                job.setRequirements(
                        joinList(
                                remote.get("tags")
                        )
                );

                job.setExperience("");

                job.setSalary(
                        getString(
                                remote,
                                "salary"
                        )
                );

                job.setVacancies(1);

                job.setVerified(false);

                job.setSource(
                        "Arbeitnow"
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
                    "ARBEITNOW IMPORT ERROR: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "ARBEITNOW NEW JOBS: "
                        + savedJobs.size()
        );

        return savedJobs;
    }

    private boolean isRemote(
            Map<?, ?> map) {

        Object value =
                map.get("remote");

        if (value instanceof Boolean b) {
            return b;
        }

        return value != null
                && value.toString()
                .equalsIgnoreCase("true");
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