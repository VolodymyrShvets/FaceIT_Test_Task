package com.faceit.task.controller;

import com.faceit.task.model.Job;
import com.faceit.task.service.api.JobsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/jobs")
public class JobsController {
    private JobsService jobsService;

    @PostMapping("/download")
    public ResponseEntity<Void> downloadJobs(@RequestParam(defaultValue = "5") int pages) {
        jobsService.downloadPages(pages);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Job>> getJobs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "createdAt,desc") String[] sort) {

        Sort sorting = sort[1].equals("asc") ? Sort.by(sort[0]).ascending() : Sort.by(sort[0]).descending();

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Job> jobPage = jobsService.findJobs(pageable);

        return ResponseEntity.ok(jobPage);
    }

    @GetMapping("/statistics")
    public ResponseEntity getCountOfJobsForAllLocations(@RequestParam String location) {
        if (location.equals("all"))
            return ResponseEntity.ok(jobsService.countJobsByAllLocations());
        else
            return ResponseEntity.ok(jobsService.countJobsByLocation(location));
    }
}
