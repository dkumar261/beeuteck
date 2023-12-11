package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 25-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceCountPerUtilizationLevel {

    @JsonProperty("statusId")
    private Integer statusId;

    @JsonProperty("resourceCount")
    private Integer resourceCount;

    @JsonProperty("statusName")
    private String statusName;

    @JsonProperty("startValue")
    private Double startValue;

    @JsonProperty("endValue")
    private Double endValue;
}
