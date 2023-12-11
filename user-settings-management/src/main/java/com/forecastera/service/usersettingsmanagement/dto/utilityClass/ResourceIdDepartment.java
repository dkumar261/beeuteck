package com.forecastera.service.usersettingsmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 21-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceIdDepartment {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("departmentName")
    private String departmentName;
}
