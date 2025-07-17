package com.MyTime.service;

import com.MyTime.entity.Project;
import com.MyTime.entity.ProjectTask;
import com.MyTime.repository.ProjectTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProjectTaskServiceTest {

    @Mock
    private ProjectTaskRepository projectTaskRepository;

    @InjectMocks
    private ProjectTaskService projectTaskService;

    private ProjectTask projectTask1;
    private ProjectTask projectTask2;
    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        project = new Project();
        project.setProjectId(1);

        projectTask1 = new ProjectTask();
        projectTask1.setTaskId(1L);
        projectTask1.setNameTask("Task Alpha");
        projectTask1.setProject(project);

        projectTask2 = new ProjectTask();
        projectTask2.setTaskId(2L);
        projectTask2.setNameTask("Task Beta");
        projectTask2.setProject(project);
    }

    @Test
    void getAllProjectTasks() {
        when(projectTaskRepository.findAll()).thenReturn(Arrays.asList(projectTask1, projectTask2));

        List<ProjectTask> projectTasks = projectTaskService.getAllProjectTasks();

        assertNotNull(projectTasks);
        assertEquals(2, projectTasks.size());
        assertEquals("Task Alpha", projectTasks.get(0).getNameTask());
    }

    @Test
    void getProjectTaskById_Found() {
        when(projectTaskRepository.findById(1L)).thenReturn(Optional.of(projectTask1));

        ProjectTask foundProjectTask = projectTaskService.getProjectTaskById(1L);

        assertNotNull(foundProjectTask);
        assertEquals("Task Alpha", foundProjectTask.getNameTask());
    }

    @Test
    void getProjectTaskById_NotFound() {
        when(projectTaskRepository.findById(3L)).thenReturn(Optional.empty());

        ProjectTask foundProjectTask = projectTaskService.getProjectTaskById(3L);

        assertNull(foundProjectTask);
    }

    @Test
    void createProjectTask() {
        when(projectTaskRepository.save(any(ProjectTask.class))).thenReturn(projectTask1);

        ProjectTask createdProjectTask = projectTaskService.createProjectTask(projectTask1);

        assertNotNull(createdProjectTask);
        assertEquals("Task Alpha", createdProjectTask.getNameTask());
        verify(projectTaskRepository, times(1)).save(projectTask1);
    }

    @Test
    void updateProjectTask_Found() {
        ProjectTask updatedTask = new ProjectTask();
        updatedTask.setNameTask("Updated Task Alpha");

        when(projectTaskRepository.findById(1L)).thenReturn(Optional.of(projectTask1));
        when(projectTaskRepository.save(any(ProjectTask.class))).thenReturn(updatedTask);

        ProjectTask result = projectTaskService.updateProjectTask(1L, updatedTask);

        assertNotNull(result);
        assertEquals("Updated Task Alpha", result.getNameTask());
        verify(projectTaskRepository, times(1)).findById(1L);
        verify(projectTaskRepository, times(1)).save(projectTask1); // Note: The original service updates existingTask and saves it.
    }

    @Test
    void updateProjectTask_NotFound() {
        ProjectTask updatedTask = new ProjectTask();
        updatedTask.setNameTask("Updated Task Alpha");

        when(projectTaskRepository.findById(3L)).thenReturn(Optional.empty());

        ProjectTask result = projectTaskService.updateProjectTask(3L, updatedTask);

        assertNull(result);
        verify(projectTaskRepository, times(1)).findById(3L);
        verify(projectTaskRepository, never()).save(any(ProjectTask.class));
    }

    @Test
    void deleteProjectTask() {
        doNothing().when(projectTaskRepository).deleteById(1L);

        projectTaskService.deleteProjectTask(1L);

        verify(projectTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllProjectTasksByProjectId() {
        when(projectTaskRepository.findByProjectProjectId(1)).thenReturn(Arrays.asList(projectTask1, projectTask2));

        List<ProjectTask> projectTasks = projectTaskService.getAllProjectTasksByProjectId(1);

        assertNotNull(projectTasks);
        assertEquals(2, projectTasks.size());
    }

    @Test
    void findByTaskId_Found() {
        when(projectTaskRepository.findById(1L)).thenReturn(Optional.of(projectTask1));

        Optional<ProjectTask> foundProjectTask = projectTaskService.findByTaskId(1L);

        assertTrue(foundProjectTask.isPresent());
        assertEquals("Task Alpha", foundProjectTask.get().getNameTask());
    }

    @Test
    void findByTaskId_NotFound() {
        when(projectTaskRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<ProjectTask> foundProjectTask = projectTaskService.findByTaskId(3L);

        assertFalse(foundProjectTask.isPresent());
    }

    @Test
    void getAllProjectTasksByProjectIdIn() {
        List<Integer> projectIds = Arrays.asList(1, 2);
        when(projectTaskRepository.findByProjectProjectIdIn(projectIds)).thenReturn(Arrays.asList(projectTask1, projectTask2));

        List<ProjectTask> projectTasks = projectTaskService.getAllProjectTasksByProjectIdIn(projectIds);

        assertNotNull(projectTasks);
        assertEquals(2, projectTasks.size());
    }
}
