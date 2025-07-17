package com.MyTime.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SetStatusTimeDto {
    private Long userTimeId;
    private String status;
    private String notes;

}
