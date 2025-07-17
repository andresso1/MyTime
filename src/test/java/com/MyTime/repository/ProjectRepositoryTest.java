package com.MyTime.repository;

import com.MyTime.entity.Company;
import com.MyTime.entity.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Company company1;
    private Company company2;
    private Project project1;
    private Project project2;

    @BeforeEach
    void setUp() {
        company1 = new Company("Company A", "Address A", "111", "NIT A");
        company2 = new Company("Company B", "Address B", "222", "NIT B");
        entityManager.persist(company1);
        entityManager.persist(company2);

        project1 = new Project();
        project1.setName("Project Alpha");
        project1.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        project1.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
        project1.setCompany(company1);

        project2 = new Project();
        project2.setName("Project Beta");
        project2.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        project2.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(20)));
        project2.setCompany(company1);

        entityManager.persist(project1);
        entityManager.persist(project2);
        entityManager.flush();
    }

    @Test
    void findByCompanyCompanyId() {
        List<Project> projects = projectRepository.findByCompanyCompanyId(company1.getCompanyId());

        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertTrue(projects.stream().anyMatch(p -> p.getName().equals("Project Alpha")));
        assertTrue(projects.stream().anyMatch(p -> p.getName().equals("Project Beta")));
    }

    @Test
    void findByProjectId_Found() {
        Optional<Project> foundProject = projectRepository.findByProjectId(project1.getProjectId());

        assertTrue(foundProject.isPresent());
        assertEquals("Project Alpha", foundProject.get().getName());
    }

    @Test
    void findByProjectId_NotFound() {
        Optional<Project> foundProject = projectRepository.findByProjectId(999);

        assertFalse(foundProject.isPresent());
    }

    @Test
    void saveProject() {
        Project newProject = new Project();
        newProject.setName("Project Gamma");
        newProject.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        newProject.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(30)));
        newProject.setCompany(company2);

        Project savedProject = projectRepository.save(newProject);

        assertNotNull(savedProject.getProjectId());
        assertEquals("Project Gamma", savedProject.getName());

        Optional<Project> found = projectRepository.findById(savedProject.getProjectId().longValue());
        assertTrue(found.isPresent());
        assertEquals("Project Gamma", found.get().getName());
    }

    @Test
    void deleteProject() {
        projectRepository.deleteById(project1.getProjectId().longValue());

        Optional<Project> found = projectRepository.findById(project1.getProjectId().longValue());
        assertFalse(found.isPresent());
    }

    @Test
    void findAllProjects() {
        List<Project> projects = projectRepository.findAll();
        assertNotNull(projects);
        assertEquals(2, projects.size());
    }
}
