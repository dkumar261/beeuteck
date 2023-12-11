package com.forecastera.service.financialmanagement.dto.request.vendorrelatedcost;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.VendorRelatedForecastExpense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 05-07-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorRelatedCostForecastDto {

    @JsonProperty("vendor_related_cost_forecast_id")
    private Integer vendorRelatedCostForecastId;

    @JsonProperty("financial_year")
    private Integer financialYear;

    @JsonProperty("row_order")
    private Integer rowOrder;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("data")
    private List<VendorRelatedCostData> vendorRelatedCostData;

    @JsonProperty("expenseData")
    private List<VendorRelatedForecastExpense> vendorRelatedForecastExpense;
}
