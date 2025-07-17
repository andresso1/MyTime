package com.MyTime.service;

import com.MyTime.entity.UserTask;
import com.MyTime.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTaskService {
    @Autowired
    UserTaskRepository userTaskRepository;

    public List<UserTask> getAllUserTasks() {
        return userTaskRepository.findAll();
    }

    public UserTask getUserTaskById(Long id) {
        return userTaskRepository.findById(id).orElse(null);
    }

    public UserTask createUserTask(UserTask userTask) {
        return userTaskRepository.save(userTask);
    }

    public UserTask updateUserTask(Long id, UserTask updatedUserTask) {
        UserTask existingUserTask = userTaskRepository.findById(id).orElse(null);
        if (existingUserTask != null) {
            // Actualizar los campos del UserTask con los valores de updatedUserTask
            // Puedes implementar esto según tus necesidades
            // Por ejemplo, existingUserTask.setTask(updatedUserTask.getTask());
            return userTaskRepository.save(existingUserTask);
        }
        return null;
    }

    public void deleteUserTask(Long id) {
        userTaskRepository.deleteById(id);
    }

    public boolean existsByUserId(Integer id) {
            return userTaskRepository.existsByUserId(id);
    }

    public List<UserTask> listByUserId(Integer id) {
        return userTaskRepository.findByUserId(id);
    }

    public void saveAll(List<UserTask> dataList) {
        userTaskRepository.saveAll(dataList);
    }

    public void deleteByUserId(Integer userId) {
        userTaskRepository.deleteByUserId(userId);
    }
}

