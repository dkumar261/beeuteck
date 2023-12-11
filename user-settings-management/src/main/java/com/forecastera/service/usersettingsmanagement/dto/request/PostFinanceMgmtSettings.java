package com.forecastera.service.usersettingsmanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 21-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.FinanceMgmtAdminComponentsDto;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.FinanceMgmtTableSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostFinanceMgmtSettings {

    @JsonProperty("componentSettings")
    private List<FinanceMgmtAdminComponentsDto> componentSettings;

    @JsonProperty("fieldSettings")
    private List<FinanceMgmtTableSettings> fieldSettings;

}
