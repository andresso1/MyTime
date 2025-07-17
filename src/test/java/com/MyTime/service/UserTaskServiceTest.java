package com.MyTime.service;

import com.MyTime.entity.User;
import com.MyTime.entity.UserTask;
import com.MyTime.repository.UserTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserTaskServiceTest {

    @Mock
    private UserTaskRepository userTaskRepository;

    @InjectMocks
    private UserTaskService userTaskService;

    private UserTask userTask1;
    private UserTask userTask2;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setUserId(1);

        userTask1 = new UserTask();
        userTask1.setUserTaskId(1L);
        userTask1.setUserId(user.getUserId());

        userTask2 = new UserTask();
        userTask2.setUserTaskId(2L);
        userTask2.setUserId(user.getUserId());
    }

    @Test
    void getAllUserTasks() {
        when(userTaskRepository.findAll()).thenReturn(Arrays.asList(userTask1, userTask2));

        List<UserTask> userTasks = userTaskService.getAllUserTasks();

        assertNotNull(userTasks);
        assertEquals(2, userTasks.size());
    }

    @Test
    void getUserTaskById_Found() {
        when(userTaskRepository.findById(1L)).thenReturn(Optional.of(userTask1));

        UserTask foundUserTask = userTaskService.getUserTaskById(1L);

        assertNotNull(foundUserTask);
        assertEquals(1L, foundUserTask.getUserTaskId());
    }

    @Test
    void getUserTaskById_NotFound() {
        when(userTaskRepository.findById(3L)).thenReturn(Optional.empty());

        UserTask foundUserTask = userTaskService.getUserTaskById(3L);

        assertNull(foundUserTask);
    }

    @Test
    void createUserTask() {
        when(userTaskRepository.save(any(UserTask.class))).thenReturn(userTask1);

        UserTask createdUserTask = userTaskService.createUserTask(userTask1);

        assertNotNull(createdUserTask);
        assertEquals(1L, createdUserTask.getUserTaskId());
        verify(userTaskRepository, times(1)).save(userTask1);
    }

    @Test
    void updateUserTask_Found() {
        UserTask updatedUserTask = new UserTask();
        updatedUserTask.setUserTaskId(1L);
        updatedUserTask.setUserId(user.getUserId());

        when(userTaskRepository.findById(1L)).thenReturn(Optional.of(userTask1));
        when(userTaskRepository.save(any(UserTask.class))).thenReturn(updatedUserTask);

        UserTask result = userTaskService.updateUserTask(1L, updatedUserTask);

        assertNotNull(result);
        assertEquals(1L, result.getUserTaskId());
        verify(userTaskRepository, times(1)).findById(1L);
        verify(userTaskRepository, times(1)).save(any(UserTask.class));
    }

    @Test
    void updateUserTask_NotFound() {
        UserTask updatedUserTask = new UserTask();
        updatedUserTask.setUserTaskId(3L);

        when(userTaskRepository.findById(3L)).thenReturn(Optional.empty());

        UserTask result = userTaskService.updateUserTask(3L, updatedUserTask);

        assertNull(result);
        verify(userTaskRepository, times(1)).findById(3L);
        verify(userTaskRepository, never()).save(any(UserTask.class));
    }

    @Test
    void deleteUserTask() {
        doNothing().when(userTaskRepository).deleteById(1L);

        userTaskService.deleteUserTask(1L);

        verify(userTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsByUserId_True() {
        when(userTaskRepository.existsByUserId(1)).thenReturn(true);

        boolean exists = userTaskService.existsByUserId(1);

        assertTrue(exists);
    }

    @Test
    void existsByUserId_False() {
        when(userTaskRepository.existsByUserId(1)).thenReturn(false);

        boolean exists = userTaskService.existsByUserId(1);

        assertFalse(exists);
    }

    @Test
    void listByUserId() {
        when(userTaskRepository.findByUserId(1)).thenReturn(Arrays.asList(userTask1, userTask2));

        List<UserTask> userTasks = userTaskService.listByUserId(1);

        assertNotNull(userTasks);
        assertEquals(2, userTasks.size());
    }

    @Test
    void saveAll() {
        List<UserTask> userTasks = Arrays.asList(userTask1, userTask2);
        userTaskService.saveAll(userTasks);

        verify(userTaskRepository, times(1)).saveAll(userTasks);
    }

    @Test
    void deleteByUserId() {
        doNothing().when(userTaskRepository).deleteByUserId(1);

        userTaskService.deleteByUserId(1);

        verify(userTaskRepository, times(1)).deleteByUserId(1);
    }
}
