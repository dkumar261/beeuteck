package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateResourceData {

    @JsonProperty("field_id")
    private Integer fieldId;

    @JsonProperty("field_name")
    private String fieldName;

    @JsonProperty("value")
    private String value;

    @JsonProperty("setting_type")
    private Integer settingType;
}
