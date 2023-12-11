package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 17-11-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
