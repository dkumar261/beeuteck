package com.forecastera.service.projectmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/*
 * @Author Sowjanya Aare
 * @Create 20-10-2023
 * @Description
 */
@Entity
@Table(name = "Delegated_Department_History")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelegatedDepartmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer delegatedDepartmentHistoryId;

    @Column(name = "resource_id")
    private Integer resourceId;

    @Column(name = "delegated_department_id")
    private Integer delegatedDepartmentId;

    @Column(name = "delegation_start_date")
    private Date delegationStartDate;

    @Column(name = "delegation_end_date")
    private Date delegationEndDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "is_active")
    private Boolean isActive;
}