package com.forecastera.service.financialmanagement.dto.response.vendoremployeedetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.FinancialExpense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/*
 * @Author Uttam Kachhad
 * @Create 29-06-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetVendorEmployeeBudgetDetailsMaster {

    @JsonProperty("vendor_employment_details_budget_id")
    private Integer vendorEmploymentDetailsBudgetId;

    @JsonProperty("financial_year")
    private Integer financial_year;

    @JsonProperty("row_order")
    private Integer row_order;

    @JsonProperty("is_active")
    private Boolean is_active;

    @JsonProperty("data")
    private List<Map<String,Object>> fields;

    @JsonProperty("expenseData")
    private List<FinancialExpense> financialExpenses;
}
