package com.forecastera.service.financialmanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 06-07-2023
 * @Description
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.financialmanagement.dto.response.SummaryDataResultDto;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.SummaryDataResult;
import com.forecastera.service.financialmanagement.repository.GeneralSettingsRepo;
import com.forecastera.service.financialmanagement.util.FinancialUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class FinancialSummaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialService.class);

    @Autowired
    @Qualifier("edbObjectMapper")
    private ObjectMapper objectMapper;

    private final EntityManagerFactory emf;

    private final EntityManager entityManager;

    @Autowired
    GeneralSettingsRepo generalSettingsRepo;

    @Autowired
    public FinancialSummaryService(EntityManagerFactory emf, EntityManager entityManager) {
        this.emf = emf;
        this.entityManager = emf.createEntityManager();
    }

    //    to convert expense data to desired format
    public List<Double> setExpenseValues(String startMonth, SummaryDataResult summaryDataResult) {

        List<Double> expenseValuesOrdered = new ArrayList<>();

        List<Double> expenseValues = new ArrayList<>();

        if (summaryDataResult.getJan() != null) {
            expenseValues.add(summaryDataResult.getJan().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getFeb() != null) {
            expenseValues.add(summaryDataResult.getFeb().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getMar() != null) {
            expenseValues.add(summaryDataResult.getMar().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getApr() != null) {
            expenseValues.add(summaryDataResult.getApr().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getMay() != null) {
            expenseValues.add(summaryDataResult.getMay().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getJun() != null) {
            expenseValues.add(summaryDataResult.getJun().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getJul() != null) {
            expenseValues.add(summaryDataResult.getJul().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getAug() != null) {
            expenseValues.add(summaryDataResult.getAug().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getSep() != null) {
            expenseValues.add(summaryDataResult.getSep().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getOct() != null) {
            expenseValues.add(summaryDataResult.getOct().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getNov() != null) {
            expenseValues.add(summaryDataResult.getNov().doubleValue());
        } else {
            expenseValues.add(0d);
        }
        if (summaryDataResult.getDec() != null) {
            expenseValues.add(summaryDataResult.getDec().doubleValue());
        } else {
            expenseValues.add(0d);
        }

        int i = FinancialUtils.MONTH_MAP.get(startMonth) - 1;

        do {
            expenseValuesOrdered.add(expenseValues.get(i));
            i++;
            if (i == 12) {
                i = 0;
            }
        } while (i != FinancialUtils.MONTH_MAP.get(startMonth) - 1);


        return expenseValuesOrdered;
    }

    public List<LinkedHashMap<String, Object>> getSummaryDirectEmployeeData(Integer financialYear, String financialYearStartMonth, String queryTextFile, String workLocation) {

//        List<GeneralSettings> generalSettings = generalSettingsRepo.findAll();
        final List<LinkedHashMap<String, Object>> objToMap = new ArrayList<>();

//        if (!generalSettings.isEmpty()) {
        if (!financialYearStartMonth.isEmpty()) {

//            String month = generalSettings.get(0).getFinancialYearStart();

            LocalDate month1 = LocalDate.of(financialYear, Month.valueOf(financialYearStartMonth.toUpperCase()), 01);
            String m1 = month1.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month1.getYear();

            LocalDate month2 = month1.plusMonths(1);
            String m2 = month2.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month2.getYear();

            LocalDate month3 = month2.plusMonths(1);
            String m3 = month3.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month3.getYear();

            LocalDate month4 = month3.plusMonths(1);
            String m4 = month4.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month4.getYear();

            LocalDate month5 = month4.plusMonths(1);
            String m5 = month5.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month5.getYear();

            LocalDate month6 = month5.plusMonths(1);
            String m6 = month6.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month6.getYear();

            LocalDate month7 = month6.plusMonths(1);
            String m7 = month7.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month7.getYear();

            LocalDate month8 = month7.plusMonths(1);
            String m8 = month8.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month8.getYear();

            LocalDate month9 = month8.plusMonths(1);
            String m9 = month9.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month9.getYear();

            LocalDate month10 = month9.plusMonths(1);
            String m10 = month10.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month10.getYear();

            LocalDate month11 = month10.plusMonths(1);
            String m11 = month11.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month11.getYear();

            LocalDate month12 = month11.plusMonths(1);
            String m12 = month12.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "-" + month12.getYear();


            String capexRelatedExpenseForecastQuery = FinancialUtils.getFile(queryTextFile);
            String b = capexRelatedExpenseForecastQuery
                    .replace("##month1##", "'" + month1.toString() + "'")
                    .replace("##m1##", "\"" + m1 + "\"")

                    .replace("##month2##", "'" + month2.toString() + "'")
                    .replace("##m2##", "\"" + m2 + "\"")

                    .replace("##month3##", "'" + month3.toString() + "'")
                    .replace("##m3##", "\"" + m3 + "\"")

                    .replace("##month4##", "'" + month4.toString() + "'")
                    .replace("##m4##", "\"" + m4 + "\"")

                    .replace("##month5##", "'" + month5.toString() + "'")
                    .replace("##m5##", "\"" + m5 + "\"")

                    .replace("##month6##", "'" + month6.toString() + "'")
                    .replace("##m6##", "\"" + m6 + "\"")

                    .replace("##month7##", "'" + month7.toString() + "'")
                    .replace("##m7##", "\"" + m7 + "\"")

                    .replace("##month8##", "'" + month8.toString() + "'")
                    .replace("##m8##", "\"" + m8 + "\"")

                    .replace("##month9##", "'" + month9.toString() + "'")
                    .replace("##m9##", "\"" + m9 + "\"")

                    .replace("##month10##", "'" + month10.toString() + "'")
                    .replace("##m10##", "\"" + m10 + "\"")

                    .replace("##month11##", "'" + month11.toString() + "'")
                    .replace("##m11##", "\"" + m11 + "\"")

                    .replace("##month12##", "'" + month12.toString() + "'")
                    .replace("##m12##", "\"" + m12 + "\"");

    /*Query query = entityManager.createNativeQuery(b).unwrap(org.hibernate.query.Query.class);
    ((NativeQueryImpl) query).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);*/

            Query query = entityManager.createNativeQuery(b, Tuple.class);
            query.setParameter("financialYear", financialYear);
            query.setParameter("workLocation", workLocation);

            final List<Tuple> queryRows = query.getResultList();
            queryRows.forEach(row -> {
                final LinkedHashMap<String, Object> formattedRow = new LinkedHashMap<>();
                AtomicReference<Integer> nullCount = new AtomicReference<>(0);

                row.getElements().forEach(column -> {

                    final String columnName = column.getAlias();
                    final Object columnValue = row.get(column);
                    if (ObjectUtils.isEmpty(columnValue)) {
                        nullCount.getAndSet(nullCount.get() + 1);
                    }
                    formattedRow.put(columnName, columnValue);
                });

                if (!ObjectUtils.isEmpty(formattedRow) && nullCount.get() != row.getElements().size()) {
                    objToMap.add(formattedRow);
                }

            });
        }
        return objToMap.isEmpty() ? null : objToMap;
    }


    public List<Double> setExpenseValuesFromListOfLinkedHashMap(List<LinkedHashMap<String, Object>> expenseDataResult) {
        List<Double> expenseValuesOrdered = new ArrayList<>();

        for (LinkedHashMap<String, Object> eachList : expenseDataResult) {
            for (Map.Entry<String, Object> entry : eachList.entrySet()) {
                expenseValuesOrdered.add(FinancialUtils.roundToTwoDecimalPlace(((BigInteger) entry.getValue()).doubleValue()));
            }
        }

        if (expenseValuesOrdered.isEmpty()) {
            expenseValuesOrdered = null;
        }

        return expenseValuesOrdered;
    }
    /*
    Financial Summary Internal Labor Expense
     */

    //    Summary Budget
    public List<SummaryDataResultDto> getFinancialSummaryBudgetInternalLabor(Integer financialYear) {
//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> summaryBudgetInternalLaborExpense = new ArrayList<>();


//        get summary budget Internal Onsite data
        String internalOnsiteBudgetQuery = "summary/budget/getSummaryBudgetInternalDirectEmp.txt";

        List<LinkedHashMap<String, Object>> getInternalOnsiteBudgetDetails = getSummaryDirectEmployeeData(financialYear, startMonth, internalOnsiteBudgetQuery, FinancialUtils.ONSITE);

        if (getInternalOnsiteBudgetDetails != null && !getInternalOnsiteBudgetDetails.isEmpty()) {
//            if () {
            SummaryDataResultDto budgetInternalOnsite = new SummaryDataResultDto();
            budgetInternalOnsite.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOnsite.setDescription(FinancialUtils.ONSITE);
            budgetInternalOnsite.setSubCategory(null);
            budgetInternalOnsite.setIsAggregate(false);
            budgetInternalOnsite.setData(setExpenseValuesFromListOfLinkedHashMap(getInternalOnsiteBudgetDetails));
            summaryBudgetInternalLaborExpense.add(budgetInternalOnsite);
//            }
        } else {
            SummaryDataResultDto budgetInternalOnsite = new SummaryDataResultDto();
            budgetInternalOnsite.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOnsite.setDescription(FinancialUtils.ONSITE);
            budgetInternalOnsite.setSubCategory(null);
            budgetInternalOnsite.setIsAggregate(false);
            budgetInternalOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryBudgetInternalLaborExpense.add(budgetInternalOnsite);
        }


//        get summary budget Internal Offshore data
        String internalOffshoreBudgetQuery = "summary/budget/getSummaryBudgetInternalDirectEmp.txt";

        List<LinkedHashMap<String, Object>> getInternalOffshoreBudgetDetails = getSummaryDirectEmployeeData(financialYear, startMonth, internalOffshoreBudgetQuery, FinancialUtils.OFFSHORE);

        if (getInternalOffshoreBudgetDetails != null && !getInternalOffshoreBudgetDetails.isEmpty()) {
//            if () {
            SummaryDataResultDto budgetInternalOffshore = new SummaryDataResultDto();
            budgetInternalOffshore.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOffshore.setDescription(FinancialUtils.OFFSHORE);
            budgetInternalOffshore.setSubCategory(null);
            budgetInternalOffshore.setIsAggregate(false);
            budgetInternalOffshore.setData(setExpenseValuesFromListOfLinkedHashMap(getInternalOffshoreBudgetDetails));
            summaryBudgetInternalLaborExpense.add(budgetInternalOffshore);
//            }
        } else {
            SummaryDataResultDto budgetInternalOffshore = new SummaryDataResultDto();
            budgetInternalOffshore.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOffshore.setDescription(FinancialUtils.OFFSHORE);
            budgetInternalOffshore.setSubCategory(null);
            budgetInternalOffshore.setIsAggregate(false);
            budgetInternalOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryBudgetInternalLaborExpense.add(budgetInternalOffshore);
        }

        // Total Employee Salaries
        if (summaryBudgetInternalLaborExpense != null && !summaryBudgetInternalLaborExpense.isEmpty()) {
            List<Double> internalLaborExpensesOnsiteData = summaryBudgetInternalLaborExpense.stream()
                    .filter(it -> it.getTableType().equals(FinancialUtils.INTERNAL_LABOR_EXPENSES)
                            && it.getDescription().equals(FinancialUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> internalLaborExpensesOffshoreData = summaryBudgetInternalLaborExpense.stream()
                    .filter(it -> it.getTableType().equals(FinancialUtils.INTERNAL_LABOR_EXPENSES)
                            && it.getDescription().equals(FinancialUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = 0; i <= 11; i++) {
                Double total = internalLaborExpensesOnsiteData.get(i) + internalLaborExpensesOffshoreData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto budgetInternalEmployeeAggregate = new SummaryDataResultDto();
            budgetInternalEmployeeAggregate.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalEmployeeAggregate.setDescription(FinancialUtils.TOTAL_EMPLOYEE_SALARIES);
            budgetInternalEmployeeAggregate.setSubCategory(null);
            budgetInternalEmployeeAggregate.setData(totalEmployeeSalariesList);
            budgetInternalEmployeeAggregate.setIsAggregate(true);
            summaryBudgetInternalLaborExpense.add(budgetInternalEmployeeAggregate);
        }

//        get summary budget Internal other direct in-house data
        String internalOtherDirectBudgetQuery = FinancialUtils.getFile("summary/budget/getSummaryBudgetInternalOtherDirect.txt");

        Query query3 = entityManager.createNativeQuery(internalOtherDirectBudgetQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query3.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getInternalOtherDirectBudgetDetails = query3.getResultList();

        if (!getInternalOtherDirectBudgetDetails.isEmpty()) {
            for (SummaryDataResult eachResult : getInternalOtherDirectBudgetDetails) {
                SummaryDataResultDto budgetInternalOtherDirect = new SummaryDataResultDto();
                budgetInternalOtherDirect.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
                budgetInternalOtherDirect.setDescription(eachResult.getTitle());
                budgetInternalOtherDirect.setSubCategory(null);
                budgetInternalOtherDirect.setIsAggregate(false);
                budgetInternalOtherDirect.setData(setExpenseValues(startMonth, eachResult));
                summaryBudgetInternalLaborExpense.add(budgetInternalOtherDirect);
            }
        }
        else{
            SummaryDataResultDto budgetInternalOtherDirect = new SummaryDataResultDto();
            budgetInternalOtherDirect.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOtherDirect.setDescription(FinancialUtils.EMPTY_INTERNAL_OTHER_EXPENSES);
            budgetInternalOtherDirect.setSubCategory(null);
            budgetInternalOtherDirect.setIsAggregate(false);
            budgetInternalOtherDirect.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryBudgetInternalLaborExpense.add(budgetInternalOtherDirect);
        }

        // Total Internal Labor Expenses
        if (summaryBudgetInternalLaborExpense != null && !summaryBudgetInternalLaborExpense.isEmpty()) {
            List<SummaryDataResultDto> summaryBudgetInternalLaborExpenseOtherList = summaryBudgetInternalLaborExpense.stream()
                    .filter(it -> !it.getDescription().equals(FinancialUtils.ONSITE)
                            && !it.getDescription().equals(FinancialUtils.OFFSHORE)
                            && !it.getDescription().equals(FinancialUtils.TOTAL_EMPLOYEE_SALARIES))
                    .collect(Collectors.toList());

            List<Double> summaryBudgetInternalLaborExpenseTotalList = summaryBudgetInternalLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.TOTAL_EMPLOYEE_SALARIES))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> totalInternalLaborExpensesTotalList = new ArrayList<>(summaryBudgetInternalLaborExpenseTotalList);

            for (SummaryDataResultDto summaryDataResultDto : summaryBudgetInternalLaborExpenseOtherList) {
                List<Double> dataList = summaryDataResultDto.getData();
                for (int i = 0; i <= 11; i++) {
                    totalInternalLaborExpensesTotalList.set(i, totalInternalLaborExpensesTotalList.get(i) + dataList.get(i));
                }
            }
            SummaryDataResultDto budgetInternalTotalAggregate = new SummaryDataResultDto();
            budgetInternalTotalAggregate.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalTotalAggregate.setDescription(FinancialUtils.TOTAL_INTERNAL_LABOR_EXPENSES);
            budgetInternalTotalAggregate.setSubCategory(null);
            budgetInternalTotalAggregate.setIsAggregate(true);
            budgetInternalTotalAggregate.setData(totalInternalLaborExpensesTotalList);
            summaryBudgetInternalLaborExpense.add(budgetInternalTotalAggregate);
        }

        if (summaryBudgetInternalLaborExpense.isEmpty()) {
            summaryBudgetInternalLaborExpense = null;
        }

        return summaryBudgetInternalLaborExpense;
    }

    //    Summary Forecast
    public List<SummaryDataResultDto> getFinancialSummaryForecastInternalLabor(Integer financialYear) {
        //        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> summaryForecastInternalLaborExpense = new ArrayList<>();

//        get summary forecast Internal Onsite data
        String internalOnsiteForecastQuery = "summary/forecast/getSummaryForecastInternalDirectEmp.txt";

        List<LinkedHashMap<String, Object>> getInternalOnsiteForecastDetails = getSummaryDirectEmployeeData(financialYear, startMonth, internalOnsiteForecastQuery, FinancialUtils.ONSITE);

        if (getInternalOnsiteForecastDetails != null && !getInternalOnsiteForecastDetails.isEmpty()) {
//            if(){
            SummaryDataResultDto forecastInternalOnsite = new SummaryDataResultDto();
            forecastInternalOnsite.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOnsite.setDescription(FinancialUtils.ONSITE);
            forecastInternalOnsite.setSubCategory(null);
            forecastInternalOnsite.setIsAggregate(false);
            forecastInternalOnsite.setData(setExpenseValuesFromListOfLinkedHashMap(getInternalOnsiteForecastDetails));
            summaryForecastInternalLaborExpense.add(forecastInternalOnsite);
//            }
        } else {
            SummaryDataResultDto forecastInternalOnsite = new SummaryDataResultDto();
            forecastInternalOnsite.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOnsite.setDescription(FinancialUtils.ONSITE);
            forecastInternalOnsite.setSubCategory(null);
            forecastInternalOnsite.setIsAggregate(false);
            forecastInternalOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryForecastInternalLaborExpense.add(forecastInternalOnsite);
        }


//        get summary forecast Internal Offshore data
        String internalOffshoreForecastQuery = "summary/forecast/getSummaryForecastInternalDirectEmp.txt";

        List<LinkedHashMap<String, Object>> getInternalOffshoreForecastDetails = getSummaryDirectEmployeeData(financialYear, startMonth, internalOffshoreForecastQuery, FinancialUtils.OFFSHORE);

        if (getInternalOffshoreForecastDetails != null && !getInternalOffshoreForecastDetails.isEmpty()) {
//            if (!getInternalOffshoreForecastDetails.isEmpty()) {
            SummaryDataResultDto forecastInternalOffshore = new SummaryDataResultDto();
            forecastInternalOffshore.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOffshore.setDescription(FinancialUtils.OFFSHORE);
            forecastInternalOffshore.setSubCategory(null);
            forecastInternalOffshore.setIsAggregate(false);
            forecastInternalOffshore.setData(setExpenseValuesFromListOfLinkedHashMap(getInternalOffshoreForecastDetails));
            summaryForecastInternalLaborExpense.add(forecastInternalOffshore);
//            }
        } else {
            SummaryDataResultDto forecastInternalOffshore = new SummaryDataResultDto();
            forecastInternalOffshore.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOffshore.setDescription(FinancialUtils.OFFSHORE);
            forecastInternalOffshore.setSubCategory(null);
            forecastInternalOffshore.setIsAggregate(false);
            forecastInternalOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryForecastInternalLaborExpense.add(forecastInternalOffshore);
        }

        // Total Employee Salaries
        if (summaryForecastInternalLaborExpense != null && !summaryForecastInternalLaborExpense.isEmpty()) {
            List<Double> internalLaborExpensesOnsiteData = summaryForecastInternalLaborExpense.stream()
                    .filter(it -> it.getTableType().equals(FinancialUtils.INTERNAL_LABOR_EXPENSES)
                            && it.getDescription().equals(FinancialUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> internalLaborExpensesOffshoreData = summaryForecastInternalLaborExpense.stream()
                    .filter(it -> it.getTableType().equals(FinancialUtils.INTERNAL_LABOR_EXPENSES)
                            && it.getDescription().equals(FinancialUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = 0; i <= 11; i++) {
                Double total = internalLaborExpensesOnsiteData.get(i) + internalLaborExpensesOffshoreData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto forecastInternalEmployeeAggregate = new SummaryDataResultDto();
            forecastInternalEmployeeAggregate.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalEmployeeAggregate.setDescription(FinancialUtils.TOTAL_EMPLOYEE_SALARIES);
            forecastInternalEmployeeAggregate.setSubCategory(null);
            forecastInternalEmployeeAggregate.setData(totalEmployeeSalariesList);
            forecastInternalEmployeeAggregate.setIsAggregate(true);
            summaryForecastInternalLaborExpense.add(forecastInternalEmployeeAggregate);
        }

//        get summary forecast Internal other direct in-house data
        String internalOtherDirectForecastQuery = FinancialUtils.getFile("summary/forecast/getSummaryForecastInternalOtherDirect.txt");

        Query query3 = entityManager.createNativeQuery(internalOtherDirectForecastQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query3.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getInternalOtherDirectForecastDetails = query3.getResultList();

        if (!getInternalOtherDirectForecastDetails.isEmpty()) {
            for (SummaryDataResult eachResult : getInternalOtherDirectForecastDetails) {
                SummaryDataResultDto forecastInternalOtherDirect = new SummaryDataResultDto();
                forecastInternalOtherDirect.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
                forecastInternalOtherDirect.setDescription(eachResult.getTitle());
                forecastInternalOtherDirect.setSubCategory(null);
                forecastInternalOtherDirect.setIsAggregate(false);
                forecastInternalOtherDirect.setData(setExpenseValues(startMonth, eachResult));
                summaryForecastInternalLaborExpense.add(forecastInternalOtherDirect);
            }
        }
        else{
            SummaryDataResultDto forecastInternalOtherDirect = new SummaryDataResultDto();
            forecastInternalOtherDirect.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOtherDirect.setDescription(FinancialUtils.EMPTY_INTERNAL_OTHER_EXPENSES);
            forecastInternalOtherDirect.setSubCategory(null);
            forecastInternalOtherDirect.setIsAggregate(false);
            forecastInternalOtherDirect.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryForecastInternalLaborExpense.add(forecastInternalOtherDirect);
        }

        // Total Internal Labor Expenses
        if (summaryForecastInternalLaborExpense != null && !summaryForecastInternalLaborExpense.isEmpty()) {
            List<SummaryDataResultDto> summaryForecastInternalLaborExpenseOtherList = summaryForecastInternalLaborExpense.stream()
                    .filter(it -> !it.getDescription().equals(FinancialUtils.ONSITE)
                            && !it.getDescription().equals(FinancialUtils.OFFSHORE)
                            && !it.getDescription().equals(FinancialUtils.TOTAL_EMPLOYEE_SALARIES))
                    .collect(Collectors.toList());

            List<Double> summaryForecastInternalLaborExpenseTotalList = summaryForecastInternalLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.TOTAL_EMPLOYEE_SALARIES))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> totalInternalLaborExpensesTotalList = new ArrayList<>(summaryForecastInternalLaborExpenseTotalList);

            for (SummaryDataResultDto summaryDataResultDto : summaryForecastInternalLaborExpenseOtherList) {
                List<Double> dataList = summaryDataResultDto.getData();
                for (int i = 0; i <= 11; i++) {
                    totalInternalLaborExpensesTotalList.set(i, totalInternalLaborExpensesTotalList.get(i) + dataList.get(i));
                }
            }
            SummaryDataResultDto forecastInternalTotalAggregate = new SummaryDataResultDto();
            forecastInternalTotalAggregate.setTableType(FinancialUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalTotalAggregate.setDescription(FinancialUtils.TOTAL_INTERNAL_LABOR_EXPENSES);
            forecastInternalTotalAggregate.setSubCategory(null);
            forecastInternalTotalAggregate.setIsAggregate(true);
            forecastInternalTotalAggregate.setData(totalInternalLaborExpensesTotalList);
            summaryForecastInternalLaborExpense.add(forecastInternalTotalAggregate);
        }

        if (summaryForecastInternalLaborExpense.isEmpty()) {
            summaryForecastInternalLaborExpense = null;
        }

        return summaryForecastInternalLaborExpense;
    }


    /*
    Financial Summary Vendor Labor Expense
     */

    //    Summary Budget
    public List<SummaryDataResultDto> getFinancialSummaryBudgetVendorLabor(Integer financialYear) {

//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> summaryBudgetVendorLaborExpense = new ArrayList<>();

//        get summary budget vendor T&M Onsite data
        String vendorBudgetTAndMOnsiteQuery = FinancialUtils.getFile("summary/budget/getSummaryBudgetVendorTAndMOnsite.txt");

        Query query1 = entityManager.createNativeQuery(vendorBudgetTAndMOnsiteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query1.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getVendorBudgetTAndMOnsiteDetails = query1.getResultList();

        if (getVendorBudgetTAndMOnsiteDetails != null && !getVendorBudgetTAndMOnsiteDetails.isEmpty()) {
            SummaryDataResultDto budgetVendorTAndMOnsite = new SummaryDataResultDto();
            budgetVendorTAndMOnsite.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTAndMOnsite.setDescription(FinancialUtils.T_AND_M);
            budgetVendorTAndMOnsite.setSubCategory(FinancialUtils.ONSITE);
            budgetVendorTAndMOnsite.setIsAggregate(false);
            budgetVendorTAndMOnsite.setData(setExpenseValues(startMonth, getVendorBudgetTAndMOnsiteDetails.get(0)));
            summaryBudgetVendorLaborExpense.add(budgetVendorTAndMOnsite);
        } else {
            SummaryDataResultDto budgetVendorTAndMOnsite = new SummaryDataResultDto();
            budgetVendorTAndMOnsite.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTAndMOnsite.setDescription(FinancialUtils.T_AND_M);
            budgetVendorTAndMOnsite.setSubCategory(FinancialUtils.ONSITE);
            budgetVendorTAndMOnsite.setIsAggregate(false);
            budgetVendorTAndMOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryBudgetVendorLaborExpense.add(budgetVendorTAndMOnsite);
        }


//        get summary budget vendor Other Expense Onsite data
        String vendorBudgetOtherExpenseOnsiteQuery = FinancialUtils.getFile("summary/budget/getSummaryBudgetVendorOtherOnsite.txt");

        Query query2 = entityManager.createNativeQuery(vendorBudgetOtherExpenseOnsiteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query2).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query2.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getVendorBudgetOtherExpenseOnsiteDetails = query2.getResultList();

        if (getVendorBudgetOtherExpenseOnsiteDetails != null && !getVendorBudgetOtherExpenseOnsiteDetails.isEmpty()) {
            SummaryDataResultDto budgetVendorOtherExpenseOnsite = new SummaryDataResultDto();
            budgetVendorOtherExpenseOnsite.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOtherExpenseOnsite.setDescription(FinancialUtils.OTHER_VENDOR_EXPENSE);
            budgetVendorOtherExpenseOnsite.setSubCategory(FinancialUtils.ONSITE);
            budgetVendorOtherExpenseOnsite.setIsAggregate(false);
            budgetVendorOtherExpenseOnsite.setData(setExpenseValues(startMonth, getVendorBudgetOtherExpenseOnsiteDetails.get(0)));
            summaryBudgetVendorLaborExpense.add(budgetVendorOtherExpenseOnsite);
        } else {
            SummaryDataResultDto budgetVendorOtherExpenseOnsite = new SummaryDataResultDto();
            budgetVendorOtherExpenseOnsite.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOtherExpenseOnsite.setDescription(FinancialUtils.OTHER_VENDOR_EXPENSE);
            budgetVendorOtherExpenseOnsite.setSubCategory(FinancialUtils.ONSITE);
            budgetVendorOtherExpenseOnsite.setIsAggregate(false);
            budgetVendorOtherExpenseOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryBudgetVendorLaborExpense.add(budgetVendorOtherExpenseOnsite);
        }

        // Total onsite vendor Salaries
        if (summaryBudgetVendorLaborExpense != null && !summaryBudgetVendorLaborExpense.isEmpty()) {
            List<Double> vendorLaborExpensesOnsiteTAndMData = summaryBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.T_AND_M)
                            && it.getSubCategory().equals(FinancialUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> vendorLaborExpensesOnsiteOtherData = summaryBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.OTHER_VENDOR_EXPENSE)
                            && it.getSubCategory().equals(FinancialUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = 0; i <= 11; i++) {
                Double total = vendorLaborExpensesOnsiteTAndMData.get(i) + vendorLaborExpensesOnsiteOtherData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto budgetVendorOnsiteAggregate = new SummaryDataResultDto();
            budgetVendorOnsiteAggregate.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOnsiteAggregate.setDescription(FinancialUtils.ONSITE);
            budgetVendorOnsiteAggregate.setSubCategory(null);
            budgetVendorOnsiteAggregate.setData(totalEmployeeSalariesList);
            budgetVendorOnsiteAggregate.setIsAggregate(true);
            summaryBudgetVendorLaborExpense.add(budgetVendorOnsiteAggregate);
        }

//        get summary budget vendor T&M Offshore data
        String vendorBudgetTAndMOffshoreQuery = FinancialUtils.getFile("summary/budget/getSummaryBudgetVendorTAndMOffshore.txt");

        Query query3 = entityManager.createNativeQuery(vendorBudgetTAndMOffshoreQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query3.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getVendorBudgetTAndMOffshoreDetails = query3.getResultList();

        if (getVendorBudgetTAndMOffshoreDetails != null && !getVendorBudgetTAndMOffshoreDetails.isEmpty()) {
            SummaryDataResultDto budgetVendorTAndMOffshore = new SummaryDataResultDto();
            budgetVendorTAndMOffshore.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTAndMOffshore.setDescription(FinancialUtils.T_AND_M);
            budgetVendorTAndMOffshore.setSubCategory(FinancialUtils.OFFSHORE);
            budgetVendorTAndMOffshore.setIsAggregate(false);
            budgetVendorTAndMOffshore.setData(setExpenseValues(startMonth, getVendorBudgetTAndMOffshoreDetails.get(0)));
            summaryBudgetVendorLaborExpense.add(budgetVendorTAndMOffshore);
        } else {
            SummaryDataResultDto budgetVendorTAndMOffshore = new SummaryDataResultDto();
            budgetVendorTAndMOffshore.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTAndMOffshore.setDescription(FinancialUtils.T_AND_M);
            budgetVendorTAndMOffshore.setSubCategory(FinancialUtils.OFFSHORE);
            budgetVendorTAndMOffshore.setIsAggregate(false);
            budgetVendorTAndMOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryBudgetVendorLaborExpense.add(budgetVendorTAndMOffshore);
        }


//        get summary budget vendor Other Expense Offshore data
        String vendorBudgetOtherExpenseOffshoreQuery = FinancialUtils.getFile("summary/budget/getSummaryBudgetVendorOtherOffshore.txt");

        Query query4 = entityManager.createNativeQuery(vendorBudgetOtherExpenseOffshoreQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query4).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query4.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getVendorBudgetOtherExpenseOffshoreDetails = query4.getResultList();

        if (getVendorBudgetOtherExpenseOffshoreDetails != null && !getVendorBudgetOtherExpenseOffshoreDetails.isEmpty()) {
            SummaryDataResultDto budgetVendorOtherExpenseOffshore = new SummaryDataResultDto();
            budgetVendorOtherExpenseOffshore.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOtherExpenseOffshore.setDescription(FinancialUtils.OTHER_VENDOR_EXPENSE);
            budgetVendorOtherExpenseOffshore.setSubCategory(FinancialUtils.OFFSHORE);
            budgetVendorOtherExpenseOffshore.setIsAggregate(false);
            budgetVendorOtherExpenseOffshore.setData(setExpenseValues(startMonth, getVendorBudgetOtherExpenseOffshoreDetails.get(0)));
            summaryBudgetVendorLaborExpense.add(budgetVendorOtherExpenseOffshore);
        } else {
            SummaryDataResultDto budgetVendorOtherExpenseOffshore = new SummaryDataResultDto();
            budgetVendorOtherExpenseOffshore.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOtherExpenseOffshore.setDescription(FinancialUtils.OTHER_VENDOR_EXPENSE);
            budgetVendorOtherExpenseOffshore.setSubCategory(FinancialUtils.OFFSHORE);
            budgetVendorOtherExpenseOffshore.setIsAggregate(false);
            budgetVendorOtherExpenseOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryBudgetVendorLaborExpense.add(budgetVendorOtherExpenseOffshore);
        }

        // Total offshore vendor Salaries
        if (summaryBudgetVendorLaborExpense != null && !summaryBudgetVendorLaborExpense.isEmpty()) {
            List<Double> vendorLaborExpensesOffshoreTAndMData = summaryBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.T_AND_M)
                            && it.getSubCategory().equals(FinancialUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> vendorLaborExpensesOffshoreOtherData = summaryBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.OTHER_VENDOR_EXPENSE)
                            && it.getSubCategory().equals(FinancialUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = 0; i <= 11; i++) {
                Double total = vendorLaborExpensesOffshoreTAndMData.get(i) + vendorLaborExpensesOffshoreOtherData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto budgetVendorOffshoreAggregate = new SummaryDataResultDto();
            budgetVendorOffshoreAggregate.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOffshoreAggregate.setDescription(FinancialUtils.OFFSHORE);
            budgetVendorOffshoreAggregate.setSubCategory(null);
            budgetVendorOffshoreAggregate.setData(totalEmployeeSalariesList);
            budgetVendorOffshoreAggregate.setIsAggregate(true);
            summaryBudgetVendorLaborExpense.add(budgetVendorOffshoreAggregate);
        }

        // Total Vendor Expenses
        if (summaryBudgetVendorLaborExpense != null && !summaryBudgetVendorLaborExpense.isEmpty()) {
            List<Double> summaryBudgetOnsiteAggregatedData = summaryBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.ONSITE))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> summaryBudgetOffshoreAggregatedData = summaryBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.OFFSHORE))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> totalVendorLaborExpensesTotalList = new ArrayList<>();


            for (int i = 0; i <= 11; i++) {
                totalVendorLaborExpensesTotalList.add(summaryBudgetOnsiteAggregatedData.get(i) + summaryBudgetOffshoreAggregatedData.get(i));
            }

            SummaryDataResultDto budgetVendorTotalAggregate = new SummaryDataResultDto();
            budgetVendorTotalAggregate.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTotalAggregate.setDescription(FinancialUtils.TOTAL_VENDOR_LABOR_EXPENSES);
            budgetVendorTotalAggregate.setSubCategory(null);
            budgetVendorTotalAggregate.setIsAggregate(true);
            budgetVendorTotalAggregate.setData(totalVendorLaborExpensesTotalList);
            summaryBudgetVendorLaborExpense.add(budgetVendorTotalAggregate);
        }

        if (summaryBudgetVendorLaborExpense.isEmpty()) {
            summaryBudgetVendorLaborExpense = null;
        }

        return summaryBudgetVendorLaborExpense;
    }

    //    Summary Forecast
    public List<SummaryDataResultDto> getFinancialSummaryForecastVendorLabor(Integer financialYear) {

//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();


        List<SummaryDataResultDto> summaryForecastVendorLaborExpense = new ArrayList<>();

//        get summary forecast vendor T&M Onsite data
        String vendorForecastTAndMOnsiteQuery = FinancialUtils.getFile("summary/forecast/getSummaryForecastVendorTAndMOnsite.txt");

        Query query1 = entityManager.createNativeQuery(vendorForecastTAndMOnsiteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query1.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getVendorForecastTAndMOnsiteDetails = query1.getResultList();

        if (getVendorForecastTAndMOnsiteDetails != null && !getVendorForecastTAndMOnsiteDetails.isEmpty()) {
            SummaryDataResultDto forecastVendorTAndMOnsite = new SummaryDataResultDto();
            forecastVendorTAndMOnsite.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTAndMOnsite.setDescription(FinancialUtils.T_AND_M);
            forecastVendorTAndMOnsite.setSubCategory(FinancialUtils.ONSITE);
            forecastVendorTAndMOnsite.setIsAggregate(false);
            forecastVendorTAndMOnsite.setData(setExpenseValues(startMonth, getVendorForecastTAndMOnsiteDetails.get(0)));
            summaryForecastVendorLaborExpense.add(forecastVendorTAndMOnsite);
        } else {
            SummaryDataResultDto forecastVendorTAndMOnsite = new SummaryDataResultDto();
            forecastVendorTAndMOnsite.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTAndMOnsite.setDescription(FinancialUtils.T_AND_M);
            forecastVendorTAndMOnsite.setSubCategory(FinancialUtils.ONSITE);
            forecastVendorTAndMOnsite.setIsAggregate(false);
            forecastVendorTAndMOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryForecastVendorLaborExpense.add(forecastVendorTAndMOnsite);
        }


//        get summary forecast vendor Other Expense Onsite data
        String vendorForecastOtherExpenseOnsiteQuery = FinancialUtils.getFile("summary/forecast/getSummaryForecastVendorOtherOnsite.txt");

        Query query2 = entityManager.createNativeQuery(vendorForecastOtherExpenseOnsiteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query2).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query2.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getVendorForecastOtherExpenseOnsiteDetails = query2.getResultList();

        if (getVendorForecastOtherExpenseOnsiteDetails != null && !getVendorForecastOtherExpenseOnsiteDetails.isEmpty()) {
            SummaryDataResultDto forecastVendorOtherExpenseOnsite = new SummaryDataResultDto();
            forecastVendorOtherExpenseOnsite.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOtherExpenseOnsite.setDescription(FinancialUtils.OTHER_VENDOR_EXPENSE);
            forecastVendorOtherExpenseOnsite.setSubCategory(FinancialUtils.ONSITE);
            forecastVendorOtherExpenseOnsite.setIsAggregate(false);
            forecastVendorOtherExpenseOnsite.setData(setExpenseValues(startMonth, getVendorForecastOtherExpenseOnsiteDetails.get(0)));
            summaryForecastVendorLaborExpense.add(forecastVendorOtherExpenseOnsite);
        } else {
            SummaryDataResultDto forecastVendorOtherExpenseOnsite = new SummaryDataResultDto();
            forecastVendorOtherExpenseOnsite.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOtherExpenseOnsite.setDescription(FinancialUtils.OTHER_VENDOR_EXPENSE);
            forecastVendorOtherExpenseOnsite.setSubCategory(FinancialUtils.ONSITE);
            forecastVendorOtherExpenseOnsite.setIsAggregate(false);
            forecastVendorOtherExpenseOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryForecastVendorLaborExpense.add(forecastVendorOtherExpenseOnsite);
        }

        // Total onsite vendor Salaries
        if (summaryForecastVendorLaborExpense != null && !summaryForecastVendorLaborExpense.isEmpty()) {
            List<Double> vendorLaborExpensesOnsiteTAndMData = summaryForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.T_AND_M)
                            && it.getSubCategory().equals(FinancialUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> vendorLaborExpensesOnsiteOtherData = summaryForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.OTHER_VENDOR_EXPENSE)
                            && it.getSubCategory().equals(FinancialUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = 0; i <= 11; i++) {
                Double total = vendorLaborExpensesOnsiteTAndMData.get(i) + vendorLaborExpensesOnsiteOtherData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto forecastVendorOnsiteAggregate = new SummaryDataResultDto();
            forecastVendorOnsiteAggregate.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOnsiteAggregate.setDescription(FinancialUtils.ONSITE);
            forecastVendorOnsiteAggregate.setSubCategory(null);
            forecastVendorOnsiteAggregate.setData(totalEmployeeSalariesList);
            forecastVendorOnsiteAggregate.setIsAggregate(true);
            summaryForecastVendorLaborExpense.add(forecastVendorOnsiteAggregate);
        }

//        get summary forecast vendor T&M Offshore data
        String vendorForecastTAndMOffshoreQuery = FinancialUtils.getFile("summary/forecast/getSummaryForecastVendorTAndMOffshore.txt");

        Query query3 = entityManager.createNativeQuery(vendorForecastTAndMOffshoreQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query3.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getVendorForecastTAndMOffshoreDetails = query3.getResultList();

        if (getVendorForecastTAndMOffshoreDetails != null && !getVendorForecastTAndMOffshoreDetails.isEmpty()) {
            SummaryDataResultDto forecastVendorTAndMOffshore = new SummaryDataResultDto();
            forecastVendorTAndMOffshore.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTAndMOffshore.setDescription(FinancialUtils.T_AND_M);
            forecastVendorTAndMOffshore.setSubCategory(FinancialUtils.OFFSHORE);
            forecastVendorTAndMOffshore.setIsAggregate(false);
            forecastVendorTAndMOffshore.setData(setExpenseValues(startMonth, getVendorForecastTAndMOffshoreDetails.get(0)));
            summaryForecastVendorLaborExpense.add(forecastVendorTAndMOffshore);
        } else {
            SummaryDataResultDto forecastVendorTAndMOffshore = new SummaryDataResultDto();
            forecastVendorTAndMOffshore.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTAndMOffshore.setDescription(FinancialUtils.T_AND_M);
            forecastVendorTAndMOffshore.setSubCategory(FinancialUtils.OFFSHORE);
            forecastVendorTAndMOffshore.setIsAggregate(false);
            forecastVendorTAndMOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryForecastVendorLaborExpense.add(forecastVendorTAndMOffshore);
        }


//        get summary forecast vendor Other Expense Offshore data
        String vendorForecastOtherExpenseOffshoreQuery = FinancialUtils.getFile("summary/forecast/getSummaryForecastVendorOtherOffshore.txt");

        Query query4 = entityManager.createNativeQuery(vendorForecastOtherExpenseOffshoreQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query4).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query4.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getVendorForecastOtherExpenseOffshoreDetails = query4.getResultList();

        if (getVendorForecastOtherExpenseOffshoreDetails != null && !getVendorForecastOtherExpenseOffshoreDetails.isEmpty()) {
            SummaryDataResultDto forecastVendorOtherExpenseOffshore = new SummaryDataResultDto();
            forecastVendorOtherExpenseOffshore.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOtherExpenseOffshore.setDescription(FinancialUtils.OTHER_VENDOR_EXPENSE);
            forecastVendorOtherExpenseOffshore.setSubCategory(FinancialUtils.OFFSHORE);
            forecastVendorOtherExpenseOffshore.setIsAggregate(false);
            forecastVendorOtherExpenseOffshore.setData(setExpenseValues(startMonth, getVendorForecastOtherExpenseOffshoreDetails.get(0)));
            summaryForecastVendorLaborExpense.add(forecastVendorOtherExpenseOffshore);
        } else {
            SummaryDataResultDto forecastVendorOtherExpenseOffshore = new SummaryDataResultDto();
            forecastVendorOtherExpenseOffshore.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOtherExpenseOffshore.setDescription(FinancialUtils.OTHER_VENDOR_EXPENSE);
            forecastVendorOtherExpenseOffshore.setSubCategory(FinancialUtils.OFFSHORE);
            forecastVendorOtherExpenseOffshore.setIsAggregate(false);
            forecastVendorOtherExpenseOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryForecastVendorLaborExpense.add(forecastVendorOtherExpenseOffshore);
        }

        // Total offshore vendor Salaries
        if (summaryForecastVendorLaborExpense != null && !summaryForecastVendorLaborExpense.isEmpty()) {
            List<Double> vendorLaborExpensesOffshoreTAndMData = summaryForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.T_AND_M)
                            && it.getSubCategory().equals(FinancialUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> vendorLaborExpensesOffshoreOtherData = summaryForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.OTHER_VENDOR_EXPENSE)
                            && it.getSubCategory().equals(FinancialUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = 0; i <= 11; i++) {
                Double total = vendorLaborExpensesOffshoreTAndMData.get(i) + vendorLaborExpensesOffshoreOtherData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto forecastVendorOffshoreAggregate = new SummaryDataResultDto();
            forecastVendorOffshoreAggregate.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOffshoreAggregate.setDescription(FinancialUtils.OFFSHORE);
            forecastVendorOffshoreAggregate.setSubCategory(null);
            forecastVendorOffshoreAggregate.setData(totalEmployeeSalariesList);
            forecastVendorOffshoreAggregate.setIsAggregate(true);
            summaryForecastVendorLaborExpense.add(forecastVendorOffshoreAggregate);
        }

        // Total Vendor Expenses
        if (summaryForecastVendorLaborExpense != null && !summaryForecastVendorLaborExpense.isEmpty()) {
            List<Double> summaryForecastOnsiteAggregatedData = summaryForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.ONSITE))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> summaryForecastOffshoreAggregatedData = summaryForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(FinancialUtils.OFFSHORE))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> totalVendorLaborExpensesTotalList = new ArrayList<>();


            for (int i = 0; i <= 11; i++) {
                totalVendorLaborExpensesTotalList.add(summaryForecastOnsiteAggregatedData.get(i) + summaryForecastOffshoreAggregatedData.get(i));
            }

            SummaryDataResultDto forecastVendorTotalAggregate = new SummaryDataResultDto();
            forecastVendorTotalAggregate.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTotalAggregate.setDescription(FinancialUtils.TOTAL_VENDOR_LABOR_EXPENSES);
            forecastVendorTotalAggregate.setSubCategory(null);
            forecastVendorTotalAggregate.setIsAggregate(true);
            forecastVendorTotalAggregate.setData(totalVendorLaborExpensesTotalList);
            summaryForecastVendorLaborExpense.add(forecastVendorTotalAggregate);
        }

        if (summaryForecastVendorLaborExpense.isEmpty()) {
            summaryForecastVendorLaborExpense = null;
        }

        return summaryForecastVendorLaborExpense;
    }


    /*
    Financial Summary Total Capex Related Expense
     */

    //    Summary Budget
    public List<SummaryDataResultDto> getFinancialSummaryBudgetTotalCapex(Integer financialYear) {

//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> summaryBudgetTotalCapexExpense = new ArrayList<>();

//        get summary budget Total Capex data
        String totalCapexExpenseQuery = FinancialUtils.getFile("summary/budget/getSummaryBudgetTotalCapex.txt");

        Query query = entityManager.createNativeQuery(totalCapexExpenseQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getTotalCapexDetails = query.getResultList();

        if (!getTotalCapexDetails.isEmpty()) {
            for (SummaryDataResult eachResult : getTotalCapexDetails) {
                SummaryDataResultDto capexEachCategory = new SummaryDataResultDto();
                capexEachCategory.setTableType(FinancialUtils.TOTAL_CAPEX_RELATED_EXPENSES);
                capexEachCategory.setDescription(eachResult.getTitle());
                capexEachCategory.setSubCategory(null);
                capexEachCategory.setIsAggregate(false);
                capexEachCategory.setData(setExpenseValues(startMonth, eachResult));
                summaryBudgetTotalCapexExpense.add(capexEachCategory);
            }
        }
        else{
            SummaryDataResultDto capexEachCategory = new SummaryDataResultDto();
            capexEachCategory.setTableType(FinancialUtils.TOTAL_CAPEX_RELATED_EXPENSES);
            capexEachCategory.setDescription(FinancialUtils.EMPTY_CAPEX_OTHER_EXPENSES);
            capexEachCategory.setSubCategory(null);
            capexEachCategory.setIsAggregate(false);
            capexEachCategory.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryBudgetTotalCapexExpense.add(capexEachCategory);
        }

        // Total Capex Expenses total
        if (!summaryBudgetTotalCapexExpense.isEmpty()) {
            List<Double> totalcapexRelatedExpenseTotalList = new ArrayList<>(Collections.nCopies(12, 0d));

            List<SummaryDataResultDto> summaryBudgetTotalCapexExpenseCategoryList = summaryBudgetTotalCapexExpense.stream()
                    .filter(it -> it.getTableType().equals(FinancialUtils.TOTAL_CAPEX_RELATED_EXPENSES))
                    .collect(Collectors.toList());

            for (SummaryDataResultDto summaryDataResultDto : summaryBudgetTotalCapexExpenseCategoryList) {
                List<Double> dataList = summaryDataResultDto.getData();
                for (int i = 0; i <= 11; i++) {
                    totalcapexRelatedExpenseTotalList.set(i, totalcapexRelatedExpenseTotalList.get(i) + dataList.get(i));
                }
            }

            SummaryDataResultDto budgetCapexRelatedTotalAggregate = new SummaryDataResultDto();
            budgetCapexRelatedTotalAggregate.setTableType(FinancialUtils.TOTAL_CAPEX_RELATED_EXPENSES);
            budgetCapexRelatedTotalAggregate.setDescription(FinancialUtils.TOTAL_CAPEX_EXPENSES_TOTAL);
            budgetCapexRelatedTotalAggregate.setSubCategory(null);
            budgetCapexRelatedTotalAggregate.setIsAggregate(true);
            budgetCapexRelatedTotalAggregate.setData(totalcapexRelatedExpenseTotalList);
            summaryBudgetTotalCapexExpense.add(budgetCapexRelatedTotalAggregate);
        }
        if (summaryBudgetTotalCapexExpense.isEmpty()) {
            summaryBudgetTotalCapexExpense = null;
        }

        return summaryBudgetTotalCapexExpense;
    }

    //    Summary Forecast
    public List<SummaryDataResultDto> getFinancialSummaryForecastTotalCapex(Integer financialYear) {

//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> summaryForecastTotalCapexExpense = new ArrayList<>();

//        get summary forecast Total Capex data
        String totalCapexExpenseQuery = FinancialUtils.getFile("summary/forecast/getSummaryForecastTotalCapex.txt");

        Query query = entityManager.createNativeQuery(totalCapexExpenseQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query.setParameter("financialYear", financialYear);

        List<SummaryDataResult> getTotalCapexDetails = query.getResultList();

        if (!getTotalCapexDetails.isEmpty()) {
            for (SummaryDataResult eachResult : getTotalCapexDetails) {
                SummaryDataResultDto capexEachCategory = new SummaryDataResultDto();
                capexEachCategory.setTableType(FinancialUtils.TOTAL_CAPEX_RELATED_EXPENSES);
                capexEachCategory.setDescription(eachResult.getTitle());
                capexEachCategory.setSubCategory(null);
                capexEachCategory.setIsAggregate(false);
                capexEachCategory.setData(setExpenseValues(startMonth, eachResult));
                summaryForecastTotalCapexExpense.add(capexEachCategory);
            }
        }
        else{
            SummaryDataResultDto capexEachCategory = new SummaryDataResultDto();
            capexEachCategory.setTableType(FinancialUtils.TOTAL_CAPEX_RELATED_EXPENSES);
            capexEachCategory.setDescription(FinancialUtils.EMPTY_CAPEX_OTHER_EXPENSES);
            capexEachCategory.setSubCategory(null);
            capexEachCategory.setIsAggregate(false);
            capexEachCategory.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            summaryForecastTotalCapexExpense.add(capexEachCategory);
        }

        // Total Capex Expenses total
        if (!summaryForecastTotalCapexExpense.isEmpty()) {
            List<Double> totalcapexRelatedExpenseTotalList = new ArrayList<>(Collections.nCopies(12, 0d));

            List<SummaryDataResultDto> summaryForecastTotalCapexExpenseCategoryList = summaryForecastTotalCapexExpense.stream()
                    .filter(it -> it.getTableType().equals(FinancialUtils.TOTAL_CAPEX_RELATED_EXPENSES))
                    .collect(Collectors.toList());

            for (SummaryDataResultDto summaryDataResultDto : summaryForecastTotalCapexExpenseCategoryList) {
                List<Double> dataList = summaryDataResultDto.getData();
                for (int i = 0; i <= 11; i++) {
                    totalcapexRelatedExpenseTotalList.set(i, totalcapexRelatedExpenseTotalList.get(i) + dataList.get(i));
                }
            }

            SummaryDataResultDto forecastCapexRelatedTotalAggregate = new SummaryDataResultDto();
            forecastCapexRelatedTotalAggregate.setTableType(FinancialUtils.TOTAL_CAPEX_RELATED_EXPENSES);
            forecastCapexRelatedTotalAggregate.setDescription(FinancialUtils.TOTAL_CAPEX_EXPENSES_TOTAL);
            forecastCapexRelatedTotalAggregate.setSubCategory(null);
            forecastCapexRelatedTotalAggregate.setIsAggregate(true);
            forecastCapexRelatedTotalAggregate.setData(totalcapexRelatedExpenseTotalList);
            summaryForecastTotalCapexExpense.add(forecastCapexRelatedTotalAggregate);
        }

        if (summaryForecastTotalCapexExpense.isEmpty()) {
            summaryForecastTotalCapexExpense = null;
        }


        return summaryForecastTotalCapexExpense;
    }

    /*
    Total Financial Summary
     */

    //    Budget
    public List<SummaryDataResultDto> getFinancialSummaryBudget(Integer financialYear) {

        List<SummaryDataResultDto> getFinancialSummaryBudget = new ArrayList<>();

        List<SummaryDataResultDto> getFinancialSummaryBudgetInternalLabor = getFinancialSummaryBudgetInternalLabor(financialYear);
        List<SummaryDataResultDto> getFinancialSummaryBudgetVendorLabor = getFinancialSummaryBudgetVendorLabor(financialYear);
        List<SummaryDataResultDto> getFinancialSummaryBudgetTotalCapex = getFinancialSummaryBudgetTotalCapex(financialYear);

        getFinancialSummaryBudget.addAll(getFinancialSummaryBudgetInternalLabor);
        getFinancialSummaryBudget.addAll(getFinancialSummaryBudgetVendorLabor);

        List<Double> getTotalInternalLaborExpense = getFinancialSummaryBudgetInternalLabor.stream()
                .filter(it -> it.getTableType().equals(FinancialUtils.INTERNAL_LABOR_EXPENSES)
                        && it.getDescription().equals(FinancialUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getTotalVendorLaborExpense = getFinancialSummaryBudgetVendorLabor.stream()
                .filter(it -> it.getTableType().equals(FinancialUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(FinancialUtils.TOTAL_VENDOR_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> totalBudgetSpentTotalList = new ArrayList<>(Collections.nCopies(12, 0d));

        for (int i = 0; i <= 11; i++) {
            totalBudgetSpentTotalList.set(i, totalBudgetSpentTotalList.get(i) + getTotalInternalLaborExpense.get(i) + getTotalVendorLaborExpense.get(i));
        }

        SummaryDataResultDto budgetTotalBudgetSpent = new SummaryDataResultDto();
        budgetTotalBudgetSpent.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
        budgetTotalBudgetSpent.setDescription(FinancialUtils.TOTAL_BUDGET_EXPENSES);
        budgetTotalBudgetSpent.setSubCategory(null);
        budgetTotalBudgetSpent.setIsAggregate(true);
        budgetTotalBudgetSpent.setData(totalBudgetSpentTotalList);
        getFinancialSummaryBudget.add(budgetTotalBudgetSpent);

        getFinancialSummaryBudget.addAll(getFinancialSummaryBudgetTotalCapex);

        return getFinancialSummaryBudget;
    }

    //    forecast
    public List<SummaryDataResultDto> getFinancialSummaryForecast(Integer financialYear) {

        List<SummaryDataResultDto> getFinancialSummaryForecast = new ArrayList<>();

        List<SummaryDataResultDto> getFinancialSummaryForecastInternalLabor = getFinancialSummaryForecastInternalLabor(financialYear);
        List<SummaryDataResultDto> getFinancialSummaryForecastVendorLabor = getFinancialSummaryForecastVendorLabor(financialYear);
        List<SummaryDataResultDto> getFinancialSummaryForecastTotalCapex = getFinancialSummaryForecastTotalCapex(financialYear);

        getFinancialSummaryForecast.addAll(getFinancialSummaryForecastInternalLabor);
        getFinancialSummaryForecast.addAll(getFinancialSummaryForecastVendorLabor);

        List<Double> getTotalInternalLaborExpense = getFinancialSummaryForecastInternalLabor.stream()
                .filter(it -> it.getTableType().equals(FinancialUtils.INTERNAL_LABOR_EXPENSES)
                        && it.getDescription().equals(FinancialUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getTotalVendorLaborExpense = getFinancialSummaryForecastVendorLabor.stream()
                .filter(it -> it.getTableType().equals(FinancialUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(FinancialUtils.TOTAL_VENDOR_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> totalForecastSpentTotalList = new ArrayList<>(Collections.nCopies(12, 0d));

        for (int i = 0; i <= 11; i++) {
            totalForecastSpentTotalList.set(i, totalForecastSpentTotalList.get(i) + getTotalInternalLaborExpense.get(i) + getTotalVendorLaborExpense.get(i));
        }

        SummaryDataResultDto forecastTotalForecastSpent = new SummaryDataResultDto();
        forecastTotalForecastSpent.setTableType(FinancialUtils.VENDOR_LABOR_EXPENSES);
        forecastTotalForecastSpent.setDescription(FinancialUtils.TOTAL_FORECAST_EXPENSES);
        forecastTotalForecastSpent.setSubCategory(null);
        forecastTotalForecastSpent.setIsAggregate(true);
        forecastTotalForecastSpent.setData(totalForecastSpentTotalList);
        getFinancialSummaryForecast.add(forecastTotalForecastSpent);

        getFinancialSummaryForecast.addAll(getFinancialSummaryForecastTotalCapex);

        return getFinancialSummaryForecast;
    }

}
