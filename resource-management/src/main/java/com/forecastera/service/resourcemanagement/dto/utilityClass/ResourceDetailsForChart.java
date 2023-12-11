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

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceDetailsForChart {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("departmentName")
    private String departmentName;

    @JsonProperty("resourceFteAvg")
    private Double resourceFteAvg;
}
