package com.forecastera.service.usersettingsmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 21-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinanceMgmtTableSettings {

    @JsonProperty("tableName")
    private String tableName;

    @JsonProperty("isEnabled")
    private Boolean isEnabled;

    @JsonProperty("tableType")
    private Integer tableType;

    @JsonProperty("fields")
    private List<FinanceMgmtAdminFieldsDto> fields;
}
