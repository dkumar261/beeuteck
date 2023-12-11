package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 12-10-2023
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
public class AllResourceAllAllocationForGanttChart {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("dateOfJoin")
    private Date dateOfJoin;

    @JsonProperty("lastWorkingDate")
    private Date lastWorkingDate;

    @JsonProperty("allocationDate")
    private Date allocationDate;

    @JsonProperty("fte")
    private BigDecimal fte;
}
