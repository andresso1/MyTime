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
@Table( name = "user_task")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_TASK_ID", unique = true, nullable = false)
    private Long userTaskId;
    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private ProjectTask task;
    @Column(name = "USER_ID")
    private Integer userId;
    private String status;
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;

    public UserTask(){

    }
    public UserTask(ProjectTask task, Integer userId) {
        this.task = task;
        this.userId = userId;
        this.status = "ACTIVE";
        this.createDate = new Date();
        this.modifiedDate = new Date();

    }
}

