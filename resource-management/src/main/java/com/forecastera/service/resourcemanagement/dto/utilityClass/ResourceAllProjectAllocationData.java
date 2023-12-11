package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 05-09-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceAllProjectAllocationData {

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("dateOfJoin")
    private Date date_of_join;

    @JsonProperty("lastWorkingDate")
    private Date last_working_date;

//    @JsonProperty("requestedFte")
//    private BigDecimal requested_fte;

    @JsonProperty("projectId")
    private Integer project_id;

    @JsonProperty("projectName")
    private String project_name;

    @JsonProperty("allocationStartDate")
    private Date allocation_start_dt;

    @JsonProperty("allocationEndDate")
    private Date allocation_end_dt;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("mapId")
    private Integer map_id;

    @JsonProperty("allocationDate")
    private Date allocation_date;

    @JsonProperty("fte")
    private BigDecimal fte;

    @JsonProperty("requestedFte")
    private BigDecimal requested_fte;

    public ResourceAllProjectAllocationData(ResourceAllProjectAllocationData resourceAllProjectAllocationData){
        this.resource_id = resourceAllProjectAllocationData.getResource_id();
        this.resource_name = resourceAllProjectAllocationData.getResource_name();
        this.date_of_join = resourceAllProjectAllocationData.getDate_of_join();
        this.last_working_date = resourceAllProjectAllocationData.getLast_working_date();
        this.project_id = resourceAllProjectAllocationData.getProject_id();
        this.project_name = resourceAllProjectAllocationData.getProject_name();
        this.allocation_start_dt = resourceAllProjectAllocationData.getAllocation_start_dt();
        this.allocation_end_dt = resourceAllProjectAllocationData.getAllocation_end_dt();
        this.id = resourceAllProjectAllocationData.getId();
        this.map_id = resourceAllProjectAllocationData.getMap_id();
        this.allocation_date = resourceAllProjectAllocationData.getAllocation_date();
        this.fte = resourceAllProjectAllocationData.getFte();
        this.requested_fte = resourceAllProjectAllocationData.getRequested_fte();
    }
}
