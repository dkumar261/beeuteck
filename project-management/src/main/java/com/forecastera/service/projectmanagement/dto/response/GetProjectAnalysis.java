package com.forecastera.service.projectmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/*
 * @Author Uttam Kachhad
 * @Create 17-05-2023
 * @Description  NOTE : json property name and field name always be same in this DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetProjectAnalysis {

//    @JsonProperty("project_id")
//    private Integer project_id;
//
//    @JsonProperty("project_name")
//    private String project_name;
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
//    private Date start_date;
//
//    @JsonProperty("end_date")
//    private Date end_date;
//
//    @JsonProperty("daysleft")
//    private Integer daysleft;
//
//    @JsonProperty("type")
//    private String type;
//
//    @JsonProperty("created_date")
//    private Date created_date;
//
//    @JsonProperty("modified_date")
//    private Date modified_date;
//
//    @JsonProperty("created_by")
//    private String created_by;
//
//    @JsonProperty("modified_by")
//    private String modified_by;

//    @JsonProperty("actual_start_date")
//    private Date actual_start_date;
//
//    @JsonProperty("actual_end_date")
//    private Date actual_end_date;
//
//    @JsonProperty("priority")
//    private String priority;
//
//    @JsonProperty("budget")
//    private BigInteger budget;
//
//    @JsonProperty("forecast")
//    private Integer forecast;
//
//    @JsonProperty("teams")
//    private String teams;

    @JsonProperty("Project Id")
    private Integer project_id;

    @JsonProperty("Created Date")
    private Date created_date;

    @JsonProperty("Last Modified Date")
    private Date modified_date;

    @JsonProperty("Created By")
    private String created_by;

    @JsonProperty("Last Modified By")
    private String modified_by;

    @JsonProperty("field_id")
    private Integer field_id;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("fieldValue")
    private String field_value;

    @JsonProperty("fieldType")
    private String field_type;
}
