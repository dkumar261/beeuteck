package com.forecastera.service.usersettingsmanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 21-06-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.utilityClass.FinanceMgmtAdminComponentsDto;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.FinanceMgmtAdminFieldsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "finance_mgmt_field_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinanceMgmtFieldMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Integer fieldId;

    @Column(name = "fields")
    private String fields;

    @Column(name = "visibility")
    private String visibility;

    @Column(name = "enabled")
    private String enabled;

    @Column(name = "edit_access")
    private String editAccess;

    @Column(name = "category")
    private String category;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "setting_type")
    private Integer settingType;

    @Column(name = "field_type")
    private String fieldType;

    @Column(name = "is_add_more")
    private String isAddMore;

    @Column(name = "table_type")
    private Integer tableType;

    @Column(name = "iseditaccessddl")
    private String isEditAccessDdl;

    @Column(name = "isenabledfreeze")
    private String isEnabledFreeze;

    public FinanceMgmtFieldMap(FinanceMgmtAdminFieldsDto financeMgmtAdminFieldsDto){

        this.fieldId = financeMgmtAdminFieldsDto.getFieldId();
        this.fields = financeMgmtAdminFieldsDto.getFields();

        if (financeMgmtAdminFieldsDto.getVisibility() != null && !financeMgmtAdminFieldsDto.getVisibility().isEmpty()) {
            this.visibility = String.join(",", financeMgmtAdminFieldsDto.getVisibility());
        }
        else{
            this.visibility = null;
        }

        this.enabled = financeMgmtAdminFieldsDto.getIsEnabled()==true?"1":"0";

        if (financeMgmtAdminFieldsDto.getEditAccess() != null && !financeMgmtAdminFieldsDto.getEditAccess().isEmpty()) {
            this.editAccess = String.join(",", financeMgmtAdminFieldsDto.getEditAccess());
        }
        else{
            this.editAccess = null;
        }

        this.category = financeMgmtAdminFieldsDto.getCategory();
        this.createdBy = financeMgmtAdminFieldsDto.getCreatedBy();
        this.createdDate = financeMgmtAdminFieldsDto.getCreatedDate();
        this.modifiedBy = financeMgmtAdminFieldsDto.getModifiedBy();
        this.modifiedDate = financeMgmtAdminFieldsDto.getModifiedDate();
        this.settingType = financeMgmtAdminFieldsDto.getSettingType();
        this.fieldType = financeMgmtAdminFieldsDto.getFieldType();
        this.isAddMore = financeMgmtAdminFieldsDto.getIsAddMore()==true?"1":"0";
        this.tableType = financeMgmtAdminFieldsDto.getTableType();
        this.isEditAccessDdl = financeMgmtAdminFieldsDto.getIsEditAccessDdl()==true?"1":"0";
        this.isEnabledFreeze = financeMgmtAdminFieldsDto.getIsEnabledFreeze()==true?"1":"0";
    }

    public FinanceMgmtFieldMap(FinanceMgmtAdminComponentsDto financeMgmtAdminComponentsDto){

        this.fieldId = financeMgmtAdminComponentsDto.getFieldId();
        this.fields = financeMgmtAdminComponentsDto.getFields();

        if (financeMgmtAdminComponentsDto.getVisibility() != null && !financeMgmtAdminComponentsDto.getVisibility().isEmpty()) {
            this.visibility = String.join(",", financeMgmtAdminComponentsDto.getVisibility());
        }
        else{
            this.visibility = null;
        }

        this.enabled = financeMgmtAdminComponentsDto.getIsEnabled()==true?"1":"0";

        if (financeMgmtAdminComponentsDto.getEditAccess() != null && !financeMgmtAdminComponentsDto.getEditAccess().isEmpty()) {
            this.editAccess = String.join(",", financeMgmtAdminComponentsDto.getEditAccess());
        }
        else{
            this.editAccess = null;
        }

        this.category = financeMgmtAdminComponentsDto.getCategory();
        this.createdBy = financeMgmtAdminComponentsDto.getCreatedBy();
        this.createdDate = financeMgmtAdminComponentsDto.getCreatedDate();
        this.modifiedBy = financeMgmtAdminComponentsDto.getModifiedBy();
        this.modifiedDate = financeMgmtAdminComponentsDto.getModifiedDate();
        this.settingType = financeMgmtAdminComponentsDto.getSettingType();
        this.fieldType = financeMgmtAdminComponentsDto.getFieldType();
        this.isAddMore = financeMgmtAdminComponentsDto.getIsAddMore()==true?"1":"0";
        this.tableType = financeMgmtAdminComponentsDto.getTableType();
        this.isEditAccessDdl = financeMgmtAdminComponentsDto.getIsEditAccessDdl()==true?"1":"0";
        this.isEnabledFreeze = financeMgmtAdminComponentsDto.getIsEnabledFreeze()==true?"1":"0";
    }
}
