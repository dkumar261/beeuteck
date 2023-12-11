package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 15-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetResourceAnalyticsMaster {

    @JsonProperty("resource_id")
    private Integer resourceId;

    @JsonProperty("fields")
    private List<Map<String,Object>> fields;
}
