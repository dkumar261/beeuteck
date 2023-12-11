package com.forecastera.service.financialmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 07-07-2023
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
public class SummaryDataResultDto {

    @JsonProperty("type")
    private String tableType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("subCategory")
    private String subCategory;

    @JsonProperty("isAggregate")
    private Boolean isAggregate;

    @JsonProperty("data")
    private List<Double> data;
}
