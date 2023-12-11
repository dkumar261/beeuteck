package com.forecastera.service.usersettingsmanagement.dto.response;
/*
 * @Author Sowjanya Aare
 * @Create 21-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetDelegatedResourceHistory {

    @JsonProperty("delegationId")
    private Integer delegationId;

    @JsonProperty("delegatedResourceId")
    private Integer delegatedResourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("resourceDeptName")
    private String resourceDeptName;

    @JsonProperty("delegatedToResourceId")
    private Integer delegatedToResourceId;

    @JsonProperty("delegatedToResourceName")
    private String delegatedToResourceName;

    @JsonProperty("delegatedToResourceDepartmentName")
    private String delegatedToResourceDepartmentName;

    @JsonProperty("delegationStartDate")
    private Date delegationStartDate;

    @JsonProperty("delegationEndDate")
    private Date delegationEndDate;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

    @JsonProperty("isActive")
    private Boolean isActive;

//    public GetDelegatedResourceHistory(DelegatedResourceHistory delegatedDepartmentHistory) {
//        this.delegatedDepartmentHistoryId =delegatedDepartmentHistory.get;
//        this.resourceId = delegatedDepartmentHistory;
//        this.delegatedDepartmentId = delegatedDepartmentHistory;
//        this.delegationStartDate = delegatedDepartmentHistory;
//        this.delegationEndDate = delegatedDepartmentHistory;
//        this.modifiedBy = delegatedDepartmentHistory;
//        this.modifiedDate = delegatedDepartmentHistory;
//    }
}
