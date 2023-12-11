package com.forecastera.service.resourcemanagement.dto.request;/*
 * @Author Sowjanya Aare
 * @Create 16/10/2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDelegatedDepartmentHistory {

//    @JsonProperty("id")
//    private Integer delegatedDepartmentHistoryId;

    @JsonProperty("resourceId")
    private Integer resourceId;

    @JsonProperty("delegatedDepartmentId")
    private Integer delegatedDepartmentId;

    @JsonProperty("delegationStartDate")
    private Date delegationStartDate;

    @JsonProperty("delegationEndDate")
    private Date delegationEndDate;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

}
