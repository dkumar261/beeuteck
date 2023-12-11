package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 15-06-2023
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
public class GetResourceAnalytics {
    @JsonProperty("Resource Id")
    private Integer resource_id;

    @JsonProperty("First Name")
    private String first_name;

    @JsonProperty("Last Name")
    private String last_name;

    @JsonProperty("Date Of Join")
    private Date date_of_join;

    @JsonProperty("Last Working Date")
    private Date last_working_date;

    @JsonProperty("Created Date")
    private Date created_date;

    @JsonProperty("Modified Date")
    private Date modified_date;

    @JsonProperty("Created By")
    private String created_by;

    @JsonProperty("Modified By")
    private String modified_by;

    @JsonProperty("Allocation(FTE)")
    private BigDecimal allocation;

    @JsonProperty("startDate")
    private Date start_date;

    @JsonProperty("endDate")
    private Date end_date;

    @JsonProperty("field_id")
    private Integer field_id;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("fieldValue")
    private String field_value;

    @JsonProperty("resourceType")
    private String resource_type;
}
