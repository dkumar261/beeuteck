package com.forecastera.service.usersettingsmanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.request.PostResourceMgmtDepartment;
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
    private Boolean isActive;

    @Column(name = "row_order")
    private Integer rowOrder;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "status")
    private String status;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "resource_id")
    private Integer resourceId;

    public ResourceMgmtDepartment(PostResourceMgmtDepartment postResourceMgmtDepartment){
        this.departmentId = postResourceMgmtDepartment.getDepartmentId();
        this.department = postResourceMgmtDepartment.getDepartment();
        this.createdBy = postResourceMgmtDepartment.getCreatedBy();
        this.createdDate = postResourceMgmtDepartment.getCreatedDate();
        this.modifiedBy = postResourceMgmtDepartment.getModifiedBy();
        this.modifiedDate = postResourceMgmtDepartment.getModifiedDate();
        this.parentDepartmentId = postResourceMgmtDepartment.getParentDepartmentId();
        this.isActive = postResourceMgmtDepartment.getIsActive();
        this.rowOrder = postResourceMgmtDepartment.getRowOrder();
        this.departmentCode = postResourceMgmtDepartment.getDepartmentCode();
        this.status = postResourceMgmtDepartment.getStatus();
        this.shortDescription = postResourceMgmtDepartment.getShortDescription();
        this.resourceId = postResourceMgmtDepartment.getDepartmentHeadId();
    }
}
