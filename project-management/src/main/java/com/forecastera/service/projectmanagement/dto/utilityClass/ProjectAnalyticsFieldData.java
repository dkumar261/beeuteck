package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 13-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectAnalyticsFieldData {

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("fieldValue")
    private Object fieldValue;

    @JsonProperty("fieldType")
    private String fieldType;
}
