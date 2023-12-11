package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 17-10-2023
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
public class ResourceDailyFteSumData {

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("allocationDate")
    private Date allocationDate;

    @JsonProperty("sumFte")
    private BigDecimal sumFte;
}
