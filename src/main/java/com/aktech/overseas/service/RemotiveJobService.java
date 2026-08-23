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
public class RemotiveJobService {

    private static final String REMOTIVE_API =
            "https://remotive.com/api/remote-jobs";

    private final JobRepository jobRepository;

    private final RestTemplate restTemplate;

    public RemotiveJobService(
            JobRepository jobRepository) {

        this.jobRepository = jobRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Job> fetchAndSaveJobs() {

        System.out.println("==================================");
        System.out.println("FETCHING JOBS FROM REMOTIVE");
        System.out.println("==================================");

        Map<String, Object> response;

        try {

            response = restTemplate.getForObject(
                    REMOTIVE_API,
                    Map.class
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR FETCHING REMOTIVE JOBS: "
                            + e.getMessage()
            );

            return List.of();
        }

        if (response == null) {
            return List.of();
        }

        Object jobsObject = response.get("jobs");

        if (!(jobsObject instanceof List<?>)) {
            return List.of();
        }

        List<?> jobs = (List<?>) jobsObject;

        List<Job> savedJobs = new ArrayList<>();

        for (Object jobObject : jobs) {

            if (!(jobObject instanceof Map<?, ?>)) {
                continue;
            }

            Map<?, ?> remoteJob =
                    (Map<?, ?>) jobObject;

            String externalJobId =
                    getString(remoteJob, "id");

            if (externalJobId.isBlank()) {
                continue;
            }

            boolean exists =
                    jobRepository.existsBySourceAndExternalJobId(
                            "Remotive",
                            externalJobId
                    );

            if (exists) {
                System.out.println(
                        "JOB ALREADY EXISTS: "
                                + externalJobId
                );
                continue;
            }

            Job job =
                    convertToJob(remoteJob);

            job.setExternalJobId(
                    externalJobId
            );

            Job saved =
                    jobRepository.save(job);

            savedJobs.add(saved);

            System.out.println(
                    "NEW REMOTIVE JOB SAVED: "
                            + saved.getPosition()
            );
        }

        System.out.println(
                "NEW REMOTIVE JOBS: "
                        + savedJobs.size()
        );

        return savedJobs;
    }

    private Job convertToJob(
            Map<?, ?> remoteJob) {

        Job job = new Job();

        job.setPosition(
                getString(remoteJob, "title")
        );

        job.setCompany(
                getString(remoteJob, "company_name")
        );

        job.setCountry(
                getString(
                        remoteJob,
                        "candidate_required_location"
                )
        );

        job.setJobType("FOREIGN");

        job.setDescription(
                getString(
                        remoteJob,
                        "description"
                )
        );

        job.setRequirements(
                getString(
                        remoteJob,
                        "job_type"
                )
        );

        job.setExperience("");

        job.setSalary(
                getString(remoteJob, "salary")
        );

        job.setVacancies(1);

        job.setVerified(false);

        job.setSource("Remotive");

        job.setSourceUrl(
                getString(remoteJob, "url")
        );

        job.setExpiryDate(
                LocalDate.now().plusDays(30)
        );

        return job;
    }

    private String getString(
            Map<?, ?> map,
            String key) {

        Object value = map.get(key);

        if (value == null) {
            return "";
        }

        return value.toString();
    }
}