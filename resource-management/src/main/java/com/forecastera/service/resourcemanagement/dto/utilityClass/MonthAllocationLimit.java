package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 05-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

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

    @JsonProperty("alreadyAllocated")
    private Double alreadyAllocated;

    @JsonProperty("noOfDays")
    private Integer noOfDays;

    @JsonProperty("workingDays")
    private Double workingDays;
}
