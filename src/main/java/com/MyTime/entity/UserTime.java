package com.MyTime.entity;

import com.MyTime.enums.TimeStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@ToString
@Getter
@Setter
@Entity
@Table( name = "user_time")
public class UserTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_TIME_ID", unique = true, nullable = false)
    private Long userTimeId;
    private String status;
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Temporal(TemporalType.DATE)
    private Date modifiedDate;
    private long week;
    private long month;
    private long year;
    private String name;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name="NOTES", length=512)
    private String notes;

    public UserTime() {
    }

    public UserTime(User user, String name, String status, String notes, long week, long month, long year) {
        this.user = user;
        this.name = name;
        this.status = String.valueOf(TimeStatus.DRAFT);
        this.notes = notes;
        this.week = week;
        this.month = month;
        this.year = year;
        this.createDate = new Date();
        this.modifiedDate = new Date();

    }
}
