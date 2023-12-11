package com.forecastera.service.financialmanagement.dto.request.directemployeedetails;

/*
 * @Author Uttam Kachhad
 * @Create 23-06-2023
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
public class DirectEmployeeDetailsBudgetDto {

    @JsonProperty("direct_employee_det_budget_id")
    private Integer directEmployeeDetBudgetId;

    @JsonProperty("financial_year")
    private Integer financialYear;

    @JsonProperty("row_order")
    private Integer rowOrder;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("data")
    private List<DirectEmployeeDetailsBudgetData> directEmployeeDetailsBudgetData;

   /* @JsonProperty("created_by")
    private Integer createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private Integer modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;*/

}
