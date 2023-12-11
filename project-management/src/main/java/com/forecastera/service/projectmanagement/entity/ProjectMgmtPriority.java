package com.forecastera.service.projectmanagement.entity;/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "proj_priority_lkup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMgmtPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id")
    private Integer priorityId;

    @Column(name = "priority")
    private String priority;

    @Column(name = "color")
    private String color;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "is_enabled")
    private String isEnabled;
}
