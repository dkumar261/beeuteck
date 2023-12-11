package com.forecastera.service.usersettingsmanagement.dto.request;/*
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
public class PostDelegatedResourceHistory {

    @JsonProperty("delegationId")
    private Integer delegationId;

    @JsonProperty("delegatedResourceId")
    private Integer delegatedResourceId;

    @JsonProperty("delegatedToResourceId")
    private Integer delegatedToResourceId;

    @JsonProperty("delegationStartDate")
    private Date delegationStartDate;

    @JsonProperty("delegationEndDate")
    private Date delegationEndDate;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

    @JsonProperty("isActive")
    private Boolean isActive;

}
