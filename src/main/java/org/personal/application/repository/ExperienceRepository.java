package org.personal.application.repository;

import org.personal.application.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    List<Experience> findByResumeId(Integer resumeId);
}
