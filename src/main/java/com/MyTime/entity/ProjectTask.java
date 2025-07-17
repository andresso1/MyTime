package com.MyTime.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@ToString
@Getter
@Setter
@Entity

@Table(name = "project_task")
public class ProjectTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TASK_ID", unique = true, nullable = false)
    private long taskId;
    private String nameTask;
    private Date startDate;
    private Date endDate;
    private String codeTask;
    private String status;
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    public ProjectTask(){

    }
    public ProjectTask(String nameTask, Date startDate, Date endDate, String codeTask, String status, Project project) {
        this.nameTask = nameTask;
        this.startDate = startDate;
        this.endDate = endDate;
        this.codeTask = codeTask;
        this.status = status;
        this.createDate = new Date();
        this.modifiedDate = new Date();
        this.project = project;
    }

    // getters y setters
}
