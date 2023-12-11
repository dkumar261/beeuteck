package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 16-10-2023
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
public class ResourceRequestedProjectDetails {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("allocationStartDate")
    private Date allocationStartDate;

    @JsonProperty("allocationEndDate")
    private Date allocationEndDate;

    @JsonProperty("requestedFte")
    private Double requestedFte;
}
