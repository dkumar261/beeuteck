package com.forecastera.service.resourcemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @Author Uttam Kachhad
 * @Create 19-05-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetTotalResource {

    @JsonProperty("employmentTypeId")
    private Integer employmentTypeId;

    @JsonProperty("employmentTypeColor")
    private String employmentTypeColor;

//    @JsonProperty("resourceCount")
//    private Long resourceCount;

    @JsonProperty("employmentTypeName")
    private String employmentTypeName;

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("resourceName")
    private String resourceName;
}
