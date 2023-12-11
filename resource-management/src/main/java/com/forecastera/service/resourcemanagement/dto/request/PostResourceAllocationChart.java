package com.forecastera.service.resourcemanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 12-09-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.GanttChartData;
import com.forecastera.service.resourcemanagement.dto.utilityClass.RequestedFteForEachAllocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResourceAllocationChart {

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("ganttChartData")
    private List<GanttChartData> ganttChartData;
}
