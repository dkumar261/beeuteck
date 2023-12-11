package com.forecastera.service.financialmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 18-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetBudgetVsForecast {

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("quarter")
    private String quarter;

    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("forecast")
    private Double forecast;
}
