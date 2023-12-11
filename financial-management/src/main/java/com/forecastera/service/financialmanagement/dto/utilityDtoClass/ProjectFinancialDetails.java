package com.forecastera.service.financialmanagement.dto.utilityDtoClass;
/*
 * @Author Kanishk Vats
 * @Create 13-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectFinancialDetails {

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("budgetDetails")
    private EachTableExpenseDetails budgetDetails;

    @JsonProperty("forecastDetails")
    private EachTableExpenseDetails forecastDetails;

    @JsonProperty("varianceDetails")
    private EachTableExpenseDetails varianceDetails;
}
