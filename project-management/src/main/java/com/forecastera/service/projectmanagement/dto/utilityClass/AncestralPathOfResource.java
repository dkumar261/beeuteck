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
public class AncestralPathOfResource {

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("depthOrder")
    private Integer depth_order;

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("ancestralPath")
    private String ancestral_path;

    @JsonProperty("ancestralPathByName")
    private String ancestral_path_by_name;
}
