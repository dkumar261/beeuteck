package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 19-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetResourceAllocationMatrixForLevelOneMatrix {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("role")
    private String role;

    @JsonProperty("empLocation")
    private String empLocation;

    @JsonProperty("empType")
    private String empType;

    @JsonProperty("avgFte")
    private double averageFte;

    @JsonProperty("departmentId")
    private Integer departmentId;

    @JsonProperty("departmentName")
    private String departmentName;

//    @JsonProperty("monthlyWorkingDays")
//    private List<MonthAllocationLimit> monthlyWorkingDays;
//
    @JsonProperty("allocationData")
    private List<GetResourceAllProjectData> allocationData;
}
