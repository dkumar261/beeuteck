package com.forecastera.service.resourcemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceCountPerUtilizationLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
 * @Author Kanishk Vats
 * @Create 24-08-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourcesByUtilization {

    @JsonProperty("statusId")
    private Integer statusId;

    @JsonProperty("resourceCount")
    private Integer resourceCount;

    @JsonProperty("statusName")
    private String statusName;

    @JsonProperty("startValue")
    private Double startValue;

    @JsonProperty("endValue")
    private Double endValue;

    @JsonProperty("resourceCountByUtilization")
    private List<ResourceCountPerUtilizationLevel> resourceCountByUtilization;
}
