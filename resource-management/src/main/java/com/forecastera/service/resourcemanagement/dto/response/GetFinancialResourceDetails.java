package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 21-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.FinancialResourceData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetFinancialResourceDetails {

    @JsonProperty("employeeId")
    private Integer employeeId;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("data")
    private List<FinancialResourceData> data;
}
