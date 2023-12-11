package com.forecastera.service.financialmanagement.dto.response.vendoremployeedetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/*
 * @Author Uttam Kachhad
 * @Create 29-06-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetVendorEmployeeBudgetDetails {

    @JsonProperty("Vendor Employee Budget Details Id")
    private Integer vendor_employment_details_budget_id;

    @JsonProperty("fieldId")
    private Integer field_id;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("fieldValue")
    private String field_value;

    @JsonProperty("fieldType")
    private String field_type;

    @JsonProperty("Financial Year")
    private Integer financial_year;

    @JsonProperty("Row Order")
    private Integer row_order;

    @JsonProperty("Is Active")
    private Boolean is_active;

    @JsonProperty("picklist_id")
    private String picklist_id;

    /*@JsonProperty("jan")
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
    private BigInteger dec;*/
}
