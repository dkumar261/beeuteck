package com.forecastera.service.financialmanagement.dto.request.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 26-06-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.FinancialExpense;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorEmployeeDetailsBudgetDto {

    @JsonProperty("vendor_employment_details_budget_id")
    private Integer vendorEmploymentDetailsBudgetId;

    @JsonProperty("financial_year")
    private Integer financialYear;

    @JsonProperty("row_order")
    private Integer rowOrder;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("data")
    private List<VendorEmployeeDetailsData> vendorEmployeeDetailsData;

    @JsonProperty("expenseData")
    private List<FinancialExpense> financialExpenses;

//    @JsonProperty("employee_id")
//    private Integer employeeId;

//    @JsonProperty("name")
//    private String name;

//    @JsonProperty("project_id")
//    private Integer projectId;
//
//    @JsonProperty("location")
//    private String location;
//
//    @JsonProperty("designation")
//    private String designation;
//
//    @JsonProperty("work_location")
//    private String workLocation;
//
//    @JsonProperty("salary")
//    private Long salary;
//
//    @JsonProperty("fringe")
//    private Long fringe;
//
//    @JsonProperty("start_date")
//    private Date startDate;
//
//    @JsonProperty("end_date")
//    private Date endDate;

//    @JsonProperty("created_by")
//    private Integer createdBy;
//
//    @JsonProperty("created_date")
//    private Date createdDate;
//
//    @JsonProperty("modified_by")
//    private Integer modifiedBy;
//
//    @JsonProperty("modified_date")
//    private Date modifiedDate;

//    @JsonProperty("jan")
//    private Long jan;
//
//    @JsonProperty("feb")
//    private Long feb;
//
//    @JsonProperty("mar")
//    private Long mar;
//
//    @JsonProperty("apr")
//    private Long apr;
//
//    @JsonProperty("may")
//    private Long may;
//
//    @JsonProperty("jun")
//    private Long jun;
//
//    @JsonProperty("jul")
//    private Long jul;
//
//    @JsonProperty("aug")
//    private Long aug;
//
//    @JsonProperty("sep")
//    private Long sep;
//
//    @JsonProperty("oct")
//    private Long oct;
//
//    @JsonProperty("nov")
//    private Long nov;
//
//    @JsonProperty("dec")
//    private Long dec;

//    @JsonProperty("financial_year")
//    private Integer financialYear;
//
//    @JsonProperty("row_order")
//    private Integer rowOrder;
}
