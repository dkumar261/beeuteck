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
@Table(name = "finance_mgmt_field_value_budget_vendor_related")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorRelatedCostFieldValueBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "finance_mgmt_field_value_id")
    private Integer id;

    @Column(name = "vendor_related_cost_budget_id")
    private Integer vendorRelatedCostBudgetId;

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
