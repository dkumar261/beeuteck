package com.forecastera.service.projectmanagement.dto.response;/*
 * @Author Ashutosh Mishra
 * @Create 6/12/2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetKanbanData {

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("dateOfJoin")
    private Date date_of_join;

    @JsonProperty("lastWorkingDate")
    private Date last_working_date;

    @JsonProperty("roleId")
    private String role_id;

    @JsonProperty("role")
    private String role;

    @JsonProperty("skillId")
    private String skill_id;

    @JsonProperty("skills")
    private String skills;

    @JsonProperty("empLocationId")
    private String emp_location_id;

    @JsonProperty("location")
    private String location;

    @JsonProperty("employmentTypeId")
    private String employment_type_id;

    @JsonProperty("employmentType")
    private String employment_type;

    @JsonProperty("employeeLocation")
    private String employee_location;

    @JsonProperty("requestStatus")
    private String request_status;

    @JsonProperty("mapId")
    private Integer map_id;

    @JsonProperty("projectId")
    private Integer project_id;

//    @JsonProperty("projectName")
//    private String project_name;
//
    @JsonProperty("startDate")
    private Date start_date;

    @JsonProperty("endDate")
    private Date end_date;

    @JsonProperty("allocationStartDate")
    private Date allocation_start_date;

    @JsonProperty("allocationEndDate")
    private Date allocation_end_date;

    @JsonProperty("description")
    private String description;

    @JsonProperty("sumFte")
    private BigDecimal sum_fte;

    @JsonProperty("minFte")
    private BigDecimal min_fte;

    @JsonProperty("maxFte")
    private BigDecimal max_fte;

    @JsonProperty("createdBy")
    private String created_by;

    @JsonProperty("createdDate")
    private Date created_date;

    @JsonProperty("modifiedBy")
    private String modified_by;

    @JsonProperty("modifiedDate")
    private Date modified_date;

//    public GetKanbanData(int mapId, int resourceId, int projectId, Date allocationStartDate,
//                         Date allocationEndDate, double fte,String description, String resourceFullName, String projectName, String empLocation) {
//        this.mapId = mapId;
//        this.resourceId = resourceId;
//        this.projectId = projectId;
//        this.allocationStartDate = allocationStartDate;
//        this.allocationEndDate = allocationEndDate;
//        this.fte = fte;
//        this.description= description;
//        this.resourceFullName = resourceFullName;
//        this.projectName = projectName;
//        this.empLocation = empLocation;
//    }

}

