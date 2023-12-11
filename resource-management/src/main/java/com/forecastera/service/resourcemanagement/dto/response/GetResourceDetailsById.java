package com.forecastera.service.resourcemanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class GetResourceDetailsById {

    @JsonProperty("fieldId")
    private Integer fieldId;

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("fieldValue")
    private String fieldValue;

    @JsonProperty("fieldType")
    private String fieldType;

    @JsonProperty("setting_type")
    private Integer settingType;

//    @JsonProperty("resource_id")
//    private Integer resourceId;
//
//    @JsonProperty("first_name")
//    private String firstName;
//
//    @JsonProperty("last_name")
//    private String lastName;
//
//    @JsonProperty("email")
//    private String email;
//
//    @JsonProperty("phone")
//    private String phone;
//
//    @JsonProperty("date_of_join")
//    private Date dateOfJoin;
//
//    @JsonProperty("location")
//    private String location;
//
//    @JsonProperty("department_id")
//    private Integer departmentId;
//
//    @JsonProperty("role")
//    private String role;
//
//    @JsonProperty("reporting_mgr")
//    private Integer reportingMgr;
//
//    @JsonProperty("cost_per_hour")
//    private Integer costPerHour;
//
//    @JsonProperty("fte_rate")
//    private BigInteger fteRate;
//
//    @JsonProperty("variable")
//    private BigInteger variable;
//
//    @JsonProperty("bonus")
//    private BigInteger bonus;
//
//    @JsonProperty("skills")
//    private String skills;
//
//    @JsonProperty("created_by")
//    private String createdBy;
//
//    @JsonProperty("created_date")
//    private Date createdDate;
//
//    @JsonProperty("modified_by")
//    private String modifiedBy;
//
//    @JsonProperty("modified_date")
//    private Date modifiedDate;
//
//    @JsonProperty("emp_type_id")
//    private Integer empTypeId;
//
//    @JsonProperty("emp_location")
//    private String empLocation;
}
