package com.faceit.task.service.api;

import com.faceit.task.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobsService {
    void downloadPages(int pagesCount);

    void saveJobs(List<Job> jobs);

    Page<Job> findJobs(Pageable pageable);

    List<Object[]> countJobsByAllLocations();

    long countJobsByLocation(String location);

    void fetchAndStoreData();
}
