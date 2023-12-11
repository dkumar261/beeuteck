package com.forecastera.service.financialmanagement.dto.response.vendorrelatedcost;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @Author Uttam Kachhad
 * @Create 05-07-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetVendorRelatedCostBudgetDetails {

    @JsonProperty("Vendor Related Cost Budget Details Id")
    private Integer vendor_related_cost_budget_id;

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

    @JsonProperty("field_id")
    private Integer field_id;

    @JsonProperty("picklist_id")
    private String picklist_id;
}
