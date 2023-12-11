package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 08-12-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceIdEmail {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("email")
    private String email;
}
