package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 07-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllResourceActiveAllocation {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("mapId")
    private Integer mapId;

    @JsonProperty("allocationDate")
    private Date allocationDate;

    @JsonProperty("allocatedFte")
    private BigDecimal allocatedFte;

    @JsonProperty("requestedFte")
    private BigDecimal requestedFte;

    @JsonProperty("availableFte")
    private Double availableFte;
}
