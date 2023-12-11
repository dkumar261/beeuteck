package com.forecastera.service.financialmanagement.dto.response.otherdirectinhousecost;

/*
 * @Author Uttam Kachhad
 * @Create 03-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.OtherDirectInHouseBudgetExpense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetOtherDirectCostInHouseBudgetDetailMaster {

    @JsonProperty("other_direct_inhouse_cost_budget_id")
    private Integer otherDirectInHouseCostBudgetId;

    @JsonProperty("financial_year")
    private Integer financial_year;

    @JsonProperty("row_order")
    private Integer row_order;

    @JsonProperty("is_active")
    private Boolean is_active;

    @JsonProperty("data")
    private List<Map<String, Object>> fields;

    @JsonProperty("expenseData")
    private List<OtherDirectInHouseBudgetExpense>  otherDirectInHouseBudgetExpenses;

}
