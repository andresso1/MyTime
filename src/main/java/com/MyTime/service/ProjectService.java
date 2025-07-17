package com.MyTime.service;


import com.MyTime.entity.Project;
import com.MyTime.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project createProject(Project project) {
        // Puedes agregar validaciones o lógica de negocio aquí antes de guardar el proyecto
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project updatedProject) {
        Project existingProject = projectRepository.findById(id).orElse(null);
        if (existingProject != null) {
            // Actualizar los campos del proyecto con los valores de updatedProject
            // Puedes implementar esto según tus necesidades
            // Aquí, asumiremos que tienes métodos setter en la clase Project
            existingProject.setName(updatedProject.getName());
            existingProject.setStartDate(updatedProject.getStartDate());
            existingProject.setEndDate(updatedProject.getEndDate());
            existingProject.setCreateddate(updatedProject.getCreateddate());
            existingProject.setModifiedate(updatedProject.getModifiedate());
            existingProject.setCompany(updatedProject.getCompany());
            return projectRepository.save(existingProject);
        }
        return null;
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Integer id) {
        return projectRepository.findByProjectId(id);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> listByCompanyId(Integer companyId) {
        return projectRepository.findByCompanyCompanyId(companyId);
    }
}
