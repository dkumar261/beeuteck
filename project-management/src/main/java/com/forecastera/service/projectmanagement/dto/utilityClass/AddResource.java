package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 16-06-2023
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
public class AddResource {

    @JsonProperty("resource_id")
    private Integer resource_id;

    @JsonProperty("resource_name")
    private String resource_name;

    @JsonProperty("role")
    private String role;

    @JsonProperty("skill")
    private String skill;

//    @JsonProperty("location")
//    private String location;

    @JsonProperty("employee_type")
    private String employee_type;

    @JsonProperty("employee_location")
    private String employee_location;

}
