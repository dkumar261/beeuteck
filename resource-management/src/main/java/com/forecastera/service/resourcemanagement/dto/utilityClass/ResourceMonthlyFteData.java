package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 19-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceMonthlyFteData {
    @JsonProperty("resource_id")
    private Integer resource_id;

    @JsonProperty("resource_name")
    private String resource_name;

    @JsonProperty("fte")
    private BigDecimal fte;
}
