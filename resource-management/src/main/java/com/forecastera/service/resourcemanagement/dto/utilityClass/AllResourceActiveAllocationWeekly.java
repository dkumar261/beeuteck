package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 07-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllResourceActiveAllocationWeekly {

    @JsonProperty("weekStartDate")
    private Date weekStartDate;

    @JsonProperty("weekEndDate")
    private Date weekEndDate;

    @JsonProperty("weeklyAvgFte")
    private Double weeklyAvgFte;

}
