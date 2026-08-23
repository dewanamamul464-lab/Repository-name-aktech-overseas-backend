package com.aktech.overseas.service;

import com.aktech.overseas.entity.Job;
import com.aktech.overseas.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HimalayasJobService {

    private static final String API =
            "https://himalayas.app/jobs/api";

    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

    public HimalayasJobService(
            JobRepository jobRepository) {

        this.jobRepository = jobRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Job> fetchAndSaveJobs() {

        List<Job> savedJobs = new ArrayList<>();

        try {

            for (int offset = 0; offset < 100; offset += 20) {

                String url =
                        API
                                + "?offset="
                                + offset
                                + "&limit=20";

                Map<String, Object> response =
                        restTemplate.getForObject(
                                url,
                                Map.class
                        );

                if (response == null) {
                    break;
                }

                Object jobsObject =
                        response.get("jobs");

                if (!(jobsObject instanceof List<?>)) {
                    break;
                }

                List<?> jobs =
                        (List<?>) jobsObject;

                if (jobs.isEmpty()) {
                    break;
                }

                for (Object object : jobs) {

                    if (!(object instanceof Map<?, ?>)) {
                        continue;
                    }

                    Map<?, ?> remote =
                            (Map<?, ?>) object;

                    String externalId =
                            getString(
                                    remote,
                                    "guid"
                            );

                    String sourceUrl =
                            getString(
                                    remote,
                                    "applicationLink"
                            );

                    if (externalId.isBlank()) {
                        externalId = sourceUrl;
                    }

                    if (externalId.isBlank()) {
                        continue;
                    }

                    if (jobRepository
                            .existsBySourceAndExternalJobId(
                                    "Himalayas",
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
                                    "companyName"
                            )
                    );

                    job.setCountry(
                            getLocation(
                                    remote
                            )
                    );

                    job.setJobType(
                            getString(
                                    remote,
                                    "employmentType"
                            )
                    );

                    job.setDescription(
                            getString(
                                    remote,
                                    "description"
                            )
                    );

                    job.setRequirements(
                            joinList(
                                    remote.get(
                                            "categories"
                                    )
                            )
                    );

                    job.setExperience(
                            joinList(
                                    remote.get(
                                            "seniority"
                                    )
                            )
                    );

                    job.setSalary(
                            buildSalary(
                                    remote
                            )
                    );

                    job.setVacancies(1);

                    job.setVerified(false);

                    job.setSource(
                            "Himalayas"
                    );

                    job.setSourceUrl(
                            sourceUrl
                    );

                    job.setExpiryDate(
                            getExpiryDate(
                                    remote
                            )
                    );

                    Job saved =
                            jobRepository.save(job);

                    savedJobs.add(saved);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "HIMALAYAS IMPORT ERROR: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "HIMALAYAS NEW JOBS: "
                        + savedJobs.size()
        );

        return savedJobs;
    }

    private String getLocation(
            Map<?, ?> remote) {

        Object locations =
                remote.get(
                        "locationRestrictions"
                );

        if (!(locations instanceof List<?>)) {
            return "Remote";
        }

        List<?> list =
                (List<?>) locations;

        List<String> names =
                new ArrayList<>();

        for (Object item : list) {

            if (item instanceof Map<?, ?> map) {

                String name =
                        getString(
                                map,
                                "name"
                        );

                if (!name.isBlank()) {
                    names.add(name);
                }
            }
        }

        return names.isEmpty()
                ? "Worldwide"
                : String.join(", ", names);
    }

    private String buildSalary(
            Map<?, ?> remote) {

        Object min =
                remote.get("minSalary");

        Object max =
                remote.get("maxSalary");

        String currency =
                getString(
                        remote,
                        "currency"
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

    private LocalDate getExpiryDate(
            Map<?, ?> remote) {

        Object value =
                remote.get("expiryDate");

        if (value instanceof Number number) {

            return Instant
                    .ofEpochMilli(
                            number.longValue()
                    )
                    .atZone(
                            ZoneOffset.UTC
                    )
                    .toLocalDate();
        }

        return LocalDate.now()
                .plusDays(30);
    }

    private String joinList(
            Object value) {

        if (!(value instanceof List<?> list)) {
            return "";
        }

        return list.stream()
                .map(String::valueOf)
                .filter(s -> !s.isBlank())
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