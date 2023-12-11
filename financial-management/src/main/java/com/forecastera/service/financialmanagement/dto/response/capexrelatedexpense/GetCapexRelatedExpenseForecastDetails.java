package com.forecastera.service.financialmanagement.dto.response.capexrelatedexpense;
/*
 * @Author Kanishk Vats
 * @Create 05-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetCapexRelatedExpenseForecastDetails {

    @JsonProperty("Capex Related Expense Forecast Details Id")
    private Integer capex_related_expense_forecast_id;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("fieldValue")
    private String field_value;

    @JsonProperty("fieldType")
    private String field_type;

    @JsonProperty("Financial Year")
    private Integer financial_year;

    @JsonProperty("Row Order")
    private Integer row_order;

    @JsonProperty("Is Active")
    private Boolean is_active;

    @JsonProperty("field_id")
    private Integer field_id;

    @JsonProperty("picklist_id")
    private String picklist_id;
}
