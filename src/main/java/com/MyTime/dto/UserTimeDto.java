package com.MyTime.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserTimeDto {
    private Long userTimeId;
    private Integer userId;
    private String status;
    private Date createDate;
    private Date modifiedDate;
    private long week;
    private long month;
    private long year;
    private String name;
    private String notes;

}
