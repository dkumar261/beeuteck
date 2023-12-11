package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 20-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MonthlyFteData {

    @JsonProperty("month")
    private String month;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("monthlyFte")
    private Double monthlyFte;
}
