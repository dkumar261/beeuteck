package com.forecastera.service.financialmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 10-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetUpcomingDeadlines {

    @JsonProperty("project_id")
    private Integer project_id;

    @JsonProperty("project_name")
    private String project_name;

    @JsonProperty("deadline")
    private String deadline;

    @JsonProperty("progress")
    private String progress;

    @JsonProperty("status_color")
    private String status_color;
}
