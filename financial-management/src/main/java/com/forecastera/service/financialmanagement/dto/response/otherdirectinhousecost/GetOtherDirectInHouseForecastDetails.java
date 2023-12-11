package com.forecastera.service.financialmanagement.dto.response.otherdirectinhousecost;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @Author Uttam Kachhad
 * @Create 04-07-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetOtherDirectInHouseForecastDetails {

    @JsonProperty("Other Direct In House Cost Forecast Details Id")
    private Integer other_direct_inhouse_cost_forecast_id;

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
