package com.forecastera.service.resourcemanagement.dto.utilityClass;
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
public class ResourceIdDepartmentHead {
    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("departmentId")
    private Integer departmentId;

    @JsonProperty("departmentName")
    private String departmentName;

    @JsonProperty("managedDepartmentId")
    private Integer managedDepartmentId;

    @JsonProperty("managedDepartmentName")
    private String managedDepartmentName;
}
