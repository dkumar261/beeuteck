package com.forecastera.service.usersettingsmanagement.dto.response;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtDepartment;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourceMgmtDepartment {

    @JsonProperty("department_id")
    private Integer departmentId;

    @JsonProperty("department")
    private String department;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;

    @JsonProperty("parent_department_id")
    private Integer parentDepartmentId;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("row_order")
    private Integer rowOrder;

    @JsonProperty("department_code")
    private String departmentCode;

    @JsonProperty("status")
    private String status;

    @JsonProperty("short_description")
    private String shortDescription;

    @JsonProperty("resource_id")
    private Integer departmentHeadId;

    @JsonProperty("resource_name")
    private String departmentHeadName;

    @JsonProperty("is_updated")
    private Boolean isUpdated;

    public GetResourceMgmtDepartment(ResourceMgmtDepartment resourceMgmtDepartment){
        this.departmentId = resourceMgmtDepartment.getDepartmentId();
        this.department = resourceMgmtDepartment.getDepartment();
        this.createdBy = resourceMgmtDepartment.getCreatedBy();
        this.createdDate = resourceMgmtDepartment.getCreatedDate();
        this.modifiedBy = resourceMgmtDepartment.getModifiedBy();
        this.modifiedDate = resourceMgmtDepartment.getModifiedDate();
        this.parentDepartmentId = resourceMgmtDepartment.getParentDepartmentId();
        this.isActive = resourceMgmtDepartment.getIsActive();
        this.departmentCode = resourceMgmtDepartment.getDepartmentCode();
        this.status = resourceMgmtDepartment.getStatus();
        this.shortDescription = resourceMgmtDepartment.getShortDescription();
        this.departmentHeadId = resourceMgmtDepartment.getResourceId();
        this.isUpdated = false;
    }
}
