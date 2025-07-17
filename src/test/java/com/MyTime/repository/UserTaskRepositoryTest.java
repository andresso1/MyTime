package com.MyTime.repository;

import com.MyTime.entity.Project;
import com.MyTime.entity.ProjectTask;
import com.MyTime.entity.User;
import com.MyTime.entity.UserTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTaskRepositoryTest {

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private Project project1;
    private ProjectTask projectTask1;
    private UserTask userTask1;
    private UserTask userTask2;

    @BeforeEach
    void setUp() {
        user1 = new User("User One", "userone", "userone@example.com", "password", "ACTIVE");
        user2 = new User("User Two", "usertwo", "usertwo@example.com", "password", "ACTIVE");
        entityManager.persist(user1);
        entityManager.persist(user2);

        project1 = new Project();
        project1.setName("Project Alpha");
        entityManager.persist(project1);

        projectTask1 = new ProjectTask();
        projectTask1.setNameTask("Task Alpha");
        projectTask1.setProject(project1);
        entityManager.persist(projectTask1);

        userTask1 = new UserTask();
        userTask1.setUserId(user1.getUserId());
        userTask1.setTask(projectTask1);
        entityManager.persist(userTask1);

        userTask2 = new UserTask();
        userTask2.setUserId(user1.getUserId());
        userTask2.setTask(projectTask1);
        entityManager.persist(userTask2);

        entityManager.flush();
    }

    @Test
    void existsByUserId_True() {
        boolean exists = userTaskRepository.existsByUserId(user1.getUserId());
        assertTrue(exists);
    }

    @Test
    void existsByUserId_False() {
        boolean exists = userTaskRepository.existsByUserId(user2.getUserId()); // user2 has no tasks assigned
        assertFalse(exists);
    }

    @Test
    void findByUserId() {
        List<UserTask> userTasks = userTaskRepository.findByUserId(user1.getUserId());
        assertNotNull(userTasks);
        assertEquals(2, userTasks.size());
        assertTrue(userTasks.stream().anyMatch(ut -> ut.getUserId().equals(user1.getUserId())));
    }

    @Test
    void deleteByUserId() {
        userTaskRepository.deleteByUserId(user1.getUserId());
        entityManager.flush(); // Ensure changes are committed

        List<UserTask> remainingTasks = userTaskRepository.findByUserId(user1.getUserId());
        assertTrue(remainingTasks.isEmpty());
    }

    @Test
    void saveUserTask() {
        UserTask newUserTask = new UserTask();
        newUserTask.setUserId(user2.getUserId());
        newUserTask.setTask(projectTask1);

        UserTask savedUserTask = userTaskRepository.save(newUserTask);

        assertNotNull(savedUserTask.getUserTaskId());
        assertEquals(user2.getUserId(), savedUserTask.getUserId());

        Optional<UserTask> found = userTaskRepository.findById(savedUserTask.getUserTaskId());
        assertTrue(found.isPresent());
        assertEquals(user2.getUserId(), found.get().getUserId());
    }

    @Test
    void findById() {
        Optional<UserTask> foundTask = userTaskRepository.findById(userTask1.getUserTaskId());
        assertTrue(foundTask.isPresent());
        assertEquals(userTask1.getUserTaskId(), foundTask.get().getUserTaskId());
    }

    @Test
    void findAllUserTasks() {
        List<UserTask> userTasks = userTaskRepository.findAll();
        assertNotNull(userTasks);
        assertEquals(2, userTasks.size());
    }

    @Test
    void deleteUserTask() {
        userTaskRepository.deleteById(userTask1.getUserTaskId());
        entityManager.flush();

        Optional<UserTask> found = userTaskRepository.findById(userTask1.getUserTaskId());
        assertFalse(found.isPresent());
    }
}
