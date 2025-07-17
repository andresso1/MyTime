package com.MyTime.service;

import com.MyTime.entity.ProjectTask;
import com.MyTime.repository.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectTaskService {
    @Autowired
    ProjectTaskRepository projectTaskRepository;

    public List<ProjectTask> getAllProjectTasks() {
        return projectTaskRepository.findAll();
    }

    public ProjectTask getProjectTaskById(Long id) {
        return projectTaskRepository.findById(id).orElse(null);
    }

    public ProjectTask createProjectTask(ProjectTask projectTask) {
        return projectTaskRepository.save(projectTask);
    }

    public ProjectTask updateProjectTask(long id, ProjectTask updatedTask) {
        ProjectTask existingTask = (ProjectTask) projectTaskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            // Actualizar los campos de la tarea con los valores de updatedTask
            // Puedes implementar esto según tus necesidades
            // Por ejemplo, existingTask.setNameTask(updatedTask.getNameTask());
            return projectTaskRepository.save(existingTask);
        }
        return null;
    }

    public void deleteProjectTask(Long id) {
        projectTaskRepository.deleteById(id);
    }

    public List<ProjectTask> getAllProjectTasksByProjectId(Integer id) {
        return projectTaskRepository.findByProjectProjectId(id);
    }

    public Optional<ProjectTask> findByTaskId(long id) {
        return projectTaskRepository.findById(id);
    }

    public List<ProjectTask> getAllProjectTasksByProjectIdIn(List<Integer> listIds) {
        return projectTaskRepository.findByProjectProjectIdIn(listIds);
    }
}
