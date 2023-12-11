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
 * @Create 12-10-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetNoOfProjectsByTypeMaster {

    @JsonProperty("typeId")
    private Integer typeId;

    @JsonProperty("typeColor")
    private String typeColor;

    @JsonProperty("projectCount")
    private Long projectCount;

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("projectDetails")
    private List<ProjectDetailsForChart> projectDetails;

}
