package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 20-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.MonthlyFteData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetUtilizationGraph {

    @JsonProperty("nextTwelveMonth")
    private List<MonthlyFteData> nextTwelveMonth;

    @JsonProperty("pastTwelveMonth")
    private List<MonthlyFteData> pastTwelveMonth;
}
