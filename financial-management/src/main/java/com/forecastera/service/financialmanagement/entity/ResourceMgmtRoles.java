package com.forecastera.service.financialmanagement.entity;

/*
 * @Author Uttam Kachhad
 * @Create 22-06-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "role_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmtRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role")
    private String role;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private String modifiedDate;

    @Column(name = "parent_role_id")
    private String parentRoleId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "row_order")
    private Integer rowOrder;
}
