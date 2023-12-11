package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 29-10-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllocatedFteAverageByMapId {

    @JsonProperty("mapId")
    private Integer mapId;

    @JsonProperty("totalAllocated")
    private BigDecimal totalAllocated;
}
