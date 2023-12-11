package com.forecastera.service.financialmanagement.dto.utilityDtoClass;
/*
 * @Author Kanishk Vats
 * @Create 28-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinancialExpense {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("vendorId")
    private Integer vendorId;

    @JsonProperty("financialYear")
    private Integer financialYear;

    @JsonProperty("data")
    private List<FinancialMonthlyExpenseData> financialMonthlyExpenseDataList;
}
