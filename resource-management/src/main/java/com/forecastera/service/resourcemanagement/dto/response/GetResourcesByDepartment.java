package com.forecastera.service.resourcemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceCountPerUtilizationLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

/*
 * @Author Kanishk Vats
 * @Create 23-08-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetResourcesByDepartment {

    @JsonProperty("departmentId")
    private Integer department_id;

    @JsonProperty("resourceCount")
    private Integer resource_count;

    @JsonProperty("departmentName")
    private String department_name;

    @JsonProperty("resourceCountByUtilization")
    private List<ResourceCountPerUtilizationLevel> resourceCountByUtilization;
}
