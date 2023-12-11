package com.forecastera.service.projectmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 24-07-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.projectmanagement.util.ProjectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FinanceData {

    @JsonProperty("Internal Labor")
    private Double internalLabor;

    @JsonProperty("External Labor")
    private Double externalLabor;

    @JsonProperty("Capex")
    private Double capex;

    @JsonProperty("Opex")
    private Double opex;

    @JsonProperty("Total")
    private Double total;

    public void calculateTotal(){
        this.total = ProjectUtils.roundToTwoDecimalPlace(this.internalLabor + this.externalLabor + this.capex + this.opex);
    }

}
