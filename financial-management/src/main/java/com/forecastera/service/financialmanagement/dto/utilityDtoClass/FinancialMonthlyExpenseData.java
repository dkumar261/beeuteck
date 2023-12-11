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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinancialMonthlyExpenseData {

    @JsonProperty("month")
    private String month;

    @JsonProperty("value")
    private Long value;
}
