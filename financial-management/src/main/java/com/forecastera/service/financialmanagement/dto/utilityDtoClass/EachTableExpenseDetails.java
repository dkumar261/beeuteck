package com.forecastera.service.financialmanagement.dto.utilityDtoClass;
/*
 * @Author Kanishk Vats
 * @Create 13-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.financialmanagement.util.FinancialUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EachTableExpenseDetails {

    @JsonProperty("directEmpDetails")
    private Double directEmpDetails;

    @JsonProperty("otherDirectCost")
    private Double otherDirectCost;

    @JsonProperty("vendorEmpDetails")
    private Double vendorEmpDetails;

    @JsonProperty("vendorRelatedCost")
    private Double vendorRelatedCost;

    @JsonProperty("capexRelatedExpense")
    private Double capexRelatedExpense;

    @JsonProperty("totalValue")
    private Double totalValue;

    public void setTotal(){
        this.totalValue = FinancialUtils.roundToTwoDecimalPlace(directEmpDetails + otherDirectCost + vendorEmpDetails + vendorRelatedCost + capexRelatedExpense);
    }
}
