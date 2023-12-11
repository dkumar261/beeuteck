package com.forecastera.service.financialmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/*
 * @Author Kanishk Vats
 * @Create 12-07-2023
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

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;
}
