package com.MyTime.dto;

import com.MyTime.entity.ProjectTask;
import com.MyTime.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserTaskDto {
    private Long userTaskId;
    private ProjectTask askId;
    private User userId;
    private String status;
    private Date createDate;
    private Date modifiedDate;
}
