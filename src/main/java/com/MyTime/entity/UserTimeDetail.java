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
@Table(name = "user_time_detail")
public class UserTimeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_TIME_DETAIL_ID", unique = true, nullable = false)
    private Long userTimeDetailId;

    private long projectId;
    private long taskId;
    private String typeId;
    private long sat;
    private long sun;
    private long mon;
    private long tus;
    private long wen;
    private long tru;
    private long fry;
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;
    @ManyToOne
    @JoinColumn(name = "USER_TIME_ID")
    private UserTime userTime;

    @Column(name = "NOTES", length = 512)
    private String notes;

    public UserTimeDetail() {
    }

    public UserTimeDetail(long projectId, long taskId, String typeId, long sat, long sun, long mon, long tus, long wen, long tru, long fry, UserTime userTime, String notes) {
        this.projectId = projectId;
        this.taskId = taskId;
        this.typeId = typeId;
        this.sat = sat;
        this.sun = sun;
        this.mon = mon;
        this.tus = tus;
        this.wen = wen;
        this.tru = tru;
        this.fry = fry;
        this.createDate = new Date();
        this.modifiedDate = new Date();
        this.userTime = userTime;
        this.notes = notes;
    }
}
