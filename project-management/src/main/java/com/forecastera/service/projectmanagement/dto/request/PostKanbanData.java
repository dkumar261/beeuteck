package com.forecastera.service.projectmanagement.dto.request;/*
 * @Author Ashutosh Mishra
 * @Create 6/12/2023
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
public class PostKanbanData {
    @JsonProperty("mapId")
    private Integer mapId;

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("allocationStartDate")
    private Date allocationStartDate;

    @JsonProperty("allocationEndDate")
    private Date allocationEndDate;

    @JsonProperty("fte")
    private Double fte;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    @JsonProperty("roleId")
    private String roleId;
}
