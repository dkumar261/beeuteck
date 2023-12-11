package com.forecastera.service.financialmanagement.dto.request.directemployeedetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @Author Uttam Kachhad
 * @Create 23-06-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DirectEmployeeDetailsBudgetData {

    @JsonProperty("fieldId")
    private Integer fieldId;

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("fieldValue")
    private String fieldValue;

    @JsonProperty("fieldType")
    private String fieldType;

    @JsonProperty("picklist_id")
    private String picklistId;

}
