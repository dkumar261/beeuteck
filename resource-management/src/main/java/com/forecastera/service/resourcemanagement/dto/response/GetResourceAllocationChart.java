package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 12-09-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.GanttChartData;
import com.forecastera.service.resourcemanagement.dto.utilityClass.WorkingDaysInMonth;
import com.forecastera.service.resourcemanagement.dto.utilityClass.WorkingDaysInWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetResourceAllocationChart {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("departmentName")
    private String department_name;

    @JsonProperty("startDate")
    private Date startDate;

    @JsonProperty("endDate")
    private Date endDate;

    @JsonProperty("weekWorkingDays")
    private List<WorkingDaysInWeek> weekWorkingDays;

    @JsonProperty("monthWorkingDays")
    private List<WorkingDaysInMonth> monthWorkingDays;

    @JsonProperty("maxFteAllowedByAdmin")
    private Double maxFteAllowedByAdmin;

    @JsonProperty("ganttChartData")
    private List<GanttChartData> ganttChartData;
}
