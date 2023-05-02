package com.faceit.task.service.impl;

import com.faceit.task.model.Job;
import com.faceit.task.repository.JobsRepository;
import com.faceit.task.service.api.JobsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class JobsServiceImpl implements JobsService {
    private RestTemplate restTemplate;
    private JobsRepository jobsRepository;
    private Environment environment;
    private ObjectMapper objectMapper;

    private static Timestamp lastTimeChecked = new Timestamp(0);

    @Override
    public void downloadPages(int pagesCount) {
        log.info("Started parsing for {} first pages", pagesCount);

        for (int i = 1; i <= pagesCount; i++) {
            StringBuilder url = new StringBuilder(Objects.requireNonNull(environment.getProperty("connection-url")));
            url.append("?").append("page=").append(i);

            log.info("For page {}", i);
            log.info("URL: {}", url);

            String response = restTemplate.getForObject(url.toString(), String.class);

            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode dataNode = rootNode.get("data");

                List<Job> jobsList = new ArrayList<>();

                for (JsonNode jobNode : dataNode) {
                    Job job = objectMapper.treeToValue(jobNode, Job.class);

                    if (job.getCreatedAt().compareTo(lastTimeChecked) > 0)
                        jobsList.add(job);
                }

                log.info("Page: {}, Jobs: {}", i, jobsList.size());
                if (jobsList.size() > 0)
                    saveJobs(jobsList);

            } catch (JsonProcessingException ex) {
                log.error("An Error occurred: {}", ex.toString());
            }
        }

        lastTimeChecked = new Timestamp(System.currentTimeMillis());
        log.info("Parsing and saving finished successfully for {} first pages", pagesCount);
    }

    @Override
    public void saveJobs(List<Job> jobs) {
        log.info("Saving jobsList into database");

        jobsRepository.saveAll(jobs);

        log.info("Saving finished successfully");
    }

    @Override
    public Page<Job> findJobs(Pageable pageable) {
        log.info("Searching jobs for page {} with size {} and sort {}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        return jobsRepository.findAll(pageable);
    }

    @Override
    public List<Object[]> countJobsByAllLocations() {
        log.info("Counting number of jobs for each location");

        return jobsRepository.countJobsByLocation();
    }

    @Override
    public long countJobsByLocation(String location) {
        log.info("Counting number of jobs for {} location", location);

        return jobsRepository.countJobsByLocationIgnoreCase(location);
    }

    @Scheduled(fixedDelay = 3600000)
    @Override
    public void fetchAndStoreData() {
        log.info("Started scheduled new data fetching");

        downloadPages(2);

        log.info("Scheduled data fetching finished");
    }
}
