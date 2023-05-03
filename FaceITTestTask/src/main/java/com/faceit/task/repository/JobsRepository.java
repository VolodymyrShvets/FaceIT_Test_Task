package com.faceit.task.repository;

import com.faceit.task.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobsRepository extends JpaRepository<Job, Long> {
    boolean existsBySlug(String slug);

    @Query("SELECT j.location, COUNT(*) FROM Job j GROUP BY j.location")
    List<Object[]> countJobsByLocation();

    @Query("SELECT COUNT(*) FROM Job j WHERE LOWER(j.location) = LOWER(:location)")
    long countJobsByLocationIgnoreCase(@Param("location") String location);
}
