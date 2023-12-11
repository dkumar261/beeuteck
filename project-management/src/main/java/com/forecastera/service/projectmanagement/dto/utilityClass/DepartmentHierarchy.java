package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 20-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DepartmentHierarchy {

    @JsonProperty("departmentId")
    private Integer department_id;

    @JsonProperty("depthOrder")
    private Integer depth_order;

    @JsonProperty("departmentName")
    private String department_name;

    @JsonProperty("ancestralPath")
    private String ancestral_path;

    @JsonProperty("ancestralPathByName")
    private String ancestral_path_by_name;
}
