package com.forecastera.service.financialmanagement.entity.vendorrelatedcost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/*
 * @Author Uttam Kachhad
 * @Create 05-07-2023
 * @Description
 */
@Entity
@Table(name = "vendor_related_cost_forecast")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorRelatedCostForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_related_cost_forecast_id")
    private Integer id;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "financial_year")
    private Integer financialYear;

    @Column(name = "row_order")
    private Integer rowOrder;

    @Column(name = "is_active")
    private Boolean isActive;
}
