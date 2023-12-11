package com.forecastera.service.resourcemanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.resourcemanagement.dto.utilityClass.CreateResourceData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/*
 * @Author Kanishk Vats
 * @Create 17-05-2023
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResourceMgmt {

    @JsonProperty("data")
    private List<CreateResourceData> resourceData;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;

}
