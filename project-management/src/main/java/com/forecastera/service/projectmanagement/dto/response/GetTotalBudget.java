package com.forecastera.service.projectmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 25-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetTotalBudget {

    @JsonProperty("Total Budget")
    private Double totalBudget;

    @JsonProperty("Projected Cost")
    private Double projectedCost;
}
