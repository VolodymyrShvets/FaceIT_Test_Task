package com.faceit.task.service;

import com.faceit.task.model.Job;
import com.faceit.task.repository.JobsRepository;
import com.faceit.task.service.impl.JobsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.faceit.task.TestUtility.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.Silent.class)
public class JobsServiceTest {
    @Mock
    private JobsRepository jobsRepository;
    @InjectMocks
    private JobsServiceImpl jobsService;

    @Test
    public void getCountOfJobsTest() {
        List<Job> jobsList = new ArrayList<>(Arrays.asList(Job_1(), Job_2(), Job_3()));
        Page<Job> page = new PageImpl<>(jobsList);
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(jobsRepository.findAll(pageable)).thenReturn(page);

        Page<Job> actual = jobsService.findJobs(pageable);

        Assert.assertEquals(actual.getTotalElements(), jobsList.size());
    }

    @Test
    public void getSingleElementOnSecondPageTest() {
        Page<Job> page = new PageImpl<>(List.of(Job_3()));
        Pageable pageable = PageRequest.of(1, 1);

        Mockito.when(jobsRepository.findAll(pageable)).thenReturn(page);

        Page<Job> actual = jobsService.findJobs(pageable);

        Job first = actual.stream().findFirst().get();

        Assert.assertEquals(actual.getTotalElements(), 1);

        assertThat(first, allOf(
                hasProperty("slug", equalTo(Job_3().getSlug())),
                hasProperty("description", equalTo(Job_3().getDescription())),
                hasProperty("location", equalTo(Job_3().getLocation()))
        ));
    }

    @Test
    public void countJobsByAllLocationsTest() {
        List<Object[]> expected = new ArrayList<>();
        expected.add(new Object[]{"Essen", 1L});
        expected.add(new Object[]{"Munchen", 1L});
        expected.add(new Object[]{"Coburg", 1L});

        Mockito.when(jobsRepository.countJobsByLocation()).thenReturn(expected);
        List<Object[]> actual = jobsService.countJobsByAllLocations();

        Assert.assertEquals(expected.size(), actual.size());

        actual.sort(Comparator.comparing(o -> ((String) o[0])));
        expected.sort(Comparator.comparing(o -> ((String) o[0])));

        for (int i = 0; i < expected.size(); i++) {
            assertArrayEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void countJobsByLocationTest() {
        String essen = "Essen";
        String munchen = "Munchen";
        String coburg = "Coburg";

        Mockito.when(jobsRepository.countJobsByLocationIgnoreCase(essen)).thenReturn(1L);
        Mockito.when(jobsRepository.countJobsByLocationIgnoreCase(munchen)).thenReturn(1L);
        Mockito.when(jobsRepository.countJobsByLocationIgnoreCase(coburg)).thenReturn(1L);

        Assert.assertEquals(jobsService.countJobsByLocation(essen), 1);
        Assert.assertEquals(jobsService.countJobsByLocation(munchen), 1);
        Assert.assertEquals(jobsService.countJobsByLocation(coburg), 1);
    }
}
