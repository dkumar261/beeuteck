package com.forecastera.service.resourcemanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceDetailsForChart;
import com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceIdNameDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 19-05-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetTotalResourceDetailsMaster {

    @JsonProperty("employmentTypeId")
    private Integer employmentTypeId;

    @JsonProperty("employmentTypeColor")
    private String employmentTypeColor;

    @JsonProperty("resourceCount")
    private Long resourceCount;

    @JsonProperty("employmentTypeName")
    private String employmentTypeName;

    @JsonProperty("resourceDetails")
    private List<ResourceIdNameDetail> resourceDetails;
}
