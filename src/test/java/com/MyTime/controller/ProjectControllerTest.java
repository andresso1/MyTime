package com.MyTime.controller;

import com.MyTime.dto.ProjectDto;
import com.MyTime.entity.Company;
import com.MyTime.entity.Project;
import com.MyTime.security.jwt.JwtProvider;
import com.MyTime.service.CompanyService;
import com.MyTime.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private CompanyService companyService;
    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void listByCompanyId() {
        when(companyService.existsById(1)).thenReturn(true);
        when(projectService.listByCompanyId(1)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = projectController.listByCompanyId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getProjectById() {
        Project project = new Project();
        project.setProjectId(1);

        when(projectService.findById(1)).thenReturn(Optional.of(project));

        ResponseEntity<Project> response = projectController.getProjectById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createProject() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Test Project");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        when(jwtProvider.generateToken(authentication)).thenReturn("testToken");
        when(jwtProvider.getCompanyIdFromToken("testToken")).thenReturn(1);
        when(companyService.getByCompanyId(1)).thenReturn(new Company());

        ResponseEntity<?> response = projectController.createProject(projectDto, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateProject() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Test Project");

        Project project = new Project();
        project.setProjectId(1);

        when(projectService.findById(1)).thenReturn(Optional.of(project));
        when(projectService.save(any())).thenReturn(project);

        ResponseEntity<?> response = projectController.updateProject(1, projectDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteProject() {
        ResponseEntity<Void> response = projectController.deleteProject(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
