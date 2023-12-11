package com.forecastera.service.financialmanagement.dto.utilityDtoClass;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OtherDirectInHouseBudgetExpense {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("otherDirectInHouseCostBudgetExpenseId")
    private Integer otherDirectInHouseCostBudgetExpenseId;

    @JsonProperty("financialYear")
    private Integer financialYear;

    @JsonProperty("data")
    private List<FinancialMonthlyExpenseData> financialMonthlyExpenseDataList;
}
