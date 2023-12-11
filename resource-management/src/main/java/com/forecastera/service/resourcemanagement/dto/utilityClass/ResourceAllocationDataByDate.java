package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 04-09-2023
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
public class ResourceAllocationDataByDate {

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("allocationDate")
    private Date allocation_date;

    @JsonProperty("sumFte")
    private BigDecimal sum_fte;

}
