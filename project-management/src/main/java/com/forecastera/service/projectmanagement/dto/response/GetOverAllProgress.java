package com.forecastera.service.projectmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @Author Uttam Kachhad
 * @Create 19-05-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetOverAllProgress {

    @JsonProperty("statusId")
    private Integer statusId;

    @JsonProperty("statusColor")
    private String statusColor;

    //@JsonProperty("projectCount")
   // private Long projectCount;

    @JsonProperty("statusName")
    private String statusName;

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("departmentId")
    private String departmentId;

}
