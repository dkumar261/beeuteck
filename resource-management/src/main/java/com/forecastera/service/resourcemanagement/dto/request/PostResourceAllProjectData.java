package com.forecastera.service.resourcemanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 19-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.response.GetProjectResourceAllocationData;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllResourceActiveAllocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResourceAllProjectData {

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

    @JsonProperty("avgFte")
    private Double avgFte;

    @JsonProperty("fteRangeMinValue")
    private Double fteRangeMinValue;

    @JsonProperty("fteRangeMaxValue")
    private Double fteRangeMaxValue;

    @JsonProperty("allocations")
    private List<AllResourceActiveAllocation> allocations;
}
