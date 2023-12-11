package com.forecastera.service.financialmanagement.entity.capexrelatedexpense;
/*
 * @Author Kanishk Vats
 * @Create 05-07-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "capex_related_expense_forecast")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapexRelatedExpenseForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capex_related_expense_forecast_id")
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
