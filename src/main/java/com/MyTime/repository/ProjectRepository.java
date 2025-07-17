package com.MyTime.repository;

import com.MyTime.entity.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCompanyCompanyId(Integer companyId);

    Optional<Project> findByProjectId(Integer projectId);

}
