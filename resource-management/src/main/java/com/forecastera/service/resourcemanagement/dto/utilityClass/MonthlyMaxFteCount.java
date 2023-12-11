package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 14-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MonthlyMaxFteCount {

    @JsonProperty("max_fte")
    private BigDecimal max_fte;

    @JsonProperty("count_of_max_fte")
    private Integer count_of_max_fte;
}
