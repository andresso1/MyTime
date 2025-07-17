package com.MyTime.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserTimeDetailsDayDto {
    private long ProjectId;
    private long taskId;
    private String typeId;
    private long sat;
    private long sun;
    private long mon;
    private long tus;
    private long wen;
    private long tru;
    private long fry;

}
