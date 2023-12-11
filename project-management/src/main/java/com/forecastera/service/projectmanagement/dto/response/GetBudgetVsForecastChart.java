package com.forecastera.service.projectmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 21-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetBudgetVsForecastChart {

    @JsonProperty("budget")
    private List<Double> budget;

    @JsonProperty("forecast")
    private List<Double> forecast;
}
