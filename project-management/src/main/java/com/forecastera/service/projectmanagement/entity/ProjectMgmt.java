package com.forecastera.service.projectmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

/*
 * @Author Uttam Kachhad
 * @Create 17-05-2023
 * @Description
 */
@Entity
@Table(name = "project_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMgmt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "owner")
    private Integer owner;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "progress")
    private String progress;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "daysleft")
    private Integer daysLeft;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "actual_start_date")
    private Date actualStartDate;

    @Column(name = "actual_end_date")
    private Date actualEndDate;

    @Column(name = "budget")
    private BigInteger budget;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "priority_id")
    private Integer priorityId;
}
