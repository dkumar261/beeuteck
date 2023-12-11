package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 30-10-2023
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
public class ProjectStartEndDate {

    @JsonProperty("project_id")
    private Integer project_id;

    @JsonProperty("project_start_date")
    private Date project_start_date;

    @JsonProperty("project_end_date")
    private Date project_end_date;
}
