package com.forecastera.service.projectmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 20-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddProject {

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;


}
