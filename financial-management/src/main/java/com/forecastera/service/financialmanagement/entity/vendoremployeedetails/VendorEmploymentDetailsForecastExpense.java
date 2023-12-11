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

@Entity
@Table(name = "vendor_employment_details_forecast_expense")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorEmploymentDetailsForecastExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "vendor_employment_details_forecast_id")
    private Integer vendorDetailsForecastId;

    @Column(name = "jan")
    private Long jan;

    @Column(name = "feb")
    private Long feb;

    @Column(name = "mar")
    private Long mar;

    @Column(name = "apr")
    private Long apr;

    @Column(name = "may")
    private Long may;

    @Column(name = "jun")
    private Long jun;

    @Column(name = "jul")
    private Long jul;

    @Column(name = "aug")
    private Long aug;

    @Column(name = "sep")
    private Long sep;

    @Column(name = "oct")
    private Long oct;

    @Column(name = "nov")
    private Long nov;

    @Column(name = "dec")
    private Long dec;

    @Column(name = "financial_year")
    private Integer financialYear;
}
