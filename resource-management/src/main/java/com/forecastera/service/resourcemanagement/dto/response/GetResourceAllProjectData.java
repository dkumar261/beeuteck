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
public class GetResourceAllProjectData {

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

    @JsonProperty("projectOwnerId")
    private Integer projectOwnerId;

    @JsonProperty("projectOwnerName")
    private String projectOwnerName;

//    @JsonProperty("allocations")
//    private List<AllResourceActiveAllocation> allocations;

//    @JsonProperty("allocationsWeekly")
//    private List<AllResourceActiveAllocationWeekly> allocationsWeekly;
//
//    @JsonProperty("allocationsMonthly")
//    private List<AllResourceActiveAllocationMonthly> allocationsMonthly;

    public GetResourceAllProjectData(GetProjectResourceAllocationData getProjectResourceAllocationData){
        this.mapId = getProjectResourceAllocationData.getMap_id();
        this.projectId = getProjectResourceAllocationData.getProject_id();
        this.projectName = getProjectResourceAllocationData.getProject_name();
        this.projectStartDate = getProjectResourceAllocationData.getStart_date();
        this.projectEndDate = getProjectResourceAllocationData.getEnd_date();
        this.allocationStartDate = getProjectResourceAllocationData.getAllocation_start_date();
        this.allocationEndDate = getProjectResourceAllocationData.getAllocation_end_date();
        if(getProjectResourceAllocationData.getFte_requested()!=null) {
            this.fteRequested = getProjectResourceAllocationData.getFte_requested().doubleValue();
        }
        else{
            this.fteRequested = 0d;
        }
        this.fteRangeMinValue = getProjectResourceAllocationData.getMin_fte().doubleValue();
        this.fteRangeMaxValue = getProjectResourceAllocationData.getMax_fte().doubleValue();
        this.createdBy = getProjectResourceAllocationData.getCreated_by();
        this.createdDate = getProjectResourceAllocationData.getCreated_date();
        this.modifiedBy = getProjectResourceAllocationData.getModified_by();
        this.modifiedDate = getProjectResourceAllocationData.getModified_date();
    }

    public GetResourceAllProjectData(GetProjectResourceAllocationDataForLevelOneMatrix getProjectResourceAllocationDataForLevelOneMatrix){
        this.mapId = getProjectResourceAllocationDataForLevelOneMatrix.getMap_id();
        this.projectId = getProjectResourceAllocationDataForLevelOneMatrix.getProject_id();
        this.projectName = getProjectResourceAllocationDataForLevelOneMatrix.getProject_name();
        this.projectStartDate = getProjectResourceAllocationDataForLevelOneMatrix.getStart_date();
        this.projectEndDate = getProjectResourceAllocationDataForLevelOneMatrix.getEnd_date();
        this.allocationStartDate = getProjectResourceAllocationDataForLevelOneMatrix.getAllocation_start_date();
        this.allocationEndDate = getProjectResourceAllocationDataForLevelOneMatrix.getAllocation_end_date();
        if(getProjectResourceAllocationDataForLevelOneMatrix.getFte_requested()!=null) {
            this.fteRequested = getProjectResourceAllocationDataForLevelOneMatrix.getFte_requested().doubleValue();
        }
        else{
            this.fteRequested = 0d;
        }
        this.fteRangeMinValue = getProjectResourceAllocationDataForLevelOneMatrix.getMin_fte().doubleValue();
        this.fteRangeMaxValue = getProjectResourceAllocationDataForLevelOneMatrix.getMax_fte().doubleValue();
        this.createdBy = getProjectResourceAllocationDataForLevelOneMatrix.getCreated_by();
        this.createdDate = getProjectResourceAllocationDataForLevelOneMatrix.getCreated_date();
        this.modifiedBy = getProjectResourceAllocationDataForLevelOneMatrix.getModified_by();
        this.modifiedDate = getProjectResourceAllocationDataForLevelOneMatrix.getModified_date();
        this.projectOwnerId = getProjectResourceAllocationDataForLevelOneMatrix.getProject_owner_id();
        this.projectOwnerName = getProjectResourceAllocationDataForLevelOneMatrix.getProject_owner_name();
    }
}
