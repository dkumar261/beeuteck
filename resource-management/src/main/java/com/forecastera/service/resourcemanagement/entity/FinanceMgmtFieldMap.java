package com.forecastera.service.resourcemanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 21-07-2023
 * @Description
 */

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

}
