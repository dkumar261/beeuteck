package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 05-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.DailyAllocationLimit;
import com.forecastera.service.resourcemanagement.dto.utilityClass.MonthAllocationLimit;
import com.forecastera.service.resourcemanagement.dto.utilityClass.WeeklyAllocationLimit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetAllocationLimitsForResourceId {

    @JsonProperty("monthlyLimit")
    private List<MonthAllocationLimit> monthlyLimit;

    @JsonProperty("weeklyLimit")
    private List<WeeklyAllocationLimit> weeklyLimit;

    @JsonProperty("dailyLimit")
    private List<DailyAllocationLimit> dailyLimit;
}
