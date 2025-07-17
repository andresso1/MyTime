package com.MyTime.controller;

import com.MyTime.dto.ProjectTaskUserDto;
import com.MyTime.entity.ProjectTask;
import com.MyTime.entity.UserTask;
import com.MyTime.service.ProjectTaskService;
import com.MyTime.service.UserTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserTaskControllerTest {

    @Mock
    private UserTaskService userTaskService;
    @Mock
    private ProjectTaskService projectTaskService;

    @InjectMocks
    private UserTaskController userTaskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void listByUserId() {
        when(userTaskService.existsByUserId(1)).thenReturn(true);
        when(userTaskService.listByUserId(1)).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserTask>> response = userTaskController.listByUserId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createUserTask() {
        ProjectTaskUserDto projectTaskUserDto = new ProjectTaskUserDto();
        projectTaskUserDto.setUserId(1);
        projectTaskUserDto.setProjectTasks(Collections.emptyList());

        when(projectTaskService.getProjectTaskById(any())).thenReturn(new ProjectTask());

        ResponseEntity<?> response = userTaskController.createUserTask(projectTaskUserDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
