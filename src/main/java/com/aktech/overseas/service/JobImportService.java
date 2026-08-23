package com.aktech.overseas.service;

import com.aktech.overseas.entity.Job;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobImportService {

    private final RemotiveJobService remotiveJobService;
    private final HimalayasJobService himalayasJobService;
    private final ArbeitnowJobService arbeitnowJobService;
    private final JobicyJobService jobicyJobService;
    private final NepalImpactJobService nepalImpactJobService;

    public JobImportService(
            RemotiveJobService remotiveJobService,
            HimalayasJobService himalayasJobService,
            ArbeitnowJobService arbeitnowJobService,
            JobicyJobService jobicyJobService,
            NepalImpactJobService nepalImpactJobService) {

        this.remotiveJobService =
                remotiveJobService;

        this.himalayasJobService =
                himalayasJobService;

        this.arbeitnowJobService =
                arbeitnowJobService;

        this.jobicyJobService =
                jobicyJobService;

        this.nepalImpactJobService =
                nepalImpactJobService;
    }

    // =========================================================
    // IMPORT ALL SOURCES
    // =========================================================

    public Map<String, Integer> importAllJobs() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("       AKTECH OVERSEAS JOB IMPORT");
        System.out.println("==========================================");

        Map<String, Integer> results =
                new LinkedHashMap<>();

        // =====================================================
        // REMOTIVE
        // =====================================================

        results.put(
                "Remotive",
                runImport(
                        "Remotive",
                        remotiveJobService::fetchAndSaveJobs
                )
        );

        // =====================================================
        // HIMALAYAS
        // =====================================================

        results.put(
                "Himalayas",
                runImport(
                        "Himalayas",
                        himalayasJobService::fetchAndSaveJobs
                )
        );

        // =====================================================
        // ARBEITNOW
        // =====================================================

        results.put(
                "Arbeitnow",
                runImport(
                        "Arbeitnow",
                        arbeitnowJobService::fetchAndSaveJobs
                )
        );

        // =====================================================
        // JOBICY
        // =====================================================

        results.put(
                "Jobicy",
                runImport(
                        "Jobicy",
                        jobicyJobService::fetchAndSaveJobs
                )
        );

        // =====================================================
        // NEPAL IMPACT
        // =====================================================

        results.put(
                "Nepal Impact",
                runImport(
                        "Nepal Impact",
                        nepalImpactJobService::fetchAndSaveJobs
                )
        );

        // =====================================================
        // TOTAL
        // =====================================================

        int total =
                results.values()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .sum();

        System.out.println();
        System.out.println("==========================================");
        System.out.println("IMPORT COMPLETE");
        System.out.println("==========================================");

        results.forEach(
                (source, count) ->
                        System.out.println(
                                source
                                        + " : "
                                        + count
                                        + " new jobs"
                        )
        );

        System.out.println(
                "TOTAL NEW JOBS: "
                        + total
        );

        System.out.println(
                "=========================================="
        );

        return results;
    }

    // =========================================================
    // AUTOMATIC IMPORT
    // =========================================================

    // Runs once every hour.
    //
    // The individual public APIs are also cached/rate-limited,
    // so hourly importing is intentionally conservative.

    @Scheduled(fixedRate = 3600000)
    public void scheduledImport() {

        System.out.println();
        System.out.println(
                "AUTOMATIC MULTI-SOURCE JOB IMPORT"
        );

        importAllJobs();
    }

    // =========================================================
    // SAFE IMPORT
    // =========================================================

    private int runImport(
            String source,
            ImportFunction function) {

        try {

            List<Job> jobs =
                    function.run();

            return jobs == null
                    ? 0
                    : jobs.size();

        } catch (Exception e) {

            System.out.println(
                    source
                            + " IMPORT FAILED: "
                            + e.getMessage()
            );

            return 0;
        }
    }

    // =========================================================
    // IMPORT FUNCTION
    // =========================================================

    @FunctionalInterface
    private interface ImportFunction {

        List<Job> run();
    }
}