package com.forecastera.service.financialmanagement.dto.utilityDtoClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 04-07-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorRelatedForecastExpense {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("vendorRelatedCostForecastId")
    private Integer vendorRelatedCostForecastId;

    @JsonProperty("financialYear")
    private Integer financialYear;

    @JsonProperty("data")
    private List<FinancialMonthlyExpenseData> financialMonthlyExpenseDataList;
}
