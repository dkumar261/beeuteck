package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 04-09-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllocationDataByMonth {

    @JsonProperty("month")
    private String month;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("allocatedAverageToShow")
    private Double allocatedAverageToShow;

    @JsonProperty("requestedAverageToShow")
    private Double requestedAverageToShow;

    @JsonProperty("availableAverageToShow")
    private Double availableAverageToShow;

    @JsonProperty("monthlyAllocations")
    private List<AllResourceActiveAllocation> monthlyAllocations;
}
