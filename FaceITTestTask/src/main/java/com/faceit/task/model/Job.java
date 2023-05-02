package com.faceit.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@ToString
@Component
@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String slug;

    private String company_name;

    private String title;

    @Column(length = 15000)
    private String description;

    private boolean remote;

    private String url;

    @ElementCollection
    private List<String> tags;

    @ElementCollection
    private List<String> job_types;

    private String location;

    @JsonProperty("created_at")
    private Timestamp createdAt;
}
