package com.forecastera.service.projectmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.entity.ProjectMgmtFieldValue;
import lombok.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 17-05-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetProjectMgmt {

    @JsonProperty("project_id")
    private Integer projectId;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("status_id")
    private String statusId;

    @JsonProperty("progress")
    private String progress;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("daysleft")
    private Integer daysLeft;

    @JsonProperty("type_id")
    private String typeId;

    @JsonProperty("actual_start_date")
    private Date actualStartDate;

    @JsonProperty("actual_end_date")
    private Date actualEndDate;

    @JsonProperty("budget")
    private BigInteger budget;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;

    @JsonProperty("priority_id")
    private String priorityId;

}
