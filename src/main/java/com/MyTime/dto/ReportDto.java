package com.MyTime.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ReportDto {
    private String name;
    private String format;
    private long companyId;
    private long year;
    private long month;
    private Integer userId;

    public ReportDto(String name, long companyId, long year, long month, String format, Integer userId) {
        this.name = name;
        this.companyId = companyId;
        this.year = year;
        this.month = month;
        this.format = format;
        this.userId = userId;
    }
}
