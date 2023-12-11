package com.forecastera.service.financialmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/*
 * @Author Uttam Kachhad
 * @Create 30-06-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetVendorMonthlyExpenseData {

    @JsonProperty("jan")
    private BigInteger jan;

    @JsonProperty("feb")
    private BigInteger feb;

    @JsonProperty("mar")
    private BigInteger mar;

    @JsonProperty("apr")
    private BigInteger apr;

    @JsonProperty("may")
    private BigInteger may;

    @JsonProperty("jun")
    private BigInteger jun;

    @JsonProperty("jul")
    private BigInteger jul;

    @JsonProperty("aug")
    private BigInteger aug;

    @JsonProperty("sep")
    private BigInteger sep;

    @JsonProperty("oct")
    private BigInteger oct;

    @JsonProperty("nov")
    private BigInteger nov;

    @JsonProperty("dec")
    private BigInteger dec;

}
