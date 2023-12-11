package com.forecastera.service.projectmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 08-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.dto.utilityClass.AllResourceActiveAllocation;
import com.forecastera.service.projectmanagement.dto.utilityClass.AllocationDataByMonth;
import com.forecastera.service.projectmanagement.dto.utilityClass.AllocationDataByWeek;
import com.forecastera.service.projectmanagement.dto.utilityClass.MonthAllocationLimit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetProjectResourceAllocationData {

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

    @JsonProperty("avgFte")
    private Double avgFte;

    @JsonProperty("fteRangeMinValue")
    private Double fteRangeMinValue;

    @JsonProperty("fteRangeMaxValue")
    private Double fteRangeMaxValue;

//    @JsonProperty("monthlyWorkingDays")
//    private List<MonthAllocationLimit> monthlyWorkingDays;

    @JsonProperty("weeklyAllocationData")
    private List<AllocationDataByWeek> weeklyAllocationData;

    @JsonProperty("monthlyAllocationData")
    private List<AllocationDataByMonth> monthlyAllocationData;

    @JsonProperty("allocations")
    private List<AllResourceActiveAllocation> allocations;

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

    public GetProjectResourceAllocationData(GetKanbanData getKanbanData, Boolean isThisNamedKanbanData){
        this.resourceId = getKanbanData.getResource_id();
        this.resourceName = getKanbanData.getResource_name();
        this.roleId = getKanbanData.getRole_id();
        this.role = getKanbanData.getRole();
        this.skillId = getKanbanData.getSkill_id();
        if(getKanbanData.getSkills()!=null && !getKanbanData.getSkills().isEmpty()) {
            this.skills = getKanbanData.getSkills();
        }
        else if(isThisNamedKanbanData){
            this.skills = "";
        }
        else{
            this.skills = "All";
        }
        this.empLocationId = getKanbanData.getEmp_location_id();
        if(getKanbanData.getLocation()!=null && !getKanbanData.getLocation().isEmpty()) {
            this.location = getKanbanData.getLocation();
        }
        else if(isThisNamedKanbanData){
            this.location = "";
        }
        else{
            this.location = "All";
        }
        this.employmentTypeId = getKanbanData.getEmployment_type_id();
        if(getKanbanData.getEmployment_type()!=null && !getKanbanData.getEmployment_type().isEmpty()) {
            this.employmentType = getKanbanData.getEmployment_type();
        }
        else if(isThisNamedKanbanData){
            this.employmentType = "";
        }
        else{
            this.employmentType = "All";
        }

        if(getKanbanData.getEmployee_location()!=null && !getKanbanData.getEmployee_location().isEmpty()) {
            this.employeeLocation = getKanbanData.getEmployee_location();
        }
        else if(isThisNamedKanbanData){
            this.employeeLocation = "";
        }
        else{
            this.employeeLocation = "All";
        }
        this.requestStatus = getKanbanData.getRequest_status();
        this.mapId = getKanbanData.getMap_id();
        this.projectId = getKanbanData.getProject_id();
        this.start_date = getKanbanData.getStart_date();
        this.end_date = getKanbanData.getEnd_date();
        this.allocationStartDate = getKanbanData.getAllocation_start_date();
        this.allocationEndDate = getKanbanData.getAllocation_end_date();
        this.description = getKanbanData.getDescription();
        this.fteRangeMinValue = getKanbanData.getMin_fte().doubleValue();
        this.fteRangeMaxValue = getKanbanData.getMax_fte().doubleValue();
        this.createdBy = getKanbanData.getCreated_by();
        this.createdDate = getKanbanData.getCreated_date();
        this.modifiedBy = getKanbanData.getModified_by();
        this.modifiedDate = getKanbanData.getModified_date();
    }
}
