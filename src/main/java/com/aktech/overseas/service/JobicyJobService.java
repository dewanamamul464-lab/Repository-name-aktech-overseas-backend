package com.aktech.overseas.service;

import com.aktech.overseas.entity.Job;
import com.aktech.overseas.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JobicyJobService {

    private static final String JOBICY_API =
            "https://jobicy.com/api/v2/remote-jobs";

    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

    public JobicyJobService(
            JobRepository jobRepository,
            RestTemplate restTemplate
    ) {
        this.jobRepository = jobRepository;
        this.restTemplate = restTemplate;
    }

    // =========================================================
    // FETCH AND SAVE JOBS
    // =========================================================

    public List<Job> fetchAndSaveJobs() {

        List<Job> savedJobs = new ArrayList<>();

        try {

            Map<String, Object> response =
                    restTemplate.getForObject(
                            JOBICY_API,
                            Map.class
                    );

            if (response == null) {

                System.out.println(
                        "Jobicy response is empty."
                );

                return savedJobs;
            }

            Object jobsObject = response.get("jobs");

            if (!(jobsObject instanceof List<?> jobs)) {

                System.out.println(
                        "No jobs found in Jobicy response."
                );

                return savedJobs;
            }

            for (Object object : jobs) {

                if (!(object instanceof Map<?, ?> jobData)) {
                    continue;
                }

                Job savedJob = saveJob(jobData);

                if (savedJob != null) {
                    savedJobs.add(savedJob);
                }
            }

            System.out.println(
                    "Jobicy import completed. Saved jobs: "
                            + savedJobs.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Jobicy import failed: "
                            + e.getMessage()
            );
        }

        return savedJobs;
    }

    // =========================================================
    // SAVE ONE JOB
    // =========================================================

    private Job saveJob(Map<?, ?> jobData) {

        try {

            String externalJobId =
                    getString(jobData, "id");

            if (externalJobId == null
                    || externalJobId.isBlank()) {

                return null;
            }

            // =================================================
            // PREVENT DUPLICATES
            // =================================================

            if (jobRepository.existsBySourceAndExternalJobId(
                    "JOBICY",
                    externalJobId
            )) {

                return null;
            }

            // =================================================
            // READ JOBICY DATA
            // =================================================

            String position =
                    getString(jobData, "jobTitle");

            String company =
                    getString(jobData, "companyName");

            String country =
                    getString(jobData, "jobGeo");

            String description =
                    getString(jobData, "jobDescription");

            String jobType =
                    getString(jobData, "jobType");

            // =================================================
            // CREATE JOB
            // =================================================

            Job job = new Job();

            job.setPosition(position);
            job.setCompany(company);
            job.setCountry(country);
            job.setDescription(description);
            job.setJobType(jobType);

            job.setSource("JOBICY");
            job.setExternalJobId(externalJobId);

            // =================================================
            // SAVE
            // =================================================

            return jobRepository.save(job);

        } catch (Exception e) {

            System.err.println(
                    "Failed to save Jobicy job: "
                            + e.getMessage()
            );

            return null;
        }
    }

    // =========================================================
    // SAFE STRING VALUE
    // =========================================================

    private String getString(
            Map<?, ?> data,
            String key
    ) {

        Object value = data.get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }
}