package com.MyTime.repository;

import com.MyTime.entity.Project;
import com.MyTime.entity.ProjectTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectTaskRepositoryTest {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Project project1;
    private Project project2;
    private ProjectTask projectTask1;
    private ProjectTask projectTask2;
    private ProjectTask projectTask3;

    @BeforeEach
    void setUp() {
        project1 = new Project();
        project1.setName("Project Alpha");
        entityManager.persist(project1);

        project2 = new Project();
        project2.setName("Project Beta");
        entityManager.persist(project2);

        projectTask1 = new ProjectTask();
        projectTask1.setNameTask("Task 1");
        projectTask1.setProject(project1);

        projectTask2 = new ProjectTask();
        projectTask2.setNameTask("Task 2");
        projectTask2.setProject(project1);

        projectTask3 = new ProjectTask();
        projectTask3.setNameTask("Task 3");
        projectTask3.setProject(project2);

        entityManager.persist(projectTask1);
        entityManager.persist(projectTask2);
        entityManager.persist(projectTask3);
        entityManager.flush();
    }

    @Test
    void findByProjectProjectId() {
        List<ProjectTask> tasks = projectTaskRepository.findByProjectProjectId(project1.getProjectId().intValue());

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getNameTask().equals("Task 1")));
        assertTrue(tasks.stream().anyMatch(t -> t.getNameTask().equals("Task 2")));
    }

    @Test
    void findByProjectProjectIdIn() {
        List<Integer> projectIds = Arrays.asList(project1.getProjectId().intValue(), project2.getProjectId().intValue());
        List<ProjectTask> tasks = projectTaskRepository.findByProjectProjectIdIn(projectIds);

        assertNotNull(tasks);
        assertEquals(3, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getNameTask().equals("Task 1")));
        assertTrue(tasks.stream().anyMatch(t -> t.getNameTask().equals("Task 2")));
        assertTrue(tasks.stream().anyMatch(t -> t.getNameTask().equals("Task 3")));
    }

    @Test
    void saveProjectTask() {
        ProjectTask newProjectTask = new ProjectTask();
        newProjectTask.setNameTask("New Task");
        newProjectTask.setProject(project1);

        ProjectTask savedTask = projectTaskRepository.save(newProjectTask);

        assertNotNull(savedTask.getTaskId());
        assertEquals("New Task", savedTask.getNameTask());

        Optional<ProjectTask> found = projectTaskRepository.findById(savedTask.getTaskId());
        assertTrue(found.isPresent());
        assertEquals("New Task", found.get().getNameTask());
    }

    @Test
    void findById() {
        Optional<ProjectTask> foundTask = projectTaskRepository.findById(projectTask1.getTaskId());

        assertTrue(foundTask.isPresent());
        assertEquals("Task 1", foundTask.get().getNameTask());
    }

    @Test
    void findAllProjectTasks() {
        List<ProjectTask> tasks = projectTaskRepository.findAll();
        assertNotNull(tasks);
        assertEquals(3, tasks.size());
    }

    @Test
    void deleteProjectTask() {
        projectTaskRepository.deleteById(projectTask1.getTaskId());

        Optional<ProjectTask> found = projectTaskRepository.findById(projectTask1.getTaskId());
        assertFalse(found.isPresent());
    }
}
