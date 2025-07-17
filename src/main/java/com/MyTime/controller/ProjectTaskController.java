package com.MyTime.controller;

import com.MyTime.dto.Message;
import com.MyTime.dto.ProjectTaskDto;
import com.MyTime.entity.Project;
import com.MyTime.entity.ProjectTask;
import com.MyTime.service.ProjectService;
import com.MyTime.service.ProjectTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/project-tasks")
@CrossOrigin(origins = "*")
public class ProjectTaskController {
    @Autowired
    ProjectTaskService projectTaskService;

    @Autowired
    ProjectService projectService;

    @GetMapping("/listAllTask/{id}")
    public ResponseEntity<List<ProjectTask>> getAllProjectTasksByCompany(@PathVariable("id") Integer id) {

        List<Project> projectList = projectService.listByCompanyId(id);
        List<Integer> ids = new ArrayList<>();

        projectList.forEach(project -> {
            ids.add(project.getProjectId());
        });

        List<ProjectTask> list = projectTaskService.getAllProjectTasksByProjectIdIn(ids);

        return new ResponseEntity(list, HttpStatus.OK);
    }


    @GetMapping("/list/{id}")
    public ResponseEntity<List<ProjectTask>> getAllProjectTasksByProjectId(@PathVariable("id") Integer id) {
        Optional<Project> project = projectService.findById(id);
        if (!project.isPresent()) {
            return new ResponseEntity(new Message("project not exist"), HttpStatus.NOT_FOUND);
        } else {
            List<ProjectTask> list = projectTaskService.getAllProjectTasksByProjectId(id);
            return new ResponseEntity(list, HttpStatus.OK);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getProjectTaskById(@PathVariable Long id) {
        ProjectTask task = projectTaskService.getProjectTaskById(id);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("Task not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createProjectTask(@Valid @RequestBody ProjectTaskDto projectTaskDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields"), HttpStatus.BAD_REQUEST);
        Optional<Project> project = projectService.findById(projectTaskDto.getProjectId());
        if (!project.isPresent())
            return new ResponseEntity(new Message("project not exist"), HttpStatus.NOT_FOUND);
        ProjectTask projectTask = new ProjectTask(projectTaskDto.getNameTask(), projectTaskDto.getStartDate(), projectTaskDto.getEndDate(), projectTaskDto.getCodeTask(), "ACTIVE", project.get());
        projectTaskService.createProjectTask(projectTask);
        return new ResponseEntity(new Message("task created"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProjectTask(@PathVariable long id, @RequestBody ProjectTask updatedTask) {

        Optional<ProjectTask> existingProject = projectTaskService.findByTaskId(id);
        if (existingProject.isPresent()) {
            ProjectTask projectTask = existingProject.get();
            projectTask.setNameTask(updatedTask.getNameTask());
            projectTask.setCodeTask(updatedTask.getCodeTask());
            projectTask.setStartDate(updatedTask.getStartDate());
            projectTask.setEndDate(updatedTask.getEndDate());
            ProjectTask task = projectTaskService.updateProjectTask(id, updatedTask);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("Task not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable long id) {
        projectTaskService.deleteProjectTask(id);
        return new ResponseEntity(new Message("task deleted"), HttpStatus.OK);
    }
}

