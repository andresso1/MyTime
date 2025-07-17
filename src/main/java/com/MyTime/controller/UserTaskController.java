package com.MyTime.controller;

import com.MyTime.dto.Message;
import com.MyTime.dto.ProjectTaskUserDto;
import com.MyTime.entity.ProjectTask;
import com.MyTime.entity.UserTask;
import com.MyTime.service.ProjectTaskService;
import com.MyTime.service.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user-tasks")
@CrossOrigin(origins = "*")
public class UserTaskController {
    @Autowired
    UserTaskService userTaskService;
    @Autowired
    ProjectTaskService projectTaskService;

    @GetMapping("/list/{id}")
    public ResponseEntity<List<UserTask>> listByUserId(@PathVariable("id")Integer id){
        if(!userTaskService.existsByUserId(id))
            return new ResponseEntity(new Message("Not found Data for user"), HttpStatus.NOT_FOUND);
        List<UserTask> list = userTaskService.listByUserId(id);
        return new ResponseEntity(list, HttpStatus.OK);
    }
/*
    @GetMapping("/{id}")
    public ResponseEntity<UserTask> getUserTaskById(@PathVariable Long id) {
        UserTask userTask = userTaskService.getUserTaskById(id);
        if (userTask != null) {
            return ResponseEntity.ok(userTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createUserTask(@RequestBody ProjectTaskUserDto projectTasksDto) {
        List<UserTask> dataList = new ArrayList<>();
        for (ProjectTask task : projectTasksDto.getProjectTasks()) {
            ProjectTask taskAdd = projectTaskService.getProjectTaskById(task.getTaskId());
            UserTask userTask = new UserTask(taskAdd,projectTasksDto.getUserId());
            dataList.add(userTask);
        }
        userTaskService.deleteByUserId(projectTasksDto.getUserId());
        userTaskService.saveAll(dataList);

        return new ResponseEntity(new Message("task created"), HttpStatus.OK);
    }
/*
    @PutMapping("/{id}")
    public ResponseEntity<UserTask> updateUserTask(@PathVariable Long id, @RequestBody UserTask updatedUserTask) {
        UserTask userTask = userTaskService.updateUserTask(id, updatedUserTask);
        if (userTask != null) {
            return ResponseEntity.ok(userTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserTask(@PathVariable Long id) {
        userTaskService.deleteUserTask(id);
        return ResponseEntity.noContent().build();
    }*/
}
