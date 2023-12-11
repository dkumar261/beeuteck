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

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetProjectResourceAllocationData {

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("dateOfJoin")
    private Date date_of_join;

    @JsonProperty("lastWorkingDate")
    private Date last_working_date;

    @JsonProperty("role")
    private String role;

    @JsonProperty("empLocation")
    private String emp_location;

    @JsonProperty("employmentType")
    private String employment_type;

    @JsonProperty("mapId")
    private Integer map_id;

    @JsonProperty("projectId")
    private Integer project_id;

    @JsonProperty("projectName")
    private String project_name;

    @JsonProperty("startDate")
    private Date start_date;

    @JsonProperty("endDate")
    private Date end_date;

    @JsonProperty("allocationStartDate")
    private Date allocation_start_date;

    @JsonProperty("allocationEndDate")
    private Date allocation_end_date;

    @JsonProperty("fteRequested")
    private BigDecimal fte_requested;

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

}
