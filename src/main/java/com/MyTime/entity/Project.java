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
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID", unique = true, nullable = false)
    private Integer projectId;
    private String name;
    private Date startDate;
    private Date endDate;
    @Temporal(TemporalType.DATE)
    private Date createddate;
    @Temporal(TemporalType.DATE)
    private Date modifiedate;
    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    public Project() {
    }

    public Project(String name, Date startDate, Date endDate, Company company) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.company = company;
        this.createddate = new Date();
        this.modifiedate = new Date();
    }
}
