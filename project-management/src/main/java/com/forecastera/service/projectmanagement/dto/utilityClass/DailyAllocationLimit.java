package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 08-08-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DailyAllocationLimit {

    @JsonProperty("date")
    private Date date;

    @JsonProperty("limit")
    private Double limit;
}
