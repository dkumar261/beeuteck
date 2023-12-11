package com.forecastera.service.projectmanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.projectmanagement.commonResponseUtil.Error;
import com.forecastera.service.projectmanagement.config.exception.DataNotFoundException;
import com.forecastera.service.projectmanagement.dto.request.*;
import com.forecastera.service.projectmanagement.dto.response.*;
import com.forecastera.service.projectmanagement.dto.utilityClass.*;
import com.forecastera.service.projectmanagement.entity.*;
import com.forecastera.service.projectmanagement.repository.*;
import com.forecastera.service.projectmanagement.util.ProjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * @Author Uttam Kachhad
 * @Create 17-05-2023
 * @Description
 */
@Service
public class ProjectService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectMgmtRepo projectMgmtRepo;

    @Autowired
    private ExcelProcessingService excelProcessingService;

    @Autowired
    private ProjectMgmtFieldValueRepo projectMgmtFieldValueRepo;

    @Autowired
    private ProjectMgmtFieldMapRepo projectMgmtFieldMapRepo;

    @Autowired
    private ProjectCustomPicklistRepo projectCustomPicklistRepo;

    @Autowired
    private ResourceMgmtRepo resourceMgmtRepo;

    @Autowired
    private ProjectMgmtStatusRepo projectMgmtStatusRepo;

    @Autowired
    private ProjectMgmtTypeRepo projectMgmtTypeRepo;

    @Autowired
    private ProjectMgmtPriorityRepo projectMgmtPriorityRepo;

    @Autowired
    private ProjectResourceMappingRepo projectResourceMappingRepo;

    @Autowired
    private ProjectResourceMappingExtendedRepo projectResourceMappingExtendedRepo;

    @Autowired
    private ResourceMgmtRolesRepo resourceMgmtRolesRepo;

    @Autowired
    private ResourceMgmtDepartmentRepo resourceMgmtDepartmentRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private final EntityManagerFactory emf;
    private final EntityManager entityManager;

    @Autowired
    GeneralSettingsRepo generalSettingsRepo;

    @Autowired
    private ResourceMgmtStatusRepo resourceMgmtStatusRepo;

    @Autowired
    private RequestedResourcesRepo requestedResourcesRepo;

    @Autowired
    private DelegatedDepartmentHistoryRepo delegatedDepartmentHistoryRepo;

    @Autowired
    private DelegatedResourceHistoryRepo delegatedResourceHistoryRepo;

    @Autowired
    public ProjectService(EntityManagerFactory emf, EntityManager entityManager) {
        this.emf = emf;
        this.entityManager = emf.createEntityManager();
    }

//    public ProjectService(ProjectMgmtRepo projectMgmtRepo, ObjectMapper objectMapper) {
//        this.projectMgmtRepo = projectMgmtRepo;
//        this.objectMapper = objectMapper;
//    }

    public Object getAll(String email) {
//        List<ProjectMgmt> projectMgmtList = projectMgmtRepo.findAll();
//        return projectMgmtList.stream().map(projectMgmt -> ProjectUtils.toModel
//                (projectMgmt, GetProjectMgmt.class, objectMapper)).collect(Collectors.toList());
        List<Integer> listOfVisibleDepartments = getVisibleProjectIds(email, false);
        return listOfVisibleDepartments;
    }

    public void dateCheckExtraFunction(){
        String date = "2023-08-17T00:00:00.000Z";
        Instant instant = Instant.parse(date);
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println(localDate);
        LocalDate localDate1 = instant.atZone(ZoneId.of("UTC")).toLocalDate();
        System.out.println(localDate1);
    }

    private List<Integer> getVisibleProjectIds(String email, Boolean myView){

//        get list of all managed department by this resource, if it exists
        List<ResourceMgmt> resourceMgmt = resourceMgmtRepo.getResourceByEmail(email);

        Set<Integer> visibleProjectIds = new HashSet<>();

        if(myView==null || myView) {
//        if resource exists
            if (resourceMgmt != null && !resourceMgmt.isEmpty()) {
                ResourceMgmt currentUser = resourceMgmt.get(0);

                List<ProjectDetailsForNotification> projectDetails = projectMgmtRepo.getProjectDetailsForNotification();
                for (ProjectDetailsForNotification eachProject : projectDetails) {
                    if (currentUser.getResourceId().equals(Integer.valueOf(eachProject.getResourceId()))) {
                        visibleProjectIds.add(eachProject.getProjectId());
                    }
                }
            }
        }
        else{
            List<ProjectMgmt> projectMgmtList = projectMgmtRepo.findAll();
            visibleProjectIds = projectMgmtList.stream().map(ProjectMgmt::getProjectId).collect(Collectors.toSet());
        }

        return new ArrayList<>(visibleProjectIds);
    }

    /*private List<Integer> getVisibleDepartmentIds(String email){

        Set<Integer> listOfVisibleDepartmentIds = null;

//        get list of all managed department by this resource, if it exists
        List<ResourceIdDepartmentHead> allDepartmentsUnderResource = resourceMgmtRepo.getAllDepartmentsUnderResource(email);

//        if resource exists
        if(allDepartmentsUnderResource!=null && !allDepartmentsUnderResource.isEmpty()){

            Set<Integer> listOfManagedDepartmentIds = new HashSet<>();
//            collect list of managed department from the resource
            for(ResourceIdDepartmentHead eachRecord: allDepartmentsUnderResource){
                if(eachRecord.getManagedDepartmentId()!=null) {
                    listOfManagedDepartmentIds.add(eachRecord.getManagedDepartmentId());
                }
            }

            listOfManagedDepartmentIds.add(allDepartmentsUnderResource.get(0).getDepartmentId());
            List<DelegatedDepartmentHistory> delegatedDepartmentHistoryList = delegatedDepartmentHistoryRepo.findAll();
            for(DelegatedDepartmentHistory eachRecord: delegatedDepartmentHistoryList){
                LocalDate localDate = LocalDate.now();
                Date currentDate = java.sql.Date.valueOf(localDate);
                if(eachRecord.getIsActive() && eachRecord.getResourceId().equals(allDepartmentsUnderResource.get(0).getResourceId())
                        && eachRecord.getDelegationStartDate().compareTo(currentDate)<=0
                        && eachRecord.getDelegationEndDate().compareTo(currentDate)>=0){
                    listOfManagedDepartmentIds.add(eachRecord.getDelegatedDepartmentId());
                }
            }

//            get department hierarchy
            String getDepartmentHierarchyQuery = ProjectUtils.getFile("getDepartmentHierarchyQuery.txt");
            Query query1 = entityManager.createNativeQuery(getDepartmentHierarchyQuery).unwrap(org.hibernate.query.Query.class);
            ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(DepartmentHierarchy.class));
            List<DepartmentHierarchy> getDepartmentHierarchyList = query1.getResultList();

//            add managed department list to visible list
            listOfVisibleDepartmentIds = new HashSet<>();

//            for each managed department, find all the sub departments and add to visible list
            for (Integer eachDepartmentId : listOfManagedDepartmentIds) {
//                get the list of all sub departments from the department hierarchy
                for (int i = 0; i < getDepartmentHierarchyList.size(); i++) {
                    if (getDepartmentHierarchyList.get(i).getDepartment_id().equals(eachDepartmentId)) {
                        DepartmentHierarchy topNode = getDepartmentHierarchyList.get(i);
                        if(!listOfVisibleDepartmentIds.contains(topNode.getDepartment_id())) {
                            listOfVisibleDepartmentIds.add(topNode.getDepartment_id());
                            Integer depthLevel = topNode.getDepth_order();
                            i++;
                            while (i < getDepartmentHierarchyList.size() && getDepartmentHierarchyList.get(i).getDepth_order() > depthLevel) {
                                if(listOfVisibleDepartmentIds.contains(getDepartmentHierarchyList.get(i).getDepartment_id())){
                                    break;
                                }
                                else {
                                    listOfVisibleDepartmentIds.add(getDepartmentHierarchyList.get(i).getDepartment_id());
                                    i++;
                                }
                            }
                        }
                    }
                }
            }

        }

        if(listOfVisibleDepartmentIds!=null && !listOfVisibleDepartmentIds.isEmpty()) {
            return new ArrayList<>(listOfVisibleDepartmentIds);
        }
        else{
            return new ArrayList<>();
        }
    }*/

    public List<GetOverAllProgressMaster> getOverAllProgress(GetDateRange getDateRange, String email, Boolean myView) {

        if(ProjectUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

//        getDateRange.setStartDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getStartDate()));
//        getDateRange.setEndDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getEndDate()));

        List<Integer> visibleProjectIds = getVisibleProjectIds(email, myView);
        Set<Integer> getVisibleProjectIdsSet = new HashSet<>(visibleProjectIds);

        List<GetOverAllProgress> getOverAllProgressList = projectMgmtRepo.getOverAllProgress(getDateRange.getStartDate(), getDateRange.getEndDate());

        List<ProjectMgmtStatus> projectMgmtStatus = projectMgmtStatusRepo.findAll();

        Map<Integer, Long> projectCount = new HashMap<>();
        Map<Integer, List<ProjectDetailsForChart>> projectDetailsMap = new HashMap<>();

            for (GetOverAllProgress eachProject : getOverAllProgressList) {
                if(eachProject.getDepartmentId()!=null
                        && !eachProject.getDepartmentId().isEmpty()
                        && getVisibleProjectIdsSet.contains(eachProject.getProjectId())) {
                    Integer statusId = null;

                    for (ProjectMgmtStatus eachStatus : projectMgmtStatus) {
                        if (eachStatus.getStatusId() == eachProject.getStatusId()) {
                            statusId = eachStatus.getStatusId();
                            break;
                        }
                    }

                    projectCount.merge(statusId, 1L, Long::sum);

                    List<ProjectDetailsForChart> projectDetailsForCharts;
                    if (projectDetailsMap.containsKey(statusId)) {
                        projectDetailsForCharts = projectDetailsMap.get(statusId);
                    } else {
                        projectDetailsForCharts = new ArrayList<>();
                    }
                    projectDetailsForCharts.add(new ProjectDetailsForChart(eachProject.getProjectId(), eachProject.getProjectName()));
                    projectDetailsMap.put(statusId, projectDetailsForCharts);
                }
            }

            List<GetOverAllProgressMaster> getOverAllProgressMaster = new ArrayList<>();

            for (ProjectMgmtStatus eachStatus : projectMgmtStatus) {
                if (projectCount.containsKey(eachStatus.getStatusId())) {
                    GetOverAllProgressMaster eachLevel = new GetOverAllProgressMaster();
                    eachLevel.setStatusId(eachStatus.getStatusId());
                    eachLevel.setStatusColor(eachStatus.getColor());
                    eachLevel.setProjectCount(projectCount.get(eachStatus.getStatusId()));
                    eachLevel.setStatusName(eachStatus.getStatus());
                    List<ProjectDetailsForChart> projectDetailsForCurrentStatus = projectDetailsMap.get(eachStatus.getStatusId());
                    projectDetailsForCurrentStatus.sort(Comparator.comparing(ProjectDetailsForChart::getProjectName, (p1, p2)->{
                        String project1 = p1.toLowerCase();
                        String project2 = p2.toLowerCase();
                        return String.CASE_INSENSITIVE_ORDER.compare(project1, project2);
                    }));
                    eachLevel.setProjectDetails(projectDetailsForCurrentStatus);
                    getOverAllProgressMaster.add(eachLevel);
                }
            }

            if(getOverAllProgressMaster.isEmpty()){
                getOverAllProgressMaster = null;
            }

        return getOverAllProgressMaster;
    }

    public List<GetNoOfProjectsByTypeMaster> getNoOfProjectsByType(GetDateRange getDateRange, String email, Boolean myView) {

        if(ProjectUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

//        getDateRange.setStartDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getStartDate()));
//        getDateRange.setEndDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getEndDate()));

        List<Integer> visibleProjectIds = getVisibleProjectIds(email, myView);
        Set<Integer> getVisibleProjectIdsSet = new HashSet<>(visibleProjectIds);

        List<GetNoOfProjectsByType> getNoOfProjectsByTypeList = projectMgmtRepo.getNoOfProjectsByType(getDateRange.getStartDate(), getDateRange.getEndDate());

        List<ProjectMgmtType> projectMgmtType = projectMgmtTypeRepo.findAll();

        Map<Integer, Long> projectCount = new HashMap<>();
        Map<Integer, List<ProjectDetailsForChart>> projectDetailsMap = new HashMap<>();

        for (GetNoOfProjectsByType eachProject : getNoOfProjectsByTypeList) {
            if(eachProject.getDepartmentId()!=null
                    && !eachProject.getDepartmentId().isEmpty()
                    && getVisibleProjectIdsSet.contains(eachProject.getProjectId())) {
                Integer typeId = null;

                for (ProjectMgmtType eachType : projectMgmtType) {
                    if (eachType.getTypeId() == eachProject.getTypeId()) {
                        typeId = eachType.getTypeId();
                        break;
                    }
                }

                projectCount.merge(typeId, 1L, Long::sum);

                List<ProjectDetailsForChart> projectDetailsForCharts;
                if (projectDetailsMap.containsKey(typeId)) {
                    projectDetailsForCharts = projectDetailsMap.get(typeId);
                } else {
                    projectDetailsForCharts = new ArrayList<>();
                }
                projectDetailsForCharts.add(new ProjectDetailsForChart(eachProject.getProjectId(), eachProject.getProjectName()));
                projectDetailsMap.put(typeId, projectDetailsForCharts);
            }
        }

        List<GetNoOfProjectsByTypeMaster> getNoOfProjectsByTypeMaster = new ArrayList<>();

        for (ProjectMgmtType eachStatus : projectMgmtType) {
            if (projectCount.containsKey(eachStatus.getTypeId())) {
                GetNoOfProjectsByTypeMaster eachLevel = new GetNoOfProjectsByTypeMaster();
                eachLevel.setTypeId(eachStatus.getTypeId());
                eachLevel.setTypeColor(eachStatus.getColor());
                eachLevel.setProjectCount(projectCount.get(eachStatus.getTypeId()));
                eachLevel.setTypeName(eachStatus.getType());
                List<ProjectDetailsForChart> projectDetailsForCurrentStatus = projectDetailsMap.get(eachStatus.getTypeId());
                projectDetailsForCurrentStatus.sort(Comparator.comparing(ProjectDetailsForChart::getProjectName, (p1, p2)->{
                    String project1 = p1.toLowerCase();
                    String project2 = p2.toLowerCase();
                    return String.CASE_INSENSITIVE_ORDER.compare(project1, project2);
                }));
                eachLevel.setProjectDetails(projectDetailsForCurrentStatus);
                getNoOfProjectsByTypeMaster.add(eachLevel);
            }
        }

        if(getNoOfProjectsByTypeMaster.isEmpty()){
            getNoOfProjectsByTypeMaster = null;
        }

        return getNoOfProjectsByTypeMaster;

    }

    public GetTotalCost getTotalCostData(GetDateRange getDateRange, String email){
//
//        if(ProjectUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
//            return null;
//        }
//
////        getDateRange.setStartDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getStartDate()));
////        getDateRange.setEndDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getEndDate()));
//
////        get financial start month from settings
//        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
////        extract month from it
//        Month month = Month.valueOf(startMonth.toUpperCase());
//        int monthStart, monthEnd, financialYearMonthStart;
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(getDateRange.getStartDate());
//
////        get year from the start date of response
//        int summaryYearStart = calendar.get(Calendar.YEAR);
//        monthStart = calendar.get(Calendar.MONTH);
//
////        make a financial year start date using year from response start date
//        Date financialYearStart = java.sql.Date.valueOf(LocalDate.of(summaryYearStart, month, 1));
//        calendar.setTime(financialYearStart);
////        get month
//        financialYearMonthStart = calendar.get(Calendar.MONTH);
//
//        Integer summaryFinancialYearStart, summaryFinancialYearEnd;
//
////        if response start date is after the created financial start date,
////        then start year will be same, and data query will start from same year
////        else, it will start from previous year
//        if(financialYearStart.compareTo(getDateRange.getStartDate())<0){
//            summaryFinancialYearStart = summaryYearStart;
//        }
//        else{
//            summaryFinancialYearStart = summaryYearStart-1;
//        }
//
////        same with this, find the year of response end date
//        calendar.setTime(getDateRange.getEndDate());
//        int summaryYearEnd = calendar.get(Calendar.YEAR);
//        monthEnd = calendar.get(Calendar.MONTH);
//
////        for same year, make the financial start date
//        Date lastFinancialYearStartDate = java.sql.Date.valueOf(LocalDate.of(summaryYearEnd, month, 1));
////        if it lies after the financial year start date,
////        data will be collected till this year
////        else, it will stop at previous year
//        if(lastFinancialYearStartDate.compareTo(getDateRange.getEndDate())<0){
//            summaryFinancialYearEnd = summaryYearEnd;
//        }
//        else{
//            summaryFinancialYearEnd = summaryYearEnd-1;
//        }
//
//        Double internalCost = 0d, externalCost = 0d;
//
////        in first year, startPoint will decide from when should we add the data
//        int startPoint,endPoint;
//        startPoint = (monthStart>=financialYearMonthStart)?monthStart-financialYearMonthStart:(ProjectUtils.LOOP_END_POINT-financialYearMonthStart+monthStart+1);
//
////        if response date range lies in same financial year,
////        we need to calculate end point,
////        else it will be till the end of the year
//        if(summaryFinancialYearStart.equals(summaryFinancialYearEnd)){
//            endPoint = (monthEnd>=financialYearMonthStart)?monthEnd-financialYearMonthStart:(ProjectUtils.LOOP_END_POINT-financialYearMonthStart+monthEnd+1);
//        }
//        else{
//            endPoint=ProjectUtils.LOOP_END_POINT;
//        }
//
////        collect list of project ids which lie in the response date range
//        List<Integer> projectIdList = projectMgmtRepo.getProjectIdsByDateRange(getDateRange.getStartDate(), getDateRange.getEndDate());
//
//
////        collect data for the first financial year
//        List<SummaryDataResultDto> internalSummaryData = getProjectFinanceBudgetInternalLabor(summaryFinancialYearStart, projectIdList);
//        List<Double> getTotalInternalData = internalSummaryData.stream()
//                .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
//                        && it.getDescription().equals(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//        internalCost = internalCost + addDoubleListElements(getTotalInternalData,startPoint,endPoint);
//
//        List<SummaryDataResultDto> externalSummaryData = getProjectFinanceBudgetVendorLabor(summaryFinancialYearStart, projectIdList);
//        List<Double> getTotalExternalData = externalSummaryData.stream()
//                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                        && it.getDescription().equals(ProjectUtils.TOTAL_VENDOR_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//        externalCost = externalCost + addDoubleListElements(getTotalExternalData,startPoint,endPoint);
//
////        if data to collect is more than one financial year
//        for(int currentYear = summaryFinancialYearStart+1; currentYear<summaryFinancialYearEnd; currentYear++){
//            internalSummaryData = getProjectFinanceBudgetInternalLabor(currentYear, projectIdList);
//            getTotalInternalData = internalSummaryData.stream()
//                    .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
//                            && it.getDescription().equals(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//            internalCost = internalCost + addDoubleListElements(getTotalInternalData,ProjectUtils.LOOP_START_POINT,ProjectUtils.LOOP_END_POINT);
//
//
//
//            externalSummaryData = getProjectFinanceBudgetVendorLabor(currentYear, projectIdList);
//            getTotalExternalData = externalSummaryData.stream()
//                    .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                            && it.getDescription().equals(ProjectUtils.TOTAL_VENDOR_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//            externalCost = externalCost + addDoubleListElements(getTotalExternalData,startPoint,endPoint);
//        }
//
////        for last financial year
//        if(!summaryFinancialYearStart.equals(summaryFinancialYearEnd)){
//            startPoint = ProjectUtils.LOOP_START_POINT;
//            endPoint = (monthEnd>=financialYearMonthStart)?monthEnd-financialYearMonthStart:(ProjectUtils.LOOP_END_POINT-financialYearMonthStart+monthEnd+1);
//            internalSummaryData = getProjectFinanceBudgetInternalLabor(summaryFinancialYearEnd, projectIdList);
//            getTotalInternalData = internalSummaryData.stream()
//                    .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
//                            && it.getDescription().equals(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//            internalCost = internalCost + addDoubleListElements(getTotalInternalData,startPoint,endPoint);
//
//
//
//            externalSummaryData = getProjectFinanceBudgetVendorLabor(summaryFinancialYearEnd, projectIdList);
//            getTotalExternalData = externalSummaryData.stream()
//                    .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                            && it.getDescription().equals(ProjectUtils.TOTAL_VENDOR_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//            externalCost = externalCost + addDoubleListElements(getTotalExternalData,startPoint,endPoint);
//        }
//
////        if data collected is zero, by any chance
//        if(internalCost.equals(0d) && externalCost.equals(0d)){
//            return null;
//        }
//
////        else
//        double internalPercentage = ProjectUtils.roundToOneDecimalPlace((internalCost*100)/(internalCost + externalCost));
//        double externalPercentage = ProjectUtils.roundToOneDecimalPlace((externalCost*100)/(internalCost + externalCost));
//
//        return new GetTotalCost(internalPercentage, externalPercentage);

        return null;
    }

    public GetTotalBudget getTotalBudgetData(GetDateRange getDateRange, String email){

//        if(ProjectUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
//            return null;
//        }
//
////        getDateRange.setStartDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getStartDate()));
////        getDateRange.setEndDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getEndDate()));
//
////        get financial start month from settings
//        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
////        extract month from it
//        Month month = Month.valueOf(startMonth.toUpperCase());
//        int monthStart, monthEnd, financialYearMonthStart;
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(getDateRange.getStartDate());
//
////        get year from the start date of response
//        int summaryYearStart = calendar.get(Calendar.YEAR);
//        monthStart = calendar.get(Calendar.MONTH);
//
////        make a financial year start date using year from response start date
//        Date financialYearStart = java.sql.Date.valueOf(LocalDate.of(summaryYearStart, month, 1));
//        calendar.setTime(financialYearStart);
////        get month
//        financialYearMonthStart = calendar.get(Calendar.MONTH);
//
//        Integer summaryFinancialYearStart, summaryFinancialYearEnd;
//
////        if response start date is after the created financial start date,
////        then start year will be same, and data query will start from same year
////        else, it will start from previous year
//        if(financialYearStart.compareTo(getDateRange.getStartDate())<0){
//            summaryFinancialYearStart = summaryYearStart;
//        }
//        else{
//            summaryFinancialYearStart = summaryYearStart-1;
//        }
//
////        same with this, find the year of response end date
//        calendar.setTime(getDateRange.getEndDate());
//        int summaryYearEnd = calendar.get(Calendar.YEAR);
//        monthEnd = calendar.get(Calendar.MONTH);
//
////        for same year, make the financial start date
//        Date lastFinancialYearStartDate = java.sql.Date.valueOf(LocalDate.of(summaryYearEnd, month, 1));
////        if it lies after the financial year start date,
////        data will be collected till this year
////        else, it will stop at previous year
//        if(lastFinancialYearStartDate.compareTo(getDateRange.getEndDate())<0){
//            summaryFinancialYearEnd = summaryYearEnd;
//        }
//        else{
//            summaryFinancialYearEnd = summaryYearEnd-1;
//        }
//
//        Double budgetCost = 0d, forecastCost = 0d;
//
////        in first year, startPoint will decide from when should we add the data
//        int startPoint,endPoint;
//        startPoint = (monthStart>=financialYearMonthStart)?monthStart-financialYearMonthStart:(ProjectUtils.LOOP_END_POINT-financialYearMonthStart+monthStart+1);
//
////        if response date range lies in same financial year,
////        we need to calculate end point,
////        else it will be till the end of the year
//        if(summaryFinancialYearStart.equals(summaryFinancialYearEnd)){
//            endPoint = (monthEnd>=financialYearMonthStart)?monthEnd-financialYearMonthStart:(ProjectUtils.LOOP_END_POINT-financialYearMonthStart+monthEnd+1);
//        }
//        else{
//            endPoint=ProjectUtils.LOOP_END_POINT;
//        }
//
////        collect list of project ids which lie in the response date range
//        List<Integer> projectIdList = projectMgmtRepo.getProjectIdsByDateRange(getDateRange.getStartDate(), getDateRange.getEndDate());
//
//
////        collect data for the first financial year
//        List<SummaryDataResultDto> projectBudgetSummary = getProjectFinanceBudgetSummary(summaryFinancialYearStart, projectIdList);
//        List<Double> getTotalBudgetData = projectBudgetSummary.stream()
//                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                        && it.getDescription().equals(ProjectUtils.TOTAL_BUDGET_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//        budgetCost = budgetCost + addDoubleListElements(getTotalBudgetData,startPoint,endPoint);
//
//        List<SummaryDataResultDto> externalSummaryData = getProjectFinanceForecastSummary(summaryFinancialYearStart, projectIdList);
//        List<Double> getTotalForecastData = externalSummaryData.stream()
//                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                        && it.getDescription().equals(ProjectUtils.TOTAL_FORECAST_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//        forecastCost = forecastCost + addDoubleListElements(getTotalForecastData,startPoint,endPoint);
//
////        if data to collect is more than one financial year
//        for(int currentYear = summaryFinancialYearStart+1; currentYear<summaryFinancialYearEnd; currentYear++){
//            projectBudgetSummary = getProjectFinanceBudgetSummary(currentYear, projectIdList);
//            getTotalBudgetData = projectBudgetSummary.stream()
//                    .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                            && it.getDescription().equals(ProjectUtils.TOTAL_BUDGET_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//            budgetCost = budgetCost + addDoubleListElements(getTotalBudgetData,ProjectUtils.LOOP_START_POINT,ProjectUtils.LOOP_END_POINT);
//
//
//
//            externalSummaryData = getProjectFinanceForecastSummary(currentYear, projectIdList);
//            getTotalForecastData = externalSummaryData.stream()
//                    .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                            && it.getDescription().equals(ProjectUtils.TOTAL_FORECAST_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//            forecastCost = forecastCost + addDoubleListElements(getTotalForecastData,startPoint,endPoint);
//        }
//
////        for last financial year
//        if(!summaryFinancialYearStart.equals(summaryFinancialYearEnd)){
//            startPoint = ProjectUtils.LOOP_START_POINT;
//            endPoint = (monthEnd>=financialYearMonthStart)?monthEnd-financialYearMonthStart:(ProjectUtils.LOOP_END_POINT-financialYearMonthStart+monthEnd+1);
//            projectBudgetSummary = getProjectFinanceBudgetSummary(summaryFinancialYearEnd, projectIdList);
//            getTotalBudgetData = projectBudgetSummary.stream()
//                    .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                            && it.getDescription().equals(ProjectUtils.TOTAL_BUDGET_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//            budgetCost = budgetCost + addDoubleListElements(getTotalBudgetData,startPoint,endPoint);
//
//
//
//            externalSummaryData = getProjectFinanceForecastSummary(summaryFinancialYearEnd, projectIdList);
//            getTotalForecastData = externalSummaryData.stream()
//                    .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
//                            && it.getDescription().equals(ProjectUtils.TOTAL_FORECAST_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());
//            forecastCost = forecastCost + addDoubleListElements(getTotalForecastData,startPoint,endPoint);
//        }
//
//        budgetCost = ProjectUtils.roundToOneDecimalPlace(budgetCost);
//        forecastCost = ProjectUtils.roundToOneDecimalPlace(forecastCost);
//
//        return new GetTotalBudget(budgetCost, forecastCost);
        return null;
    }

    public Object downloadCreateProjectExcelFile() throws IOException {

        List<ProjectMgmtFieldMap> projectMgmtFieldMapList = projectMgmtFieldMapRepo.findAll();
        projectMgmtFieldMapList.sort(Comparator.comparing(ProjectMgmtFieldMap::getFieldId));

        byte[] excelFile = null;
        try {
            excelFile = excelProcessingService.generateCreateProjectExcelFile(projectMgmtFieldMapList);
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ProjectUtils.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        return excelFile;
    }

    public Object createProjectDetailsFromExcel(@RequestPart MultipartFile excelFile, String modifiedBy) throws IOException{

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        String dateFormat = generalSettingsList.get(0).getDateFormat();
        dateFormat = ProjectUtils.VALID_DATE_FORMAT.get(dateFormat);

//        Get all the field data, as well as all picklist data

        List<ProjectMgmtFieldMap> projectMgmtFieldMapList = projectMgmtFieldMapRepo.findAll();
        Map<String, ProjectMgmtFieldMap> projectMgmtFieldMapListToMap = new HashMap<>();
        for(ProjectMgmtFieldMap data: projectMgmtFieldMapList){
            projectMgmtFieldMapListToMap.put(data.getFields().toLowerCase(), data);
        }

        List<ProjectMgmtStatus> projectMgmtStatusList = projectMgmtStatusRepo.findAll();
        Map<String, ProjectMgmtStatus> projectMgmtStatusListToMap = new HashMap<>();
        for(ProjectMgmtStatus data: projectMgmtStatusList){
            projectMgmtStatusListToMap.put(data.getStatus().toLowerCase(), data);
        }

        List<ProjectMgmtType> projectMgmtTypeList = projectMgmtTypeRepo.findAll();
        Map<String, ProjectMgmtType> projectMgmtTypeListToMap = new HashMap<>();
        for(ProjectMgmtType data: projectMgmtTypeList){
            projectMgmtTypeListToMap.put(data.getType().toLowerCase(), data);
        }

        List<ProjectMgmtPriority> projectMgmtPriorityList = projectMgmtPriorityRepo.findAll();
        Map<String, ProjectMgmtPriority> projectMgmtPriorityListToMap = new HashMap<>();
        for(ProjectMgmtPriority data: projectMgmtPriorityList){
            projectMgmtPriorityListToMap.put(data.getPriority().toLowerCase(), data);
        }

        List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
        Map<String, ResourceMgmtDepartment> resourceMgmtDepartmentListToMap = new HashMap<>();
        for(ResourceMgmtDepartment data: resourceMgmtDepartmentList){
            resourceMgmtDepartmentListToMap.put(data.getDepartment().toLowerCase(), data);
        }

        List<ResourceIdEmail> resourceIdEmailList = resourceMgmtRepo.getAllResourceIdEmail();
        Map<String, ResourceIdEmail> resourceIdEmailMap = new HashMap<>();
        for(ResourceIdEmail data: resourceIdEmailList){
            resourceIdEmailMap.put(data.getEmail(), data);
        }

        List<ProjectCustomPicklist> projectCustomPicklistList = projectCustomPicklistRepo.findAll();
        Map<Integer, List<ProjectCustomPicklist>> projectCustomPicklistListGroupByPicklistId
                = projectCustomPicklistList.stream().collect(Collectors.groupingBy(ProjectCustomPicklist::getPicklistId));

        Map<Integer, Map<String, ProjectCustomPicklist>> projectCustomPicklistListToMap = new HashMap<>();
        for(Map.Entry<Integer, List<ProjectCustomPicklist>> entry: projectCustomPicklistListGroupByPicklistId.entrySet()){

            List<ProjectCustomPicklist> thisPicklistValues = entry.getValue();

            Map<String, ProjectCustomPicklist> groupByPicklistIdToMap = new HashMap<>();
            for(ProjectCustomPicklist data: thisPicklistValues){
                groupByPicklistIdToMap.put(data.getPicklistValue().toLowerCase(), data);
            }

            projectCustomPicklistListToMap.put(entry.getKey(), groupByPicklistIdToMap);
        }

//        get all the project data converted to create project format
        Map<String, Object> returnData = excelProcessingService.excelToProjectDetails(excelFile, projectMgmtFieldMapListToMap, projectMgmtStatusListToMap,
                projectMgmtTypeListToMap, projectMgmtPriorityListToMap, resourceIdEmailMap, projectCustomPicklistListToMap, dateFormat, resourceMgmtDepartmentListToMap);

        if(!(boolean)returnData.get("result")){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ProjectUtils.INCORRECT_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        List<String> result = new ArrayList<>();
        int numberOfFields = (int)returnData.get("numberOfFields");
        List<PostProjectMgmt> listOfProjects = (List<PostProjectMgmt>)returnData.get("projectList");

        List<AddProject> projectNameFromDb = projectMgmtRepo.getProjectList();

        if(listOfProjects!=null && !listOfProjects.isEmpty()) {
            for (PostProjectMgmt newProject : listOfProjects) {
                String resultOfInsert = createProjectDetailsFromExcelExtended(newProject, modifiedBy, projectMgmtFieldMapList, projectNameFromDb);
                result.add(resultOfInsert);
            }
        }

        return excelProcessingService.generateUploadedCreateProjectExcelFileResponse(excelFile, result, numberOfFields);
    }

    public String createProjectDetailsFromExcelExtended(PostProjectMgmt postProjectMgmt, String modifiedBy, List<ProjectMgmtFieldMap> projectMgmtFieldMaps, List<AddProject> projectNameFromDb) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating Project..");
        }

        List<CreateProjectData> allProjectDataList = postProjectMgmt.getProjectData();

//        will check if all the compulsory fields are present,
//        as well as if the data inside all the coming fields is correct or not

        List<CreateProjectData> allValidProjectDataList = new ArrayList<>();
        Map<Integer, CreateProjectData> allProjectDataListToMap
                = allProjectDataList.stream().collect(Collectors.toMap(CreateProjectData::getFieldId, fieldData -> fieldData));

        Date startDate = null, endDate = null, actualStartDate = null, actualEndDate = null;

        String projectName = null;

        for(ProjectMgmtFieldMap eachField: projectMgmtFieldMaps){

//            compulsory data and field type validation
            if(eachField.getIsEnabled().equals("1") && eachField.getProjCreationView().equals("1")){

                boolean isFieldCompulsory = false, isDataAddedToList = false;

                if(eachField.getIsEnabledFreeze().equals("1") && eachField.getIsEditAccessDdl().equals("1")){
                    isFieldCompulsory = true;
                }

                if(allProjectDataListToMap.containsKey(eachField.getFieldId())){

                    CreateProjectData currentFieldData = allProjectDataListToMap.get(eachField.getFieldId());

                    boolean isDataCorrect = true;
                    try {
                        if (currentFieldData.getValue()!=null && !currentFieldData.getValue().isEmpty()){
                            switch (eachField.getFieldType()) {
                                case ProjectUtils.FIELD_TYPE_PICKLIST:
                                    Set<Integer> allIds = new HashSet<>();
                                    String[] ids = currentFieldData.getValue().split(",");
                                    for (String eachId : ids) {
                                        allIds.add(Integer.valueOf(eachId));
                                    }
                                    if(allIds.size()>1){
                                        return ProjectUtils.PICKLIST_MORE_THAN_ONE_VALUE;
                                    }
                                    currentFieldData.setValue(String.valueOf(currentFieldData.getValue()));
                                    break;

                                case ProjectUtils.FIELD_TYPE_PICKLIST_MULTISELECT:
                                    currentFieldData.setValue(String.valueOf(currentFieldData.getValue()));
                                    break;
                                case ProjectUtils.FIELD_TYPE_TEXT:
                                    if(eachField.getFields().equals(ProjectUtils.FIELD_PROJECT_NAME)) {
                                        projectName= ProjectUtils.removeAllExtraSpace(currentFieldData.getValue());
                                        for(AddProject projectNames:projectNameFromDb){
                                            if(projectName.equalsIgnoreCase(projectNames.getProjectName())){
                                                return ProjectUtils.PROJECT_PROJECT_NAME_DUPLICATE;
                                            }
                                        }
                                        currentFieldData.setValue(projectName);
                                    }

                                    break;
                                case ProjectUtils.FIELD_TYPE_DATE:
                                    LocalDate localDate = LocalDate.parse(currentFieldData.getValue());
                                    Date date = java.sql.Date.valueOf(localDate);
                                    currentFieldData.setValue(String.valueOf(date));
                                    if(currentFieldData.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_START_DATE)){
                                        startDate = date;
                                    }
                                    if(currentFieldData.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_END_DATE)){
                                        endDate = date;
                                    }
                                    if(currentFieldData.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_ACTUAL_START_DATE)){
                                        actualStartDate = date;
                                    }
                                    if(currentFieldData.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_ACTUAL_END_DATE)){
                                        actualEndDate = date;
                                    }
                                    break;

                                case ProjectUtils.FIELD_TYPE_NUMBER:
//                                    TODO:: can only proceed if we know what type of input to expect
                                    break;

                                case ProjectUtils.FIELD_TYPE_CURRENCY:
//                                    TODO:: ask for currency checks, like 2 decimal, negative currency, maximum limit, etc.
                                    break;

                                case ProjectUtils.FIELD_TYPE_EMAIL:
                                    String email = currentFieldData.getValue();
                                    String basicEmailFormat = "^([A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

                                    Pattern emailPattern = Pattern.compile(basicEmailFormat);
                                    boolean isEmailPatternCorrect =emailPattern.matcher(email).matches();
                                    if(!isEmailPatternCorrect){
                                        if(eachField.getSettingType().equals(ProjectUtils.FIELD_TYPE)){
                                            return ProjectUtils.ESSENTIAL_FIELDS_MISSING + ": " + ProjectUtils.INCORRECT_EMAIL_FORMAT;
                                        }
                                        LOGGER.warn(ProjectUtils.INCORRECT_EMAIL_FORMAT);
                                        isDataCorrect = false;
                                    }
                                    break;
                            }
                        }
                        else{
                            isDataCorrect = false;
                        }
                    }
                    catch(Exception e){
                        isDataCorrect = false;
                    }

                    if(isDataCorrect) {
                        allValidProjectDataList.add(currentFieldData);
                        isDataAddedToList = true;
                    }
                }
                if(isFieldCompulsory && !isDataAddedToList){
                    return ProjectUtils.ESSENTIAL_FIELDS_MISSING + ": " + eachField.getFields();
                }
            }
        }

        if(allValidProjectDataList.isEmpty()) {
            return ProjectUtils.ESSENTIAL_FIELDS_MISSING;
        }

        if(startDate!=null && endDate!=null){
            if(startDate.compareTo(endDate)>0){
                return ProjectUtils.PROJECT_START_DATE_AFTER_END_DATE;
            }
        }
        else{
            return ProjectUtils.PROJECT_START_END_DATE_EMPTY;
        }

        if(actualStartDate!=null && actualEndDate!=null){
            if(actualStartDate.compareTo(actualEndDate)>0){
                return ProjectUtils.PROJECT_ACTUAL_START_DATE_AFTER_ACTUAL_END_DATE;
            }
        }

        ProjectMgmt resourceManagement = new ProjectMgmt();

        resourceManagement.setCreatedBy(modifiedBy);
        resourceManagement.setCreatedDate(new Date());
        resourceManagement.setModifiedBy(modifiedBy);
        resourceManagement.setModifiedDate(new Date());

        ProjectMgmt projectManagementWithId = projectMgmtRepo.save(resourceManagement);

        List<ProjectMgmtFieldValue> createProjectCustomFields = new ArrayList<>();

        boolean isProgressNotPresent = true;

        for(CreateProjectData eachCustomField: allValidProjectDataList){
            if(eachCustomField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROGRESS)){
                isProgressNotPresent = false;
            }
            ProjectMgmtFieldValue eachCustomData = new ProjectMgmtFieldValue(projectManagementWithId, eachCustomField);
            eachCustomData.setCreatedBy(modifiedBy);
            eachCustomData.setCreatedDate(new Date());
            eachCustomData.setModifiedBy(modifiedBy);
            eachCustomData.setModifiedDate(new Date());
            createProjectCustomFields.add(eachCustomData);
        }

        if(isProgressNotPresent){
            Integer progressFieldId = projectMgmtFieldMapRepo.getProjectFieldIdByName(ProjectUtils.FIELD_PROJECT_PROGRESS);
            ProjectMgmtFieldValue progressField = new ProjectMgmtFieldValue();
            progressField.setProjectId(projectManagementWithId.getProjectId());
            progressField.setFieldId(progressFieldId);
            progressField.setFieldValue(ProjectUtils.PROJECT_ZERO_PROGRESS);
            progressField.setCreatedBy(modifiedBy);
            progressField.setCreatedDate(new Date());
            progressField.setModifiedBy(modifiedBy);
            progressField.setModifiedDate(new Date());
            createProjectCustomFields.add(progressField);
        }

        projectMgmtFieldValueRepo.saveAll(createProjectCustomFields);
        projectNameFromDb.add(new AddProject(projectManagementWithId.getProjectId(), projectName));
        return ProjectUtils.PROJECT_CREATED_SUCCESSFULLY;
    }

    private Object checkForValidPicklistIds (ProjectMgmtFieldMap fieldSetting, List<Integer> listOfIds, Map<Integer, ProjectCustomPicklist> projectCustomPicklistToMap){

        if(fieldSetting==null){
            String message = ProjectUtils.FIELD_ID_MISMATCH;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

//        check if picklist type has more than one values
        if(fieldSetting.getFieldType().equals(ProjectUtils.FIELD_TYPE_PICKLIST)){
            if(listOfIds.size()>1){
                String message = ProjectUtils.PICKLIST_MORE_THAN_ONE_VALUE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }

        List<Integer> correctIdsList = new ArrayList<>();

        if(fieldSetting.getSettingType()==3){
            for(Integer id: listOfIds){
                ProjectCustomPicklist customPicklist = projectCustomPicklistToMap.get(id);
                if(customPicklist!=null && customPicklist.getPicklistId().equals(fieldSetting.getFieldId())){
                    correctIdsList.add(id);
                }
            }
        }
        else{
            switch(fieldSetting.getFields()){
                case ProjectUtils.FIELD_PROJECT_PROJECT_OWNER:
                    Optional<ResourceMgmt> data = resourceMgmtRepo.findById(listOfIds.get(0));
                    data.ifPresent(resourceMgmt -> correctIdsList.add(resourceMgmt.getResourceId()));
                    break;

                case ProjectUtils.FIELD_PROJECT_STATUS:
                    Optional<ProjectMgmtStatus> data2 = projectMgmtStatusRepo.findById(listOfIds.get(0));
                    data2.ifPresent(projectMgmtStatus -> correctIdsList.add(projectMgmtStatus.getStatusId()));
                    break;

                case ProjectUtils.FIELD_PROJECT_PROJECT_TYPE:
                    Optional<ProjectMgmtType> data3 = projectMgmtTypeRepo.findById(listOfIds.get(0));
                    data3.ifPresent(projectMgmtType -> correctIdsList.add(projectMgmtType.getTypeId()));
                    break;

                case ProjectUtils.FIELD_PROJECT_PRIORITY:
                    Optional<ProjectMgmtPriority> data4 = projectMgmtPriorityRepo.findById(listOfIds.get(0));
                    data4.ifPresent(projectMgmtPriority -> correctIdsList.add(projectMgmtPriority.getPriorityId()));
                    break;

                case ProjectUtils.FIELD_PROJECT_DEPARTMENT:
                    Optional<ResourceMgmtDepartment> data5 = resourceMgmtDepartmentRepo.findById(listOfIds.get(0));
                    data5.ifPresent(resourceMgmtDepartment -> correctIdsList.add(resourceMgmtDepartment.getDepartmentId()));
                    break;
            }
        }

        if(correctIdsList.isEmpty()){
            return "";
        }

        return correctIdsList.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public Object createProject (PostProjectMgmt postProjectMgmt, String modifiedBy){

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating Project..");
        }
        List<CreateProjectData> allProjectDataList = postProjectMgmt.getProjectData();


//        project custom picklist options
        List<ProjectCustomPicklist> projectCustomPicklist =projectCustomPicklistRepo.findAll();
        Map<Integer, ProjectCustomPicklist> projectCustomPicklistToMap
                = projectCustomPicklist.stream().collect(Collectors.toMap(ProjectCustomPicklist::getId, pcp -> pcp));


//        will check if all the compulsory fields are present,
//        as well as if the data inside all the coming fields is correct or not

        List<CreateProjectData> allValidProjectDataList = new ArrayList<>();
        List<ProjectMgmtFieldMap> projectMgmtFieldMaps = projectMgmtFieldMapRepo.findAll();
        Map<Integer, CreateProjectData> allProjectDataListToMap
                = allProjectDataList.stream().collect(Collectors.toMap(CreateProjectData::getFieldId, fieldData -> fieldData));

        Date startDate = null, endDate = null, actualStartDate = null, actualEndDate = null;

        for(ProjectMgmtFieldMap eachField: projectMgmtFieldMaps){

//            compulsory data and field type validation
            if(eachField.getIsEnabled().equals("1") && eachField.getProjCreationView().equals("1")){

                boolean isFieldCompulsory = false, isDataAddedToList = false;

                if(eachField.getIsEnabledFreeze().equals("1") && eachField.getIsEditAccessDdl().equals("1")){
                    isFieldCompulsory = true;
                }

                if(allProjectDataListToMap.containsKey(eachField.getFieldId())){

                    CreateProjectData currentFieldData = allProjectDataListToMap.get(eachField.getFieldId());

                    boolean isDataCorrect = true;
                    try {
                        if (currentFieldData.getValue()!=null && !currentFieldData.getValue().isEmpty()){
                            switch (eachField.getFieldType()) {
                                case ProjectUtils.FIELD_TYPE_PICKLIST:
                                case ProjectUtils.FIELD_TYPE_PICKLIST_MULTISELECT:
                                    Set<Integer> allIds = new HashSet<>();
                                    String[] ids = currentFieldData.getValue().split(",");
                                    for (String eachId : ids) {
                                        allIds.add(Integer.valueOf(eachId));
                                    }

                                    Object returnData = checkForValidPicklistIds(eachField, new ArrayList<>(allIds), projectCustomPicklistToMap);

                                    if(returnData instanceof Error){
                                        return returnData;
                                    }

                                    currentFieldData.setValue(String.valueOf(returnData));

                                    break;
                                case ProjectUtils.FIELD_TYPE_TEXT:
                                    if(eachField.getFields().equals(ProjectUtils.FIELD_PROJECT_NAME)) {
                                        String projectName= ProjectUtils.removeAllExtraSpace(currentFieldData.getValue());
                                        List<AddProject> projectNameFromDb= projectMgmtRepo.getProjectList();
                                        for(AddProject projectNames:projectNameFromDb){
                                            if(projectName.equalsIgnoreCase(projectNames.getProjectName())){
                                                String message = ProjectUtils.PROJECT_PROJECT_NAME_DUPLICATE;
                                                Error error = new Error();
                                                error.setRequestAt(new Date());
                                                error.setMessage(message);
                                                error.setStatus(HttpStatus.BAD_REQUEST.value());
                                                return error;
                                            }
                                        }
                                        currentFieldData.setValue(projectName);
                                    }

                                    break;
                                case ProjectUtils.FIELD_TYPE_DATE:
                                    LocalDate localDate = LocalDate.parse(currentFieldData.getValue());
                                    Date date = java.sql.Date.valueOf(localDate);
                                    currentFieldData.setValue(String.valueOf(date));
                                    if(currentFieldData.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_START_DATE)){
                                        startDate = date;
                                    }
                                    if(currentFieldData.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_END_DATE)){
                                        endDate = date;
                                    }
                                    if(currentFieldData.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_ACTUAL_START_DATE)){
                                        actualStartDate = date;
                                    }
                                    if(currentFieldData.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_ACTUAL_END_DATE)){
                                        actualEndDate = date;
                                    }

                                    break;
                                case ProjectUtils.FIELD_TYPE_NUMBER:
//                                    TODO:: can only proceed if we know what type of input to expect

                                    break;
                                case ProjectUtils.FIELD_TYPE_CURRENCY:
//                                    TODO:: ask for currency checks, like 2 decimal, negative currency, maximum limit, etc.

                                    break;
                                case ProjectUtils.FIELD_TYPE_EMAIL:
                                    String email = currentFieldData.getValue();
                                    String basicEmailFormat = "^([A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

                                    Pattern emailPattern = Pattern.compile(basicEmailFormat);
                                    boolean isEmailPatternCorrect =emailPattern.matcher(email).matches();
                                    if(!isEmailPatternCorrect){
                                        LOGGER.warn("Incorrect email format");
                                        isDataCorrect = false;
                                    }
                                    break;

                            }
                        }
                    }
                    catch(Exception e){
                        isDataCorrect = false;
                    }

                    if(isDataCorrect) {
                        allValidProjectDataList.add(currentFieldData);
                        isDataAddedToList = true;
                    }
                }
                if(isFieldCompulsory && !isDataAddedToList){
                    String message = ProjectUtils.ESSENTIAL_FIELDS_MISSING;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
            }
        }
       if(allValidProjectDataList.isEmpty()){
            String message = ProjectUtils.ESSENTIAL_FIELDS_MISSING;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

       if(startDate!=null && endDate!=null){
            if(startDate.compareTo(endDate)>0){
                String message = ProjectUtils.PROJECT_START_DATE_AFTER_END_DATE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }
        else{
            String message = ProjectUtils.PROJECT_START_END_DATE_EMPTY;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        if(actualStartDate!=null && actualEndDate!=null){
            if(actualStartDate.compareTo(actualEndDate)>0){
                String message = ProjectUtils.PROJECT_ACTUAL_START_DATE_AFTER_ACTUAL_END_DATE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }
//        else if((actualStartDate!=null && actualEndDate==null) || (actualStartDate==null && actualEndDate!=null)){
//
//        }

//        saving record after all the checks
        ProjectMgmt projectManagement = new ProjectMgmt();

        projectManagement.setCreatedBy(modifiedBy);
        projectManagement.setCreatedDate(new Date());
        projectManagement.setModifiedBy(modifiedBy);
        projectManagement.setModifiedDate(new Date());

        ProjectMgmt projectManagementWithId = projectMgmtRepo.save(projectManagement);


//        boolean isStatusNotPresent = true;
        boolean isProgressNotPresent = true;

        List<ProjectMgmtFieldValue> createProjectFieldsValue = new ArrayList<>();

        for(CreateProjectData eachCustomField: allValidProjectDataList){
            if(eachCustomField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROGRESS)){
                isProgressNotPresent = false;
            }
            ProjectMgmtFieldValue eachCustomData = new ProjectMgmtFieldValue(projectManagementWithId, eachCustomField);
            eachCustomData.setCreatedBy(modifiedBy);
            eachCustomData.setCreatedDate(new Date());
            eachCustomData.setModifiedBy(modifiedBy);
            eachCustomData.setModifiedDate(new Date());
            createProjectFieldsValue.add(eachCustomData);
        }

        if(isProgressNotPresent){
            Integer progressFieldId = projectMgmtFieldMapRepo.getProjectFieldIdByName(ProjectUtils.FIELD_PROJECT_PROGRESS);
            ProjectMgmtFieldValue progressField = new ProjectMgmtFieldValue();
            progressField.setProjectId(projectManagementWithId.getProjectId());
            progressField.setFieldId(progressFieldId);
            progressField.setFieldValue(ProjectUtils.PROJECT_ZERO_PROGRESS);
            progressField.setCreatedBy(modifiedBy);
            progressField.setCreatedDate(new Date());
            progressField.setModifiedBy(modifiedBy);
            progressField.setModifiedDate(new Date());
            createProjectFieldsValue.add(progressField);
        }

        projectMgmtFieldValueRepo.saveAll(createProjectFieldsValue);

        return true;
    }

//    public List<GetProjectDetails> getProjectDetails(Integer projectId){
//
////        get the project details using projectId
//        GetProjectDetailsById projectDetail = projectMgmtRepo.getProjectDetailsById(projectId);
//        if (Objects.isNull(projectDetail)) {
//            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Project Not Found");
//        }
//
////        get the project custom details using projectId
//        List<ProjectMgmtFieldValue> projectCustomDetails = projectMgmtFieldValueRepo.getProjectCustomDetailsById(projectId);
//
////        convert project custom details to map of format <fieldId, custom details> for quick access
//        Map<Integer, ProjectMgmtFieldValue> projectCustomDetailsMap = new HashMap<>();
//        for(ProjectMgmtFieldValue eachDetail : projectCustomDetails){
//            projectCustomDetailsMap.put(eachDetail.getFieldId(), eachDetail);
//        }
//
////        get all the settings which are enabled and projDetailsView is true,
////        means it is visible for project details component
//        List<ProjectMgmtFieldMap> projectDetailsSetting = projectMgmtFieldMapRepo.getProjectDetailsSettings();
//
////        get all the custom picklist list, so that we can convert the picklist id
////        stored inside the project custom details
//        List<ProjectCustomPicklist> projectCustomPicklistList = projectCustomPicklistRepo.findAll();
//
////        convert the custom picklist list to map of format <id, custom picklist list> for quick access
//        Map<Integer, ProjectCustomPicklist> projectCustomPicklistMap = new HashMap<>();
//        for(ProjectCustomPicklist eachPicklist : projectCustomPicklistList){
//            projectCustomPicklistMap.put(eachPicklist.getId(), eachPicklist);
//        }
//
////        all visible settings and their values list
//        List<GetProjectDetails> getProjectDetails = new ArrayList<>();
//
////        traverse each visible setting
//        for(ProjectMgmtFieldMap eachSetting : projectDetailsSetting){
//
////            set details of the field
//            GetProjectDetails fieldDetails = new GetProjectDetails();
//            fieldDetails.setFieldName(eachSetting.getFields());
//            fieldDetails.setFieldId(eachSetting.getFieldId());
//            fieldDetails.setFieldType(eachSetting.getFieldType());
//
//            switch(eachSetting.getSettingType()){
//                case 4:
//                    /*
//                    both are pre-defined settings
//                    break statement is missing on purpose
//                     */
//                case 2:
////                    switch case to check the field name, based on which appropriate field value
////                    and its picklistId (if applicable) is assigned to the fieldDetails
//                    switch (eachSetting.getFields()){
//                        case "Project Name":
//                            fieldDetails.setFieldValue(projectDetail.getProjectName());
//                            break;
//                        case "Project Owner":
//                            if(projectDetail.getOwner()!=null){
//                                fieldDetails.setFieldValue(resourceMgmtRepo.getProjectOwnerById(projectDetail.getOwner()));
//                                fieldDetails.setPicklistId(String.valueOf(projectDetail.getOwner()));
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Status":
//                            if(projectDetail.getStatusId()!=null){
//                                fieldDetails.setFieldValue(projectMgmtStatusRepo.getStatusById(projectDetail.getStatusId()));
//                                fieldDetails.setPicklistId(String.valueOf(projectDetail.getStatusId()));
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Progress":
//                            fieldDetails.setFieldValue(projectDetail.getProgress());
//                            break;
//                        case "Budget":
//                            fieldDetails.setFieldValue(projectDetail.getBudget());
//                            break;
//                        case "Start Date":
//                            fieldDetails.setFieldValue(projectDetail.getStartDate());
//                            break;
//                        case "End Date":
//                            fieldDetails.setFieldValue(projectDetail.getEndDate());
//                            break;
//                        case "Actual Start Date":
//                            fieldDetails.setFieldValue(projectDetail.getActualStartDate());
//                            break;
//                        case "Actual End Date":
//                            fieldDetails.setFieldValue(projectDetail.getActualEndDate());
//                            break;
//                        case "Created By":
//                            fieldDetails.setFieldValue(projectDetail.getCreatedBy());
//                            break;
//                        case "Created Date":
//                            fieldDetails.setFieldValue(projectDetail.getCreatedDate());
//                            break;
//                        case "Modified By":
//                            fieldDetails.setFieldValue(projectDetail.getModifiedBy());
//                            break;
//                        case "Modified Date":
//                            fieldDetails.setFieldValue(projectDetail.getModifiedDate());
//                            break;
//                        case "Project Type":
//                            if(projectDetail.getTypeId()!=null){
//                                fieldDetails.setFieldValue(projectMgmtTypeRepo.getTypeById(projectDetail.getTypeId()));
//                                fieldDetails.setPicklistId(String.valueOf(projectDetail.getTypeId()));
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Priority":
//                            if(projectDetail.getPriorityId()!=null){
//                                fieldDetails.setFieldValue(projectMgmtPriorityRepo.getPriorityById(projectDetail.getPriorityId()));
//                                fieldDetails.setPicklistId(String.valueOf(projectDetail.getPriorityId()));
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Description":
//                            fieldDetails.setFieldValue(projectDetail.getDescription());
//                            break;
//                        default:
//                            fieldDetails.setFieldValue(null);
//                            LOGGER.warn("Unrecognised field_name: {}", eachSetting.getFields());
//                    }
//                    break;
//                case 3:
//                    /*
//                    custom settings
//                     */
//
////                    obtain the custom project data of the field based on field id
//                    ProjectMgmtFieldValue customFieldValue = projectCustomDetailsMap.get(eachSetting.getFieldId());
//
////                    check for picklist
//                    if(eachSetting.getFieldType().contains("Picklist")){
//
////                        check if custom data for this field exists
//                        if(customFieldValue!=null){
////                            if the field value is not null
//                            if(StringUtils.isNotBlank(customFieldValue.getFieldValue())){
//
////                            convert the comma separated id(s) into string lists (in case of multi-picklist)
//                                String[] picklistIdList = customFieldValue.getFieldValue().split(",");
//
////                            to store the picklist values associated with each picklist ids
//                                List<String> picklistIdValue = new ArrayList<>();
//
////                            for each id
//                                for(String id: picklistIdList){
////                                check its value and add to the list
//                                    picklistIdValue.add(projectCustomPicklistMap.get(Integer.valueOf(id)).getPicklistValue());
//                                }
//
////                            convert the list back to comma separated string,
//                                fieldDetails.setFieldValue(String.join(",",picklistIdValue));
//                                fieldDetails.setPicklistId(customFieldValue.getFieldValue());
//                            }
////                            if field value is null
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//
//                        }
//                        /*
//                        customFieldValue is null typically means that
//                          1. this field was created after the project creation, hence no record exists
//                          2. yet to find it
//                        in this case, we assign empty values to it, to avoid misunderstanding from the frontend
//                         */
//                        else{
//                            fieldDetails.setFieldValue("");
//                            fieldDetails.setPicklistId("");
//                        }
//                    }
////                    if not picklist, pass the values as it is
//                    else{
////                        again, check if records do exist
//                        if(customFieldValue!=null){
//                            fieldDetails.setFieldValue(customFieldValue.getFieldValue());
//                        }
////                        if not, assign empty string
//                        else{
//                            fieldDetails.setFieldValue("");
//                        }
//
//                    }
//                    break;
//                default:
//                    LOGGER.warn("Unrecognised Setting Type: {}",eachSetting.getSettingType());
//            }
//
//            getProjectDetails.add(fieldDetails);
//        }
//
//        return getProjectDetails;
//    }

    public List<GetProjectDetails> getProjectDetails(Integer projectId){

        ProjectMgmt projectDetailsById = projectMgmtRepo.getProjectDetailsById(projectId);
        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
//        String timeZone = "Asia/Kolkata";
//        String dateFormat = "yyyy-MM-dd";

        if (Objects.isNull(projectDetailsById)) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Project Not Found");
        }

        List<GetProjectDetailsById> projectDetailsValue = projectMgmtFieldMapRepo.getProjectDetailsById(projectId);

//        if (Objects.isNull(projectDetailsValue)) {
//            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Project Not Found");
//        }

        List<GetProjectDetails> getProjectDetails = new ArrayList<>();

        String projectEndDate = null;
        boolean isDaysLeftPresent = false;
        Integer daysLeftFieldId = null;

        for(GetProjectDetailsById eachField : projectDetailsValue){

            GetProjectDetails fieldDetails = new GetProjectDetails();

            if(eachField.getFieldType().contains(ProjectUtils.FIELD_TYPE_PICKLIST)){
                if(eachField.getFieldValue()!=null && !eachField.getFieldValue().isEmpty()){
                    fieldDetails.setPicklistId(eachField.getFieldValue());
                    switch (eachField.getSettingType()){
                        case ProjectUtils.HIDE_PICKLIST_TYPE:
                            /*

                             */
                        case ProjectUtils.FIELD_TYPE:
                            switch (eachField.getFieldName()){
                                case ProjectUtils.FIELD_PROJECT_STATUS:
                                    fieldDetails.setFieldValue(projectMgmtStatusRepo.getStatusById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
                                case ProjectUtils.FIELD_PROJECT_PRIORITY:
                                    fieldDetails.setFieldValue(projectMgmtPriorityRepo.getPriorityById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
                                case ProjectUtils.FIELD_PROJECT_PROJECT_TYPE:
                                    fieldDetails.setFieldValue(projectMgmtTypeRepo.getTypeById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
                                case ProjectUtils.FIELD_PROJECT_PROJECT_OWNER:
                                    fieldDetails.setFieldValue(resourceMgmtRepo.getResourceNameById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
                                case ProjectUtils.FIELD_PROJECT_DEPARTMENT:
                                    fieldDetails.setFieldValue(resourceMgmtDepartmentRepo.getDepartmentById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
//                                case "Created By":
//                                    fieldDetails.setFieldValue(resourceMgmtRepo.getResourceNameById(projectDetailsById.getCreatedBy()));
//                                    break;
//                                case "Created Date":
//                                    fieldDetails.setFieldValue(String.valueOf(projectDetailsById.getCreatedDate()));
//                                    break;
//                                case "Modified By":
//                                    fieldDetails.setFieldValue(resourceMgmtRepo.getResourceNameById(projectDetailsById.getModifiedBy()));
//                                    break;
//                                case "Modified Date":
//                                    fieldDetails.setFieldValue(String.valueOf(projectDetailsById.getModifiedDate()));
//                                    break;
                                default:
                                    LOGGER.warn("Unrecognised field_name: {}", eachField.getFieldName());
                            }
                            break;
                        case ProjectUtils.CUSTOM_TYPE:
                            String[] picklistIdList = eachField.getFieldValue().split(",");

                            List<Integer> picklistIdValue = new ArrayList<>();

                            for(String id: picklistIdList){
                                picklistIdValue.add(Integer.valueOf(id));
                            }
                            fieldDetails.setFieldValue(projectCustomPicklistRepo.getProjectCustomPicklistByPicklistId(picklistIdValue));
                            break;
                        default:
                            LOGGER.warn("Unrecognised field_id: {}", eachField.getFieldId());
                    }
                }
                else{
                    fieldDetails.setPicklistId("");
                    fieldDetails.setFieldValue("");
                }

            }
            else{
                if(eachField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_CREATED_BY)){
                    fieldDetails.setFieldValue(projectDetailsById.getCreatedBy());
                }
                else if(eachField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_CREATED_DATE)){
//                    String createdDate = ProjectUtils.convertDateToAdminTimeZoneStringDate(projectDetailsById.getCreatedDate(), timeZone);
//                    fieldDetails.setFieldValue(createdDate);
                    fieldDetails.setFieldValue(String.valueOf(projectDetailsById.getCreatedDate()));
                }
                else if(eachField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_MODIFIED_BY)){
                    fieldDetails.setFieldValue(projectDetailsById.getModifiedBy());
                }
                else if(eachField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_MODIFIED_DATE)){
//                    String modifiedDate = ProjectUtils.convertDateToAdminTimeZoneStringDate(projectDetailsById.getModifiedDate(), timeZone);
//                    fieldDetails.setFieldValue(modifiedDate);
                    fieldDetails.setFieldValue(String.valueOf(projectDetailsById.getModifiedDate()));
                }
                else if(eachField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_DAYS_LEFT)){
                    isDaysLeftPresent = true;
                    daysLeftFieldId = eachField.getFieldId();
                }
//                else if(eachField.getFieldType().equals(ProjectUtils.FIELD_TYPE_DATE)){
////                    String stringDate = ProjectUtils.convertStringDateToAdminTimeZoneStringDate(eachField.getFieldValue(), timeZone);
////                    fieldDetails.setFieldValue(stringDate);
//                    fieldDetails.setFieldValue(eachField.getFieldValue());
//                }
                else if(eachField.getFieldValue()!=null){
                    if(eachField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_END_DATE)){
                        projectEndDate = eachField.getFieldValue();
                    }
                    fieldDetails.setFieldValue(eachField.getFieldValue());
                }
                else{
                    fieldDetails.setFieldValue("");
                }

            }

            if(!eachField.getFieldName().equals(ProjectUtils.FIELD_PROJECT_DAYS_LEFT)) {
                fieldDetails.setFieldId(eachField.getFieldId());
                fieldDetails.setFieldName(eachField.getFieldName());
                fieldDetails.setFieldType(eachField.getFieldType());
                getProjectDetails.add(fieldDetails);
            }
        }

        if(projectEndDate!=null && isDaysLeftPresent){
            GetProjectDetails fieldDetails = new GetProjectDetails();
            fieldDetails.setFieldId(daysLeftFieldId);
            fieldDetails.setFieldName(ProjectUtils.FIELD_PROJECT_DAYS_LEFT);
            fieldDetails.setFieldType("Number");
            LocalDate currentDate, endDate;
            currentDate = LocalDate.now();
//            new edition
            try{
                endDate = LocalDate.parse(projectEndDate);
            }
            catch(Exception e) {
                Instant instant = Instant.parse(projectEndDate);
                endDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            }
            Long daysLeft = ChronoUnit.DAYS.between(currentDate, endDate) + 1;
            daysLeft = daysLeft>0?daysLeft:0;
            fieldDetails.setFieldValue(String.valueOf(daysLeft));
            fieldDetails.setPicklistId("");
            getProjectDetails.add(fieldDetails);
        }

        return getProjectDetails;
    }

//
//    public void updateProjectDetails(Integer projectId, List<PostProjectDetails> updateDetails){
//
////        declare two arrays to separate pre-defined and custom field data, since both have different tables,
////        and we need modified by and modified date values from pre-defined fields for custom fields
//        List<PostProjectDetails> preDefinedFields = new ArrayList<>();
//        List<PostProjectDetails> customFields = new ArrayList<>();
//
//
////        get the stored project details using projectId
//        ProjectMgmt projectDetailsUpdate = projectMgmtRepo.getProjectDetailsByProjectId(projectId);
//        if (Objects.isNull(projectDetailsUpdate)) {
//            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Project Not Found");
//        }
//
////        array to store updated custom field values
//        List<ProjectMgmtFieldValue> customFieldsUpdate = new ArrayList<>();
//
////        get the stored project custom details using projectId
//        List<ProjectMgmtFieldValue> projectCustomDetails = projectMgmtFieldValueRepo.getProjectCustomDetailsById(projectId);
//
////        convert the stored project custom field values to a map for quick access
//        Map<Integer, ProjectMgmtFieldValue> projectCustomFieldValueMap = new HashMap<>();
//        for(ProjectMgmtFieldValue eachValue : projectCustomDetails){
//            projectCustomFieldValueMap.put(eachValue.getFieldId(), eachValue);
//        }
//
//
////        get all the settings which are enabled and projDetailsView is true,
////        means it is visible for project details component
//        List<ProjectMgmtFieldMap> projectDetailsSetting = projectMgmtFieldMapRepo.getProjectDetailsSettings();
//
////        convert the project settings to a map for quick access
//        Map<Integer, ProjectMgmtFieldMap> projectDetailsSettingMap = new HashMap<>();
//        for(ProjectMgmtFieldMap eachSetting : projectDetailsSetting){
//            projectDetailsSettingMap.put(eachSetting.getFieldId(), eachSetting);
//        }
//
////        Step 1: Separate pre-defined and custom fields data inside incoming data
////        System.out.println("Step 1");
//        for(PostProjectDetails eachField : updateDetails){
//            if(projectDetailsSettingMap.get(eachField.getFieldId()).getSettingType()==3){
//                customFields.add(eachField);
//            }
//            else{
//                preDefinedFields.add(eachField);
//            }
//        }
//
////        Step 2: Traverse pre-defined project data from updateDetails and update the current Project data
////        System.out.println("Step 2");
//        for(PostProjectDetails eachFieldValue : preDefinedFields){
//
//            Date date = new Date();
//
//            switch(eachFieldValue.getFieldName()){
//                case "Project Name":
//                    projectDetailsUpdate.setProjectName(String.valueOf(eachFieldValue.getFieldValue()));
//                    break;
//                case "Project Owner":
//                    projectDetailsUpdate.setOwner(Integer.valueOf(eachFieldValue.getPicklistId()));
//                    break;
//                case "Status":
//                    projectDetailsUpdate.setStatusId(Integer.valueOf(eachFieldValue.getPicklistId()));
//                    break;
//                case "Progress":
//                    projectDetailsUpdate.setProgress(String.valueOf(eachFieldValue.getFieldValue()));
//                    break;
//                case "Budget":
////                    System.out.println(eachFieldValue.getFieldName());
//                    projectDetailsUpdate.setBudget( new BigInteger(String.valueOf(eachFieldValue.getFieldValue())));
//                    break;
//                case "Start Date":
//                    try{
//                        date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(eachFieldValue.getFieldValue()));
//                    }
//                    catch (java.text.ParseException e) {
//                        LOGGER.warn("Parse Exception");
////                                e.printStackTrace();
//                    }
//
//                    projectDetailsUpdate.setStartDate(date);
//                    break;
//                case "End Date":
//                    try{
//                        date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(eachFieldValue.getFieldValue()));
//                    }
//                    catch (java.text.ParseException e) {
//                        LOGGER.warn("Parse Exception");
////                                e.printStackTrace();
//                    }
//
//                    projectDetailsUpdate.setEndDate(date);
//                    break;
//                case "Actual Start Date":
//                    try{
//                        date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(eachFieldValue.getFieldValue()));
//                    }
//                    catch (java.text.ParseException e) {
//                        LOGGER.warn("Parse Exception");
////                                e.printStackTrace();
//                    }
//
//                    projectDetailsUpdate.setActualStartDate(date);
//                    break;
//                case "Actual End Date":
//                    try{
//                        date = new SimpleDateFormat("yyyy-MM-dd").parse((String)eachFieldValue.getFieldValue());
//                    }
//                    catch (java.text.ParseException e) {
//                        LOGGER.warn("Parse Exception");
////                                e.printStackTrace();
//                    }
//
//                    projectDetailsUpdate.setActualEndDate(date);
//                    break;
//                case "Modified By":
//                    projectDetailsUpdate.setModifiedBy(Integer.valueOf(eachFieldValue.getPicklistId()));
//                    break;
//                case "Modified Date":
////                    System.out.println(eachFieldValue.getFieldName());
////                    System.out.println(eachFieldValue.getFieldValue());
////                    System.out.println(eachFieldValue.getFieldValue().getClass().getSimpleName());
//                    try{
//                        date = new SimpleDateFormat("yyyy-MM-dd").parse((String)eachFieldValue.getFieldValue());
//                    }
//                    catch (java.text.ParseException e) {
//                        LOGGER.warn("Parse Exception");
////                                e.printStackTrace();
//                    }
//
//                    projectDetailsUpdate.setModifiedDate(date);
//                    break;
//                case "Project Type":
//                    projectDetailsUpdate.setTypeId(Integer.valueOf(eachFieldValue.getPicklistId()));
//                    break;
//                case "Priority":
//                    projectDetailsUpdate.setPriorityId(Integer.valueOf(eachFieldValue.getPicklistId()));
//                    break;
//                case "Description":
//                    projectDetailsUpdate.setDescription(String.valueOf(eachFieldValue.getFieldValue()));
//                    break;
//                default:
//
//            }
//        }
//
////        Step 3: Traverse the project custom field data from updateDetails and update the current data of table
////        System.out.println("Step 3");
//        for(PostProjectDetails eachFieldValue : customFields){
////            if the data for that custom field exists, update that
//            if(projectCustomFieldValueMap.containsKey(eachFieldValue.getFieldId())){
//                ProjectMgmtFieldValue currentValue = projectCustomFieldValueMap.get(eachFieldValue.getFieldId());
//                if(eachFieldValue.getFieldType().contains("Picklist")){
//                    currentValue.setFieldValue(eachFieldValue.getPicklistId());
//                }
//                else{
//                    currentValue.setFieldValue(String.valueOf(eachFieldValue.getFieldValue()));
//                }
//                currentValue.setModifiedDate(projectDetailsUpdate.getModifiedDate());
//                currentValue.setModifiedBy(projectDetailsUpdate.getModifiedBy());
//                customFieldsUpdate.add(currentValue);
//            }
////            if the data for that custom field does not exist, create a new instance with the new data
//            else{
//                ProjectMgmtFieldValue newValue = new ProjectMgmtFieldValue();
//                newValue.setProjectId(projectId);
//                newValue.setFieldId(eachFieldValue.getFieldId());
////                System.out.println("id-----> " + eachFieldValue.getFieldId());
//                if(eachFieldValue.getFieldType().contains("Picklist")){
////                    System.out.println("Data-----> " + eachFieldValue.getPicklistId());
//                    newValue.setFieldValue(eachFieldValue.getPicklistId());
//                }
//                else{
////                    System.out.println("Data-----> " + eachFieldValue.getFieldValue());
//                    newValue.setFieldValue(String.valueOf(eachFieldValue.getFieldValue()));
//                }
//                newValue.setCreatedDate(projectDetailsUpdate.getModifiedDate());
//                newValue.setCreatedBy(projectDetailsUpdate.getModifiedBy());
//                newValue.setModifiedDate(projectDetailsUpdate.getModifiedDate());
//                newValue.setModifiedBy(projectDetailsUpdate.getModifiedBy());
//                customFieldsUpdate.add(newValue);
//            }
//        }
//
////        update the project pre-defined field details and the custom field details
//        projectMgmtRepo.save(projectDetailsUpdate);
//        projectMgmtFieldValueRepo.saveAll(customFieldsUpdate);
//    }

    public Object updateProjectDetails(Integer projectId, List<PostProjectDetails> updateDetails, Boolean forceDeallocateResources, String modifiedBy){


//        get the stored project details using projectId
        ProjectMgmt projectDetailsUpdate = projectMgmtRepo.getProjectDetailsById(projectId);

        if (Objects.isNull(projectDetailsUpdate)) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Project Not Found");
        }

        projectDetailsUpdate.setCreatedBy(projectDetailsUpdate.getCreatedBy());
        projectDetailsUpdate.setCreatedDate(projectDetailsUpdate.getCreatedDate());
        projectDetailsUpdate.setModifiedBy(modifiedBy);
//        projectDetailsUpdate.setModifiedDate(ProjectUtils.convertDateToUTCTimeZoneDate(new Date()));
        projectDetailsUpdate.setModifiedDate(new Date());

//        project custom picklist options
        List<ProjectCustomPicklist> projectCustomPicklist =projectCustomPicklistRepo.findAll();
        Map<Integer, ProjectCustomPicklist> projectCustomPicklistToMap
                = projectCustomPicklist.stream().collect(Collectors.toMap(ProjectCustomPicklist::getId, pcp -> pcp));

//        project field map admin settings
        List<ProjectMgmtFieldMap> projectMgmtFieldMap =projectMgmtFieldMapRepo.findAll();
        Map<Integer, ProjectMgmtFieldMap> projectMgmtFieldMapToMap
                = projectMgmtFieldMap.stream().collect(Collectors.toMap(ProjectMgmtFieldMap::getFieldId, pmf -> pmf));


//        array to store updated field values
        List<ProjectMgmtFieldValue> fieldValuesUpdate = new ArrayList<>();

//        get the stored project custom details using projectId
        List<ProjectMgmtFieldValue> projectDetailStored = projectMgmtFieldValueRepo.getProjectDetailsForUpdateById(projectId);

//        convert the stored project custom field values to a map for quick access
        Map<Integer, ProjectMgmtFieldValue> projectCustomFieldValueMap = new HashMap<>();
        for(ProjectMgmtFieldValue eachValue : projectDetailStored){
            projectCustomFieldValueMap.put(eachValue.getFieldId(), eachValue);
        }

        String responseProjectStartDate = null, responseProjectEndDate = null, responseProjectActualStartDate = null, responseProjectActualEndDate = null;

        for(PostProjectDetails eachFieldValue : updateDetails){
//            check if fields are editable or not
            if(!ProjectUtils.UNEDITABLE_FIELDS.containsKey(eachFieldValue.getFieldName())){
//            if the data for that custom field exists, update that
                if (projectCustomFieldValueMap.containsKey(eachFieldValue.getFieldId())) {

                    ProjectMgmtFieldValue valueToUpdate = projectCustomFieldValueMap.get(eachFieldValue.getFieldId());

                    if(eachFieldValue.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_START_DATE)){
                        responseProjectStartDate = String.valueOf(eachFieldValue.getFieldValue());
                    }
                    if(eachFieldValue.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_END_DATE)){
                        responseProjectEndDate = String.valueOf(eachFieldValue.getFieldValue());
                    }
                    if(eachFieldValue.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_ACTUAL_START_DATE)){
                        responseProjectActualStartDate = String.valueOf(eachFieldValue.getFieldValue());
                    }
                    if(eachFieldValue.getFieldName().equals(ProjectUtils.FIELD_PROJECT_PROJECT_ACTUAL_END_DATE)){
                        responseProjectActualEndDate = String.valueOf(eachFieldValue.getFieldValue());
                    }

//                    validations start from here

                    if (eachFieldValue.getFieldType().contains(ProjectUtils.FIELD_TYPE_PICKLIST)) {
//                        Apply check if these picklist ids are valid or not for the given picklist
                        try {
                            if(eachFieldValue.getPicklistId()!=null && !eachFieldValue.getPicklistId().isEmpty()) {
                                Set<Integer> allIds = new HashSet<>();
                                String[] ids = eachFieldValue.getPicklistId().split(",");
                                for (String eachId : ids) {
                                    allIds.add(Integer.valueOf(eachId));
                                }
                                Object returnData = checkForValidPicklistIds(
                                        projectMgmtFieldMapToMap.get(eachFieldValue.getFieldId()),
                                        new ArrayList<>(allIds),
                                        projectCustomPicklistToMap);

                                if(returnData instanceof Error){
                                    return returnData;
                                }

                                eachFieldValue.setPicklistId(String.valueOf(returnData));
                            }

                            valueToUpdate.setFieldValue(eachFieldValue.getPicklistId());
                        }
                        catch(Exception e){
                            LOGGER.warn("Picklist error while updating field id: " + eachFieldValue.getFieldId() + ", field name: " + eachFieldValue.getFieldName());
                        }
                    }
                    else {
//                        check if data sent is of valid format or not
                        try {
                            if (eachFieldValue.getFieldValue() != null) {
//                                Apply checks on data being stored based on the field type taken from settings

                                boolean isDataCorrect = true;

                                try {
                                    String fieldValue = String.valueOf(eachFieldValue.getFieldValue());
                                    if (!fieldValue.isEmpty()){
                                        switch (eachFieldValue.getFieldType()) {
                                            case ProjectUtils.FIELD_TYPE_DATE:
                                                LocalDate localDate = LocalDate.parse(fieldValue);
                                                Date date = java.sql.Date.valueOf(localDate);
                                                eachFieldValue.setFieldValue(String.valueOf(date));
                                                break;
                                            case ProjectUtils.FIELD_TYPE_TEXT:
                                                if(eachFieldValue.getFieldName().equals(ProjectUtils.FIELD_PROJECT_NAME)) {
                                                    String projectName= ProjectUtils.removeAllExtraSpace(String.valueOf(eachFieldValue.getFieldValue()));
                                                    List<AddProject> projectNameFromDb= projectMgmtRepo.getProjectList();
                                                    for(AddProject projects:projectNameFromDb){
                                                        if(projectName.equalsIgnoreCase(projects.getProjectName())){
                                                            if(!projects.getProjectId().equals(projectId)) {
                                                                String message = ProjectUtils.PROJECT_PROJECT_NAME_DUPLICATE;
                                                                Error error = new Error();
                                                                error.setRequestAt(new Date());
                                                                error.setMessage(message);
                                                                error.setStatus(HttpStatus.BAD_REQUEST.value());
                                                                return error;
                                                            }
                                                        }
                                                    }
                                                    eachFieldValue.setFieldValue(projectName);
                                                }
                                                break;
                                            case ProjectUtils.FIELD_TYPE_NUMBER:
//                                                TODO:: can only proceed if we know what type of input to expect

                                                break;
                                            case ProjectUtils.FIELD_TYPE_CURRENCY:
//                                                TODO:: ask for currency checks, like 2 decimal, negative currency, maximum limit, etc.

                                                break;
                                            case ProjectUtils.FIELD_TYPE_EMAIL:
                                                String email = fieldValue;
                                                String basicEmailFormat = "^([A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

                                                Pattern emailPattern = Pattern.compile(basicEmailFormat);
                                                boolean isEmailPatternCorrect =emailPattern.matcher(email).matches();
                                                if(!isEmailPatternCorrect){
                                                    LOGGER.warn(ProjectUtils.INCORRECT_EMAIL_FORMAT);
                                                    isDataCorrect = false;
                                                }
                                                break;
                                        }
                                    }
                                }
                                catch(Exception e){
                                    LOGGER.warn("Picklist error while updating field id: " + eachFieldValue.getFieldId() + ", field name: " + eachFieldValue.getFieldName());
                                    isDataCorrect = false;
                                }

                                if(isDataCorrect) {
                                    valueToUpdate.setFieldValue(String.valueOf(eachFieldValue.getFieldValue()));
                                }
                            } else {
                                valueToUpdate.setFieldValue("");
                            }
                        }
                        catch(Exception e){
                            LOGGER.warn("Picklist error while updating field id: " + eachFieldValue.getFieldId() + ", field name: " + eachFieldValue.getFieldName());
                        }
                    }
//                valueToUpdate.setModifiedDate(projectDetailsUpdate.getModifiedDate());
//                valueToUpdate.setModifiedBy(projectDetailsUpdate.getModifiedBy());
                    valueToUpdate.setCreatedBy(valueToUpdate.getCreatedBy());
                    valueToUpdate.setCreatedDate(valueToUpdate.getCreatedDate());
                    valueToUpdate.setModifiedBy(modifiedBy);
//                    valueToUpdate.setModifiedDate(ProjectUtils.convertDateToUTCTimeZoneDate(new Date()));
                    valueToUpdate.setModifiedDate(new Date());
                    fieldValuesUpdate.add(valueToUpdate);
                }
//            if the data for that custom field does not exist, create a new instance with the new data
                else {
                    ProjectMgmtFieldValue newValue = new ProjectMgmtFieldValue();
                    newValue.setProjectId(projectId);
                    newValue.setFieldId(eachFieldValue.getFieldId());
                    newValue.setCreatedBy(modifiedBy);
//                    newValue.setCreatedDate(ProjectUtils.convertDateToUTCTimeZoneDate(new Date()));
                    newValue.setCreatedDate(new Date());
                    newValue.setModifiedBy(modifiedBy);
//                    newValue.setModifiedDate(ProjectUtils.convertDateToUTCTimeZoneDate(new Date()));
                    newValue.setModifiedDate(new Date());
//                System.out.println("id-----> " + eachFieldValue.getFieldId());
                    if (eachFieldValue.getFieldType().contains(ProjectUtils.FIELD_TYPE_PICKLIST)) {
//                        Apply check if these picklist ids are valid or not for the given picklist
                        try {
                            if(eachFieldValue.getPicklistId()!=null && !eachFieldValue.getPicklistId().isEmpty()) {
                                Set<Integer> allIds = new HashSet<>();
                                String[] ids = eachFieldValue.getPicklistId().split(",");
                                for (String eachId : ids) {
                                    allIds.add(Integer.valueOf(eachId));
                                }

                                Object returnData = checkForValidPicklistIds(
                                        projectMgmtFieldMapToMap.get(eachFieldValue.getFieldId()),
                                        new ArrayList<>(allIds),
                                        projectCustomPicklistToMap);

                                if(returnData instanceof Error){
                                    return returnData;
                                }

                                eachFieldValue.setPicklistId(String.valueOf(returnData));
                            }

                            newValue.setFieldValue(eachFieldValue.getPicklistId());
                        }
                        catch(Exception e){
                            LOGGER.warn("Picklist error while updating field id: " + eachFieldValue.getFieldId() + ", field name: " + eachFieldValue.getFieldName());
                        }
                    }
                    else {
                        try {
//                            Apply checks on data being stored based on the field type taken from settings

                            boolean isDataCorrect = true;

                            try {
                                String fieldValue = String.valueOf(eachFieldValue.getFieldValue());
                                if (!fieldValue.isEmpty()){
                                    switch (eachFieldValue.getFieldType()) {
                                        case ProjectUtils.FIELD_TYPE_DATE:
                                            LocalDate localDate = LocalDate.parse(fieldValue);
                                            Date date = java.sql.Date.valueOf(localDate);
                                            eachFieldValue.setFieldValue(String.valueOf(date));
                                            break;
                                        case ProjectUtils.FIELD_TYPE_NUMBER:
//                                                TODO:: can only proceed if we know what type of input to expect

                                            break;
                                        case ProjectUtils.FIELD_TYPE_CURRENCY:
//                                                TODO:: ask for currency checks, like 2 decimal, negative currency, maximum limit, etc.

                                            break;
                                        case ProjectUtils.FIELD_TYPE_EMAIL:
                                            String email = fieldValue;
                                            String basicEmailFormat = "^([A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

                                            Pattern emailPattern = Pattern.compile(basicEmailFormat);
                                            boolean isEmailPatternCorrect =emailPattern.matcher(email).matches();
                                            if(!isEmailPatternCorrect){
                                                LOGGER.warn(ProjectUtils.INCORRECT_EMAIL_FORMAT);
                                                isDataCorrect = false;
                                            }
                                            break;
                                    }
                                }
                            }
                            catch(Exception e){
                                LOGGER.warn("Picklist error while updating field id: " + eachFieldValue.getFieldId() + ", field name: " + eachFieldValue.getFieldName());
                                isDataCorrect = false;
                            }

                            if(isDataCorrect) {
                                newValue.setFieldValue(String.valueOf(eachFieldValue.getFieldValue()));
                            }
                            else{
                                newValue.setFieldValue("");
                            }

                        }
                        catch(Exception e){
                            LOGGER.warn("Picklist error while updating field id: " + eachFieldValue.getFieldId() + ", field name: " + eachFieldValue.getFieldName());
                        }
                    }

                    fieldValuesUpdate.add(newValue);
                }
            }
        }

        Date startDate = null, endDate = null, actualStartDate = null, actualEndDate = null;
        if(responseProjectStartDate!=null) {
            try {
                LocalDate localStartDate = LocalDate.parse(responseProjectStartDate);
                startDate = java.sql.Date.valueOf(localStartDate);
            } catch (Exception e) {
                Instant instant = Instant.parse(responseProjectStartDate);
                LocalDate localStartDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                startDate = java.sql.Date.valueOf(localStartDate);
            }
        }
        if(responseProjectEndDate!=null) {
            try {
                LocalDate localEndDate = LocalDate.parse(responseProjectEndDate);
                endDate = java.sql.Date.valueOf(localEndDate);
            } catch (Exception e) {
                Instant instant = Instant.parse(responseProjectEndDate);
                LocalDate localEndDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                endDate = java.sql.Date.valueOf(localEndDate);
            }
        }
        if(responseProjectActualStartDate!=null) {
            try {
                LocalDate localStartDate = LocalDate.parse(responseProjectActualStartDate);
                actualStartDate = java.sql.Date.valueOf(localStartDate);
            } catch (Exception e) {
                Instant instant = Instant.parse(responseProjectActualStartDate);
                LocalDate localStartDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                actualStartDate = java.sql.Date.valueOf(localStartDate);
            }
        }
        if(responseProjectActualEndDate!=null) {
            try {
                LocalDate localEndDate = LocalDate.parse(responseProjectActualEndDate);
                actualEndDate = java.sql.Date.valueOf(localEndDate);
            } catch (Exception e) {
                Instant instant = Instant.parse(responseProjectActualEndDate);
                LocalDate localEndDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                actualEndDate = java.sql.Date.valueOf(localEndDate);
            }
        }

        if(startDate!=null && endDate!=null){
            if(startDate.compareTo(endDate)>0){
                String message = ProjectUtils.PROJECT_START_DATE_AFTER_END_DATE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }
        else{
            String message = ProjectUtils.PROJECT_START_END_DATE_EMPTY;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }
        if(actualStartDate!=null && actualEndDate!=null){
            if(actualStartDate.compareTo(actualEndDate)>0){
                String message = ProjectUtils.PROJECT_ACTUAL_START_DATE_AFTER_ACTUAL_END_DATE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }
//        else if((actualStartDate!=null && actualEndDate==null) || (actualStartDate==null && actualEndDate!=null)){
//
//        }

        if(actualStartDate!=null && (startDate.compareTo(actualStartDate)>0)) {
            startDate = actualStartDate;
        }
        if(actualEndDate!=null && (endDate.compareTo(actualEndDate)<0)) {
            endDate = actualEndDate;
        }
        List<String> resourceNames = projectResourceMappingRepo.getResourceListOutsideProjectDateRange(projectId, startDate, endDate);
        if(resourceNames!=null && !resourceNames.isEmpty()){
            if(forceDeallocateResources!=null && forceDeallocateResources){
                List<PostProjectResourceAllocationData> allocationDataToUpdate = new ArrayList<>();

                List<GetProjectResourceAllocationData> getKanbanData = getKanbanForUpdateProject(projectId);
                if(getKanbanData!=null && !getKanbanData.isEmpty()) {
                    for (GetProjectResourceAllocationData eachKanbanData : getKanbanData) {
                        eachKanbanData.setAllocationEndDate(endDate);
                        eachKanbanData.setAllocationStartDate(startDate);

                        allocationDataToUpdate.add(new PostProjectResourceAllocationData(eachKanbanData));
                    }
                }

                if(!allocationDataToUpdate.isEmpty()){
                    Object response = projectDetailsUpdateKanbanData(allocationDataToUpdate, modifiedBy);
                    if(response instanceof Error){
                        return response;
                    }
                }
            }
            else {
                String message = ProjectUtils.outsideProjectDateRangeAllocation(resourceNames);
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }
        else{
//            check if date range changed? if yes, change allocation date range for all resources
            String projectAnalysisQuery = ProjectUtils.getFile("getProjectStartEndDates.txt");
            Query query = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
            ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ProjectStartEndDate.class));
            List<Integer> projectIds = new ArrayList<>();
            projectIds.add(projectId);
            query.setParameter("projectIds", projectIds);

            List<ProjectStartEndDate> projectStartEndDateList = query.getResultList();
            ProjectStartEndDate currentProject = projectStartEndDateList.get(0);
            if(currentProject.getProject_start_date().compareTo(startDate)!=0 || currentProject.getProject_end_date().compareTo(endDate)!=0){
                List<PostProjectResourceAllocationData> allocationDataToUpdate = new ArrayList<>();

                List<GetProjectResourceAllocationData> getKanbanData = getKanbanForUpdateProject(projectId);
                if(getKanbanData!=null && !getKanbanData.isEmpty()) {
                    for (GetProjectResourceAllocationData eachKanbanData : getKanbanData) {
                        if(eachKanbanData.getResourceId()!=null) {
                            eachKanbanData.setAllocationEndDate(endDate);
                            eachKanbanData.setAllocationStartDate(startDate);
                        }
                        else{
                            if(eachKanbanData.getAllocationEndDate().compareTo(startDate)<0 || eachKanbanData.getAllocationStartDate().compareTo(endDate)>0){
                                String message = "One or more unnamed requests are completely outside the project's new date range. Please delete them before changing the project dates";
                                Error error = new Error();
                                error.setRequestAt(new Date());
                                error.setMessage(message);
                                error.setStatus(HttpStatus.BAD_REQUEST.value());
                                return error;
                            }
                            else{
                                if(eachKanbanData.getAllocationStartDate().compareTo(startDate)<0){
                                    eachKanbanData.setAllocationStartDate(startDate);
                                }
                                if(eachKanbanData.getAllocationEndDate().compareTo(endDate)>0){
                                    eachKanbanData.setAllocationEndDate(endDate);
                                }
                            }
                        }
                        allocationDataToUpdate.add(new PostProjectResourceAllocationData(eachKanbanData));
                    }
                }

                if(!allocationDataToUpdate.isEmpty()){
                    Object response = projectDetailsUpdateKanbanData(allocationDataToUpdate, modifiedBy);
                    if(response instanceof Error){
                        return response;
                    }
                }
            }
        }

//        update the project pre-defined field details and the custom field details
        projectMgmtRepo.save(projectDetailsUpdate);
        projectMgmtFieldValueRepo.saveAll(fieldValuesUpdate);
        LOGGER.info("User " + modifiedBy + " UpdateResourseMgmtSettings skills at " + new Date());
        return true;
    }

    private List<GetProjectResourceAllocationData> getKanbanForUpdateProject(Integer projectId) {

//        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
//        int startDateOfMonth = 1;
//
//        Month month = Month.valueOf(startMonth.toUpperCase());
//        LocalDate customStartDate = LocalDate.of(financialYear, month, startDateOfMonth);
//        LocalDate customEndDate = customStartDate.plusYears(1).minusDays(1);

        String getKanbanDataNamedQuery = ProjectUtils.getFile("getKanbanDataQuery.txt");

        Query query1 = entityManager.createNativeQuery(getKanbanDataNamedQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(GetKanbanData.class));

        query1.setParameter("projectId", projectId);

        List<GetKanbanData> getKanbanDataNamedList = query1.getResultList();

        String getKanbanDataUnnamedQuery = ProjectUtils.getFile("getKanbanDataQueryUnnamedResources.txt");

        Query query2 = entityManager.createNativeQuery(getKanbanDataUnnamedQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query2).setResultTransformer(new AliasToBeanResultTransformer(GetKanbanData.class));

        query2.setParameter("projectId", projectId);

        List<GetKanbanData> getKanbanDataUnnamedList = query2.getResultList();

        List<GetKanbanData> getKanbanDataList = new ArrayList<>();
        if(getKanbanDataNamedList!=null && !getKanbanDataNamedList.isEmpty()){
            getKanbanDataList.addAll(getKanbanDataNamedList);
        }
        if(getKanbanDataUnnamedList!=null && !getKanbanDataUnnamedList.isEmpty()){
            getKanbanDataList.addAll(getKanbanDataUnnamedList);
        }

        List<GetProjectResourceAllocationData> getProjectResourceAllocationDataList = null;
        if(getKanbanDataList != null && !getKanbanDataList.isEmpty()){

//            List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
//            GeneralSettings generalSettings = generalSettingsList.get(0);
//            String timeZone = "Asia/Kolkata";

//            List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
//            Double maxFteAllowedByAdmin = 0d;
//
//            if(resourceMgmtStatusList!=null && !resourceMgmtStatusList.isEmpty()) {
////                resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
//                for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
//                    if (eachStatus.getIsEnabled() && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
//                        if (eachStatus.getEndValue() > maxFteAllowedByAdmin) {
//                            maxFteAllowedByAdmin = eachStatus.getEndValue();
//                        }
//                    }
//                }
//            }

//            if(maxFteAllowedByAdmin<=0){
//                maxFteAllowedByAdmin = 1.5d;
//            }

//            bring all the date wise allocation data for all non-deleted allocations
            List<AllResourceActiveAllocation> allResourceActiveData = projectResourceMappingRepo.getAllResourceActiveAllocationAtUpdateProjectDetails(projectId);
//            group date wise allocations by resource id
            Map<Integer, List<AllResourceActiveAllocation>> allResourceActiveDataToMap = null;

//            Set<Integer> listOfResourceIds = new HashSet<>();
//            Date startDate = getKanbanDataList.get(0).getStart_date();
//            Date endDate = getKanbanDataList.get(0).getEnd_date();
//            for(GetKanbanData eachRecord: getKanbanDataList){
//                listOfResourceIds.add(eachRecord.getResource_id());
//                startDate = startDate.compareTo(eachRecord.getStart_date())<0?startDate:eachRecord.getStart_date();
//                endDate = endDate.compareTo(eachRecord.getEnd_date())<0?endDate:eachRecord.getEnd_date();
//            }

//            List<ResourceDailyFteSumData> resourceDailyFteSumDataList = projectResourceMappingRepo.getResourceDailyFteData(new ArrayList<>(listOfResourceIds), startDate, endDate);

//            Map<Integer, Map<LocalDate, Double>> resourceDailyFteSumDataListToMap = new HashMap<>();

//            if(resourceDailyFteSumDataList!=null && !resourceDailyFteSumDataList.isEmpty()) {
//                for (ResourceDailyFteSumData eachResource : resourceDailyFteSumDataList) {
//                    Map<LocalDate, Double> eachResourceDailyData = null;
//                    if(resourceDailyFteSumDataListToMap.containsKey(eachResource.getResourceId())){
//                        eachResourceDailyData = resourceDailyFteSumDataListToMap.get(eachResource.getResourceId());
//                    }
//                    else{
//                        eachResourceDailyData = new HashMap<>();
//                    }
//                    LocalDate localDate = new java.sql.Date(eachResource.getAllocationDate().getTime()).toLocalDate();
//                    eachResourceDailyData.merge(localDate, eachResource.getSumFte().doubleValue(), Double::sum);
//                    resourceDailyFteSumDataListToMap.put(eachResource.getResourceId(), eachResourceDailyData);
//                }
//            }

            if(allResourceActiveData!=null && !allResourceActiveData.isEmpty()) {

                getProjectResourceAllocationDataList = new ArrayList<>();

                allResourceActiveDataToMap = allResourceActiveData.stream().collect(Collectors.groupingBy(AllResourceActiveAllocation::getMapId));

                for (GetKanbanData eachRecord: getKanbanDataList) {

                    LocalDate dateOfJoin = null, lastWorkingDate = null;
                    if(eachRecord.getDate_of_join()!=null) {
                        dateOfJoin = new java.sql.Date(eachRecord.getDate_of_join().getTime()).toLocalDate();
                    }
                    if(eachRecord.getLast_working_date()!=null) {
                        lastWorkingDate = new java.sql.Date(eachRecord.getLast_working_date().getTime()).toLocalDate();
                    }

//                    String startDateMessage = null, endDateMessage = null;
//                    if(eachRecord.getStart_date().compareTo(eachRecord.getDate_of_join())==0){
//                        startDateMessage = ProjectUtils.START_DATE_DATE_OF_JOIN_MESSAGE;
//                    }
//                    else{
//                        startDateMessage = ProjectUtils.START_DATE_PROJECT_START_DATE_MESSAGE;
//                    }
//                    if(eachRecord.getLast_working_date()!=null && eachRecord.getEnd_date().compareTo(eachRecord.getLast_working_date())==0){
//                        endDateMessage = ProjectUtils.END_DATE_LAST_WORKING_DATE_MESSAGE;
//                    }
//                    else{
//                        endDateMessage = ProjectUtils.END_DATE_PROJECT_END_DATE_MESSAGE;
//                    }

                    GetProjectResourceAllocationData eachAllocation = new GetProjectResourceAllocationData(eachRecord, true);
//                    eachAllocation.setMax_fte_possible(maxFteAllowedByAdmin);
//                    eachAllocation.setStart_date_message(startDateMessage);
//                    eachAllocation.setEnd_date_message(endDateMessage);

//                    LocalDate workingDaysStartDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
//                    LocalDate workingDaysEndDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();
//
//                    if(dateOfJoin!=null && workingDaysStartDate.isBefore(dateOfJoin)){
//                        workingDaysStartDate = dateOfJoin;
//                    }
//                    if(lastWorkingDate!=null && lastWorkingDate.isBefore(workingDaysEndDate)){
//                        workingDaysEndDate = lastWorkingDate;
//                    }

//                    Map<Date, AllResourceActiveAllocation> eachRecordAllocationListToMap = new HashMap<>();

////                    resource daily allocations
//                    Map<LocalDate, Double> resourceDailyFte = resourceDailyFteSumDataListToMap.get(eachRecord.getResource_id());
//
                    List<AllResourceActiveAllocation> eachRecordAllocationList = allResourceActiveDataToMap.get(eachRecord.getMap_id());
                    List<AllResourceActiveAllocation> eachRecordAllocationListNew = new ArrayList<>();
                    eachRecordAllocationList.sort(Comparator.comparing(AllResourceActiveAllocation::getAllocationDate));
                    for(AllResourceActiveAllocation eachDailyAllocation: eachRecordAllocationList){
                        LocalDate allocationDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();
                        eachDailyAllocation.setAllocationDate(java.sql.Date.valueOf(allocationDate));

//                        if(resourceDailyFte.containsKey(allocationDate)) {
//                            Double availableFte = ProjectUtils.roundToOneDecimalPlace(maxFteAllowedByAdmin - resourceDailyFte.get(allocationDate));
//                            eachDailyAllocation.setAvailableFte(availableFte);
//                        }
//                        else{
//                            eachDailyAllocation.setAvailableFte(maxFteAllowedByAdmin);
//                        }

                        eachRecordAllocationListNew.add(eachDailyAllocation);

//                        eachRecordAllocationListToMap.put(eachDailyAllocation.getAllocationDate(), eachDailyAllocation);
                    }
                    eachAllocation.setAllocations(eachRecordAllocationListNew);

//                    make changes here
//                    List<AllocationDataByMonth> allocationDataByMonthList = new ArrayList<>();
//                    List<AllocationDataByWeek> allocationDataByWeekList = new ArrayList<>();
//
//                    LocalDate monthEndDate, weekStartDate, weekEndDate;
//                    monthEndDate = workingDaysStartDate.withDayOfMonth(workingDaysStartDate.lengthOfMonth());
//                    weekStartDate = workingDaysStartDate.with(DayOfWeek.MONDAY);
//                    weekEndDate = workingDaysStartDate.with(DayOfWeek.SUNDAY);
//                    Integer workingDaysInTheMonth = 0, workingDaysInTheWeek = 0;
//                    Double weekAllocatedSum = 0d,  weekAvailableSum = 0d,  weekRequestedSum = 0d;
//                    Double monthAllocatedSum = 0d, monthAvailableSum = 0d, monthRequestedSum = 0d;
//                    List<AllResourceActiveAllocation> weeklyAllocationData = new ArrayList<>();
//                    List<AllResourceActiveAllocation> monthlyAllocationData = new ArrayList<>();
//
//                    while(!workingDaysStartDate.isAfter(workingDaysEndDate)){
//
//                        if(workingDaysStartDate.isAfter(weekEndDate)){
////                            make all three parameters zero
//                            weekAllocatedSum  = 0d;
//                            weekAvailableSum = 0d;
//                            weekRequestedSum = 0d;
//
//                            workingDaysInTheWeek = 0;
//                            weeklyAllocationData = new ArrayList<>();
//                            weekStartDate = workingDaysStartDate;
//                            weekEndDate = workingDaysStartDate.with(DayOfWeek.SUNDAY);
//                        }
//
//                        if(workingDaysStartDate.isAfter(monthEndDate)){
////                            make all three parameters zero
//                            monthAllocatedSum = 0d;
//                            monthAvailableSum = 0d;
//                            monthRequestedSum = 0d;
//
//                            workingDaysInTheMonth = 0;
//                            monthlyAllocationData = new ArrayList<>();
//                            monthEndDate = workingDaysStartDate.withDayOfMonth(workingDaysStartDate.lengthOfMonth());
//                        }
//
//                        if(workingDaysStartDate.getDayOfWeek()!=DayOfWeek.SATURDAY && workingDaysStartDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
//                            Date currentDate = java.sql.Date.valueOf(workingDaysStartDate);
//                            if(eachRecordAllocationListToMap.containsKey(currentDate)){
//
//                                AllResourceActiveAllocation currentDateRecord = eachRecordAllocationListToMap.get(currentDate);
//
//                                weekAllocatedSum += currentDateRecord.getAllocatedFte().doubleValue();
//                                weekRequestedSum += currentDateRecord.getRequestedFte().doubleValue();
//                                weekAvailableSum += currentDateRecord.getAvailableFte();
//                                workingDaysInTheWeek++;
//                                weeklyAllocationData.add(currentDateRecord);
//
//                                monthAllocatedSum += currentDateRecord.getAllocatedFte().doubleValue();
//                                monthRequestedSum += currentDateRecord.getRequestedFte().doubleValue();
//                                monthAvailableSum += currentDateRecord.getAvailableFte();
//                                workingDaysInTheMonth++;
//                                monthlyAllocationData.add(currentDateRecord);
//                            }
//                        }
//
//                        if(workingDaysStartDate.isEqual(monthEndDate) || workingDaysStartDate.isEqual(workingDaysEndDate)){
//                            Double monthAllocatedAverage = monthAllocatedSum /workingDaysInTheMonth;
//                            monthAllocatedAverage = ProjectUtils.roundToOneDecimalPlace(monthAllocatedAverage);
//
//                            Double monthAvailableAverage = monthAvailableSum /workingDaysInTheMonth;
//                            monthAvailableAverage = ProjectUtils.roundToOneDecimalPlace(monthAvailableAverage);
//
//                            Double monthRequestedAverage = monthRequestedSum /workingDaysInTheMonth;
//                            monthRequestedAverage = ProjectUtils.roundToOneDecimalPlace(monthRequestedAverage);
//
//                            AllocationDataByMonth currentMonth = new AllocationDataByMonth(monthEndDate.getMonth().toString(), monthEndDate.getYear(), monthAllocatedAverage, monthRequestedAverage, monthAvailableAverage, monthlyAllocationData);
//                            allocationDataByMonthList.add(currentMonth);
//                        }
//
//                        if(workingDaysStartDate.isEqual(weekEndDate) || workingDaysStartDate.isEqual(workingDaysEndDate)){
//                            Double weekAllocatedAverage = weekAllocatedSum/workingDaysInTheWeek;
//                            weekAllocatedAverage = ProjectUtils.roundToOneDecimalPlace(weekAllocatedAverage);
//
//                            Double weekAvailableAverage = weekAvailableSum/workingDaysInTheWeek;
//                            weekAvailableAverage = ProjectUtils.roundToOneDecimalPlace(weekAvailableAverage);
//
//                            Double weekRequestedAverage = weekRequestedSum/workingDaysInTheWeek;
//                            weekRequestedAverage = ProjectUtils.roundToOneDecimalPlace(weekRequestedAverage);
//
//                            Date weekStart = java.sql.Date.valueOf(weekStartDate);
//                            AllocationDataByWeek currentWeek = new AllocationDataByWeek(weekStart, weekStartDate.getYear(), weekAllocatedAverage, weekRequestedAverage, weekAvailableAverage, weeklyAllocationData);
//                            allocationDataByWeekList.add(currentWeek);
//                        }
//
//                        workingDaysStartDate = workingDaysStartDate.plusDays(1);
//                    }
//
//                    eachAllocation.setMonthlyAllocationData(allocationDataByMonthList);
//                    eachAllocation.setWeeklyAllocationData(allocationDataByWeekList);
////                    till here
//
//                    LocalDate resourceWorkingStartDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
//                    LocalDate resourceWorkingEndDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();
//
//                    Integer workingDays = ProjectUtils.numberOfWorkingDays(resourceWorkingStartDate, resourceWorkingEndDate);
//
//                    eachAllocation.setAvgFte(ProjectUtils.roundToOneDecimalPlace(eachRecord.getSum_fte().doubleValue()/workingDays));

                    getProjectResourceAllocationDataList.add(eachAllocation);
                }
            }
        }

//        Map<String, Object> returnData = new HashMap<>();
//
//        if(getProjectResourceAllocationDataList!=null && !getProjectResourceAllocationDataList.isEmpty()) {
//            returnData.put("data", getProjectResourceAllocationDataList);
////            returnData.put("startDate", totalStartDate);
////            returnData.put("endDate", totalEndDate);
//        }
//        else{
//            returnData = null;
//        }

        return getProjectResourceAllocationDataList;
    }

    public Object projectDetailsUpdateKanbanData(List<PostProjectResourceAllocationData> postProjectResourceAllocationDataList, String modifiedBy) {

//        check if response data is empty
        if(postProjectResourceAllocationDataList!=null && !postProjectResourceAllocationDataList.isEmpty()){

            Set<Integer> collectResourceId = new HashSet<>();
//            Set<Integer> collectProjectId = new HashSet<>();

//            collect all the resource id and project id
            for(PostProjectResourceAllocationData eachData: postProjectResourceAllocationDataList){
                if(eachData.getResourceId()!=null) {
                    collectResourceId.add(eachData.getResourceId());
                }
//                collectProjectId.add(eachData.getProjectId());
            }
            List<ResourceMgmt> listOfResourceId = resourceMgmtRepo.findAllById(collectResourceId);
//            List<ProjectMgmt> listOfProjectId = projectMgmtRepo.findAllById(collectProjectId);

            /*String projectAnalysisQuery = ProjectUtils.getFile("getProjectStartEndDates.txt");
            Query query = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
            ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ProjectStartEndDate.class));

            query.setParameter("projectIds", collectProjectId);

            List<ProjectStartEndDate> projectStartEndDateList = query.getResultList();
            ProjectStartEndDate currentProject = null;
            for(ProjectStartEndDate eachProject: projectStartEndDateList){
                if(collectProjectId.contains(eachProject.getProject_id())){
                    currentProject = eachProject;
                    break;
                }
            }*/


            Map<Integer, ResourceMgmt> resourceDetailsMap = new HashMap<>();
            for(ResourceMgmt eachResource: listOfResourceId){
                collectResourceId.add(eachResource.getResourceId());
                resourceDetailsMap.put(eachResource.getResourceId(), eachResource);
            }

            List<ProjectResourceMapping> allocationDataToUpdate = new ArrayList<>();
            List<PostProjectResourceAllocationData> allocationDataToInsert = new ArrayList<>();
            List<RequestedResources> requestedResourcesList = new ArrayList<>();
            List<ProjectResourceMappingExtended> dailyAllocationDataToUpsert = new ArrayList<>();


            for(PostProjectResourceAllocationData eachData: postProjectResourceAllocationDataList){

//                TODO::add checks for data which will be inserted or updated to requested_resource table as well

                if(!ProjectUtils.isEndDateBeforeStartDate(eachData.getAllocationStartDate(), eachData.getAllocationEndDate())){

                    if(!collectResourceId.contains(eachData.getResourceId())){
                        eachData.setResourceId(null);
                    }

//                    remove time data from the response date range
                    LocalDate allocStartDate, allocEndDate;
                    allocStartDate = new java.sql.Date(eachData.getAllocationStartDate().getTime()).toLocalDate();
                    allocEndDate = new java.sql.Date(eachData.getAllocationEndDate().getTime()).toLocalDate();
                    Long initialNumberOfDays = ChronoUnit.DAYS.between(allocStartDate, allocEndDate) +1;

                    if(eachData.getResourceId()!=null){
                        ResourceMgmt resourceMgmt = resourceDetailsMap.get(eachData.getResourceId());
                        if(resourceMgmt.getDateOfJoin()!=null){
                            LocalDate resourceDateOfJoin = new java.sql.Date(resourceMgmt.getDateOfJoin().getTime()).toLocalDate();
                            if(allocStartDate.isBefore(resourceDateOfJoin)){
                                allocStartDate = resourceDateOfJoin;
                            }
                        }
                        if(resourceMgmt.getLastWorkingDate()!=null){
                            LocalDate resourceLastWorkingDate = new java.sql.Date(resourceMgmt.getLastWorkingDate().getTime()).toLocalDate();
                            if(allocEndDate.isAfter(resourceLastWorkingDate)){
                                allocEndDate = resourceLastWorkingDate;
                            }
                        }
                    }
                    Long updatedNumberOfDays = ChronoUnit.DAYS.between(allocStartDate, allocEndDate) +1;
                    eachData.setAllocationStartDate(java.sql.Date.valueOf(allocStartDate));
                    eachData.setAllocationEndDate(java.sql.Date.valueOf(allocEndDate));

//                    make sure data for every date inside allocation date range is present
                    HashSet<Date> dateRecordTracker = new HashSet<>();
                    List<AllResourceActiveAllocation> emptyAllocations = new ArrayList<>();
                    if(eachData.getAllocations()!=null && !eachData.getAllocations().isEmpty()){

//                        remove time data from allocationDate and keep track of all the dates in the list,
//                        to make sure that all the inside date range records are present

                        for(AllResourceActiveAllocation eachAllocation: eachData.getAllocations()){
                            LocalDate localDate = new java.sql.Date(eachAllocation.getAllocationDate().getTime()).toLocalDate();
                            eachAllocation.setAllocationDate(java.sql.Date.valueOf(localDate));

                            dateRecordTracker.add(eachAllocation.getAllocationDate());
                            emptyAllocations.add(eachAllocation);
                        }
                    }

//                    get the start date, whether financial year start or project start, which is later
                    LocalDate allocationStartDate = new java.sql.Date(eachData.getAllocationStartDate().getTime()).toLocalDate();
//                    get the start date, whether financial year start or project start, which is later
                    LocalDate allocationEndDate = new java.sql.Date(eachData.getAllocationEndDate().getTime()).toLocalDate();

                    while(!allocationStartDate.isAfter(allocationEndDate)){
                        if(allocationStartDate.getDayOfWeek()!=DayOfWeek.SATURDAY && allocationStartDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
                            Date currentDate = java.sql.Date.valueOf(allocationStartDate);
                            if(!dateRecordTracker.contains(currentDate)){
                                emptyAllocations.add(new AllResourceActiveAllocation(null, eachData.getMapId(), currentDate, BigDecimal.valueOf(0d), BigDecimal.valueOf(0), null));
                            }
                        }
                        allocationStartDate = allocationStartDate.plusDays(1);
                    }
                    eachData.setAllocations(emptyAllocations);

                    Optional<ProjectResourceMapping> storedAllocation = projectResourceMappingRepo.findById(eachData.getMapId());

//                    if map id does not exist in database, make map id = null and insert it to list
                    if (storedAllocation.isPresent()) {
//                        eachData.setCreatedBy(storedAllocation.get().getCreatedBy());
//                        eachData.setCreatedDate(storedAllocation.get().getCreatedDate());
//                        eachData.setModifiedBy(modifiedBy);
//                        eachData.setModifiedDate(new Date());

//                        collect the daily allocation data from the response
                        List<AllResourceActiveAllocation> thisRecordAllocations = eachData.getAllocations();
//                        boolean isInsideDateRangeRecordNotPresent = true;
//                        for each allocation
                        Double avgRequestedFte = 0d, avgAllocatedFte = 0d;
                        Integer workingDays = ProjectUtils.numberOfWorkingDays(allocStartDate, allocEndDate);
                        for(AllResourceActiveAllocation eachDailyAllocation: thisRecordAllocations){

//                            eachDailyAllocation.setAllocationDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachDailyAllocation.getAllocationDate()));
                            LocalDate currentDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();

//                            check if the date is on weekend, if nop, then proceed
                            if((currentDate.getDayOfWeek()!= DayOfWeek.SATURDAY) && (currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY)) {

//                                check if the daily allocation data date lies in between the allocation start date and allocation end date
                                if ((eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationEndDate()) <= 0)
                                        && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationStartDate()) >= 0)) {

//                                    if mapId is not matching with the record mapId, or if not present, discard the data,
//                                    or if it already has id, discard as well, as no data change is allowed from here
                                    if (Objects.equals(eachDailyAllocation.getMapId(), eachData.getMapId())) {
//                                        if(isInsideDateRangeRecordNotPresent) {
//                                            isInsideDateRangeRecordNotPresent = false;
//                                        }
                                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                                    }
                                } else {
//                                    if no id and still outside allocation date range, ignore
//                                    if is present, but outside allocation date range, make it zero
                                    if (eachDailyAllocation.getId() != null && Objects.equals(eachDailyAllocation.getMapId(), eachData.getMapId())) {
                                        eachDailyAllocation.setRequestedFte(BigDecimal.valueOf(0d));
                                        eachDailyAllocation.setAllocatedFte(BigDecimal.valueOf(0d));
                                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                                    }
                                }
                            }
                        }

                        avgAllocatedFte = ProjectUtils.roundToTwoDecimalPlace(avgAllocatedFte/workingDays);
                        avgRequestedFte = ProjectUtils.roundToTwoDecimalPlace(avgRequestedFte/workingDays);

//                        add the updated data to the list
                        ProjectResourceMapping projectResourceMapping = storedAllocation.get();
                        projectResourceMapping.setAllocationStartDate(eachData.getAllocationStartDate());
                        projectResourceMapping.setAllocationEndDate(eachData.getAllocationEndDate());
                        projectResourceMapping.setModifiedBy(modifiedBy);
                        projectResourceMapping.setModifiedDate(new Date());
                        projectResourceMapping.setAllocatedAvgFte(avgAllocatedFte);
                        projectResourceMapping.setRequestedFteAvg(avgRequestedFte);
                        allocationDataToUpdate.add(projectResourceMapping);

                        Optional<RequestedResources> resourceRequest = requestedResourcesRepo.findById(storedAllocation.get().getMapId());
                        if(resourceRequest.isPresent()) {
                            RequestedResources requestedResources = resourceRequest.get();

                            if (initialNumberOfDays < updatedNumberOfDays) {
                                requestedResources.setPmLastAction(ProjectUtils.REQUEST_RESOURCE_FTE_PERIOD_INCREASED);
                            } else if (initialNumberOfDays > updatedNumberOfDays) {
                                requestedResources.setPmLastAction(ProjectUtils.REQUEST_RESOURCE_FTE_PERIOD_DECREASED);
                            }
                            if (!requestedResources.getRequestStatus().equals(ProjectUtils.REQUEST_RESOURCE_STATUS_OPEN)) {
                                if (avgAllocatedFte < avgRequestedFte) {
                                    requestedResources.setNotify(true);
                                    requestedResources.setRequestStatus(ProjectUtils.REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED);

                                } else if (avgAllocatedFte > avgRequestedFte) {
                                    requestedResources.setNotify(false);
                                    requestedResources.setRequestStatus(ProjectUtils.REQUEST_RESOURCE_STATUS_OVER_FULFILLED);
                                } else {
                                    requestedResources.setNotify(false);
                                    requestedResources.setRequestStatus(ProjectUtils.REQUEST_RESOURCE_STATUS_COMPLETELY_FULFILLED);
                                }
                            }
                            requestedResources.setNotify(true);
                            requestedResources.setViewedByRMIds("");
                            requestedResources.setNotifyPm(false);
                            requestedResourcesList.add(requestedResources);
                        }
                    }
                }
            }

//            update records with map id
            if(!allocationDataToUpdate.isEmpty()) {
//                update project resource allocations
                requestedResourcesRepo.saveAll(requestedResourcesList);
//                update requested resource table for new changes, if any
                projectResourceMappingRepo.saveAll(allocationDataToUpdate);
            }

/*//            for new records, with no map id
            for(PostProjectResourceAllocationData eachData: allocationDataToInsert){

//                insert the record to get map id
                ProjectResourceMapping eachDataWithMapId = projectResourceMappingRepo.save(new ProjectResourceMapping(eachData));

//                get allocations
                List<AllResourceActiveAllocation> thisRecordAllocations = eachData.getAllocations();

                for(AllResourceActiveAllocation eachDailyAllocation: thisRecordAllocations){

//                    eachDailyAllocation.setAllocationDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachDailyAllocation.getAllocationDate()));
                    LocalDate currentDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();

//                    for each allocation, ignore those which lie on weekend, and those which are outside date range
                    if((currentDate.getDayOfWeek()!= DayOfWeek.SATURDAY) && (currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY)
                            && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationEndDate()) <= 0)
                            && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationStartDate()) >= 0)) {

//                        set map id
                        eachDailyAllocation.setMapId(eachDataWithMapId.getMapId());
//                        add to list
                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                    }
                }

            }*/

//            upsert list
            if(!dailyAllocationDataToUpsert.isEmpty()){
                projectResourceMappingExtendedRepo.saveAll(dailyAllocationDataToUpsert);
            }
        }

        return true;
    }

//    public List<GetProjectAnalysisMaster> getProjAnalyticsData() {
//
//        List<GetProjectAnalysisMaster> getProjectAnalysisMasterList = null;
//        List<GetProjectAnalysis> getProjectAnalysisList = projectMgmtRepo.getProjAnalyticsData();
//
//        if (!getProjectAnalysisList.isEmpty()) {
//
//            Map<Integer, List<GetProjectAnalysis>> groupByProject = getProjectAnalysisList.stream().collect(Collectors.groupingBy(GetProjectAnalysis::getProject_id));
//            getProjectAnalysisMasterList = new ArrayList<>();
//
//            for (Map.Entry<Integer, List<GetProjectAnalysis>> entry : groupByProject.entrySet()) {
//
//                GetProjectAnalysisMaster getProjectAnalysisMaster = new GetProjectAnalysisMaster();
//
//                if (!entry.getValue().isEmpty()) {
//
//                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);
//                    getProjectAnalysisMaster.setProjectId(entry.getValue().get(0).getProject_id());
//                    List<Map<String, Object>> fieldList = new ArrayList<>();
//
//                    for (Map.Entry<String, Object> getProjectAnalyses : objToMap.entrySet()) {
//                        if (!getProjectAnalyses.getKey().equals("fields") && !getProjectAnalyses.getKey().equals("field_value") && !getProjectAnalyses.getKey().equals("field_type")) {
//                            Map<String, Object> fields = new HashMap<>();
//                            fields.put("fieldName", getProjectAnalyses.getKey());
//                            fields.put("fieldValue", getProjectAnalyses.getValue());
//                            try {
//                                if(getProjectAnalyses.getKey().equals("budget") || getProjectAnalyses.getKey().equals("forecast")){
//                                    fields.put("fieldType", "Currency");
//                                } else {
//                                    fields.put("fieldType", entry.getValue().get(0).getClass().getDeclaredField(getProjectAnalyses.getKey()).getType().getSimpleName());
//                                }
//
//                            } catch (NoSuchFieldException e) {
//                                fields.put("fieldType", "");
//                            }
//                            fieldList.add(fields);
//                        }
//                    }
//
//                    for (GetProjectAnalysis getProjectAnalyses : entry.getValue()) {
//                        if (StringUtils.isNotBlank(getProjectAnalyses.getFields())) {
//                            Map<String, Object> fields = new HashMap<>();
//                            fields.put("fieldName", getProjectAnalyses.getFields());
//                            fields.put("fieldValue", getProjectAnalyses.getField_value());
//                            fields.put("fieldType", getProjectAnalyses.getField_type());
//                            fieldList.add(fields);
//                        }
//                    }
//                    getProjectAnalysisMaster.setFields(fieldList);
//                }
//                getProjectAnalysisMasterList.add(getProjectAnalysisMaster);
//            }
//
//        }
//        return getProjectAnalysisMasterList;
//    }

//    public List<GetProjectAnalysisMaster> getProjAnalyticsData() {
//
//        List<GetProjectAnalysisMaster> getProjectAnalysisMasterList = null;
//        List<GetProjectAnalysis> getProjectAnalysisList = projectMgmtRepo.getProjAnalyticsData();
//
//        if (!getProjectAnalysisList.isEmpty()) {
//
//            Map<Integer, List<GetProjectAnalysis>> groupByProject = getProjectAnalysisList.stream().collect(Collectors.groupingBy(GetProjectAnalysis::getProject_id));
//            getProjectAnalysisMasterList = new ArrayList<>();
//
//            for (Map.Entry<Integer, List<GetProjectAnalysis>> entry : groupByProject.entrySet()) {
//
//                GetProjectAnalysisMaster getProjectAnalysisMaster = new GetProjectAnalysisMaster();
//
//                if (!entry.getValue().isEmpty()) {
//
//                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);
//                    getProjectAnalysisMaster.setProjectId(entry.getValue().get(0).getProject_id());
//                    List<Map<String, Object>> fieldList = new ArrayList<>();
//
//                    for (Map.Entry<String, Object> getProjectAnalyses : objToMap.entrySet()) {
//                        if (!getProjectAnalyses.getKey().equals("fields") && !getProjectAnalyses.getKey().equals("field_value") && !getProjectAnalyses.getKey().equals("field_type")) {
//                            Map<String, Object> fields = new HashMap<>();
//                            fields.put("fieldName", getProjectAnalyses.getKey());
//                            fields.put("fieldValue", getProjectAnalyses.getValue());
//                            try {
//                                fields.put("fieldType", entry.getValue().get(0).getClass().getDeclaredField(getProjectAnalyses.getKey()).getType().getSimpleName());
//
//                            } catch (NoSuchFieldException e) {
//                                fields.put("fieldType", "");
//                            }
//                            fieldList.add(fields);
//                        }
//                    }
//
//                    for (GetProjectAnalysis getProjectAnalyses : entry.getValue()) {
//                        if (StringUtils.isNotBlank(getProjectAnalyses.getFields())) {
//                            Map<String, Object> fields = new HashMap<>();
//                            fields.put("fieldName", getProjectAnalyses.getFields());
//                            fields.put("fieldValue", getProjectAnalyses.getField_value());
//                            fields.put("fieldType", getProjectAnalyses.getField_type());
//                            fieldList.add(fields);
//                        }
//                    }
//                    getProjectAnalysisMaster.setFields(fieldList);
//                }
//                getProjectAnalysisMasterList.add(getProjectAnalysisMaster);
//            }
//
//        }
//        return getProjectAnalysisMasterList;
//    }
    public List<GetProjectAnalysisMaster> getProjAnalyticsData(GetDateRange getDateRange, String email, Boolean myView) {

        if(ProjectUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }
        List<Integer> getVisibleDepartmentIdsList = getVisibleProjectIds(email, myView);


//        getDateRange.setStartDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getStartDate()));
//        getDateRange.setEndDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getEndDate()));

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
//        String timeZone = "Asia/Kolkata";
//        String dateFormat = "yyyy-MM-dd";

        List<ProjectMgmtFieldMap> getAnalyticsVisibleField = projectMgmtFieldMapRepo.getProjectAnalyticsSettings();
        List<ProjectMgmtFieldMap> getFieldsData = projectMgmtFieldMapRepo.getFieldDataByFieldName(ProjectUtils.PROJECT_FIELD_NAMES_DATA);
        Map<String, ProjectMgmtFieldMap> getFieldsDataMap = getFieldsData.stream().collect(Collectors.toMap(ProjectMgmtFieldMap::getFields, data -> data));

        List<GetProjectAnalysisMaster> getProjectAnalysisMasterList = null;
        String projectAnalysisQuery = ProjectUtils.getFile("getProjAnalyticsData.txt");

        Query query = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetProjectAnalysis.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("projectIds", getVisibleDepartmentIdsList);

        List<GetProjectAnalysis> getProjectAnalysisList = query.getResultList();

        if (getProjectAnalysisList != null && !getProjectAnalysisList.isEmpty()) {

            Map<Integer, List<GetProjectAnalysis>> groupByProject = getProjectAnalysisList.stream().collect(Collectors.groupingBy(GetProjectAnalysis::getProject_id));
            getProjectAnalysisMasterList = new ArrayList<>();

            for (Map.Entry<Integer, List<GetProjectAnalysis>> entry : groupByProject.entrySet()) {

                GetProjectAnalysisMaster getProjectAnalysisMaster = new GetProjectAnalysisMaster();

                if (!entry.getValue().isEmpty()) {
                    Map<Integer, Boolean> presentFields = new HashMap<>();
                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);
                    getProjectAnalysisMaster.setProjectId(entry.getValue().get(0).getProject_id());
                    List<Map<String, Object>> fieldList = new ArrayList<>();

                    for (Map.Entry<String, Object> getProjectAnalyses : objToMap.entrySet()) {
                        if (!getProjectAnalyses.getKey().equals("fields") && !getProjectAnalyses.getKey().equals("fieldValue") && !getProjectAnalyses.getKey().equals("fieldType")) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", getProjectAnalyses.getKey());
                            if(getProjectAnalyses.getKey().equals("Project Id")){
                                fields.put("fieldValue", getProjectAnalyses.getValue());
                                fields.put("fieldType", "Integer");
                                fields.put("fieldId", 0);
                            }
                            else if(getProjectAnalyses.getKey().equals(ProjectUtils.FIELD_PROJECT_CREATED_DATE)
                                    && (getFieldsDataMap.containsKey(ProjectUtils.FIELD_PROJECT_CREATED_DATE))
                                    && (getFieldsDataMap.get(getProjectAnalyses.getKey()).getIsEnabled().equals("1"))
                                    && (getFieldsDataMap.get(getProjectAnalyses.getKey()).getProjAnalysisView().equals("1"))){
//                                String stringDate = ProjectUtils.convertStringDateToAdminTimeZoneStringDate(String.valueOf(getProjectAnalyses.getValue()), timeZone);
//                                fields.put("fieldValue", stringDate);
                                fields.put("fieldValue", String.valueOf(getProjectAnalyses.getValue()));
                                fields.put("fieldType", "Date");
                                fields.put("fieldId", getFieldsDataMap.get(getProjectAnalyses.getKey()).getFieldId());
                                presentFields.put(getFieldsDataMap.get(getProjectAnalyses.getKey()).getFieldId(), true);
                            }
                            else if(getProjectAnalyses.getKey().equals(ProjectUtils.FIELD_PROJECT_CREATED_BY)
                                    && getFieldsDataMap.containsKey(ProjectUtils.FIELD_PROJECT_CREATED_BY)
                                    && getFieldsDataMap.get(getProjectAnalyses.getKey()).getIsEnabled().equals("1")
                                    && getFieldsDataMap.get(getProjectAnalyses.getKey()).getProjAnalysisView().equals("1")){
                                fields.put("fieldValue", getProjectAnalyses.getValue());
                                fields.put("fieldType", "Text");
                                fields.put("fieldId", getFieldsDataMap.get(getProjectAnalyses.getKey()).getFieldId());
                                presentFields.put(getFieldsDataMap.get(getProjectAnalyses.getKey()).getFieldId(), true);
                            }
                            else if(getProjectAnalyses.getKey().equals(ProjectUtils.FIELD_PROJECT_MODIFIED_DATE)
                                    && getFieldsDataMap.containsKey(ProjectUtils.FIELD_PROJECT_MODIFIED_DATE)
                                    && getFieldsDataMap.get(getProjectAnalyses.getKey()).getIsEnabled().equals("1")
                                    && getFieldsDataMap.get(getProjectAnalyses.getKey()).getProjAnalysisView().equals("1")){
//                                String stringDate = ProjectUtils.convertStringDateToAdminTimeZoneStringDate(String.valueOf(getProjectAnalyses.getValue()), timeZone);
//                                fields.put("fieldValue", stringDate);
                                fields.put("fieldValue", String.valueOf(getProjectAnalyses.getValue()));
                                fields.put("fieldType", "Date");
                                fields.put("fieldId", getFieldsDataMap.get(getProjectAnalyses.getKey()).getFieldId());
                                presentFields.put(getFieldsDataMap.get(getProjectAnalyses.getKey()).getFieldId(), true);
                            }
                            else if(getProjectAnalyses.getKey().equals(ProjectUtils.FIELD_PROJECT_MODIFIED_BY)
                                    && getFieldsDataMap.containsKey(ProjectUtils.FIELD_PROJECT_MODIFIED_BY)
                                    && getFieldsDataMap.get(getProjectAnalyses.getKey()).getIsEnabled().equals("1")
                                    && getFieldsDataMap.get(getProjectAnalyses.getKey()).getProjAnalysisView().equals("1")){
                                fields.put("fieldValue", getProjectAnalyses.getValue());
                                fields.put("fieldType", "Text");
                                fields.put("fieldId", getFieldsDataMap.get(getProjectAnalyses.getKey()).getFieldId());
                                presentFields.put(getFieldsDataMap.get(getProjectAnalyses.getKey()).getFieldId(), true);
                            }

                            if(fields.containsKey("fieldType")){
                                fieldList.add(fields);
                            }
                        }
                    }
                    String projectEndDate = null;
                    for (GetProjectAnalysis getProjectAnalyses : entry.getValue()) {
                        if (StringUtils.isNotBlank(getProjectAnalyses.getFields())) {
                            if(getProjectAnalyses.getFields().equals(ProjectUtils.FIELD_PROJECT_PROJECT_END_DATE)){
                                projectEndDate = getProjectAnalyses.getField_value();
                            }
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", getProjectAnalyses.getFields());
//                            if(getProjectAnalyses.getField_type().equals(ProjectUtils.FIELD_TYPE_DATE)){
//                                String stringDate = ProjectUtils.convertStringDateToAdminTimeZoneStringDate(String.valueOf(getProjectAnalyses.getField_value()), timeZone);
//                                fields.put("fieldValue", stringDate);
//                            }
//                            else{
                                fields.put("fieldValue", getProjectAnalyses.getField_value());
//                            }
                            fields.put("fieldType", getProjectAnalyses.getField_type());
                            fields.put("fieldId", getProjectAnalyses.getField_id());
                            presentFields.put(getProjectAnalyses.getField_id(), true);
                            fieldList.add(fields);
                        }
                    }
                    boolean isDaysLeftVisible = getFieldsDataMap.containsKey(ProjectUtils.FIELD_PROJECT_DAYS_LEFT)
                            && getFieldsDataMap.get(ProjectUtils.FIELD_PROJECT_DAYS_LEFT).getIsEnabled().equals("1")
                            && getFieldsDataMap.get(ProjectUtils.FIELD_PROJECT_DAYS_LEFT).getProjAnalysisView().equals("1");

                    if(isDaysLeftVisible && projectEndDate!=null){
                        Map<String, Object> fields = new HashMap<>();
                        fields.put("fieldName", ProjectUtils.FIELD_PROJECT_DAYS_LEFT);
                        LocalDate currentDate, endDate;
                        currentDate = LocalDate.now();
                        try{
                            endDate = LocalDate.parse(projectEndDate);
                        }
                        catch(Exception e) {
                            Instant instant = Instant.parse(projectEndDate);
                            endDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                        }
                        long daysLeft = ChronoUnit.DAYS.between(currentDate, endDate) + 1;
                        daysLeft = daysLeft>0?daysLeft:0;
                        fields.put("fieldValue", String.valueOf(daysLeft));
                        fields.put("fieldType", "Number");
                        fields.put("fieldId", getFieldsDataMap.get(ProjectUtils.FIELD_PROJECT_DAYS_LEFT).getFieldId());
                        presentFields.put(getFieldsDataMap.get(ProjectUtils.FIELD_PROJECT_DAYS_LEFT).getFieldId(), true);
                        fieldList.add(fields);
                    }
                    for(ProjectMgmtFieldMap eachField: getAnalyticsVisibleField){
                        if(!presentFields.containsKey(eachField.getFieldId())){
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", eachField.getFields());
                            fields.put("fieldValue", "");
                            fields.put("fieldType", eachField.getFieldType());
                            fields.put("fieldId", eachField.getFieldId());
                            fieldList.add(fields);
                        }
                    }
                    fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                    getProjectAnalysisMaster.setFields(fieldList);
                }
                getProjectAnalysisMasterList.add(getProjectAnalysisMaster);
            }

        }

        if(getProjectAnalysisMasterList==null || getProjectAnalysisMasterList.isEmpty()){
            getProjectAnalysisMasterList=null;
        }
        else{
            getProjectAnalysisMasterList.sort(Comparator.comparing(GetProjectAnalysisMaster::getProjectId).reversed());
        }

        return getProjectAnalysisMasterList;
    }

    public Object downloadProjectAnalyticsTable(GetDateRange getDateRange, String email, Boolean myView){

        List<GetProjectAnalysisMaster> getProjectAnalysisMasterList = getProjAnalyticsData(getDateRange, email, myView);
        List<ProjectMgmtFieldMap> projectMgmtFieldMapList = projectMgmtFieldMapRepo.findAll();
        projectMgmtFieldMapList.sort(Comparator.comparingInt(ProjectMgmtFieldMap::getFieldId));

        byte[] excelFile = null;
        try {
            excelFile = excelProcessingService.generateExcelFileForResourceAnalyticsTable(getProjectAnalysisMasterList, projectMgmtFieldMapList);
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ProjectUtils.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        return excelFile;
    }

    public List<AddProject> getProjectList() {
        List<AddProject> getProjectList = projectMgmtRepo.getProjectList();
//        List<AddProject> getProjectListWithFormattedTimeZone = null;
//        if(getProjectList!=null && !getProjectList.isEmpty()){
//
//            List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
//            GeneralSettings generalSettings = generalSettingsList.get(0);
//            String timeZone = "Asia/Kolkata";
//
//            getProjectListWithFormattedTimeZone = new ArrayList<>();
//            for(AddProject eachProject : getProjectList){
//                eachProject.setStartDate(ProjectUtils.convertDateToAdminTimeZoneDate(eachProject.getStartDate(), timeZone));
//                eachProject.setEndDate(ProjectUtils.convertDateToAdminTimeZoneDate(eachProject.getEndDate(), timeZone));
//                getProjectListWithFormattedTimeZone.add(eachProject);
//            }
//        }
        if(getProjectList==null || getProjectList.isEmpty()){
            getProjectList=null;
        }
//        return getProjectListWithFormattedTimeZone;
        return getProjectList;
    }

    /*public List<GetProjectResourceAllocationData> fetchKanbanData(Integer projectId) {
        String getKanbanDataQuery = ProjectUtils.getFile("getKanbanDataQuery.txt");

        Query query = entityManager.createNativeQuery(getKanbanDataQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetKanbanData.class));

        query.setParameter("projectId", projectId);

        List<GetKanbanData> getKanbanDataList = query.getResultList();

        List<GetProjectResourceAllocationData> getProjectResourceAllocationDataList = null;

        if(getKanbanDataList != null && !getKanbanDataList.isEmpty()){

            List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
            GeneralSettings generalSettings = generalSettingsList.get(0);
//            String timeZone = "Asia/Kolkata";

            List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
            Double maxFteAllowedByAdmin = 0d;

            if(resourceMgmtStatusList!=null && !resourceMgmtStatusList.isEmpty()) {
                resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
                for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                    if (eachStatus.getIsEnabled() && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                        if (eachStatus.getEndValue() > maxFteAllowedByAdmin) {
                            maxFteAllowedByAdmin = eachStatus.getEndValue();
                        }
                    }
                }
            }

            if(maxFteAllowedByAdmin<=0){
                maxFteAllowedByAdmin = 1.5d;
            }

//            bring all the date wise allocation data for all non-deleted allocations
            List<AllResourceActiveAllocation> allResourceActiveData = projectResourceMappingRepo.getAllResourceActiveAllocation(projectId);
//            group date wise allocations by resource id
            Map<Integer, List<AllResourceActiveAllocation>> allResourceActiveDataToMap = null;

            if(allResourceActiveData!=null && !allResourceActiveData.isEmpty()) {

                getProjectResourceAllocationDataList = new ArrayList<>();

                allResourceActiveDataToMap = allResourceActiveData.stream().collect(Collectors.groupingBy(AllResourceActiveAllocation::getMapId));

                for (GetKanbanData eachRecord: getKanbanDataList) {

                    List<MonthAllocationLimit> monthlyLimit = new ArrayList<>();

                    LocalDate dateOfJoin = null, lastWorkingDate = null;
                    dateOfJoin = new java.sql.Date(eachRecord.getDate_of_join().getTime()).toLocalDate();
                    if(eachRecord.getLast_working_date()!=null) {
                        lastWorkingDate = new java.sql.Date(eachRecord.getLast_working_date().getTime()).toLocalDate();
                    }

                    String startDateMessage = null, endDateMessage = null;
                    if(eachRecord.getStart_date().compareTo(eachRecord.getDate_of_join())==0){
                        startDateMessage = ProjectUtils.START_DATE_DATE_OF_JOIN_MESSAGE;
                    }
                    else{
                        startDateMessage = ProjectUtils.START_DATE_PROJECT_START_DATE_MESSAGE;
                    }
                    if(eachRecord.getLast_working_date()!=null && eachRecord.getEnd_date().compareTo(eachRecord.getLast_working_date())==0){
                        endDateMessage = ProjectUtils.END_DATE_LAST_WORKING_DATE_MESSAGE;
                    }
                    else{
                        endDateMessage = ProjectUtils.END_DATE_PROJECT_END_DATE_MESSAGE;
                    }

                    GetProjectResourceAllocationData eachAllocation = new GetProjectResourceAllocationData(eachRecord);
                    eachAllocation.setMax_fte_possible(maxFteAllowedByAdmin);
                    eachAllocation.setStart_date_message(startDateMessage);
                    eachAllocation.setEnd_date_message(endDateMessage);

                    LocalDate workingDaysStartDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
                    LocalDate workingDaysEndDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();

                    workingDaysStartDate = workingDaysStartDate.withDayOfMonth(1);
                    workingDaysEndDate = workingDaysEndDate.withDayOfMonth(workingDaysEndDate.lengthOfMonth());

                    if(dateOfJoin!=null && workingDaysStartDate.isBefore(dateOfJoin)){
                        workingDaysStartDate = dateOfJoin;
                    }
                    if(lastWorkingDate!=null && lastWorkingDate.isBefore(workingDaysEndDate)){
                        workingDaysEndDate = lastWorkingDate;
                    }

                    Month month = workingDaysStartDate.getMonth();
                    Integer year = workingDaysStartDate.getYear();
                    Integer workingDaysInTheMonth = 0;

                    while(!workingDaysStartDate.isAfter(workingDaysEndDate)){
                        if(workingDaysStartDate.getMonth()!=month){
                            monthlyLimit.add(new MonthAllocationLimit(month.toString(), year, null, workingDaysInTheMonth.doubleValue()));
                            month = workingDaysStartDate.getMonth();
                            year = workingDaysStartDate.getYear();
                            workingDaysInTheMonth = 0;
                        }
                        if(workingDaysStartDate.getDayOfWeek()!=DayOfWeek.SATURDAY && workingDaysStartDate.getDayOfWeek()!=DayOfWeek.SUNDAY) {
                            workingDaysInTheMonth++;
                        }
                        workingDaysStartDate = workingDaysStartDate.plusDays(1);
                    }
                    monthlyLimit.add(new MonthAllocationLimit(month.toString(), year, null, workingDaysInTheMonth.doubleValue()));
                    eachAllocation.setMonthlyWorkingDays(monthlyLimit);

                    List<AllResourceActiveAllocation> eachRecordAllocationList = allResourceActiveDataToMap.get(eachRecord.getMap_id());
                    eachRecordAllocationList.sort(Comparator.comparing(AllResourceActiveAllocation::getAllocationDate));
                    for(AllResourceActiveAllocation eachDailyAllocation: eachRecordAllocationList){
                        LocalDate allocationDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();
                        eachDailyAllocation.setAllocationDate(java.sql.Date.valueOf(allocationDate));
//                        eachDailyAllocation.setAllocationDate(ProjectUtils.convertDateToAdminTimeZoneDate(eachDailyAllocation.getAllocationDate(), timeZone));
                    }
                    eachAllocation.setAllocations(eachRecordAllocationList);

                    LocalDate startDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
                    LocalDate endDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();

//                    LocalDate startDate = new java.sql.Date(ProjectUtils.convertDateToAdminTimeZoneDate(eachAllocation.getAllocationStartDate(), timeZone).getTime()).toLocalDate();
//                    LocalDate endDate = new java.sql.Date(ProjectUtils.convertDateToAdminTimeZoneDate(eachAllocation.getAllocationEndDate(), timeZone).getTime()).toLocalDate();

                    Integer workingDays = ProjectUtils.numberOfWorkingDays(startDate, endDate);

                    eachAllocation.setAvgFte(ProjectUtils.roundToOneDecimalPlace(eachRecord.getSum_fte().doubleValue()/workingDays));

                    getProjectResourceAllocationDataList.add(eachAllocation);
                }
            }
        }

        return getProjectResourceAllocationDataList;
    }*/

    public Object getNamedAllocatedResourceDetails(Integer projectId, Integer financialYear) {

        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month month = Month.valueOf(startMonth.toUpperCase());
        LocalDate customStartDate = LocalDate.of(financialYear, month, startDateOfMonth);
        LocalDate customEndDate = customStartDate.plusYears(1).minusDays(1);
        Date financialYearStartDate = java.sql.Date.valueOf(customStartDate);
        Date financialYearEndDate = java.sql.Date.valueOf(customEndDate);

        String getKanbanDataQuery = ProjectUtils.getFile("getKanbanDataQuery.txt");

        Query query = entityManager.createNativeQuery(getKanbanDataQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetKanbanData.class));

        query.setParameter("projectId", projectId);
//        query.setParameter("startDate", financialYearStartDate);
//        query.setParameter("endDate", financialYearEndDate);

        List<GetKanbanData> getKanbanDataList = query.getResultList();

        List<GetProjectResourceAllocationData> getProjectResourceAllocationDataList = null;
        if(getKanbanDataList != null && !getKanbanDataList.isEmpty()){

            List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
            Double maxFteAllowedByAdmin = 0d;

            if(resourceMgmtStatusList!=null && !resourceMgmtStatusList.isEmpty()) {
//                resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
                for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                    if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                        if (eachStatus.getEndValue() > maxFteAllowedByAdmin) {
                            maxFteAllowedByAdmin = eachStatus.getEndValue();
                        }
                    }
                }
            }

            if(maxFteAllowedByAdmin<=0){
                maxFteAllowedByAdmin = 1.5d;
            }

            LocalDate yearStartWeekStartLocalDate = customStartDate.with(DayOfWeek.MONDAY);
            LocalDate yearEndWeekDateEndLocalDate = customEndDate.with(DayOfWeek.SUNDAY);
            Date yearStartWeekStartDate = java.sql.Date.valueOf(yearStartWeekStartLocalDate);
            Date yearEndWeekDateEndDate = java.sql.Date.valueOf(yearEndWeekDateEndLocalDate);

//            bring all the date wise allocation data for all non-deleted allocations
            List<AllResourceActiveAllocation> allResourceActiveData = projectResourceMappingRepo.getAllResourceActiveAllocation(projectId, yearStartWeekStartDate, yearEndWeekDateEndDate);
//            group date wise allocations by resource id
            Map<Integer, List<AllResourceActiveAllocation>> allResourceActiveDataToMap = null;

            Set<Integer> listOfResourceIds = new HashSet<>();
            Date startDate = getKanbanDataList.get(0).getStart_date();
            Date endDate = getKanbanDataList.get(0).getEnd_date();
            for(GetKanbanData eachRecord: getKanbanDataList){
                listOfResourceIds.add(eachRecord.getResource_id());
                startDate = startDate.compareTo(eachRecord.getStart_date())<0?startDate:eachRecord.getStart_date();
                endDate = endDate.compareTo(eachRecord.getEnd_date())<0?endDate:eachRecord.getEnd_date();
            }

            List<ResourceDailyFteSumData> resourceDailyFteSumDataList = projectResourceMappingRepo.getResourceDailyFteData(new ArrayList<>(listOfResourceIds), startDate, endDate);

            Map<Integer, Map<LocalDate, Double>> resourceDailyFteSumDataListToMap = new HashMap<>();

            if(resourceDailyFteSumDataList!=null && !resourceDailyFteSumDataList.isEmpty()) {
                for (ResourceDailyFteSumData eachResource : resourceDailyFteSumDataList) {
                    Map<LocalDate, Double> eachResourceDailyData = null;
                    if(resourceDailyFteSumDataListToMap.containsKey(eachResource.getResourceId())){
                        eachResourceDailyData = resourceDailyFteSumDataListToMap.get(eachResource.getResourceId());
                    }
                    else{
                        eachResourceDailyData = new HashMap<>();
                    }
                    LocalDate localDate = new java.sql.Date(eachResource.getAllocationDate().getTime()).toLocalDate();
                    eachResourceDailyData.merge(localDate, eachResource.getSumFte().doubleValue(), Double::sum);
                    resourceDailyFteSumDataListToMap.put(eachResource.getResourceId(), eachResourceDailyData);
                }
            }

            if(allResourceActiveData!=null && !allResourceActiveData.isEmpty()) {

                getProjectResourceAllocationDataList = new ArrayList<>();

                allResourceActiveDataToMap = allResourceActiveData.stream().collect(Collectors.groupingBy(AllResourceActiveAllocation::getMapId));

                for (GetKanbanData eachRecord: getKanbanDataList) {

                    if(allResourceActiveDataToMap.containsKey(eachRecord.getMap_id())) {

                        LocalDate dateOfJoin = null, lastWorkingDate = null;
                        dateOfJoin = new java.sql.Date(eachRecord.getDate_of_join().getTime()).toLocalDate();
                        if (eachRecord.getLast_working_date() != null) {
                            lastWorkingDate = new java.sql.Date(eachRecord.getLast_working_date().getTime()).toLocalDate();
                        }

                        String startDateMessage = null, endDateMessage = null;
                        if (eachRecord.getStart_date().compareTo(eachRecord.getDate_of_join()) == 0) {
                            startDateMessage = ProjectUtils.START_DATE_DATE_OF_JOIN_MESSAGE;
                        } else {
                            startDateMessage = ProjectUtils.START_DATE_PROJECT_START_DATE_MESSAGE;
                        }
                        if (eachRecord.getLast_working_date() != null && eachRecord.getEnd_date().compareTo(eachRecord.getLast_working_date()) == 0) {
                            endDateMessage = ProjectUtils.END_DATE_LAST_WORKING_DATE_MESSAGE;
                        } else {
                            endDateMessage = ProjectUtils.END_DATE_PROJECT_END_DATE_MESSAGE;
                        }

                        GetProjectResourceAllocationData eachAllocation = new GetProjectResourceAllocationData(eachRecord, true);
                        eachAllocation.setMax_fte_possible(maxFteAllowedByAdmin);
                        eachAllocation.setStart_date_message(startDateMessage);
                        eachAllocation.setEnd_date_message(endDateMessage);

//                        calculate dates for which i need to run the loop
                        LocalDate workingDaysStartDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
                        LocalDate workingDaysEndDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();

                        if (dateOfJoin != null && workingDaysStartDate.isBefore(dateOfJoin)) {
                            workingDaysStartDate = dateOfJoin;
                        }
                        if (lastWorkingDate != null && lastWorkingDate.isBefore(workingDaysEndDate)) {
                            workingDaysEndDate = lastWorkingDate;
                        }

                        if (workingDaysStartDate.isBefore(yearStartWeekStartLocalDate)) {
                            workingDaysStartDate = yearStartWeekStartLocalDate;
                        }
                        if (yearEndWeekDateEndLocalDate.isBefore(workingDaysEndDate)) {
                            workingDaysEndDate = yearEndWeekDateEndLocalDate;
                        }

                        Map<Date, AllResourceActiveAllocation> eachRecordAllocationListToMap = new HashMap<>();

//                    resource daily allocations
                        Map<LocalDate, Double> resourceDailyFte = resourceDailyFteSumDataListToMap.get(eachRecord.getResource_id());

                        List<AllResourceActiveAllocation> eachRecordAllocationList = allResourceActiveDataToMap.get(eachRecord.getMap_id());
                        List<AllResourceActiveAllocation> eachRecordAllocationListNew = new ArrayList<>();
                        eachRecordAllocationList.sort(Comparator.comparing(AllResourceActiveAllocation::getAllocationDate));
                        for (AllResourceActiveAllocation eachDailyAllocation : eachRecordAllocationList) {
                            LocalDate allocationDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();
                            eachDailyAllocation.setAllocationDate(java.sql.Date.valueOf(allocationDate));

                            if (resourceDailyFte!=null && resourceDailyFte.containsKey(allocationDate)) {
                                Double availableFte = ProjectUtils.roundToTwoDecimalPlace(maxFteAllowedByAdmin - resourceDailyFte.get(allocationDate));
                                eachDailyAllocation.setAvailableFte(availableFte);
                            } else {
                                eachDailyAllocation.setAvailableFte(maxFteAllowedByAdmin);
                            }

                            if(eachDailyAllocation.getAllocationDate().compareTo(eachAllocation.getAllocationStartDate())>=0
                                    && eachDailyAllocation.getAllocationDate().compareTo(financialYearStartDate)>=0
                                    && eachDailyAllocation.getAllocationDate().compareTo(eachAllocation.getAllocationEndDate())<=0
                                    && eachDailyAllocation.getAllocationDate().compareTo(financialYearEndDate)<=0) {
                                eachRecordAllocationListNew.add(eachDailyAllocation);
                            }

                            eachRecordAllocationListToMap.put(eachDailyAllocation.getAllocationDate(), eachDailyAllocation);
                        }
                        eachAllocation.setAllocations(eachRecordAllocationListNew);

//                    make changes here
                        List<AllocationDataByMonth> allocationDataByMonthList = new ArrayList<>();
                        List<AllocationDataByWeek> allocationDataByWeekList = new ArrayList<>();

                        LocalDate monthEndDate, weekStartDate, weekEndDate;
                        monthEndDate = workingDaysStartDate.withDayOfMonth(workingDaysStartDate.lengthOfMonth());
                        weekStartDate = workingDaysStartDate.with(DayOfWeek.MONDAY);
                        weekEndDate = workingDaysStartDate.with(DayOfWeek.SUNDAY);
                        Integer workingDaysInTheMonth = 0, workingDaysInTheWeek = 0;
                        Double weekAllocatedSum = 0d, weekAvailableSum = 0d, weekRequestedSum = 0d;
                        Double monthAllocatedSum = 0d, monthAvailableSum = 0d, monthRequestedSum = 0d;
                        List<AllResourceActiveAllocation> weeklyAllocationData = new ArrayList<>();
                        List<AllResourceActiveAllocation> monthlyAllocationData = new ArrayList<>();

                        while (!workingDaysStartDate.isAfter(workingDaysEndDate)) {

                            if (workingDaysStartDate.isAfter(weekEndDate)) {
//                            make all three parameters zero
                                weekAllocatedSum = 0d;
                                weekAvailableSum = 0d;
                                weekRequestedSum = 0d;

                                workingDaysInTheWeek = 0;
                                weeklyAllocationData = new ArrayList<>();
                                weekStartDate = workingDaysStartDate;
                                weekEndDate = workingDaysStartDate.with(DayOfWeek.SUNDAY);
                            }

                            if (workingDaysStartDate.isAfter(monthEndDate)) {
//                            make all three parameters zero
                                monthAllocatedSum = 0d;
                                monthAvailableSum = 0d;
                                monthRequestedSum = 0d;

                                workingDaysInTheMonth = 0;
                                monthlyAllocationData = new ArrayList<>();
                                monthEndDate = workingDaysStartDate.withDayOfMonth(workingDaysStartDate.lengthOfMonth());
                            }

                            if (workingDaysStartDate.getDayOfWeek() != DayOfWeek.SATURDAY && workingDaysStartDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                                Date currentDate = java.sql.Date.valueOf(workingDaysStartDate);
                                if (eachRecordAllocationListToMap.containsKey(currentDate)) {

                                    AllResourceActiveAllocation currentDateRecord = eachRecordAllocationListToMap.get(currentDate);

                                    weekAllocatedSum += currentDateRecord.getAllocatedFte().doubleValue();
                                    weekRequestedSum += currentDateRecord.getRequestedFte().doubleValue();
                                    weekAvailableSum += currentDateRecord.getAvailableFte();
                                    workingDaysInTheWeek++;
                                    weeklyAllocationData.add(currentDateRecord);

                                    monthAllocatedSum += currentDateRecord.getAllocatedFte().doubleValue();
                                    monthRequestedSum += currentDateRecord.getRequestedFte().doubleValue();
                                    monthAvailableSum += currentDateRecord.getAvailableFte();
                                    workingDaysInTheMonth++;
                                    monthlyAllocationData.add(currentDateRecord);
                                }
                            }

                            if (workingDaysStartDate.isEqual(monthEndDate) || workingDaysStartDate.isEqual(workingDaysEndDate)) {
                                Double monthAllocatedAverage = monthAllocatedSum / workingDaysInTheMonth;
                                monthAllocatedAverage = ProjectUtils.roundToTwoDecimalPlace(monthAllocatedAverage);

                                Double monthAvailableAverage = monthAvailableSum / workingDaysInTheMonth;
                                monthAvailableAverage = ProjectUtils.roundToTwoDecimalPlace(monthAvailableAverage);

                                Double monthRequestedAverage = monthRequestedSum / workingDaysInTheMonth;
                                monthRequestedAverage = ProjectUtils.roundToTwoDecimalPlace(monthRequestedAverage);

                                if(!monthEndDate.isBefore(customStartDate) && !monthEndDate.isAfter(customEndDate)) {
                                    AllocationDataByMonth currentMonth = new AllocationDataByMonth(monthEndDate.getMonth().toString(), monthEndDate.getYear(), monthAllocatedAverage, monthRequestedAverage, monthAvailableAverage, monthlyAllocationData);
                                    allocationDataByMonthList.add(currentMonth);
                                }
                            }

                            if (workingDaysStartDate.isEqual(weekEndDate) || workingDaysStartDate.isEqual(workingDaysEndDate)) {
                                Double weekAllocatedAverage = weekAllocatedSum / workingDaysInTheWeek;
                                weekAllocatedAverage = ProjectUtils.roundToTwoDecimalPlace(weekAllocatedAverage);

                                Double weekAvailableAverage = weekAvailableSum / workingDaysInTheWeek;
                                weekAvailableAverage = ProjectUtils.roundToTwoDecimalPlace(weekAvailableAverage);

                                Double weekRequestedAverage = weekRequestedSum / workingDaysInTheWeek;
                                weekRequestedAverage = ProjectUtils.roundToTwoDecimalPlace(weekRequestedAverage);

                                Date weekStart = java.sql.Date.valueOf(weekStartDate);
                                AllocationDataByWeek currentWeek = new AllocationDataByWeek(weekStart, weekStartDate.getYear(), weekAllocatedAverage, weekRequestedAverage, weekAvailableAverage, weeklyAllocationData);
                                allocationDataByWeekList.add(currentWeek);
                            }

                            workingDaysStartDate = workingDaysStartDate.plusDays(1);
                        }

                        eachAllocation.setMonthlyAllocationData(allocationDataByMonthList);
                        eachAllocation.setWeeklyAllocationData(allocationDataByWeekList);
//                    till here

                        LocalDate resourceWorkingStartDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
                        LocalDate resourceWorkingEndDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();

                        Integer workingDays = ProjectUtils.numberOfWorkingDays(resourceWorkingStartDate, resourceWorkingEndDate);

                        eachAllocation.setAvgFte(ProjectUtils.roundToTwoDecimalPlace(eachRecord.getSum_fte().doubleValue() / workingDays));

                        getProjectResourceAllocationDataList.add(eachAllocation);
                    }
                }
            }
        }

        Map<String, Object> returnData = new HashMap<>();

        if(getProjectResourceAllocationDataList!=null && !getProjectResourceAllocationDataList.isEmpty()) {
            returnData.put("data", getProjectResourceAllocationDataList);
            returnData.put("startDate", financialYearStartDate);
            returnData.put("endDate", financialYearEndDate);
        }
        else{
            returnData = null;
        }

        return returnData;
    }

    public Object getUnnamedAllocatedResourceDetails(Integer projectId, Integer financialYear) {

        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month month = Month.valueOf(startMonth.toUpperCase());
        LocalDate customStartDate = LocalDate.of(financialYear, month, startDateOfMonth);
        LocalDate customEndDate = customStartDate.plusYears(1).minusDays(1);
        Date financialYearStartDate = java.sql.Date.valueOf(customStartDate);
        Date financialYearEndDate = java.sql.Date.valueOf(customEndDate);

        String getKanbanDataQueryUnnamed = ProjectUtils.getFile("getKanbanDataQueryUnnamedResources.txt");

        Query query2 = entityManager.createNativeQuery(getKanbanDataQueryUnnamed).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query2).setResultTransformer(new AliasToBeanResultTransformer(GetKanbanData.class));

        query2.setParameter("projectId", projectId);

        List<GetKanbanData> getKanbanDataQueryForOpenUnnamedResource = query2.getResultList();

        List<GetProjectResourceAllocationData> getProjectResourceAllocationDataList = null;

        List<GetKanbanData> getKanbanDataList = new ArrayList<>();

//        if(getKanbanDataQueryForOpenNamedResource!=null && !getKanbanDataQueryForOpenNamedResource.isEmpty()) {
//            getKanbanDataList.addAll(getKanbanDataQueryForOpenNamedResource);
//        }
        if(getKanbanDataQueryForOpenUnnamedResource!=null && !getKanbanDataQueryForOpenUnnamedResource.isEmpty()) {
            getKanbanDataList.addAll(getKanbanDataQueryForOpenUnnamedResource);
        }

        if(!getKanbanDataList.isEmpty()){

//            List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
//            GeneralSettings generalSettings = generalSettingsList.get(0);
//            String timeZone = "Asia/Kolkata";

            List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
            Double maxFteAllowedByAdmin = 0d;

            if(resourceMgmtStatusList!=null && !resourceMgmtStatusList.isEmpty()) {
//                resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
                for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                    if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                        if (eachStatus.getEndValue() > maxFteAllowedByAdmin) {
                            maxFteAllowedByAdmin = eachStatus.getEndValue();
                        }
                    }
                }
            }

            if(maxFteAllowedByAdmin<=0){
                maxFteAllowedByAdmin = 1.5d;
            }

            LocalDate yearStartWeekStartLocalDate = customStartDate.with(DayOfWeek.MONDAY);
            LocalDate yearEndWeekDateEndLocalDate = customEndDate.with(DayOfWeek.SUNDAY);
            Date yearStartWeekStartDate = java.sql.Date.valueOf(yearStartWeekStartLocalDate);
            Date yearEndWeekDateEndDate = java.sql.Date.valueOf(yearEndWeekDateEndLocalDate);

//            bring all the date wise allocation data for all non-deleted allocations
            List<AllResourceActiveAllocation> allResourceActiveData = projectResourceMappingRepo.getAllResourceActiveAllocation(projectId, yearStartWeekStartDate, yearEndWeekDateEndDate);
//            group date wise allocations by resource id
            Map<Integer, List<AllResourceActiveAllocation>> allResourceActiveDataToMap = null;

            Set<Integer> listOfResourceIds = new HashSet<>();
            Date startDate = getKanbanDataList.get(0).getStart_date();
            Date endDate = getKanbanDataList.get(0).getEnd_date();
            for(GetKanbanData eachRecord: getKanbanDataList){
                if(eachRecord.getResource_id()!=null) {
                    listOfResourceIds.add(eachRecord.getResource_id());
                    startDate = startDate.compareTo(eachRecord.getStart_date()) < 0 ? startDate : eachRecord.getStart_date();
                    endDate = endDate.compareTo(eachRecord.getEnd_date()) < 0 ? endDate : eachRecord.getEnd_date();
                }
            }

            List<ResourceDailyFteSumData> resourceDailyFteSumDataList = projectResourceMappingRepo.getResourceDailyFteData(new ArrayList<>(listOfResourceIds), startDate, endDate);

            Map<Integer, Map<LocalDate, Double>> resourceDailyFteSumDataListToMap = new HashMap<>();

            if(resourceDailyFteSumDataList!=null && !resourceDailyFteSumDataList.isEmpty()) {
                for (ResourceDailyFteSumData eachResource : resourceDailyFteSumDataList) {
                    Map<LocalDate, Double> eachResourceDailyData = null;
                    if(resourceDailyFteSumDataListToMap.containsKey(eachResource.getResourceId())){
                        eachResourceDailyData = resourceDailyFteSumDataListToMap.get(eachResource.getResourceId());
                    }
                    else{
                        eachResourceDailyData = new HashMap<>();
                    }
                    LocalDate localDate = new java.sql.Date(eachResource.getAllocationDate().getTime()).toLocalDate();
                    eachResourceDailyData.merge(localDate, eachResource.getSumFte().doubleValue(), Double::sum);
                    resourceDailyFteSumDataListToMap.put(eachResource.getResourceId(), eachResourceDailyData);
                }
            }

            if(allResourceActiveData!=null && !allResourceActiveData.isEmpty()) {

                getProjectResourceAllocationDataList = new ArrayList<>();

                allResourceActiveDataToMap = allResourceActiveData.stream().collect(Collectors.groupingBy(AllResourceActiveAllocation::getMapId));

                for (GetKanbanData eachRecord: getKanbanDataList) {

                    if(allResourceActiveDataToMap.containsKey(eachRecord.getMap_id())) {

                        LocalDate dateOfJoin = null, lastWorkingDate = null;
                        if (eachRecord.getDate_of_join() != null) {
                            dateOfJoin = new java.sql.Date(eachRecord.getDate_of_join().getTime()).toLocalDate();
                        }
                        if (eachRecord.getLast_working_date() != null) {
                            lastWorkingDate = new java.sql.Date(eachRecord.getLast_working_date().getTime()).toLocalDate();
                        }

                        String startDateMessage = null, endDateMessage = null;
                        if (eachRecord.getDate_of_join() != null && eachRecord.getStart_date().compareTo(eachRecord.getDate_of_join()) == 0) {
                            startDateMessage = ProjectUtils.START_DATE_DATE_OF_JOIN_MESSAGE;
                        } else {
                            startDateMessage = ProjectUtils.START_DATE_PROJECT_START_DATE_MESSAGE;
                        }
                        if (eachRecord.getLast_working_date() != null && eachRecord.getEnd_date().compareTo(eachRecord.getLast_working_date()) == 0) {
                            endDateMessage = ProjectUtils.END_DATE_LAST_WORKING_DATE_MESSAGE;
                        } else {
                            endDateMessage = ProjectUtils.END_DATE_PROJECT_END_DATE_MESSAGE;
                        }

                        GetProjectResourceAllocationData eachAllocation = new GetProjectResourceAllocationData(eachRecord, false);
                        eachAllocation.setMax_fte_possible(maxFteAllowedByAdmin);
                        eachAllocation.setStart_date_message(startDateMessage);
                        eachAllocation.setEnd_date_message(endDateMessage);

//                        calculate dates for which i need to run the loop
                        LocalDate workingDaysStartDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
                        LocalDate workingDaysEndDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();

                        if (dateOfJoin != null && workingDaysStartDate.isBefore(dateOfJoin)) {
                            workingDaysStartDate = dateOfJoin;
                        }
                        if (lastWorkingDate != null && lastWorkingDate.isBefore(workingDaysEndDate)) {
                            workingDaysEndDate = lastWorkingDate;
                        }

                        if (workingDaysStartDate.isBefore(yearStartWeekStartLocalDate)) {
                            workingDaysStartDate = yearStartWeekStartLocalDate;
                        }
                        if (yearEndWeekDateEndLocalDate.isBefore(workingDaysEndDate)) {
                            workingDaysEndDate = yearEndWeekDateEndLocalDate;
                        }

                        Map<Date, AllResourceActiveAllocation> eachRecordAllocationListToMap = new HashMap<>();

//                    resource daily allocations
                        Map<LocalDate, Double> resourceDailyFte = null;
                        if (eachRecord.getResource_id() != null) {
                            resourceDailyFte = resourceDailyFteSumDataListToMap.get(eachRecord.getResource_id());
                        }

                        List<AllResourceActiveAllocation> eachRecordAllocationList = allResourceActiveDataToMap.get(eachRecord.getMap_id());
                        List<AllResourceActiveAllocation> eachRecordAllocationListNew = new ArrayList<>();
                        eachRecordAllocationList.sort(Comparator.comparing(AllResourceActiveAllocation::getAllocationDate));
                        for (AllResourceActiveAllocation eachDailyAllocation : eachRecordAllocationList) {
                            LocalDate allocationDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();
                            eachDailyAllocation.setAllocationDate(java.sql.Date.valueOf(allocationDate));

                            if (resourceDailyFte != null && resourceDailyFte.containsKey(allocationDate)) {
                                Double availableFte = ProjectUtils.roundToTwoDecimalPlace(maxFteAllowedByAdmin - resourceDailyFte.get(allocationDate));
                                eachDailyAllocation.setAvailableFte(availableFte);
                            } else {
                                eachDailyAllocation.setAvailableFte(maxFteAllowedByAdmin);
                            }

                            if(eachDailyAllocation.getAllocationDate().compareTo(eachAllocation.getAllocationStartDate())>=0
                                    && eachDailyAllocation.getAllocationDate().compareTo(financialYearStartDate)>=0
                                    && eachDailyAllocation.getAllocationDate().compareTo(eachAllocation.getAllocationEndDate())<=0
                                    && eachDailyAllocation.getAllocationDate().compareTo(financialYearEndDate)<=0) {
                                eachRecordAllocationListNew.add(eachDailyAllocation);
                            }

                            eachRecordAllocationListToMap.put(eachDailyAllocation.getAllocationDate(), eachDailyAllocation);
                        }
                        eachAllocation.setAllocations(eachRecordAllocationListNew);

//                    make changes here
                        List<AllocationDataByMonth> allocationDataByMonthList = new ArrayList<>();
                        List<AllocationDataByWeek> allocationDataByWeekList = new ArrayList<>();

                        LocalDate monthEndDate, weekStartDate, weekEndDate;
                        monthEndDate = workingDaysStartDate.withDayOfMonth(workingDaysStartDate.lengthOfMonth());
                        weekStartDate = workingDaysStartDate.with(DayOfWeek.MONDAY);
                        weekEndDate = workingDaysStartDate.with(DayOfWeek.SUNDAY);
                        Integer workingDaysInTheMonth = 0, workingDaysInTheWeek = 0;
                        Double weekAllocatedSum = 0d, weekAvailableSum = 0d, weekRequestedSum = 0d;
                        Double monthAllocatedSum = 0d, monthAvailableSum = 0d, monthRequestedSum = 0d;
                        List<AllResourceActiveAllocation> weeklyAllocationData = new ArrayList<>();
                        List<AllResourceActiveAllocation> monthlyAllocationData = new ArrayList<>();

                        while (!workingDaysStartDate.isAfter(workingDaysEndDate)) {

                            if (workingDaysStartDate.isAfter(weekEndDate)) {
//                            make all three parameters zero
                                weekAllocatedSum = 0d;
                                weekAvailableSum = 0d;
                                weekRequestedSum = 0d;

                                workingDaysInTheWeek = 0;
                                weeklyAllocationData = new ArrayList<>();
                                weekStartDate = workingDaysStartDate;
                                weekEndDate = workingDaysStartDate.with(DayOfWeek.SUNDAY);
                            }

                            if (workingDaysStartDate.isAfter(monthEndDate)) {
//                            make all three parameters zero
                                monthAllocatedSum = 0d;
                                monthAvailableSum = 0d;
                                monthRequestedSum = 0d;

                                workingDaysInTheMonth = 0;
                                monthlyAllocationData = new ArrayList<>();
                                monthEndDate = workingDaysStartDate.withDayOfMonth(workingDaysStartDate.lengthOfMonth());
                            }

                            if (workingDaysStartDate.getDayOfWeek() != DayOfWeek.SATURDAY && workingDaysStartDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                                Date currentDate = java.sql.Date.valueOf(workingDaysStartDate);
                                if (eachRecordAllocationListToMap.containsKey(currentDate)) {

                                    AllResourceActiveAllocation currentDateRecord = eachRecordAllocationListToMap.get(currentDate);

                                    weekAllocatedSum += currentDateRecord.getAllocatedFte().doubleValue();
                                    weekRequestedSum += currentDateRecord.getRequestedFte().doubleValue();
                                    weekAvailableSum += currentDateRecord.getAvailableFte();
                                    workingDaysInTheWeek++;
                                    weeklyAllocationData.add(currentDateRecord);

                                    monthAllocatedSum += currentDateRecord.getAllocatedFte().doubleValue();
                                    monthRequestedSum += currentDateRecord.getRequestedFte().doubleValue();
                                    monthAvailableSum += currentDateRecord.getAvailableFte();
                                    workingDaysInTheMonth++;
                                    monthlyAllocationData.add(currentDateRecord);
                                }
                            }

                            if (workingDaysStartDate.isEqual(monthEndDate) || workingDaysStartDate.isEqual(workingDaysEndDate)) {
                                Double monthAllocatedAverage = monthAllocatedSum / workingDaysInTheMonth;
                                monthAllocatedAverage = ProjectUtils.roundToTwoDecimalPlace(monthAllocatedAverage);

                                Double monthAvailableAverage = monthAvailableSum / workingDaysInTheMonth;
                                monthAvailableAverage = ProjectUtils.roundToTwoDecimalPlace(monthAvailableAverage);

                                Double monthRequestedAverage = monthRequestedSum / workingDaysInTheMonth;
                                monthRequestedAverage = ProjectUtils.roundToTwoDecimalPlace(monthRequestedAverage);

                                if(!monthEndDate.isBefore(customStartDate) && !monthEndDate.isAfter(customEndDate)) {
                                    AllocationDataByMonth currentMonth = new AllocationDataByMonth(monthEndDate.getMonth().toString(), monthEndDate.getYear(), monthAllocatedAverage, monthRequestedAverage, monthAvailableAverage, monthlyAllocationData);
                                    allocationDataByMonthList.add(currentMonth);
                                }
                            }

                            if (workingDaysStartDate.isEqual(weekEndDate) || workingDaysStartDate.isEqual(workingDaysEndDate)) {
                                Double weekAllocatedAverage = weekAllocatedSum / workingDaysInTheWeek;
                                weekAllocatedAverage = ProjectUtils.roundToTwoDecimalPlace(weekAllocatedAverage);

                                Double weekAvailableAverage = weekAvailableSum / workingDaysInTheWeek;
                                weekAvailableAverage = ProjectUtils.roundToTwoDecimalPlace(weekAvailableAverage);

                                Double weekRequestedAverage = weekRequestedSum / workingDaysInTheWeek;
                                weekRequestedAverage = ProjectUtils.roundToTwoDecimalPlace(weekRequestedAverage);

                                Date weekStart = java.sql.Date.valueOf(weekStartDate);
                                AllocationDataByWeek currentWeek = new AllocationDataByWeek(weekStart, weekStartDate.getYear(), weekAllocatedAverage, weekRequestedAverage, weekAvailableAverage, weeklyAllocationData);
                                allocationDataByWeekList.add(currentWeek);
                            }

                            workingDaysStartDate = workingDaysStartDate.plusDays(1);
                        }

                        eachAllocation.setMonthlyAllocationData(allocationDataByMonthList);
                        eachAllocation.setWeeklyAllocationData(allocationDataByWeekList);
//                    till here

                        LocalDate resourceWorkingStartDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
                        LocalDate resourceWorkingEndDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();

                        Integer workingDays = ProjectUtils.numberOfWorkingDays(resourceWorkingStartDate, resourceWorkingEndDate);

                        eachAllocation.setAvgFte(ProjectUtils.roundToTwoDecimalPlace(eachRecord.getSum_fte().doubleValue() / workingDays));

                        getProjectResourceAllocationDataList.add(eachAllocation);
                    }
                }
            }
        }

        Map<String, Object> returnData = new HashMap<>();

        if(getProjectResourceAllocationDataList!=null && !getProjectResourceAllocationDataList.isEmpty()) {
            returnData.put("data", getProjectResourceAllocationDataList);
            returnData.put("startDate", financialYearStartDate);
            returnData.put("endDate", financialYearEndDate);
        }
        else{
            returnData = null;
        }

        return returnData;
    }

    /*public Object createUpdateKanbanDataNew(List<PostProjectResourceAllocationData> postProjectResourceAllocationDataList, Integer financialYear, String modifiedBy) {

//        check if response data is empty
        if(postProjectResourceAllocationDataList!=null && !postProjectResourceAllocationDataList.isEmpty()){

            Set<Integer> collectMapIds = new HashSet<>();

            String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
            int startDateOfMonth = 1;

            Month month = Month.valueOf(startMonth.toUpperCase());
            LocalDate customStartDate = LocalDate.of(financialYear, month, startDateOfMonth);
            LocalDate customEndDate = customStartDate.plusYears(1).minusDays(1);
//            Date financialYearStartDate = java.sql.Date.valueOf(customStartDate);
//            Date financialYearEndDate = java.sql.Date.valueOf(customEndDate);

            Set<Integer> collectResourceId = new HashSet<>();
            Set<Integer> collectProjectId = new HashSet<>();

//            collect all the resource id and project id
            for(PostProjectResourceAllocationData eachData: postProjectResourceAllocationDataList){
                if(eachData.getResourceId()!=null) {
                    collectResourceId.add(eachData.getResourceId());
                }
                collectProjectId.add(eachData.getProjectId());
            }
            List<ResourceMgmt> listOfResourceId = resourceMgmtRepo.findAllById(collectResourceId);

            String projectAnalysisQuery = ProjectUtils.getFile("getProjectStartEndDates.txt");
            Query query = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
            ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ProjectStartEndDate.class));

            query.setParameter("projectIds", collectProjectId);

            List<ProjectStartEndDate> projectStartEndDateList = query.getResultList();


            Map<Integer, String> resourceNameIdMap = new HashMap<>();
            for(ResourceMgmt eachResource: listOfResourceId){
                String firstName = eachResource.getFirstName();
                String lastName = eachResource.getLastName();
                if(lastName!=null && !lastName.isEmpty()){
                    resourceNameIdMap.put(eachResource.getResourceId(), String.join(" ", firstName, lastName));
                }
                else{
                    resourceNameIdMap.put(eachResource.getResourceId(), firstName);
                }
            }
            collectResourceId = listOfResourceId.stream().map(ResourceMgmt::getResourceId).collect(Collectors.toSet());
            collectProjectId = projectStartEndDateList.stream().map(ProjectStartEndDate::getProject_id).collect(Collectors.toSet());

            List<ProjectResourceMapping> allocationDataToUpdate = new ArrayList<>();
            List<PostProjectResourceAllocationData> allocationDataToInsert = new ArrayList<>();
            List<RequestedResources> requestedResourcesList = new ArrayList<>();
            List<ProjectResourceMappingExtended> dailyAllocationDataToUpsert = new ArrayList<>();

//            check if these resource id and project id exists in database
//            check if daily allocations is null or empty
//            check if allocation start date is before or equal to end date

            List<String> overlappingDateRangeResourceName = new ArrayList<>();
            for(PostProjectResourceAllocationData eachData: postProjectResourceAllocationDataList){
                if(collectResourceId.contains(eachData.getResourceId()) && collectProjectId.contains(eachData.getProjectId())
                        && !ProjectUtils.isEndDateBeforeStartDate(eachData.getAllocationStartDate(), eachData.getAllocationEndDate())) {

//                    if no map id, make it zero
                    if (eachData.getMapId() == null) {
                        eachData.setMapId(0);
                    }
                    List<ProjectResourceMapping> overlappingAllocations = projectResourceMappingRepo.getOverlappingAllocations(eachData.getMapId(), eachData.getProjectId(),
                            eachData.getResourceId(), eachData.getAllocationStartDate(), eachData.getAllocationEndDate());
                    if(overlappingAllocations!=null && !overlappingAllocations.isEmpty()){
                        overlappingDateRangeResourceName.add(resourceNameIdMap.get(eachData.getResourceId()));
                    }
                }
            }
//            if resource allocation is overlapping
            if(!overlappingDateRangeResourceName.isEmpty()){
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                error.setMessage(ProjectUtils.returnOverlappingDateRangeErrorMessage(overlappingDateRangeResourceName));
                return error;
            }

            Map<Integer, ProjectStartEndDate> projectStartEndDateListToMap
                    = projectStartEndDateList.stream().collect(Collectors.toMap(ProjectStartEndDate::getProject_id, startEndDate->startEndDate));

            for(PostProjectResourceAllocationData eachData: postProjectResourceAllocationDataList){

//                TODO::add checks for data which will be inserted or updated to requested_resource table as well

//                eachData.setAllocationStartDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachData.getAllocationStartDate()));
//                eachData.setAllocationEndDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachData.getAllocationEndDate()));

                if(collectProjectId.contains(eachData.getProjectId())
                        && !ProjectUtils.isEndDateBeforeStartDate(eachData.getAllocationStartDate(), eachData.getAllocationEndDate())){

                    if(!collectResourceId.contains(eachData.getResourceId())){
                        eachData.setResourceId(null);
                    }

//                    apply check that allocation updated has the allocation date range inside the project date range

//                    remove time data from the response date range
                    LocalDate allocStartDate, allocEndDate;
                    allocStartDate = new java.sql.Date(eachData.getAllocationStartDate().getTime()).toLocalDate();
                    allocEndDate = new java.sql.Date(eachData.getAllocationEndDate().getTime()).toLocalDate();
                    eachData.setAllocationStartDate(java.sql.Date.valueOf(allocStartDate));
                    eachData.setAllocationEndDate(java.sql.Date.valueOf(allocEndDate));

                    ProjectStartEndDate currentProject = projectStartEndDateListToMap.get(eachData.getProjectId());
                    if((eachData.getAllocationStartDate().compareTo(currentProject.getProject_start_date())<0)
                            || (eachData.getAllocationEndDate().compareTo(currentProject.getProject_end_date())>0)){
                        String message = ProjectUtils.ALLOCATION_OUTSIDE_DATE_RANGE;
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }

//                    if(eachData.getAllocations()==null || eachData.getAllocations().isEmpty()){
//                        List<AllResourceActiveAllocation> emptyAllocations= new ArrayList<>();
//                        LocalDate startDate = new java.sql.Date(eachData.getAllocationStartDate().getTime()).toLocalDate();
//                        LocalDate endDate = new java.sql.Date(eachData.getAllocationEndDate().getTime()).toLocalDate();
//                        while(!startDate.isAfter(endDate)){
//                            if(startDate.getDayOfWeek()!= DayOfWeek.SATURDAY && startDate.getDayOfWeek()!= DayOfWeek.SUNDAY) {
//                                Date currentDate = java.sql.Date.valueOf(startDate);
//                                AllResourceActiveAllocation newAllocation = new AllResourceActiveAllocation(null, null, currentDate, BigDecimal.valueOf(0d));
//                                emptyAllocations.add(newAllocation);
//                            }
//                            startDate = startDate.plusDays(1);
//                        }
//                        eachData.setAllocations(emptyAllocations);
//                    }

//                    make sure data for every date inside allocation date range is present
                    HashSet<Date> dateRecordTracker = new HashSet<>();
                    List<AllResourceActiveAllocation> emptyAllocations = new ArrayList<>();
                    if(eachData.getAllocations()!=null && !eachData.getAllocations().isEmpty()){

//                        remove time data from allocationDate and keep track of all the dates in the list,
//                        to make sure that all the inside date range records are present

                        for(AllResourceActiveAllocation eachAllocation: eachData.getAllocations()){
                            LocalDate localDate = new java.sql.Date(eachAllocation.getAllocationDate().getTime()).toLocalDate();
                            eachAllocation.setAllocationDate(java.sql.Date.valueOf(localDate));

                            dateRecordTracker.add(eachAllocation.getAllocationDate());
                            emptyAllocations.add(eachAllocation);
                        }
                    }

//                    get the start date, whether financial year start or project start, which is later
                    LocalDate allocationStartDate = new java.sql.Date(eachData.getAllocationStartDate().getTime()).toLocalDate();
                    if(customStartDate.isAfter(allocationStartDate)){
                        allocationStartDate = customStartDate;
                    }
//                    get the start date, whether financial year start or project start, which is later
                    LocalDate allocationEndDate = new java.sql.Date(eachData.getAllocationEndDate().getTime()).toLocalDate();
                    if(allocationEndDate.isAfter(customEndDate)){
                        allocationEndDate = customEndDate;
                    }
                    while(!allocationStartDate.isAfter(allocationEndDate)){
                        if(allocationStartDate.getDayOfWeek()!=DayOfWeek.SATURDAY && allocationStartDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
                            Date currentDate = java.sql.Date.valueOf(allocationStartDate);
                            if(!dateRecordTracker.contains(currentDate)){
                                emptyAllocations.add(new AllResourceActiveAllocation(null, eachData.getMapId(), currentDate, BigDecimal.valueOf(0d), null, null));
                            }
                        }
                        allocationStartDate = allocationStartDate.plusDays(1);
                    }
                    eachData.setAllocations(emptyAllocations);

                    Optional<ProjectResourceMapping> storedAllocation = projectResourceMappingRepo.findById(eachData.getMapId());

//                    if map id does not exist in database, make map id = null and insert it to list
                    if (!storedAllocation.isPresent()) {
                        eachData.setMapId(null);
                        eachData.setCreatedBy(modifiedBy);
                        eachData.setCreatedDate(new Date());
                        eachData.setModifiedBy(modifiedBy);
                        eachData.setModifiedDate(new Date());
//                        insert the request to the resource request first
                        RequestedResources requestedResources = requestedResourcesRepo.save(new RequestedResources(eachData));
                        eachData.setRequestId(requestedResources.getRequestId());
                        allocationDataToInsert.add(eachData);
                    }
                    else {
                        collectMapIds.add(eachData.getMapId());
                        eachData.setCreatedBy(storedAllocation.get().getCreatedBy());
                        eachData.setCreatedDate(storedAllocation.get().getCreatedDate());
                        eachData.setModifiedBy(modifiedBy);
                        eachData.setModifiedDate(new Date());

//                        collect the daily allocation data from the response
                        List<AllResourceActiveAllocation> thisRecordAllocations = eachData.getAllocations();
//                        boolean isInsideDateRangeRecordNotPresent = true;
//                        for each allocation
                        for(AllResourceActiveAllocation eachDailyAllocation: thisRecordAllocations){

//                            eachDailyAllocation.setAllocationDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachDailyAllocation.getAllocationDate()));
                            LocalDate currentDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();

//                            check if the date is on weekend, if nop, then proceed
                            if((currentDate.getDayOfWeek()!= DayOfWeek.SATURDAY) && (currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY)) {

//                                check if the daily allocation data date lies in between the allocation start date and allocation end date
                                if ((eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationEndDate()) <= 0)
                                        && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationStartDate()) >= 0)) {

//                                    if mapId is not matching with the record mapId, or if not present, discard the data,
//                                    or if it already has id, discard as well, as no data change is allowed from here
                                    if (Objects.equals(eachDailyAllocation.getMapId(), eachData.getMapId())) {
//                                        if(isInsideDateRangeRecordNotPresent) {
//                                            isInsideDateRangeRecordNotPresent = false;
//                                        }
                                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                                    }
                                } else {
//                                    if no id and still outside allocation date range, ignore
//                                    if is present, but outside allocation date range, make it zero
                                    if (eachDailyAllocation.getId() != null && Objects.equals(eachDailyAllocation.getMapId(), eachData.getMapId())) {
                                        eachDailyAllocation.setRequestedFte(BigDecimal.valueOf(0d));
                                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                                    }
                                }
                            }
                        }

//                        add the updated data to the list
                        allocationDataToUpdate.add(new ProjectResourceMapping(eachData));
                        requestedResourcesList.add(new RequestedResources(eachData));
//                        if(isInsideDateRangeRecordNotPresent){
//                            LocalDate localStartDate = new java.sql.Date(eachData.getAllocationStartDate().getTime()).toLocalDate();
//                            LocalDate localEndDate = new java.sql.Date(eachData.getAllocationEndDate().getTime()).toLocalDate();
//                            while(!localStartDate.isAfter(localEndDate)){
//                                if(localStartDate.getDayOfWeek()!= DayOfWeek.SATURDAY && localStartDate.getDayOfWeek()!= DayOfWeek.SUNDAY) {
//                                    Date currentDate = java.sql.Date.valueOf(localStartDate);
//                                    ProjectResourceMappingExtended newAllocation = new ProjectResourceMappingExtended(null, eachData.getMapId(), currentDate, BigDecimal.valueOf(0d));
//                                    dailyAllocationDataToUpsert.add(newAllocation);
//                                }
//                                localStartDate = localStartDate.plusDays(1);
//                            }
//                        }
                    }
                }
            }

//            update records with map id
            if(!allocationDataToUpdate.isEmpty()) {
//                update project resource allocations
                requestedResourcesRepo.saveAll(requestedResourcesList);
//                update requested resource table for new changes, if any
                projectResourceMappingRepo.saveAll(allocationDataToUpdate);
            }

//            for new records, with no map id
            for(PostProjectResourceAllocationData eachData: allocationDataToInsert){

//                insert the record to get map id
                ProjectResourceMapping eachDataWithMapId = projectResourceMappingRepo.save(new ProjectResourceMapping(eachData));
                collectMapIds.add(eachDataWithMapId.getMapId());

//                get allocations
                List<AllResourceActiveAllocation> thisRecordAllocations = eachData.getAllocations();

                for(AllResourceActiveAllocation eachDailyAllocation: thisRecordAllocations){

//                    eachDailyAllocation.setAllocationDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachDailyAllocation.getAllocationDate()));
                    LocalDate currentDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();

//                    for each allocation, ignore those which lie on weekend, and those which are outside date range
                    if((currentDate.getDayOfWeek()!= DayOfWeek.SATURDAY) && (currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY)
                            && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationEndDate()) <= 0)
                            && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationStartDate()) >= 0)) {

//                        set map id
                        eachDailyAllocation.setMapId(eachDataWithMapId.getMapId());
//                        add to list
                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                    }
                }

            }

//            upsert list
            if(!dailyAllocationDataToUpsert.isEmpty()){
                projectResourceMappingExtendedRepo.saveAll(dailyAllocationDataToUpsert);
            }
        }

        return true;
    }*/

    public Object createUpdateKanbanDataNew(List<PostProjectResourceAllocationData> postProjectResourceAllocationDataList,
                                            Integer financialYear, String allocationFrequency, String modifiedBy) {

    //        check if response data is empty
        if(postProjectResourceAllocationDataList!=null && !postProjectResourceAllocationDataList.isEmpty()){

            String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
            int startDateOfMonth = 1;

            Month month = Month.valueOf(startMonth.toUpperCase());
            LocalDate customStartDate = LocalDate.of(financialYear, month, startDateOfMonth);
            LocalDate customEndDate = customStartDate.plusYears(1).minusDays(1);
        //            Date financialYearStartDate = java.sql.Date.valueOf(customStartDate);
        //            Date financialYearEndDate = java.sql.Date.valueOf(customEndDate);

            Set<Integer> collectResourceId = new HashSet<>();
            Set<Integer> collectProjectId = new HashSet<>();

        //            collect all the resource id and project id
            for(PostProjectResourceAllocationData eachData: postProjectResourceAllocationDataList){
                if(eachData.getResourceId()!=null) {
                    collectResourceId.add(eachData.getResourceId());
                }
                collectProjectId.add(eachData.getProjectId());
            }
            List<ResourceMgmt> listOfResourceId = resourceMgmtRepo.findAllById(collectResourceId);

            String projectAnalysisQuery = ProjectUtils.getFile("getProjectStartEndDates.txt");
            Query query = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
            ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ProjectStartEndDate.class));

            query.setParameter("projectIds", collectProjectId);

            List<ProjectStartEndDate> projectStartEndDateList = query.getResultList();


            Map<Integer, String> resourceNameIdMap = new HashMap<>();
            Map<Integer, ResourceMgmt> resourceDetailsMap = new HashMap<>();
            for(ResourceMgmt eachResource: listOfResourceId){
//                to create map of resource Id and its details
                resourceDetailsMap.put(eachResource.getResourceId(), eachResource);

                String firstName = eachResource.getFirstName();
                String lastName = eachResource.getLastName();
                if(lastName!=null && !lastName.isEmpty()){
                    resourceNameIdMap.put(eachResource.getResourceId(), String.join(" ", firstName, lastName));
                }
                else{
                    resourceNameIdMap.put(eachResource.getResourceId(), firstName);
                }
                collectResourceId.add(eachResource.getResourceId());
            }
//            collectResourceId = listOfResourceId.stream().map(ResourceMgmt::getResourceId).collect(Collectors.toSet());

            Map<Integer, ProjectStartEndDate> projectStartEndDateListToMap = new HashMap<>();
            for(ProjectStartEndDate eachProject: projectStartEndDateList){
                collectProjectId.add(eachProject.getProject_id());
                projectStartEndDateListToMap.put(eachProject.getProject_id(), eachProject);
            }
//            collectProjectId = projectStartEndDateList.stream().map(ProjectStartEndDate::getProject_id).collect(Collectors.toSet());

//            List<ProjectResourceMapping> allocationDataToUpdate = new ArrayList<>();
            List<PostProjectResourceAllocationData> allocationDataToInsert = new ArrayList<>();
//            List<RequestedResources> requestedResourcesList = new ArrayList<>();
//            List<ProjectResourceMappingExtended> dailyAllocationDataToUpsert = new ArrayList<>();

        //            check if these resource id and project id exists in database
        //            check if daily allocations is null or empty
        //            check if allocation start date is before or equal to end date

            List<String> overlappingDateRangeResourceName = new ArrayList<>();
            for(PostProjectResourceAllocationData eachData: postProjectResourceAllocationDataList){
                if(collectResourceId.contains(eachData.getResourceId()) && collectProjectId.contains(eachData.getProjectId())
                        && !ProjectUtils.isEndDateBeforeStartDate(eachData.getAllocationStartDate(), eachData.getAllocationEndDate())) {

        //                    if no map id, make it zero
                    if (eachData.getMapId() == null) {
                        eachData.setMapId(0);
                    }
                    List<ProjectResourceMapping> overlappingAllocations = projectResourceMappingRepo.getOverlappingAllocations(eachData.getMapId(), eachData.getProjectId(),
                            eachData.getResourceId(), eachData.getAllocationStartDate(), eachData.getAllocationEndDate());
                    if(overlappingAllocations!=null && !overlappingAllocations.isEmpty()){
                        overlappingDateRangeResourceName.add(resourceNameIdMap.get(eachData.getResourceId()));
                    }
                }
            }
        //            if resource allocation is overlapping
            if(!overlappingDateRangeResourceName.isEmpty()){
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                error.setMessage(ProjectUtils.returnOverlappingDateRangeErrorMessage(overlappingDateRangeResourceName));
                return error;
            }

            for(PostProjectResourceAllocationData eachData: postProjectResourceAllocationDataList){

        //                TODO::add checks for data which will be inserted or updated to requested_resource table as well

        //                eachData.setAllocationStartDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachData.getAllocationStartDate()));
        //                eachData.setAllocationEndDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachData.getAllocationEndDate()));

                if(collectProjectId.contains(eachData.getProjectId())
                        && !ProjectUtils.isEndDateBeforeStartDate(eachData.getAllocationStartDate(), eachData.getAllocationEndDate())){

                    if(eachData.getMapId()==null ||  eachData.getMapId().equals(0)){
                        eachData.setMapId(null);
                    }

//                  set resource allocation date to common interval between project duration (start date and end date) and resource duration (date of join and last working date)
                    Date projectStartdate = projectStartEndDateListToMap.get(eachData.getProjectId()).getProject_start_date();
                    Date projectEnddate = projectStartEndDateListToMap.get(eachData.getProjectId()).getProject_end_date();

                    Date resourceDateOfJoin = null, resourcelastWorkingDate = null;
                    if(eachData.getResourceId()!=null){
                        resourceDateOfJoin = resourceDetailsMap.get(eachData.getResourceId()).getDateOfJoin();
                        if(resourceDateOfJoin!=null){
                            resourceDateOfJoin = java.sql.Date.valueOf(new java.sql.Date(resourceDateOfJoin.getTime()).toLocalDate());
                        }
                        resourcelastWorkingDate = resourceDetailsMap.get(eachData.getResourceId()).getLastWorkingDate();
                        if(resourcelastWorkingDate!=null){
                            resourcelastWorkingDate = java.sql.Date.valueOf(new java.sql.Date(resourcelastWorkingDate.getTime()).toLocalDate());
                        }
                    }

                    if(resourcelastWorkingDate!=null && resourcelastWorkingDate.compareTo(projectStartdate)<0){
                        String message = ProjectUtils.RESOURCE_LEFT_BEFORE_PROJECT_START_DATE;
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }

                    if(resourceDateOfJoin!=null && resourceDateOfJoin.compareTo(projectEnddate)>0){
                        String message = ProjectUtils.RESOURCE_JOINED_AFTER_PROJECT_END_DATE;
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }

                    Date resourceAllocationStartDate = projectStartdate, resourceAllocationEndDate = projectEnddate;
                    if(resourceDateOfJoin!=null && resourceDateOfJoin.compareTo(projectStartdate)>0){
                        resourceAllocationStartDate = resourceDateOfJoin;
                    }
                    if(resourcelastWorkingDate!=null && resourcelastWorkingDate.compareTo(projectEnddate)<0){
                        resourceAllocationEndDate = resourcelastWorkingDate;
                    }
                    if(eachData.getResourceId()!=null) {
                        eachData.setAllocationStartDate(resourceAllocationStartDate);
                        eachData.setAllocationEndDate(resourceAllocationEndDate);
                    }
//                    resource project allocation date assignment done

                    List<ProjectResourceMappingExtended> dailyAllocationDataToUpsert = new ArrayList<>();

                    if(!collectResourceId.contains(eachData.getResourceId())){
                        eachData.setResourceId(null);
                    }

//                    remove time data from the response date range
                    LocalDate allocStartDate, allocEndDate;
                    allocStartDate = new java.sql.Date(eachData.getAllocationStartDate().getTime()).toLocalDate();
                    allocEndDate = new java.sql.Date(eachData.getAllocationEndDate().getTime()).toLocalDate();
                    eachData.setAllocationStartDate(java.sql.Date.valueOf(allocStartDate));
                    eachData.setAllocationEndDate(java.sql.Date.valueOf(allocEndDate));

                    if((eachData.getAllocationStartDate().compareTo(projectStartdate)<0)
                            || (eachData.getAllocationEndDate().compareTo(projectEnddate)>0)){
                        String message = ProjectUtils.ALLOCATION_OUTSIDE_DATE_RANGE;
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }

        //                    make sure data for every date inside allocation date range is present
                    HashSet<Date> dateRecordTracker = new HashSet<>();
                    List<AllResourceActiveAllocation> emptyAllocations = new ArrayList<>();

                    List<AllResourceActiveAllocation> allAllocations = new ArrayList<>();
                    if(allocationFrequency.equals(ProjectUtils.ALLOCATION_FREQUENCY_DAILY)){
                        if(eachData.getAllocations()!=null) {
                            allAllocations.addAll(eachData.getAllocations());
                        }
                    }
                    else if(allocationFrequency.equals(ProjectUtils.ALLOCATION_FREQUENCY_WEEKLY)){
                        if(eachData.getWeeklyAllocationData()!=null) {
                            for (AllocationDataByWeek eachWeek : eachData.getWeeklyAllocationData()) {
                                if (eachWeek.getWeeklyAllocations() != null) {
                                    allAllocations.addAll(eachWeek.getWeeklyAllocations());
                                }
                            }
                        }
                    }
                    else if(allocationFrequency.equals(ProjectUtils.ALLOCATION_FREQUENCY_MONTHLY)){
                        if(eachData.getMonthlyAllocationData()!=null) {
                            for (AllocationDataByMonth eachMonth : eachData.getMonthlyAllocationData()) {
                                if (eachMonth.getMonthlyAllocations() != null) {
                                    allAllocations.addAll(eachMonth.getMonthlyAllocations());
                                }
                            }
                        }
                    }


                    if(!allAllocations.isEmpty()){

        //                        remove time data from allocationDate and keep track of all the dates in the list,
        //                        to make sure that all the inside date range records are present

                        for(AllResourceActiveAllocation eachAllocation: allAllocations){
                            LocalDate localDate = new java.sql.Date(eachAllocation.getAllocationDate().getTime()).toLocalDate();
                            eachAllocation.setAllocationDate(java.sql.Date.valueOf(localDate));

                            dateRecordTracker.add(eachAllocation.getAllocationDate());
                            emptyAllocations.add(eachAllocation);
                        }
                    }

                    if(allocationFrequency.equals(ProjectUtils.ALLOCATION_FREQUENCY_WEEKLY)){
                        customStartDate = customStartDate.with(DayOfWeek.MONDAY);
                        customEndDate = customEndDate.with(DayOfWeek.SUNDAY);
                    }

        //                    get the start date, whether financial year start or project start, which is later
                    LocalDate allocationStartDate = new java.sql.Date(eachData.getAllocationStartDate().getTime()).toLocalDate();
                    if(eachData.getMapId()!=null && customStartDate.isAfter(allocationStartDate)){
                        allocationStartDate = customStartDate;
                    }
        //                    get the start date, whether financial year start or project start, which is later
                    LocalDate allocationEndDate = new java.sql.Date(eachData.getAllocationEndDate().getTime()).toLocalDate();
                    if(eachData.getMapId()!=null && allocationEndDate.isAfter(customEndDate)){
                        allocationEndDate = customEndDate;
                    }
                    while(!allocationStartDate.isAfter(allocationEndDate)){
                        if(allocationStartDate.getDayOfWeek()!=DayOfWeek.SATURDAY && allocationStartDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
                            Date currentDate = java.sql.Date.valueOf(allocationStartDate);
                            if(!dateRecordTracker.contains(currentDate)){
                                emptyAllocations.add(new AllResourceActiveAllocation(null, eachData.getMapId(), currentDate, BigDecimal.valueOf(0d), BigDecimal.valueOf(0), 0d));
                            }
                        }
                        allocationStartDate = allocationStartDate.plusDays(1);
                    }
                    allAllocations = new ArrayList<>(emptyAllocations);
                    eachData.setAllocations(allAllocations);

                    if(eachData.getMapId()==null) {
                        eachData.setMapId(0);
                    }
                    Optional<ProjectResourceMapping> storedAllocation = projectResourceMappingRepo.findById(eachData.getMapId());

        //                    if map id does not exist in database, make map id = null and insert it to list
                    if (!storedAllocation.isPresent()) {
                        eachData.setMapId(null);
                        allocationDataToInsert.add(eachData);
                    }
                    else {
        //                        collect the daily allocation data from the response
        //                        boolean isInsideDateRangeRecordNotPresent = true;
        //                        for each allocation
                        for(AllResourceActiveAllocation eachDailyAllocation: allAllocations){

        //                            eachDailyAllocation.setAllocationDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachDailyAllocation.getAllocationDate()));
                            LocalDate currentDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();

        //                            check if the date is on weekend, if nop, then proceed
                            if((currentDate.getDayOfWeek()!= DayOfWeek.SATURDAY) && (currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY)) {

        //                                check if the daily allocation data date lies in between the allocation start date and allocation end date
                                if ((eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationEndDate()) <= 0)
                                        && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationStartDate()) >= 0)) {

        //                                    if mapId is not matching with the record mapId, or if not present, discard the data,
        //                                    or if it already has id, discard as well, as no data change is allowed from here
                                    if (Objects.equals(eachDailyAllocation.getMapId(), eachData.getMapId())) {
        //                                        if(isInsideDateRangeRecordNotPresent) {
        //                                            isInsideDateRangeRecordNotPresent = false;
        //                                        }
                                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                                    }
                                } else {
        //                                    if no id and still outside allocation date range, ignore
        //                                    if is present, but outside allocation date range, make it zero
                                    if (eachDailyAllocation.getId() != null && Objects.equals(eachDailyAllocation.getMapId(), eachData.getMapId())) {
                                        eachDailyAllocation.setRequestedFte(BigDecimal.valueOf(0d));
                                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                                    }
                                }
                            }
                        }

        //                        add the updated data to the list
                        projectResourceMappingExtendedRepo.saveAll(dailyAllocationDataToUpsert);

                        Integer workingDays
                                = ProjectUtils.numberOfWorkingDays(new java.sql.Date(storedAllocation.get().getAllocationStartDate().getTime()).toLocalDate(),
                                new java.sql.Date(storedAllocation.get().getAllocationEndDate().getTime()).toLocalDate());

                        Double requestedFteAvg = projectResourceMappingRepo.getTotalRequestedFte(eachData.getMapId()).doubleValue();
                        requestedFteAvg = ProjectUtils.roundToTwoDecimalPlace(requestedFteAvg/workingDays);

                        ProjectResourceMapping currentRecord = storedAllocation.get();
                        currentRecord.setModifiedDate(new Date());
                        currentRecord.setModifiedBy(modifiedBy);
                        String pmLastAction = null;
                        String requestStatus = null;

                        if(currentRecord.getRequestedFteAvg()==null){
                            currentRecord.setRequestedFteAvg(0d);
                        }
                        if(currentRecord.getAllocatedAvgFte()==null){
                            currentRecord.setAllocatedAvgFte(0d);
                        }

                        if(currentRecord.getRequestedFteAvg()>requestedFteAvg){
                            pmLastAction = ProjectUtils.REQUEST_RESOURCE_AVG_REQUESTED_FTE_DECREASED + (ProjectUtils.roundToTwoDecimalPlace(currentRecord.getRequestedFteAvg()-requestedFteAvg));
                        }
                        else if(currentRecord.getRequestedFteAvg()<requestedFteAvg){
                            pmLastAction = ProjectUtils.REQUEST_RESOURCE_AVG_REQUESTED_FTE_INCREASED + (ProjectUtils.roundToTwoDecimalPlace(requestedFteAvg - currentRecord.getRequestedFteAvg()));
                        }
                        currentRecord.setRequestedFteAvg(requestedFteAvg);
                        if(currentRecord.getRequestedFteAvg()>currentRecord.getAllocatedAvgFte()){
                            requestStatus = ProjectUtils.REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED;
                        }
                        else if(currentRecord.getRequestedFteAvg()<currentRecord.getAllocatedAvgFte()){
                            requestStatus = ProjectUtils.REQUEST_RESOURCE_STATUS_OVER_FULFILLED;
                        }
                        else{
                            requestStatus = ProjectUtils.REQUEST_RESOURCE_STATUS_COMPLETELY_FULFILLED;
                        }

                        projectResourceMappingRepo.save(currentRecord);
                        Optional<RequestedResources> storedRequest = requestedResourcesRepo.findById(currentRecord.getRequestId());
                        if(storedRequest.isPresent()){
                            RequestedResources requestedResources = storedRequest.get();
                            requestedResources.setModifiedBy(modifiedBy);
                            requestedResources.setModifiedDate(new Date());
                            if(pmLastAction!=null){
                                if(requestedResources.getRmLastAction()!=null && !requestedResources.getRmLastAction().isEmpty()) {
                                    requestedResources.setPmLastAction(pmLastAction);
                                }
                                if(!requestedResources.getRequestStatus().equals(ProjectUtils.REQUEST_RESOURCE_STATUS_OPEN)) {
                                    requestedResources.setNotify(requestStatus.equals(ProjectUtils.REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED));
                                    requestedResources.setRequestStatus(requestStatus);
                                    if(requestStatus.equals(ProjectUtils.REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED)) {
                                        requestedResources.setViewedByRMIds("");
                                    }
                                }
                                else{
                                    requestedResources.setNotify(true);
                                    requestedResources.setViewedByRMIds("");
                                }
                            }
                            requestedResources.setNotifyPm(false);
                            requestedResourcesRepo.save(requestedResources);
                        }
                        else{
                            Error error = new Error();
                            error.setRequestAt(new Date());
                            error.setStatus(HttpStatus.BAD_REQUEST.value());
                            error.setMessage("Database Error");
                            LOGGER.warn("PRM and RQR table not in sync");
                            return error;
                        }


                    }
                }
            }

        //            for new records, with no map id
            for(PostProjectResourceAllocationData eachData: allocationDataToInsert){

                eachData.setMapId(null);
                eachData.setCreatedBy(modifiedBy);
                eachData.setCreatedDate(new Date());
                eachData.setModifiedBy(modifiedBy);
                eachData.setModifiedDate(new Date());

//                insert the request to the resource request first
                RequestedResources requestedResources = requestedResourcesRepo.save(new RequestedResources(eachData));
                eachData.setRequestId(requestedResources.getRequestId());
                if(eachData.getEmpLocationId().equals("All")){
                    eachData.setEmpLocationId("");
                }
                if(eachData.getEmploymentTypeId().equals("All")){
                    eachData.setEmploymentTypeId("");
                }
                if(eachData.getSkillId().equals("All")){
                    eachData.setSkillId("");
                }

                List<ProjectResourceMappingExtended> dailyAllocationDataToUpsert = new ArrayList<>();

        //                insert the record to get map id
                ProjectResourceMapping newAllocation = new ProjectResourceMapping(eachData);
                newAllocation.setRequestedFteAvg(0.00);
                newAllocation.setAllocatedAvgFte(0.00);

                ProjectResourceMapping eachDataWithMapId = projectResourceMappingRepo.save(newAllocation);

        //                get allocations
                List<AllResourceActiveAllocation> thisRecordAllocations = eachData.getAllocations();

                for(AllResourceActiveAllocation eachDailyAllocation: thisRecordAllocations){

        //                    eachDailyAllocation.setAllocationDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachDailyAllocation.getAllocationDate()));
                    LocalDate currentDate = new java.sql.Date(eachDailyAllocation.getAllocationDate().getTime()).toLocalDate();

        //                    for each allocation, ignore those which lie on weekend, and those which are outside date range
                    if((currentDate.getDayOfWeek()!= DayOfWeek.SATURDAY) && (currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY)
                            && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationEndDate()) <= 0)
                            && (eachDailyAllocation.getAllocationDate().compareTo(eachData.getAllocationStartDate()) >= 0)) {

        //                        set map id
                        eachDailyAllocation.setMapId(eachDataWithMapId.getMapId());
        //                        add to list
                        dailyAllocationDataToUpsert.add(new ProjectResourceMappingExtended(eachDailyAllocation));
                    }
                }

                projectResourceMappingExtendedRepo.saveAll(dailyAllocationDataToUpsert);

                Integer workingDays
                        = ProjectUtils.numberOfWorkingDays(new java.sql.Date(eachDataWithMapId.getAllocationStartDate().getTime()).toLocalDate(),
                        new java.sql.Date(eachDataWithMapId.getAllocationEndDate().getTime()).toLocalDate());

                Double requestedFteAvg = projectResourceMappingRepo.getTotalRequestedFte(eachData.getMapId()).doubleValue();
                requestedFteAvg = ProjectUtils.roundToTwoDecimalPlace(requestedFteAvg/workingDays);

                String pmLastAction = ProjectUtils.REQUEST_RESOURCE_NEW_REQUEST;
                String requestStatus = ProjectUtils.REQUEST_RESOURCE_STATUS_OPEN;

                eachDataWithMapId.setRequestedFteAvg(requestedFteAvg);
                projectResourceMappingRepo.save(eachDataWithMapId);

                requestedResources.setPmLastAction(pmLastAction);
                requestedResources.setNotify(true);
                requestedResources.setNotifyPm(false);
                requestedResources.setRequestStatus(requestStatus);

                requestedResourcesRepo.save(requestedResources);
            }
        }

        return true;
    }

//    this API need to check with time zone
/*
    public GetAllocationLimitsForResourceId getResourceAllocationDataLimitsById(Integer resourceId, Integer mapId, GetDateRange getDateRange){

        if(ProjectUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        Optional<ResourceMgmt> resourceMgmt = resourceMgmtRepo.findById(resourceId);
        if(!resourceMgmt.isPresent()){
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
        }

//        getDateRange.setStartDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getStartDate()));
//        getDateRange.setEndDate(ProjectUtils.convertDateToUTCTimeZoneDate(getDateRange.getEndDate()));

//        bring status list to get max possible fte in a day
        List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();

        double maxFteAllowed = ProjectUtils.MIN_FTE;

        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
            if (eachStatus.getStartValue() !=null && eachStatus.getEndValue()!=null) {
                maxFteAllowed = (maxFteAllowed < eachStatus.getEndValue())?eachStatus.getEndValue():maxFteAllowed;
            }
        }

//        bring all records for the resource
        List<AllResourceActiveAllocation> resourceAllActiveAllocations =
                projectResourceMappingRepo.getResourceByIdActiveAllocation(resourceId, getDateRange.getStartDate(), getDateRange.getEndDate());

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
//        String timeZone = "Asia/Kolkata";

//        go through all records and calculate daily allocation for those which lie in date range,
//        exclude the record for which limit is being calculated
        Map<Date, Double> dailyAllocation = new HashMap<>();
        for(AllResourceActiveAllocation eachAllocation : resourceAllActiveAllocations){

//            eachAllocation.setAllocationDate(ProjectUtils.convertDateToUTCTimeZoneDate(eachAllocation.getAllocationDate()));

            LocalDate currentDate = new java.sql.Date(eachAllocation.getAllocationDate().getTime()).toLocalDate();
            LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
            LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();

            if((!Objects.equals(eachAllocation.getMapId(), mapId)) && !(currentDate.isAfter(endDate)) && !(currentDate.isBefore(startDate))){
//                checks if key is present, if no, adds it, if yes, updates it
                dailyAllocation.compute(eachAllocation.getAllocationDate(), (k, v) -> v == null ? eachAllocation.getFte().doubleValue() : v + eachAllocation.getFte().doubleValue());
            }
        }
        List<MonthAllocationLimit> monthlyLimit = new ArrayList<>();
        List<DailyAllocationLimit> dailyLimit = new ArrayList<>();

        List<GetDateRange> dateRangeList = createDateRangeList(getDateRange);
        assert dateRangeList != null;

        Date dateOfJoin = resourceMgmt.get().getDateOfJoin();
        Date lastWorkingDate = resourceMgmt.get().getLastWorkingDate();

        for(GetDateRange eachRange : dateRangeList){

            Date startDate = eachRange.getStartDate();
            Date endDate = eachRange.getEndDate();

            if(dateOfJoin.compareTo(startDate)>0){
                startDate = dateOfJoin;
            }
            if(lastWorkingDate!=null && lastWorkingDate.compareTo(endDate)<0){
                endDate = lastWorkingDate;
            }

            LocalDate localStartDate = new java.sql.Date(startDate.getTime()).toLocalDate();
            LocalDate localEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();
            Integer workingDays = ProjectUtils.numberOfWorkingDays(localStartDate, localEndDate);
            BigDecimal monthlyAllocatedTillNow =
                    projectResourceMappingRepo.getMonthlyAllocatedTillNow(mapId, resourceId, eachRange.getStartDate(), eachRange.getEndDate());
            Double monthlyMaxAllowed = workingDays.doubleValue() - monthlyAllocatedTillNow.doubleValue();
            monthlyMaxAllowed = ProjectUtils.roundToOneDecimalPlace(monthlyMaxAllowed>0?monthlyMaxAllowed:0);
            monthlyLimit.add(new MonthAllocationLimit(localStartDate.getMonth().toString(), localStartDate.getYear(), monthlyMaxAllowed, workingDays.doubleValue()));
        }

//        for(Map.Entry<Date, Double> entry : dailyAllocation.entrySet()){
//            dailyLimit.put(entry.getKey(), maxFteAllowed - entry.getValue());
//        }

        Date startDate = getDateRange.getStartDate();
        Date endDate = getDateRange.getEndDate();

        if(dateOfJoin.compareTo(startDate)>0){
            startDate = dateOfJoin;
        }
        if(lastWorkingDate!=null && lastWorkingDate.compareTo(endDate)<0){
            endDate = lastWorkingDate;
        }

        LocalDate responseStartDate = new java.sql.Date(startDate.getTime()).toLocalDate();
        LocalDate responseEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();

        while(!responseStartDate.isAfter(responseEndDate)){
            Date currentDate = java.sql.Date.valueOf(responseStartDate);
            Double currentAllocationPossible = dailyAllocation.get(currentDate);
            if(currentAllocationPossible==null){
                currentAllocationPossible = 0d;
            }
            if(responseStartDate.getDayOfWeek() != DayOfWeek.SATURDAY && responseStartDate.getDayOfWeek() != DayOfWeek.SUNDAY){
                dailyLimit.add(new DailyAllocationLimit(currentDate, maxFteAllowed - currentAllocationPossible));
            }
            responseStartDate = responseStartDate.plusDays(1);
        }

        return new GetAllocationLimitsForResourceId(monthlyLimit, dailyLimit);
    }
*/

    public void removeResource(Integer mapId, String modifiedBy) {
        Optional<ProjectResourceMapping> projectResourceMapping = projectResourceMappingRepo.findById(mapId);
        if(projectResourceMapping.isPresent()){
            ProjectResourceMapping updateData = projectResourceMapping.get();
            updateData.setIsDeleted("1");
            updateData.setModifiedBy(modifiedBy);
            updateData.setModifiedDate(new Date());
            Optional<RequestedResources> requestedResources = requestedResourcesRepo.findById(updateData.getRequestId());
            if(requestedResources.isPresent()){
                RequestedResources resources = requestedResources.get();
                resources.setModifiedDate(new Date());
                resources.setModifiedBy(modifiedBy);
                resources.setNotify(false);
                resources.setNotifyPm(false);
                resources.setRequestStatus(ProjectUtils.REQUEST_RESOURCE_STATUS_RELEASED);
                resources.setPmLastAction(ProjectUtils.REQUEST_RESOURCE_RELEASED_FROM_PROJECT);
                resources.setViewedByRMIds("");
                requestedResourcesRepo.save(resources);
            }
            projectResourceMappingRepo.save(updateData);
        }
    }

//    public List<AddResource> getResourceByRolesAndSkills(String roles, String skills) {
//
//        if(Objects.equals(roles, null)){
//            roles="";
//        }
//
//        if(Objects.equals(skills, null)){
//            skills="";
//        }
//        roles = roles.replaceAll("\\s","");
//        skills = skills.replaceAll("\\s","");
//
//        String projectAnalysisQuery = ProjectUtils.getFile("getAddResourceList.txt");
//
//        Query query = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(AddResource.class));
//
//        query.setParameter("roles", roles);
//        query.setParameter("skills", skills);
//
//        List<AddResource> addResourceList = query.getResultList();
//        if(addResourceList==null || addResourceList.isEmpty()){
//            addResourceList=null;
//        }
//        return addResourceList;
//    }

//    public ProjectResourceMapping getByMapId(Integer mapId) {
//        ProjectResourceMapping projectResourceMapping = projectResourceMappingRepo.findByMapId(mapId);
//        if (Objects.isNull(projectResourceMapping)) {
//            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Allocated Resource Not Found");
//        }
//        return projectResourceMapping;
//    }

    private List<GetDateRange> createDateRangeList(GetDateRange getDateRange){

        if(ProjectUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<GetDateRange> dateRange = new ArrayList<>();

        Date startDate = getDateRange.getStartDate(), endDate = getDateRange.getEndDate();

        while(startDate.compareTo(endDate)<=0){
            Calendar calendar = Calendar.getInstance();
//            create month start date
            calendar.setTime(startDate);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            Date start = calendar.getTime();
//            create month end date
            calendar.add(Calendar.MONTH, 1);

//            update start
            startDate = calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date end = calendar.getTime();
            dateRange.add(new GetDateRange(start, end));
        }

        return dateRange;
    }

//    public List<GetRequestResourceByFilters> getAddResourceBySkillRoleId(String roles, String skills, GetDateRange getDateRange) {
//
//        List<GetDateRange> dateRange = createDateRangeList(getDateRange);
//
//        List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
//
//        List<GetRequestResourceByFilters> getResourceBySkillRoleLists = new ArrayList<>();
//
//        double maxFte = ProjectUtils.MIN_FTE;
//
//        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
//            maxFte = (maxFte < eachStatus.getEndValue())?eachStatus.getEndValue():maxFte;
//        }
//
//        if(Objects.equals(roles, null)){
//            roles="";
//        }
//
//        if(Objects.equals(skills, null)){
//            skills="";
//        }
//        roles = roles.replaceAll("\\s","");
//        skills = skills.replaceAll("\\s","");
//
//        String projectAnalysisQuery = ProjectUtils.getFile("getAddResourceList.txt");
//
//        Query query = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(AddResource.class));
//
//        query.setParameter("roles", roles);
//        query.setParameter("skills", skills);
////        update query
//        query.setParameter("startDate", getDateRange.getStartDate());
//        query.setParameter("endDate", getDateRange.getEndDate());
//
//        List<AddResource> addResourceList = query.getResultList();
//
//        Set<Integer> resourceIds = addResourceList.stream().map(AddResource::getResource_id).collect(Collectors.toSet());
//
//        Map<Integer, Double> resourceMaxPossibleAllotment = new HashMap<>();
//
////        check for total fte assignment in each month
//        String resourceMonthlyFteQuery = ProjectUtils.getFile("getResourceMonthlyFteByIds.txt");
//        Query query1 = entityManager.createNativeQuery(resourceMonthlyFteQuery).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(AddResource.class));
//
//        query1.setParameter("resourceIds", resourceIds);
//
//        for(GetDateRange monthRange: dateRange){
//            query1.setParameter("startDate", monthRange.getStartDate());
//            query1.setParameter("endDate", monthRange.getEndDate());
//
//            List<AddResource> resourceMonthlyFteList = query1.getResultList();
//
//            Date fteCalculateStartDate = monthRange.getStartDate().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():monthRange.getStartDate();
//            Date fteCalculateEndDate = monthRange.getEndDate().compareTo(getDateRange.getEndDate())<0?monthRange.getEndDate():getDateRange.getEndDate();
//
//            for(AddResource eachResource: resourceMonthlyFteList){
//
//                LocalDate date1, date2;
//                date1 = new java.sql.Date(monthRange.getStartDate().getTime()).toLocalDate();
//                date2 = new java.sql.Date(monthRange.getEndDate().getTime()).toLocalDate();
//                long monthDateDiff = ChronoUnit.DAYS.between(date1, date2) + 1;
//
//                date1 = new java.sql.Date(fteCalculateStartDate.getTime()).toLocalDate();
//                date2 = new java.sql.Date(fteCalculateEndDate.getTime()).toLocalDate();
//                long assignedDateDiff = ChronoUnit.DAYS.between(date1, date2) + 1;
//
//                double dataFromQuery = eachResource.getResource_avg_fte().doubleValue();
//
//                double maxPossibleAllotment = ProjectUtils.roundToTwoDecimalPlace((monthDateDiff-dataFromQuery)/assignedDateDiff);
//
//                double validMaxPossibleAllotment = Math.max(ProjectUtils.MIN_FTE, maxPossibleAllotment);
//
//                validMaxPossibleAllotment = Math.min(validMaxPossibleAllotment, maxFte);
//
//                resourceMaxPossibleAllotment.merge(eachResource.getResource_id(), validMaxPossibleAllotment, (a, b) -> Math.min(b, a));
//            }
//        }
//
//        for(AddResource eachResource: addResourceList){
//            GetRequestResourceByFilters eachResourceDetail = new GetRequestResourceByFilters();
//            eachResourceDetail.setResource_id(eachResource.getResource_id());
//            eachResourceDetail.setResource_name(eachResource.getResource_name());
//            for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
//                if (eachResource.getResource_avg_fte().doubleValue() >= eachStatus.getStartValue() && eachResource.getResource_avg_fte().doubleValue() < eachStatus.getEndValue()) {
//                    eachResourceDetail.setResource_status(eachStatus.getStatus());
//                    break;
//                }
//            }
//            eachResourceDetail.setMax_fte_possible(resourceMaxPossibleAllotment.get(eachResource.getResource_id()));
//            getResourceBySkillRoleLists.add(eachResourceDetail);
//        }
//
//        if(getResourceBySkillRoleLists.isEmpty()){
//            getResourceBySkillRoleLists=null;
//        }
//        return getResourceBySkillRoleLists;
//    }

/*
    public List<GetRequestResourceByFilters> getAddResourceBySkillRoleId(String roles, String skills, GetDateRange getDateRange) {

        if(ProjectUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

//        List<GetDateRange> dateRange = createDateRangeList(getDateRange);

        Double maxFteAllowedByAdmin = 0d;
        List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
        if(resourceMgmtStatusList!=null && !resourceMgmtStatusList.isEmpty()){
            resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
            for(ResourceMgmtStatus eachStatus: resourceMgmtStatusList){
                if(eachStatus.getIsEnabled() && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                    if(maxFteAllowedByAdmin < eachStatus.getEndValue()){
                        maxFteAllowedByAdmin = eachStatus.getEndValue();
                    }
                }
            }
        }

        if(maxFteAllowedByAdmin<=0){
            maxFteAllowedByAdmin = 1.5d;
        }

        List<GetRequestResourceByFilters> getResourceBySkillRoleLists = new ArrayList<>();

//        double maxFte = ProjectUtils.MIN_FTE;
//
//        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
//            maxFte = (maxFte < eachStatus.getEndValue())?eachStatus.getEndValue():maxFte;
//        }

        if(Objects.equals(roles, null)){
            roles="";
        }

        if(Objects.equals(skills, null)){
            skills="";
        }
        roles = roles.replaceAll("\\s","");
        skills = skills.replaceAll("\\s","");

        String getResourceBySkillRoleQuery = ProjectUtils.getFile("getAddResourceList.txt");

        Query query = entityManager.createNativeQuery(getResourceBySkillRoleQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(AddResource.class));

        query.setParameter("roles", roles);
        query.setParameter("skills", skills);
//        update query
        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());

        List<AddResource> addResourceList = query.getResultList();

//        Set<Integer> resourceIds = addResourceList.stream().map(AddResource::getResource_id).collect(Collectors.toSet());
//
//        Map<Integer, Double> resourceMaxAssignmentInMonth = new HashMap<>();
//
////        check for total fte assignment in each month
//        String resourceMonthlyTotalFteQuery = ProjectUtils.getFile("getResourceMonthlyAllocationByIds.txt");
//        Query query1 = entityManager.createNativeQuery(resourceMonthlyTotalFteQuery).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllocationData.class));
//
//        query1.setParameter("resourceIds", resourceIds);
//
//        for(GetDateRange monthRange: dateRange){
//            query1.setParameter("startDate", monthRange.getStartDate());
//            query1.setParameter("endDate", monthRange.getEndDate());
//
////            get resource allocation of all resources by ids for month
//            List<ResourceAllocationData> resourceMonthlyFteList = query1.getResultList();
//
////            group them by resource id
////            Map<Integer, List<ResourceAllocationData>> groupByResource = resourceMonthlyFteList.stream().collect(Collectors.groupingBy(ResourceAllocationData::getResource_id));
//
//            Date fteCalculateStartDate = monthRange.getStartDate().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():monthRange.getStartDate();
//            Date fteCalculateEndDate = monthRange.getEndDate().compareTo(getDateRange.getEndDate())<0?monthRange.getEndDate():getDateRange.getEndDate();
//
//            LocalDate fteCalculateStartDateLocal, fteCalculateEndDateLocal, monthStartDate, monthEndDate;
//
////            in case the response date range starts from the middle of the month
//            fteCalculateStartDateLocal = new java.sql.Date(fteCalculateStartDate.getTime()).toLocalDate();
//            fteCalculateEndDateLocal = new java.sql.Date(fteCalculateEndDate.getTime()).toLocalDate();
//
//            Integer workingDays = ProjectUtils.numberOfWorkingDays(fteCalculateStartDateLocal, fteCalculateEndDateLocal);
//
//            for(ResourceAllocationData eachResourceData: resourceMonthlyFteList){
//                if(fteCalculateStartDate.compareTo(eachResourceData.getStart_date())!=0 || fteCalculateEndDate.compareTo(eachResourceData.getEnd_date())!=0){
//                    LocalDate localStartDate = new java.sql.Date(eachResourceData.getStart_date().getTime()).toLocalDate();
//                    LocalDate localEndDate = new java.sql.Date(eachResourceData.getEnd_date().getTime()).toLocalDate();
//                    workingDays = ProjectUtils.numberOfWorkingDays(localStartDate, localEndDate);
//                }
//                Double avgFte = ProjectUtils.roundToTwoDecimalPlace(eachResourceData.getFte_worked().doubleValue()/workingDays);
//                resourceMaxAssignmentInMonth.merge(eachResourceData.getResource_id(), avgFte, (a, b) -> Math.max(b, a));
//            }
////            long monthDateDiff = ChronoUnit.DAYS.between(fteCalculateStartDateLocal, fteCalculateEndDateLocal) + 1;
//
////            month start and end date to calculate number of days in the month
////            monthStartDate = new java.sql.Date(monthRange.getStartDate().getTime()).toLocalDate();
////            monthEndDate = new java.sql.Date(monthRange.getEndDate().getTime()).toLocalDate();
////            long numberOfDaysInMonth = ChronoUnit.DAYS.between(monthStartDate, monthEndDate) + 1;
//
////            for(Integer eachResource: resourceIds){
////                List<ResourceAllocationData> monthRecords = groupByResource.get(eachResource);
////                Map<LocalDate, Double> dateMap = new HashMap<>();
////
////                double maxAllottedInADay = 0d;
////                double totalMonthlyFteWorked = 0d;
////
////                LocalDate currentDate, endDate;
////
////                if(monthRecords!=null && !monthRecords.isEmpty()) {
////                    for (ResourceAllocationData eachRecord : monthRecords) {
////
////                        currentDate = new java.sql.Date(eachRecord.getStart_date().getTime()).toLocalDate();
////                        endDate = new java.sql.Date(eachRecord.getEnd_date().getTime()).toLocalDate();
////                        while (!currentDate.isAfter(endDate)) {
////                            dateMap.put(currentDate, dateMap.getOrDefault(currentDate, 0d) + eachRecord.getFte().doubleValue());
////                            if((currentDate.isAfter(fteCalculateStartDateLocal) || currentDate.isEqual(fteCalculateStartDateLocal))
////                                    && (currentDate.isBefore(fteCalculateEndDateLocal) || currentDate.isEqual(fteCalculateEndDateLocal))) {
////                                maxAllottedInADay = maxAllottedInADay < dateMap.get(currentDate) ? dateMap.get(currentDate) : maxAllottedInADay;
////                            }
////                            currentDate = currentDate.plusDays(1);
////                        }
////                        totalMonthlyFteWorked = totalMonthlyFteWorked + eachRecord.getFte_worked().doubleValue();
////                    }
////                }
////                double totalMonthlyFteLeft = numberOfDaysInMonth - totalMonthlyFteWorked;
////                totalMonthlyFteLeft = ProjectUtils.roundToTwoDecimalPlace(totalMonthlyFteLeft/monthDateDiff);
////
////                double maxPossibleAllotmentDayWise = ProjectUtils.roundToTwoDecimalPlace(maxFte-maxAllottedInADay);
////                double validMaxPossibleAllotmentForMonth = Math.max(ProjectUtils.MIN_FTE, maxPossibleAllotmentDayWise);
////                validMaxPossibleAllotmentForMonth = Math.min(validMaxPossibleAllotmentForMonth, Math.min(maxFte,totalMonthlyFteLeft));
////                resourceMaxAssignmentInMonth.merge(eachResource, validMaxPossibleAllotmentForMonth, (a, b) -> Math.min(b, a));
////            }
//        }
        LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
        LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
        Integer workingDays = ProjectUtils.numberOfWorkingDays(startDate, endDate);

        for(AddResource eachResource: addResourceList){
            GetRequestResourceByFilters eachResourceDetail = new GetRequestResourceByFilters();
            eachResourceDetail.setResource_id(eachResource.getResource_id());
            eachResourceDetail.setResource_name(eachResource.getResource_name());
//            Double resourceAlloc = resourceMaxAssignmentInMonth.get(eachResource.getResource_id());
            Double resourceAlloc = eachResource.getSum_fte().doubleValue()/workingDays;
            resourceAlloc = ProjectUtils.roundToOneDecimalPlace(resourceAlloc);

            boolean ifAppropriateStatusDoesNotExist = true;

            Double maxEnabledFteInAdmin = -1d;
            String maxEnabledFteInAdminName = null;

            for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                if(eachStatus.getIsEnabled() && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                    if (resourceAlloc >= eachStatus.getStartValue() && resourceAlloc < eachStatus.getEndValue()) {
                        eachResourceDetail.setResource_status(eachStatus.getStatus());
                        ifAppropriateStatusDoesNotExist = false;
                        break;
                    }
                    if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                        maxEnabledFteInAdmin = eachStatus.getEndValue();
                        maxEnabledFteInAdminName = eachStatus.getStatus();
                    }
                }
            }
            if(ifAppropriateStatusDoesNotExist){
                String resourceStatusIfAbsent = null;
                if(maxEnabledFteInAdminName!=null && resourceAlloc.equals(maxEnabledFteInAdmin)){
                    resourceStatusIfAbsent = maxEnabledFteInAdminName;
                }
                else {
                    resourceStatusIfAbsent = ProjectUtils.UNDEFINED_UTILIZATION_NAME;
                }
                eachResourceDetail.setResource_status(resourceStatusIfAbsent);
            }
            eachResourceDetail.setMax_fte_possible(maxFteAllowedByAdmin);
            getResourceBySkillRoleLists.add(eachResourceDetail);
        }

        if(getResourceBySkillRoleLists.isEmpty()){
            getResourceBySkillRoleLists=null;
        }
        return getResourceBySkillRoleLists;
    }
*/

    public Object getRequestResourceByFilters(PostResourceRequestByFilters postResourceRequestByFilters) {

        LocalDate responseStartDate = new java.sql.Date(postResourceRequestByFilters.getStartDate().getTime()).toLocalDate();
        LocalDate responseEndDate = new java.sql.Date(postResourceRequestByFilters.getEndDate().getTime()).toLocalDate();

        postResourceRequestByFilters.setStartDate(java.sql.Date.valueOf(responseStartDate));
        postResourceRequestByFilters.setEndDate(java.sql.Date.valueOf(responseEndDate));

        if(ProjectUtils.isEndDateBeforeStartDate(postResourceRequestByFilters.getStartDate(), postResourceRequestByFilters.getEndDate())){
            return null;
        }

        String projectAnalysisQuery = ProjectUtils.getFile("getProjectStartEndDates.txt");
        Query query1 = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ProjectStartEndDate.class));

        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(postResourceRequestByFilters.getProjectId());
        query1.setParameter("projectIds", projectIds);
        List<ProjectStartEndDate> projectStartEndDateList = query1.getResultList();

        if(projectStartEndDateList==null || projectStartEndDateList.isEmpty()){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Invalid Project Id");
            return error;
        }

        ProjectStartEndDate currentProjectDates = projectStartEndDateList.get(0);

        if(currentProjectDates.getProject_start_date().compareTo(postResourceRequestByFilters.getStartDate())>0
                || currentProjectDates.getProject_end_date().compareTo(postResourceRequestByFilters.getEndDate())<0){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Search dates are outside project duration");
            return error;
        }

        List<GetRequestResourceByFilters> getRequestResourceByFilters = new ArrayList<>();

        if (Objects.equals(postResourceRequestByFilters.getResourceName(), null)) {
            postResourceRequestByFilters.setResourceName("");
        }

        if (Objects.equals(postResourceRequestByFilters.getRoles(), null)) {
            postResourceRequestByFilters.setRoles("");
        }

        if (Objects.equals(postResourceRequestByFilters.getSkills(), null)) {
            postResourceRequestByFilters.setSkills("");
        }

        if (Objects.equals(postResourceRequestByFilters.getEmpLocation(), null)) {
            postResourceRequestByFilters.setEmpLocation("");
        }

//        if (Objects.equals(location, null)) {
//            location = "";
//        }

        if (Objects.equals(postResourceRequestByFilters.getEmployeeType(), null)) {
            postResourceRequestByFilters.setEmployeeType("");
        }

        postResourceRequestByFilters.setResourceName("%" + postResourceRequestByFilters.getResourceName().replaceAll("\\s", "") + "%");
        postResourceRequestByFilters.setRoles(postResourceRequestByFilters.getRoles().replaceAll("\\s", ""));
        postResourceRequestByFilters.setSkills(postResourceRequestByFilters.getSkills().replaceAll("\\s", ""));
        postResourceRequestByFilters.setEmpLocation(postResourceRequestByFilters.getEmpLocation().replaceAll("\\s", ""));
        postResourceRequestByFilters.setEmployeeType(postResourceRequestByFilters.getEmployeeType().replaceAll("\\s", ""));
//        location = location.replaceAll("\\s", "");

        String getResourceBySkillRoleQuery = ProjectUtils.getFile("getAddResourceList.txt");

        Query query = entityManager.createNativeQuery(getResourceBySkillRoleQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(AddResource.class));

        query.setParameter("resourceName", postResourceRequestByFilters.getResourceName());
        query.setParameter("roles", postResourceRequestByFilters.getRoles());
        query.setParameter("skills", postResourceRequestByFilters.getSkills());
//        query.setParameter("location", location);
        query.setParameter("employeeLocation", postResourceRequestByFilters.getEmpLocation());
        query.setParameter("employeeType", postResourceRequestByFilters.getEmployeeType());
//        update query
        query.setParameter("startDate", postResourceRequestByFilters.getStartDate());
        query.setParameter("endDate", postResourceRequestByFilters.getEndDate());

        List<AddResource> addResourceList = query.getResultList();

        if(addResourceList!=null && !addResourceList.isEmpty()){

            List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
            Double maxFteAllowedByAdmin = 0d;

            if(resourceMgmtStatusList!=null && !resourceMgmtStatusList.isEmpty()) {
//                resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
                for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                    if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                        if (eachStatus.getEndValue() > maxFteAllowedByAdmin) {
                            maxFteAllowedByAdmin = eachStatus.getEndValue();
                        }
                    }
                }
            }

            if(maxFteAllowedByAdmin<=0){
                maxFteAllowedByAdmin = 1.5d;
            }

            List<ProjectResourceMapping> alreadyAddedResources = projectResourceMappingRepo.getAlreadyAddedResourceList(postResourceRequestByFilters.getProjectId());
            Set<Integer> alreadyAddedResourceIds = alreadyAddedResources.stream().map(ProjectResourceMapping::getResourceId).collect(Collectors.toSet());

            List<Integer> resourceIdsToRetrieve = new ArrayList<>();
            for(AddResource eachResource: addResourceList){
                if(!alreadyAddedResourceIds.contains(eachResource.getResource_id())){
                    resourceIdsToRetrieve.add(eachResource.getResource_id());
                }
            }

            List<ResourceDailyFteSumData> allResourceActiveAllocations
                    = projectResourceMappingRepo.getAllAllocationsForResources(resourceIdsToRetrieve, postResourceRequestByFilters.getStartDate(), postResourceRequestByFilters.getEndDate());

            for(int i=0; i<allResourceActiveAllocations.size(); i++){
                ResourceDailyFteSumData eachData = allResourceActiveAllocations.get(i);
                eachData.setAllocationDate(java.sql.Date.valueOf(new java.sql.Date(eachData.getAllocationDate().getTime()).toLocalDate()));
                allResourceActiveAllocations.set(i, eachData);
            }

            Map<Integer, List<ResourceDailyFteSumData>> groupAllocationsByResourceId
                    = allResourceActiveAllocations.stream().collect(Collectors.groupingBy(ResourceDailyFteSumData::getResourceId));

            for(AddResource eachResource: addResourceList){
                if(!alreadyAddedResourceIds.contains(eachResource.getResource_id())) {
                    List<ResourceDailyFteSumData> resourceDailyFteSumDataList = null;
                    if (groupAllocationsByResourceId.containsKey(eachResource.getResource_id())) {
                        resourceDailyFteSumDataList = groupAllocationsByResourceId.get(eachResource.getResource_id());
                    } else {
                        resourceDailyFteSumDataList = new ArrayList<>();
                    }

                    Map<Date, ResourceDailyFteSumData> dateTracker = new HashMap<>();
                    for (ResourceDailyFteSumData eachDate : resourceDailyFteSumDataList) {
                        dateTracker.put(eachDate.getAllocationDate(), eachDate);
                    }

                    LocalDate currentDate = responseStartDate;

                    List<DailyAvailableFteData> dailyAllocations = new ArrayList<>();
//                List<DailyAvailableFteData> weeklyAllocations = new ArrayList<>();
//                List<DailyAvailableFteData> monthlyAllocations = new ArrayList<>();

                    Double weekAvailableSum = 0d;
                    Double monthAvailableSum = 0d;
                    Integer workingDaysInTheMonth = 0, workingDaysInTheWeek = 0;

                    List<ResourceWeeklyFteSumData> weeklyFteSumData = new ArrayList<>();
                    List<ResourceMonthlyFteSumData> monthlyFteSumData = new ArrayList<>();

                    LocalDate weekStartDate = currentDate.with(DayOfWeek.MONDAY);
                    LocalDate weekEndDate = currentDate.with(DayOfWeek.SUNDAY);
                    LocalDate monthStartDate = currentDate.withDayOfMonth(1);
                    LocalDate monthEndDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

                    while (!currentDate.isAfter(responseEndDate)) {
                        Date date = java.sql.Date.valueOf(currentDate);

                        if (currentDate.isAfter(weekEndDate)) {
//                            make all three parameters zero
                            weekAvailableSum = 0d;

                            workingDaysInTheWeek = 0;
//                        weeklyAllocations = new ArrayList<>();
                            weekStartDate = currentDate;
                            weekEndDate = currentDate.with(DayOfWeek.SUNDAY);
                        }

                        if (currentDate.isAfter(monthEndDate)) {
//                            make all three parameters zero
                            monthAvailableSum = 0d;

                            workingDaysInTheMonth = 0;
//                        monthlyAllocations = new ArrayList<>();
                            monthStartDate = currentDate;
                            monthEndDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
                        }

                        if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                            Double currentAvailableFte = 0d;
                            if (dateTracker.containsKey(date)) {
                                currentAvailableFte = maxFteAllowedByAdmin - dateTracker.get(date).getSumFte().doubleValue();
                            } else {
                                currentAvailableFte = maxFteAllowedByAdmin;
                            }
                            DailyAvailableFteData currentData = new DailyAvailableFteData(date, BigDecimal.valueOf(currentAvailableFte));
                            dailyAllocations.add(currentData);
//                        weeklyAllocations.add(currentData);
//                        monthlyAllocations.add(currentData);

                            weekAvailableSum += currentAvailableFte;
                            monthAvailableSum += currentAvailableFte;

                            workingDaysInTheWeek++;
                            workingDaysInTheMonth++;
                        }

                        if (currentDate.isEqual(monthEndDate) || currentDate.isEqual(responseEndDate)) {
                            Double monthAvailableAverage = 0d;
                            if (workingDaysInTheMonth != 0) {
                                monthAvailableAverage = monthAvailableSum / workingDaysInTheMonth;
                            }
                            monthAvailableAverage = ProjectUtils.roundToTwoDecimalPlace(monthAvailableAverage);
                            Date monthStart = java.sql.Date.valueOf(monthStartDate);
                            monthlyFteSumData.add(new ResourceMonthlyFteSumData(monthStart, monthStartDate.getMonth().toString(), monthStartDate.getYear(), monthAvailableAverage));
                        }

                        if (currentDate.isEqual(weekEndDate) || currentDate.isEqual(responseEndDate)) {
                            Double weekAvailableAverage = 0d;
                            if (workingDaysInTheWeek != 0) {
                                weekAvailableAverage = weekAvailableSum / workingDaysInTheWeek;
                            }
                            weekAvailableAverage = ProjectUtils.roundToTwoDecimalPlace(weekAvailableAverage);
                            Date weekStart = java.sql.Date.valueOf(weekStartDate);
                            weeklyFteSumData.add(new ResourceWeeklyFteSumData(weekStart, weekAvailableAverage));
                        }

                        currentDate = currentDate.plusDays(1);
                    }

                    dailyAllocations.sort(Comparator.comparing(DailyAvailableFteData::getAllocationDate));
                    weeklyFteSumData.sort(Comparator.comparing(ResourceWeeklyFteSumData::getWeekStartDate));
                    monthlyFteSumData.sort(Comparator.comparing(ResourceMonthlyFteSumData::getMonthStartDate));

                    getRequestResourceByFilters.add(new GetRequestResourceByFilters(eachResource, dailyAllocations, weeklyFteSumData, monthlyFteSumData));
                }
            }
        }

        if(getRequestResourceByFilters.isEmpty()){
            getRequestResourceByFilters=null;
        }
        else{
            getRequestResourceByFilters.sort(Comparator.comparing(GetRequestResourceByFilters::getResourceName, (r1,r2)->{
                String resource1 = r1.toLowerCase();
                String resource2 = r2.toLowerCase();

                return String.CASE_INSENSITIVE_ORDER.compare(resource1, resource2);
            }));
        }
        return getRequestResourceByFilters;
    }


    private List<Integer> getVisibleResourceIds(String email){

        Set<Integer> listOfVisibleResourceIds = null;

//        get list of all managed department by this resource, if it exists
        List<ResourceMgmt> resourceMgmt = resourceMgmtRepo.getResourceByEmail(email);

//        if resource exists
        if(resourceMgmt!=null && !resourceMgmt.isEmpty()){
            ResourceMgmt currentUser = resourceMgmt.get(0);

            Set<Integer> listOfManagedResourceIds = new HashSet<>();
            listOfManagedResourceIds.add(currentUser.getResourceId());

//            collect list of delegated resources from the resource

            List<DelegatedResourceHistory> delegatedResourceHistoryList = delegatedResourceHistoryRepo.findAll();
            for(DelegatedResourceHistory eachRecord: delegatedResourceHistoryList){
                LocalDate localDate = LocalDate.now();
                Date currentDate = java.sql.Date.valueOf(localDate);
                if(eachRecord.getIsActive() && eachRecord.getDelegatedToResourceId().equals(currentUser.getResourceId())
                        && eachRecord.getDelegationStartDate().compareTo(currentDate)<=0
                        && eachRecord.getDelegationEndDate().compareTo(currentDate)>=0){
                    listOfManagedResourceIds.add(eachRecord.getDelegatedResourceId());
                }
            }

//            get resource hierarchy
            String getResourceHierarchyQuery = ProjectUtils.getFile("getResourceHierarchyQuery.txt");
            Query query1 = entityManager.createNativeQuery(getResourceHierarchyQuery).unwrap(org.hibernate.query.Query.class);
            ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(AncestralPathOfResource.class));
            List<AncestralPathOfResource> getResourceHierarchy = query1.getResultList();

//            add managed department list to visible list
            listOfVisibleResourceIds = new HashSet<>();

//            for each managed department, find all the sub departments and add to visible list
            for (Integer eachResourceId : listOfManagedResourceIds) {
//                get the list of all sub departments from the department hierarchy
                for (int i = 0; i < getResourceHierarchy.size(); i++) {
                    if (getResourceHierarchy.get(i).getResource_id().equals(eachResourceId)) {
                        AncestralPathOfResource topNode = getResourceHierarchy.get(i);
//                        check if current resource is already part of visibleResourceIds as those resource hierarchy have already been added
                        if(!listOfVisibleResourceIds.contains(topNode.getResource_id())) {
                            listOfVisibleResourceIds.add(topNode.getResource_id());
                            Integer depthLevel = topNode.getDepth_order();
                            i++;
                            while (i < getResourceHierarchy.size() && getResourceHierarchy.get(i).getDepth_order() > depthLevel) {
                                if(listOfVisibleResourceIds.contains(getResourceHierarchy.get(i).getResource_id())){
                                    break;
                                }
                                else {
                                    listOfVisibleResourceIds.add(getResourceHierarchy.get(i).getResource_id());
                                    i++;
                                }
                            }
                        }
                    }
                }
            }

//            in case user cannot see his own record, please uncomment this
//            listOfVisibleResourceIds.remove(currentUser.getResourceId());
        }

        if(listOfVisibleResourceIds!=null && !listOfVisibleResourceIds.isEmpty()) {
            return new ArrayList<>(listOfVisibleResourceIds);
        }
        else{
            return new ArrayList<>();
        }
    }
    public Object getRequestResourceByFiltersByRM(PostResourceRequestByFilters postResourceRequestByFilters, String email) {

        LocalDate responseStartDate = new java.sql.Date(postResourceRequestByFilters.getStartDate().getTime()).toLocalDate();
        LocalDate responseEndDate = new java.sql.Date(postResourceRequestByFilters.getEndDate().getTime()).toLocalDate();

        postResourceRequestByFilters.setStartDate(java.sql.Date.valueOf(responseStartDate));
        postResourceRequestByFilters.setEndDate(java.sql.Date.valueOf(responseEndDate));

        if(ProjectUtils.isEndDateBeforeStartDate(postResourceRequestByFilters.getStartDate(), postResourceRequestByFilters.getEndDate())){
            return null;
        }

        String projectAnalysisQuery = ProjectUtils.getFile("getProjectStartEndDates.txt");
        Query query1 = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ProjectStartEndDate.class));

        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(postResourceRequestByFilters.getProjectId());
        query1.setParameter("projectIds", projectIds);
        List<ProjectStartEndDate> projectStartEndDateList = query1.getResultList();

        if(projectStartEndDateList==null || projectStartEndDateList.isEmpty()){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Invalid Project Id");
            return error;
        }

        ProjectStartEndDate currentProjectDates = projectStartEndDateList.get(0);

        if(currentProjectDates.getProject_start_date().compareTo(postResourceRequestByFilters.getStartDate())>0
                || currentProjectDates.getProject_end_date().compareTo(postResourceRequestByFilters.getEndDate())<0){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Search dates are outside project duration");
            return error;
        }

        List<GetRequestResourceByFilters> getRequestResourceByFilters = new ArrayList<>();

        if (Objects.equals(postResourceRequestByFilters.getResourceName(), null)) {
            postResourceRequestByFilters.setResourceName("");
        }

        if (Objects.equals(postResourceRequestByFilters.getRoles(), null)) {
            postResourceRequestByFilters.setRoles("");
        }

        if (Objects.equals(postResourceRequestByFilters.getSkills(), null)) {
            postResourceRequestByFilters.setSkills("");
        }

        if (Objects.equals(postResourceRequestByFilters.getEmpLocation(), null)) {
            postResourceRequestByFilters.setEmpLocation("");
        }

//        if (Objects.equals(location, null)) {
//            location = "";
//        }

        if (Objects.equals(postResourceRequestByFilters.getEmployeeType(), null)) {
            postResourceRequestByFilters.setEmployeeType("");
        }

        postResourceRequestByFilters.setResourceName("%" + postResourceRequestByFilters.getResourceName().replaceAll("\\s", "") + "%");
        postResourceRequestByFilters.setRoles(postResourceRequestByFilters.getRoles().replaceAll("\\s", ""));
        postResourceRequestByFilters.setSkills(postResourceRequestByFilters.getSkills().replaceAll("\\s", ""));
        postResourceRequestByFilters.setEmpLocation(postResourceRequestByFilters.getEmpLocation().replaceAll("\\s", ""));
        postResourceRequestByFilters.setEmployeeType(postResourceRequestByFilters.getEmployeeType().replaceAll("\\s", ""));
//        location = location.replaceAll("\\s", "");

        String getResourceBySkillRoleQuery = ProjectUtils.getFile("getAddResourceListForRM.txt");

        Query query = entityManager.createNativeQuery(getResourceBySkillRoleQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(AddResource.class));

        query.setParameter("resourceName", postResourceRequestByFilters.getResourceName());
        query.setParameter("roles", postResourceRequestByFilters.getRoles());
        query.setParameter("skills", postResourceRequestByFilters.getSkills());
//        query.setParameter("location", location);
        query.setParameter("employeeLocation", postResourceRequestByFilters.getEmpLocation());
        query.setParameter("employeeType", postResourceRequestByFilters.getEmployeeType());
//        update query
        query.setParameter("startDate", postResourceRequestByFilters.getStartDate());
        query.setParameter("endDate", postResourceRequestByFilters.getEndDate());
        query.setParameter("resourceIds", getVisibleResourceIds(email));

        List<AddResource> addResourceList = query.getResultList();

        if(addResourceList!=null && !addResourceList.isEmpty()){

            List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
            Double maxFteAllowedByAdmin = 0d;

            if(resourceMgmtStatusList!=null && !resourceMgmtStatusList.isEmpty()) {
//                resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
                for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                    if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                        if (eachStatus.getEndValue() > maxFteAllowedByAdmin) {
                            maxFteAllowedByAdmin = eachStatus.getEndValue();
                        }
                    }
                }
            }

            if(maxFteAllowedByAdmin<=0){
                maxFteAllowedByAdmin = 1.5d;
            }

            List<ProjectResourceMapping> alreadyAddedResources = projectResourceMappingRepo.getAlreadyAddedResourceList(postResourceRequestByFilters.getProjectId());
            Set<Integer> alreadyAddedResourceIds = alreadyAddedResources.stream().map(ProjectResourceMapping::getResourceId).collect(Collectors.toSet());

            List<Integer> resourceIdsToRetrieve = new ArrayList<>();
            for(AddResource eachResource: addResourceList){
                if(!alreadyAddedResourceIds.contains(eachResource.getResource_id())){
                    resourceIdsToRetrieve.add(eachResource.getResource_id());
                }
            }

            List<ResourceDailyFteSumData> allResourceActiveAllocations
                    = projectResourceMappingRepo.getAllAllocationsForResources(resourceIdsToRetrieve, postResourceRequestByFilters.getStartDate(), postResourceRequestByFilters.getEndDate());

            for(int i=0; i<allResourceActiveAllocations.size(); i++){
                ResourceDailyFteSumData eachData = allResourceActiveAllocations.get(i);
                eachData.setAllocationDate(java.sql.Date.valueOf(new java.sql.Date(eachData.getAllocationDate().getTime()).toLocalDate()));
                allResourceActiveAllocations.set(i, eachData);
            }

            Map<Integer, List<ResourceDailyFteSumData>> groupAllocationsByResourceId
                    = allResourceActiveAllocations.stream().collect(Collectors.groupingBy(ResourceDailyFteSumData::getResourceId));

            for(AddResource eachResource: addResourceList){
                if(!alreadyAddedResourceIds.contains(eachResource.getResource_id())) {
                    List<ResourceDailyFteSumData> resourceDailyFteSumDataList = null;
                    if (groupAllocationsByResourceId.containsKey(eachResource.getResource_id())) {
                        resourceDailyFteSumDataList = groupAllocationsByResourceId.get(eachResource.getResource_id());
                    } else {
                        resourceDailyFteSumDataList = new ArrayList<>();
                    }

                    Map<Date, ResourceDailyFteSumData> dateTracker = new HashMap<>();
                    for (ResourceDailyFteSumData eachDate : resourceDailyFteSumDataList) {
                        dateTracker.put(eachDate.getAllocationDate(), eachDate);
                    }

                    LocalDate currentDate = responseStartDate;

                    List<DailyAvailableFteData> dailyAllocations = new ArrayList<>();
//                List<DailyAvailableFteData> weeklyAllocations = new ArrayList<>();
//                List<DailyAvailableFteData> monthlyAllocations = new ArrayList<>();

                    Double weekAvailableSum = 0d;
                    Double monthAvailableSum = 0d;
                    Integer workingDaysInTheMonth = 0, workingDaysInTheWeek = 0;

                    List<ResourceWeeklyFteSumData> weeklyFteSumData = new ArrayList<>();
                    List<ResourceMonthlyFteSumData> monthlyFteSumData = new ArrayList<>();

                    LocalDate weekStartDate = currentDate.with(DayOfWeek.MONDAY);
                    LocalDate weekEndDate = currentDate.with(DayOfWeek.SUNDAY);
                    LocalDate monthStartDate = currentDate.withDayOfMonth(1);
                    LocalDate monthEndDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

                    while (!currentDate.isAfter(responseEndDate)) {
                        Date date = java.sql.Date.valueOf(currentDate);

                        if (currentDate.isAfter(weekEndDate)) {
//                            make all three parameters zero
                            weekAvailableSum = 0d;

                            workingDaysInTheWeek = 0;
//                        weeklyAllocations = new ArrayList<>();
                            weekStartDate = currentDate;
                            weekEndDate = currentDate.with(DayOfWeek.SUNDAY);
                        }

                        if (currentDate.isAfter(monthEndDate)) {
//                            make all three parameters zero
                            monthAvailableSum = 0d;

                            workingDaysInTheMonth = 0;
//                        monthlyAllocations = new ArrayList<>();
                            monthStartDate = currentDate;
                            monthEndDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
                        }

                        if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                            Double currentAvailableFte = 0d;
                            if (dateTracker.containsKey(date)) {
                                currentAvailableFte = maxFteAllowedByAdmin - dateTracker.get(date).getSumFte().doubleValue();
                            } else {
                                currentAvailableFte = maxFteAllowedByAdmin;
                            }
                            DailyAvailableFteData currentData = new DailyAvailableFteData(date, BigDecimal.valueOf(currentAvailableFte));
                            dailyAllocations.add(currentData);
//                        weeklyAllocations.add(currentData);
//                        monthlyAllocations.add(currentData);

                            weekAvailableSum += currentAvailableFte;
                            monthAvailableSum += currentAvailableFte;

                            workingDaysInTheWeek++;
                            workingDaysInTheMonth++;
                        }

                        if (currentDate.isEqual(monthEndDate) || currentDate.isEqual(responseEndDate)) {
                            Double monthAvailableAverage = 0d;
                            if (workingDaysInTheMonth != 0) {
                                monthAvailableAverage = monthAvailableSum / workingDaysInTheMonth;
                            }
                            monthAvailableAverage = ProjectUtils.roundToTwoDecimalPlace(monthAvailableAverage);
                            Date monthStart = java.sql.Date.valueOf(monthStartDate);
                            monthlyFteSumData.add(new ResourceMonthlyFteSumData(monthStart, monthStartDate.getMonth().toString(), monthStartDate.getYear(), monthAvailableAverage));
                        }

                        if (currentDate.isEqual(weekEndDate) || currentDate.isEqual(responseEndDate)) {
                            Double weekAvailableAverage = 0d;
                            if (workingDaysInTheWeek != 0) {
                                weekAvailableAverage = weekAvailableSum / workingDaysInTheWeek;
                            }
                            weekAvailableAverage = ProjectUtils.roundToTwoDecimalPlace(weekAvailableAverage);
                            Date weekStart = java.sql.Date.valueOf(weekStartDate);
                            weeklyFteSumData.add(new ResourceWeeklyFteSumData(weekStart, weekAvailableAverage));
                        }

                        currentDate = currentDate.plusDays(1);
                    }

                    dailyAllocations.sort(Comparator.comparing(DailyAvailableFteData::getAllocationDate));
                    weeklyFteSumData.sort(Comparator.comparing(ResourceWeeklyFteSumData::getWeekStartDate));
                    monthlyFteSumData.sort(Comparator.comparing(ResourceMonthlyFteSumData::getMonthStartDate));

                    getRequestResourceByFilters.add(new GetRequestResourceByFilters(eachResource, dailyAllocations, weeklyFteSumData, monthlyFteSumData));
                }
            }
        }

        if(getRequestResourceByFilters.isEmpty()){
            getRequestResourceByFilters=null;
        }
        else{
            getRequestResourceByFilters.sort(Comparator.comparing(GetRequestResourceByFilters::getResourceName, (r1,r2)->{
                String resource1 = r1.toLowerCase();
                String resource2 = r2.toLowerCase();

                return String.CASE_INSENSITIVE_ORDER.compare(resource1, resource2);
            }));
        }
        return getRequestResourceByFilters;
    }

    /*Double addTwoDoubleList(List<Double> greater, List<Double> smaller){
        double sum = 0d;
        for(int i=0; i<12; i++){
            sum = sum + greater.get(i) + smaller.get(i);
        }
        return sum;
    }

    Double subtractTwoDoubleList(List<Double> greater, List<Double> smaller){
        double diff = 0d;
        for(int i=0; i<12; i++){
            diff = diff + greater.get(i) - smaller.get(i);
        }
        return diff;
    }

    Double addDoubleListElements(List<Double> list, int startPoint, int endPoint){
//        both startPoint and endPoint is included
        double sum = 0d;
        for(int i=startPoint; i<=endPoint; i++){
            sum = sum + list.get(i);
        }
        return sum;
    }

    public GetBreakdownChartData getBreakdownChart (Integer financialYear, Integer projectId){
        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(projectId);

        List<SummaryDataResultDto> getProjectFinanceBudget = getProjectFinanceBudgetSummary(financialYear, projectIds);
        List<SummaryDataResultDto> getProjectFinanceForecast = getProjectFinanceForecastSummary(financialYear, projectIds);

        FinanceData budgetData = new FinanceData();
        FinanceData forecastData = new FinanceData();

        List<Double> getBudgetInternalData = getProjectFinanceBudget.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_EMPLOYEE_SALARIES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getBudgetTotalInternalData = getProjectFinanceBudget.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getBudgetVendorOnsiteData = getProjectFinanceBudget.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES) && it.getDescription().equals(ProjectUtils.T_AND_M)
                        && it.getSubCategory().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getBudgetVendorOffshoreData = getProjectFinanceBudget.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES) && it.getDescription().equals(ProjectUtils.T_AND_M)
                        && it.getSubCategory().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getBudgetVendorOtherOnsiteData = getProjectFinanceBudget.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.OTHER_VENDOR_EXPENSE)
                        && it.getSubCategory().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getBudgetVendorOtherOffshoreData = getProjectFinanceBudget.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.OTHER_VENDOR_EXPENSE)
                        && it.getSubCategory().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getBudgetCapexData = getProjectFinanceBudget.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_CAPEX_EXPENSES_TOTAL)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        budgetData.setInternalLabor(ProjectUtils.roundToOneDecimalPlace(addDoubleListElements(getBudgetInternalData,ProjectUtils.LOOP_START_POINT,ProjectUtils.LOOP_END_POINT)));
        budgetData.setExternalLabor(ProjectUtils.roundToOneDecimalPlace(addTwoDoubleList(getBudgetVendorOnsiteData, getBudgetVendorOffshoreData)));
        budgetData.setCapex(ProjectUtils.roundToOneDecimalPlace(addDoubleListElements(getBudgetCapexData,ProjectUtils.LOOP_START_POINT,ProjectUtils.LOOP_END_POINT)));
        budgetData.setOpex(ProjectUtils.roundToOneDecimalPlace(subtractTwoDoubleList(getBudgetTotalInternalData, getBudgetInternalData)
                + addTwoDoubleList(getBudgetVendorOtherOnsiteData, getBudgetVendorOtherOffshoreData)));
        budgetData.calculateTotal();

        List<Double> getForecastInternalData = getProjectFinanceForecast.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_EMPLOYEE_SALARIES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getForecastTotalInternalData = getProjectFinanceForecast.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getForecastVendorOnsiteData = getProjectFinanceForecast.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.T_AND_M)
                ).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getForecastVendorOffshoreData = getProjectFinanceForecast.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.T_AND_M)
                        && it.getSubCategory().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getForecastVendorOtherOnsiteData = getProjectFinanceForecast.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.OTHER_VENDOR_EXPENSE)
                        && it.getSubCategory().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getForecastVendorOtherOffshoreData = getProjectFinanceForecast.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.OTHER_VENDOR_EXPENSE)
                        && it.getSubCategory().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getForecastCapexData = getProjectFinanceForecast.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_CAPEX_EXPENSES_TOTAL)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        forecastData.setInternalLabor(ProjectUtils.roundToOneDecimalPlace(addDoubleListElements(getForecastInternalData,ProjectUtils.LOOP_START_POINT,ProjectUtils.LOOP_END_POINT)));
        forecastData.setExternalLabor(ProjectUtils.roundToOneDecimalPlace(addTwoDoubleList(getForecastVendorOnsiteData, getForecastVendorOffshoreData)));
        forecastData.setCapex(ProjectUtils.roundToOneDecimalPlace(addDoubleListElements(getForecastCapexData,ProjectUtils.LOOP_START_POINT,ProjectUtils.LOOP_END_POINT)));
        forecastData.setOpex(ProjectUtils.roundToOneDecimalPlace(subtractTwoDoubleList(getForecastTotalInternalData, getForecastInternalData)
                + addTwoDoubleList(getForecastVendorOtherOnsiteData, getForecastVendorOtherOffshoreData)));
        forecastData.calculateTotal();

        return new GetBreakdownChartData(budgetData,forecastData);
    }

    public GetBudgetVsForecastChart getBudgetVsForecastChart(Integer financialYear, Integer projectId){

        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(projectId);

        List<SummaryDataResultDto> getProjectFinanceBudget = getProjectFinanceBudgetSummary(financialYear, projectIds);
        List<SummaryDataResultDto> getProjectFinanceForecast = getProjectFinanceForecastSummary(financialYear, projectIds);

        List<Double> getProjectFinanceBudgetData = getProjectFinanceBudget.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_BUDGET_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getProjectFinanceForecastData = getProjectFinanceForecast.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_FORECAST_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        return new GetBudgetVsForecastChart(getProjectFinanceBudgetData, getProjectFinanceForecastData);
    }

    */
    /*
    PROJECT FINANCE
     */
    /*

    //    to convert expense data to desired format
    public List<Double> setExpenseValues (String startMonth, SummaryDataResult summaryDataResult){

        List<Double> expenseValuesOrdered = new ArrayList<>();

        List<Double> expenseValues = new ArrayList<>();

        if(summaryDataResult.getJan()!=null){
            expenseValues.add(summaryDataResult.getJan().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getFeb()!=null){
            expenseValues.add(summaryDataResult.getFeb().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getMar()!=null){
            expenseValues.add(summaryDataResult.getMar().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getApr()!=null){
            expenseValues.add(summaryDataResult.getApr().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getMay()!=null){
            expenseValues.add(summaryDataResult.getMay().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getJun()!=null){
            expenseValues.add(summaryDataResult.getJun().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getJul()!=null){
            expenseValues.add(summaryDataResult.getJul().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getAug()!=null){
            expenseValues.add(summaryDataResult.getAug().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getSep()!=null){
            expenseValues.add(summaryDataResult.getSep().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getOct()!=null){
            expenseValues.add(summaryDataResult.getOct().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getNov()!=null){
            expenseValues.add(summaryDataResult.getNov().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }
        if(summaryDataResult.getDec()!=null){
            expenseValues.add(summaryDataResult.getDec().doubleValue());
        }
        else{
            expenseValues.add(0d);
        }

        int i = ProjectUtils.MONTH_MAP.get(startMonth)-1;

        do{
            expenseValuesOrdered.add(ProjectUtils.roundToOneDecimalPlace(expenseValues.get(i)));
            i++;
            if(i==12){
                i=0;
            }
        }while(i != ProjectUtils.MONTH_MAP.get(startMonth)-1);


        return expenseValuesOrdered;
    }

    public List<Double> setExpenseValuesFromListOfLinkedHashMap(List<LinkedHashMap<String, Object>> expenseDataResult) {
        List<Double> expenseValuesOrdered = new ArrayList<>();

        for (LinkedHashMap<String, Object> eachList : expenseDataResult) {
            for (Map.Entry<String, Object> entry : eachList.entrySet()) {
                expenseValuesOrdered.add(ProjectUtils.roundToOneDecimalPlace(((BigDecimal) entry.getValue()).doubleValue()));
            }
        }

        if (expenseValuesOrdered.isEmpty()) {
            expenseValuesOrdered = null;
        }

        return expenseValuesOrdered;
    }

    public List<LinkedHashMap<String, Object>> getProjectFinanceDirectEmployeeData(Integer financialYear, String financialYearStartMonth, String queryTextFile, String workLocation, List<Integer> projectIds) {

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


            String capexRelatedExpenseForecastQuery = ProjectUtils.getFile(queryTextFile);
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

    */
    /*Query query = entityManager.createNativeQuery(b).unwrap(org.hibernate.query.Query.class);
    ((NativeQueryImpl) query).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);*/
    /*

            Query query = entityManager.createNativeQuery(b, Tuple.class);
            query.setParameter("financialYear", financialYear);
            query.setParameter("workLocation", workLocation);
            query.setParameter("projectIds", projectIds);

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

    String convertIntegerArrayToStringArray(List<Integer> projectIds){
        List<String> arrayToString = new ArrayList<>();
        for(Integer eachId: projectIds){
            arrayToString.add(String.valueOf(eachId));
        }
        String projectIdsAsString = String.join(",", arrayToString);

        return projectIdsAsString;
    }
    */
    /*
    Project Finance Internal Labor Expense
     */
    /*

    //    Project Finance Budget
    public List<SummaryDataResultDto> getProjectFinanceBudgetInternalLabor(Integer financialYear, List<Integer> projectIds){
//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> projectFinanceBudgetInternalLaborExpense = new ArrayList<>();


//        get summary budget Internal Onsite data
        String internalOnsiteBudgetQuery = "projectfinance/budget/getSummaryBudgetInternalDirectEmp.txt";

//        List<LinkedHashMap<String, Object>> getInternalOnsiteBudgetDetails = getSummaryDirectEmployeeData(financialYear, startMonth, internalOnsiteBudgetQuery, ProjectUtils.ONSITE);

        List<LinkedHashMap<String, Object>> getInternalOnsiteBudgetDetails = null;

        if (getInternalOnsiteBudgetDetails != null && !getInternalOnsiteBudgetDetails.isEmpty()) {
//            if () {
            SummaryDataResultDto budgetInternalOnsite = new SummaryDataResultDto();
            budgetInternalOnsite.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOnsite.setDescription(ProjectUtils.ONSITE);
            budgetInternalOnsite.setSubCategory(null);
            budgetInternalOnsite.setIsAggregate(false);
            budgetInternalOnsite.setData(setExpenseValuesFromListOfLinkedHashMap(getInternalOnsiteBudgetDetails));
            projectFinanceBudgetInternalLaborExpense.add(budgetInternalOnsite);
//            }
        }
        else {
            SummaryDataResultDto budgetInternalOnsite = new SummaryDataResultDto();
            budgetInternalOnsite.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOnsite.setDescription(ProjectUtils.ONSITE);
            budgetInternalOnsite.setSubCategory(null);
            budgetInternalOnsite.setIsAggregate(false);
            budgetInternalOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceBudgetInternalLaborExpense.add(budgetInternalOnsite);
        }


//        get summary budget Internal Offshore data
        String internalOffshoreBudgetQuery = "projectfinance/budget/getSummaryBudgetInternalDirectEmp.txt";

//        List<LinkedHashMap<String, Object>> getInternalOffshoreBudgetDetails = getSummaryDirectEmployeeData(financialYear, startMonth, internalOffshoreBudgetQuery, ProjectUtils.OFFSHORE);

        List<LinkedHashMap<String, Object>> getInternalOffshoreBudgetDetails = null;

        if (getInternalOffshoreBudgetDetails != null && !getInternalOffshoreBudgetDetails.isEmpty()) {
//            if () {
            SummaryDataResultDto budgetInternalOffshore = new SummaryDataResultDto();
            budgetInternalOffshore.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOffshore.setDescription(ProjectUtils.OFFSHORE);
            budgetInternalOffshore.setSubCategory(null);
            budgetInternalOffshore.setIsAggregate(false);
            budgetInternalOffshore.setData(setExpenseValuesFromListOfLinkedHashMap(getInternalOffshoreBudgetDetails));
            projectFinanceBudgetInternalLaborExpense.add(budgetInternalOffshore);
//            }
        }
        else {
            SummaryDataResultDto budgetInternalOffshore = new SummaryDataResultDto();
            budgetInternalOffshore.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOffshore.setDescription(ProjectUtils.OFFSHORE);
            budgetInternalOffshore.setSubCategory(null);
            budgetInternalOffshore.setIsAggregate(false);
            budgetInternalOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceBudgetInternalLaborExpense.add(budgetInternalOffshore);
        }

        // Total Employee Salaries
        if (projectFinanceBudgetInternalLaborExpense != null && !projectFinanceBudgetInternalLaborExpense.isEmpty()) {
            List<Double> internalLaborExpensesOnsiteData = projectFinanceBudgetInternalLaborExpense.stream()
                    .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                            && it.getDescription().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> internalLaborExpensesOffshoreData = projectFinanceBudgetInternalLaborExpense.stream()
                    .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                            && it.getDescription().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                Double total = internalLaborExpensesOnsiteData.get(i) + internalLaborExpensesOffshoreData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto budgetInternalEmployeeAggregate = new SummaryDataResultDto();
            budgetInternalEmployeeAggregate.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalEmployeeAggregate.setDescription(ProjectUtils.TOTAL_EMPLOYEE_SALARIES);
            budgetInternalEmployeeAggregate.setSubCategory(null);
            budgetInternalEmployeeAggregate.setData(totalEmployeeSalariesList);
            budgetInternalEmployeeAggregate.setIsAggregate(true);
            projectFinanceBudgetInternalLaborExpense.add(budgetInternalEmployeeAggregate);
        }

//        get project finance budget Internal other direct in-house data
        String internalOtherDirectBudgetQuery = ProjectUtils.getFile("projectfinance/budget/getProjectFinanceBudgetInternalOtherDirect.txt");

        Query query3 = entityManager.createNativeQuery(internalOtherDirectBudgetQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query3.setParameter("financialYear", financialYear);
        query3.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getInternalOtherDirectBudgetDetails = query3.getResultList();

        if(getInternalOtherDirectBudgetDetails!=null && !getInternalOtherDirectBudgetDetails.isEmpty()){
            for(SummaryDataResult eachResult: getInternalOtherDirectBudgetDetails){
                SummaryDataResultDto budgetInternalOtherDirect = new SummaryDataResultDto();
                budgetInternalOtherDirect.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
                budgetInternalOtherDirect.setDescription(eachResult.getTitle());
                budgetInternalOtherDirect.setSubCategory(null);
                budgetInternalOtherDirect.setIsAggregate(false);
                budgetInternalOtherDirect.setData(setExpenseValues(startMonth, eachResult));
                projectFinanceBudgetInternalLaborExpense.add(budgetInternalOtherDirect);
            }
        }
        else{
            SummaryDataResultDto budgetInternalOtherDirect = new SummaryDataResultDto();
            budgetInternalOtherDirect.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalOtherDirect.setDescription(ProjectUtils.EMPTY_INTERNAL_OTHER_EXPENSES);
            budgetInternalOtherDirect.setSubCategory(null);
            budgetInternalOtherDirect.setIsAggregate(false);
            budgetInternalOtherDirect.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceBudgetInternalLaborExpense.add(budgetInternalOtherDirect);
        }

        if (projectFinanceBudgetInternalLaborExpense != null && !projectFinanceBudgetInternalLaborExpense.isEmpty()) {
            List<SummaryDataResultDto> projectFinanceBudgetInternalLaborExpenseOtherList = projectFinanceBudgetInternalLaborExpense.stream()
                    .filter(it -> !it.getDescription().equals(ProjectUtils.ONSITE)
                            && !it.getDescription().equals(ProjectUtils.OFFSHORE)
                            && !it.getDescription().equals(ProjectUtils.TOTAL_EMPLOYEE_SALARIES))
                    .collect(Collectors.toList());

            List<Double> projectFinanceBudgetInternalLaborExpenseTotalList = projectFinanceBudgetInternalLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.TOTAL_EMPLOYEE_SALARIES))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> totalInternalLaborExpensesTotalList = new ArrayList<>(projectFinanceBudgetInternalLaborExpenseTotalList);

            for (SummaryDataResultDto summaryDataResultDto : projectFinanceBudgetInternalLaborExpenseOtherList) {
                List<Double> dataList = summaryDataResultDto.getData();
                for (int i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                    totalInternalLaborExpensesTotalList.set(i, totalInternalLaborExpensesTotalList.get(i) + dataList.get(i));
                }
            }
            SummaryDataResultDto budgetInternalTotalAggregate = new SummaryDataResultDto();
            budgetInternalTotalAggregate.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            budgetInternalTotalAggregate.setDescription(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES);
            budgetInternalTotalAggregate.setSubCategory(null);
            budgetInternalTotalAggregate.setIsAggregate(true);
            budgetInternalTotalAggregate.setData(totalInternalLaborExpensesTotalList);
            projectFinanceBudgetInternalLaborExpense.add(budgetInternalTotalAggregate);
        }

        if(projectFinanceBudgetInternalLaborExpense.isEmpty()){
            projectFinanceBudgetInternalLaborExpense = null;
        }

        return projectFinanceBudgetInternalLaborExpense;
    }

    //    Project Finance Forecast
    public List<SummaryDataResultDto> getProjectFinanceForecastInternalLabor(Integer financialYear, List<Integer> projectIds){
//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> projectFinanceForecastInternalLaborExpense = new ArrayList<>();


//        get summary forecast Internal Onsite data
        String internalOnsiteForecastQuery = "projectfinance/forecast/getSummaryForecastInternalDirectEmp.txt";

        List<LinkedHashMap<String, Object>> getInternalOnsiteForecastDetails = getProjectFinanceDirectEmployeeData(financialYear, startMonth, internalOnsiteForecastQuery, ProjectUtils.ONSITE, projectIds);

//        List<LinkedHashMap<String, Object>> getInternalOnsiteForecastDetails = null;

        if (getInternalOnsiteForecastDetails != null && !getInternalOnsiteForecastDetails.isEmpty()) {
//            if () {
            SummaryDataResultDto forecastInternalOnsite = new SummaryDataResultDto();
            forecastInternalOnsite.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOnsite.setDescription(ProjectUtils.ONSITE);
            forecastInternalOnsite.setSubCategory(null);
            forecastInternalOnsite.setIsAggregate(false);
            forecastInternalOnsite.setData(setExpenseValuesFromListOfLinkedHashMap(getInternalOnsiteForecastDetails));
            projectFinanceForecastInternalLaborExpense.add(forecastInternalOnsite);
//            }
        }
        else {
            SummaryDataResultDto forecastInternalOnsite = new SummaryDataResultDto();
            forecastInternalOnsite.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOnsite.setDescription(ProjectUtils.ONSITE);
            forecastInternalOnsite.setSubCategory(null);
            forecastInternalOnsite.setIsAggregate(false);
            forecastInternalOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceForecastInternalLaborExpense.add(forecastInternalOnsite);
        }


//        get summary forecast Internal Offshore data
        String internalOffshoreForecastQuery = "projectfinance/forecast/getSummaryForecastInternalDirectEmp.txt";

        List<LinkedHashMap<String, Object>> getInternalOffshoreForecastDetails = getProjectFinanceDirectEmployeeData(financialYear, startMonth, internalOffshoreForecastQuery, ProjectUtils.OFFSHORE, projectIds);

//        List<LinkedHashMap<String, Object>> getInternalOffshoreForecastDetails = null;

        if (getInternalOffshoreForecastDetails != null && !getInternalOffshoreForecastDetails.isEmpty()) {
//            if () {
            SummaryDataResultDto forecastInternalOffshore = new SummaryDataResultDto();
            forecastInternalOffshore.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOffshore.setDescription(ProjectUtils.OFFSHORE);
            forecastInternalOffshore.setSubCategory(null);
            forecastInternalOffshore.setIsAggregate(false);
            forecastInternalOffshore.setData(setExpenseValuesFromListOfLinkedHashMap(getInternalOffshoreForecastDetails));
            projectFinanceForecastInternalLaborExpense.add(forecastInternalOffshore);
//            }
        }
        else {
            SummaryDataResultDto forecastInternalOffshore = new SummaryDataResultDto();
            forecastInternalOffshore.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOffshore.setDescription(ProjectUtils.OFFSHORE);
            forecastInternalOffshore.setSubCategory(null);
            forecastInternalOffshore.setIsAggregate(false);
            forecastInternalOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceForecastInternalLaborExpense.add(forecastInternalOffshore);
        }

        // Total Employee Salaries
        if (projectFinanceForecastInternalLaborExpense != null && !projectFinanceForecastInternalLaborExpense.isEmpty()) {
            List<Double> internalLaborExpensesOnsiteData = projectFinanceForecastInternalLaborExpense.stream()
                    .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                            && it.getDescription().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> internalLaborExpensesOffshoreData = projectFinanceForecastInternalLaborExpense.stream()
                    .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                            && it.getDescription().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                Double total = internalLaborExpensesOnsiteData.get(i) + internalLaborExpensesOffshoreData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto forecastInternalEmployeeAggregate = new SummaryDataResultDto();
            forecastInternalEmployeeAggregate.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalEmployeeAggregate.setDescription(ProjectUtils.TOTAL_EMPLOYEE_SALARIES);
            forecastInternalEmployeeAggregate.setSubCategory(null);
            forecastInternalEmployeeAggregate.setData(totalEmployeeSalariesList);
            forecastInternalEmployeeAggregate.setIsAggregate(true);
            projectFinanceForecastInternalLaborExpense.add(forecastInternalEmployeeAggregate);
        }

//        get project finance forecast Internal other direct in-house data
        String internalOtherDirectForecastQuery = ProjectUtils.getFile("projectfinance/forecast/getProjectFinanceForecastInternalOtherDirect.txt");

        Query query3 = entityManager.createNativeQuery(internalOtherDirectForecastQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query3.setParameter("financialYear", financialYear);
        query3.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getInternalOtherDirectForecastDetails = query3.getResultList();

        if(getInternalOtherDirectForecastDetails!=null && !getInternalOtherDirectForecastDetails.isEmpty()){
            for(SummaryDataResult eachResult: getInternalOtherDirectForecastDetails){
                SummaryDataResultDto forecastInternalOtherDirect = new SummaryDataResultDto();
                forecastInternalOtherDirect.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
                forecastInternalOtherDirect.setDescription(eachResult.getTitle());
                forecastInternalOtherDirect.setSubCategory(null);
                forecastInternalOtherDirect.setIsAggregate(false);
                forecastInternalOtherDirect.setData(setExpenseValues(startMonth, eachResult));
                projectFinanceForecastInternalLaborExpense.add(forecastInternalOtherDirect);
            }
        }
        else{
            SummaryDataResultDto forecastInternalOtherDirect = new SummaryDataResultDto();
            forecastInternalOtherDirect.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalOtherDirect.setDescription(ProjectUtils.EMPTY_INTERNAL_OTHER_EXPENSES);
            forecastInternalOtherDirect.setSubCategory(null);
            forecastInternalOtherDirect.setIsAggregate(false);
            forecastInternalOtherDirect.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceForecastInternalLaborExpense.add(forecastInternalOtherDirect);
        }

        if (projectFinanceForecastInternalLaborExpense != null && !projectFinanceForecastInternalLaborExpense.isEmpty()) {
            List<SummaryDataResultDto> projectFinanceForecastInternalLaborExpenseOtherList = projectFinanceForecastInternalLaborExpense.stream()
                    .filter(it -> !it.getDescription().equals(ProjectUtils.ONSITE)
                            && !it.getDescription().equals(ProjectUtils.OFFSHORE)
                            && !it.getDescription().equals(ProjectUtils.TOTAL_EMPLOYEE_SALARIES))
                    .collect(Collectors.toList());

            List<Double> projectFinanceForecastInternalLaborExpenseTotalList = projectFinanceForecastInternalLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.TOTAL_EMPLOYEE_SALARIES))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> totalInternalLaborExpensesTotalList = new ArrayList<>(projectFinanceForecastInternalLaborExpenseTotalList);

            for (SummaryDataResultDto summaryDataResultDto : projectFinanceForecastInternalLaborExpenseOtherList) {
                List<Double> dataList = summaryDataResultDto.getData();
                for (int i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                    totalInternalLaborExpensesTotalList.set(i, totalInternalLaborExpensesTotalList.get(i) + dataList.get(i));
                }
            }
            SummaryDataResultDto forecastInternalTotalAggregate = new SummaryDataResultDto();
            forecastInternalTotalAggregate.setTableType(ProjectUtils.INTERNAL_LABOR_EXPENSES);
            forecastInternalTotalAggregate.setDescription(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES);
            forecastInternalTotalAggregate.setSubCategory(null);
            forecastInternalTotalAggregate.setIsAggregate(true);
            forecastInternalTotalAggregate.setData(totalInternalLaborExpensesTotalList);
            projectFinanceForecastInternalLaborExpense.add(forecastInternalTotalAggregate);
        }

        if(projectFinanceForecastInternalLaborExpense.isEmpty()){
            projectFinanceForecastInternalLaborExpense = null;
        }

        return projectFinanceForecastInternalLaborExpense;
    }


    */
    /*
    Project Finance Vendor Labor Expense
     */
    /*

    //    Project Finance Budget
    public List<SummaryDataResultDto> getProjectFinanceBudgetVendorLabor(Integer financialYear, List<Integer> projectIds){

//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> projectFinanceBudgetVendorLaborExpense = new ArrayList<>();

//        get project finance budget vendor T&M Onsite data
        String vendorBudgetTAndMOnsiteQuery = ProjectUtils.getFile("projectfinance/budget/getProjectFinanceBudgetVendorTAndMOnsite.txt");

        Query query1 = entityManager.createNativeQuery(vendorBudgetTAndMOnsiteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query1.setParameter("financialYear", financialYear);
        query1.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getVendorBudgetTAndMOnsiteDetails = query1.getResultList();

        if(getVendorBudgetTAndMOnsiteDetails != null && !getVendorBudgetTAndMOnsiteDetails.isEmpty()){
            SummaryDataResultDto budgetVendorTAndMOnsite = new SummaryDataResultDto();
            budgetVendorTAndMOnsite.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTAndMOnsite.setDescription(ProjectUtils.T_AND_M);
            budgetVendorTAndMOnsite.setSubCategory(ProjectUtils.ONSITE);
            budgetVendorTAndMOnsite.setIsAggregate(false);
            budgetVendorTAndMOnsite.setData(setExpenseValues(startMonth, getVendorBudgetTAndMOnsiteDetails.get(0)));
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorTAndMOnsite);
        } else {
            SummaryDataResultDto budgetVendorTAndMOnsite = new SummaryDataResultDto();
            budgetVendorTAndMOnsite.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTAndMOnsite.setDescription(ProjectUtils.T_AND_M);
            budgetVendorTAndMOnsite.setSubCategory(ProjectUtils.ONSITE);
            budgetVendorTAndMOnsite.setIsAggregate(false);
            budgetVendorTAndMOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorTAndMOnsite);
        }


//        get project finance budget vendor Other Expense Onsite data
        String vendorBudgetOtherExpenseOnsiteQuery = ProjectUtils.getFile("projectfinance/budget/getProjectFinanceBudgetVendorOtherOnsite.txt");

        Query query2 = entityManager.createNativeQuery(vendorBudgetOtherExpenseOnsiteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query2).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query2.setParameter("financialYear", financialYear);
        query2.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getVendorBudgetOtherExpenseOnsiteDetails = query2.getResultList();

        if(getVendorBudgetOtherExpenseOnsiteDetails != null && !getVendorBudgetOtherExpenseOnsiteDetails.isEmpty()){
            SummaryDataResultDto budgetVendorOtherExpenseOnsite = new SummaryDataResultDto();
            budgetVendorOtherExpenseOnsite.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOtherExpenseOnsite.setDescription(ProjectUtils.OTHER_VENDOR_EXPENSE);
            budgetVendorOtherExpenseOnsite.setSubCategory(ProjectUtils.ONSITE);
            budgetVendorOtherExpenseOnsite.setIsAggregate(false);
            budgetVendorOtherExpenseOnsite.setData(setExpenseValues(startMonth, getVendorBudgetOtherExpenseOnsiteDetails.get(0)));
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorOtherExpenseOnsite);
        } else {
            SummaryDataResultDto budgetVendorOtherExpenseOnsite = new SummaryDataResultDto();
            budgetVendorOtherExpenseOnsite.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOtherExpenseOnsite.setDescription(ProjectUtils.OTHER_VENDOR_EXPENSE);
            budgetVendorOtherExpenseOnsite.setSubCategory(ProjectUtils.ONSITE);
            budgetVendorOtherExpenseOnsite.setIsAggregate(false);
            budgetVendorOtherExpenseOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorOtherExpenseOnsite);
        }

        // Total onsite vendor Salaries
        if (projectFinanceBudgetVendorLaborExpense != null && !projectFinanceBudgetVendorLaborExpense.isEmpty()) {
            List<Double> vendorLaborExpensesOnsiteTAndMData = projectFinanceBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.T_AND_M)
                            && it.getSubCategory().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> vendorLaborExpensesOnsiteOtherData = projectFinanceBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.OTHER_VENDOR_EXPENSE)
                            && it.getSubCategory().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                Double total = vendorLaborExpensesOnsiteTAndMData.get(i) + vendorLaborExpensesOnsiteOtherData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto budgetVendorOnsiteAggregate = new SummaryDataResultDto();
            budgetVendorOnsiteAggregate.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOnsiteAggregate.setDescription(ProjectUtils.ONSITE);
            budgetVendorOnsiteAggregate.setSubCategory(null);
            budgetVendorOnsiteAggregate.setData(totalEmployeeSalariesList);
            budgetVendorOnsiteAggregate.setIsAggregate(true);
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorOnsiteAggregate);
        }

//        get project finance budget vendor T&M Offshore data
        String vendorBudgetTAndMOffshoreQuery = ProjectUtils.getFile("projectfinance/budget/getProjectFinanceBudgetVendorTAndMOffshore.txt");

        Query query3 = entityManager.createNativeQuery(vendorBudgetTAndMOffshoreQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query3.setParameter("financialYear", financialYear);
        query3.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getVendorBudgetTAndMOffshoreDetails = query3.getResultList();

        if(getVendorBudgetTAndMOffshoreDetails != null && !getVendorBudgetTAndMOffshoreDetails.isEmpty()){
            SummaryDataResultDto budgetVendorTAndMOffshore = new SummaryDataResultDto();
            budgetVendorTAndMOffshore.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTAndMOffshore.setDescription(ProjectUtils.T_AND_M);
            budgetVendorTAndMOffshore.setSubCategory(ProjectUtils.OFFSHORE);
            budgetVendorTAndMOffshore.setIsAggregate(false);
            budgetVendorTAndMOffshore.setData(setExpenseValues(startMonth, getVendorBudgetTAndMOffshoreDetails.get(0)));
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorTAndMOffshore);
        } else {
            SummaryDataResultDto budgetVendorTAndMOffshore = new SummaryDataResultDto();
            budgetVendorTAndMOffshore.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTAndMOffshore.setDescription(ProjectUtils.T_AND_M);
            budgetVendorTAndMOffshore.setSubCategory(ProjectUtils.OFFSHORE);
            budgetVendorTAndMOffshore.setIsAggregate(false);
            budgetVendorTAndMOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorTAndMOffshore);
        }


//        get project finance budget vendor Other Expense Offshore data
        String vendorBudgetOtherExpenseOffshoreQuery = ProjectUtils.getFile("projectfinance/budget/getProjectFinanceBudgetVendorOtherOffshore.txt");

        Query query4 = entityManager.createNativeQuery(vendorBudgetOtherExpenseOffshoreQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query4).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query4.setParameter("financialYear", financialYear);
        query4.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getVendorBudgetOtherExpenseOffshoreDetails = query4.getResultList();

        if(!getVendorBudgetOtherExpenseOffshoreDetails.isEmpty()){
            SummaryDataResultDto budgetVendorOtherExpenseOffshore = new SummaryDataResultDto();
            budgetVendorOtherExpenseOffshore.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOtherExpenseOffshore.setDescription(ProjectUtils.OTHER_VENDOR_EXPENSE);
            budgetVendorOtherExpenseOffshore.setSubCategory(ProjectUtils.OFFSHORE);
            budgetVendorOtherExpenseOffshore.setIsAggregate(false);
            budgetVendorOtherExpenseOffshore.setData(setExpenseValues(startMonth, getVendorBudgetOtherExpenseOffshoreDetails.get(0)));
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorOtherExpenseOffshore);
        } else {
            SummaryDataResultDto budgetVendorOtherExpenseOffshore = new SummaryDataResultDto();
            budgetVendorOtherExpenseOffshore.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOtherExpenseOffshore.setDescription(ProjectUtils.OTHER_VENDOR_EXPENSE);
            budgetVendorOtherExpenseOffshore.setSubCategory(ProjectUtils.OFFSHORE);
            budgetVendorOtherExpenseOffshore.setIsAggregate(false);
            budgetVendorOtherExpenseOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorOtherExpenseOffshore);
        }

        // Total offshore vendor Salaries
        if (projectFinanceBudgetVendorLaborExpense != null && !projectFinanceBudgetVendorLaborExpense.isEmpty()) {
            List<Double> vendorLaborExpensesOffshoreTAndMData = projectFinanceBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.T_AND_M)
                            && it.getSubCategory().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> vendorLaborExpensesOffshoreOtherData = projectFinanceBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.OTHER_VENDOR_EXPENSE)
                            && it.getSubCategory().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                Double total = vendorLaborExpensesOffshoreTAndMData.get(i) + vendorLaborExpensesOffshoreOtherData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto budgetVendorOffshoreAggregate = new SummaryDataResultDto();
            budgetVendorOffshoreAggregate.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorOffshoreAggregate.setDescription(ProjectUtils.OFFSHORE);
            budgetVendorOffshoreAggregate.setSubCategory(null);
            budgetVendorOffshoreAggregate.setData(totalEmployeeSalariesList);
            budgetVendorOffshoreAggregate.setIsAggregate(true);
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorOffshoreAggregate);
        }

        // Total Vendor Expenses
        if (projectFinanceBudgetVendorLaborExpense != null && !projectFinanceBudgetVendorLaborExpense.isEmpty()) {
            List<Double> summaryBudgetOnsiteAggregatedData = projectFinanceBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.ONSITE))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> summaryBudgetOffshoreAggregatedData = projectFinanceBudgetVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.OFFSHORE))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> totalVendorLaborExpensesTotalList = new ArrayList<>();


            for (int i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                totalVendorLaborExpensesTotalList.add(summaryBudgetOnsiteAggregatedData.get(i) + summaryBudgetOffshoreAggregatedData.get(i));
            }

            SummaryDataResultDto budgetVendorTotalAggregate = new SummaryDataResultDto();
            budgetVendorTotalAggregate.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            budgetVendorTotalAggregate.setDescription(ProjectUtils.TOTAL_VENDOR_LABOR_EXPENSES);
            budgetVendorTotalAggregate.setSubCategory(null);
            budgetVendorTotalAggregate.setIsAggregate(true);
            budgetVendorTotalAggregate.setData(totalVendorLaborExpensesTotalList);
            projectFinanceBudgetVendorLaborExpense.add(budgetVendorTotalAggregate);
        }

        if(projectFinanceBudgetVendorLaborExpense.isEmpty()){
            projectFinanceBudgetVendorLaborExpense = null;
        }

        return projectFinanceBudgetVendorLaborExpense;
    }

    //    Project Finance Forecast
    public List<SummaryDataResultDto> getProjectFinanceForecastVendorLabor(Integer financialYear,List<Integer> projectIds){

//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> projectFinanceForecastVendorLaborExpense = new ArrayList<>();

//        get project finance forecast vendor T&M Onsite data
        String vendorForecastTAndMOnsiteQuery = ProjectUtils.getFile("projectfinance/forecast/getProjectFinanceForecastVendorTAndMOnsite.txt");

        Query query1 = entityManager.createNativeQuery(vendorForecastTAndMOnsiteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query1.setParameter("financialYear", financialYear);
        query1.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getVendorForecastTAndMOnsiteDetails = query1.getResultList();

        if(getVendorForecastTAndMOnsiteDetails != null && !getVendorForecastTAndMOnsiteDetails.isEmpty()){
            SummaryDataResultDto forecastVendorTAndMOnsite = new SummaryDataResultDto();
            forecastVendorTAndMOnsite.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTAndMOnsite.setDescription(ProjectUtils.T_AND_M);
            forecastVendorTAndMOnsite.setSubCategory(ProjectUtils.ONSITE);
            forecastVendorTAndMOnsite.setIsAggregate(false);
            forecastVendorTAndMOnsite.setData(setExpenseValues(startMonth, getVendorForecastTAndMOnsiteDetails.get(0)));
            projectFinanceForecastVendorLaborExpense.add(forecastVendorTAndMOnsite);
        } else {
            SummaryDataResultDto forecastVendorTAndMOnsite = new SummaryDataResultDto();
            forecastVendorTAndMOnsite.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTAndMOnsite.setDescription(ProjectUtils.T_AND_M);
            forecastVendorTAndMOnsite.setSubCategory(ProjectUtils.ONSITE);
            forecastVendorTAndMOnsite.setIsAggregate(false);
            forecastVendorTAndMOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceForecastVendorLaborExpense.add(forecastVendorTAndMOnsite);
        }


//        get project finance forecast vendor Other Expense Onsite data
        String vendorForecastOtherExpenseOnsiteQuery = ProjectUtils.getFile("projectfinance/forecast/getProjectFinanceForecastVendorOtherOnsite.txt");

        Query query2 = entityManager.createNativeQuery(vendorForecastOtherExpenseOnsiteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query2).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query2.setParameter("financialYear", financialYear);
        query2.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getVendorForecastOtherExpenseOnsiteDetails = query2.getResultList();

        if(getVendorForecastOtherExpenseOnsiteDetails != null && !getVendorForecastOtherExpenseOnsiteDetails.isEmpty()){
            SummaryDataResultDto forecastVendorOtherExpenseOnsite = new SummaryDataResultDto();
            forecastVendorOtherExpenseOnsite.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOtherExpenseOnsite.setDescription(ProjectUtils.OTHER_VENDOR_EXPENSE);
            forecastVendorOtherExpenseOnsite.setSubCategory(ProjectUtils.ONSITE);
            forecastVendorOtherExpenseOnsite.setIsAggregate(false);
            forecastVendorOtherExpenseOnsite.setData(setExpenseValues(startMonth, getVendorForecastOtherExpenseOnsiteDetails.get(0)));
            projectFinanceForecastVendorLaborExpense.add(forecastVendorOtherExpenseOnsite);
        } else {
            SummaryDataResultDto forecastVendorOtherExpenseOnsite = new SummaryDataResultDto();
            forecastVendorOtherExpenseOnsite.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOtherExpenseOnsite.setDescription(ProjectUtils.OTHER_VENDOR_EXPENSE);
            forecastVendorOtherExpenseOnsite.setSubCategory(ProjectUtils.ONSITE);
            forecastVendorOtherExpenseOnsite.setIsAggregate(false);
            forecastVendorOtherExpenseOnsite.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceForecastVendorLaborExpense.add(forecastVendorOtherExpenseOnsite);
        }

        // Total onsite vendor Salaries
        if (projectFinanceForecastVendorLaborExpense != null && !projectFinanceForecastVendorLaborExpense.isEmpty()) {
            List<Double> vendorLaborExpensesOnsiteTAndMData = projectFinanceForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.T_AND_M)
                            && it.getSubCategory().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> vendorLaborExpensesOnsiteOtherData = projectFinanceForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.OTHER_VENDOR_EXPENSE)
                            && it.getSubCategory().equals(ProjectUtils.ONSITE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                Double total = vendorLaborExpensesOnsiteTAndMData.get(i) + vendorLaborExpensesOnsiteOtherData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto forecastVendorOnsiteAggregate = new SummaryDataResultDto();
            forecastVendorOnsiteAggregate.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOnsiteAggregate.setDescription(ProjectUtils.ONSITE);
            forecastVendorOnsiteAggregate.setSubCategory(null);
            forecastVendorOnsiteAggregate.setData(totalEmployeeSalariesList);
            forecastVendorOnsiteAggregate.setIsAggregate(true);
            projectFinanceForecastVendorLaborExpense.add(forecastVendorOnsiteAggregate);
        }

//        get project finance forecast vendor T&M Offshore data
        String vendorForecastTAndMOffshoreQuery = ProjectUtils.getFile("projectfinance/forecast/getProjectFinanceForecastVendorTAndMOffshore.txt");

        Query query3 = entityManager.createNativeQuery(vendorForecastTAndMOffshoreQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query3).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query3.setParameter("financialYear", financialYear);
        query3.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getVendorForecastTAndMOffshoreDetails = query3.getResultList();

        if(getVendorForecastTAndMOffshoreDetails != null && !getVendorForecastTAndMOffshoreDetails.isEmpty()){
            SummaryDataResultDto forecastVendorTAndMOffshore = new SummaryDataResultDto();
            forecastVendorTAndMOffshore.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTAndMOffshore.setDescription(ProjectUtils.T_AND_M);
            forecastVendorTAndMOffshore.setSubCategory(ProjectUtils.OFFSHORE);
            forecastVendorTAndMOffshore.setIsAggregate(false);
            forecastVendorTAndMOffshore.setData(setExpenseValues(startMonth, getVendorForecastTAndMOffshoreDetails.get(0)));
            projectFinanceForecastVendorLaborExpense.add(forecastVendorTAndMOffshore);
        } else {
            SummaryDataResultDto forecastVendorTAndMOffshore = new SummaryDataResultDto();
            forecastVendorTAndMOffshore.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTAndMOffshore.setDescription(ProjectUtils.T_AND_M);
            forecastVendorTAndMOffshore.setSubCategory(ProjectUtils.OFFSHORE);
            forecastVendorTAndMOffshore.setIsAggregate(false);
            forecastVendorTAndMOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceForecastVendorLaborExpense.add(forecastVendorTAndMOffshore);
        }


//        get project finance forecast vendor Other Expense Offshore data
        String vendorForecastOtherExpenseOffshoreQuery = ProjectUtils.getFile("projectfinance/forecast/getProjectFinanceForecastVendorOtherOffshore.txt");

        Query query4 = entityManager.createNativeQuery(vendorForecastOtherExpenseOffshoreQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query4).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query4.setParameter("financialYear", financialYear);
        query4.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getVendorForecastOtherExpenseOffshoreDetails = query4.getResultList();

        if(!getVendorForecastOtherExpenseOffshoreDetails.isEmpty()){
            SummaryDataResultDto forecastVendorOtherExpenseOffshore = new SummaryDataResultDto();
            forecastVendorOtherExpenseOffshore.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOtherExpenseOffshore.setDescription(ProjectUtils.OTHER_VENDOR_EXPENSE);
            forecastVendorOtherExpenseOffshore.setSubCategory(ProjectUtils.OFFSHORE);
            forecastVendorOtherExpenseOffshore.setIsAggregate(false);
            forecastVendorOtherExpenseOffshore.setData(setExpenseValues(startMonth, getVendorForecastOtherExpenseOffshoreDetails.get(0)));
            projectFinanceForecastVendorLaborExpense.add(forecastVendorOtherExpenseOffshore);
        } else {
            SummaryDataResultDto forecastVendorOtherExpenseOffshore = new SummaryDataResultDto();
            forecastVendorOtherExpenseOffshore.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOtherExpenseOffshore.setDescription(ProjectUtils.OTHER_VENDOR_EXPENSE);
            forecastVendorOtherExpenseOffshore.setSubCategory(ProjectUtils.OFFSHORE);
            forecastVendorOtherExpenseOffshore.setIsAggregate(false);
            forecastVendorOtherExpenseOffshore.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceForecastVendorLaborExpense.add(forecastVendorOtherExpenseOffshore);
        }

        // Total offshore vendor Salaries
        if (projectFinanceForecastVendorLaborExpense != null && !projectFinanceForecastVendorLaborExpense.isEmpty()) {
            List<Double> vendorLaborExpensesOffshoreTAndMData = projectFinanceForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.T_AND_M)
                            && it.getSubCategory().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> vendorLaborExpensesOffshoreOtherData = projectFinanceForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.OTHER_VENDOR_EXPENSE)
                            && it.getSubCategory().equals(ProjectUtils.OFFSHORE)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

            List<Double> totalEmployeeSalariesList = new ArrayList<>();
            for (Integer i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                Double total = vendorLaborExpensesOffshoreTAndMData.get(i) + vendorLaborExpensesOffshoreOtherData.get(i);
                totalEmployeeSalariesList.add(total);
            }

            SummaryDataResultDto forecastVendorOffshoreAggregate = new SummaryDataResultDto();
            forecastVendorOffshoreAggregate.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorOffshoreAggregate.setDescription(ProjectUtils.OFFSHORE);
            forecastVendorOffshoreAggregate.setSubCategory(null);
            forecastVendorOffshoreAggregate.setData(totalEmployeeSalariesList);
            forecastVendorOffshoreAggregate.setIsAggregate(true);
            projectFinanceForecastVendorLaborExpense.add(forecastVendorOffshoreAggregate);
        }

        // Total Vendor Expenses
        if (projectFinanceForecastVendorLaborExpense != null && !projectFinanceForecastVendorLaborExpense.isEmpty()) {
            List<Double> summaryForecastOnsiteAggregatedData = projectFinanceForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.ONSITE))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> summaryForecastOffshoreAggregatedData = projectFinanceForecastVendorLaborExpense.stream()
                    .filter(it -> it.getDescription().equals(ProjectUtils.OFFSHORE))
                    .collect(Collectors.toList()).get(0).getData();

            List<Double> totalVendorLaborExpensesTotalList = new ArrayList<>();


            for (int i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                totalVendorLaborExpensesTotalList.add(summaryForecastOnsiteAggregatedData.get(i) + summaryForecastOffshoreAggregatedData.get(i));
            }

            SummaryDataResultDto forecastVendorTotalAggregate = new SummaryDataResultDto();
            forecastVendorTotalAggregate.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
            forecastVendorTotalAggregate.setDescription(ProjectUtils.TOTAL_VENDOR_LABOR_EXPENSES);
            forecastVendorTotalAggregate.setSubCategory(null);
            forecastVendorTotalAggregate.setIsAggregate(true);
            forecastVendorTotalAggregate.setData(totalVendorLaborExpensesTotalList);
            projectFinanceForecastVendorLaborExpense.add(forecastVendorTotalAggregate);
        }

        if(projectFinanceForecastVendorLaborExpense.isEmpty()){
            projectFinanceForecastVendorLaborExpense = null;
        }

        return projectFinanceForecastVendorLaborExpense;
    }


    */
    /*
    Project Finance Total Capex Related Expense
     */
    /*

    //    Project Finance Budget
    public List<SummaryDataResultDto> getProjectFinanceBudgetTotalCapex(Integer financialYear, List<Integer> projectIds){

//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> projectFinanceBudgetTotalCapexExpense = new ArrayList<>();

//        get summary budget Total Capex data
        String totalCapexExpenseQuery = ProjectUtils.getFile("projectfinance/budget/getProjectFinanceBudgetTotalCapex.txt");

        Query query = entityManager.createNativeQuery(totalCapexExpenseQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query.setParameter("financialYear", financialYear);
        query.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getTotalCapexDetails = query.getResultList();

        if(!getTotalCapexDetails.isEmpty()){
            for(SummaryDataResult eachResult : getTotalCapexDetails){
                SummaryDataResultDto capexEachCategory = new SummaryDataResultDto();
                capexEachCategory.setTableType(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES);
                capexEachCategory.setDescription(eachResult.getTitle());
                capexEachCategory.setSubCategory(null);
                capexEachCategory.setIsAggregate(false);
                capexEachCategory.setData(setExpenseValues(startMonth, eachResult));
                projectFinanceBudgetTotalCapexExpense.add(capexEachCategory);
            }
        }
        else{
            SummaryDataResultDto capexEachCategory = new SummaryDataResultDto();
            capexEachCategory.setTableType(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES);
            capexEachCategory.setDescription(ProjectUtils.EMPTY_CAPEX_OTHER_EXPENSES);
            capexEachCategory.setSubCategory(null);
            capexEachCategory.setIsAggregate(false);
            capexEachCategory.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceBudgetTotalCapexExpense.add(capexEachCategory);
        }

        // Total Capex Expenses total
        if (!projectFinanceBudgetTotalCapexExpense.isEmpty()) {
            List<Double> totalcapexRelatedExpenseTotalList = new ArrayList<>(Collections.nCopies(12, 0d));

            List<SummaryDataResultDto> summaryBudgetTotalCapexExpenseCategoryList = projectFinanceBudgetTotalCapexExpense.stream()
                    .filter(it -> it.getTableType().equals(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES))
                    .collect(Collectors.toList());

            for (SummaryDataResultDto summaryDataResultDto : summaryBudgetTotalCapexExpenseCategoryList) {
                List<Double> dataList = summaryDataResultDto.getData();
                for (int i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                    totalcapexRelatedExpenseTotalList.set(i, totalcapexRelatedExpenseTotalList.get(i) + dataList.get(i));
                }
            }

            SummaryDataResultDto budgetCapexRelatedTotalAggregate = new SummaryDataResultDto();
            budgetCapexRelatedTotalAggregate.setTableType(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES);
            budgetCapexRelatedTotalAggregate.setDescription(ProjectUtils.TOTAL_CAPEX_EXPENSES_TOTAL);
            budgetCapexRelatedTotalAggregate.setSubCategory(null);
            budgetCapexRelatedTotalAggregate.setIsAggregate(true);
            budgetCapexRelatedTotalAggregate.setData(totalcapexRelatedExpenseTotalList);
            projectFinanceBudgetTotalCapexExpense.add(budgetCapexRelatedTotalAggregate);
        }

        if(projectFinanceBudgetTotalCapexExpense.isEmpty()){
            projectFinanceBudgetTotalCapexExpense=null;
        }

        return projectFinanceBudgetTotalCapexExpense;
    }

    //    Project Finance Forecast
    public List<SummaryDataResultDto> getProjectFinanceForecastTotalCapex(Integer financialYear, List<Integer> projectIds){

//        retrieve the starting month of the financial year from admin general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();

        List<SummaryDataResultDto> projectFinanceForecastTotalCapexExpense = new ArrayList<>();

//        get summary forecast Total Capex data
        String totalCapexExpenseQuery = ProjectUtils.getFile("projectfinance/forecast/getProjectFinanceForecastTotalCapex.txt");

        Query query = entityManager.createNativeQuery(totalCapexExpenseQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(SummaryDataResult.class));

        query.setParameter("financialYear", financialYear);
        query.setParameter("projectIds", convertIntegerArrayToStringArray(projectIds));

        List<SummaryDataResult> getTotalCapexDetails = query.getResultList();

        if(!getTotalCapexDetails.isEmpty()){
            for(SummaryDataResult eachResult : getTotalCapexDetails){
                SummaryDataResultDto capexEachCategory = new SummaryDataResultDto();
                capexEachCategory.setTableType(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES);
                capexEachCategory.setDescription(eachResult.getTitle());
                capexEachCategory.setSubCategory(null);
                capexEachCategory.setIsAggregate(false);
                capexEachCategory.setData(setExpenseValues(startMonth, eachResult));
                projectFinanceForecastTotalCapexExpense.add(capexEachCategory);
            }
        }
        else{
            SummaryDataResultDto capexEachCategory = new SummaryDataResultDto();
            capexEachCategory.setTableType(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES);
            capexEachCategory.setDescription(ProjectUtils.EMPTY_CAPEX_OTHER_EXPENSES);
            capexEachCategory.setSubCategory(null);
            capexEachCategory.setIsAggregate(false);
            capexEachCategory.setData(setExpenseValues(startMonth, new SummaryDataResult()));
            projectFinanceForecastTotalCapexExpense.add(capexEachCategory);
        }

        // Total Capex Expenses total
        if (!projectFinanceForecastTotalCapexExpense.isEmpty()) {
            List<Double> totalcapexRelatedExpenseTotalList = new ArrayList<>(Collections.nCopies(12, 0d));

            List<SummaryDataResultDto> summaryForecastTotalCapexExpenseCategoryList = projectFinanceForecastTotalCapexExpense.stream()
                    .filter(it -> it.getTableType().equals(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES))
                    .collect(Collectors.toList());

            for (SummaryDataResultDto summaryDataResultDto : summaryForecastTotalCapexExpenseCategoryList) {
                List<Double> dataList = summaryDataResultDto.getData();
                for (int i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
                    totalcapexRelatedExpenseTotalList.set(i, totalcapexRelatedExpenseTotalList.get(i) + dataList.get(i));
                }
            }

            SummaryDataResultDto forecastCapexRelatedTotalAggregate = new SummaryDataResultDto();
            forecastCapexRelatedTotalAggregate.setTableType(ProjectUtils.TOTAL_CAPEX_RELATED_EXPENSES);
            forecastCapexRelatedTotalAggregate.setDescription(ProjectUtils.TOTAL_CAPEX_EXPENSES_TOTAL);
            forecastCapexRelatedTotalAggregate.setSubCategory(null);
            forecastCapexRelatedTotalAggregate.setIsAggregate(true);
            forecastCapexRelatedTotalAggregate.setData(totalcapexRelatedExpenseTotalList);
            projectFinanceForecastTotalCapexExpense.add(forecastCapexRelatedTotalAggregate);
        }

        if(projectFinanceForecastTotalCapexExpense.isEmpty()){
            projectFinanceForecastTotalCapexExpense=null;
        }

        return projectFinanceForecastTotalCapexExpense;
    }

//    total project finance

//    budget
    public List<SummaryDataResultDto> getProjectFinanceBudgetSummary (Integer financialYear, List<Integer> projectIds){

        List<ProjectMgmt> projectMgmt = projectMgmtRepo.findAllById(projectIds);

        if (projectMgmt.isEmpty()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Project Not Found");
        }

        List<SummaryDataResultDto> getProjectFinanceBudget = new ArrayList<>();

        List<SummaryDataResultDto> getProjectFinanceBudgetInternalLabor = getProjectFinanceBudgetInternalLabor(financialYear, projectIds);
        List<SummaryDataResultDto> getProjectFinanceBudgetVendorLabor = getProjectFinanceBudgetVendorLabor(financialYear, projectIds);
        List<SummaryDataResultDto> getProjectFinanceBudgetTotalCapex = getProjectFinanceBudgetTotalCapex(financialYear, projectIds);

        getProjectFinanceBudget.addAll(getProjectFinanceBudgetInternalLabor);
        getProjectFinanceBudget.addAll(getProjectFinanceBudgetVendorLabor);

        List<Double> getTotalInternalLaborExpense = getProjectFinanceBudgetInternalLabor.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getTotalVendorLaborExpense = getProjectFinanceBudgetVendorLabor.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_VENDOR_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> totalBudgetSpentTotalList = new ArrayList<>(Collections.nCopies(12, 0d));

        for (int i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
            totalBudgetSpentTotalList.set(i, totalBudgetSpentTotalList.get(i) + getTotalInternalLaborExpense.get(i) + getTotalVendorLaborExpense.get(i));
        }

        SummaryDataResultDto budgetTotalBudgetSpent = new SummaryDataResultDto();
        budgetTotalBudgetSpent.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
        budgetTotalBudgetSpent.setDescription(ProjectUtils.TOTAL_BUDGET_EXPENSES);
        budgetTotalBudgetSpent.setSubCategory(null);
        budgetTotalBudgetSpent.setIsAggregate(true);
        budgetTotalBudgetSpent.setData(totalBudgetSpentTotalList);
        getProjectFinanceBudget.add(budgetTotalBudgetSpent);

        getProjectFinanceBudget.addAll(getProjectFinanceBudgetTotalCapex);

        return getProjectFinanceBudget;
    }

//    forecast
    public List<SummaryDataResultDto> getProjectFinanceForecastSummary (Integer financialYear, List<Integer> projectIds){

        List<ProjectMgmt> projectMgmt = projectMgmtRepo.findAllById(projectIds);

        if (projectMgmt.isEmpty()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Project Not Found");
        }

        List<SummaryDataResultDto> getProjectFinanceForecast = new ArrayList<>();

        List<SummaryDataResultDto> getProjectFinanceForecastInternalLabor = getProjectFinanceForecastInternalLabor(financialYear, projectIds);
        List<SummaryDataResultDto> getProjectFinanceForecastVendorLabor = getProjectFinanceForecastVendorLabor(financialYear, projectIds);
        List<SummaryDataResultDto> getProjectFinanceForecastTotalCapex = getProjectFinanceForecastTotalCapex(financialYear, projectIds);

        getProjectFinanceForecast.addAll(getProjectFinanceForecastInternalLabor);
        getProjectFinanceForecast.addAll(getProjectFinanceForecastVendorLabor);

        List<Double> getTotalInternalLaborExpense = getProjectFinanceForecastInternalLabor.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.INTERNAL_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_INTERNAL_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> getTotalVendorLaborExpense = getProjectFinanceForecastVendorLabor.stream()
                .filter(it -> it.getTableType().equals(ProjectUtils.VENDOR_LABOR_EXPENSES)
                        && it.getDescription().equals(ProjectUtils.TOTAL_VENDOR_LABOR_EXPENSES)).flatMap(it -> it.getData().stream()).collect(Collectors.toList());

        List<Double> totalForecastSpentTotalList = new ArrayList<>(Collections.nCopies(12, 0d));

        for (int i = ProjectUtils.LOOP_START_POINT; i <= ProjectUtils.LOOP_END_POINT; i++) {
            totalForecastSpentTotalList.set(i, totalForecastSpentTotalList.get(i) + getTotalInternalLaborExpense.get(i) + getTotalVendorLaborExpense.get(i));
        }

        SummaryDataResultDto forecastTotalForecastSpent = new SummaryDataResultDto();
        forecastTotalForecastSpent.setTableType(ProjectUtils.VENDOR_LABOR_EXPENSES);
        forecastTotalForecastSpent.setDescription(ProjectUtils.TOTAL_FORECAST_EXPENSES);
        forecastTotalForecastSpent.setSubCategory(null);
        forecastTotalForecastSpent.setIsAggregate(true);
        forecastTotalForecastSpent.setData(totalForecastSpentTotalList);
        getProjectFinanceForecast.add(forecastTotalForecastSpent);

        getProjectFinanceForecast.addAll(getProjectFinanceForecastTotalCapex);

        return getProjectFinanceForecast;
    }
    */


    /*public Object saveResourceRequestByProjects(List<PostRequestedResource> postRequestedResourceList, String modifiedBy) {

        List<RequestedResources> requestedResources = new ArrayList<>();
        for (PostRequestedResource postRequestedResource : postRequestedResourceList) {
            RequestedResources requestedResource = new RequestedResources();
            requestedResource.setProjectId(postRequestedResource.getProjectId());
            requestedResource.setRequestedRoleId(postRequestedResource.getRoleId());
            requestedResource.setRequestedResourceCount(1);
            requestedResource.setRequestStatus(postRequestedResource.getRequestedStatus());
            requestedResource.setRequestedResourceId(postRequestedResource.getResourceId());
            requestedResource.setAllocationStartDate(postRequestedResource.getAllocationStartDate());
            requestedResource.setAllocationEndDate(postRequestedResource.getAllocationEndDate());
            requestedResource.setNotify(postRequestedResource.getNotify());
            requestedResource.setCreatedBy(modifiedBy);
            requestedResource.setCreatedDate(new Date());
            requestedResource.setModifiedBy(modifiedBy);
            requestedResource.setModifiedDate(new Date());
            requestedResources.add(requestedResource);
        }
        if(!requestedResources.isEmpty()) {
            requestedResourcesRepo.saveAll(requestedResources);
        }
        return true;
    }*/

//    public List<GetRequestedResourcesData> fetchResourceRequestData()    {
//        List<GetRequestedResourcesData> getRequestedResourcesData = requestedResourcesRepo.getAllRequestedResourceData();
//        for(int i=0; i<getRequestedResourcesData.size(); i++){
//            GetRequestedResourcesData eachRequest = getRequestedResourcesData.get(i);
//            eachRequest.setAllocationStartDate(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getAllocationStartDate().getTime()).toLocalDate()));
//            eachRequest.setAllocationEndDate(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getAllocationEndDate().getTime()).toLocalDate()));
//            eachRequest.setCreatedDate(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getCreatedDate().getTime()).toLocalDate()));
//            eachRequest.setModifiedDate(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getModifiedDate().getTime()).toLocalDate()));
//            getRequestedResourcesData.set(i, eachRequest);
//        }
////       List<RequestedResources> requestedResources = requstedResourcesRepo.findAll();
////       for(RequestedResources requstedResource: requestedResources){
////           GetRequestedResourcesData getRequestedResources = new GetRequestedResourcesData();
////
////           getRequestedResources.setRequestedId(requstedResource.getRequestId());
////
////          String resourceName =  resourceMgmtRepo.getResourceNameByresourceId(requstedResource.getRequestedResourceId());
////           getRequestedResources.setResourceName(resourceName);
////           String roleNameByroleId = resourceMgmtRolesRepo.roleNameByroleId(requstedResource.getRequestedRoleId());
////           getRequestedResources.setRole(roleNameByroleId);
////
////           getRequestedResources.setProjectName(null);
////           getRequestedResources.setAllocationStartDate(requstedResource.getAllocationStartDate());
////           getRequestedResources.setAllocationEndDate(requstedResource.getAllocationEndDate());
////           getRequestedResources.setRequestedFte(requstedResource.getFte());
////           getRequestedResourcesData.add(getRequestedResources);
//
////       }
//
//        return getRequestedResourcesData;
//    }

}



