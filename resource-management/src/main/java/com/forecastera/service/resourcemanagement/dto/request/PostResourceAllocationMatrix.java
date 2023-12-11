package com.forecastera.service.resourcemanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 19-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResourceAllocationMatrix {

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

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

    @JsonProperty("allocationData")
    List<PostResourceAllProjectData> allocationData;
}
