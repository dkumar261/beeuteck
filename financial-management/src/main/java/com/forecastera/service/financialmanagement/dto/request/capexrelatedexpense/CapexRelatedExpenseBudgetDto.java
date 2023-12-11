package com.forecastera.service.financialmanagement.dto.request.capexrelatedexpense;
/*
 * @Author Kanishk Vats
 * @Create 05-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.CapexFinancialExpense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CapexRelatedExpenseBudgetDto {

    @JsonProperty("capex_related_expense_budget_id")
    private Integer capexRelatedExpenseBudgetId;

    @JsonProperty("financial_year")
    private Integer financialYear;

    @JsonProperty("row_order")
    private Integer rowOrder;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("data")
    private List<CapexRelatedExpenseData> capexRelatedExpenseData;

    @JsonProperty("expenseData")
    private List<CapexFinancialExpense> capexFinancialExpenseList;
}
