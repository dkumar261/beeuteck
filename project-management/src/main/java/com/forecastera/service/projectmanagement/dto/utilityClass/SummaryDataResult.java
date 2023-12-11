package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 11-07-2023
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
public class SummaryDataResult {

    @JsonProperty("title")
    private String title;

    @JsonProperty("jan")
    private BigDecimal jan;

    @JsonProperty("feb")
    private BigDecimal feb;

    @JsonProperty("mar")
    private BigDecimal mar;

    @JsonProperty("apr")
    private BigDecimal apr;

    @JsonProperty("may")
    private BigDecimal may;

    @JsonProperty("jun")
    private BigDecimal jun;

    @JsonProperty("jul")
    private BigDecimal jul;

    @JsonProperty("aug")
    private BigDecimal aug;

    @JsonProperty("sep")
    private BigDecimal sep;

    @JsonProperty("oct")
    private BigDecimal oct;

    @JsonProperty("nov")
    private BigDecimal nov;

    @JsonProperty("dec")
    private BigDecimal dec;
}
