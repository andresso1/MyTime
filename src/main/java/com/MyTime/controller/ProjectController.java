package com.MyTime.controller;

import com.MyTime.dto.Message;
import com.MyTime.dto.ProjectDto;
import com.MyTime.entity.Company;
import com.MyTime.entity.Project;
import com.MyTime.security.jwt.JwtProvider;
import com.MyTime.service.CompanyService;
import com.MyTime.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
    @RequestMapping("/projects")
@CrossOrigin(origins = "*")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    CompanyService companyService;

    @Autowired
    JwtProvider jwtProvider;

    @GetMapping("/list/{id}")
    public ResponseEntity<List<Project>> listByCompanyId(@PathVariable("id")int id){
            if (!companyService.existsById(id))
            return new ResponseEntity(new Message("company not exist"), HttpStatus.NOT_FOUND);
         List<Project> list = projectService.listByCompanyId(id);
        return new ResponseEntity(list, HttpStatus.OK);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        Optional<Project> project = projectService.findById(id);
        if (project.isPresent()) {
            return ResponseEntity.ok(project.get());
        } else {
            return new ResponseEntity(new Message("Project not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDto projectDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields"), HttpStatus.BAD_REQUEST);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String jwt = jwtProvider.generateToken(authentication);

        Integer orgId = jwtProvider.getCompanyIdFromToken(jwt);

        Company company = companyService.getByCompanyId(orgId);

        Project project = new Project(projectDto.getName(),projectDto.getStartDate(), projectDto.getEndDate(),company);

        projectService.save(project);

        return new ResponseEntity(new Message("project created"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Integer id, @RequestBody ProjectDto projectDto) {
        Optional<Project> existingProject = projectService.findById(id);
        if (existingProject.isPresent()) {
            Project project = existingProject.get();
            project.setName(projectDto.getName());
            project.setStartDate(projectDto.getStartDate());
            project.setEndDate(projectDto.getEndDate());
            project = projectService.save(project);
            return ResponseEntity.ok(project);
        } else {
            return new ResponseEntity(new Message("Project not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}

