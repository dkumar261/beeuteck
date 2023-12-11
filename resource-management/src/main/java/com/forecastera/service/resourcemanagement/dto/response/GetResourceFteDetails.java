package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 20-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourceFteDetails {

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("resourceName")
    private String resource_name;

    @JsonProperty("departmentName")
    private String department_name;

    @JsonProperty("startDate")
    private Date start_date;

    @JsonProperty("endDate")
    private Date end_date;

    @JsonProperty("sumFte")
    private BigDecimal sum_fte;

//    @JsonProperty("avgFte")
//    private BigDecimal avg_fte;
}
