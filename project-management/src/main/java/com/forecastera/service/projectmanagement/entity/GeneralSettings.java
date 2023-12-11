package com.forecastera.service.projectmanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 11-07-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "base_setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralSettings {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "base_setting_id")
    private Integer baseSettingId;

    @Column(name = "date_format")
    private String dateFormat;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "financial_year_start")
    private String financialYearStart;

    @Column(name = "financial_year_name")
    private String financialYearName;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

}
