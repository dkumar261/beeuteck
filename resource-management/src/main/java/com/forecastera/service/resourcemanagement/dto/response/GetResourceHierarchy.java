package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 15-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceHierarchyDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetResourceHierarchy {

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("department")
    private String department;

    @JsonProperty("managerId")
    private Integer manager_id;

    @JsonProperty("role")
    private List<String> role;

    public GetResourceHierarchy(ResourceHierarchyDetails resourceHierarchyDetails){
        this.resource_id = resourceHierarchyDetails.getResource_id();
        this.resource_name = resourceHierarchyDetails.getResource_name();
        this.department = resourceHierarchyDetails.getDepartment();
        this.manager_id = resourceHierarchyDetails.getManager_id();
        this.role = List.of(resourceHierarchyDetails.getRole().split(","));
    }
}
