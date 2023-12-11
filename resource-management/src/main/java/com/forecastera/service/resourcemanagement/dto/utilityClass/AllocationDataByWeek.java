package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 29-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllocationDataByWeek {

    @JsonProperty("week")
    private Date week;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("averageToShow")
    private Double averageToShow;

    @JsonProperty("averageRequestedToShow")
    private Double averageRequestedToShow;

    @JsonProperty("thisWeekAllocationData")
    private List<AllResourceActiveAllocation> thisWeekAllocationData;
}
