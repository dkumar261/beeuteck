package com.forecastera.service.financialmanagement.entity.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 04-07-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "finance_mgmt_field_value_forecast_vendor_emp")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinanceMgmtFieldValueForecastVendorEmp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "finance_mgmt_field_value_id")
    private Integer id;

    @Column(name = "vendor_employment_details_forecast_id")
    private Integer vendorForecastId;

    @Column(name = "field_id")
    private Integer fieldId;

    @Column(name = "field_value")
    private String fieldValue;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;
}
