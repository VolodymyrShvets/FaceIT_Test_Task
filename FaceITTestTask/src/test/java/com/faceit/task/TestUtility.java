package com.faceit.task;

import com.faceit.task.model.Job;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtility {
    public static Job Job_1() {
        List<String> tags = new ArrayList<>();
        tags.add("Management");
        tags.add("other");

        List<String> job_types = new ArrayList<>();
        job_types.add("full time");
        job_types.add("intern/student");
        job_types.add("student");

        Job job = new Job();

        job.setSlug("praktikant-beratung-in-munchen-37535");
        job.setCompany_name("Oberender AG");
        job.setTitle("Praktikant Beratung (m/w/d) in Munchen");
        job.setDescription("some very long job description");
        job.setRemote(true);
        job.setUrl("https://www.arbeitnow.com/jobs/companies/oberender-ag/praktikant-beratung-in-munchen-37535");
        job.setTags(tags);
        job.setJob_types(job_types);
        job.setLocation("Munchen");
        job.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        return job;
    }

    public static Job Job_2() {
        List<String> tags = new ArrayList<>();
        tags.add("Gambling and casinos");
        tags.add("sales");

        List<String> job_types = new ArrayList<>();
        job_types.add("Associate");
        job_types.add("part time");

        Job job = new Job();

        job.setSlug("shopmitarbeiter-in-teilzeit-coburg-446870");
        job.setCompany_name("Tipico Shop Agency North");
        job.setTitle("Shopmitarbeiter in Teilzeit (m/w/d)");
        job.setDescription("another very long job description");
        job.setRemote(false);
        job.setUrl("https://www.arbeitnow.com/jobs/companies/tipico-shop-agency-north/shopmitarbeiter-in-teilzeit-coburg-446870");
        job.setTags(tags);
        job.setJob_types(job_types);
        job.setLocation("Coburg");
        job.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(5)));

        return job;
    }

    public static Job Job_3() {
        List<String> tags = new ArrayList<>();
        tags.add("Accounts Receivable");

        Job job = new Job();

        job.setSlug("buchhalter-fur-einen-getrankegrosshandel-essen-392891");
        job.setCompany_name("JobAtlas");
        job.setTitle("Buchhalter (m/w/d) fur einen Getrankegrobhandel");
        job.setDescription("yet another long job description");
        job.setRemote(true);
        job.setUrl("https://www.arbeitnow.com/jobs/companies/jobatlas/buchhalter-fur-einen-getrankegrosshandel-essen-392891");
        job.setTags(tags);
        job.setJob_types(new ArrayList<>());
        job.setLocation("Essen");
        job.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(4)));

        return job;
    }
}
