package com.faceit.task.controller;

import com.faceit.task.model.Job;
import com.faceit.task.service.api.JobsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.faceit.task.TestUtility.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(JobsController.class)
public class JobsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JobsService jobsService;

    @Test
    public void getJobsTest() throws Exception {
        List<Job> jobsList = new ArrayList<>(Arrays.asList(Job_1(), Job_2(), Job_3()));
        Page<Job> page = new PageImpl<>(jobsList);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        when(jobsService.findJobs(pageable)).thenReturn(page);

        mockMvc.perform(get("/jobs/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void getCountOfJobsForAllLocationsTest() throws Exception {
        List<Object[]> expected = new ArrayList<>();
        expected.add(new Object[]{"Essen", 1L});
        expected.add(new Object[]{"Munchen", 1L});
        expected.add(new Object[]{"Coburg", 1L});

        String expectedJSON = "[[\"Essen\",1],[\"Munchen\",1],[\"Coburg\",1]]";

        when(jobsService.countJobsByAllLocations()).thenReturn(expected);

        mockMvc.perform(get("/jobs/statistics?location=all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedJSON));
    }

    @Test
    public void getCountOfJobsForSingleLocationTest() throws Exception {
        String location = Job_3().getLocation();

        when(jobsService.countJobsByLocation(location)).thenReturn(1L);

        mockMvc.perform(get("/jobs/statistics?location=" + location))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1L));
    }
}
