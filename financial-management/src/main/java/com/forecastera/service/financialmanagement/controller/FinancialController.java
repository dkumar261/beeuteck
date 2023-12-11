package com.forecastera.service.financialmanagement.controller;

import com.forecastera.service.financialmanagement.dto.request.capexrelatedexpense.CapexRelatedExpenseBudgetDto;
import com.forecastera.service.financialmanagement.dto.request.capexrelatedexpense.CapexRelatedExpenseForecastDto;
import com.forecastera.service.financialmanagement.dto.request.directemployeedetails.DirectEmployeeDetailsBudgetDto;
import com.forecastera.service.financialmanagement.dto.request.directemployeedetails.DirectEmployeeDetailsForecastDto;
import com.forecastera.service.financialmanagement.dto.request.otherdirectinhousecost.OtherDirectInHouseCostBudgetDto;
import com.forecastera.service.financialmanagement.dto.request.otherdirectinhousecost.OtherDirectInHouseCostForecastDto;
import com.forecastera.service.financialmanagement.dto.request.vendoremployeedetails.VendorEmployeeDetailsBudgetDto;
import com.forecastera.service.financialmanagement.dto.request.vendoremployeedetails.VendorEmployeeDetailsForecastDto;
import com.forecastera.service.financialmanagement.dto.request.vendorrelatedcost.VendorRelatedCostBudgetDto;
import com.forecastera.service.financialmanagement.dto.request.vendorrelatedcost.VendorRelatedCostForecastDto;
import com.forecastera.service.financialmanagement.dto.response.*;
import com.forecastera.service.financialmanagement.dto.response.capexrelatedexpense.GetCapexRelatedExpenseBudgetDetailMaster;
import com.forecastera.service.financialmanagement.dto.response.capexrelatedexpense.GetCapexRelatedExpenseForecastDetailMaster;
import com.forecastera.service.financialmanagement.dto.response.directemployeedetails.GetDirectEmployeeBudgetDetailsMaster;
import com.forecastera.service.financialmanagement.dto.response.directemployeedetails.GetDirectEmployeeForecastDetailsMaster;
import com.forecastera.service.financialmanagement.dto.response.otherdirectinhousecost.GetOtherDirectCostInHouseBudgetDetailMaster;
import com.forecastera.service.financialmanagement.dto.response.otherdirectinhousecost.GetOtherDirectCostInHouseForecastDetailMaster;
import com.forecastera.service.financialmanagement.dto.response.vendoremployeedetails.GetVendorEmployeeBudgetDetailsMaster;
import com.forecastera.service.financialmanagement.dto.response.vendoremployeedetails.GetVendorEmployeeForecastDetailsMaster;
import com.forecastera.service.financialmanagement.dto.response.vendorrelatedcost.GetVendorRelatedCostBudgetDetailMaster;
import com.forecastera.service.financialmanagement.dto.response.vendorrelatedcost.GetVendorRelatedCostForecastDetailMaster;
import com.forecastera.service.financialmanagement.services.FinancialDashboardService;
import com.forecastera.service.financialmanagement.services.FinancialService;
import com.forecastera.service.financialmanagement.services.FinancialSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 26-05-2023
 * @Description
 */
@RestController
@RequestMapping("/financial")
public class FinancialController {

    @Autowired
    private FinancialService financialService;

    @Autowired
    private FinancialSummaryService financialSummaryService;

    @Autowired
    private FinancialDashboardService financialDashboardService;

    @GetMapping("/getAll")
    public ResponseEntity<List<String>> getAll() {
        return ResponseEntity.ok(Arrays.asList("Financial Management"));
    }

    /*
    DASHBOARD
     */

//    Top 5 Project Variance

    @GetMapping("/getTopProjectVariancesList")
    public ResponseEntity<List<GetTopProjectVariance>> getTopProjectVariancesList(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialDashboardService.getTopProjectVariancesList(financialYear));
    }

//    Upcoming Deadline
    @GetMapping("/getUpcomingDeadlines")
    public ResponseEntity<List<GetUpcomingDeadlines>> getUpcomingDeadlines() {
        return ResponseEntity.ok(financialDashboardService.getUpcomingDeadlines());
    }

//    Project Variance and Cost Waterfall
    @GetMapping("/getProjectVariancesCostWaterfall")
    public ResponseEntity<List<GetProjectVarianceCostWaterfall>> getProjectVariancesCostWaterfall(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialDashboardService.getProjectVariancesCostWaterfall(financialYear));
    }

    @GetMapping("/getBudgetVsForecast")
    public ResponseEntity<List<GetBudgetVsForecast>> getBudgetVsForecast(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialDashboardService.getBudgetVsForecast(financialYear));
    }

    /*
    SUMMARY
     */

    @GetMapping("/getFinancialSummaryBudget")
    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryBudget(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialSummaryService.getFinancialSummaryBudget(financialYear));
    }

    @GetMapping("/getFinancialSummaryForecast")
    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryForecast(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialSummaryService.getFinancialSummaryForecast(financialYear));
    }
//
//    /*
//    Internal Labor Expenses
//     */
//    @GetMapping("/getFinancialSummaryBudgetInternalLabor")
//    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryBudgetInternalLabor(@Valid @RequestParam Integer financialYear) {
//        return ResponseEntity.ok(financialSummaryService.getFinancialSummaryBudgetInternalLabor(financialYear));
//    }
//
//    @GetMapping("/getFinancialSummaryForecastInternalLabor")
//    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryForecastInternalLabor(@Valid @RequestParam Integer financialYear) {
//        return ResponseEntity.ok(financialSummaryService.getFinancialSummaryForecastInternalLabor(financialYear));
//    }
//
//
//    /*
//    Vendor Labor Expenses
//     */
//    @GetMapping("/getFinancialSummaryBudgetVendorLabor")
//    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryBudgetVendorLabor(@Valid @RequestParam Integer financialYear) {
//        return ResponseEntity.ok(financialSummaryService.getFinancialSummaryBudgetVendorLabor(financialYear));
//    }
//
//    @GetMapping("/getFinancialSummaryForecastVendorLabor")
//    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryForecastVendorLabor(@Valid @RequestParam Integer financialYear) {
//        return ResponseEntity.ok(financialSummaryService.getFinancialSummaryForecastVendorLabor(financialYear));
//    }
//
//    /*
//    Total Capex Related Expenses
//     */
//
//    @GetMapping("/getFinancialSummaryBudgetTotalCapex")
//    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryBudgetTotalCapex(@Valid @RequestParam Integer financialYear) {
//        return ResponseEntity.ok(financialSummaryService.getFinancialSummaryBudgetTotalCapex(financialYear));
//    }
//
//    @GetMapping("/getFinancialSummaryForecastTotalCapex")
//    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryForecastTotalCapex(@Valid @RequestParam Integer financialYear) {
//        return ResponseEntity.ok(financialSummaryService.getFinancialSummaryForecastTotalCapex(financialYear));
//    }


    /*
    WORKSHEET
     */

    /*
    Direct Employee Details Table
     */

    @PostMapping("/getDirectEmployeeBudgetDetails")
    public ResponseEntity<List<GetDirectEmployeeBudgetDetailsMaster>> getDirectEmployeeDetailsBudget(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getDirectEmployeeDetailsBudget(financialYear));
    }

    @PostMapping("/createUpdateDirectEmployeeBudgetDetails")
    public ResponseEntity<List<String>> createUpdateDirectEmployeeBudgetDetails(@Valid @RequestBody List<DirectEmployeeDetailsBudgetDto> directEmployeeDetailsBudgetDto) {
        financialService.createUpdateDirectEmployeeBudgetDetails(directEmployeeDetailsBudgetDto);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }

    @PostMapping("/getDirectEmployeeForecastDetails")
    public ResponseEntity<List<GetDirectEmployeeForecastDetailsMaster>> getDirectEmployeeForecastDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getDirectEmployeeForecastDetails(financialYear));
    }

    @PostMapping("/createUpdateDirectEmployeeForecastDetails")
    public ResponseEntity<List<String>> createUpdateDirectEmployeeForecastDetails(@Valid @RequestBody List<DirectEmployeeDetailsForecastDto> directEmployeeDetailsForecastDto) {
        financialService.createUpdateDirectEmployeeForecastDetails(directEmployeeDetailsForecastDto);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }


    /*
    Other Direct In-House Cost Table
     */

    @PostMapping("/getOtherDirectInHouseCostBudgetDetails")
    public ResponseEntity<List<GetOtherDirectCostInHouseBudgetDetailMaster>> getOtherDirectInHouseCostBudgetDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getOtherDirectInHouseCostBudgetDetails(financialYear));
    }

    @PostMapping("/createUpdateOtherDirectInHouseBudgetDetails")
    public ResponseEntity<List<String>> createUpdateOtherDirectInHouseBudgetDetails(@Valid @RequestBody List<OtherDirectInHouseCostBudgetDto> otherDirectInHouseCostBudgetDtoList) {
        financialService.createUpdateOtherDirectInHouseBudgetDetails(otherDirectInHouseCostBudgetDtoList);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }

    @PostMapping("/getOtherDirectInHouseCostForecastDetails")
    public ResponseEntity<List<GetOtherDirectCostInHouseForecastDetailMaster>> getOtherDirectInHouseCostForecastDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getOtherDirectInHouseCostForecastDetails(financialYear));
    }

    @PostMapping("/createUpdateOtherDirectInHouseForecastDetails")
    public ResponseEntity<List<String>> createUpdateOtherDirectInHouseForecastDetails(@Valid @RequestBody List<OtherDirectInHouseCostForecastDto> otherDirectInHouseCostForecastDtoList) {
        financialService.createUpdateOtherDirectInHouseForecastDetails(otherDirectInHouseCostForecastDtoList);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }


    /*
    Vendor Employee Details Table
     */

    @PostMapping("/getVendorEmployeeBudgetDetails")
    public ResponseEntity<List<GetVendorEmployeeBudgetDetailsMaster>> getVendorEmployeeBudgetDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getVendorEmployeeBudgetDetails(financialYear));
    }

    @PostMapping("/createUpdateVendorEmploymentBudgetDetails")
    public ResponseEntity<List<String>> createUpdateVendorEmploymentBudgetDetails(@Valid @RequestBody List<VendorEmployeeDetailsBudgetDto> vendorEmployeeDetailsBudgetDtoList) {
        financialService.createUpdateVendorEmployeeDetailsBudget(vendorEmployeeDetailsBudgetDtoList);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }

    @PostMapping("/getVendorEmployeeForecastDetails")
    public ResponseEntity<List<GetVendorEmployeeForecastDetailsMaster>> getVendorEmployeeForecastDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getVendorEmployeeForecastDetails(financialYear));
    }

    @PostMapping("/createUpdateVendorEmploymentForecastDetails")
    public ResponseEntity<List<String>> createUpdateVendorEmploymentForecastDetails(@Valid @RequestBody List<VendorEmployeeDetailsForecastDto> vendorEmployeeDetailsForecastDtoList) {
        financialService.createUpdateVendorEmployeeDetailsForecast(vendorEmployeeDetailsForecastDtoList);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }


    /*
    Vendor Related Cost Table
     */

    @PostMapping("/getVendorRelatedCostBudgetDetails")
    public ResponseEntity<List<GetVendorRelatedCostBudgetDetailMaster>> getVendorRelatedCostBudgetDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getVendorRelatedCostBudgetDetails(financialYear));
    }

    @PostMapping("/createUpdateVendorRelatedCostBudgetDetails")
    public ResponseEntity<List<String>> createUpdateVendorRelatedCostBudgetDetails(@Valid @RequestBody List<VendorRelatedCostBudgetDto> vendorRelatedCostBudgetDtoList) {
        financialService.createUpdateVendorRelatedCostBudgetDetails(vendorRelatedCostBudgetDtoList);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }

    @PostMapping("/getVendorRelatedCostForecastDetails")
    public ResponseEntity<List<GetVendorRelatedCostForecastDetailMaster>> getVendorRelatedCostForecastDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getVendorRelatedCostForecastDetails(financialYear));
    }

    @PostMapping("/createUpdateVendorRelatedCostForecastDetails")
    public ResponseEntity<List<String>> createUpdateVendorRelatedCostForecastDetails(@Valid @RequestBody List<VendorRelatedCostForecastDto> vendorRelatedCostForecastDtoList) {
        financialService.createUpdateVendorRelatedCostForecastDetails(vendorRelatedCostForecastDtoList);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }


    /*
    Capex Related Expense Table
     */

    @PostMapping("/getCapexRelatedExpenseBudgetDetails")
    public ResponseEntity<List<GetCapexRelatedExpenseBudgetDetailMaster>> getCapexRelatedExpenseBudgetDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getCapexRelatedExpenseBudgetDetails(financialYear));
    }

    @PostMapping("/createUpdateCapexRelatedExpenseBudgetDetails")
    public ResponseEntity<List<String>> createUpdateCapexRelatedExpenseBudgetDetails(@Valid @RequestBody List<CapexRelatedExpenseBudgetDto> capexRelatedExpenseBudgetDtoList) {
        financialService.createUpdateCapexRelatedExpenseBudgetDetails(capexRelatedExpenseBudgetDtoList);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }

    @PostMapping("/getCapexRelatedExpenseForecastDetails")
    public ResponseEntity<List<GetCapexRelatedExpenseForecastDetailMaster>> getCapexRelatedExpenseForecastDetails(@Valid @RequestParam Integer financialYear) {
        return ResponseEntity.ok(financialService.getCapexRelatedExpenseForecastDetails(financialYear));
    }

    @PostMapping("/createUpdateCapexRelatedExpenseForecastDetails")
    public ResponseEntity<List<String>> createUpdateCapexRelatedExpenseForecastDetails(@Valid @RequestBody List<CapexRelatedExpenseForecastDto> capexRelatedExpenseForecastDtoList) {
        financialService.createUpdateCapexRelatedExpenseForecastDetails(capexRelatedExpenseForecastDtoList);
        return ResponseEntity.ok(Arrays.asList("Data Saved/Updated"));
    }
}
