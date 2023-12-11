package com.forecastera.service.financialmanagement.dto.request.directemployeedetails;

/*
 * @Author Uttam Kachhad
 * @Create 28-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DirectEmployeeDetailsForecastDto {

    @JsonProperty("direct_employee_det_forecast_id")
    private Integer directEmployeeDetForecastId;

    @JsonProperty("financial_year")
    private Integer financialYear;

    @JsonProperty("row_order")
    private Integer rowOrder;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("data")
    private List<DirectEmployeeDetailsForecastData> directEmployeeDetailsForecastData;
}
