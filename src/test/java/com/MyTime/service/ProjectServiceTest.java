package com.MyTime.service;

import com.MyTime.entity.Company;
import com.MyTime.entity.Project;
import com.MyTime.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project1;
    private Project project2;
    private Company company;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        company = new Company("Test Company", "Address", "12345", "NIT");
        company.setCompanyId(1);

        project1 = new Project();
        project1.setProjectId(1);
        project1.setName("Project Alpha");
        project1.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        project1.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
        project1.setCompany(company);

        project2 = new Project();
        project2.setProjectId(2);
        project2.setName("Project Beta");
        project2.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        project2.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(20)));
        project2.setCompany(company);
    }

    @Test
    void getAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertEquals("Project Alpha", projects.get(0).getName());
    }

    @Test
    void getProjectById_Found() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project1));

        Project foundProject = projectService.getProjectById(1L);

        assertNotNull(foundProject);
        assertEquals("Project Alpha", foundProject.getName());
    }

    @Test
    void getProjectById_NotFound() {
        when(projectRepository.findById(3L)).thenReturn(Optional.empty());

        Project foundProject = projectService.getProjectById(3L);

        assertNull(foundProject);
    }

    @Test
    void createProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project1);

        Project createdProject = projectService.createProject(project1);

        assertNotNull(createdProject);
        assertEquals("Project Alpha", createdProject.getName());
        verify(projectRepository, times(1)).save(project1);
    }

    @Test
    void updateProject_Found() {
        Project updatedProject = new Project();
        updatedProject.setName("Updated Project Alpha");
        updatedProject.setStartDate(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        updatedProject.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(11)));
        updatedProject.setCompany(company);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project1));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        Project result = projectService.updateProject(1L, updatedProject);

        assertNotNull(result);
        assertEquals("Updated Project Alpha", result.getName());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(project1);
    }

    @Test
    void updateProject_NotFound() {
        Project updatedProject = new Project();
        updatedProject.setName("Updated Project Alpha");

        when(projectRepository.findById(3L)).thenReturn(Optional.empty());

        Project result = projectService.updateProject(3L, updatedProject);

        assertNull(result);
        verify(projectRepository, times(1)).findById(3L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void deleteProject() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void findAll_ReturnsAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.findAll();

        assertNotNull(projects);
        assertEquals(2, projects.size());
    }

    @Test
    void findById_Found() {
        when(projectRepository.findByProjectId(1)).thenReturn(Optional.of(project1));

        Optional<Project> foundProject = projectService.findById(1);

        assertTrue(foundProject.isPresent());
        assertEquals("Project Alpha", foundProject.get().getName());
    }

    @Test
    void findById_NotFound() {
        when(projectRepository.findByProjectId(3)).thenReturn(Optional.empty());

        Optional<Project> foundProject = projectService.findById(3);

        assertFalse(foundProject.isPresent());
    }

    @Test
    void save_Project() {
        when(projectRepository.save(any(Project.class))).thenReturn(project1);

        Project savedProject = projectService.save(project1);

        assertNotNull(savedProject);
        assertEquals("Project Alpha", savedProject.getName());
        verify(projectRepository, times(1)).save(project1);
    }

    @Test
    void listByCompanyId() {
        when(projectRepository.findByCompanyCompanyId(1)).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.listByCompanyId(1);

        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertEquals("Project Alpha", projects.get(0).getName());
    }
}
