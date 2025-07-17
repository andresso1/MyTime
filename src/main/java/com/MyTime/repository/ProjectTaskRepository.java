package com.MyTime.repository;

import com.MyTime.entity.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
    List<ProjectTask> findByProjectProjectId(Integer id);

    List<ProjectTask> findByProjectProjectIdIn(List<Integer> listIds);
}
