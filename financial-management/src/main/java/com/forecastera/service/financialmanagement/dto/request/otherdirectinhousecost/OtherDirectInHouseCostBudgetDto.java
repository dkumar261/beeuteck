package com.forecastera.service.financialmanagement.dto.request.otherdirectinhousecost;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.OtherDirectInHouseBudgetExpense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 03-07-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OtherDirectInHouseCostBudgetDto {

    @JsonProperty("other_direct_inhouse_cost_budget_id")
    private Integer otherDirectInHouseCostBudgetId;

    @JsonProperty("financial_year")
    private Integer financialYear;

    @JsonProperty("row_order")
    private Integer rowOrder;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("data")
    private List<OtherDirectInHouseCostData> otherDirectInHouseCostBudgetData;

    @JsonProperty("expenseData")
    private List<OtherDirectInHouseBudgetExpense> otherDirectInHouseBudgetExpenses;
}
