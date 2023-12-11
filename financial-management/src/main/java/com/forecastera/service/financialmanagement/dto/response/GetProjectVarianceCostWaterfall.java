package com.forecastera.service.financialmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 14-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetProjectVarianceCostWaterfall {

    @JsonProperty("project_id")
    private Integer project_id;

    @JsonProperty("project_name")
    private String project_name;

    @JsonProperty("variance")
    private Double variance;

    @JsonProperty("forecast")
    private Double forecast;

    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("internalLabor")
    private Double internalLabor;

    @JsonProperty("externalLabor")
    private Double externalLabor;

    @JsonProperty("capex")
    private Double capex;

    @JsonProperty("opex")
    private Double opex;

}
