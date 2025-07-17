package com.MyTime.dto;

import com.MyTime.entity.UserTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserTimeDetailDto {

    private UserTime userTime;

    private ArrayList<UserTimeDetailsDayDto> userTimeDetailsDayDto;


}
