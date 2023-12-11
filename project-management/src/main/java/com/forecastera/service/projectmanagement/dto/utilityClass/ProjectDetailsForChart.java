package com.forecastera.service.projectmanagement.dto.utilityClass;

/*
 * @Author Sowjanya Aare
 * @Create 12-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectDetailsForChart {

    @JsonProperty("project_Id")
    private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;
}
