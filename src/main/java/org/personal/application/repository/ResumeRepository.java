package org.personal.application.repository;

import org.personal.application.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    List<Resume> findByUserId(Integer userId);

    @Query("SELECT r FROM Resume r WHERE r.title LIKE :keyword OR r.summary LIKE :keyword")
    List<Resume> search(@Param("keyword") String keyword);

}
