package com.forecastera.service.usersettingsmanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostGeneralSettings {

    @JsonProperty("base_setting_id")
    private Integer baseSettingId;

    @JsonProperty("date_format")
    private String dateFormat;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("financial_year_start")
    private String financialYearStart;

    @JsonProperty("financial_year_name")
    private String financialYearName;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;
}
