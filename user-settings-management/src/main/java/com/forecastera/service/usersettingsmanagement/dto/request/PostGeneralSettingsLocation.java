package com.forecastera.service.usersettingsmanagement.dto.request;

/*
 * @Author Kanishk Vats
 * @Create 19-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.entity.GeneralSettingsLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostGeneralSettingsLocation {

    @JsonProperty("location_id")
    private Integer locationId;

    @JsonProperty("location")
    private String location;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;

    @JsonProperty("parent_location_id")
    private Integer parentLocationId;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("row_order")
    private Integer rowOrder;
}
