package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 04-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @JsonProperty("fte")
    private BigDecimal fte;

    @JsonProperty("requestedFte")
    private BigDecimal requestedFte;


    public AllResourceActiveAllocation(ResourceAllProjectAllocationData resourceAllProjectAllocationData){
        this.id = resourceAllProjectAllocationData.getId();
        this.mapId = resourceAllProjectAllocationData.getMap_id();
        this.fte = resourceAllProjectAllocationData.getFte();
        this.requestedFte =resourceAllProjectAllocationData.getRequested_fte();

        Date date = resourceAllProjectAllocationData.getAllocation_date();
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
        this.allocationDate = java.sql.Date.valueOf(localDate);
    }
}
