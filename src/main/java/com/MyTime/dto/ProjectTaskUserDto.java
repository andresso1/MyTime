package com.MyTime.dto;

import com.MyTime.entity.ProjectTask;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ProjectTaskUserDto {
    private List<ProjectTask> projectTasks;
    private Integer userId;



}
