package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 08-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MonthAllocationLimit {

    @JsonProperty("month")
    private String month;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("limit")
    private Double limit;

    @JsonProperty("workingDays")
    private Double workingDays;
}
