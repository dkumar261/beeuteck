package com.forecastera.service.projectmanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 28-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResourceRequestByFilters {

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("roles")
    private String roles;

    @JsonProperty("skills")
    private String skills;

    @JsonProperty("empLocation")
    private String empLocation;

    @JsonProperty("employeeType")
    private String employeeType;

    @JsonProperty("startDate")
    private Date startDate;

    @JsonProperty("endDate")
    private Date endDate;

    @JsonProperty("projectId")
    private Integer projectId;
}
