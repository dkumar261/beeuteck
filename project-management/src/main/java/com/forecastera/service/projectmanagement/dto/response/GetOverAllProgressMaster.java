package com.forecastera.service.projectmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.dto.utilityClass.ProjectDetailsForChart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
 * @Author Sowjanya Aare
 * @Create 13-10-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetOverAllProgressMaster {
    @JsonProperty("statusId")
    private Integer statusId;

    @JsonProperty("statusColor")
    private String statusColor;

    @JsonProperty("projectCount")
     private Long projectCount;

    @JsonProperty("statusName")
    private String statusName;

    @JsonProperty("projectDetails")
    private List<ProjectDetailsForChart> projectDetails;
}
