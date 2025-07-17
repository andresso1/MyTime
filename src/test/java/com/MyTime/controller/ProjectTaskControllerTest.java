package com.MyTime.controller;

import com.MyTime.dto.ProjectTaskDto;
import com.MyTime.entity.Project;
import com.MyTime.entity.ProjectTask;
import com.MyTime.service.ProjectService;
import com.MyTime.service.ProjectTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;

import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectTaskControllerTest {

    @Mock
    private ProjectTaskService projectTaskService;
    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectTaskController projectTaskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllProjectTasksByCompany() {
        when(projectService.listByCompanyId(1)).thenReturn(Collections.emptyList());
        when(projectTaskService.getAllProjectTasksByProjectIdIn(Collections.emptyList())).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = projectTaskController.getAllProjectTasksByCompany(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllProjectTasksByProjectId() {
        Project project = new Project();
        project.setProjectId(1);

        when(projectService.findById(1)).thenReturn(Optional.of(project));
        when(projectTaskService.getAllProjectTasksByProjectId(1)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = projectTaskController.getAllProjectTasksByProjectId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getProjectTaskById() {
        ProjectTask projectTask = new ProjectTask();
        projectTask.setTaskId(1L);

        when(projectTaskService.getProjectTaskById(1L)).thenReturn(projectTask);

        ResponseEntity<?> response = projectTaskController.getProjectTaskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createProjectTask() {
        ProjectTaskDto projectTaskDto = new ProjectTaskDto();
        projectTaskDto.setProjectId(1);

        Project project = new Project();
        project.setProjectId(1);

        when(projectService.findById(1)).thenReturn(Optional.of(project));

        ResponseEntity<?> response = projectTaskController.createProjectTask(projectTaskDto, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateProjectTask() {
        ProjectTask updatedTask = new ProjectTask();
        updatedTask.setTaskId(1L);

        ProjectTask projectTask = new ProjectTask();
        projectTask.setTaskId(1L);

        when(projectTaskService.findByTaskId(1L)).thenReturn(Optional.of(projectTask));
        when(projectTaskService.updateProjectTask(1L, updatedTask)).thenReturn(updatedTask);

        ResponseEntity<?> response = projectTaskController.updateProjectTask(1L, updatedTask);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteProjectTask() {
        ResponseEntity<?> response = projectTaskController.deleteProjectTask(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
