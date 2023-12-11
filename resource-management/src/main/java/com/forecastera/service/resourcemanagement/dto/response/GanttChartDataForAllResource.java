package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 12-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllocationDataByDaily;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllocationDataByMonth;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllocationDataByWeek;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceRequestedProjectDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GanttChartDataForAllResource {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("startDate")
    private Date startDate;

    @JsonProperty("endDate")
    private Date endDate;

    @JsonProperty("resourceAllocatedProjects")
    List<ResourceRequestedProjectDetails> resourceAllocatedProjects;

    @JsonProperty("dailyAllocation")
    List<AllocationDataByDaily> dailyAllocation;

    @JsonProperty("weeklyAllocation")
    List<AllocationDataByWeek> weeklyAllocation;

    @JsonProperty("monthlyAllocation")
    List<AllocationDataByMonth> monthlyAllocation;

}
