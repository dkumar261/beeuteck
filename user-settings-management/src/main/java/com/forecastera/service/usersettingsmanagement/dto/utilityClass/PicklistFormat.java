package com.forecastera.service.usersettingsmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PicklistFormat {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("field_id")
    private Integer fieldId;

    @JsonProperty("color")
    private String color;

    @JsonProperty("start_value")
    private Double startValue;

    @JsonProperty("end_value")
    private Double endValue;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;

    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("row_order")
    private Integer rowOrder;

    @JsonProperty("departmentId")
    private Integer departmentId;

    public PicklistFormat(ProjectMgmtPriority projectMgmtPriority){
        this.id = projectMgmtPriority.getPriorityId();
        this.name = projectMgmtPriority.getPriority();
        this.color = projectMgmtPriority.getColor();
        this.createdBy = projectMgmtPriority.getCreatedBy();
        this.createdDate = projectMgmtPriority.getCreatedDate();
        this.modifiedBy = projectMgmtPriority.getModifiedBy();
        this.modifiedDate = projectMgmtPriority.getModifiedDate();
        this.isEnabled = projectMgmtPriority.getIsEnabled();
    }

    public PicklistFormat(ProjectMgmtStatus projectMgmtStatus){
        this.id = projectMgmtStatus.getStatusId();
        this.name = projectMgmtStatus.getStatus();
        this.color = projectMgmtStatus.getColor();
        this.createdBy = projectMgmtStatus.getCreatedBy();
        this.createdDate = projectMgmtStatus.getCreatedDate();
        this.modifiedBy = projectMgmtStatus.getModifiedBy();
        this.modifiedDate = projectMgmtStatus.getModifiedDate();
        this.isEnabled = projectMgmtStatus.getIsEnabled();
    }

    public PicklistFormat(ProjectMgmtType projectMgmtType){
        this.id = projectMgmtType.getTypeId();
        this.name = projectMgmtType.getType();
        this.color = projectMgmtType.getColor();
        this.createdBy = projectMgmtType.getCreatedBy();
        this.createdDate = projectMgmtType.getCreatedDate();
        this.modifiedBy = projectMgmtType.getModifiedBy();
        this.modifiedDate = projectMgmtType.getModifiedDate();
        this.isEnabled = projectMgmtType.getIsEnabled();
    }

    public PicklistFormat(ResourceMgmtEmploymentType resourceMgmtEmploymentType){
        this.id = resourceMgmtEmploymentType.getEmpTypeId();
        this.name = resourceMgmtEmploymentType.getEmploymentType();
        this.color = resourceMgmtEmploymentType.getColor();
        this.createdBy = resourceMgmtEmploymentType.getCreatedBy();
        this.createdDate = resourceMgmtEmploymentType.getCreatedDate();
        this.modifiedBy = resourceMgmtEmploymentType.getModifiedBy();
        this.modifiedDate = resourceMgmtEmploymentType.getModifiedDate();
        this.isEnabled = resourceMgmtEmploymentType.getIsEnabled();
    }

    public PicklistFormat(ResourceMgmtStatus resourceMgmtStatus){
        this.id = resourceMgmtStatus.getStatusId();
        this.name = resourceMgmtStatus.getStatus();
        this.color = resourceMgmtStatus.getColor();
        this.startValue = resourceMgmtStatus.getStartValue();
        this.endValue = resourceMgmtStatus.getEndValue();
        this.createdBy = resourceMgmtStatus.getCreatedBy();
        this.createdDate = resourceMgmtStatus.getCreatedDate();
        this.modifiedBy = resourceMgmtStatus.getModifiedBy();
        this.modifiedDate = resourceMgmtStatus.getModifiedDate();
        this.isEnabled = resourceMgmtStatus.getIsEnabled();
    }

    public PicklistFormat(ProjectCustomPicklist projectCustomPicklist){
        this.id = projectCustomPicklist.getId();
        this.name = projectCustomPicklist.getPicklistValue();
        this.color = projectCustomPicklist.getColor();
        this.fieldId = projectCustomPicklist.getPicklistId();
        this.createdBy = projectCustomPicklist.getCreatedBy();
        this.createdDate = projectCustomPicklist.getCreatedDate();
        this.modifiedBy = projectCustomPicklist.getModifiedBy();
        this.modifiedDate = projectCustomPicklist.getModifiedDate();
        this.isEnabled = projectCustomPicklist.getIsEnabled();
    }

    public PicklistFormat(ResourceCustomPicklist resourceCustomPicklist){
        this.id = resourceCustomPicklist.getId();
        this.name = resourceCustomPicklist.getPicklistValue();
        this.color = resourceCustomPicklist.getColor();
        this.fieldId = resourceCustomPicklist.getPicklistId();
        this.createdBy = resourceCustomPicklist.getCreatedBy();
        this.createdDate = resourceCustomPicklist.getCreatedDate();
        this.modifiedBy = resourceCustomPicklist.getModifiedBy();
        this.modifiedDate = resourceCustomPicklist.getModifiedDate();
        this.isEnabled = resourceCustomPicklist.getIsEnabled();
    }

    public PicklistFormat(FinanceCustomPicklist financeCustomPicklist){
        this.id = financeCustomPicklist.getId();
        this.name = financeCustomPicklist.getPicklistValue();
        this.color = financeCustomPicklist.getColor();
        this.fieldId = financeCustomPicklist.getPicklistId();
        this.createdBy = financeCustomPicklist.getCreatedBy();
        this.createdDate = financeCustomPicklist.getCreatedDate();
        this.modifiedBy = financeCustomPicklist.getModifiedBy();
        this.modifiedDate = financeCustomPicklist.getModifiedDate();
        this.isEnabled = financeCustomPicklist.getIsEnabled().equals("1");
    }

    public PicklistFormat(ResourceMgmt resourceMgmt){
        this.id = resourceMgmt.getResourceId();
        this.name = resourceMgmt.getFirstName() + " " + resourceMgmt.getLastName();
        this.createdBy = resourceMgmt.getCreatedBy();
        this.createdDate = resourceMgmt.getCreatedDate();
        this.modifiedBy = resourceMgmt.getModifiedBy();
        this.modifiedDate = resourceMgmt.getModifiedDate();
        if(resourceMgmt.getLastWorkingDate()!=null && resourceMgmt.getLastWorkingDate().compareTo(new Date())<0){
            this.isEnabled = false;
        }
        else{
            this.isEnabled = true;
        }
    }

    public PicklistFormat(ResourceMgmtRoles resourceMgmtRoles){
        this.id = resourceMgmtRoles.getRoleId();
        this.name = resourceMgmtRoles.getRole();
        this.createdBy = resourceMgmtRoles.getCreatedBy();
        this.createdDate = resourceMgmtRoles.getCreatedDate();
        this.modifiedBy = resourceMgmtRoles.getModifiedBy();
        this.modifiedDate = resourceMgmtRoles.getModifiedDate();
        this.isActive = resourceMgmtRoles.getIsActive();
        this.rowOrder = resourceMgmtRoles.getRowOrder();
        this.isEnabled = resourceMgmtRoles.getIsActive();
        this.departmentId = resourceMgmtRoles.getDepartmentId();
    }

    public PicklistFormat(ResourceMgmtSkills resourceMgmtSkills){
        this.id = resourceMgmtSkills.getSkillId();
        this.name = resourceMgmtSkills.getSkill();
        this.createdBy = resourceMgmtSkills.getCreatedBy();
        this.createdDate = resourceMgmtSkills.getCreatedDate();
        this.modifiedBy = resourceMgmtSkills.getModifiedBy();
        this.modifiedDate = resourceMgmtSkills.getModifiedDate();
        this.isActive = resourceMgmtSkills.getIsActive();
        this.rowOrder = resourceMgmtSkills.getRowOrder();
        this.isEnabled = resourceMgmtSkills.getIsActive();
    }

    public PicklistFormat(ResourceMgmtDepartment resourceMgmtDepartment){
        this.id = resourceMgmtDepartment.getDepartmentId();
        this.name = resourceMgmtDepartment.getDepartment();
        this.createdBy = resourceMgmtDepartment.getCreatedBy();
        this.createdDate = resourceMgmtDepartment.getCreatedDate();
        this.modifiedBy = resourceMgmtDepartment.getModifiedBy();
        this.modifiedDate = resourceMgmtDepartment.getModifiedDate();
        this.isActive = resourceMgmtDepartment.getIsActive();
        this.rowOrder = resourceMgmtDepartment.getRowOrder();
        this.isEnabled = resourceMgmtDepartment.getIsActive();
    }

    public PicklistFormat(GeneralSettingsLocation generalSettingsLocation){
        this.id = generalSettingsLocation.getLocationId();
        this.name = generalSettingsLocation.getLocation();
        this.createdBy = generalSettingsLocation.getCreatedBy();
        this.createdDate = generalSettingsLocation.getCreatedDate();
        this.modifiedBy = generalSettingsLocation.getModifiedBy();
        this.modifiedDate = generalSettingsLocation.getModifiedDate();
        this.isActive = generalSettingsLocation.getIsActive();
        this.rowOrder = generalSettingsLocation.getRowOrder();
        this.isEnabled = generalSettingsLocation.getIsActive();
    }
}
