package com.forecastera.service.financialmanagement.dto.response.directemployeedetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 * @Author Uttam Kachhad
 * @Create 26-06-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetDirectEmployeeBudgetDetailsMaster {

    @JsonProperty("direct_employee_det_budget_id")
    private Integer directEmployeeDetailsBudgetId;

    @JsonProperty("financial_year")
    private Integer financial_year;

    @JsonProperty("row_order")
    private Integer row_order;

    @JsonProperty("is_active")
    private Boolean is_active;

    @JsonProperty("data")
    private List<Map<String,Object>> fields;

}
