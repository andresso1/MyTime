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
public class ProjectDto {
    private Long projectId;
    private String name;
    private Date startDate;
    private Date endDate;
    private Date createddate;
    private Date modifiedate;
    private Integer companyId;

}
