package com.forecastera.service.resourcemanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "role_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmtRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role")
    private String role;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "parent_role_id")
    private Integer parentRoleId;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "row_order")
    private Integer rowOrder;

    @Column(name = "department_id")
    private Integer departmentId;
}
