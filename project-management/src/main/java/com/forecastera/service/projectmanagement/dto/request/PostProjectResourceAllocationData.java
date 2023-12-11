package com.forecastera.service.projectmanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 08-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.dto.response.GetProjectResourceAllocationData;
import com.forecastera.service.projectmanagement.dto.utilityClass.AllResourceActiveAllocation;
import com.forecastera.service.projectmanagement.dto.utilityClass.AllocationDataByMonth;
import com.forecastera.service.projectmanagement.dto.utilityClass.AllocationDataByWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostProjectResourceAllocationData {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("roleId")
    private String roleId;

    @JsonProperty("role")
    private String role;

    @JsonProperty("skillId")
    private String skillId;

    @JsonProperty("skills")
    private String skills;

    @JsonProperty("empLocationId")
    private String empLocationId;

    @JsonProperty("location")
    private String location;

    @JsonProperty("employmentTypeId")
    private String employmentTypeId;

    @JsonProperty("employmentType")
    private String employmentType;

    @JsonProperty("employeeLocation")
    private String employeeLocation;

    @JsonProperty("requestStatus")
    private String requestStatus;

    @JsonProperty("mapId")
    private Integer mapId;

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("startDate")
    private Date start_date;

    @JsonProperty("startDateMessage")
    private String start_date_message;

    @JsonProperty("endDate")
    private Date end_date;

    @JsonProperty("endDateMessage")
    private String end_date_message;

    @JsonProperty("allocationStartDate")
    private Date allocationStartDate;

    @JsonProperty("allocationEndDate")
    private Date allocationEndDate;

    @JsonProperty("max_fte_possible")
    private Double max_fte_possible;

    @JsonProperty("description")
    private String description;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

    @JsonProperty("requestId")
    private Integer requestId;

    @JsonProperty("notify")
    private Boolean notify;

    @JsonProperty("avgFte")
    private Double avgFte;

    @JsonProperty("fteRangeMinValue")
    private Double fteRangeMinValue;

    @JsonProperty("fteRangeMaxValue")
    private Double fteRangeMaxValue;

    @JsonProperty("weeklyAllocationData")
    private List<AllocationDataByWeek> weeklyAllocationData;

    @JsonProperty("monthlyAllocationData")
    private List<AllocationDataByMonth> monthlyAllocationData;

    @JsonProperty("allocations")
    private List<AllResourceActiveAllocation> allocations;

    public PostProjectResourceAllocationData(GetProjectResourceAllocationData getProjectResourceAllocationData){
        this.resourceId = getProjectResourceAllocationData.getResourceId();
        this.mapId = getProjectResourceAllocationData.getMapId();
        this.projectId = getProjectResourceAllocationData.getProjectId();
        this.allocationStartDate = getProjectResourceAllocationData.getAllocationStartDate();
        this.allocationEndDate = getProjectResourceAllocationData.getAllocationEndDate();
        this.description = getProjectResourceAllocationData.getDescription();
        this.createdBy = getProjectResourceAllocationData.getCreatedBy();
        this.createdDate = getProjectResourceAllocationData.getCreatedDate();
        this.modifiedBy = getProjectResourceAllocationData.getModifiedBy();
        this.modifiedDate = getProjectResourceAllocationData.getModifiedDate();
        this.allocations = getProjectResourceAllocationData.getAllocations();
        this.requestId = getProjectResourceAllocationData.getRequestId();
    }
}
