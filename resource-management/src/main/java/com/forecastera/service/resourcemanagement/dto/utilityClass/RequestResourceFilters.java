package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 31-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.entity.ProjectResourceMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestResourceFilters {

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

    public RequestResourceFilters(ProjectResourceMapping projectResourceMapping){
        this.resourceName = "";
        this.roles = projectResourceMapping.getRoleId();
        this.skills = projectResourceMapping.getSkillId();
        this.empLocation = projectResourceMapping.getEmpLocationId();
        this.employeeType = projectResourceMapping.getEmpTypeId();
        this.startDate = java.sql.Date.valueOf(new java.sql.Date(projectResourceMapping.getAllocationStartDate().getTime()).toLocalDate());
        this.endDate = java.sql.Date.valueOf(new java.sql.Date(projectResourceMapping.getAllocationEndDate().getTime()).toLocalDate());
        this.projectId = projectResourceMapping.getProjectId();
    }
}
