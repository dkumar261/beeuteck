package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 13-11-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectDetailsForNotification {

    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("resourceId")
    private String resourceId;

    @JsonProperty("createdBy")
    private String createdBy;
}
