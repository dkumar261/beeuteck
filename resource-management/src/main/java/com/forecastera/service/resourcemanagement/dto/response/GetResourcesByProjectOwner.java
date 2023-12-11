package com.forecastera.service.resourcemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceCountPerUtilizationLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
 * @Author Kanishk Vats
 * @Create 28-08-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourcesByProjectOwner {

    @JsonProperty("projectOwnerId")
    private Integer projectOwnerId;

    @JsonProperty("resourceCount")
    private Integer resourceCount;

    @JsonProperty("projectOwnerName")
    private String projectOwnerName;

    @JsonProperty("resourceCountByUtilization")
    private List<ResourceCountPerUtilizationLevel> resourceCountByUtilization;
}
