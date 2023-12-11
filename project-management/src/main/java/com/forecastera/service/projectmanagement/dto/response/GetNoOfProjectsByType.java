package com.forecastera.service.projectmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/*
 * @Author Uttam Kachhad
 * @Create 19-05-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetNoOfProjectsByType {

    @JsonProperty("typeId")
    private Integer typeId;

    @JsonProperty("typeColor")
    private String typeColor;

   // @JsonProperty("projectCount")
   // private Long projectCount;

    @JsonProperty("typeName")
    private String typeName;

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("departmentId")
    private String departmentId;

}
