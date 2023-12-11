package com.forecastera.service.resourcemanagement.dto.utilityClass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @Author Kanishk Vats
 * @Create 21-10-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data

public class DepartmentIdName {

    @JsonProperty("department_id")
    private Integer departmentId;

    @JsonProperty("department")
    private String departmentName;
}
