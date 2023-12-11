package com.forecastera.service.resourcemanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 12-06-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "department_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmtDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "department")
    private String department;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "parent_department_id")
    private Integer parentDepartmentId;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "row_order")
    private Integer rowOrder;

    @Column(name = "resource_id")
    private Integer resourceId;
}
