package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 29-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllResourceActiveAllocation;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllocationDataByMonth;
import com.forecastera.service.resourcemanagement.dto.utilityClass.AllocationDataByWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetAllocationDataByMapId {

    @JsonProperty("dailyAllocationData")
    List<AllResourceActiveAllocation> dailyData;

    @JsonProperty("weeklyAllocationData")
    private List<AllocationDataByWeek> weeklyAllocationData;

    @JsonProperty("monthlyAllocationData")
    private List<AllocationDataByMonth> monthlyAllocationData;
}
