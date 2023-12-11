package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 12-09-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestedFteForEachAllocation {

    @JsonProperty("allocationStartDate")
    private Date allocationStartDate;

    @JsonProperty("allocationEndDate")
    private Date allocationEndDate;

    @JsonProperty("requestedFte")
    private Double requestedFte;
}
