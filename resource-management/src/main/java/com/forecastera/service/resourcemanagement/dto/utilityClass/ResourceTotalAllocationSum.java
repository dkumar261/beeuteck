package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 08-11-2023
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
public class ResourceTotalAllocationSum {

    @JsonProperty("resource_id")
    private Integer resource_id;

    @JsonProperty("resource_name")
    private String resource_name;

    @JsonProperty("date_of_join")
    private Date date_of_join;

    @JsonProperty("last_working_date")
    private Date last_working_date;

    @JsonProperty("sum_fte")
    private BigDecimal sum_fte;
}
