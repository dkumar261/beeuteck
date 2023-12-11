package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 19-06-2023
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
public class GetResourceAllocationMatrix {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("role")
    private String role;

    @JsonProperty("empLocation")
    private String empLocation;

    @JsonProperty("empType")
    private String empType;

    @JsonProperty("avgFte")
    private double averageFte;

    @JsonProperty("mapId")
    private Integer mapId;

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("projectStartDate")
    private Date projectStartDate;

    @JsonProperty("projectEndDate")
    private Date projectEndDate;

    @JsonProperty("allocationStartDate")
    private Date allocationStartDate;

    @JsonProperty("allocationEndDate")
    private Date allocationEndDate;

    @JsonProperty("fteRequested")
    private Double fteRequested;

    @JsonProperty("avgFteForProject")
    private Double avgFteForProject;

    @JsonProperty("fteRangeMinValue")
    private Double fteRangeMinValue;

    @JsonProperty("fteRangeMaxValue")
    private Double fteRangeMaxValue;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

//    @JsonProperty("monthlyWorkingDays")
//    private List<MonthAllocationLimit> monthlyWorkingDays;
//
//    @JsonProperty("allocationData")
//    private List<GetResourceAllProjectData> allocationData;

    public GetResourceAllocationMatrix(GetProjectResourceAllocationData getProjectResourceAllocationData){
        this.resourceId = getProjectResourceAllocationData.getResource_id();
        this.resourceName = getProjectResourceAllocationData.getResource_name();
        this.role = getProjectResourceAllocationData.getRole();
        this.empLocation = getProjectResourceAllocationData.getEmp_location();
        this.empType = getProjectResourceAllocationData.getEmployment_type();
        this.mapId = getProjectResourceAllocationData.getMap_id();
        this.projectId = getProjectResourceAllocationData.getProject_id();
        this.projectName = getProjectResourceAllocationData.getProject_name();
        this.projectStartDate = getProjectResourceAllocationData.getStart_date();
        this.projectEndDate = getProjectResourceAllocationData.getEnd_date();
        this.allocationStartDate = getProjectResourceAllocationData.getAllocation_start_date();
        this.allocationEndDate = getProjectResourceAllocationData.getAllocation_end_date();
        this.fteRequested = getProjectResourceAllocationData.getFte_requested().doubleValue();
        this.avgFteForProject = 0d;
        this.fteRangeMinValue = getProjectResourceAllocationData.getMin_fte().doubleValue();
        this.fteRangeMaxValue = getProjectResourceAllocationData.getMax_fte().doubleValue();
        this.createdBy = getProjectResourceAllocationData.getCreated_by();
        this.createdDate = getProjectResourceAllocationData.getCreated_date();
        this.modifiedBy = getProjectResourceAllocationData.getModified_by();
        this.modifiedDate = getProjectResourceAllocationData.getModified_date();
    }
}
