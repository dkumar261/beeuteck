package com.forecastera.service.financialmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 12-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetTopProjectVariance {

    @JsonProperty("project_id")
    private Integer project_id;

    @JsonProperty("project_name")
    private String project_name;

    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("forecast")
    private Double forecast;

    @JsonProperty("variance")
    private Double variance;
}
