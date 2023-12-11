package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 05-09-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllResourceActiveAllocation;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllocationDataByMonth;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllocationDataByWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GanttChartData {

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("allocationStartDate")
    private Date allocationStartDate;

    @JsonProperty("allocationEndDate")
    private Date allocationEndDate;

//    @JsonProperty("requestedFte")
//    private List<RequestedFteForEachAllocation> requested_fte;

    @JsonProperty("isUpdated")
    private Boolean isUpdated;

    @JsonProperty("allocationData")
    List<AllResourceActiveAllocation> allocationData;

    @JsonProperty("weeklyAllocations")
    List<AllocationDataByWeek> weeklyAllocations;

    @JsonProperty("monthlyAllocations")
    List<AllocationDataByMonth> monthlyAllocations;
}
