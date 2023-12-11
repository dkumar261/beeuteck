package com.forecastera.service.usersettingsmanagement.dto.utilityClass;
/*
 * @Author Kanishk Vats
 * @Create 21-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.entity.FinanceMgmtFieldMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinanceMgmtAdminComponentsDto {
    @JsonProperty("fieldId")
    private Integer fieldId;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("visibility")
    private List<String> visibility;

    @JsonProperty("isEnabled")
    private Boolean isEnabled;

    @JsonProperty("editAccess")
    private List<String> editAccess;

    @JsonProperty("category")
    private String category;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("modifiedBy")
    private String modifiedBy;

    @JsonProperty("modifiedDate")
    private Date modifiedDate;

    @JsonProperty("settingType")
    private Integer settingType;

    @JsonProperty("fieldType")
    private String fieldType;

    @JsonProperty("isAddMore")
    private Boolean isAddMore;

    @JsonProperty("tableType")
    private Integer tableType;

    @JsonProperty("isEditAccessDdl")
    private Boolean isEditAccessDdl;

    @JsonProperty("isEnabledFreeze")
    private Boolean isEnabledFreeze;

    public FinanceMgmtAdminComponentsDto(FinanceMgmtFieldMap financeMgmtFieldMap){

        this.fieldId = financeMgmtFieldMap.getFieldId();
        this.fields = financeMgmtFieldMap.getFields();

        if(StringUtils.isNotBlank(financeMgmtFieldMap.getVisibility())){
            this.visibility = Arrays.asList(financeMgmtFieldMap.getVisibility().split(","));
        }
        else{
            this.visibility = new ArrayList<>();
        }

        this.isEnabled = financeMgmtFieldMap.getEnabled().equals("1");

        if(StringUtils.isNotBlank(financeMgmtFieldMap.getEditAccess())){
            this.editAccess = Arrays.asList(financeMgmtFieldMap.getEditAccess().split(","));
        }
        else{
            this.editAccess = new ArrayList<>();
        }
        this.category = financeMgmtFieldMap.getCategory();
        this.createdBy = financeMgmtFieldMap.getCreatedBy();
        this.createdDate = financeMgmtFieldMap.getCreatedDate();
        this.modifiedBy = financeMgmtFieldMap.getModifiedBy();
        this.modifiedDate = financeMgmtFieldMap.getModifiedDate();
        this.settingType = financeMgmtFieldMap.getSettingType();
        this.fieldType = financeMgmtFieldMap.getFieldType();
        this.isAddMore = financeMgmtFieldMap.getIsAddMore().equals("1");
        this.tableType = financeMgmtFieldMap.getTableType();
        this.isEditAccessDdl = financeMgmtFieldMap.getIsEditAccessDdl().equals("1");
        this.isEnabledFreeze = financeMgmtFieldMap.getIsEnabledFreeze().equals("1");
    }
}
