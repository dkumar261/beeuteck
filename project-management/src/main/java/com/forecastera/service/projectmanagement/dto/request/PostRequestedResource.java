package com.forecastera.service.projectmanagement.dto.request;/*
 * @Author Sowjanya Aare
 * @Create 16/10/2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.dto.utilityClass.AllResourceActiveAllocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostRequestedResource {

    @JsonProperty("mapId")
    private Integer mapId;

    @JsonProperty("requestedStatus")
    private String requestedStatus;

    @JsonProperty("allocationStartDate")
    private Date allocationStartDate;

    @JsonProperty("allocationEndDate")
    private Date allocationEndDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("requestedFte")
    private Double requestedFte;

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

    @JsonProperty("roleId")
    private Integer roleId;

    @JsonProperty("notify")
    private Boolean notify;

    @JsonProperty("allocations")
    private List<AllResourceActiveAllocation> allocations;

}
