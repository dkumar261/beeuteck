package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 20-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceDetailsForChart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetUtilizationLevel {

    @JsonProperty("statusId")
    private Integer statusId;

    @JsonProperty("color")
    private String color;

    @JsonProperty("resourceCount")
    private Long resourceCount;

    @JsonProperty("statusName")
    private String statusName;

    @JsonProperty("resourceDetails")
    private List<ResourceDetailsForChart> resourceDetails;
}
