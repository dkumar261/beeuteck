package com.forecastera.service.resourcemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceCountPerUtilizationLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

/*
 * @Author Kanishk Vats
 * @Create 23-08-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourcesByProject {

    @JsonProperty("projectId")
    private Integer project_id;

    @JsonProperty("resourceCount")
    private Integer resource_count;

    @JsonProperty("projectName")
    private String project_name;

    @JsonProperty("resourceCountByUtilization")
    private List<ResourceCountPerUtilizationLevel> resourceCountByUtilization;
}
