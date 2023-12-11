package com.forecastera.service.financialmanagement.services;

/*
 * @Author Uttam Kachhad
 * @Create 22-06-2023
 * @Description
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.financialmanagement.config.exception.BadRequestException;
import com.forecastera.service.financialmanagement.dto.request.capexrelatedexpense.*;
import com.forecastera.service.financialmanagement.dto.request.directemployeedetails.*;
import com.forecastera.service.financialmanagement.dto.request.otherdirectinhousecost.*;
import com.forecastera.service.financialmanagement.dto.request.vendoremployeedetails.*;
import com.forecastera.service.financialmanagement.dto.request.vendorrelatedcost.*;
import com.forecastera.service.financialmanagement.dto.response.capexrelatedexpense.*;
import com.forecastera.service.financialmanagement.dto.response.directemployeedetails.*;
import com.forecastera.service.financialmanagement.dto.response.otherdirectinhousecost.*;
import com.forecastera.service.financialmanagement.dto.response.vendoremployeedetails.*;
import com.forecastera.service.financialmanagement.dto.response.vendorrelatedcost.*;
import com.forecastera.service.financialmanagement.dto.utilityDtoClass.*;
import com.forecastera.service.financialmanagement.entity.*;
import com.forecastera.service.financialmanagement.entity.capexrelatedexpense.*;
import com.forecastera.service.financialmanagement.entity.directemployeedetails.*;
import com.forecastera.service.financialmanagement.entity.otherdirectinhousecost.*;
import com.forecastera.service.financialmanagement.entity.vendoremployeedetails.*;
import com.forecastera.service.financialmanagement.entity.vendorrelatedcost.*;
import com.forecastera.service.financialmanagement.repository.*;
import com.forecastera.service.financialmanagement.repository.capexrelatedexpense.*;
import com.forecastera.service.financialmanagement.repository.directemployeedetails.*;
import com.forecastera.service.financialmanagement.repository.otherdirectinhousecost.*;
import com.forecastera.service.financialmanagement.repository.vendoremployeedetails.*;
import com.forecastera.service.financialmanagement.repository.vendorrelatedcost.*;
import com.forecastera.service.financialmanagement.util.FinancialUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@Service
public class FinancialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancialService.class);

    @Autowired
    @Qualifier("edbObjectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    private DirectEmployeeDetailsBudgetRepo directEmployeeDetailsBudgetRepo;

    @Autowired
    private ResourceMgmtRolesRepo resourceMgmtRolesRepo;

    @Autowired
    private ResourceMgmtRepo resourceMgmtRepo;

    @Autowired
    private FinanceDirectEmployeeBudgetRepo financeDirectEmployeeBudgetRepo;

    @Autowired
    private FinanceMgmtFieldMapRepo financeMgmtFieldMapRepo;

    @Autowired
    private FinanceCustomPicklistRepo financeCustomPicklistRepo;

    @Autowired
    private VendorEmployeeDetailsBudgetRepo vendorEmployeeDetailsBudgetRepo;

    @Autowired
    private VendorEmployeeDetailsForecastRepo vendorEmployeeDetailsForecastRepo;

    @Autowired
    private FinanceMgmtFieldValueBudgetVendorEmpRepo financeMgmtFieldValueBudgetVendorEmpRepo;

    @Autowired
    private FinanceMgmtFieldValueForecastVendorEmpRepo financeMgmtFieldValueForecastVendorEmpRepo;

    @Autowired
    private VendorEmploymentDetailsBudgetExpenseRepo vendorEmploymentDetailsBudgetExpenseRepo;

    @Autowired
    private VendorEmploymentDetailsForecastExpenseRepo vendorEmploymentDetailsForecastExpenseRepo;

    @Autowired
    private VendorRelatedCostBudgetRepo vendorRelatedCostBudgetRepo;

    @Autowired
    private VendorRelatedCostFieldValueBudgetRepo vendorRelatedCostFieldValueBudgetRepo;

    @Autowired
    private VendorRelatedCostForecastRepo vendorRelatedCostForecastRepo;

    @Autowired
    private VendorRelatedCostForecastExpenseRepo vendorRelatedCostForecastExpenseRepo;

    @Autowired
    private VendorRelatedCostFieldValueForecastRepo vendorRelatedCostFieldValueForecastRepo;

    private final EntityManagerFactory emf;

    private final EntityManager entityManager;

    @Autowired
    private DirectEmployeeDetailsForecastRepo directEmployeeDetailsForecastRepo;

    @Autowired
    private FinanceDirectEmployeeForecastRepo financeDirectEmployeeForecastRepo;

    @Autowired
    private OtherDirectInHouseCostBudgetRepo otherDirectInHouseCostBudgetRepo;

    @Autowired
    private OtherDirectInHouseCostFieldValueBudgetRepo otherDirectInHouseCostFieldValueBudgetRepo;

    @Autowired
    private OtherDirectInHouseBudgetExpenseRepo otherDirectInHouseBudgetExpenseRepo;

    @Autowired
    private OtherDirectInHouseCostForecastRepo otherDirectInHouseCostForecastRepo;

    @Autowired
    private OtherDirectInHouseCostFieldValueForecastRepo otherDirectInHouseCostFieldValueForecastRepo;

    @Autowired
    private OtherDirectInHouseForecastExpenseRepo otherDirectInHouseForecastExpenseRepo;

    @Autowired
    private CapexRelatedExpenseBudgetRepo capexRelatedExpenseBudgetRepo;

    @Autowired
    private VendorRelatedCostBudgetExpenseRepo vendorRelatedCostBudgetExpenseRepo;

    @Autowired
    private CapexRelatedExpenseBudgetExpenseRepo capexRelatedExpenseBudgetExpenseRepo;

    @Autowired
    private CapexRelatedExpenseForecastRepo capexRelatedExpenseForecastRepo;

    @Autowired
    private CapexRelatedExpenseForecastExpenseRepo capexRelatedExpenseForecastExpenseRepo;

    @Autowired
    private FinanceMgmtFieldValueBudgetCapexRepo financeMgmtFieldValueBudgetCapexRepo;

    @Autowired
    private FinanceMgmtFieldValueForecastCapexRepo financeMgmtFieldValueForecastCapexRepo;

    @Autowired
    private GeneralSettingsLocationRepo generalSettingsLocationRepo;

    @Autowired
    private ProjectMgmtRepo projectMgmtRepo;

    @Autowired
    private GeneralSettingsRepo generalSettingsRepo;

    @Autowired
    public FinancialService(EntityManagerFactory emf, EntityManager entityManager) {
        this.emf = emf;
        this.entityManager = emf.createEntityManager();
    }

    /*
    Direct Employee Details Table
     */

    public void createUpdateDirectEmployeeBudgetDetails(List<DirectEmployeeDetailsBudgetDto> directEmployeeDetailsBudgetDto) {

        try {

            for (DirectEmployeeDetailsBudgetDto directEmployeeDetailsBudgetDtoObj : directEmployeeDetailsBudgetDto) {

                Integer id = 0;

                DirectEmployeeDetailsBudget directEmployeeDetailsBudget = null;

//                check if id is available or not
                if (ObjectUtils.isEmpty(directEmployeeDetailsBudgetDtoObj.getDirectEmployeeDetBudgetId())
                        || directEmployeeDetailsBudgetDtoObj.getDirectEmployeeDetBudgetId() == 0) {

//                    if not available
                    directEmployeeDetailsBudget = new DirectEmployeeDetailsBudget();
                    directEmployeeDetailsBudget.setCreatedBy(0);
                    directEmployeeDetailsBudget.setCreatedDate(new Date());

                } else {

                    Optional<DirectEmployeeDetailsBudget> directEmployeeDetailsBudgetObj =
                            directEmployeeDetailsBudgetRepo.findById(directEmployeeDetailsBudgetDtoObj.getDirectEmployeeDetBudgetId());

//                    if available and present in db
                    if (directEmployeeDetailsBudgetObj.isPresent()) {
                        directEmployeeDetailsBudget = directEmployeeDetailsBudgetObj.get();
                        directEmployeeDetailsBudget.setModifiedBy(0);
                        directEmployeeDetailsBudget.setModifiedDate(new Date());
                    }
//                    if available but not present in db
                    else {
                        directEmployeeDetailsBudget = new DirectEmployeeDetailsBudget();
                        directEmployeeDetailsBudget.setCreatedBy(0);
                        directEmployeeDetailsBudget.setCreatedDate(new Date());
                    }
                }
                directEmployeeDetailsBudget.setRowOrder(directEmployeeDetailsBudgetDtoObj.getRowOrder());
                directEmployeeDetailsBudget.setIsActive(directEmployeeDetailsBudgetDtoObj.getIsActive());
                directEmployeeDetailsBudget.setFinancialYear(directEmployeeDetailsBudgetDtoObj.getFinancialYear());

//                save record to get id
                DirectEmployeeDetailsBudget savedDirectEmployeeDetailsBudget = directEmployeeDetailsBudgetRepo.save(directEmployeeDetailsBudget);

//                if data is present
                if (!ObjectUtils.isEmpty(directEmployeeDetailsBudgetDtoObj.getDirectEmployeeDetailsBudgetData())) {

//                    collect field ids from all the data
                    Set<Integer> fieldIdList = directEmployeeDetailsBudgetDtoObj.getDirectEmployeeDetailsBudgetData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

//                        check if these field ids are present in db
                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<FinanceDirectEmployeeBudget> financeDirectEmployeeBudgetsList = new ArrayList<>();

//                        traverse data
                        for (DirectEmployeeDetailsBudgetData directEmployeeDetailsBudgetData : directEmployeeDetailsBudgetDtoObj.getDirectEmployeeDetailsBudgetData()) {

//                            check if given id is present or not
                            if (availableFieldList.contains(directEmployeeDetailsBudgetData.getFieldId())
                                    && !directEmployeeDetailsBudgetData.getFieldName().equalsIgnoreCase(FinancialUtils.FINANCE_FIRST_NAME)
                                    && !directEmployeeDetailsBudgetData.getFieldName().equalsIgnoreCase(FinancialUtils.FINANCE_LAST_NAME)) {

//                                in case of update, check if field value exist for this id by using id and field id
                                Optional<FinanceDirectEmployeeBudget> financeDirectEmployeeBudgetObj =
                                        financeDirectEmployeeBudgetRepo.findByDirectEmployeeBudgetIdAndFieldId(savedDirectEmployeeDetailsBudget.getId(), directEmployeeDetailsBudgetData.getFieldId());

                                FinanceDirectEmployeeBudget financeDirectEmployeeBudget = null;

//                                if present
                                if (financeDirectEmployeeBudgetObj.isPresent()) {
                                    financeDirectEmployeeBudget = financeDirectEmployeeBudgetObj.get();
                                    financeDirectEmployeeBudget.setModifiedBy(0);
                                    financeDirectEmployeeBudget.setModifiedDate(new Date());
                                }
//                                if not present, create new field_value record
                                else {
                                    financeDirectEmployeeBudget = new FinanceDirectEmployeeBudget();
                                    financeDirectEmployeeBudget.setCreatedBy(0);
                                    financeDirectEmployeeBudget.setCreatedDate(new Date());
                                }
                                financeDirectEmployeeBudget.setDirectEmployeeBudgetId(savedDirectEmployeeDetailsBudget.getId());
                                financeDirectEmployeeBudget.setFieldId(directEmployeeDetailsBudgetData.getFieldId());

//                                find the field detail of the field for which current loop is running
                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(directEmployeeDetailsBudgetData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST)) {
                                        if(!ObjectUtils.isEmpty(directEmployeeDetailsBudgetData.getPicklistId())){
                                            if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(directEmployeeDetailsBudgetData.getPicklistId().split(",")[0]));

                                                if (locationPicklist.isPresent()) {
                                                    financeDirectEmployeeBudget.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else {
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(directEmployeeDetailsBudgetData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    financeDirectEmployeeBudget.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                        }
                                        else{
                                            financeDirectEmployeeBudget.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(directEmployeeDetailsBudgetData.getPicklistId())) {
                                            if (findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_DESIGNATION)) {

                                                Set<Integer> pickListIds = Stream.of(directEmployeeDetailsBudgetData.getPicklistId().split(","))
                                                        .map(Integer::parseInt).collect(Collectors.toSet());

                                                List<ResourceMgmtRoles> designationPicklist = resourceMgmtRolesRepo.findAllById(pickListIds);

                                                if (!ObjectUtils.isEmpty(designationPicklist) && designationPicklist.size() > 0) {
                                                    financeDirectEmployeeBudget.setFieldValue(designationPicklist.stream()
                                                            .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                                }
                                            } else {
                                                Set<Integer> pickListIds = Stream.of(directEmployeeDetailsBudgetData.getPicklistId().split(","))
                                                        .map(Integer::parseInt).collect(Collectors.toSet());

                                                List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                                if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                    financeDirectEmployeeBudget.setFieldValue(financeCustomPicklists.stream()
                                                            .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                                }
                                            }
                                        }
                                        else{
                                            financeDirectEmployeeBudget.setFieldValue("");
                                        }

                                    } else {
                                        financeDirectEmployeeBudget.setFieldValue(directEmployeeDetailsBudgetData.getFieldValue());
                                    }

                                }
                                financeDirectEmployeeBudgetsList.add(financeDirectEmployeeBudget);
                            }
                        }
                        if (!ObjectUtils.isEmpty(financeDirectEmployeeBudgetsList)) {
                            financeDirectEmployeeBudgetRepo.saveAll(financeDirectEmployeeBudgetsList);
                        }
                    }
                }

            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public List<GetDirectEmployeeBudgetDetailsMaster> getDirectEmployeeDetailsBudget(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        List<GetDirectEmployeeBudgetDetailsMaster> getDirectEmployeeBudgetDetailsMasterList = null;
        String directEmployeeDetailsBudgetQuery = FinancialUtils.getFile("worksheet/getDirectEmployeeDetailsBudget.txt");

        Query query = entityManager.createNativeQuery(directEmployeeDetailsBudgetQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetDirectEmployeeBudgetDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetDirectEmployeeBudgetDetails> getDirectEmployeeDetailsBudget = query.getResultList();

        if (getDirectEmployeeDetailsBudget != null && !getDirectEmployeeDetailsBudget.isEmpty()) {

            FinanceMgmtFieldMap fieldFirstName = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName("First Name", FinancialUtils.DIRECT_EMPLOYEE_DETAILS);
            FinanceMgmtFieldMap fieldLastName = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName("Last Name", FinancialUtils.DIRECT_EMPLOYEE_DETAILS);

            Map<Integer, List<GetDirectEmployeeBudgetDetails>> groupByBudgetId = getDirectEmployeeDetailsBudget.stream().collect(Collectors.groupingBy(GetDirectEmployeeBudgetDetails::getDirect_employee_det_budget_id));
            getDirectEmployeeBudgetDetailsMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetDirectEmployeeBudgetDetails>> entry : groupByBudgetId.entrySet()) {

                GetDirectEmployeeBudgetDetailsMaster getDirectEmployeeBudgetDetailsMaster = new GetDirectEmployeeBudgetDetailsMaster();

                if (!entry.getValue().isEmpty()) {

                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);
                    getDirectEmployeeBudgetDetailsMaster.setDirectEmployeeDetailsBudgetId(entry.getValue().get(0).getDirect_employee_det_budget_id());

                    getDirectEmployeeBudgetDetailsMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getDirectEmployeeBudgetDetailsMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getDirectEmployeeBudgetDetailsMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    List<Map<String, Object>> fieldList = new ArrayList<>();

                    /*for (Map.Entry<String, Object> getDirectEmployeeBudgetDetails : objToMap.entrySet()) {
                        if (!getDirectEmployeeBudgetDetails.getKey().equals("fields")
                                && !getDirectEmployeeBudgetDetails.getKey().equals("fieldValue")
                                && !getDirectEmployeeBudgetDetails.getKey().equals("fieldType")
                                && !getDirectEmployeeBudgetDetails.getKey().equals("field_id")
                                && !getDirectEmployeeBudgetDetails.getKey().equals("Direct Employee Budget Details Id")
                                && !getDirectEmployeeBudgetDetails.getKey().equals("Financial Year")
                                && !getDirectEmployeeBudgetDetails.getKey().equals("Row Order")
                                && !getDirectEmployeeBudgetDetails.getKey().equals("Is Active")) {

                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", getDirectEmployeeBudgetDetails.getKey());
                            fields.put("fieldValue", getDirectEmployeeBudgetDetails.getValue());
                            *//*if (getDirectEmployeeBudgetDetails.getKey().equals("Direct Employee Budget Details Id")) {
                                fields.put("fieldType", "Integer");
                            } else if*//*
                            if (getDirectEmployeeBudgetDetails.getKey().equals("Created Date")) {
                                fields.put("fieldType", "Date");
                            } else if (getDirectEmployeeBudgetDetails.getKey().equals("Created By")) {
                                fields.put("fieldType", "Text");
                            } else if (getDirectEmployeeBudgetDetails.getKey().equals("Modified Date")) {
                                fields.put("fieldType", "Date");
                            } else if (getDirectEmployeeBudgetDetails.getKey().equals("Modified By")) {
                                fields.put("fieldType", "Text");
                            }
                            fieldList.add(fields);
                        }
                    }*/

                    for (GetDirectEmployeeBudgetDetails getDirectEmployeeBudgetDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(getDirectEmployeeBudgetDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            if (getDirectEmployeeBudgetDetails.getFields().equalsIgnoreCase("Employee ID")) {
                                if (!ObjectUtils.isEmpty(getDirectEmployeeBudgetDetails.getField_value())) {
                                    ResourceMgmt resourceMgmt = resourceMgmtRepo.getReferenceById(Integer.parseInt(getDirectEmployeeBudgetDetails.getField_value()));
                                    Map<String, Object> childFields = new HashMap<>();
                                    if(fieldFirstName!=null && fieldFirstName.getEnabled()) {
                                        childFields.put("fieldName", "First Name");
                                        childFields.put("fieldValue", resourceMgmt.getFirstName());
                                        childFields.put("fieldType", "String");
                                        childFields.put("fieldId", fieldFirstName.getFieldId());
                                        fieldList.add(childFields);
                                    }
                                    if(fieldLastName!=null && fieldLastName.getEnabled()) {
                                        childFields = new HashMap<>();
                                        childFields.put("fieldName", "Last Name");
                                        childFields.put("fieldValue", resourceMgmt.getLastName());
                                        childFields.put("fieldType", "String");
                                        childFields.put("fieldId", fieldLastName.getFieldId());
                                        fieldList.add(childFields);
                                    }
                                    childFields = new HashMap<>();
                                    childFields.put("fieldName", "Employee Id");
                                    childFields.put("fieldValue", resourceMgmt.getResourceId());
                                    childFields.put("fieldType", "Number");
                                    childFields.put("fieldId", getDirectEmployeeBudgetDetails.getField_id());
                                    fieldList.add(childFields);
                                }
                            } else {
                                fields.put("fieldName", getDirectEmployeeBudgetDetails.getFields());
                                if(getDirectEmployeeBudgetDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                    String stringDate = FinancialUtils.formatStringDateToStringDate(getDirectEmployeeBudgetDetails.getField_value(), timeZone, dateFormat);
                                    fields.put("fieldValue", stringDate);
                                }
                                else{
                                    fields.put("fieldValue", getDirectEmployeeBudgetDetails.getField_value());
                                }

                                fields.put("fieldType", getDirectEmployeeBudgetDetails.getField_type());
                                if(!Objects.equals(getDirectEmployeeBudgetDetails.getField_value(), getDirectEmployeeBudgetDetails.getPicklist_id())){
                                    fields.put("picklist_id", getDirectEmployeeBudgetDetails.getPicklist_id());
                                }
                                fields.put("fieldId", getDirectEmployeeBudgetDetails.getField_id());
                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                    fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                    getDirectEmployeeBudgetDetailsMaster.setFields(fieldList);
                }
                getDirectEmployeeBudgetDetailsMasterList.add(getDirectEmployeeBudgetDetailsMaster);
            }

            getDirectEmployeeBudgetDetailsMasterList.sort(Comparator.comparingInt(GetDirectEmployeeBudgetDetailsMaster::getRow_order));
        }
        return getDirectEmployeeBudgetDetailsMasterList;
    }

    public void createUpdateDirectEmployeeForecastDetails(List<DirectEmployeeDetailsForecastDto> directEmployeeDetailsForecastDto) {

        try {

            for (DirectEmployeeDetailsForecastDto directEmployeeDetailsForecastDtoObj : directEmployeeDetailsForecastDto) {

                Integer id = 0;

                DirectEmployeeDetailsForecast directEmployeeDetailsForecast = null;

//                check if id is available or not
                if (ObjectUtils.isEmpty(directEmployeeDetailsForecastDtoObj.getDirectEmployeeDetForecastId())
                        || directEmployeeDetailsForecastDtoObj.getDirectEmployeeDetForecastId() == 0) {

//                    if not available
                    directEmployeeDetailsForecast = new DirectEmployeeDetailsForecast();
                    directEmployeeDetailsForecast.setCreatedBy(0);
                    directEmployeeDetailsForecast.setCreatedDate(new Date());

                } else {

                    Optional<DirectEmployeeDetailsForecast> directEmployeeDetailsForecastObj =
                            directEmployeeDetailsForecastRepo.findById(directEmployeeDetailsForecastDtoObj.getDirectEmployeeDetForecastId());

//                    if available and present in db
                    if (directEmployeeDetailsForecastObj.isPresent()) {
                        directEmployeeDetailsForecast = directEmployeeDetailsForecastObj.get();
                        directEmployeeDetailsForecast.setModifiedBy(0);
                        directEmployeeDetailsForecast.setModifiedDate(new Date());
                    }
//                    if available but not present in db
                    else {
                        directEmployeeDetailsForecast = new DirectEmployeeDetailsForecast();
                        directEmployeeDetailsForecast.setCreatedBy(0);
                        directEmployeeDetailsForecast.setCreatedDate(new Date());
                    }
                }
                directEmployeeDetailsForecast.setRowOrder(directEmployeeDetailsForecastDtoObj.getRowOrder());
                directEmployeeDetailsForecast.setIsActive(directEmployeeDetailsForecastDtoObj.getIsActive());
                directEmployeeDetailsForecast.setFinancialYear(directEmployeeDetailsForecastDtoObj.getFinancialYear());

//                save record to get id
                DirectEmployeeDetailsForecast savedDirectEmployeeDetailsForecast = directEmployeeDetailsForecastRepo.save(directEmployeeDetailsForecast);

//                if data is present
                if (!ObjectUtils.isEmpty(directEmployeeDetailsForecastDtoObj.getDirectEmployeeDetailsForecastData())) {

//                    collect field ids from all the data
                    Set<Integer> fieldIdList = directEmployeeDetailsForecastDtoObj.getDirectEmployeeDetailsForecastData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

//                        check if these field ids are present in db
                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<FinanceDirectEmployeeForecast> financeDirectEmployeeForecastList = new ArrayList<>();

//                        traverse data
                        for (DirectEmployeeDetailsForecastData directEmployeeDetailsForecastData : directEmployeeDetailsForecastDtoObj.getDirectEmployeeDetailsForecastData()) {

//                            check if given id is present or not
                            if (availableFieldList.contains(directEmployeeDetailsForecastData.getFieldId())
                                    && !directEmployeeDetailsForecastData.getFieldName().equalsIgnoreCase(FinancialUtils.FINANCE_FIRST_NAME)
                                    && !directEmployeeDetailsForecastData.getFieldName().equalsIgnoreCase(FinancialUtils.FINANCE_LAST_NAME)) {

//                                in case of update, check if field value exist for this id by using id and field id
                                Optional<FinanceDirectEmployeeForecast> financeDirectEmployeeForecastObj =
                                        financeDirectEmployeeForecastRepo.findByDirectEmployeeForecastIdAndFieldId(savedDirectEmployeeDetailsForecast.getId(), directEmployeeDetailsForecastData.getFieldId());

                                FinanceDirectEmployeeForecast financeDirectEmployeeForecast = null;

//                                if present
                                if (financeDirectEmployeeForecastObj.isPresent()) {
                                    financeDirectEmployeeForecast = financeDirectEmployeeForecastObj.get();
                                    financeDirectEmployeeForecast.setModifiedBy(0);
                                    financeDirectEmployeeForecast.setModifiedDate(new Date());
                                }
//                                if not present, create new field_value record
                                else {
                                    financeDirectEmployeeForecast = new FinanceDirectEmployeeForecast();
                                    financeDirectEmployeeForecast.setCreatedBy(0);
                                    financeDirectEmployeeForecast.setCreatedDate(new Date());
                                }
                                financeDirectEmployeeForecast.setDirectEmployeeForecastId(savedDirectEmployeeDetailsForecast.getId());
                                financeDirectEmployeeForecast.setFieldId(directEmployeeDetailsForecastData.getFieldId());

//                                find the field detail of the field for which current loop is running
                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(directEmployeeDetailsForecastData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST)) {

                                        if(!ObjectUtils.isEmpty(directEmployeeDetailsForecastData.getPicklistId())) {
                                            if (findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)) {
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(directEmployeeDetailsForecastData.getPicklistId().split(",")[0]));

                                                if (locationPicklist.isPresent()) {
                                                    financeDirectEmployeeForecast.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            } else {
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(directEmployeeDetailsForecastData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    financeDirectEmployeeForecast.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                        }
                                        else {
                                            financeDirectEmployeeForecast.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI) ) {
                                        if(!ObjectUtils.isEmpty(directEmployeeDetailsForecastData.getPicklistId())){
                                            if (findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_DESIGNATION)) {

                                                Set<Integer> pickListIds = Stream.of(directEmployeeDetailsForecastData.getPicklistId().split(","))
                                                        .map(Integer::parseInt).collect(Collectors.toSet());

                                                List<ResourceMgmtRoles> designationPicklist = resourceMgmtRolesRepo.findAllById(pickListIds);

                                                if (!ObjectUtils.isEmpty(designationPicklist) && designationPicklist.size() > 0) {
                                                    financeDirectEmployeeForecast.setFieldValue(designationPicklist.stream()
                                                            .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                                }
                                            } else {
                                                Set<Integer> pickListIds = Stream.of(directEmployeeDetailsForecastData.getPicklistId().split(","))
                                                        .map(Integer::parseInt).collect(Collectors.toSet());

                                                List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                                if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                    financeDirectEmployeeForecast.setFieldValue(financeCustomPicklists.stream()
                                                            .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                                }
                                            }
                                        }
                                        else{
                                            financeDirectEmployeeForecast.setFieldValue("");
                                        }
                                    } else {
                                        financeDirectEmployeeForecast.setFieldValue(directEmployeeDetailsForecastData.getFieldValue());
                                    }

                                }
                                financeDirectEmployeeForecastList.add(financeDirectEmployeeForecast);
                            }
                        }
                        if (!ObjectUtils.isEmpty(financeDirectEmployeeForecastList)) {
                            financeDirectEmployeeForecastRepo.saveAll(financeDirectEmployeeForecastList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public List<GetDirectEmployeeForecastDetailsMaster> getDirectEmployeeForecastDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        List<GetDirectEmployeeForecastDetailsMaster> getDirectEmployeeForecastDetailsMasterList = null;
        String directEmployeeDetailsForecastQuery = FinancialUtils.getFile("worksheet/getDirectEmployeeDetailsForecast.txt");

        Query query = entityManager.createNativeQuery(directEmployeeDetailsForecastQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetDirectEmployeeForecastDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetDirectEmployeeForecastDetails> getDirectEmployeeDetailsForecast = query.getResultList();

        if (getDirectEmployeeDetailsForecast != null && !getDirectEmployeeDetailsForecast.isEmpty()) {

            FinanceMgmtFieldMap fieldFirstName = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName("First Name", FinancialUtils.DIRECT_EMPLOYEE_DETAILS);
            FinanceMgmtFieldMap fieldLastName = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName("Last Name", FinancialUtils.DIRECT_EMPLOYEE_DETAILS);

            Map<Integer, List<GetDirectEmployeeForecastDetails>> groupByForecastId = getDirectEmployeeDetailsForecast.stream().collect(groupingBy(GetDirectEmployeeForecastDetails::getDirect_employee_det_forecast_id));
            getDirectEmployeeForecastDetailsMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetDirectEmployeeForecastDetails>> entry : groupByForecastId.entrySet()) {

                GetDirectEmployeeForecastDetailsMaster directEmployeeForecastDetailsMaster = new GetDirectEmployeeForecastDetailsMaster();

                if (!entry.getValue().isEmpty()) {

                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);
                    directEmployeeForecastDetailsMaster.setDirectEmployeeDetailsForecastId(entry.getValue().get(0).getDirect_employee_det_forecast_id());

                    directEmployeeForecastDetailsMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    directEmployeeForecastDetailsMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    directEmployeeForecastDetailsMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    List<Map<String, Object>> fieldList = new ArrayList<>();

                    /*for (Map.Entry<String, Object> getDirectEmployeeForecastDetails : objToMap.entrySet()) {
                        if (!getDirectEmployeeForecastDetails.getKey().equals("fields")
                                && !getDirectEmployeeForecastDetails.getKey().equals("fieldValue")
                                && !getDirectEmployeeForecastDetails.getKey().equals("fieldType")
                                && !getDirectEmployeeForecastDetails.getKey().equals("Direct Employee Forecast Details Id")
                                && !getDirectEmployeeForecastDetails.getKey().equals("Financial Year")
                                && !getDirectEmployeeForecastDetails.getKey().equals("Row Order")
                                && !getDirectEmployeeForecastDetails.getKey().equals("Is Active")) {

                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", getDirectEmployeeForecastDetails.getKey());
                            fields.put("fieldValue", getDirectEmployeeForecastDetails.getValue());
                            if (getDirectEmployeeForecastDetails.getKey().equals("Created Date")) {
                                fields.put("fieldType", "Date");
                            } else if (getDirectEmployeeForecastDetails.getKey().equals("Created By")) {
                                fields.put("fieldType", "Text");
                            } else if (getDirectEmployeeForecastDetails.getKey().equals("Modified Date")) {
                                fields.put("fieldType", "Date");
                            } else if (getDirectEmployeeForecastDetails.getKey().equals("Modified By")) {
                                fields.put("fieldType", "Text");
                            }
                            fieldList.add(fields);
                        }
                    }*/

                    for (GetDirectEmployeeForecastDetails getDirectEmployeeForecastDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(getDirectEmployeeForecastDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            if (getDirectEmployeeForecastDetails.getFields().equalsIgnoreCase("Employee ID")) {
                                if (!ObjectUtils.isEmpty(getDirectEmployeeForecastDetails.getField_value())) {
                                    ResourceMgmt resourceMgmt = resourceMgmtRepo.getReferenceById(Integer.parseInt(getDirectEmployeeForecastDetails.getField_value()));
                                    Map<String, Object> childFields = new HashMap<>();
                                    if(fieldFirstName!=null && fieldFirstName.getEnabled()) {
                                        childFields.put("fieldName", "First Name");
                                        childFields.put("fieldValue", resourceMgmt.getFirstName());
                                        childFields.put("fieldType", "String");
                                        childFields.put("fieldId", fieldFirstName.getFieldId());
                                        fieldList.add(childFields);
                                    }
                                    if(fieldLastName!=null && fieldLastName.getEnabled()) {
                                        childFields = new HashMap<>();
                                        childFields.put("fieldName", "Last Name");
                                        childFields.put("fieldValue", resourceMgmt.getLastName());
                                        childFields.put("fieldType", "String");
                                        childFields.put("fieldId", fieldLastName.getFieldId());
                                        fieldList.add(childFields);
                                    }
                                    childFields = new HashMap<>();
                                    childFields.put("fieldName", "Employee Id");
                                    childFields.put("fieldValue", resourceMgmt.getResourceId());
                                    childFields.put("fieldType", "Number");
                                    childFields.put("fieldId", getDirectEmployeeForecastDetails.getField_id());
                                    fieldList.add(childFields);
                                }
                            } else {
                                fields.put("fieldName", getDirectEmployeeForecastDetails.getFields());
                                if(getDirectEmployeeForecastDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                    String stringDate = FinancialUtils.formatStringDateToStringDate(getDirectEmployeeForecastDetails.getField_value(), timeZone, dateFormat);
                                    fields.put("fieldValue", stringDate);
                                }
                                else{
                                    fields.put("fieldValue", getDirectEmployeeForecastDetails.getField_value());
                                }
                                fields.put("fieldType", getDirectEmployeeForecastDetails.getField_type());
                                if(!Objects.equals(getDirectEmployeeForecastDetails.getField_value(), getDirectEmployeeForecastDetails.getPicklist_id())){
                                    fields.put("picklist_id", getDirectEmployeeForecastDetails.getPicklist_id());
                                }
                                fields.put("fieldId", getDirectEmployeeForecastDetails.getField_id());
                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                    fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                    directEmployeeForecastDetailsMaster.setFields(fieldList);
                }
                getDirectEmployeeForecastDetailsMasterList.add(directEmployeeForecastDetailsMaster);
            }

            getDirectEmployeeForecastDetailsMasterList.sort(Comparator.comparingInt(GetDirectEmployeeForecastDetailsMaster::getRow_order));
        }
        return getDirectEmployeeForecastDetailsMasterList;
    }


    /*
    Other Direct In-House Cost Table
     */

    public void createUpdateOtherDirectInHouseBudgetDetails(List<OtherDirectInHouseCostBudgetDto> otherDirectInHouseCostBudgetDtoList) {

        try {
            for (OtherDirectInHouseCostBudgetDto otherDirectInHouseCostBudgetDtoObj : otherDirectInHouseCostBudgetDtoList) {
                Integer id = 0;

                OtherDirectInHouseCostBudget otherDirectInHouseCostBudget = null;

                if (ObjectUtils.isEmpty(otherDirectInHouseCostBudgetDtoObj.getOtherDirectInHouseCostBudgetId())
                        || otherDirectInHouseCostBudgetDtoObj.getOtherDirectInHouseCostBudgetId() == 0) {

                    otherDirectInHouseCostBudget = new OtherDirectInHouseCostBudget();
                    otherDirectInHouseCostBudget.setCreatedBy(0);
                    otherDirectInHouseCostBudget.setCreatedDate(new Date());

                } else {

                    Optional<OtherDirectInHouseCostBudget> otherDirectInHouseCostBudgetObj =
                            otherDirectInHouseCostBudgetRepo.findById(otherDirectInHouseCostBudgetDtoObj.getOtherDirectInHouseCostBudgetId());

                    if (otherDirectInHouseCostBudgetObj.isPresent()) {
                        otherDirectInHouseCostBudget = otherDirectInHouseCostBudgetObj.get();
                        otherDirectInHouseCostBudget.setModifiedBy(0);
                        otherDirectInHouseCostBudget.setModifiedDate(new Date());
                    } else {
                        otherDirectInHouseCostBudget = new OtherDirectInHouseCostBudget();
                        otherDirectInHouseCostBudget.setCreatedBy(0);
                        otherDirectInHouseCostBudget.setCreatedDate(new Date());
                    }
                }
                otherDirectInHouseCostBudget.setRowOrder(otherDirectInHouseCostBudgetDtoObj.getRowOrder());
                otherDirectInHouseCostBudget.setIsActive(otherDirectInHouseCostBudgetDtoObj.getIsActive());
                otherDirectInHouseCostBudget.setFinancialYear(otherDirectInHouseCostBudgetDtoObj.getFinancialYear());

                OtherDirectInHouseCostBudget savedOtherDirectInHouseCostBudget = otherDirectInHouseCostBudgetRepo.save(otherDirectInHouseCostBudget);

//            handle custom field data
                if (!ObjectUtils.isEmpty(otherDirectInHouseCostBudgetDtoObj.getOtherDirectInHouseCostBudgetData())) {

                    Set<Integer> fieldIdList = otherDirectInHouseCostBudgetDtoObj.getOtherDirectInHouseCostBudgetData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<OtherDirectInHouseCostFieldValueBudget> otherDirectInHouseCostBudgetsList = new ArrayList<>();

                        for (OtherDirectInHouseCostData otherDirectInHouseCostBudgetData : otherDirectInHouseCostBudgetDtoObj.getOtherDirectInHouseCostBudgetData()) {

                            if (availableFieldList.contains(otherDirectInHouseCostBudgetData.getFieldId())) {

                                Optional<OtherDirectInHouseCostFieldValueBudget> otherDirectInHouseCostBudgetObj =
                                        otherDirectInHouseCostFieldValueBudgetRepo.findByOtherDirectInHouseCostBudgetIdAndFieldId(savedOtherDirectInHouseCostBudget.getId(), otherDirectInHouseCostBudgetData.getFieldId());

                                OtherDirectInHouseCostFieldValueBudget otherDirectInHouseCostFieldValueBudget = null;

                                if (otherDirectInHouseCostBudgetObj.isPresent()) {
                                    otherDirectInHouseCostFieldValueBudget = otherDirectInHouseCostBudgetObj.get();
                                    otherDirectInHouseCostFieldValueBudget.setModifiedBy(0);
                                    otherDirectInHouseCostFieldValueBudget.setModifiedDate(new Date());
                                } else {
                                    otherDirectInHouseCostFieldValueBudget = new OtherDirectInHouseCostFieldValueBudget();
                                    otherDirectInHouseCostFieldValueBudget.setCreatedBy(0);
                                    otherDirectInHouseCostFieldValueBudget.setCreatedDate(new Date());
                                }
                                otherDirectInHouseCostFieldValueBudget.setOtherDirectInHouseCostBudgetId(savedOtherDirectInHouseCostBudget.getId());
                                otherDirectInHouseCostFieldValueBudget.setFieldId(otherDirectInHouseCostBudgetData.getFieldId());

                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(otherDirectInHouseCostBudgetData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST)) {
                                        if(!ObjectUtils.isEmpty(otherDirectInHouseCostBudgetData.getFieldValue())) {
                                            if(otherDirectInHouseCostBudgetData.getFieldName().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(otherDirectInHouseCostBudgetData.getPicklistId().split(",")[0]));
                                                if (locationPicklist.isPresent()) {
                                                    otherDirectInHouseCostFieldValueBudget.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else if(otherDirectInHouseCostBudgetData.getFieldName().equalsIgnoreCase(FinancialUtils.FINANCE_PROJECT)){
                                                Optional<ProjectMgmt> projectPicklist = projectMgmtRepo.findById(Integer.parseInt(otherDirectInHouseCostBudgetData.getPicklistId().split(",")[0]));
                                                if (projectPicklist.isPresent()) {
                                                    otherDirectInHouseCostFieldValueBudget.setFieldValue(projectPicklist.get().getProjectId().toString());
                                                }
                                            }
                                            else{
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(otherDirectInHouseCostBudgetData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    otherDirectInHouseCostFieldValueBudget.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                        }
                                        else{
                                            otherDirectInHouseCostFieldValueBudget.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(otherDirectInHouseCostBudgetData.getFieldValue())) {
                                            Set<Integer> pickListIds = Stream.of(otherDirectInHouseCostBudgetData.getPicklistId().split(","))
                                                    .map(Integer::parseInt).collect(Collectors.toSet());

                                            List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                            if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                otherDirectInHouseCostFieldValueBudget.setFieldValue(financeCustomPicklists.stream()
                                                        .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                            }
                                        }
                                        else{
                                            otherDirectInHouseCostFieldValueBudget.setFieldValue("");
                                        }
                                    } else {
                                        otherDirectInHouseCostFieldValueBudget.setFieldValue(otherDirectInHouseCostBudgetData.getFieldValue());
                                    }

                                }
                                otherDirectInHouseCostBudgetsList.add(otherDirectInHouseCostFieldValueBudget);
                            }
                        }
                        if (!ObjectUtils.isEmpty(otherDirectInHouseCostBudgetsList)) {
                            otherDirectInHouseCostFieldValueBudgetRepo.saveAll(otherDirectInHouseCostBudgetsList);
                        }
                    }
                }

//            handle finance expense data
                if (!ObjectUtils.isEmpty(otherDirectInHouseCostBudgetDtoObj.getOtherDirectInHouseBudgetExpenses())) {

                    List<OtherDirectInHouseCostBudgetExpense> otherDirectInHouseCostBudgetExpenses = new ArrayList<>();

                    for (OtherDirectInHouseBudgetExpense financialExpense : otherDirectInHouseCostBudgetDtoObj.getOtherDirectInHouseBudgetExpenses()) {

                        Optional<OtherDirectInHouseCostBudgetExpense> otherDirectInHouseCostExpenseObj;

                        if (financialExpense.getId() == null) {
                            financialExpense.setId(0);
                        }
                        otherDirectInHouseCostExpenseObj = otherDirectInHouseBudgetExpenseRepo.findById(financialExpense.getId());

                        OtherDirectInHouseCostBudgetExpense otherDirectInHouseCostBudgetExpense = new OtherDirectInHouseCostBudgetExpense();

                        List<FinancialMonthlyExpenseData> monthlyExpenseData = financialExpense.getFinancialMonthlyExpenseDataList();

                        if (otherDirectInHouseCostExpenseObj.isPresent()) {
                            otherDirectInHouseCostBudgetExpense.setId(otherDirectInHouseCostExpenseObj.get().getId());
                        }

                        for (FinancialMonthlyExpenseData eachMonth : monthlyExpenseData) {
                            switch (eachMonth.getMonth().toLowerCase()) {
                                case FinancialUtils.JANUARY:
                                    otherDirectInHouseCostBudgetExpense.setJan(eachMonth.getValue());
                                    break;
                                case FinancialUtils.FEBRUARY:
                                    otherDirectInHouseCostBudgetExpense.setFeb(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MARCH:
                                    otherDirectInHouseCostBudgetExpense.setMar(eachMonth.getValue());
                                    break;
                                case FinancialUtils.APRIL:
                                    otherDirectInHouseCostBudgetExpense.setApr(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MAY:
                                    otherDirectInHouseCostBudgetExpense.setMay(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JUNE:
                                    otherDirectInHouseCostBudgetExpense.setJun(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JULY:
                                    otherDirectInHouseCostBudgetExpense.setJul(eachMonth.getValue());
                                    break;
                                case FinancialUtils.AUGUST:
                                    otherDirectInHouseCostBudgetExpense.setAug(eachMonth.getValue());
                                    break;
                                case FinancialUtils.SEPTEMBER:
                                    otherDirectInHouseCostBudgetExpense.setSep(eachMonth.getValue());
                                    break;
                                case FinancialUtils.OCTOBER:
                                    otherDirectInHouseCostBudgetExpense.setOct(eachMonth.getValue());
                                    break;
                                case FinancialUtils.NOVEMBER:
                                    otherDirectInHouseCostBudgetExpense.setNov(eachMonth.getValue());
                                    break;
                                case FinancialUtils.DECEMBER:
                                    otherDirectInHouseCostBudgetExpense.setDec(eachMonth.getValue());
                                    break;
                                default:
                            }
                        }

                        otherDirectInHouseCostBudgetExpense.setOtherDirectInHouseCostBudgetId(savedOtherDirectInHouseCostBudget.getId());
                        otherDirectInHouseCostBudgetExpense.setFinancialYear(financialExpense.getFinancialYear());

                        otherDirectInHouseCostBudgetExpenses.add(otherDirectInHouseCostBudgetExpense);
                    }
                    if (!ObjectUtils.isEmpty(otherDirectInHouseCostBudgetExpenses)) {
                        otherDirectInHouseBudgetExpenseRepo.saveAll(otherDirectInHouseCostBudgetExpenses);
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public List<GetOtherDirectCostInHouseBudgetDetailMaster> getOtherDirectInHouseCostBudgetDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        String startMonth = generalSettings.getFinancialYearStart().substring(0,3).toLowerCase();

        List<GetOtherDirectCostInHouseBudgetDetailMaster> getOtherDirectCostInHouseBudgetDetailMasterList = null;
        String otherDirectCostInHouseBudgetQuery = FinancialUtils.getFile("worksheet/getOtherDirectInHouseCostBudgetDetails.txt");

        Query query = entityManager.createNativeQuery(otherDirectCostInHouseBudgetQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetOtherDirectInHouseBudgetDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetOtherDirectInHouseBudgetDetails> getOtherDirectInHouseBudgetDetails = query.getResultList();

        if (getOtherDirectInHouseBudgetDetails != null && !getOtherDirectInHouseBudgetDetails.isEmpty()) {

            Map<Integer, List<GetOtherDirectInHouseBudgetDetails>> groupByForecastId = getOtherDirectInHouseBudgetDetails.stream().collect(groupingBy(GetOtherDirectInHouseBudgetDetails::getOther_direct_inhouse_cost_budget_id));
            getOtherDirectCostInHouseBudgetDetailMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetOtherDirectInHouseBudgetDetails>> entry : groupByForecastId.entrySet()) {

                GetOtherDirectCostInHouseBudgetDetailMaster getOtherDirectCostInHouseBudgetDetailMaster = new GetOtherDirectCostInHouseBudgetDetailMaster();

                List<Map<String, Object>> fieldList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    getOtherDirectCostInHouseBudgetDetailMaster.setOtherDirectInHouseCostBudgetId(entry.getValue().get(0).getOther_direct_inhouse_cost_budget_id());

                    getOtherDirectCostInHouseBudgetDetailMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getOtherDirectCostInHouseBudgetDetailMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getOtherDirectCostInHouseBudgetDetailMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    for (GetOtherDirectInHouseBudgetDetails otherDirectInHouseBudgetDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(otherDirectInHouseBudgetDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", otherDirectInHouseBudgetDetails.getFields());
                            if(otherDirectInHouseBudgetDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                String stringDate = FinancialUtils.formatStringDateToStringDate(otherDirectInHouseBudgetDetails.getField_value(), timeZone, dateFormat);
                                fields.put("fieldValue", stringDate);
                            }
                            else{
                                fields.put("fieldValue", otherDirectInHouseBudgetDetails.getField_value());
                            }
                            if(!Objects.equals(otherDirectInHouseBudgetDetails.getField_value(), otherDirectInHouseBudgetDetails.getPicklist_id())){
                                fields.put("picklist_id", otherDirectInHouseBudgetDetails.getPicklist_id());
                            }
                            fields.put("fieldType", otherDirectInHouseBudgetDetails.getField_type());
                            fields.put("fieldId", otherDirectInHouseBudgetDetails.getField_id());
//                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                }
                List<OtherDirectInHouseCostBudgetExpense> otherDirectInHouseCostBudgetExpenseList = otherDirectInHouseBudgetExpenseRepo.findAllByOtherDirectInHouseCostBudgetId(entry.getKey());
                List<OtherDirectInHouseBudgetExpense> otherDirectInHouseCostBudgetList = new ArrayList<>();
                if (!ObjectUtils.isEmpty(otherDirectInHouseCostBudgetExpenseList)) {
                    otherDirectInHouseCostBudgetExpenseList.sort(Comparator.comparingInt(OtherDirectInHouseCostBudgetExpense::getFinancialYear));
                    for (OtherDirectInHouseCostBudgetExpense otherDirectInHouseCostBudgetExpense : otherDirectInHouseCostBudgetExpenseList) {
                        Map<String, Object> objToMap = objectMapper.convertValue(otherDirectInHouseCostBudgetExpense, Map.class);
                        OtherDirectInHouseBudgetExpense otherDirectInHouseBudgetExpense = new OtherDirectInHouseBudgetExpense();
                        otherDirectInHouseBudgetExpense.setId(otherDirectInHouseCostBudgetExpense.getId());
                        otherDirectInHouseBudgetExpense.setOtherDirectInHouseCostBudgetExpenseId(entry.getKey());
                        otherDirectInHouseBudgetExpense.setFinancialYear((Integer) objToMap.get("financial_year"));
                        otherDirectInHouseBudgetExpense.setFinancialMonthlyExpenseDataList(setOtherDirectInHouseCostBudgetExpenseData(objToMap, startMonth, financialYear.equals(objToMap.get("financial_year"))));
                        otherDirectInHouseCostBudgetList.add(otherDirectInHouseBudgetExpense);
                    }
                }
                fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                getOtherDirectCostInHouseBudgetDetailMaster.setFields(fieldList);
                getOtherDirectCostInHouseBudgetDetailMaster.setOtherDirectInHouseBudgetExpenses(otherDirectInHouseCostBudgetList);
                getOtherDirectCostInHouseBudgetDetailMasterList.add(getOtherDirectCostInHouseBudgetDetailMaster);
            }
            getOtherDirectCostInHouseBudgetDetailMasterList.sort(Comparator.comparingInt(GetOtherDirectCostInHouseBudgetDetailMaster::getRow_order));
        }

        return getOtherDirectCostInHouseBudgetDetailMasterList;
    }

    private List<FinancialMonthlyExpenseData> setOtherDirectInHouseCostBudgetExpenseData(Map<String, Object> objToMap, String startMonth, boolean isSameFinancialYear) {

        List<FinancialMonthlyExpenseData> financialMonthlyExpenseData = new ArrayList<>();

        Map<String, Object> collect = objToMap.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        collect.remove("id");
        collect.remove("other_direct_in_house_cost_budget_id");

        Map<Integer, FinancialMonthlyExpenseData> orderedData = new HashMap<>();

        for (Map.Entry<String, Object> getVendorBudgetDetails : collect.entrySet()) {
            if (!getVendorBudgetDetails.getKey().equalsIgnoreCase("financial_year")) {
                FinancialMonthlyExpenseData monthlyExpenseData = new FinancialMonthlyExpenseData(getVendorBudgetDetails.getKey(), (Long) getVendorBudgetDetails.getValue());
                orderedData.put(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(monthlyExpenseData.getMonth()), monthlyExpenseData);
            }
        }
        if(isSameFinancialYear){
            for(int i = FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i<=12; i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        else{
            for(int i = 1; i<FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }

        return financialMonthlyExpenseData;
    }

    public void createUpdateOtherDirectInHouseForecastDetails(List<OtherDirectInHouseCostForecastDto> otherDirectInHouseCostForecastDtoList) {

        try {
            for (OtherDirectInHouseCostForecastDto otherDirectInHouseCostForecastDtoObj : otherDirectInHouseCostForecastDtoList) {
                Integer id = 0;

                OtherDirectInHouseCostForecast otherDirectInHouseCostForecast = null;

                if (ObjectUtils.isEmpty(otherDirectInHouseCostForecastDtoObj.getOtherDirectInHouseCostForecastId())
                        || otherDirectInHouseCostForecastDtoObj.getOtherDirectInHouseCostForecastId() == 0) {

                    otherDirectInHouseCostForecast = new OtherDirectInHouseCostForecast();
                    otherDirectInHouseCostForecast.setCreatedBy(0);
                    otherDirectInHouseCostForecast.setCreatedDate(new Date());

                } else {

                    Optional<OtherDirectInHouseCostForecast> otherDirectInHouseCostForecastObj =
                            otherDirectInHouseCostForecastRepo.findById(otherDirectInHouseCostForecastDtoObj.getOtherDirectInHouseCostForecastId());

                    if (otherDirectInHouseCostForecastObj.isPresent()) {
                        otherDirectInHouseCostForecast = otherDirectInHouseCostForecastObj.get();
                        otherDirectInHouseCostForecast.setModifiedBy(0);
                        otherDirectInHouseCostForecast.setModifiedDate(new Date());
                    } else {
                        otherDirectInHouseCostForecast = new OtherDirectInHouseCostForecast();
                        otherDirectInHouseCostForecast.setCreatedBy(0);
                        otherDirectInHouseCostForecast.setCreatedDate(new Date());
                    }
                }
                otherDirectInHouseCostForecast.setRowOrder(otherDirectInHouseCostForecastDtoObj.getRowOrder());
                otherDirectInHouseCostForecast.setIsActive(otherDirectInHouseCostForecastDtoObj.getIsActive());
                otherDirectInHouseCostForecast.setFinancialYear(otherDirectInHouseCostForecastDtoObj.getFinancialYear());

                OtherDirectInHouseCostForecast savedOtherDirectInHouseCostForecast = otherDirectInHouseCostForecastRepo.save(otherDirectInHouseCostForecast);

//            handle custom field data
                if (!ObjectUtils.isEmpty(otherDirectInHouseCostForecastDtoObj.getOtherDirectInHouseCostForecastData())) {

                    Set<Integer> fieldIdList = otherDirectInHouseCostForecastDtoObj.getOtherDirectInHouseCostForecastData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<OtherDirectInHouseCostFieldValueForecast> otherDirectInHouseCostForecastsList = new ArrayList<>();

                        for (OtherDirectInHouseCostData otherDirectInHouseCostForecastData : otherDirectInHouseCostForecastDtoObj.getOtherDirectInHouseCostForecastData()) {

                            if (availableFieldList.contains(otherDirectInHouseCostForecastData.getFieldId())) {

                                Optional<OtherDirectInHouseCostFieldValueForecast> otherDirectInHouseCostForecastObj =
                                        otherDirectInHouseCostFieldValueForecastRepo.findByOtherDirectInHouseCostForecastIdAndFieldId(savedOtherDirectInHouseCostForecast.getId(), otherDirectInHouseCostForecastData.getFieldId());

                                OtherDirectInHouseCostFieldValueForecast otherDirectInHouseCostFieldValueForecast = null;

                                if (otherDirectInHouseCostForecastObj.isPresent()) {
                                    otherDirectInHouseCostFieldValueForecast = otherDirectInHouseCostForecastObj.get();
                                    otherDirectInHouseCostFieldValueForecast.setModifiedBy(0);
                                    otherDirectInHouseCostFieldValueForecast.setModifiedDate(new Date());
                                } else {
                                    otherDirectInHouseCostFieldValueForecast = new OtherDirectInHouseCostFieldValueForecast();
                                    otherDirectInHouseCostFieldValueForecast.setCreatedBy(0);
                                    otherDirectInHouseCostFieldValueForecast.setCreatedDate(new Date());
                                }
                                otherDirectInHouseCostFieldValueForecast.setOtherDirectInHouseCostForecastId(savedOtherDirectInHouseCostForecast.getId());
                                otherDirectInHouseCostFieldValueForecast.setFieldId(otherDirectInHouseCostForecastData.getFieldId());

                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(otherDirectInHouseCostForecastData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST)) {
                                        if(!ObjectUtils.isEmpty(otherDirectInHouseCostForecastData.getFieldValue())) {
                                            if(otherDirectInHouseCostForecastData.getFieldName().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(otherDirectInHouseCostForecastData.getPicklistId().split(",")[0]));
                                                if (locationPicklist.isPresent()) {
                                                    otherDirectInHouseCostFieldValueForecast.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else if(otherDirectInHouseCostForecastData.getFieldName().equalsIgnoreCase(FinancialUtils.FINANCE_PROJECT)){
                                                Optional<ProjectMgmt> projectPicklist = projectMgmtRepo.findById(Integer.parseInt(otherDirectInHouseCostForecastData.getPicklistId().split(",")[0]));
                                                if (projectPicklist.isPresent()) {
                                                    otherDirectInHouseCostFieldValueForecast.setFieldValue(projectPicklist.get().getProjectId().toString());
                                                }
                                            }
                                            else{
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(otherDirectInHouseCostForecastData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    otherDirectInHouseCostFieldValueForecast.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                        }
                                        else{
                                            otherDirectInHouseCostFieldValueForecast.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(otherDirectInHouseCostForecastData.getFieldValue())) {
                                            Set<Integer> pickListIds = Stream.of(otherDirectInHouseCostForecastData.getPicklistId().split(","))
                                                    .map(Integer::parseInt).collect(Collectors.toSet());

                                            List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                            if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                otherDirectInHouseCostFieldValueForecast.setFieldValue(financeCustomPicklists.stream()
                                                        .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                            }
                                        }
                                        else{
                                            otherDirectInHouseCostFieldValueForecast.setFieldValue("");
                                        }
                                    } else {
                                        otherDirectInHouseCostFieldValueForecast.setFieldValue(otherDirectInHouseCostForecastData.getFieldValue());
                                    }

                                }
                                otherDirectInHouseCostForecastsList.add(otherDirectInHouseCostFieldValueForecast);
                            }
                        }
                        if (!ObjectUtils.isEmpty(otherDirectInHouseCostForecastsList)) {
                            otherDirectInHouseCostFieldValueForecastRepo.saveAll(otherDirectInHouseCostForecastsList);
                        }
                    }
                }

//            handle finance expense data
                if (!ObjectUtils.isEmpty(otherDirectInHouseCostForecastDtoObj.getOtherDirectInHouseForecastExpense())) {

                    List<OtherDirectInHouseCostForecastExpense> otherDirectInHouseCostForecastExpenses = new ArrayList<>();

                    for (OtherDirectInHouseForecastExpense financialExpense : otherDirectInHouseCostForecastDtoObj.getOtherDirectInHouseForecastExpense()) {

                        Optional<OtherDirectInHouseCostForecastExpense> otherDirectInHouseCostExpenseObj;

                        if (financialExpense.getId() == null) {
                            financialExpense.setId(0);
                        }
                        otherDirectInHouseCostExpenseObj = otherDirectInHouseForecastExpenseRepo.findById(financialExpense.getId());

                        OtherDirectInHouseCostForecastExpense otherDirectInHouseCostForecastExpense = new OtherDirectInHouseCostForecastExpense();

                        List<FinancialMonthlyExpenseData> monthlyExpenseData = financialExpense.getFinancialMonthlyExpenseDataList();

                        if (otherDirectInHouseCostExpenseObj.isPresent()) {
                            otherDirectInHouseCostForecastExpense.setId(otherDirectInHouseCostExpenseObj.get().getId());
                        }

                        for (FinancialMonthlyExpenseData eachMonth : monthlyExpenseData) {
                            switch (eachMonth.getMonth().toLowerCase()) {
                                case FinancialUtils.JANUARY:
                                    otherDirectInHouseCostForecastExpense.setJan(eachMonth.getValue());
                                    break;
                                case FinancialUtils.FEBRUARY:
                                    otherDirectInHouseCostForecastExpense.setFeb(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MARCH:
                                    otherDirectInHouseCostForecastExpense.setMar(eachMonth.getValue());
                                    break;
                                case FinancialUtils.APRIL:
                                    otherDirectInHouseCostForecastExpense.setApr(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MAY:
                                    otherDirectInHouseCostForecastExpense.setMay(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JUNE:
                                    otherDirectInHouseCostForecastExpense.setJun(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JULY:
                                    otherDirectInHouseCostForecastExpense.setJul(eachMonth.getValue());
                                    break;
                                case FinancialUtils.AUGUST:
                                    otherDirectInHouseCostForecastExpense.setAug(eachMonth.getValue());
                                    break;
                                case FinancialUtils.SEPTEMBER:
                                    otherDirectInHouseCostForecastExpense.setSep(eachMonth.getValue());
                                    break;
                                case FinancialUtils.OCTOBER:
                                    otherDirectInHouseCostForecastExpense.setOct(eachMonth.getValue());
                                    break;
                                case FinancialUtils.NOVEMBER:
                                    otherDirectInHouseCostForecastExpense.setNov(eachMonth.getValue());
                                    break;
                                case FinancialUtils.DECEMBER:
                                    otherDirectInHouseCostForecastExpense.setDec(eachMonth.getValue());
                                    break;
                                default:
                            }
                        }

                        otherDirectInHouseCostForecastExpense.setOtherDirectInHouseCostForecastId(savedOtherDirectInHouseCostForecast.getId());
                        otherDirectInHouseCostForecastExpense.setFinancialYear(financialExpense.getFinancialYear());

                        otherDirectInHouseCostForecastExpenses.add(otherDirectInHouseCostForecastExpense);
                    }
                    if (!ObjectUtils.isEmpty(otherDirectInHouseCostForecastExpenses)) {
                        otherDirectInHouseForecastExpenseRepo.saveAll(otherDirectInHouseCostForecastExpenses);
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public List<GetOtherDirectCostInHouseForecastDetailMaster> getOtherDirectInHouseCostForecastDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        String startMonth = generalSettings.getFinancialYearStart().substring(0,3).toLowerCase();

        List<GetOtherDirectCostInHouseForecastDetailMaster> getOtherDirectCostInHouseForecastDetailMasterList = null;
        String otherDirectCostInHouseForecastQuery = FinancialUtils.getFile("worksheet/getOtherDirectInHouseCostForecastDetails.txt");

        Query query = entityManager.createNativeQuery(otherDirectCostInHouseForecastQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetOtherDirectInHouseForecastDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetOtherDirectInHouseForecastDetails> getOtherDirectInHouseForecastDetails = query.getResultList();

        if (getOtherDirectInHouseForecastDetails != null && !getOtherDirectInHouseForecastDetails.isEmpty()) {

            Map<Integer, List<GetOtherDirectInHouseForecastDetails>> groupByForecastId = getOtherDirectInHouseForecastDetails.stream().collect(groupingBy(GetOtherDirectInHouseForecastDetails::getOther_direct_inhouse_cost_forecast_id));
            getOtherDirectCostInHouseForecastDetailMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetOtherDirectInHouseForecastDetails>> entry : groupByForecastId.entrySet()) {

                GetOtherDirectCostInHouseForecastDetailMaster getOtherDirectCostInHouseForecastDetailMaster = new GetOtherDirectCostInHouseForecastDetailMaster();

                List<Map<String, Object>> fieldList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    getOtherDirectCostInHouseForecastDetailMaster.setOtherDirectInHouseCostForecastId(entry.getValue().get(0).getOther_direct_inhouse_cost_forecast_id());

                    getOtherDirectCostInHouseForecastDetailMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getOtherDirectCostInHouseForecastDetailMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getOtherDirectCostInHouseForecastDetailMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    for (GetOtherDirectInHouseForecastDetails otherDirectInHouseForecastDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(otherDirectInHouseForecastDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", otherDirectInHouseForecastDetails.getFields());
                            if(otherDirectInHouseForecastDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                String stringDate = FinancialUtils.formatStringDateToStringDate(otherDirectInHouseForecastDetails.getField_value(), timeZone, dateFormat);
                                fields.put("fieldValue", stringDate);
                            }
                            else{
                                fields.put("fieldValue", otherDirectInHouseForecastDetails.getField_value());
                            }
                            if(!Objects.equals(otherDirectInHouseForecastDetails.getField_value(), otherDirectInHouseForecastDetails.getPicklist_id())){
                                fields.put("picklist_id", otherDirectInHouseForecastDetails.getPicklist_id());
                            }
                            fields.put("fieldType", otherDirectInHouseForecastDetails.getField_type());
                            fields.put("fieldId", otherDirectInHouseForecastDetails.getField_id());
//                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                }
                List<OtherDirectInHouseCostForecastExpense> otherDirectInHouseCostForecastExpenseList = otherDirectInHouseForecastExpenseRepo.findAllByOtherDirectInHouseCostForecastId(entry.getKey());
                List<OtherDirectInHouseForecastExpense> otherDirectInHouseCostForecastList = new ArrayList<>();
                if (!ObjectUtils.isEmpty(otherDirectInHouseCostForecastExpenseList)) {
                    otherDirectInHouseCostForecastExpenseList.sort(Comparator.comparingInt(OtherDirectInHouseCostForecastExpense::getFinancialYear));
                    for (OtherDirectInHouseCostForecastExpense otherDirectInHouseCostForecastExpense : otherDirectInHouseCostForecastExpenseList) {
                        Map<String, Object> objToMap = objectMapper.convertValue(otherDirectInHouseCostForecastExpense, Map.class);
                        OtherDirectInHouseForecastExpense otherDirectInHouseForecastExpense = new OtherDirectInHouseForecastExpense();
                        otherDirectInHouseForecastExpense.setId(otherDirectInHouseCostForecastExpense.getId());
                        otherDirectInHouseForecastExpense.setOtherDirectInHouseCostForecastExpenseId(entry.getKey());
                        otherDirectInHouseForecastExpense.setFinancialYear((Integer) objToMap.get("financial_year"));
                        otherDirectInHouseForecastExpense.setFinancialMonthlyExpenseDataList(setOtherDirectInHouseCostForecastExpenseData(objToMap, startMonth, financialYear.equals(objToMap.get("financial_year"))));
                        otherDirectInHouseCostForecastList.add(otherDirectInHouseForecastExpense);
                    }
                }
                fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                getOtherDirectCostInHouseForecastDetailMaster.setFields(fieldList);
                getOtherDirectCostInHouseForecastDetailMaster.setOtherDirectInHouseForecastExpense(otherDirectInHouseCostForecastList);
                getOtherDirectCostInHouseForecastDetailMasterList.add(getOtherDirectCostInHouseForecastDetailMaster);
            }

            getOtherDirectCostInHouseForecastDetailMasterList.sort(Comparator.comparingInt(GetOtherDirectCostInHouseForecastDetailMaster::getRow_order));
        }

        return getOtherDirectCostInHouseForecastDetailMasterList;
    }

    private List<FinancialMonthlyExpenseData> setOtherDirectInHouseCostForecastExpenseData(Map<String, Object> objToMap, String startMonth, boolean isSameFinancialYear) {

        List<FinancialMonthlyExpenseData> financialMonthlyExpenseData = new ArrayList<>();

        Map<String, Object> collect = objToMap.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        collect.remove("id");
        collect.remove("other_direct_in_house_cost_forecast_id");

        Map<Integer, FinancialMonthlyExpenseData> orderedData = new HashMap<>();

        for (Map.Entry<String, Object> getVendorBudgetDetails : collect.entrySet()) {
            if (!getVendorBudgetDetails.getKey().equalsIgnoreCase("financial_year")) {
                FinancialMonthlyExpenseData monthlyExpenseData = new FinancialMonthlyExpenseData(getVendorBudgetDetails.getKey(), (Long) getVendorBudgetDetails.getValue());
                
                orderedData.put(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(monthlyExpenseData.getMonth()), monthlyExpenseData);
            }
        }
        if(isSameFinancialYear){
            for(int i = FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i<=12; i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        else{
            for(int i = 1; i<FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }

        return financialMonthlyExpenseData;
    }


    /*
    Vendor Employee Details Table
     */

    public void createUpdateVendorEmployeeDetailsBudget(List<VendorEmployeeDetailsBudgetDto> vendorEmployeeDetailsBudgetDtoList) {

        try {
            for (VendorEmployeeDetailsBudgetDto vendorEmployeeDetailsBudgetDtoObj : vendorEmployeeDetailsBudgetDtoList) {
                Integer id = 0;

                VendorEmployeeDetailsBudget vendorEmployeeDetailsBudget = null;

                if (ObjectUtils.isEmpty(vendorEmployeeDetailsBudgetDtoObj.getVendorEmploymentDetailsBudgetId())
                        || vendorEmployeeDetailsBudgetDtoObj.getVendorEmploymentDetailsBudgetId() == 0) {

                    vendorEmployeeDetailsBudget = new VendorEmployeeDetailsBudget();
                    vendorEmployeeDetailsBudget.setCreatedBy(0);
                    vendorEmployeeDetailsBudget.setCreatedDate(new Date());

                } else {

                    Optional<VendorEmployeeDetailsBudget> vendorEmployeeDetailsBudgetObj =
                            vendorEmployeeDetailsBudgetRepo.findById(vendorEmployeeDetailsBudgetDtoObj.getVendorEmploymentDetailsBudgetId());

                    if (vendorEmployeeDetailsBudgetObj.isPresent()) {
                        vendorEmployeeDetailsBudget = vendorEmployeeDetailsBudgetObj.get();
                        vendorEmployeeDetailsBudget.setModifiedBy(0);
                        vendorEmployeeDetailsBudget.setModifiedDate(new Date());
                    } else {
                        vendorEmployeeDetailsBudget = new VendorEmployeeDetailsBudget();
                        vendorEmployeeDetailsBudget.setCreatedBy(0);
                        vendorEmployeeDetailsBudget.setCreatedDate(new Date());
                    }
                }
                vendorEmployeeDetailsBudget.setRowOrder(vendorEmployeeDetailsBudgetDtoObj.getRowOrder());
                vendorEmployeeDetailsBudget.setIsActive(vendorEmployeeDetailsBudgetDtoObj.getIsActive());
                vendorEmployeeDetailsBudget.setFinancialYear(vendorEmployeeDetailsBudgetDtoObj.getFinancialYear());

                VendorEmployeeDetailsBudget savedVendorEmployeeDetailsBudget = vendorEmployeeDetailsBudgetRepo.save(vendorEmployeeDetailsBudget);

//            handle custom field data
                if (!ObjectUtils.isEmpty(vendorEmployeeDetailsBudgetDtoObj.getVendorEmployeeDetailsData())) {

                    Set<Integer> fieldIdList = vendorEmployeeDetailsBudgetDtoObj.getVendorEmployeeDetailsData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<FinanceMgmtFieldValueBudgetVendorEmp> financeVendorEmployeeBudgetsList = new ArrayList<>();

                        for (VendorEmployeeDetailsData vendorEmployeeDetailsData : vendorEmployeeDetailsBudgetDtoObj.getVendorEmployeeDetailsData()) {

                            if (availableFieldList.contains(vendorEmployeeDetailsData.getFieldId())
                                    && !vendorEmployeeDetailsData.getFieldName().equalsIgnoreCase("First Name")
                                    && !vendorEmployeeDetailsData.getFieldName().equalsIgnoreCase("Last Name")) {

                                Optional<FinanceMgmtFieldValueBudgetVendorEmp> financeVendorEmployeeBudgetObj =
                                        financeMgmtFieldValueBudgetVendorEmpRepo.findByVendorBudgetIdAndFieldId(savedVendorEmployeeDetailsBudget.getId(), vendorEmployeeDetailsData.getFieldId());

                                FinanceMgmtFieldValueBudgetVendorEmp financeVendorEmployeeBudget = null;

                                if (financeVendorEmployeeBudgetObj.isPresent()) {
                                    financeVendorEmployeeBudget = financeVendorEmployeeBudgetObj.get();
                                    financeVendorEmployeeBudget.setModifiedBy(0);
                                    financeVendorEmployeeBudget.setModifiedDate(new Date());
                                } else {
                                    financeVendorEmployeeBudget = new FinanceMgmtFieldValueBudgetVendorEmp();
                                    financeVendorEmployeeBudget.setCreatedBy(0);
                                    financeVendorEmployeeBudget.setCreatedDate(new Date());
                                }
                                financeVendorEmployeeBudget.setVendorBudgetId(savedVendorEmployeeDetailsBudget.getId());
                                financeVendorEmployeeBudget.setFieldId(vendorEmployeeDetailsData.getFieldId());

                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(vendorEmployeeDetailsData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST) ) {
                                        if(!ObjectUtils.isEmpty(vendorEmployeeDetailsData.getPicklistId())) {
                                            if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(vendorEmployeeDetailsData.getPicklistId().split(",")[0]));

                                                if (locationPicklist.isPresent()) {
                                                    financeVendorEmployeeBudget.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_PROJECT)){
                                                Optional<ProjectMgmt> projectPicklist = projectMgmtRepo.findById(Integer.parseInt(vendorEmployeeDetailsData.getPicklistId().split(",")[0]));
                                                if (projectPicklist.isPresent()) {
                                                    financeVendorEmployeeBudget.setFieldValue(projectPicklist.get().getProjectId().toString());
                                                }
                                            }
                                            else{
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(vendorEmployeeDetailsData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    financeVendorEmployeeBudget.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                        }
                                        else{
                                            financeVendorEmployeeBudget.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(vendorEmployeeDetailsData.getPicklistId())) {
                                            if (findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_DESIGNATION)) {

                                                Set<Integer> pickListIds = Stream.of(vendorEmployeeDetailsData.getPicklistId().split(","))
                                                        .map(Integer::parseInt).collect(Collectors.toSet());

                                                List<ResourceMgmtRoles> designationPicklist = resourceMgmtRolesRepo.findAllById(pickListIds);

                                                if (!ObjectUtils.isEmpty(designationPicklist) && designationPicklist.size() > 0) {
                                                    financeVendorEmployeeBudget.setFieldValue(designationPicklist.stream()
                                                            .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                                }
                                            }
                                            else{
                                                Set<Integer> pickListIds = Stream.of(vendorEmployeeDetailsData.getPicklistId().split(","))
                                                        .map(Integer::parseInt).collect(Collectors.toSet());

                                                List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                                if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                    financeVendorEmployeeBudget.setFieldValue(financeCustomPicklists.stream()
                                                            .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                                }
                                            }
                                            
                                        }
                                        else{
                                            financeVendorEmployeeBudget.setFieldValue("");
                                        }
                                    } else {
                                        financeVendorEmployeeBudget.setFieldValue(vendorEmployeeDetailsData.getFieldValue());
                                    }

                                }
                                financeVendorEmployeeBudgetsList.add(financeVendorEmployeeBudget);
                            }
                        }
                        if (!ObjectUtils.isEmpty(financeVendorEmployeeBudgetsList)) {
                            financeMgmtFieldValueBudgetVendorEmpRepo.saveAll(financeVendorEmployeeBudgetsList);
                        }
                    }
                }

//            handle finance expense data
                if (!ObjectUtils.isEmpty(vendorEmployeeDetailsBudgetDtoObj.getFinancialExpenses())) {


                    List<VendorEmploymentDetailsBudgetExpense> vendorEmploymentDetailsBudgetExpenses = new ArrayList<>();

                    for (FinancialExpense financialExpense : vendorEmployeeDetailsBudgetDtoObj.getFinancialExpenses()) {

//                        if (availableFieldList.contains(financialExpense.getFieldId()) && financeMgmtFieldMapObj.get(0).getFields().contains("Months")) {

                        Optional<VendorEmploymentDetailsBudgetExpense> financeVendorEmployeeBudgetExpenseObj;
                        if (financialExpense.getVendorId() == null) {
                            financialExpense.setId(0);
                        }
                        financeVendorEmployeeBudgetExpenseObj = vendorEmploymentDetailsBudgetExpenseRepo.findById(financialExpense.getId());

                        VendorEmploymentDetailsBudgetExpense financeVendorEmployeeBudgetExpense = new VendorEmploymentDetailsBudgetExpense();

                        List<FinancialMonthlyExpenseData> monthlyExpenseData = financialExpense.getFinancialMonthlyExpenseDataList();

                        if (financeVendorEmployeeBudgetExpenseObj.isPresent()) {
                            financeVendorEmployeeBudgetExpense.setId(financeVendorEmployeeBudgetExpenseObj.get().getId());
                        }

                        for (FinancialMonthlyExpenseData eachMonth : monthlyExpenseData) {
                            switch (eachMonth.getMonth().toLowerCase()) {
                                case FinancialUtils.JANUARY:
                                    financeVendorEmployeeBudgetExpense.setJan(eachMonth.getValue());
                                    break;
                                case FinancialUtils.FEBRUARY:
                                    financeVendorEmployeeBudgetExpense.setFeb(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MARCH:
                                    financeVendorEmployeeBudgetExpense.setMar(eachMonth.getValue());
                                    break;
                                case FinancialUtils.APRIL:
                                    financeVendorEmployeeBudgetExpense.setApr(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MAY:
                                    financeVendorEmployeeBudgetExpense.setMay(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JUNE:
                                    financeVendorEmployeeBudgetExpense.setJun(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JULY:
                                    financeVendorEmployeeBudgetExpense.setJul(eachMonth.getValue());
                                    break;
                                case FinancialUtils.AUGUST:
                                    financeVendorEmployeeBudgetExpense.setAug(eachMonth.getValue());
                                    break;
                                case FinancialUtils.SEPTEMBER:
                                    financeVendorEmployeeBudgetExpense.setSep(eachMonth.getValue());
                                    break;
                                case FinancialUtils.OCTOBER:
                                    financeVendorEmployeeBudgetExpense.setOct(eachMonth.getValue());
                                    break;
                                case FinancialUtils.NOVEMBER:
                                    financeVendorEmployeeBudgetExpense.setNov(eachMonth.getValue());
                                    break;
                                case FinancialUtils.DECEMBER:
                                    financeVendorEmployeeBudgetExpense.setDec(eachMonth.getValue());
                                    break;
                                default:
                            }
                        }

                        financeVendorEmployeeBudgetExpense.setVendorDetailsBudgetId(savedVendorEmployeeDetailsBudget.getId());
                        financeVendorEmployeeBudgetExpense.setFinancialYear(financialExpense.getFinancialYear());

                        vendorEmploymentDetailsBudgetExpenses.add(financeVendorEmployeeBudgetExpense);
                    }
                    if (!ObjectUtils.isEmpty(vendorEmploymentDetailsBudgetExpenses)) {
                        vendorEmploymentDetailsBudgetExpenseRepo.saveAll(vendorEmploymentDetailsBudgetExpenses);
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    public List<GetVendorEmployeeBudgetDetailsMaster> getVendorEmployeeBudgetDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        String startMonth = generalSettings.getFinancialYearStart().substring(0,3).toLowerCase();

        List<GetVendorEmployeeBudgetDetailsMaster> getVendorEmployeeBudgetDetailsMasterList = null;
        String vendorEmployeeDetailsBudgetQuery = FinancialUtils.getFile("worksheet/getVendorEmployeeDetailsBudget.txt");

        Query query = entityManager.createNativeQuery(vendorEmployeeDetailsBudgetQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetVendorEmployeeBudgetDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetVendorEmployeeBudgetDetails> getVendorEmployeeDetailsBudget = query.getResultList();

        if (getVendorEmployeeDetailsBudget != null && !getVendorEmployeeDetailsBudget.isEmpty()) {

            FinanceMgmtFieldMap fieldFirstName = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName("First Name", FinancialUtils.VENDOR_EMPLOYEE_DETAILS);
            FinanceMgmtFieldMap fieldLastName = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName("Last Name", FinancialUtils.VENDOR_EMPLOYEE_DETAILS);

            Map<Integer, List<GetVendorEmployeeBudgetDetails>> groupByBudgetId = getVendorEmployeeDetailsBudget.stream().collect(groupingBy(GetVendorEmployeeBudgetDetails::getVendor_employment_details_budget_id));
            getVendorEmployeeBudgetDetailsMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetVendorEmployeeBudgetDetails>> entry : groupByBudgetId.entrySet()) {

                GetVendorEmployeeBudgetDetailsMaster getVendorEmployeeBudgetDetailsMaster = new GetVendorEmployeeBudgetDetailsMaster();

                List<Map<String, Object>> fieldList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    getVendorEmployeeBudgetDetailsMaster.setVendorEmploymentDetailsBudgetId(entry.getValue().get(0).getVendor_employment_details_budget_id());

                    getVendorEmployeeBudgetDetailsMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getVendorEmployeeBudgetDetailsMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getVendorEmployeeBudgetDetailsMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    for (GetVendorEmployeeBudgetDetails getVendorEmployeeBudgetDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(getVendorEmployeeBudgetDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            if(getVendorEmployeeBudgetDetails.getFields().equalsIgnoreCase("Employee ID")){
                                if (!ObjectUtils.isEmpty(getVendorEmployeeBudgetDetails.getField_value())) {
                                    ResourceMgmt resourceMgmt = resourceMgmtRepo.getReferenceById(Integer.parseInt(getVendorEmployeeBudgetDetails.getField_value()));
                                    Map<String, Object> childFields = new HashMap<>();
                                    if(fieldFirstName!=null && fieldFirstName.getEnabled()) {
                                        childFields.put("fieldName", "First Name");
                                        childFields.put("fieldValue", resourceMgmt.getFirstName());
                                        childFields.put("fieldType", "String");
                                        childFields.put("fieldId", fieldFirstName.getFieldId());
                                        fieldList.add(childFields);
                                    }
                                    if(fieldLastName!=null && fieldLastName.getEnabled()) {
                                        childFields = new HashMap<>();
                                        childFields.put("fieldName", "Last Name");
                                        childFields.put("fieldValue", resourceMgmt.getLastName());
                                        childFields.put("fieldType", "String");
                                        childFields.put("fieldId", fieldLastName.getFieldId());
                                        fieldList.add(childFields);
                                    }
                                    childFields = new HashMap<>();
                                    childFields.put("fieldName", "Employee Id");
                                    childFields.put("fieldValue", resourceMgmt.getResourceId());
                                    childFields.put("fieldType", "Number");
                                    childFields.put("fieldId", getVendorEmployeeBudgetDetails.getField_id());
                                    fieldList.add(childFields);
                                }
                            }
                            else {
                                fields.put("fieldId", getVendorEmployeeBudgetDetails.getField_id());
                                fields.put("fieldName", getVendorEmployeeBudgetDetails.getFields());
                                if(getVendorEmployeeBudgetDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                    String stringDate = FinancialUtils.formatStringDateToStringDate(getVendorEmployeeBudgetDetails.getField_value(), timeZone, dateFormat);
                                    fields.put("fieldValue", stringDate);
                                }
                                else{
                                    fields.put("fieldValue", getVendorEmployeeBudgetDetails.getField_value());
                                }
                                if (!Objects.equals(getVendorEmployeeBudgetDetails.getField_value(), getVendorEmployeeBudgetDetails.getPicklist_id())) {
                                    fields.put("picklist_id", getVendorEmployeeBudgetDetails.getPicklist_id());
                                }
                                fields.put("fieldType", getVendorEmployeeBudgetDetails.getField_type());
//                            }
                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                }

                List<VendorEmploymentDetailsBudgetExpense> vendorEmploymentDetailsBudgetExpenses = vendorEmploymentDetailsBudgetExpenseRepo.findAllByVendorDetailsBudgetId(entry.getKey());
                List<FinancialExpense> financialExpenseList = new ArrayList<>();
                if (!ObjectUtils.isEmpty(vendorEmploymentDetailsBudgetExpenses)) {
                    vendorEmploymentDetailsBudgetExpenses.sort(Comparator.comparingInt(VendorEmploymentDetailsBudgetExpense::getFinancialYear));
                    for (VendorEmploymentDetailsBudgetExpense vendorEmploymentDetailsBudgetExpense : vendorEmploymentDetailsBudgetExpenses) {
                        Map<String, Object> objToMap = objectMapper.convertValue(vendorEmploymentDetailsBudgetExpense, Map.class);
                        FinancialExpense eachFinancialExpense = new FinancialExpense();
                        eachFinancialExpense.setId((Integer) objToMap.get("id"));
                        eachFinancialExpense.setVendorId(entry.getKey());
                        eachFinancialExpense.setFinancialYear((Integer) objToMap.get("financial_year"));
                        eachFinancialExpense.setFinancialMonthlyExpenseDataList(setVendorEmploymentDetailsBudgetExpenseData(objToMap, startMonth, financialYear.equals(objToMap.get("financial_year"))));
                        financialExpenseList.add(eachFinancialExpense);
                    }
                }
                getVendorEmployeeBudgetDetailsMaster.setFinancialExpenses(financialExpenseList);
                fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                getVendorEmployeeBudgetDetailsMaster.setFields(fieldList);
                getVendorEmployeeBudgetDetailsMasterList.add(getVendorEmployeeBudgetDetailsMaster);
            }
            getVendorEmployeeBudgetDetailsMasterList.sort(Comparator.comparingInt(GetVendorEmployeeBudgetDetailsMaster::getRow_order));
        }

        return getVendorEmployeeBudgetDetailsMasterList;
    }

    private List<FinancialMonthlyExpenseData> setVendorEmploymentDetailsBudgetExpenseData(Map<String, Object> objToMap, String startMonth, boolean isSameFinancialYear) {

        List<FinancialMonthlyExpenseData> financialMonthlyExpenseData = new ArrayList<>();

        Map<String, Object> collect = objToMap.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        collect.remove("id");
        collect.remove("vendor_details_budget_id");

        Map<Integer, FinancialMonthlyExpenseData> orderedData = new HashMap<>();

        for (Map.Entry<String, Object> getVendorBudgetDetails : collect.entrySet()) {
            if (!getVendorBudgetDetails.getKey().equalsIgnoreCase("financial_year")) {
                FinancialMonthlyExpenseData monthlyExpenseData = new FinancialMonthlyExpenseData(getVendorBudgetDetails.getKey(), (Long) getVendorBudgetDetails.getValue());
                
                orderedData.put(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(monthlyExpenseData.getMonth()), monthlyExpenseData);
            }
        }

        if(isSameFinancialYear){
            for(int i = FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i<=12; i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        else{
            for(int i = 1; i<FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }

        return financialMonthlyExpenseData;
    }

    public void createUpdateVendorEmployeeDetailsForecast(List<VendorEmployeeDetailsForecastDto> vendorEmployeeDetailsForecastDtoList) {

        try {
            for (VendorEmployeeDetailsForecastDto vendorEmployeeDetailsForecastDtoObj : vendorEmployeeDetailsForecastDtoList) {
                Integer id = 0;

                VendorEmployeeDetailsForecast vendorEmployeeDetailsForecast = null;

                if (ObjectUtils.isEmpty(vendorEmployeeDetailsForecastDtoObj.getVendorEmploymentDetailsForecastId())
                        || vendorEmployeeDetailsForecastDtoObj.getVendorEmploymentDetailsForecastId() == 0) {

                    vendorEmployeeDetailsForecast = new VendorEmployeeDetailsForecast();
                    vendorEmployeeDetailsForecast.setCreatedBy(0);
                    vendorEmployeeDetailsForecast.setCreatedDate(new Date());

                } else {

                    Optional<VendorEmployeeDetailsForecast> vendorEmployeeDetailsForecastObj =
                            vendorEmployeeDetailsForecastRepo.findById(vendorEmployeeDetailsForecastDtoObj.getVendorEmploymentDetailsForecastId());

                    if (vendorEmployeeDetailsForecastObj.isPresent()) {
                        vendorEmployeeDetailsForecast = vendorEmployeeDetailsForecastObj.get();
                        vendorEmployeeDetailsForecast.setModifiedBy(0);
                        vendorEmployeeDetailsForecast.setModifiedDate(new Date());
                    } else {
                        vendorEmployeeDetailsForecast = new VendorEmployeeDetailsForecast();
                        vendorEmployeeDetailsForecast.setCreatedBy(0);
                        vendorEmployeeDetailsForecast.setCreatedDate(new Date());
                    }
                }
                vendorEmployeeDetailsForecast.setRowOrder(vendorEmployeeDetailsForecastDtoObj.getRowOrder());
                vendorEmployeeDetailsForecast.setIsActive(vendorEmployeeDetailsForecastDtoObj.getIsActive());
                vendorEmployeeDetailsForecast.setFinancialYear(vendorEmployeeDetailsForecastDtoObj.getFinancialYear());

                VendorEmployeeDetailsForecast savedVendorEmployeeDetailsForecast = vendorEmployeeDetailsForecastRepo.save(vendorEmployeeDetailsForecast);

//            handle custom field data
                if (!ObjectUtils.isEmpty(vendorEmployeeDetailsForecastDtoObj.getVendorEmployeeDetailsData())) {

                    Set<Integer> fieldIdList = vendorEmployeeDetailsForecastDtoObj.getVendorEmployeeDetailsData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<FinanceMgmtFieldValueForecastVendorEmp> financeVendorEmployeeForecastsList = new ArrayList<>();

                        for (VendorEmployeeDetailsData vendorEmployeeDetailsData : vendorEmployeeDetailsForecastDtoObj.getVendorEmployeeDetailsData()) {

                            if (availableFieldList.contains(vendorEmployeeDetailsData.getFieldId())
                                    && !vendorEmployeeDetailsData.getFieldName().equalsIgnoreCase("First Name")
                                    && !vendorEmployeeDetailsData.getFieldName().equalsIgnoreCase("Last Name")) {

                                Optional<FinanceMgmtFieldValueForecastVendorEmp> financeVendorEmployeeForecastObj =
                                        financeMgmtFieldValueForecastVendorEmpRepo.findByVendorForecastIdAndFieldId(savedVendorEmployeeDetailsForecast.getId(), vendorEmployeeDetailsData.getFieldId());

                                FinanceMgmtFieldValueForecastVendorEmp financeVendorEmployeeForecast = null;

                                if (financeVendorEmployeeForecastObj.isPresent()) {
                                    financeVendorEmployeeForecast = financeVendorEmployeeForecastObj.get();
                                    financeVendorEmployeeForecast.setModifiedBy(0);
                                    financeVendorEmployeeForecast.setModifiedDate(new Date());
                                } else {
                                    financeVendorEmployeeForecast = new FinanceMgmtFieldValueForecastVendorEmp();
                                    financeVendorEmployeeForecast.setCreatedBy(0);
                                    financeVendorEmployeeForecast.setCreatedDate(new Date());
                                }
                                financeVendorEmployeeForecast.setVendorForecastId(savedVendorEmployeeDetailsForecast.getId());
                                financeVendorEmployeeForecast.setFieldId(vendorEmployeeDetailsData.getFieldId());

                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(vendorEmployeeDetailsData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST) ) {
                                        if(!ObjectUtils.isEmpty(vendorEmployeeDetailsData.getPicklistId())) {
                                            if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(vendorEmployeeDetailsData.getPicklistId().split(",")[0]));

                                                if (locationPicklist.isPresent()) {
                                                    financeVendorEmployeeForecast.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_PROJECT)){
                                                Optional<ProjectMgmt> projectPicklist = projectMgmtRepo.findById(Integer.parseInt(vendorEmployeeDetailsData.getPicklistId().split(",")[0]));
                                                if (projectPicklist.isPresent()) {
                                                    financeVendorEmployeeForecast.setFieldValue(projectPicklist.get().getProjectId().toString());
                                                }
                                            }
                                            else{
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(vendorEmployeeDetailsData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    financeVendorEmployeeForecast.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                        }
                                        else{
                                            financeVendorEmployeeForecast.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(vendorEmployeeDetailsData.getPicklistId())) {
                                            if (findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_DESIGNATION)) {

                                                Set<Integer> pickListIds = Stream.of(vendorEmployeeDetailsData.getPicklistId().split(","))
                                                        .map(Integer::parseInt).collect(Collectors.toSet());

                                                List<ResourceMgmtRoles> designationPicklist = resourceMgmtRolesRepo.findAllById(pickListIds);

                                                if (!ObjectUtils.isEmpty(designationPicklist) && designationPicklist.size() > 0) {
                                                    financeVendorEmployeeForecast.setFieldValue(designationPicklist.stream()
                                                            .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                                }
                                            }
                                            else{
                                                Set<Integer> pickListIds = Stream.of(vendorEmployeeDetailsData.getPicklistId().split(","))
                                                        .map(Integer::parseInt).collect(Collectors.toSet());

                                                List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                                if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                    financeVendorEmployeeForecast.setFieldValue(financeCustomPicklists.stream()
                                                            .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                                }
                                            }

                                        }
                                        else{
                                            financeVendorEmployeeForecast.setFieldValue("");
                                        }
                                    } else {
                                        financeVendorEmployeeForecast.setFieldValue(vendorEmployeeDetailsData.getFieldValue());
                                    }

                                }
                                financeVendorEmployeeForecastsList.add(financeVendorEmployeeForecast);
                            }
                        }
                        if (!ObjectUtils.isEmpty(financeVendorEmployeeForecastsList)) {
                            financeMgmtFieldValueForecastVendorEmpRepo.saveAll(financeVendorEmployeeForecastsList);
                        }
                    }
                }

//            handle finance expense data
                if (!ObjectUtils.isEmpty(vendorEmployeeDetailsForecastDtoObj.getFinancialExpenses())) {


                    List<VendorEmploymentDetailsForecastExpense> vendorEmploymentDetailsForecastExpenses = new ArrayList<>();

                    for (FinancialExpense financialExpense : vendorEmployeeDetailsForecastDtoObj.getFinancialExpenses()) {

//                        if (availableFieldList.contains(financialExpense.getFieldId()) && financeMgmtFieldMapObj.get(0).getFields().contains("Months")) {

                        Optional<VendorEmploymentDetailsForecastExpense> financeVendorEmployeeForecastExpenseObj;
                        if (financialExpense.getVendorId() == null) {
                            financialExpense.setId(0);
                        }
                        financeVendorEmployeeForecastExpenseObj = vendorEmploymentDetailsForecastExpenseRepo.findById(financialExpense.getId());

                        VendorEmploymentDetailsForecastExpense financeVendorEmployeeForecastExpense = new VendorEmploymentDetailsForecastExpense();

                        List<FinancialMonthlyExpenseData> monthlyExpenseData = financialExpense.getFinancialMonthlyExpenseDataList();

                        if (financeVendorEmployeeForecastExpenseObj.isPresent()) {
                            financeVendorEmployeeForecastExpense.setId(financeVendorEmployeeForecastExpenseObj.get().getId());
                        }

                        for (FinancialMonthlyExpenseData eachMonth : monthlyExpenseData) {
                            switch (eachMonth.getMonth().toLowerCase()) {
                                case FinancialUtils.JANUARY:
                                    financeVendorEmployeeForecastExpense.setJan(eachMonth.getValue());
                                    break;
                                case FinancialUtils.FEBRUARY:
                                    financeVendorEmployeeForecastExpense.setFeb(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MARCH:
                                    financeVendorEmployeeForecastExpense.setMar(eachMonth.getValue());
                                    break;
                                case FinancialUtils.APRIL:
                                    financeVendorEmployeeForecastExpense.setApr(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MAY:
                                    financeVendorEmployeeForecastExpense.setMay(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JUNE:
                                    financeVendorEmployeeForecastExpense.setJun(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JULY:
                                    financeVendorEmployeeForecastExpense.setJul(eachMonth.getValue());
                                    break;
                                case FinancialUtils.AUGUST:
                                    financeVendorEmployeeForecastExpense.setAug(eachMonth.getValue());
                                    break;
                                case FinancialUtils.SEPTEMBER:
                                    financeVendorEmployeeForecastExpense.setSep(eachMonth.getValue());
                                    break;
                                case FinancialUtils.OCTOBER:
                                    financeVendorEmployeeForecastExpense.setOct(eachMonth.getValue());
                                    break;
                                case FinancialUtils.NOVEMBER:
                                    financeVendorEmployeeForecastExpense.setNov(eachMonth.getValue());
                                    break;
                                case FinancialUtils.DECEMBER:
                                    financeVendorEmployeeForecastExpense.setDec(eachMonth.getValue());
                                    break;
                                default:
                            }
                        }

                        financeVendorEmployeeForecastExpense.setVendorDetailsForecastId(savedVendorEmployeeDetailsForecast.getId());
                        financeVendorEmployeeForecastExpense.setFinancialYear(financialExpense.getFinancialYear());

                        vendorEmploymentDetailsForecastExpenses.add(financeVendorEmployeeForecastExpense);
                    }
                    if (!ObjectUtils.isEmpty(vendorEmploymentDetailsForecastExpenses)) {
                        vendorEmploymentDetailsForecastExpenseRepo.saveAll(vendorEmploymentDetailsForecastExpenses);
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    public List<GetVendorEmployeeForecastDetailsMaster> getVendorEmployeeForecastDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        String startMonth = generalSettings.getFinancialYearStart().substring(0,3).toLowerCase();

        List<GetVendorEmployeeForecastDetailsMaster> getVendorEmployeeForecastDetailsMasterList = null;
        String vendorEmployeeDetailsForecastQuery = FinancialUtils.getFile("worksheet/getVendorEmployeeDetailsForecast.txt");

        Query query = entityManager.createNativeQuery(vendorEmployeeDetailsForecastQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetVendorEmployeeForecastDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetVendorEmployeeForecastDetails> getVendorEmployeeDetailsForecast = query.getResultList();

        if (getVendorEmployeeDetailsForecast != null && !getVendorEmployeeDetailsForecast.isEmpty()) {

            FinanceMgmtFieldMap fieldFirstName = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName("First Name", FinancialUtils.VENDOR_EMPLOYEE_DETAILS);
            FinanceMgmtFieldMap fieldLastName = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName("Last Name", FinancialUtils.VENDOR_EMPLOYEE_DETAILS);

            Map<Integer, List<GetVendorEmployeeForecastDetails>> groupByForecastId = getVendorEmployeeDetailsForecast.stream().collect(groupingBy(GetVendorEmployeeForecastDetails::getVendor_employment_details_forecast_id));
            getVendorEmployeeForecastDetailsMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetVendorEmployeeForecastDetails>> entry : groupByForecastId.entrySet()) {

                GetVendorEmployeeForecastDetailsMaster getVendorEmployeeForecastDetailsMaster = new GetVendorEmployeeForecastDetailsMaster();

                List<Map<String, Object>> fieldList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    getVendorEmployeeForecastDetailsMaster.setVendorEmploymentDetailsForecastId(entry.getValue().get(0).getVendor_employment_details_forecast_id());

                    getVendorEmployeeForecastDetailsMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getVendorEmployeeForecastDetailsMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getVendorEmployeeForecastDetailsMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    for (GetVendorEmployeeForecastDetails getVendorEmployeeForecastDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(getVendorEmployeeForecastDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            if(getVendorEmployeeForecastDetails.getFields().equalsIgnoreCase("Employee ID")){
                                ResourceMgmt resourceMgmt = resourceMgmtRepo.getReferenceById(Integer.parseInt(getVendorEmployeeForecastDetails.getField_value()));
                                Map<String, Object> childFields = new HashMap<>();
                                if(fieldFirstName!=null && fieldFirstName.getEnabled()) {
                                    childFields.put("fieldName", "First Name");
                                    childFields.put("fieldValue", resourceMgmt.getFirstName());
                                    childFields.put("fieldType", "String");
                                    childFields.put("fieldId", fieldFirstName.getFieldId());
                                    fieldList.add(childFields);
                                }
                                if(fieldLastName!=null && fieldLastName.getEnabled()) {
                                    childFields = new HashMap<>();
                                    childFields.put("fieldName", "Last Name");
                                    childFields.put("fieldValue", resourceMgmt.getLastName());
                                    childFields.put("fieldType", "String");
                                    childFields.put("fieldId", fieldLastName.getFieldId());
                                    fieldList.add(childFields);
                                }
                                childFields = new HashMap<>();
                                childFields.put("fieldName", "Employee Id");
                                childFields.put("fieldValue", resourceMgmt.getResourceId());
                                childFields.put("fieldType", "Number");
                                childFields.put("fieldId", getVendorEmployeeForecastDetails.getField_id());
                                fieldList.add(childFields);
                            }
                            else {
                                fields.put("fieldId", getVendorEmployeeForecastDetails.getField_id());
                                fields.put("fieldName", getVendorEmployeeForecastDetails.getFields());
                                if(getVendorEmployeeForecastDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                    String stringDate = FinancialUtils.formatStringDateToStringDate(getVendorEmployeeForecastDetails.getField_value(), timeZone, dateFormat);
                                    fields.put("fieldValue", stringDate);
                                }
                                else{
                                    fields.put("fieldValue", getVendorEmployeeForecastDetails.getField_value());
                                }
                                if (!Objects.equals(getVendorEmployeeForecastDetails.getField_value(), getVendorEmployeeForecastDetails.getPicklist_id())) {
                                    fields.put("picklist_id", getVendorEmployeeForecastDetails.getPicklist_id());
                                }
                                fields.put("fieldType", getVendorEmployeeForecastDetails.getField_type());
//                            }
                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                }

                List<VendorEmploymentDetailsForecastExpense> vendorEmploymentDetailsForecastExpenses = vendorEmploymentDetailsForecastExpenseRepo.findAllByVendorDetailsForecastId(entry.getKey());
                List<FinancialExpense> financialExpenseList = new ArrayList<>();
                if (!ObjectUtils.isEmpty(vendorEmploymentDetailsForecastExpenses)) {
                    vendorEmploymentDetailsForecastExpenses.sort(Comparator.comparingInt(VendorEmploymentDetailsForecastExpense::getFinancialYear));
                    for (VendorEmploymentDetailsForecastExpense vendorEmploymentDetailsForecastExpense : vendorEmploymentDetailsForecastExpenses) {
                        Map<String, Object> objToMap = objectMapper.convertValue(vendorEmploymentDetailsForecastExpense, Map.class);
                        FinancialExpense eachFinancialExpense = new FinancialExpense();
                        eachFinancialExpense.setId((Integer) objToMap.get("id"));
                        eachFinancialExpense.setVendorId(entry.getKey());
                        eachFinancialExpense.setFinancialYear((Integer) objToMap.get("financial_year"));
                        eachFinancialExpense.setFinancialMonthlyExpenseDataList(setVendorEmploymentDetailsForecastExpenseData(objToMap, startMonth, financialYear.equals(objToMap.get("financial_year"))));
                        financialExpenseList.add(eachFinancialExpense);
                    }
                }
                getVendorEmployeeForecastDetailsMaster.setFinancialExpenses(financialExpenseList);
                fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                getVendorEmployeeForecastDetailsMaster.setFields(fieldList);
                getVendorEmployeeForecastDetailsMasterList.add(getVendorEmployeeForecastDetailsMaster);
            }
            getVendorEmployeeForecastDetailsMasterList.sort(Comparator.comparingInt(GetVendorEmployeeForecastDetailsMaster::getRow_order));
        }

        return getVendorEmployeeForecastDetailsMasterList;
    }

    private List<FinancialMonthlyExpenseData> setVendorEmploymentDetailsForecastExpenseData(Map<String, Object> objToMap, String startMonth, boolean isSameFinancialYear) {

        List<FinancialMonthlyExpenseData> financialMonthlyExpenseData = new ArrayList<>();

        Map<String, Object> collect = objToMap.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        collect.remove("id");
        collect.remove("vendor_details_forecast_id");

        Map<Integer, FinancialMonthlyExpenseData> orderedData = new HashMap<>();

        for (Map.Entry<String, Object> getVendorForecastDetails : collect.entrySet()) {
            if (!getVendorForecastDetails.getKey().equalsIgnoreCase("financial_year")) {
                FinancialMonthlyExpenseData monthlyExpenseData = new FinancialMonthlyExpenseData(getVendorForecastDetails.getKey(), (Long) getVendorForecastDetails.getValue());
                
                orderedData.put(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(monthlyExpenseData.getMonth()), monthlyExpenseData);
            }
        }

        if(isSameFinancialYear){
            for(int i = FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i<=12; i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        else{
            for(int i = 1; i<FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }

        return financialMonthlyExpenseData;
    }


    /*
    Vendor Related Cost Table
     */

    public void createUpdateVendorRelatedCostBudgetDetails(List<VendorRelatedCostBudgetDto> vendorRelatedCostBudgetDtoList) {

        try {
            for (VendorRelatedCostBudgetDto vendorRelatedCostBudgetDtoObj : vendorRelatedCostBudgetDtoList) {
                Integer id = 0;

                VendorRelatedCostBudget vendorRelatedCostBudget = null;

                if (ObjectUtils.isEmpty(vendorRelatedCostBudgetDtoObj.getVendorRelatedCostBudgetId())
                        || vendorRelatedCostBudgetDtoObj.getVendorRelatedCostBudgetId() == 0) {

                    vendorRelatedCostBudget = new VendorRelatedCostBudget();
                    vendorRelatedCostBudget.setCreatedBy(0);
                    vendorRelatedCostBudget.setCreatedDate(new Date());

                } else {

                    Optional<VendorRelatedCostBudget> vendorRelatedCostBudgetObj =
                            vendorRelatedCostBudgetRepo.findById(vendorRelatedCostBudgetDtoObj.getVendorRelatedCostBudgetId());

                    if (vendorRelatedCostBudgetObj.isPresent()) {
                        vendorRelatedCostBudget = vendorRelatedCostBudgetObj.get();
                        vendorRelatedCostBudget.setModifiedBy(0);
                        vendorRelatedCostBudget.setModifiedDate(new Date());
                    } else {
                        vendorRelatedCostBudget = new VendorRelatedCostBudget();
                        vendorRelatedCostBudget.setCreatedBy(0);
                        vendorRelatedCostBudget.setCreatedDate(new Date());
                    }
                }
                vendorRelatedCostBudget.setRowOrder(vendorRelatedCostBudgetDtoObj.getRowOrder());
                vendorRelatedCostBudget.setIsActive(vendorRelatedCostBudgetDtoObj.getIsActive());
                vendorRelatedCostBudget.setFinancialYear(vendorRelatedCostBudgetDtoObj.getFinancialYear());

                VendorRelatedCostBudget savedVendorRelatedCostBudget = vendorRelatedCostBudgetRepo.save(vendorRelatedCostBudget);

//            handle custom field data
                if (!ObjectUtils.isEmpty(vendorRelatedCostBudgetDtoObj.getVendorRelatedCostData())) {

                    Set<Integer> fieldIdList = vendorRelatedCostBudgetDtoObj.getVendorRelatedCostData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<VendorRelatedCostFieldValueBudget> vendorRelatedCostFieldValueBudgetList = new ArrayList<>();

                        for (VendorRelatedCostData vendorRelatedCostData : vendorRelatedCostBudgetDtoObj.getVendorRelatedCostData()) {

                            if (availableFieldList.contains(vendorRelatedCostData.getFieldId())) {

                                Optional<VendorRelatedCostFieldValueBudget> vendorRelatedCostFieldValueBudgetObj =
                                        vendorRelatedCostFieldValueBudgetRepo.findByVendorRelatedCostBudgetIdAndFieldId(savedVendorRelatedCostBudget.getId(), vendorRelatedCostData.getFieldId());

                                VendorRelatedCostFieldValueBudget vendorRelatedCostFieldValueBudget = null;

                                if (vendorRelatedCostFieldValueBudgetObj.isPresent()) {
                                    vendorRelatedCostFieldValueBudget = vendorRelatedCostFieldValueBudgetObj.get();
                                    vendorRelatedCostFieldValueBudget.setModifiedBy(0);
                                    vendorRelatedCostFieldValueBudget.setModifiedDate(new Date());
                                } else {
                                    vendorRelatedCostFieldValueBudget = new VendorRelatedCostFieldValueBudget();
                                    vendorRelatedCostFieldValueBudget.setCreatedBy(0);
                                    vendorRelatedCostFieldValueBudget.setCreatedDate(new Date());
                                }
                                vendorRelatedCostFieldValueBudget.setVendorRelatedCostBudgetId(savedVendorRelatedCostBudget.getId());
                                vendorRelatedCostFieldValueBudget.setFieldId(vendorRelatedCostData.getFieldId());

                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(vendorRelatedCostData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST) ) {

                                        if(!ObjectUtils.isEmpty(vendorRelatedCostData.getPicklistId())) {
                                            if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(vendorRelatedCostData.getPicklistId().split(",")[0]));

                                                if (locationPicklist.isPresent()) {
                                                    vendorRelatedCostFieldValueBudget.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_PROJECT)){
                                                Optional<ProjectMgmt> projectPicklist = projectMgmtRepo.findById(Integer.parseInt(vendorRelatedCostData.getPicklistId().split(",")[0]));
                                                if (projectPicklist.isPresent()) {
                                                    vendorRelatedCostFieldValueBudget.setFieldValue(projectPicklist.get().getProjectId().toString());
                                                }
                                            }
                                            else{
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(vendorRelatedCostData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    vendorRelatedCostFieldValueBudget.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                        }
                                        else{
                                            vendorRelatedCostFieldValueBudget.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(vendorRelatedCostData.getPicklistId())) {
                                            Set<Integer> pickListIds = Stream.of(vendorRelatedCostData.getPicklistId().split(","))
                                                    .map(Integer::parseInt).collect(Collectors.toSet());

                                            List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                            if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                vendorRelatedCostFieldValueBudget.setFieldValue(financeCustomPicklists.stream()
                                                        .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                            }
                                        }
                                        else{
                                            vendorRelatedCostFieldValueBudget.setFieldValue("");
                                        }
                                    } else {
                                        vendorRelatedCostFieldValueBudget.setFieldValue(vendorRelatedCostData.getFieldValue());
                                    }

                                }
                                vendorRelatedCostFieldValueBudgetList.add(vendorRelatedCostFieldValueBudget);
                            }
                        }
                        if (!ObjectUtils.isEmpty(vendorRelatedCostFieldValueBudgetList)) {
                            vendorRelatedCostFieldValueBudgetRepo.saveAll(vendorRelatedCostFieldValueBudgetList);
                        }
                    }
                }

//            handle finance expense data
                if (!ObjectUtils.isEmpty(vendorRelatedCostBudgetDtoObj.getVendorRelatedCostBudgetExpense())) {

                    List<VendorRelatedCostBudgetExpense> vendorRelatedCostBudgetExpenses = new ArrayList<>();

                    for (VendorRelatedBudgetExpense financialExpense : vendorRelatedCostBudgetDtoObj.getVendorRelatedCostBudgetExpense()) {

                        if (financialExpense.getId() == null) {
                            financialExpense.setId(0);
                        }

                        Optional<VendorRelatedCostBudgetExpense> vendorRelatedCostBudgetExpenseObj =
                                vendorRelatedCostBudgetExpenseRepo.findById(financialExpense.getId());

                        VendorRelatedCostBudgetExpense vendorRelatedCostBudgetExpense = new VendorRelatedCostBudgetExpense();

                        List<FinancialMonthlyExpenseData> monthlyExpenseData = financialExpense.getFinancialMonthlyExpenseDataList();

                        if (vendorRelatedCostBudgetExpenseObj.isPresent()) {
                            vendorRelatedCostBudgetExpense.setId(vendorRelatedCostBudgetExpenseObj.get().getId());
                        }

                        for (FinancialMonthlyExpenseData eachMonth : monthlyExpenseData) {
                            switch (eachMonth.getMonth().toLowerCase()) {
                                case FinancialUtils.JANUARY:
                                    vendorRelatedCostBudgetExpense.setJan(eachMonth.getValue());
                                    break;
                                case FinancialUtils.FEBRUARY:
                                    vendorRelatedCostBudgetExpense.setFeb(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MARCH:
                                    vendorRelatedCostBudgetExpense.setMar(eachMonth.getValue());
                                    break;
                                case FinancialUtils.APRIL:
                                    vendorRelatedCostBudgetExpense.setApr(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MAY:
                                    vendorRelatedCostBudgetExpense.setMay(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JUNE:
                                    vendorRelatedCostBudgetExpense.setJun(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JULY:
                                    vendorRelatedCostBudgetExpense.setJul(eachMonth.getValue());
                                    break;
                                case FinancialUtils.AUGUST:
                                    vendorRelatedCostBudgetExpense.setAug(eachMonth.getValue());
                                    break;
                                case FinancialUtils.SEPTEMBER:
                                    vendorRelatedCostBudgetExpense.setSep(eachMonth.getValue());
                                    break;
                                case FinancialUtils.OCTOBER:
                                    vendorRelatedCostBudgetExpense.setOct(eachMonth.getValue());
                                    break;
                                case FinancialUtils.NOVEMBER:
                                    vendorRelatedCostBudgetExpense.setNov(eachMonth.getValue());
                                    break;
                                case FinancialUtils.DECEMBER:
                                    vendorRelatedCostBudgetExpense.setDec(eachMonth.getValue());
                                    break;
                                default:
                            }
                        }

                        vendorRelatedCostBudgetExpense.setVendorRelatedCostBudgetId(savedVendorRelatedCostBudget.getId());
                        vendorRelatedCostBudgetExpense.setFinancialYear(financialExpense.getFinancialYear());

                        vendorRelatedCostBudgetExpenses.add(vendorRelatedCostBudgetExpense);
                    }
                    if (!ObjectUtils.isEmpty(vendorRelatedCostBudgetExpenses)) {
                        vendorRelatedCostBudgetExpenseRepo.saveAll(vendorRelatedCostBudgetExpenses);
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public List<GetVendorRelatedCostBudgetDetailMaster> getVendorRelatedCostBudgetDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        String startMonth = generalSettings.getFinancialYearStart().substring(0,3).toLowerCase();

        List<GetVendorRelatedCostBudgetDetailMaster> getVendorRelatedCostBudgetDetailMasterList = null;
        String vendorRelatedCostBudgetQuery = FinancialUtils.getFile("worksheet/getVendorRelatedCostBudgetDetails.txt");

        Query query = entityManager.createNativeQuery(vendorRelatedCostBudgetQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetVendorRelatedCostBudgetDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetVendorRelatedCostBudgetDetails> getVendorRelatedCostBudgetDetails = query.getResultList();

        if (getVendorRelatedCostBudgetDetails != null && !getVendorRelatedCostBudgetDetails.isEmpty()) {

            Map<Integer, List<GetVendorRelatedCostBudgetDetails>> groupByBudgetId = getVendorRelatedCostBudgetDetails.stream().collect(groupingBy(GetVendorRelatedCostBudgetDetails::getVendor_related_cost_budget_id));
            getVendorRelatedCostBudgetDetailMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetVendorRelatedCostBudgetDetails>> entry : groupByBudgetId.entrySet()) {

                GetVendorRelatedCostBudgetDetailMaster getVendorRelatedCostBudgetDetailMaster = new GetVendorRelatedCostBudgetDetailMaster();

                List<Map<String, Object>> fieldList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    getVendorRelatedCostBudgetDetailMaster.setVendorRelatedCostBudgetId(entry.getValue().get(0).getVendor_related_cost_budget_id());

                    getVendorRelatedCostBudgetDetailMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getVendorRelatedCostBudgetDetailMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getVendorRelatedCostBudgetDetailMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    for (GetVendorRelatedCostBudgetDetails vendorRelatedCostBudgetDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(vendorRelatedCostBudgetDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", vendorRelatedCostBudgetDetails.getFields());
                            if(vendorRelatedCostBudgetDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                String stringDate = FinancialUtils.formatStringDateToStringDate(vendorRelatedCostBudgetDetails.getField_value(), timeZone, dateFormat);
                                fields.put("fieldValue", stringDate);
                            }
                            else{
                                fields.put("fieldValue", vendorRelatedCostBudgetDetails.getField_value());
                            }
                            if(!Objects.equals(vendorRelatedCostBudgetDetails.getField_value(), vendorRelatedCostBudgetDetails.getPicklist_id())){
                                fields.put("picklist_id", vendorRelatedCostBudgetDetails.getPicklist_id());
                            }
                            fields.put("fieldType", vendorRelatedCostBudgetDetails.getField_type());
                            fields.put("fieldId", vendorRelatedCostBudgetDetails.getField_id());
//                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                }
                List<VendorRelatedCostBudgetExpense> vendorRelatedCostBudgetExpenseList = vendorRelatedCostBudgetExpenseRepo.findAllByVendorRelatedCostBudgetId(entry.getKey());
                List<VendorRelatedBudgetExpense> vendorRelatedBudgetExpenseList = new ArrayList<>();
                if (!ObjectUtils.isEmpty(vendorRelatedCostBudgetExpenseList)) {
                    vendorRelatedCostBudgetExpenseList.sort(Comparator.comparingInt(VendorRelatedCostBudgetExpense::getFinancialYear));
                    for (VendorRelatedCostBudgetExpense vendorRelatedCostBudgetExpense : vendorRelatedCostBudgetExpenseList) {
                        Map<String, Object> objToMap = objectMapper.convertValue(vendorRelatedCostBudgetExpense, Map.class);
                        VendorRelatedBudgetExpense vendorRelatedBudgetExpense = new VendorRelatedBudgetExpense();
                        vendorRelatedBudgetExpense.setId(vendorRelatedCostBudgetExpense.getId());
                        vendorRelatedBudgetExpense.setVendorRelatedCostBudgetId(entry.getKey());
                        vendorRelatedBudgetExpense.setFinancialYear((Integer) objToMap.get("financial_year"));
                        vendorRelatedBudgetExpense.setFinancialMonthlyExpenseDataList(setVendorRelatedCostBudgetExpenseData(objToMap, startMonth, financialYear.equals(objToMap.get("financial_year"))));
                        vendorRelatedBudgetExpenseList.add(vendorRelatedBudgetExpense);
                    }
                }
                fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                getVendorRelatedCostBudgetDetailMaster.setFields(fieldList);
                getVendorRelatedCostBudgetDetailMaster.setVendorRelatedBudgetExpense(vendorRelatedBudgetExpenseList);
                getVendorRelatedCostBudgetDetailMasterList.add(getVendorRelatedCostBudgetDetailMaster);
            }
            getVendorRelatedCostBudgetDetailMasterList.sort(Comparator.comparingInt(GetVendorRelatedCostBudgetDetailMaster::getRow_order));
        }

        return getVendorRelatedCostBudgetDetailMasterList;
    }

    private List<FinancialMonthlyExpenseData> setVendorRelatedCostBudgetExpenseData(Map<String, Object> objToMap, String startMonth, boolean isSameFinancialYear) {

        List<FinancialMonthlyExpenseData> financialMonthlyExpenseData = new ArrayList<>();

        Map<String, Object> collect = objToMap.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        collect.remove("id");
        collect.remove("vendor_related_cost_budget_id");

        Map<Integer, FinancialMonthlyExpenseData> orderedData = new HashMap<>();

        for (Map.Entry<String, Object> getVendorBudgetDetails : collect.entrySet()) {
            if (!getVendorBudgetDetails.getKey().equalsIgnoreCase("financial_year")) {
                FinancialMonthlyExpenseData monthlyExpenseData = new FinancialMonthlyExpenseData(getVendorBudgetDetails.getKey(), (Long) getVendorBudgetDetails.getValue());
                
                orderedData.put(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(monthlyExpenseData.getMonth()), monthlyExpenseData);
            }
        }

        if(isSameFinancialYear){
            for(int i = FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i<=12; i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        else{
            for(int i = 1; i<FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }

        return financialMonthlyExpenseData;
    }

    public void createUpdateVendorRelatedCostForecastDetails(List<VendorRelatedCostForecastDto> vendorRelatedCostForecastDtoList) {

        try {
            for (VendorRelatedCostForecastDto vendorRelatedCostForecastDtoObj : vendorRelatedCostForecastDtoList) {
                Integer id = 0;

                VendorRelatedCostForecast vendorRelatedCostForecast = null;

                if (ObjectUtils.isEmpty(vendorRelatedCostForecastDtoObj.getVendorRelatedCostForecastId())
                        || vendorRelatedCostForecastDtoObj.getVendorRelatedCostForecastId() == 0) {

                    vendorRelatedCostForecast = new VendorRelatedCostForecast();
                    vendorRelatedCostForecast.setCreatedBy(0);
                    vendorRelatedCostForecast.setCreatedDate(new Date());

                } else {

                    Optional<VendorRelatedCostForecast> vendorRelatedCostForecastObj =
                            vendorRelatedCostForecastRepo.findById(vendorRelatedCostForecastDtoObj.getVendorRelatedCostForecastId());

                    if (vendorRelatedCostForecastObj.isPresent()) {
                        vendorRelatedCostForecast = vendorRelatedCostForecastObj.get();
                        vendorRelatedCostForecast.setModifiedBy(0);
                        vendorRelatedCostForecast.setModifiedDate(new Date());
                    } else {
                        vendorRelatedCostForecast = new VendorRelatedCostForecast();
                        vendorRelatedCostForecast.setCreatedBy(0);
                        vendorRelatedCostForecast.setCreatedDate(new Date());
                    }
                }
                vendorRelatedCostForecast.setRowOrder(vendorRelatedCostForecastDtoObj.getRowOrder());
                vendorRelatedCostForecast.setIsActive(vendorRelatedCostForecastDtoObj.getIsActive());
                vendorRelatedCostForecast.setFinancialYear(vendorRelatedCostForecastDtoObj.getFinancialYear());

                VendorRelatedCostForecast savedVendorRelatedCostForecast = vendorRelatedCostForecastRepo.save(vendorRelatedCostForecast);

//            handle custom field data
                if (!ObjectUtils.isEmpty(vendorRelatedCostForecastDtoObj.getVendorRelatedCostData())) {

                    Set<Integer> fieldIdList = vendorRelatedCostForecastDtoObj.getVendorRelatedCostData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<VendorRelatedCostFieldValueForecast> vendorRelatedCostFieldValueForecastList = new ArrayList<>();

                        for (VendorRelatedCostData vendorRelatedCostData : vendorRelatedCostForecastDtoObj.getVendorRelatedCostData()) {

                            if (availableFieldList.contains(vendorRelatedCostData.getFieldId())) {

                                Optional<VendorRelatedCostFieldValueForecast> vendorRelatedCostFieldValueForecastObj =
                                        vendorRelatedCostFieldValueForecastRepo.findByVendorRelatedCostForecastIdAndFieldId(savedVendorRelatedCostForecast.getId(), vendorRelatedCostData.getFieldId());

                                VendorRelatedCostFieldValueForecast vendorRelatedCostFieldValueForecast = null;

                                if (vendorRelatedCostFieldValueForecastObj.isPresent()) {
                                    vendorRelatedCostFieldValueForecast = vendorRelatedCostFieldValueForecastObj.get();
                                    vendorRelatedCostFieldValueForecast.setModifiedBy(0);
                                    vendorRelatedCostFieldValueForecast.setModifiedDate(new Date());
                                } else {
                                    vendorRelatedCostFieldValueForecast = new VendorRelatedCostFieldValueForecast();
                                    vendorRelatedCostFieldValueForecast.setCreatedBy(0);
                                    vendorRelatedCostFieldValueForecast.setCreatedDate(new Date());
                                }
                                vendorRelatedCostFieldValueForecast.setVendorRelatedCostForecastId(savedVendorRelatedCostForecast.getId());
                                vendorRelatedCostFieldValueForecast.setFieldId(vendorRelatedCostData.getFieldId());

                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(vendorRelatedCostData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST) ) {

                                        if(!ObjectUtils.isEmpty(vendorRelatedCostData.getPicklistId())) {
                                            if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(vendorRelatedCostData.getPicklistId().split(",")[0]));

                                                if (locationPicklist.isPresent()) {
                                                    vendorRelatedCostFieldValueForecast.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_PROJECT)){
                                                Optional<ProjectMgmt> projectPicklist = projectMgmtRepo.findById(Integer.parseInt(vendorRelatedCostData.getPicklistId().split(",")[0]));
                                                if (projectPicklist.isPresent()) {
                                                    vendorRelatedCostFieldValueForecast.setFieldValue(projectPicklist.get().getProjectId().toString());
                                                }
                                            }
                                            else{
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(vendorRelatedCostData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    vendorRelatedCostFieldValueForecast.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                        }
                                        else{
                                            vendorRelatedCostFieldValueForecast.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(vendorRelatedCostData.getPicklistId())) {
                                            Set<Integer> pickListIds = Stream.of(vendorRelatedCostData.getPicklistId().split(","))
                                                    .map(Integer::parseInt).collect(Collectors.toSet());

                                            List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                            if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                vendorRelatedCostFieldValueForecast.setFieldValue(financeCustomPicklists.stream()
                                                        .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                            }
                                        }
                                        else{
                                            vendorRelatedCostFieldValueForecast.setFieldValue("");
                                        }
                                    } else {
                                        vendorRelatedCostFieldValueForecast.setFieldValue(vendorRelatedCostData.getFieldValue());
                                    }

                                }
                                vendorRelatedCostFieldValueForecastList.add(vendorRelatedCostFieldValueForecast);
                            }
                        }
                        if (!ObjectUtils.isEmpty(vendorRelatedCostFieldValueForecastList)) {
                            vendorRelatedCostFieldValueForecastRepo.saveAll(vendorRelatedCostFieldValueForecastList);
                        }
                    }
                }

//            handle finance expense data
                if (!ObjectUtils.isEmpty(vendorRelatedCostForecastDtoObj.getVendorRelatedForecastExpense())) {

                    List<VendorRelatedCostForecastExpense> vendorRelatedCostForecastExpenses = new ArrayList<>();

                    for (VendorRelatedForecastExpense financialExpense : vendorRelatedCostForecastDtoObj.getVendorRelatedForecastExpense()) {

                        if (financialExpense.getId() == null) {
                            financialExpense.setId(0);
                        }

                        Optional<VendorRelatedCostForecastExpense> vendorRelatedCostForecastExpenseObj =
                                vendorRelatedCostForecastExpenseRepo.findById(financialExpense.getId());

                        VendorRelatedCostForecastExpense vendorRelatedCostForecastExpense = new VendorRelatedCostForecastExpense();

                        List<FinancialMonthlyExpenseData> monthlyExpenseData = financialExpense.getFinancialMonthlyExpenseDataList();

                        if (vendorRelatedCostForecastExpenseObj.isPresent()) {
                            vendorRelatedCostForecastExpense.setId(vendorRelatedCostForecastExpenseObj.get().getId());
                        }

                        for (FinancialMonthlyExpenseData eachMonth : monthlyExpenseData) {
                            switch (eachMonth.getMonth().toLowerCase()) {
                                case FinancialUtils.JANUARY:
                                    vendorRelatedCostForecastExpense.setJan(eachMonth.getValue());
                                    break;
                                case FinancialUtils.FEBRUARY:
                                    vendorRelatedCostForecastExpense.setFeb(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MARCH:
                                    vendorRelatedCostForecastExpense.setMar(eachMonth.getValue());
                                    break;
                                case FinancialUtils.APRIL:
                                    vendorRelatedCostForecastExpense.setApr(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MAY:
                                    vendorRelatedCostForecastExpense.setMay(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JUNE:
                                    vendorRelatedCostForecastExpense.setJun(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JULY:
                                    vendorRelatedCostForecastExpense.setJul(eachMonth.getValue());
                                    break;
                                case FinancialUtils.AUGUST:
                                    vendorRelatedCostForecastExpense.setAug(eachMonth.getValue());
                                    break;
                                case FinancialUtils.SEPTEMBER:
                                    vendorRelatedCostForecastExpense.setSep(eachMonth.getValue());
                                    break;
                                case FinancialUtils.OCTOBER:
                                    vendorRelatedCostForecastExpense.setOct(eachMonth.getValue());
                                    break;
                                case FinancialUtils.NOVEMBER:
                                    vendorRelatedCostForecastExpense.setNov(eachMonth.getValue());
                                    break;
                                case FinancialUtils.DECEMBER:
                                    vendorRelatedCostForecastExpense.setDec(eachMonth.getValue());
                                    break;
                                default:
                            }
                        }

                        vendorRelatedCostForecastExpense.setVendorRelatedCostForecastId(savedVendorRelatedCostForecast.getId());
                        vendorRelatedCostForecastExpense.setFinancialYear(financialExpense.getFinancialYear());

                        vendorRelatedCostForecastExpenses.add(vendorRelatedCostForecastExpense);
                    }
                    if (!ObjectUtils.isEmpty(vendorRelatedCostForecastExpenses)) {
                        vendorRelatedCostForecastExpenseRepo.saveAll(vendorRelatedCostForecastExpenses);
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public List<GetVendorRelatedCostForecastDetailMaster> getVendorRelatedCostForecastDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        String startMonth = generalSettings.getFinancialYearStart().substring(0,3).toLowerCase();

        List<GetVendorRelatedCostForecastDetailMaster> getVendorRelatedCostForecastDetailMasterList = null;
        String vendorRelatedCostForecastQuery = FinancialUtils.getFile("worksheet/getVendorRelatedCostForecastDetails.txt");

        Query query = entityManager.createNativeQuery(vendorRelatedCostForecastQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetVendorRelatedCostForecastDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetVendorRelatedCostForecastDetails> getVendorRelatedCostForecastDetails = query.getResultList();

        if (getVendorRelatedCostForecastDetails != null && !getVendorRelatedCostForecastDetails.isEmpty()) {

            Map<Integer, List<GetVendorRelatedCostForecastDetails>> groupByForecastId = getVendorRelatedCostForecastDetails.stream().collect(groupingBy(GetVendorRelatedCostForecastDetails::getVendor_related_cost_forecast_id));
            getVendorRelatedCostForecastDetailMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetVendorRelatedCostForecastDetails>> entry : groupByForecastId.entrySet()) {

                GetVendorRelatedCostForecastDetailMaster getVendorRelatedCostForecastDetailMaster = new GetVendorRelatedCostForecastDetailMaster();

                List<Map<String, Object>> fieldList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    getVendorRelatedCostForecastDetailMaster.setVendorRelatedCostForecastId(entry.getValue().get(0).getVendor_related_cost_forecast_id());

                    getVendorRelatedCostForecastDetailMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getVendorRelatedCostForecastDetailMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getVendorRelatedCostForecastDetailMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    for (GetVendorRelatedCostForecastDetails vendorRelatedCostForecastDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(vendorRelatedCostForecastDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", vendorRelatedCostForecastDetails.getFields());
                            if(vendorRelatedCostForecastDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                String stringDate = FinancialUtils.formatStringDateToStringDate(vendorRelatedCostForecastDetails.getField_value(), timeZone, dateFormat);
                                fields.put("fieldValue", stringDate);
                            }
                            else{
                                fields.put("fieldValue", vendorRelatedCostForecastDetails.getField_value());
                            }
                            if(!Objects.equals(vendorRelatedCostForecastDetails.getField_value(), vendorRelatedCostForecastDetails.getPicklist_id())){
                                fields.put("picklist_id", vendorRelatedCostForecastDetails.getPicklist_id());
                            }
                            fields.put("fieldType", vendorRelatedCostForecastDetails.getField_type());
                            fields.put("fieldId", vendorRelatedCostForecastDetails.getField_id());
//                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                }
                List<VendorRelatedCostForecastExpense> vendorRelatedCostForecastExpenseList = vendorRelatedCostForecastExpenseRepo.findAllByVendorRelatedCostForecastId(entry.getKey());
                List<VendorRelatedForecastExpense> vendorRelatedForecastExpenseList = new ArrayList<>();
                if (!ObjectUtils.isEmpty(vendorRelatedCostForecastExpenseList)) {
                    vendorRelatedCostForecastExpenseList.sort(Comparator.comparingInt(VendorRelatedCostForecastExpense::getFinancialYear));
                    for (VendorRelatedCostForecastExpense vendorRelatedCostForecastExpense : vendorRelatedCostForecastExpenseList) {
                        Map<String, Object> objToMap = objectMapper.convertValue(vendorRelatedCostForecastExpense, Map.class);
                        VendorRelatedForecastExpense vendorRelatedForecastExpense = new VendorRelatedForecastExpense();
                        vendorRelatedForecastExpense.setId(vendorRelatedCostForecastExpense.getId());
                        vendorRelatedForecastExpense.setVendorRelatedCostForecastId(entry.getKey());
                        vendorRelatedForecastExpense.setFinancialYear((Integer) objToMap.get("financial_year"));
                        vendorRelatedForecastExpense.setFinancialMonthlyExpenseDataList(setVendorRelatedCostForecastExpenseData(objToMap, startMonth, financialYear.equals(objToMap.get("financial_year"))));
                        vendorRelatedForecastExpenseList.add(vendorRelatedForecastExpense);
                    }
                }
                fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                getVendorRelatedCostForecastDetailMaster.setFields(fieldList);
                getVendorRelatedCostForecastDetailMaster.setVendorRelatedForecastExpense(vendorRelatedForecastExpenseList);
                getVendorRelatedCostForecastDetailMasterList.add(getVendorRelatedCostForecastDetailMaster);
            }
            getVendorRelatedCostForecastDetailMasterList.sort(Comparator.comparingInt(GetVendorRelatedCostForecastDetailMaster::getRow_order));
        }

        return getVendorRelatedCostForecastDetailMasterList;
    }

    private List<FinancialMonthlyExpenseData> setVendorRelatedCostForecastExpenseData(Map<String, Object> objToMap, String startMonth, boolean isSameFinancialYear) {

        List<FinancialMonthlyExpenseData> financialMonthlyExpenseData = new ArrayList<>();

        Map<String, Object> collect = objToMap.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        collect.remove("id");
        collect.remove("vendor_related_cost_forecast_id");

        Map<Integer, FinancialMonthlyExpenseData> orderedData = new HashMap<>();

        for (Map.Entry<String, Object> getVendorBudgetDetails : collect.entrySet()) {
            if (!getVendorBudgetDetails.getKey().equalsIgnoreCase("financial_year")) {
                FinancialMonthlyExpenseData monthlyExpenseData = new FinancialMonthlyExpenseData(getVendorBudgetDetails.getKey(), (Long) getVendorBudgetDetails.getValue());
                
                orderedData.put(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(monthlyExpenseData.getMonth()), monthlyExpenseData);
            }
        }

        if(isSameFinancialYear){
            for(int i = FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i<=12; i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        else{
            for(int i = 1; i<FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }

        return financialMonthlyExpenseData;
    }


    /*
    Capex Related Expense Table
     */

    public void createUpdateCapexRelatedExpenseBudgetDetails(List<CapexRelatedExpenseBudgetDto> capexRelatedExpenseBudgetDtoList) {

        try {
            for (CapexRelatedExpenseBudgetDto capexRelatedExpenseBudgetDtoObj : capexRelatedExpenseBudgetDtoList) {
                Integer id = 0;

                CapexRelatedExpenseBudget capexRelatedExpenseBudget = null;

                if (ObjectUtils.isEmpty(capexRelatedExpenseBudgetDtoObj.getCapexRelatedExpenseBudgetId())
                        || capexRelatedExpenseBudgetDtoObj.getCapexRelatedExpenseBudgetId() == 0) {

                    capexRelatedExpenseBudget = new CapexRelatedExpenseBudget();
                    capexRelatedExpenseBudget.setCreatedBy(0);
                    capexRelatedExpenseBudget.setCreatedDate(new Date());

                } else {

                    Optional<CapexRelatedExpenseBudget> capexRelatedExpenseBudgetObj =
                            capexRelatedExpenseBudgetRepo.findById(capexRelatedExpenseBudgetDtoObj.getCapexRelatedExpenseBudgetId());

                    if (capexRelatedExpenseBudgetObj.isPresent()) {
                        capexRelatedExpenseBudget = capexRelatedExpenseBudgetObj.get();
                        capexRelatedExpenseBudget.setModifiedBy(0);
                        capexRelatedExpenseBudget.setModifiedDate(new Date());
                    } else {
                        capexRelatedExpenseBudget = new CapexRelatedExpenseBudget();
                        capexRelatedExpenseBudget.setCreatedBy(0);
                        capexRelatedExpenseBudget.setCreatedDate(new Date());
                    }
                }
                capexRelatedExpenseBudget.setRowOrder(capexRelatedExpenseBudgetDtoObj.getRowOrder());
                capexRelatedExpenseBudget.setIsActive(capexRelatedExpenseBudgetDtoObj.getIsActive());
                capexRelatedExpenseBudget.setFinancialYear(capexRelatedExpenseBudgetDtoObj.getFinancialYear());

                CapexRelatedExpenseBudget savedCapexRelatedExpenseBudget = capexRelatedExpenseBudgetRepo.save(capexRelatedExpenseBudget);

//            handle custom field data
                if (!ObjectUtils.isEmpty(capexRelatedExpenseBudgetDtoObj.getCapexRelatedExpenseData())) {

                    Set<Integer> fieldIdList = capexRelatedExpenseBudgetDtoObj.getCapexRelatedExpenseData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<FinanceMgmtFieldValueBudgetCapex> capexRelatedExpenseBudgetsList = new ArrayList<>();

                        for (CapexRelatedExpenseData capexRelatedExpenseBudgetData : capexRelatedExpenseBudgetDtoObj.getCapexRelatedExpenseData()) {

                            if (availableFieldList.contains(capexRelatedExpenseBudgetData.getFieldId())) {

                                Optional<FinanceMgmtFieldValueBudgetCapex> capexRelatedExpenseBudgetObj =
                                        financeMgmtFieldValueBudgetCapexRepo.findByCapexIdAndFieldId(savedCapexRelatedExpenseBudget.getId(), capexRelatedExpenseBudgetData.getFieldId());

                                FinanceMgmtFieldValueBudgetCapex financeMgmtFieldValueBudgetCapex = null;

                                if (capexRelatedExpenseBudgetObj.isPresent()) {
                                    financeMgmtFieldValueBudgetCapex = capexRelatedExpenseBudgetObj.get();
                                    financeMgmtFieldValueBudgetCapex.setModifiedBy(0);
                                    financeMgmtFieldValueBudgetCapex.setModifiedDate(new Date());
                                } else {
                                    financeMgmtFieldValueBudgetCapex = new FinanceMgmtFieldValueBudgetCapex();
                                    financeMgmtFieldValueBudgetCapex.setCreatedBy(0);
                                    financeMgmtFieldValueBudgetCapex.setCreatedDate(new Date());
                                }
                                financeMgmtFieldValueBudgetCapex.setCapexId(savedCapexRelatedExpenseBudget.getId());
                                financeMgmtFieldValueBudgetCapex.setFieldId(capexRelatedExpenseBudgetData.getFieldId());

                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(capexRelatedExpenseBudgetData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST)) {

                                        if(!ObjectUtils.isEmpty(capexRelatedExpenseBudgetData.getPicklistId())) {
                                            if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(capexRelatedExpenseBudgetData.getPicklistId().split(",")[0]));

                                                if (locationPicklist.isPresent()) {
                                                    financeMgmtFieldValueBudgetCapex.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_PROJECT)){
                                                Optional<ProjectMgmt> projectPicklist = projectMgmtRepo.findById(Integer.parseInt(capexRelatedExpenseBudgetData.getPicklistId().split(",")[0]));
                                                if (projectPicklist.isPresent()) {
                                                    financeMgmtFieldValueBudgetCapex.setFieldValue(projectPicklist.get().getProjectId().toString());
                                                }
                                            }
                                            else{
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(capexRelatedExpenseBudgetData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    financeMgmtFieldValueBudgetCapex.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }
                                            
                                        }
                                        else{
                                            financeMgmtFieldValueBudgetCapex.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(capexRelatedExpenseBudgetData.getPicklistId())) {
                                            Set<Integer> pickListIds = Stream.of(capexRelatedExpenseBudgetData.getPicklistId().split(","))
                                                    .map(Integer::parseInt).collect(Collectors.toSet());

                                            List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                            if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                financeMgmtFieldValueBudgetCapex.setFieldValue(financeCustomPicklists.stream()
                                                        .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                            }
                                        }
                                        else{
                                            financeMgmtFieldValueBudgetCapex.setFieldValue("");
                                        }
                                    } else {
                                        financeMgmtFieldValueBudgetCapex.setFieldValue(capexRelatedExpenseBudgetData.getValue());
                                    }

                                }
                                capexRelatedExpenseBudgetsList.add(financeMgmtFieldValueBudgetCapex);
                            }
                        }
                        if (!ObjectUtils.isEmpty(capexRelatedExpenseBudgetsList)) {
                            financeMgmtFieldValueBudgetCapexRepo.saveAll(capexRelatedExpenseBudgetsList);
                        }
                    }
                }

//            handle finance expense data
                if (!ObjectUtils.isEmpty(capexRelatedExpenseBudgetDtoObj.getCapexFinancialExpenseList())) {

                    List<CapexRelatedExpenseBudgetExpense> capexRelatedExpenseBudgetExpenses = new ArrayList<>();

                    for (CapexFinancialExpense financialExpense : capexRelatedExpenseBudgetDtoObj.getCapexFinancialExpenseList()) {

                        if(financialExpense.getId()==null){
                            financialExpense.setId(0);
                        }
                        Optional<CapexRelatedExpenseBudgetExpense> capexRelatedExpenseExpenseObj =
                                capexRelatedExpenseBudgetExpenseRepo.findById(financialExpense.getId());

                        CapexRelatedExpenseBudgetExpense capexRelatedExpenseBudgetExpense = new CapexRelatedExpenseBudgetExpense();

                        List<FinancialMonthlyExpenseData> monthlyExpenseData = financialExpense.getFinancialMonthlyExpenseDataList();

                        if (capexRelatedExpenseExpenseObj.isPresent()) {
                            capexRelatedExpenseBudgetExpense.setId(capexRelatedExpenseExpenseObj.get().getId());
                        }
                        for (FinancialMonthlyExpenseData eachMonth : monthlyExpenseData) {
                            switch (eachMonth.getMonth().toLowerCase()) {
                                case FinancialUtils.JANUARY:
                                    capexRelatedExpenseBudgetExpense.setJan(eachMonth.getValue());
                                    break;
                                case FinancialUtils.FEBRUARY:
                                    capexRelatedExpenseBudgetExpense.setFeb(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MARCH:
                                    capexRelatedExpenseBudgetExpense.setMar(eachMonth.getValue());
                                    break;
                                case FinancialUtils.APRIL:
                                    capexRelatedExpenseBudgetExpense.setApr(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MAY:
                                    capexRelatedExpenseBudgetExpense.setMay(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JUNE:
                                    capexRelatedExpenseBudgetExpense.setJun(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JULY:
                                    capexRelatedExpenseBudgetExpense.setJul(eachMonth.getValue());
                                    break;
                                case FinancialUtils.AUGUST:
                                    capexRelatedExpenseBudgetExpense.setAug(eachMonth.getValue());
                                    break;
                                case FinancialUtils.SEPTEMBER:
                                    capexRelatedExpenseBudgetExpense.setSep(eachMonth.getValue());
                                    break;
                                case FinancialUtils.OCTOBER:
                                    capexRelatedExpenseBudgetExpense.setOct(eachMonth.getValue());
                                    break;
                                case FinancialUtils.NOVEMBER:
                                    capexRelatedExpenseBudgetExpense.setNov(eachMonth.getValue());
                                    break;
                                case FinancialUtils.DECEMBER:
                                    capexRelatedExpenseBudgetExpense.setDec(eachMonth.getValue());
                                    break;
                                default:
                            }
                        }

                        capexRelatedExpenseBudgetExpense.setCapexBudgetId(savedCapexRelatedExpenseBudget.getId());
                        capexRelatedExpenseBudgetExpense.setFinancialYear(financialExpense.getFinancialYear());

                        capexRelatedExpenseBudgetExpenses.add(capexRelatedExpenseBudgetExpense);
                    }
                    if (!ObjectUtils.isEmpty(capexRelatedExpenseBudgetExpenses)) {
                        capexRelatedExpenseBudgetExpenseRepo.saveAll(capexRelatedExpenseBudgetExpenses);
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public List<GetCapexRelatedExpenseBudgetDetailMaster> getCapexRelatedExpenseBudgetDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        String startMonth = generalSettings.getFinancialYearStart().substring(0,3).toLowerCase();

        List<GetCapexRelatedExpenseBudgetDetailMaster> getCapexRelatedExpenseBudgetDetailMasterList = null;
        String capexRelatedExpenseBudgetQuery = FinancialUtils.getFile("worksheet/getCapexRelatedExpenseBudgetDetails.txt");

        Query query = entityManager.createNativeQuery(capexRelatedExpenseBudgetQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetCapexRelatedExpenseBudgetDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetCapexRelatedExpenseBudgetDetails> getCapexRelatedExpenseBudgetDetails = query.getResultList();

        if (getCapexRelatedExpenseBudgetDetails != null && !getCapexRelatedExpenseBudgetDetails.isEmpty()) {

            Map<Integer, List<GetCapexRelatedExpenseBudgetDetails>> groupByBudgetId = getCapexRelatedExpenseBudgetDetails.stream().collect(groupingBy(GetCapexRelatedExpenseBudgetDetails::getCapex_related_expense_budget_id));
            getCapexRelatedExpenseBudgetDetailMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetCapexRelatedExpenseBudgetDetails>> entry : groupByBudgetId.entrySet()) {

                GetCapexRelatedExpenseBudgetDetailMaster getCapexRelatedExpenseBudgetDetailMaster = new GetCapexRelatedExpenseBudgetDetailMaster();

                List<Map<String, Object>> fieldList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    getCapexRelatedExpenseBudgetDetailMaster.setCapexRelatedExpenseBudgetId(entry.getValue().get(0).getCapex_related_expense_budget_id());

                    getCapexRelatedExpenseBudgetDetailMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getCapexRelatedExpenseBudgetDetailMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getCapexRelatedExpenseBudgetDetailMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    for (GetCapexRelatedExpenseBudgetDetails capexRelatedExpenseBudgetDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(capexRelatedExpenseBudgetDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", capexRelatedExpenseBudgetDetails.getFields());
                            if(capexRelatedExpenseBudgetDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                String stringDate = FinancialUtils.formatStringDateToStringDate(capexRelatedExpenseBudgetDetails.getField_value(), timeZone, dateFormat);
                                fields.put("fieldValue", stringDate);
                            }
                            else{
                                fields.put("fieldValue", capexRelatedExpenseBudgetDetails.getField_value());
                            }
                            if(!Objects.equals(capexRelatedExpenseBudgetDetails.getField_value(), capexRelatedExpenseBudgetDetails.getPicklist_id())){
                                fields.put("picklist_id", capexRelatedExpenseBudgetDetails.getPicklist_id());
                            }
                            fields.put("fieldType", capexRelatedExpenseBudgetDetails.getField_type());
                            fields.put("fieldId", capexRelatedExpenseBudgetDetails.getField_id());
//                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                }
                List<CapexRelatedExpenseBudgetExpense> capexRelatedExpenseBudgetExpenseList = capexRelatedExpenseBudgetExpenseRepo.findAllByCapexBudgetId(entry.getKey());
                List<CapexFinancialExpense> capexFinancialExpenseList = new ArrayList<>();
                if (!ObjectUtils.isEmpty(capexRelatedExpenseBudgetExpenseList)) {
                    capexRelatedExpenseBudgetExpenseList.sort(Comparator.comparingInt(CapexRelatedExpenseBudgetExpense::getFinancialYear));
                    for (CapexRelatedExpenseBudgetExpense capexRelatedFinancialExpense : capexRelatedExpenseBudgetExpenseList) {
                        Map<String, Object> objToMap = objectMapper.convertValue(capexRelatedFinancialExpense, Map.class);
                        CapexFinancialExpense capexRelatedExpenseBudgetExpense = new CapexFinancialExpense();
                        capexRelatedExpenseBudgetExpense.setId(capexRelatedFinancialExpense.getId());
                        capexRelatedExpenseBudgetExpense.setCapexId(entry.getKey());
                        capexRelatedExpenseBudgetExpense.setFinancialYear((Integer) objToMap.get("financial_year"));
                        capexRelatedExpenseBudgetExpense.setFinancialMonthlyExpenseDataList(setCapexRelatedExpenseBudgetExpenseData(objToMap, startMonth, financialYear.equals(objToMap.get("financial_year"))));
                        capexFinancialExpenseList.add(capexRelatedExpenseBudgetExpense);
                    }
                }
                fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                getCapexRelatedExpenseBudgetDetailMaster.setFields(fieldList);
                getCapexRelatedExpenseBudgetDetailMaster.setCapexFinancialExpenseList(capexFinancialExpenseList);
                getCapexRelatedExpenseBudgetDetailMasterList.add(getCapexRelatedExpenseBudgetDetailMaster);
            }
            getCapexRelatedExpenseBudgetDetailMasterList.sort(Comparator.comparingInt(GetCapexRelatedExpenseBudgetDetailMaster::getRow_order));
        }

        return getCapexRelatedExpenseBudgetDetailMasterList;
    }

    private List<FinancialMonthlyExpenseData> setCapexRelatedExpenseBudgetExpenseData(Map<String, Object> objToMap, String startMonth, boolean isSameFinancialYear) {

        List<FinancialMonthlyExpenseData> financialMonthlyExpenseData = new ArrayList<>();

        Map<String, Object> collect = objToMap.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        collect.remove("id");
        collect.remove("capex_budget_id");

        Map<Integer, FinancialMonthlyExpenseData> orderedData = new HashMap<>();

        for (Map.Entry<String, Object> getCapexExpenseDetails : collect.entrySet()) {
            if (!getCapexExpenseDetails.getKey().equalsIgnoreCase("financial_year")) {
                FinancialMonthlyExpenseData monthlyExpenseData = new FinancialMonthlyExpenseData(getCapexExpenseDetails.getKey(), (Long) getCapexExpenseDetails.getValue());
                
                orderedData.put(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(monthlyExpenseData.getMonth()), monthlyExpenseData);
            }
        }
        if(isSameFinancialYear){
            for(int i = FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i<=12; i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        else{
            for(int i = 1; i<FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        return financialMonthlyExpenseData;
    }

    public void createUpdateCapexRelatedExpenseForecastDetails(List<CapexRelatedExpenseForecastDto> capexRelatedExpenseForecastDtoList) {

        try {
            for (CapexRelatedExpenseForecastDto capexRelatedExpenseForecastDtoObj : capexRelatedExpenseForecastDtoList) {
                Integer id = 0;

                CapexRelatedExpenseForecast capexRelatedExpenseForecast = null;

                if (ObjectUtils.isEmpty(capexRelatedExpenseForecastDtoObj.getCapexRelatedExpenseForecastId())
                        || capexRelatedExpenseForecastDtoObj.getCapexRelatedExpenseForecastId() == 0) {

                    capexRelatedExpenseForecast = new CapexRelatedExpenseForecast();
                    capexRelatedExpenseForecast.setCreatedBy(0);
                    capexRelatedExpenseForecast.setCreatedDate(new Date());

                } else {

                    Optional<CapexRelatedExpenseForecast> capexRelatedExpenseForecastObj =
                            capexRelatedExpenseForecastRepo.findById(capexRelatedExpenseForecastDtoObj.getCapexRelatedExpenseForecastId());

                    if (capexRelatedExpenseForecastObj.isPresent()) {
                        capexRelatedExpenseForecast = capexRelatedExpenseForecastObj.get();
                        capexRelatedExpenseForecast.setModifiedBy(0);
                        capexRelatedExpenseForecast.setModifiedDate(new Date());
                    } else {
                        capexRelatedExpenseForecast = new CapexRelatedExpenseForecast();
                        capexRelatedExpenseForecast.setCreatedBy(0);
                        capexRelatedExpenseForecast.setCreatedDate(new Date());
                    }
                }
                capexRelatedExpenseForecast.setRowOrder(capexRelatedExpenseForecastDtoObj.getRowOrder());
                capexRelatedExpenseForecast.setIsActive(capexRelatedExpenseForecastDtoObj.getIsActive());
                capexRelatedExpenseForecast.setFinancialYear(capexRelatedExpenseForecastDtoObj.getFinancialYear());

                CapexRelatedExpenseForecast savedCapexRelatedExpenseForecast = capexRelatedExpenseForecastRepo.save(capexRelatedExpenseForecast);

//            handle custom field data
                if (!ObjectUtils.isEmpty(capexRelatedExpenseForecastDtoObj.getCapexRelatedExpenseData())) {

                    Set<Integer> fieldIdList = capexRelatedExpenseForecastDtoObj.getCapexRelatedExpenseData().stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                    if (!ObjectUtils.isEmpty(fieldIdList)) {

                        List<FinanceMgmtFieldMap> financeMgmtFieldMapObj = financeMgmtFieldMapRepo.findAllById(fieldIdList);

                        Set<Integer> availableFieldList = financeMgmtFieldMapObj.stream().map(it -> it.getFieldId()).collect(Collectors.toSet());

                        List<FinanceMgmtFieldValueForecastCapex> capexRelatedExpenseForecastsList = new ArrayList<>();

                        for (CapexRelatedExpenseData capexRelatedExpenseForecastData : capexRelatedExpenseForecastDtoObj.getCapexRelatedExpenseData()) {

                            if (availableFieldList.contains(capexRelatedExpenseForecastData.getFieldId())) {

                                Optional<FinanceMgmtFieldValueForecastCapex> capexRelatedExpenseForecastObj =
                                        financeMgmtFieldValueForecastCapexRepo.findByCapexIdAndFieldId(savedCapexRelatedExpenseForecast.getId(), capexRelatedExpenseForecastData.getFieldId());

                                FinanceMgmtFieldValueForecastCapex financeMgmtFieldValueForecastCapex = null;

                                if (capexRelatedExpenseForecastObj.isPresent()) {
                                    financeMgmtFieldValueForecastCapex = capexRelatedExpenseForecastObj.get();
                                    financeMgmtFieldValueForecastCapex.setModifiedBy(0);
                                    financeMgmtFieldValueForecastCapex.setModifiedDate(new Date());
                                } else {
                                    financeMgmtFieldValueForecastCapex = new FinanceMgmtFieldValueForecastCapex();
                                    financeMgmtFieldValueForecastCapex.setCreatedBy(0);
                                    financeMgmtFieldValueForecastCapex.setCreatedDate(new Date());
                                }
                                financeMgmtFieldValueForecastCapex.setCapexId(savedCapexRelatedExpenseForecast.getId());
                                financeMgmtFieldValueForecastCapex.setFieldId(capexRelatedExpenseForecastData.getFieldId());

                                Optional<FinanceMgmtFieldMap> findPickList = financeMgmtFieldMapObj.stream().filter(it -> it.getFieldId().equals(capexRelatedExpenseForecastData.getFieldId())).findFirst();

                                if (findPickList.isPresent()) {

                                    if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST)) {

                                        if(!ObjectUtils.isEmpty(capexRelatedExpenseForecastData.getPicklistId())) {
                                            if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_LOCATION)){
                                                Optional<GeneralSettingsLocation> locationPicklist = generalSettingsLocationRepo.findById(Integer.parseInt(capexRelatedExpenseForecastData.getPicklistId().split(",")[0]));

                                                if (locationPicklist.isPresent()) {
                                                    financeMgmtFieldValueForecastCapex.setFieldValue(locationPicklist.get().getLocationId().toString());
                                                }
                                            }
                                            else if(findPickList.get().getFields().equalsIgnoreCase(FinancialUtils.FINANCE_PROJECT)){
                                                Optional<ProjectMgmt> projectPicklist = projectMgmtRepo.findById(Integer.parseInt(capexRelatedExpenseForecastData.getPicklistId().split(",")[0]));
                                                if (projectPicklist.isPresent()) {
                                                    financeMgmtFieldValueForecastCapex.setFieldValue(projectPicklist.get().getProjectId().toString());
                                                }
                                            }
                                            else{
                                                Optional<FinanceCustomPicklist> financeCustomPick = financeCustomPicklistRepo.findById(Integer.parseInt(capexRelatedExpenseForecastData.getPicklistId().split(",")[0]));

                                                if (financeCustomPick.isPresent()) {
                                                    financeMgmtFieldValueForecastCapex.setFieldValue(financeCustomPick.get().getId().toString());
                                                }
                                            }

                                        }
                                        else{
                                            financeMgmtFieldValueForecastCapex.setFieldValue("");
                                        }
                                    } else if (findPickList.get().getFieldType().equals(FinancialUtils.PICKLIST_MULTI)) {

                                        if(!ObjectUtils.isEmpty(capexRelatedExpenseForecastData.getPicklistId())) {
                                            Set<Integer> pickListIds = Stream.of(capexRelatedExpenseForecastData.getPicklistId().split(","))
                                                    .map(Integer::parseInt).collect(Collectors.toSet());

                                            List<FinanceCustomPicklist> financeCustomPicklists = financeCustomPicklistRepo.findAllById(pickListIds);

                                            if (!ObjectUtils.isEmpty(financeCustomPicklists) && financeCustomPicklists.size() > 0) {
                                                financeMgmtFieldValueForecastCapex.setFieldValue(financeCustomPicklists.stream()
                                                        .map(it -> String.valueOf(it.getId())).collect(Collectors.joining(",")));
                                            }
                                        }
                                        else{
                                            financeMgmtFieldValueForecastCapex.setFieldValue("");
                                        }
                                    } else {
                                        financeMgmtFieldValueForecastCapex.setFieldValue(capexRelatedExpenseForecastData.getValue());
                                    }

                                }
                                capexRelatedExpenseForecastsList.add(financeMgmtFieldValueForecastCapex);
                            }
                        }
                        if (!ObjectUtils.isEmpty(capexRelatedExpenseForecastsList)) {
                            financeMgmtFieldValueForecastCapexRepo.saveAll(capexRelatedExpenseForecastsList);
                        }
                    }
                }

//            handle finance expense data
                if (!ObjectUtils.isEmpty(capexRelatedExpenseForecastDtoObj.getCapexFinancialExpenseList())) {

                    List<CapexRelatedExpenseForecastExpense> capexRelatedExpenseForecastExpenses = new ArrayList<>();

                    for (CapexFinancialExpense financialExpense : capexRelatedExpenseForecastDtoObj.getCapexFinancialExpenseList()) {

                        if(financialExpense.getId()==null){
                            financialExpense.setId(0);
                        }
                        Optional<CapexRelatedExpenseForecastExpense> capexRelatedExpenseExpenseObj =
                                capexRelatedExpenseForecastExpenseRepo.findById(financialExpense.getId());

                        CapexRelatedExpenseForecastExpense capexRelatedExpenseForecastExpense = new CapexRelatedExpenseForecastExpense();

                        List<FinancialMonthlyExpenseData> monthlyExpenseData = financialExpense.getFinancialMonthlyExpenseDataList();

                        if (capexRelatedExpenseExpenseObj.isPresent()) {
                            capexRelatedExpenseForecastExpense.setId(capexRelatedExpenseExpenseObj.get().getId());
                        }

                        for (FinancialMonthlyExpenseData eachMonth : monthlyExpenseData) {
                            switch (eachMonth.getMonth().toLowerCase()) {
                                case FinancialUtils.JANUARY:
                                    capexRelatedExpenseForecastExpense.setJan(eachMonth.getValue());
                                    break;
                                case FinancialUtils.FEBRUARY:
                                    capexRelatedExpenseForecastExpense.setFeb(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MARCH:
                                    capexRelatedExpenseForecastExpense.setMar(eachMonth.getValue());
                                    break;
                                case FinancialUtils.APRIL:
                                    capexRelatedExpenseForecastExpense.setApr(eachMonth.getValue());
                                    break;
                                case FinancialUtils.MAY:
                                    capexRelatedExpenseForecastExpense.setMay(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JUNE:
                                    capexRelatedExpenseForecastExpense.setJun(eachMonth.getValue());
                                    break;
                                case FinancialUtils.JULY:
                                    capexRelatedExpenseForecastExpense.setJul(eachMonth.getValue());
                                    break;
                                case FinancialUtils.AUGUST:
                                    capexRelatedExpenseForecastExpense.setAug(eachMonth.getValue());
                                    break;
                                case FinancialUtils.SEPTEMBER:
                                    capexRelatedExpenseForecastExpense.setSep(eachMonth.getValue());
                                    break;
                                case FinancialUtils.OCTOBER:
                                    capexRelatedExpenseForecastExpense.setOct(eachMonth.getValue());
                                    break;
                                case FinancialUtils.NOVEMBER:
                                    capexRelatedExpenseForecastExpense.setNov(eachMonth.getValue());
                                    break;
                                case FinancialUtils.DECEMBER:
                                    capexRelatedExpenseForecastExpense.setDec(eachMonth.getValue());
                                    break;
                                default:
                            }
                        }

                        capexRelatedExpenseForecastExpense.setCapexForecastId(savedCapexRelatedExpenseForecast.getId());
                        capexRelatedExpenseForecastExpense.setFinancialYear(financialExpense.getFinancialYear());

                        capexRelatedExpenseForecastExpenses.add(capexRelatedExpenseForecastExpense);
                    }
                    if (!ObjectUtils.isEmpty(capexRelatedExpenseForecastExpenses)) {
                        capexRelatedExpenseForecastExpenseRepo.saveAll(capexRelatedExpenseForecastExpenses);
                    }
                }
            }
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    public List<GetCapexRelatedExpenseForecastDetailMaster> getCapexRelatedExpenseForecastDetails(Integer financialYear) {

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        String startMonth = generalSettings.getFinancialYearStart().substring(0,3).toLowerCase();

        List<GetCapexRelatedExpenseForecastDetailMaster> getCapexRelatedExpenseForecastDetailMasterList = null;
        String capexRelatedExpenseForecastQuery = FinancialUtils.getFile("worksheet/getCapexRelatedExpenseForecastDetails.txt");

        Query query = entityManager.createNativeQuery(capexRelatedExpenseForecastQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetCapexRelatedExpenseForecastDetails.class));

        query.setParameter("financialYear", financialYear);

        List<GetCapexRelatedExpenseForecastDetails> getCapexRelatedExpenseForecastDetails = query.getResultList();

        if (getCapexRelatedExpenseForecastDetails != null && !getCapexRelatedExpenseForecastDetails.isEmpty()) {

            Map<Integer, List<GetCapexRelatedExpenseForecastDetails>> groupByForecastId = getCapexRelatedExpenseForecastDetails.stream().collect(groupingBy(GetCapexRelatedExpenseForecastDetails::getCapex_related_expense_forecast_id));
            getCapexRelatedExpenseForecastDetailMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetCapexRelatedExpenseForecastDetails>> entry : groupByForecastId.entrySet()) {

                GetCapexRelatedExpenseForecastDetailMaster getCapexRelatedExpenseForecastDetailMaster = new GetCapexRelatedExpenseForecastDetailMaster();

                List<Map<String, Object>> fieldList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    getCapexRelatedExpenseForecastDetailMaster.setCapexRelatedExpenseForecastId(entry.getValue().get(0).getCapex_related_expense_forecast_id());

                    getCapexRelatedExpenseForecastDetailMaster.setRow_order(entry.getValue().get(0).getRow_order());
                    getCapexRelatedExpenseForecastDetailMaster.setFinancial_year(entry.getValue().get(0).getFinancial_year());
                    getCapexRelatedExpenseForecastDetailMaster.setIs_active(entry.getValue().get(0).getIs_active());

                    for (GetCapexRelatedExpenseForecastDetails capexRelatedExpenseForecastDetails : entry.getValue()) {
                        if (StringUtils.isNotBlank(capexRelatedExpenseForecastDetails.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", capexRelatedExpenseForecastDetails.getFields());
                            if(capexRelatedExpenseForecastDetails.getField_type().equals(FinancialUtils.FIELD_TYPE_DATE)){
                                String stringDate = FinancialUtils.formatStringDateToStringDate(capexRelatedExpenseForecastDetails.getField_value(), timeZone, dateFormat);
                                fields.put("fieldValue", stringDate);
                            }
                            else{
                                fields.put("fieldValue", capexRelatedExpenseForecastDetails.getField_value());
                            }
                            if(!Objects.equals(capexRelatedExpenseForecastDetails.getField_value(), capexRelatedExpenseForecastDetails.getPicklist_id())){
                                fields.put("picklist_id", capexRelatedExpenseForecastDetails.getPicklist_id());
                            }
                            fields.put("fieldType", capexRelatedExpenseForecastDetails.getField_type());
                            fields.put("fieldId", capexRelatedExpenseForecastDetails.getField_id());
//                            }
                            if (!fields.isEmpty()) {
                                fieldList.add(fields);
                            }
                        }
                    }
                }
                List<CapexRelatedExpenseForecastExpense> capexRelatedExpenseForecastExpenseList = capexRelatedExpenseForecastExpenseRepo.findAllByCapexForecastId(entry.getKey());
                List<CapexFinancialExpense> capexFinancialExpenseList = new ArrayList<>();
                if (!ObjectUtils.isEmpty(capexRelatedExpenseForecastExpenseList)) {
                    capexRelatedExpenseForecastExpenseList.sort(Comparator.comparingInt(CapexRelatedExpenseForecastExpense::getFinancialYear));
                    for (CapexRelatedExpenseForecastExpense capexRelatedFinancialExpense : capexRelatedExpenseForecastExpenseList) {
                        Map<String, Object> objToMap = objectMapper.convertValue(capexRelatedFinancialExpense, Map.class);
                        CapexFinancialExpense capexRelatedExpenseForecastExpense = new CapexFinancialExpense();
                        capexRelatedExpenseForecastExpense.setId(capexRelatedFinancialExpense.getId());
                        capexRelatedExpenseForecastExpense.setCapexId(entry.getKey());
                        capexRelatedExpenseForecastExpense.setFinancialYear((Integer) objToMap.get("financial_year"));
                        capexRelatedExpenseForecastExpense.setFinancialMonthlyExpenseDataList(setCapexRelatedExpenseForecastExpenseData(objToMap, startMonth, financialYear.equals(objToMap.get("financial_year"))));
                        capexFinancialExpenseList.add(capexRelatedExpenseForecastExpense);
                    }
                }
                fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                getCapexRelatedExpenseForecastDetailMaster.setFields(fieldList);
                getCapexRelatedExpenseForecastDetailMaster.setCapexFinancialExpenseList(capexFinancialExpenseList);
                getCapexRelatedExpenseForecastDetailMasterList.add(getCapexRelatedExpenseForecastDetailMaster);
            }
            getCapexRelatedExpenseForecastDetailMasterList.sort(Comparator.comparingInt(GetCapexRelatedExpenseForecastDetailMaster::getRow_order));
        }

        return getCapexRelatedExpenseForecastDetailMasterList;
    }

    private List<FinancialMonthlyExpenseData> setCapexRelatedExpenseForecastExpenseData(Map<String, Object> objToMap, String startMonth, boolean isSameFinancialYear) {

        List<FinancialMonthlyExpenseData> financialMonthlyExpenseData = new ArrayList<>();

        Map<String, Object> collect = objToMap.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        collect.remove("id");
        collect.remove("capex_forecast_id");

        Map<Integer, FinancialMonthlyExpenseData> orderedData = new HashMap<>();

        for (Map.Entry<String, Object> getCapexExpenseDetails : collect.entrySet()) {
            if (!getCapexExpenseDetails.getKey().equalsIgnoreCase("financial_year")) {
                FinancialMonthlyExpenseData monthlyExpenseData = new FinancialMonthlyExpenseData(getCapexExpenseDetails.getKey(), (Long) getCapexExpenseDetails.getValue());
                
                orderedData.put(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(monthlyExpenseData.getMonth()), monthlyExpenseData);
            }
        }
        if(isSameFinancialYear){
            for(int i = FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i<=12; i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        else{
            for(int i = 1; i<FinancialUtils.STRING_INTEGER_MONTH_MAP.get(startMonth); i++){
                if(orderedData.get(i)!=null){
                    FinancialMonthlyExpenseData data = orderedData.get(i);
                    data.setMonth(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(FinancialUtils.STRING_INTEGER_MONTH_MAP.get(data.getMonth())));
                    financialMonthlyExpenseData.add(data);
                }
                else{
                    financialMonthlyExpenseData.add(new FinancialMonthlyExpenseData(FinancialUtils.INTEGER_STRING_MONTH_MAP.get(i), 0L));
                }
            }
        }
        return financialMonthlyExpenseData;
    }
}
