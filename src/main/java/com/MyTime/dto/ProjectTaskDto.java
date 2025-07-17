package com.MyTime.dto;

import com.MyTime.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ProjectTaskDto {
    private long taskId;
    private String nameTask;
    private Date startDate;
    private Date endDate;
    private String codeTask;
    private String status;
    private Date createDate;
    private Date modifiedDate;
    private Integer projectId;
    private Project project;
    // getters y setters
}
