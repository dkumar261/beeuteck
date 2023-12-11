package com.forecastera.service.projectmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.dto.utilityClass.ProjectAnalyticsFieldData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/*
 * @Author Uttam Kachhad
 * @Create 17-05-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetProjectAnalysisMaster {

//    @JsonProperty("project_id")
//    private Integer projectId;
//
//    @JsonProperty("project_name")
//    private String projectName;
//
//    @JsonProperty("owner")
//    private String owner;
//
//    @JsonProperty("status")
//    private String status;
//
//    @JsonProperty("progress")
//    private String progress;
//
//    @JsonProperty("start_date")
//    private Date startDate;
//
//    @JsonProperty("end_date")
//    private Date endDate;
//
//    @JsonProperty("days_left")
//    private Integer daysLeft;
//
//    @JsonProperty("type")
//    private String type;
//
//    @JsonProperty("created_date")
//    private Date createdDate;
//
//    @JsonProperty("modified_date")
//    private Date modifiedDate;
//
//    @JsonProperty("created_by")
//    private String createdBy;
//
//    @JsonProperty("modified_by")
//    private String modifiedBy;
//
//    @JsonProperty("actual_start_date")
//    private Date actualStartDate;
//
//    @JsonProperty("actual_end_date")
//    private Date actualEndDate;
//
//    @JsonProperty("priority")
//    private String priority;
//
//    @JsonProperty("budget")
//    private BigInteger budget;
//
//    @JsonProperty("actualBudget")
//    private Integer actualBudget;
//
//    @JsonProperty("teams")
//    private String teams;
//
//    @JsonProperty("field")
//    private List<Map<String, String>> fields;

    @JsonProperty("project_id")
    private Integer projectId;

    @JsonProperty("fields")
    private List<Map<String,Object>> fields;

}
