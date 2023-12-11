package com.forecastera.service.financialmanagement.entity.directemployeedetails;

/*
 * @Author Uttam Kachhad
 * @Create 22-06-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "direct_employee_det_budget")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DirectEmployeeDetailsBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direct_employee_det_budget_id")
    private Integer id;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "designation")
    private String designation;

    @Column(name = "location")
    private String location;

    @Column(name = "salary")
    private Long salary;

    @Column(name = "fringe")
    private Long fringe;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "valid")
    private String valid;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "work_location")
    private String workLocation;

    @Column(name = "row_order")
    private Integer rowOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "financial_year")
    private Integer financialYear;

}
