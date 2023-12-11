package com.forecastera.service.usersettingsmanagement.dto.response;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtRoles;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourceMgmtRoles {

    @JsonProperty("role_id")
    private Integer roleId;

    @JsonProperty("role")
    private String role;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;

    @JsonProperty("parent_role_id")
    private Integer parentRoleId;

    @JsonProperty("department_id")
    private Integer departmentId;

    @JsonProperty("department_name")
    private String departmentName;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("row_order")
    private Integer rowOrder;

    public GetResourceMgmtRoles(ResourceMgmtRoles resourceMgmtRoles){
        this.roleId = resourceMgmtRoles.getRoleId();
        this.role = resourceMgmtRoles.getRole();
        this.createdBy = resourceMgmtRoles.getCreatedBy();
        this.createdDate = resourceMgmtRoles.getCreatedDate();
        this.modifiedBy = resourceMgmtRoles.getModifiedBy();
        this.modifiedDate = resourceMgmtRoles.getModifiedDate();
        this.parentRoleId = resourceMgmtRoles.getParentRoleId();
        this.departmentId = resourceMgmtRoles.getDepartmentId();
        this.isActive = resourceMgmtRoles.getIsActive();
        this.rowOrder = resourceMgmtRoles.getRowOrder();
    }
}
