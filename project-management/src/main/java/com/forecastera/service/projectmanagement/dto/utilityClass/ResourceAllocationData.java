package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 27-07-2023
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
public class ResourceAllocationData {

    @JsonProperty("resource_id")
    private Integer resource_id;

    @JsonProperty("start_date")
    private Date start_date;

    @JsonProperty("end_date")
    private Date end_date;

//    @JsonProperty("fte")
//    private BigDecimal fte;

    @JsonProperty("fte_worked")
    private BigDecimal fte_worked;
}
