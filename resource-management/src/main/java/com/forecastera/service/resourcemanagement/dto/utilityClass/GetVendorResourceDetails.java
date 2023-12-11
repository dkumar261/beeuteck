package com.forecastera.service.resourcemanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 13-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetVendorResourceDetails {

    @JsonProperty("resourceId")
    private Integer resource_id;

    @JsonProperty("firstName")
    private String first_name;

    @JsonProperty("lastName")
    private String last_name;

    @JsonProperty("dateOfJoin")
    private Date date_of_join;

    @JsonProperty("lastWorkingDate")
    private Date last_working_date;

    @JsonProperty("empLocation")
    private String emp_location;

    @JsonProperty("roleId")
    private String role_id;

    @JsonProperty("role")
    private String role;

    @JsonProperty("salary")
    private BigDecimal salary;

    @JsonProperty("locationId")
    private String location_id;

    @JsonProperty("location")
    private String location;
}
