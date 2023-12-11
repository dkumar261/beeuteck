package com.forecastera.service.financialmanagement.dto.response.directemployeedetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
 * @Author Uttam Kachhad
 * @Create 28-06-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetDirectEmployeeForecastDetails {

    @JsonProperty("Direct Employee Forecast Details Id")
    private Integer direct_employee_det_forecast_id;

    @JsonProperty("Financial Year")
    private Integer financial_year;

    @JsonProperty("Row Order")
    private Integer row_order;

    @JsonProperty("Is Active")
    private Boolean is_active;

    @JsonProperty("Created Date")
    private Date created_date;

    @JsonProperty("Modified Date")
    private Date modified_date;

    @JsonProperty("Created By")
    private String created_by;

    @JsonProperty("Modified By")
    private String modified_by;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("picklist_id")
    private String picklist_id;

    @JsonProperty("fieldValue")
    private String field_value;

    @JsonProperty("fieldType")
    private String field_type;

    @JsonProperty("field_id")
    private Integer field_id;
}
