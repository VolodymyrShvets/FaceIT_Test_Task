package com.faceit.task.repository;

import com.faceit.task.model.Job;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.faceit.task.TestUtility.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobsRepositoryTest {
    @Autowired
    private JobsRepository repository;

    @Before
    public void setUp() throws Exception {
        List<Job> jobsList = new ArrayList<>(Arrays.asList(Job_1(), Job_2(), Job_3()));
        List<Job> saved = repository.saveAll(jobsList);
        System.out.println("saved" + saved.size());
    }

    @Test
    public void existsBySlugTest() {
        Assert.assertTrue(repository.existsBySlug(Job_1().getSlug()));
        Assert.assertTrue(repository.existsBySlug(Job_2().getSlug()));
        Assert.assertTrue(repository.existsBySlug(Job_3().getSlug()));
    }

    @Test
    public void countJobsByLocationIgnoreCaseTest() {
        String essen = "Essen";
        String munchen = "Munchen";
        String coburg = "Coburg";

        Assert.assertEquals(repository.countJobsByLocationIgnoreCase(essen), 1);
        Assert.assertEquals(repository.countJobsByLocationIgnoreCase(munchen), 1);
        Assert.assertEquals(repository.countJobsByLocationIgnoreCase(coburg), 1);
    }

    @Test
    public void countJobsByLocationTest() {
        List<Object[]> expected = new ArrayList<>();
        expected.add(new Object[]{"Essen", 1L});
        expected.add(new Object[]{"Munchen", 1L});
        expected.add(new Object[]{"Coburg", 1L});

        List<Object[]> actual = repository.countJobsByLocation();

        Assert.assertEquals(expected.size(), actual.size());

        actual.sort(Comparator.comparing(o -> ((String) o[0])));
        expected.sort(Comparator.comparing(o -> ((String) o[0])));

        for (int i = 0; i < expected.size(); i++) {
            assertArrayEquals(expected.get(i), actual.get(i));
        }
    }
}
