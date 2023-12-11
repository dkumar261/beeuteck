package com.forecastera.service.projectmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 05-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.dto.utilityClass.DailyAllocationLimit;
import com.forecastera.service.projectmanagement.dto.utilityClass.MonthAllocationLimit;
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

    @JsonProperty("dailyLimit")
    private List<DailyAllocationLimit> dailyLimit;
}
