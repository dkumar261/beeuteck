package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 14-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeeklyAllocationLimit {

    @JsonProperty("weekStartDate")
    private Date weekStartDate;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("limit")
    private Double limit;

    @JsonProperty("alreadyAllocated")
    private Double alreadyAllocated;

    @JsonProperty("noOfDays")
    private Integer noOfDays;
}
