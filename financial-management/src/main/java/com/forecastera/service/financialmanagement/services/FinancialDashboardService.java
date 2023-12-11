package com.forecastera.service.financialmanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 06-07-2023
 * @Description
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.financialmanagement.config.exception.DataNotFoundException;
import com.forecastera.service.financialmanagement.dto.response.*;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.EachTableExpenseDetails;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.ProjectFinancialDetails;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.ProjectIdName;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.ProjectWiseExpenseTotalEachTable;
import com.forecastera.service.financialmanagement.repository.GeneralSettingsRepo;
import com.forecastera.service.financialmanagement.repository.ProjectMgmtRepo;
import com.forecastera.service.financialmanagement.util.FinancialUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinancialDashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialService.class);

    @Autowired
    @Qualifier("edbObjectMapper")
    private ObjectMapper objectMapper;

    private final EntityManagerFactory emf;

    private final EntityManager entityManager;

    @Autowired
    public FinancialDashboardService(EntityManagerFactory emf, EntityManager entityManager) {
        this.emf = emf;
        this.entityManager = emf.createEntityManager();
    }

    @Autowired
    ProjectMgmtRepo projectMgmtRepo;

    @Autowired
    GeneralSettingsRepo generalSettingsRepo;

    @Autowired
    private FinancialSummaryService financialSummaryService;

    //    helper function to convert received expense data to a map for easier access
    private Map<Integer, Double> convertProjectWiseExpenseToMap(List<ProjectWiseExpenseTotalEachTable> projectWiseExpenseTotalEachTableList, Set<Integer> setOfProjectIds) {
        Map<Integer, Double> expenseToMap = new HashMap<>();
        for (ProjectWiseExpenseTotalEachTable eachProject : projectWiseExpenseTotalEachTableList) {
            expenseToMap.put(eachProject.getProject_id(), eachProject.getExpense().doubleValue());

//            collect ids to retrieve names later
            setOfProjectIds.add(eachProject.getProject_id());
        }

        return expenseToMap;
    }

    private Double checkForNull(Double data) {
        if (data == null) {
            return 0d;
        } else {
            return data;
        }
    }

    public List<ProjectFinancialDetails> getProjectExpenseDetails(Integer financialYear) {

        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month month = Month.valueOf(startMonth.toUpperCase());
        LocalDate customStartDate = LocalDate.of(financialYear, month, startDateOfMonth);
        LocalDate customEndDate = customStartDate.plusYears(1).minusDays(1);

        Date startDate = java.sql.Date.valueOf(customStartDate);
        Date endDate = java.sql.Date.valueOf(customEndDate);


//        to collect all the project ids from financial data
        Set<Integer> setOfProjectIds = new HashSet<>();

//        other direct in-house cost budget project wise
//        String getBudgetProjectWiseDirectEmpQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getBudgetProjectWiseDirectEmpQuery.txt");
//
//        Query query1 = entityManager.createNativeQuery(getBudgetProjectWiseDirectEmpQuery).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
////        query1.setParameter("financialYear", financialYear);
//        query1.setParameter("startDate", startDate);
//        query1.setParameter("endDate", endDate);

//        List<ProjectWiseExpenseTotalEachTable> getBudgetProjectWiseDirectEmpData = query1.getResultList();

//        convert to map for easier access
//        Map<Integer, Double> budgetProjectWiseDirectEmpDataMap = convertProjectWiseExpenseToMap(getBudgetProjectWiseDirectEmpData, setOfProjectIds);
        Map<Integer, Double> budgetProjectWiseDirectEmpDataMap = new HashMap<>();


//        other direct in-house cost forecast project wise
        String getForecastProjectWiseDirectEmpQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getForecastProjectWiseDirectEmpQuery.txt");

        Query query2 = entityManager.createNativeQuery(getForecastProjectWiseDirectEmpQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query2).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
//        query2.setParameter("financialYear", financialYear);
        query2.setParameter("startDate", startDate);
        query2.setParameter("endDate", endDate);

        List<ProjectWiseExpenseTotalEachTable> getForecastProjectWiseDirectEmpData = query2.getResultList();

//        convert to map for easier access
        Map<Integer, Double> forecastProjectWiseDirectEmpDataMap = convertProjectWiseExpenseToMap(getForecastProjectWiseDirectEmpData, setOfProjectIds);


//        other direct in-house cost budget project wise
        String getBudgetProjectWiseOtherDirectQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getBudgetProjectWiseOtherDirectCostQuery.txt");

        Query query3 = entityManager.createNativeQuery(getBudgetProjectWiseOtherDirectQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
        query3.setParameter("financialYear", financialYear);

        List<ProjectWiseExpenseTotalEachTable> getBudgetProjectWiseOtherDirectData = query3.getResultList();

//        convert to map for easier access
        Map<Integer, Double> budgetProjectWiseOtherDirectDataMap = convertProjectWiseExpenseToMap(getBudgetProjectWiseOtherDirectData, setOfProjectIds);


//        other direct in-house cost forecast project wise
        String getForecastProjectWiseOtherDirectQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getForecastProjectWiseOtherDirectCostQuery.txt");

        Query query4 = entityManager.createNativeQuery(getForecastProjectWiseOtherDirectQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query4).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
        query4.setParameter("financialYear", financialYear);

        List<ProjectWiseExpenseTotalEachTable> getForecastProjectWiseOtherDirectData = query4.getResultList();

//        convert to map for easier access
        Map<Integer, Double> forecastProjectWiseOtherDirectDataMap = convertProjectWiseExpenseToMap(getForecastProjectWiseOtherDirectData, setOfProjectIds);


//        vendor emp details budget project wise
        String getBudgetProjectWiseVendorEmpQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getBudgetProjectWiseVendorEmpCostQuery.txt");

        Query query5 = entityManager.createNativeQuery(getBudgetProjectWiseVendorEmpQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query5).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
        query5.setParameter("financialYear", financialYear);

        List<ProjectWiseExpenseTotalEachTable> getBudgetProjectWiseVendorEmpData = query5.getResultList();

//        convert to map for easier access
        Map<Integer, Double> budgetProjectWiseVendorEmpDataMap = convertProjectWiseExpenseToMap(getBudgetProjectWiseVendorEmpData, setOfProjectIds);


//        vendor emp details forecast project wise
        String getForecastProjectWiseVendorEmpQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getForecastProjectWiseVendorEmpCostQuery.txt");

        Query query6 = entityManager.createNativeQuery(getForecastProjectWiseVendorEmpQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query6).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
        query6.setParameter("financialYear", financialYear);

        List<ProjectWiseExpenseTotalEachTable> getForecastProjectWiseVendorEmpData = query6.getResultList();

//        convert to map for easier access
        Map<Integer, Double> forecastProjectWiseVendorEmpDataMap = convertProjectWiseExpenseToMap(getForecastProjectWiseVendorEmpData, setOfProjectIds);


//        vendor related cost budget project wise
        String getBudgetProjectWiseVendorRelatedQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getBudgetProjectWiseVendorRelatedCostQuery.txt");

        Query query7 = entityManager.createNativeQuery(getBudgetProjectWiseVendorRelatedQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query7).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
        query7.setParameter("financialYear", financialYear);

        List<ProjectWiseExpenseTotalEachTable> getBudgetProjectWiseVendorRelatedData = query7.getResultList();

//        convert to map for easier access
        Map<Integer, Double> budgetProjectWiseVendorRelatedDataMap = convertProjectWiseExpenseToMap(getBudgetProjectWiseVendorRelatedData, setOfProjectIds);


//        vendor related cost forecast project wise
        String getForecastProjectWiseVendorRelatedQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getForecastProjectWiseVendorRelatedCostQuery.txt");

        Query query8 = entityManager.createNativeQuery(getForecastProjectWiseVendorRelatedQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query8).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
        query8.setParameter("financialYear", financialYear);

        List<ProjectWiseExpenseTotalEachTable> getForecastProjectWiseVendorRelatedData = query8.getResultList();

//        convert to map for easier access
        Map<Integer, Double> forecastProjectWiseVendorRelatedDataMap = convertProjectWiseExpenseToMap(getForecastProjectWiseVendorRelatedData, setOfProjectIds);


//        total capex expense budget project wise
        String getBudgetProjectWiseTotalCapexQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getBudgetProjectWiseTotalCapexCostQuery.txt");

        Query query9 = entityManager.createNativeQuery(getBudgetProjectWiseTotalCapexQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query9).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
        query9.setParameter("financialYear", financialYear);

        List<ProjectWiseExpenseTotalEachTable> getBudgetProjectWiseTotalCapexData = query9.getResultList();

//        convert to map for easier access
        Map<Integer, Double> budgetProjectWiseTotalCapexDataMap = convertProjectWiseExpenseToMap(getBudgetProjectWiseTotalCapexData, setOfProjectIds);


//        total capex expense forecast project wise
        String getForecastProjectWiseTotalCapexQuery = FinancialUtils.getFile("dashboard/projectvariancequery/getForecastProjectWiseTotalCapexCostQuery.txt");

        Query query10 = entityManager.createNativeQuery(getForecastProjectWiseTotalCapexQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query10).setResultTransformer(new AliasToBeanResultTransformer(ProjectWiseExpenseTotalEachTable.class));
        query10.setParameter("financialYear", financialYear);

        List<ProjectWiseExpenseTotalEachTable> getForecastProjectWiseTotalCapexData = query10.getResultList();

//        convert to map for easier access
        Map<Integer, Double> forecastProjectWiseTotalCapexDataMap = convertProjectWiseExpenseToMap(getForecastProjectWiseTotalCapexData, setOfProjectIds);


        List<ProjectIdName> projectIdNameList = projectMgmtRepo.getProjectNamesByIds(setOfProjectIds);
        //Map<Integer, String> projectIdNameMap = projectIdNameList.stream().collect(Collectors.toMap(ProjectIdName::getProjectId, ProjectIdName::getProjectName));

        List<ProjectFinancialDetails> allProjectFinanceDetails = new ArrayList<>();
        for (ProjectIdName eachProject : projectIdNameList) {

            ProjectFinancialDetails eachProjectDetail = new ProjectFinancialDetails();

            EachTableExpenseDetails projectBudget = new EachTableExpenseDetails();
            EachTableExpenseDetails projectForecast = new EachTableExpenseDetails();

            /*
            Here, variance is forecast - budget, as per BRD, if change is needed, please contact upper management
             */
            EachTableExpenseDetails projectVariance = new EachTableExpenseDetails();

            projectBudget.setDirectEmpDetails(checkForNull(budgetProjectWiseDirectEmpDataMap.get(eachProject.getProjectId())));
            projectForecast.setDirectEmpDetails(checkForNull(forecastProjectWiseDirectEmpDataMap.get(eachProject.getProjectId())));
            projectVariance.setDirectEmpDetails(projectForecast.getDirectEmpDetails() - projectBudget.getDirectEmpDetails());

            projectBudget.setOtherDirectCost(checkForNull(budgetProjectWiseOtherDirectDataMap.get(eachProject.getProjectId())));
            projectForecast.setOtherDirectCost(checkForNull(forecastProjectWiseOtherDirectDataMap.get(eachProject.getProjectId())));
            projectVariance.setOtherDirectCost(projectForecast.getOtherDirectCost() - projectBudget.getOtherDirectCost());

            projectBudget.setVendorEmpDetails(checkForNull(budgetProjectWiseVendorEmpDataMap.get(eachProject.getProjectId())));
            projectForecast.setVendorEmpDetails(checkForNull(forecastProjectWiseVendorEmpDataMap.get(eachProject.getProjectId())));
            projectVariance.setVendorEmpDetails(projectForecast.getVendorEmpDetails() - projectBudget.getVendorEmpDetails());

            projectBudget.setVendorRelatedCost(checkForNull(budgetProjectWiseVendorRelatedDataMap.get(eachProject.getProjectId())));
            projectForecast.setVendorRelatedCost(checkForNull(forecastProjectWiseVendorRelatedDataMap.get(eachProject.getProjectId())));
            projectVariance.setVendorRelatedCost(projectForecast.getVendorRelatedCost() - projectBudget.getVendorRelatedCost());

            projectBudget.setCapexRelatedExpense(checkForNull(budgetProjectWiseTotalCapexDataMap.get(eachProject.getProjectId())));
            projectForecast.setCapexRelatedExpense(checkForNull(forecastProjectWiseTotalCapexDataMap.get(eachProject.getProjectId())));
            projectVariance.setCapexRelatedExpense(projectForecast.getCapexRelatedExpense() - projectBudget.getCapexRelatedExpense());

            projectBudget.setTotal();
            projectForecast.setTotal();
            projectVariance.setTotal();

            eachProjectDetail.setProjectId(eachProject.getProjectId());
            eachProjectDetail.setProjectName(eachProject.getProjectName());
            eachProjectDetail.setBudgetDetails(projectBudget);
            eachProjectDetail.setForecastDetails(projectForecast);
            eachProjectDetail.setVarianceDetails(projectVariance);

            allProjectFinanceDetails.add(eachProjectDetail);
        }

        return allProjectFinanceDetails;
    }

    public List<GetTopProjectVariance> getTopProjectVariancesList(Integer financialYear) {

        List<ProjectFinancialDetails> projectFinancialDetailsList = getProjectExpenseDetails(financialYear);
        if (Objects.isNull(projectFinancialDetailsList)) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Data Not Found");
        }
        Collections.sort(projectFinancialDetailsList, Comparator.comparing(ProjectFinancialDetails -> ProjectFinancialDetails.getVarianceDetails().getTotalValue(), Comparator.reverseOrder()));

        int i = 0;
        List<GetTopProjectVariance> getTopProjectVariancesList = new ArrayList<>();
        for (ProjectFinancialDetails eachProject : projectFinancialDetailsList) {
            GetTopProjectVariance newProject = new GetTopProjectVariance();
            newProject.setProject_id(eachProject.getProjectId());
            newProject.setProject_name(eachProject.getProjectName());
            newProject.setBudget(eachProject.getBudgetDetails().getTotalValue());
            newProject.setForecast(eachProject.getForecastDetails().getTotalValue());
            newProject.setVariance(eachProject.getVarianceDetails().getTotalValue());
            getTopProjectVariancesList.add(newProject);
            i++;
            if (i == FinancialUtils.NO_OF_RECORDS_TO_SEND) {
                break;
            }
        }
        return getTopProjectVariancesList;
    }

    public List<GetProjectVarianceCostWaterfall> getProjectVariancesCostWaterfall(Integer financialYear) {

        List<ProjectFinancialDetails> projectFinancialDetailsList = getProjectExpenseDetails(financialYear);

        if (Objects.isNull(projectFinancialDetailsList)) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Data Not Found");
        }

        GetProjectVarianceCostWaterfall totalVarianceData = new GetProjectVarianceCostWaterfall(null, "Total", 0d, 0d, 0d, 0d, 0d, 0d, 0d);

        List<GetProjectVarianceCostWaterfall> getTopProjectVarianceCostWaterfallList = new ArrayList<>();
        for (ProjectFinancialDetails eachProject : projectFinancialDetailsList) {
            GetProjectVarianceCostWaterfall newProjectData = new GetProjectVarianceCostWaterfall();
            newProjectData.setProject_id(eachProject.getProjectId());
            newProjectData.setProject_name(eachProject.getProjectName());
            newProjectData.setVariance(eachProject.getVarianceDetails().getTotalValue());
            newProjectData.setBudget(eachProject.getBudgetDetails().getTotalValue());
            newProjectData.setForecast(eachProject.getForecastDetails().getTotalValue());
//            data is forecast - budget
            newProjectData.setInternalLabor(eachProject.getForecastDetails().getDirectEmpDetails() - eachProject.getBudgetDetails().getDirectEmpDetails());
            newProjectData.setExternalLabor(eachProject.getForecastDetails().getVendorEmpDetails() - eachProject.getBudgetDetails().getVendorEmpDetails());
            newProjectData.setCapex(eachProject.getForecastDetails().getCapexRelatedExpense() - eachProject.getBudgetDetails().getCapexRelatedExpense());
            newProjectData.setOpex((eachProject.getForecastDetails().getOtherDirectCost() + eachProject.getForecastDetails().getVendorRelatedCost())
                    - (eachProject.getBudgetDetails().getOtherDirectCost() + eachProject.getBudgetDetails().getVendorRelatedCost()));

//            add this to total data
            totalVarianceData.setVariance(FinancialUtils.roundToTwoDecimalPlace(totalVarianceData.getVariance() + newProjectData.getVariance()));
            totalVarianceData.setBudget(FinancialUtils.roundToTwoDecimalPlace(totalVarianceData.getBudget() + newProjectData.getBudget()));
            totalVarianceData.setForecast(FinancialUtils.roundToTwoDecimalPlace(totalVarianceData.getForecast() + newProjectData.getForecast()));
            totalVarianceData.setInternalLabor(FinancialUtils.roundToTwoDecimalPlace(totalVarianceData.getInternalLabor() + newProjectData.getInternalLabor()));
            totalVarianceData.setExternalLabor(FinancialUtils.roundToTwoDecimalPlace(totalVarianceData.getExternalLabor() + newProjectData.getExternalLabor()));
            totalVarianceData.setCapex(FinancialUtils.roundToTwoDecimalPlace(totalVarianceData.getCapex() + newProjectData.getCapex()));
            totalVarianceData.setOpex(FinancialUtils.roundToTwoDecimalPlace(totalVarianceData.getOpex() + newProjectData.getOpex()));

            getTopProjectVarianceCostWaterfallList.add(newProjectData);
        }

        Collections.sort(getTopProjectVarianceCostWaterfallList, Comparator.comparing(GetProjectVarianceCostWaterfall::getVariance));

        getTopProjectVarianceCostWaterfallList.add(totalVarianceData);

        return getTopProjectVarianceCostWaterfallList;
    }

    public List<GetUpcomingDeadlines> getUpcomingDeadlines() {

        String getUpcomingDeadlinesQuery = FinancialUtils.getFile("dashboard/getUpcomingDeadlinesQuery.txt");

        Query query = entityManager.createNativeQuery(getUpcomingDeadlinesQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetUpcomingDeadlines.class));

        List<GetUpcomingDeadlines> getUpcomingDeadlinesList = query.getResultList();

        if (getUpcomingDeadlinesList == null || getUpcomingDeadlinesList.isEmpty()) {
            getUpcomingDeadlinesList = null;
        }
        return getUpcomingDeadlinesList;
    }

    Double getQuarterDataSum(List<Double> data, int quarter) {
        Double sum = 0d;
        if (data == null || data.isEmpty()) {
            return null;
        }
        sum = sum + data.get((quarter * 3) - 1) + data.get((quarter * 3) - 2) + data.get((quarter * 3) - 3);
        return sum;
    }

    public List<GetBudgetVsForecast> getBudgetVsForecast(Integer financialYear) {

        List<GetBudgetVsForecast> getBudgetVsForecastList = new ArrayList<>();

        Month startMonth = Month.valueOf(generalSettingsRepo.getFinancialYearStartMonth().toUpperCase());

        LocalDate currentDate = LocalDate.now();
        String currentMonth = currentDate.getMonth().toString();
        Integer currentQuarter = null;

        Map<Integer, String> quarterMap = new HashMap<>();

        for (int i = 1; i <= 4; i++) {
            String month = startMonth.toString() + "," + startMonth.plus(1).toString() + "," + startMonth.plus(2).toString();
            if (month.contains(currentMonth)) {
                currentQuarter = i;
            }
            quarterMap.put(i, month);
            startMonth = startMonth.plus(3);
        }

        List<SummaryDataResultDto> summaryBudgetCurrentYear = financialSummaryService.getFinancialSummaryBudget(financialYear);
        List<SummaryDataResultDto> summaryForecastCurrentYear = financialSummaryService.getFinancialSummaryForecast(financialYear);

        List<Double> budgetCurrentYear, forecastCurrentYear;

        budgetCurrentYear = summaryBudgetCurrentYear.stream()
                .filter(it -> it.getDescription().equals(FinancialUtils.TOTAL_BUDGET_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
        forecastCurrentYear = summaryForecastCurrentYear.stream()
                .filter(it -> it.getDescription().equals(FinancialUtils.TOTAL_FORECAST_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());


        if (Objects.isNull(currentQuarter)) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Error in loop of quarterMap assignment or Database Error");
        }
        int currentQuarterCounter = currentQuarter;

        while(currentQuarterCounter!=0) {
            GetBudgetVsForecast thisQuarterBvF = new GetBudgetVsForecast();
            thisQuarterBvF.setBudget(getQuarterDataSum(budgetCurrentYear, currentQuarterCounter));
            thisQuarterBvF.setForecast(getQuarterDataSum(forecastCurrentYear, currentQuarterCounter));
            thisQuarterBvF.setQuarter("Q" + currentQuarterCounter);
            thisQuarterBvF.setYear(financialYear);
            getBudgetVsForecastList.add(thisQuarterBvF);
            currentQuarterCounter--;
        }

        if (currentQuarter < 4) {
            List<SummaryDataResultDto> summaryBudgetPreviousYear = financialSummaryService.getFinancialSummaryBudget(financialYear - 1);
            List<SummaryDataResultDto> summaryForecastPreviousYear = financialSummaryService.getFinancialSummaryForecast(financialYear - 1);
            List<Double> budgetPreviousYear, forecastPreviousYear;
            budgetPreviousYear = summaryBudgetPreviousYear.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.TOTAL_BUDGET_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
            forecastPreviousYear = summaryForecastPreviousYear.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.TOTAL_FORECAST_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            currentQuarterCounter+=4;

            while((currentQuarterCounter > currentQuarter)){
                GetBudgetVsForecast thisQuarterBvF = new GetBudgetVsForecast();
                thisQuarterBvF.setBudget(getQuarterDataSum(budgetPreviousYear, currentQuarterCounter));
                thisQuarterBvF.setForecast(getQuarterDataSum(forecastPreviousYear, currentQuarterCounter));
                thisQuarterBvF.setQuarter("Q" + currentQuarterCounter);
                thisQuarterBvF.setYear(financialYear-1);
                getBudgetVsForecastList.add(thisQuarterBvF);
                currentQuarterCounter--;
            }
        }

        if (getBudgetVsForecastList.isEmpty()) {
            getBudgetVsForecastList = null;
        }
        else{
            Collections.reverse(getBudgetVsForecastList);
        }
        return getBudgetVsForecastList;
    }

}
