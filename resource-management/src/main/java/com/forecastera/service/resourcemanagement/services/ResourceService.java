package com.forecastera.service.resourcemanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 12-06-2023
 * @Description
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.resourcemanagement.commonResponseUtil.Error;
import com.forecastera.service.resourcemanagement.config.exception.DataNotFoundException;
import com.forecastera.service.resourcemanagement.dto.request.*;
import com.forecastera.service.resourcemanagement.dto.response.*;
import com.forecastera.service.resourcemanagement.dto.utilityClass.*;
import com.forecastera.service.resourcemanagement.entity.*;
import com.forecastera.service.resourcemanagement.repository.*;
import com.forecastera.service.resourcemanagement.util.ResourceUtils;
import org.apache.commons.lang.NullArgumentException;
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
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ResourceService {
    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    private ExcelProcessingService excelProcessingService;

    @Autowired
    private ResourceMgmtRepo resourceMgmtRepo;

    @Autowired
    private ResourceMgmtFieldValueRepo resourceMgmtFieldValueRepo;

    @Autowired
    private ResourceMgmtFieldMapRepo resourceMgmtFieldMapRepo;

    @Autowired
    private ResourceCustomPicklistRepo resourceCustomPicklistRepo;

    @Autowired
    private ResourceMgmtRolesRepo resourceMgmtRolesRepo;

    @Autowired
    private ResourceMgmtDepartmentRepo resourceMgmtDepartmentRepo;

    @Autowired
    private ResourceMgmtSkillsRepo resourceMgmtSkillsRepo;

    @Autowired
    private ResourceMgmtEmploymentTypeRepo resourceMgmtEmploymentTypeRepo;

    @Autowired
    private ProjectResourceMappingRepo projectResourceMappingRepo;

    @Autowired
    private ResourceMgmtStatusRepo resourceMgmtStatusRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GeneralSettingsRepo generalSettingsRepo;

    @Autowired
    private GeneralSettingsLocationRepo generalSettingsLocationRepo;

    @Autowired
    private FinanceMgmtFieldMapRepo financeMgmtFieldMapRepo;

    @Autowired
    private ProjectResourceMappingExtendedRepo projectResourceMappingExtendedRepo;

    @Autowired
    private ProjectMgmtRepo projectMgmtRepo;

    @Autowired
    private RequestedResourcesRepo requestedResourcesRepo;

    @Autowired
    private DelegatedDepartmentHistoryRepo delegatedDepartmentHistoryRepo;

    @Autowired
    private DelegatedResourceHistoryRepo delegatedResourceHistoryRepo;

    private final EntityManagerFactory emf;
    private final EntityManager entityManager;

    public ResourceService(EntityManagerFactory emf, EntityManager entityManager) {
        this.emf = emf;
        this.entityManager = emf.createEntityManager();
    }

    public Object getAll(String email) {
//        List<Integer> listOfVisibleDepartments = getVisibleDepartmentIds("ab@51.com");
//        return getVisibleResourceIds(email);
        return null;
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

//            get department hierarchy
            String getDepartmentHierarchyQuery = ResourceUtils.getFile("getDepartmentHierarchyQuery.txt");
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

            listOfVisibleDepartmentIds.add(allDepartmentsUnderResource.get(0).getDepartmentId());

        }

        if(listOfVisibleDepartmentIds!=null && !listOfVisibleDepartmentIds.isEmpty()) {
            return new ArrayList<>(listOfVisibleDepartmentIds);
        }
        else{
            return new ArrayList<>();
        }
    }*/

    private List<Integer> getVisibleResourceIds(String email, String role){

        Set<Integer> listOfVisibleResourceIds = null;

        if(role.equals(ResourceUtils.USER_ROLE_MANAGER)) {
//        get list of all managed department by this resource, if it exists
            List<ResourceMgmt> resourceMgmt = resourceMgmtRepo.getResourceByEmail(email);

//        if resource exists
            if (resourceMgmt != null && !resourceMgmt.isEmpty()) {
                ResourceMgmt currentUser = resourceMgmt.get(0);

                Set<Integer> listOfManagedResourceIds = new HashSet<>();
                listOfManagedResourceIds.add(currentUser.getResourceId());

//            collect list of delegated resources from the resource

                List<DelegatedResourceHistory> delegatedResourceHistoryList = delegatedResourceHistoryRepo.findAll();
                for (DelegatedResourceHistory eachRecord : delegatedResourceHistoryList) {
                    LocalDate localDate = LocalDate.now();
                    Date currentDate = java.sql.Date.valueOf(localDate);
                    if (eachRecord.getIsActive().equals("1") && eachRecord.getDelegatedToResourceId().equals(currentUser.getResourceId())
                            && eachRecord.getDelegationStartDate().compareTo(currentDate) <= 0
                            && eachRecord.getDelegationEndDate().compareTo(currentDate) >= 0) {
                        listOfManagedResourceIds.add(eachRecord.getDelegatedResourceId());
                    }
                }

//            get resource hierarchy
                String getResourceHierarchyQuery = ResourceUtils.getFile("getResourceHierarchyQuery.txt");
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
                            if (!listOfVisibleResourceIds.contains(topNode.getResource_id())) {
                                listOfVisibleResourceIds.add(topNode.getResource_id());
                                Integer depthLevel = topNode.getDepth_order();
                                i++;
                                while (i < getResourceHierarchy.size() && getResourceHierarchy.get(i).getDepth_order() > depthLevel) {
                                    if (listOfVisibleResourceIds.contains(getResourceHierarchy.get(i).getResource_id())) {
                                        break;
                                    } else {
                                        listOfVisibleResourceIds.add(getResourceHierarchy.get(i).getResource_id());
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                }

//            in case user cannot see his own record, please uncomment this
                List<ResourceMgmtFieldValue> resourceMgmtFieldValueList = resourceMgmtRepo.getResourceReportingManagerById(currentUser.getResourceId());
                if (resourceMgmtFieldValueList != null && !resourceMgmtFieldValueList.isEmpty()
                        && resourceMgmtFieldValueList.get(0).getFieldValue() != null && !resourceMgmtFieldValueList.get(0).getFieldValue().isEmpty()
                        && !resourceMgmtFieldValueList.get(0).getFieldValue().equals("0")) {
                    listOfVisibleResourceIds.remove(currentUser.getResourceId());
                }
                listOfManagedResourceIds.remove(currentUser.getResourceId());
                listOfVisibleResourceIds.removeAll(listOfManagedResourceIds);
            }

            if (listOfVisibleResourceIds != null && !listOfVisibleResourceIds.isEmpty()) {
                return new ArrayList<>(listOfVisibleResourceIds);
            } else {
                return new ArrayList<>();
            }
        }
        else if(role.equals(ResourceUtils.USER_ROLE_RESOURCE_MANAGER)){
            List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.findAll();
            listOfVisibleResourceIds = resourceMgmtList.stream().map(ResourceMgmt::getResourceId).collect(Collectors.toSet());
            if (!listOfVisibleResourceIds.isEmpty()) {
                return new ArrayList<>(listOfVisibleResourceIds);
            } else {
                return new ArrayList<>();
            }
        }
        else{
            return new ArrayList<>();
        }
    }

//    need to think this through
//    TODO:: should show all departments reporting to him as well?
    public List<DepartmentIdName> departmentDropdownOptions(String email, String role){
        List<Integer> getVisibleResourceIdList = getVisibleResourceIds(email, role);

        Set<String> getResourceDepartmentIdList = new HashSet<>(resourceMgmtRepo.getAllResourceDepartmentList(getVisibleResourceIdList));
        List<Integer> getVisibleDepartmentIdList = new ArrayList<>();

        for(String eachDepartmentId: getResourceDepartmentIdList){
            getVisibleDepartmentIdList.add(Integer.valueOf(eachDepartmentId));
        }

        List<ResourceMgmtDepartment> getDepartmentList = resourceMgmtDepartmentRepo.findAllById(getVisibleDepartmentIdList);
        List<DepartmentIdName> getResourcesByDepartmentList = new ArrayList<>();
        for(ResourceMgmtDepartment eachDepartment: getDepartmentList){
            if(eachDepartment.getIsActive().equals("1")) {
                getResourcesByDepartmentList.add(new DepartmentIdName(eachDepartment.getDepartmentId(), eachDepartment.getDepartment()));
            }
        }
        if(!getResourcesByDepartmentList.isEmpty()){
            getResourcesByDepartmentList.sort(Comparator.comparing(DepartmentIdName::getDepartmentName));
        }
        else{
            getResourcesByDepartmentList = null;
        }

        return getResourcesByDepartmentList;
    }

    public List<ResourceIdNameDetail> getResourceListForDepartmentHead(){
        List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.findAll();
        List<ResourceIdNameDetail> validResourceList = new ArrayList<>();
        for(ResourceMgmt eachResource: resourceMgmtList){
            if(eachResource.getLastWorkingDate()==null || eachResource.getLastWorkingDate().compareTo(new Date())>0){
                validResourceList.add(new ResourceIdNameDetail(eachResource.getResourceId(), eachResource.getFirstName() + " " + eachResource.getLastName()));
            }
        }

        if(validResourceList.isEmpty()){
            validResourceList = null;
        }
        else{
            validResourceList.sort(Comparator.comparing(ResourceIdNameDetail::getResourceName, (r1,r2)->{
                String resource1 = r1.toLowerCase();
                String resource2 = r2.toLowerCase();

                return String.CASE_INSENSITIVE_ORDER.compare(resource1, resource2);
            }));
        }

        return validResourceList;
    }

    public List<GetTotalResourceDetailsMaster> getTotalResource(GetDateRange getDateRange, Integer departmentId, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

//        List<Integer> getVisibleDepartmentIdsList = getVisibleDepartmentIds(email);
//        List<String> getVisibleDepartmentIdsSet = new ArrayList<>();
//        for(Integer i:getVisibleDepartmentIdsList){
//            getVisibleDepartmentIdsSet.add(String.valueOf(i));
//        }

        List<Integer> getVisibleResourceIds = getVisibleResourceIds(email, role);


        List<GetTotalResource> getTotalResourceList;

        if(departmentId!=null && departmentId!=0){
            getTotalResourceList = resourceMgmtRepo.getTotalResourceByDepartmentId(getDateRange.getStartDate(), getDateRange.getEndDate(), departmentId);
        }
        else{
            getTotalResourceList = resourceMgmtRepo.getTotalResource(getDateRange.getStartDate(), getDateRange.getEndDate(), getVisibleResourceIds);
        }



        List<ResourceMgmtEmploymentType> resourceMgmtEmploymentTypeList = resourceMgmtEmploymentTypeRepo.findAll();

        Map<Integer, Long> resourceCount = new HashMap<>();
        Map<Integer, List<ResourceIdNameDetail>> resourceDetailsMap = new HashMap<>();

        for (GetTotalResource eachResource : getTotalResourceList) {

            Integer statusId = null;

            for (ResourceMgmtEmploymentType eachType : resourceMgmtEmploymentTypeList) {
                if (eachType.getEmpTypeId().equals(eachResource.getEmploymentTypeId())){
                    statusId = eachType.getEmpTypeId();
                    break;
                }
            }

            resourceCount.merge(statusId, 1L, Long::sum);

            List<ResourceIdNameDetail> resourceDetailsForCharts;
            if(resourceDetailsMap.containsKey(statusId)){
                resourceDetailsForCharts = resourceDetailsMap.get(statusId);
            }
            else{
                resourceDetailsForCharts = new ArrayList<>();
            }
            resourceDetailsForCharts.add(new ResourceIdNameDetail(eachResource.getResourceId(), eachResource.getResourceName()));
            resourceDetailsMap.put(statusId, resourceDetailsForCharts);

        }

        List<GetTotalResourceDetailsMaster> getTotalResourceListMaster = new ArrayList<>();

        for (ResourceMgmtEmploymentType eachType : resourceMgmtEmploymentTypeList) {
            if (resourceCount.containsKey(eachType.getEmpTypeId())) {
                GetTotalResourceDetailsMaster eachLevel = new GetTotalResourceDetailsMaster();
                eachLevel.setEmploymentTypeId(eachType.getEmpTypeId());
                eachLevel.setEmploymentTypeColor(eachType.getColor());
                eachLevel.setResourceCount(resourceCount.get(eachType.getEmpTypeId()));
                eachLevel.setEmploymentTypeName(eachType.getEmploymentType());
                List<ResourceIdNameDetail> resourceDetailsForCurrentStatus = resourceDetailsMap.get(eachType.getEmpTypeId());
                resourceDetailsForCurrentStatus.sort(Comparator.comparing(ResourceIdNameDetail::getResourceName, (r1, r2)->{
                    String resource1 = r1.toLowerCase();
                    String resource2 = r2.toLowerCase();
                    return String.CASE_INSENSITIVE_ORDER.compare(resource1, resource2);
                }));
                eachLevel.setResourceDetails(resourceDetailsForCurrentStatus);
                getTotalResourceListMaster.add(eachLevel);
            }
        }

        if(getTotalResourceListMaster.isEmpty()){
            getTotalResourceListMaster = null;
        }

        return getTotalResourceListMaster;
    }

    public List<GetUtilizationLevel> getUtilizationLevel(GetDateRange getDateRange, Integer departmentId, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        String getUtilizationLevelQuery = ResourceUtils.getFile("getUtilizationLevelData.txt");

        if(departmentId!=null && departmentId!=0){
            getUtilizationLevelQuery = ResourceUtils.getFile("getUtilizationLevelDataByDepartmentId.txt");
        }

        Query query = entityManager.createNativeQuery(getUtilizationLevelQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourceFteDetails.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        if(departmentId!=null && departmentId!=0){
            query.setParameter("departmentId", departmentId);
        }
        else{
            List<Integer> getVisibleResourceIds = getVisibleResourceIds(email, role);
            query.setParameter("resourceIds", getVisibleResourceIds);
        }

        List<GetResourceFteDetails> resourceFteDetails = query.getResultList();
//        List<GetResourceFteDetails> resourceFteDetails = resourceMgmtRepo.getResourceAvgFte(getDateRange.getStartDate(), getDateRange.getEndDate());
//        System.out.println(resourceFteDetails);

        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        List<ResourceMgmtStatus> resourceMgmtStatusList = new ArrayList<>();
        Double maxEnabledFteInAdmin = -1d;
        Integer maxEnabledFteInAdminId = null;

        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                resourceMgmtStatusList.add(eachStatus);
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                    maxEnabledFteInAdminId = eachStatus.getStatusId();
                }
            }
        }
        if(!resourceMgmtStatusList.isEmpty()){
            resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
        }
        Map<Integer, Long> resourceCount = new HashMap<>();
        Map<Integer, List<ResourceDetailsForChart>> resourceDetailsMap = new HashMap<>();

        LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
        LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();

        Integer workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

        for (GetResourceFteDetails eachResource : resourceFteDetails) {

            Double resourceFteAvg;

            if((eachResource.getStart_date().compareTo(getDateRange.getStartDate())>0) || (eachResource.getEnd_date().compareTo(getDateRange.getEndDate())<=0)){
                Date resourceStartDate = eachResource.getStart_date().compareTo(getDateRange.getStartDate())>0?eachResource.getStart_date():getDateRange.getStartDate();
                Date resourceEndDate = eachResource.getEnd_date().compareTo(getDateRange.getEndDate())<0?eachResource.getEnd_date():getDateRange.getEndDate();

                LocalDate localStartDate = new java.sql.Date(resourceStartDate.getTime()).toLocalDate();
                LocalDate localEndDate = new java.sql.Date(resourceEndDate.getTime()).toLocalDate();
                resourceFteAvg = eachResource.getSum_fte().doubleValue()/ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
            }
            else{
                resourceFteAvg = eachResource.getSum_fte().doubleValue()/workingDays;
            }
            resourceFteAvg = ResourceUtils.roundToTwoDecimalPlace(resourceFteAvg);

            boolean ifAppropriateStatusDoesNotExist = true;

            Integer statusId = null;

            for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() !=null && eachStatus.getEndValue()!=null){
                    if(resourceFteAvg >= eachStatus.getStartValue() && resourceFteAvg < eachStatus.getEndValue()) {
                        statusId = eachStatus.getStatusId();
                        ifAppropriateStatusDoesNotExist = false;
                        break;
                    }
                }
            }
            if (ifAppropriateStatusDoesNotExist) {
                if(maxEnabledFteInAdminId!=null && resourceFteAvg.equals(maxEnabledFteInAdmin)){
                    statusId = maxEnabledFteInAdminId;
                }
                else {
                    statusId = ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID;
                }
            }

            resourceCount.merge(statusId, 1L, Long::sum);

            List<ResourceDetailsForChart> resourceDetailsForCharts;
            if(resourceDetailsMap.containsKey(statusId)){
                resourceDetailsForCharts = resourceDetailsMap.get(statusId);
            }
            else{
                resourceDetailsForCharts = new ArrayList<>();
            }
            resourceDetailsForCharts.add(new ResourceDetailsForChart(eachResource.getResource_id(), eachResource.getResource_name(), eachResource.getDepartment_name(), resourceFteAvg));
            resourceDetailsMap.put(statusId, resourceDetailsForCharts);
        }

//        System.out.println(resourceCount);

        List<GetUtilizationLevel> getUtilizationLevelList = new ArrayList<>();

        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
            if (resourceCount.containsKey(eachStatus.getStatusId())) {
                GetUtilizationLevel eachLevel = new GetUtilizationLevel();
                eachLevel.setStatusId(eachStatus.getStatusId());
                eachLevel.setColor(eachStatus.getColor());
                eachLevel.setResourceCount(resourceCount.get(eachStatus.getStatusId()));
                eachLevel.setStatusName(eachStatus.getStatus());
                List<ResourceDetailsForChart> resourceDetailsForCurrentStatus = resourceDetailsMap.get(eachStatus.getStatusId());
                resourceDetailsForCurrentStatus.sort(Comparator.comparing(ResourceDetailsForChart::getResourceFteAvg).reversed()
                        .thenComparing(ResourceDetailsForChart::getResourceName, (s1, s2)->{
                            String resource1 = s1.toLowerCase();
                            String resource2 = s2.toLowerCase();
                            return String.CASE_INSENSITIVE_ORDER.compare(resource1, resource2);
                        }));
                eachLevel.setResourceDetails(resourceDetailsForCurrentStatus);
                getUtilizationLevelList.add(eachLevel);
            }
        }
        if(resourceCount.containsKey(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID)){
            GetUtilizationLevel eachLevel = new GetUtilizationLevel();
            eachLevel.setStatusId(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID);
            eachLevel.setColor(ResourceUtils.UNDEFINED_UTILIZATION_COLOR);
            eachLevel.setResourceCount(resourceCount.get(0));
            eachLevel.setStatusName(ResourceUtils.UNDEFINED_UTILIZATION_NAME);
            List<ResourceDetailsForChart> resourceDetailsForCurrentStatus = resourceDetailsMap.get(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID);
            resourceDetailsForCurrentStatus.sort(Comparator.comparing(ResourceDetailsForChart::getResourceFteAvg).reversed()
                    .thenComparing(ResourceDetailsForChart::getResourceName, (s1, s2)->{
                        String resource1 = s1.toLowerCase();
                        String resource2 = s2.toLowerCase();
                        return String.CASE_INSENSITIVE_ORDER.compare(resource1, resource2);
                    }));
            eachLevel.setResourceDetails(resourceDetailsForCurrentStatus);
            getUtilizationLevelList.add(eachLevel);
        }

        if(getUtilizationLevelList.isEmpty()){
            getUtilizationLevelList = null;
        }
        return getUtilizationLevelList;
    }

    public Map<String, Long> getUtilizationPercent(GetDateRange getDateRange, Integer departmentId, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        String getUtilizationLevelQuery = ResourceUtils.getFile("getUtilizationLevelData.txt");

        if(departmentId!=null && departmentId!=0){
            getUtilizationLevelQuery = ResourceUtils.getFile("getUtilizationLevelDataByDepartmentId.txt");
        }

        Query query = entityManager.createNativeQuery(getUtilizationLevelQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourceFteDetails.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        if(departmentId!=null && departmentId!=0){
            query.setParameter("departmentId", departmentId);
        }
        else{
            List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);
            query.setParameter("resourceIds", visibleResourceIds);
        }

        List<GetResourceFteDetails> resourceFteDetails = query.getResultList();

        if(resourceFteDetails==null || resourceFteDetails.isEmpty()){
            return null;
        }

        double utilization = 0;
        int resourceCount = resourceFteDetails.size();

        LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
        LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();

        Integer workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

        for (GetResourceFteDetails eachResource : resourceFteDetails) {

            Double resourceFteAvg;

            if((eachResource.getStart_date().compareTo(getDateRange.getStartDate())!=0) || (eachResource.getEnd_date().compareTo(getDateRange.getEndDate())!=0)){
                LocalDate localStartDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
                LocalDate localEndDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
                resourceFteAvg = eachResource.getSum_fte().doubleValue()/ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
            }
            else{
                resourceFteAvg = eachResource.getSum_fte().doubleValue()/workingDays;
            }

            utilization += resourceFteAvg;
        }
        utilization = (utilization * 100) / (double) resourceCount;


        Map<String, Long> utilizationPercent = new HashMap<>();

        utilizationPercent.put("value", Math.round(utilization));

//        System.out.println("check error");

        return utilizationPercent;
    }

    public List<GetResourcesByRoles> getResourcesByRoles(GetDateRange getDateRange, Integer departmentId, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        String resourcesByRoles = ResourceUtils.getFile("getResourcesByRolesList.txt");
        if(departmentId!=null && departmentId!=0){
            resourcesByRoles = ResourceUtils.getFile("getResourcesByRolesListByDepartmentId.txt");
        }

        Query query = entityManager.createNativeQuery(resourcesByRoles).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourcesByRoles.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        if(departmentId!=null && departmentId!=0){
            query.setParameter("departmentId", departmentId);
        }
        else{
            List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);
            query.setParameter("resourceIds", visibleResourceIds);
        }

        List<GetResourcesByRoles> getResourcesByRolesList = query.getResultList();

        if(getResourcesByRolesList==null || getResourcesByRolesList.isEmpty()){
            getResourcesByRolesList = null;
        }
        return getResourcesByRolesList;

        //return resourceMgmtRepo.getResourcesByRoles(getDateRange.getStartDate(), getDateRange.getEndDate());
    }

    public List<GetResourcesByLocation> getResourcesByLocation(GetDateRange getDateRange, Integer departmentId, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        String resourcesByLocation = ResourceUtils.getFile("getResourcesByLocationList.txt");
        if(departmentId!=null && departmentId!=0){
            resourcesByLocation = ResourceUtils.getFile("getResourcesByLocationListByDepartmentId.txt");
        }

        Query query = entityManager.createNativeQuery(resourcesByLocation).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourcesByLocation.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        if(departmentId!=null && departmentId!=0){
            query.setParameter("departmentId", departmentId);
        }
        else{
            List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);
            query.setParameter("resourceIds", visibleResourceIds);
        }

        List<GetResourcesByLocation> getResourcesByLocationList = query.getResultList();

        if(getResourcesByLocationList==null || getResourcesByLocationList.isEmpty()){
            getResourcesByLocationList = null;
        }
        return getResourcesByLocationList;

        //return resourceMgmtRepo.getResourcesByLocation(getDateRange.getStartDate(), getDateRange.getEndDate());
    }

    public Object notificationViewedByUser(String email, Boolean isRmTrue, Integer requestId){

        List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.getResourceByEmail(email);

        if(resourceMgmtList!=null && !resourceMgmtList.isEmpty()){
            ResourceMgmt currentUser = resourceMgmtList.get(0);

            Optional<RequestedResources> requestedResourcesOptional = requestedResourcesRepo.findById(requestId);

            if(requestedResourcesOptional.isPresent()){
                RequestedResources currentRequest = requestedResourcesOptional.get();
                if(isRmTrue){
                    List<String> viewedRMIds = new ArrayList<>();
                    viewedRMIds.add(String.valueOf(currentUser.getResourceId()));
                    if(currentRequest.getViewedByRMIds()!=null && !currentRequest.getViewedByRMIds().isEmpty()){
                        viewedRMIds.addAll(List.of(currentRequest.getViewedByRMIds().split(",")));
                    }
                    currentRequest.setViewedByRMIds(String.join(",", viewedRMIds));
                }
                else{
                    List<String> viewedPMIds = new ArrayList<>();
                    viewedPMIds.add(String.valueOf(currentUser.getResourceId()));
                    if(currentRequest.getViewedByPMIds()!=null && !currentRequest.getViewedByPMIds().isEmpty()){
                        viewedPMIds.addAll(List.of(currentRequest.getViewedByPMIds().split(",")));
                    }
                    currentRequest.setViewedByPMIds(String.join(",", viewedPMIds));
                }
            }

        }

        return true;
    }

//    TODO:: need to update
    public Object getNotificationCount(Boolean isRMNotification, String email, String role){

        if(isRMNotification==null){
            return null;
        }

        int notificationCount = 0;
        if(isRMNotification){
            notificationCount = getResourceNotificationNumber(email, role);
        }
        else{
            notificationCount = getProjectNotificationNumber(email);
        }

        return notificationCount;
    }

    private Integer getResourceNotificationNumber(String email, String role){

        Integer notificationCount = 0;

        Set<Integer> visibleResourceIds = new HashSet<>();

        visibleResourceIds.addAll(getVisibleResourceIdsForRMNotifications(email, true, role));
        visibleResourceIds.addAll(getVisibleResourceIdsForRMNotifications(email, false, role));

        List<RequestedResources> getAllRequestedResources = requestedResourcesRepo.getAllResourceNotifications(new ArrayList<>(visibleResourceIds));

        for(RequestedResources eachRequest: getAllRequestedResources){
            if(eachRequest.getRequestStatus().equals(ResourceUtils.REQUEST_RESOURCE_STATUS_OPEN)
                    || eachRequest.getRequestStatus().equals(ResourceUtils.REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED)
                    || eachRequest.getRequestStatus().equals(ResourceUtils.REQUEST_RESOURCE_STATUS_RELEASED)){
                if(eachRequest.getRequestStatus().equals(ResourceUtils.REQUEST_RESOURCE_STATUS_RELEASED)) {
                    LocalDate modifiedDate = new java.sql.Date(eachRequest.getModifiedDate().getTime()).toLocalDate();
                    LocalDate currentDate = LocalDate.now();
                    if ((ChronoUnit.DAYS.between(modifiedDate, currentDate) + 1) < 8) {
                        if (eachRequest.getNotify() != null && eachRequest.getNotify().equals("1")) {
                            notificationCount++;
                        }
                    }
                }
                else{
                    notificationCount++;
                }
            }
        }

        return notificationCount;
    }

    private Integer getProjectNotificationNumber(String email){

        Integer notificationCount = 0;

        Set<Integer> getAllVisibleProjectIds = new HashSet<>();

        getAllVisibleProjectIds.addAll(getVisibleProjectIds(email, true));
        getAllVisibleProjectIds.addAll(getVisibleProjectIds(email, false));

        List<RequestedResources> getAllRequestedResources = requestedResourcesRepo.getAllProjectNotifications(new ArrayList<>(getAllVisibleProjectIds));

        for(RequestedResources eachRequest: getAllRequestedResources){
            LocalDate modifiedDate = new java.sql.Date(eachRequest.getModifiedDate().getTime()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            if ((ChronoUnit.DAYS.between(modifiedDate, currentDate) + 1) < 8) {
                if (eachRequest.getNotify() != null && eachRequest.getNotifyPm().equals("1")) {
                    notificationCount++;
                }
            }
        }

        return notificationCount;
    }

    private List<Integer> getVisibleResourceIdsForRMNotifications(String email, Boolean isTeam, String role){

        Set<Integer> listOfVisibleResourceIds = null;

        if(role.equals(ResourceUtils.USER_ROLE_MANAGER)) {
//        get list of all managed department by this resource, if it exists
            List<ResourceMgmt> resourceMgmt = resourceMgmtRepo.getResourceByEmail(email);

//        if resource exists
            if (resourceMgmt != null && !resourceMgmt.isEmpty()) {
                ResourceMgmt currentUser = resourceMgmt.get(0);

                Set<Integer> listOfManagedResourceIds = new HashSet<>();
                listOfManagedResourceIds.add(currentUser.getResourceId());

//            collect list of delegated resources from the resource

                if (!isTeam) {
                    List<DelegatedResourceHistory> delegatedResourceHistoryList = delegatedResourceHistoryRepo.findAll();
                    for (DelegatedResourceHistory eachRecord : delegatedResourceHistoryList) {
                        LocalDate localDate = LocalDate.now();
                        Date currentDate = java.sql.Date.valueOf(localDate);
                        if (eachRecord.getIsActive().equals("1") && eachRecord.getDelegatedToResourceId().equals(currentUser.getResourceId())
                                && eachRecord.getDelegationStartDate().compareTo(currentDate) <= 0
                                && eachRecord.getDelegationEndDate().compareTo(currentDate) >= 0) {
                            listOfManagedResourceIds.add(eachRecord.getDelegatedResourceId());
                        }
                    }
                }

//            get resource hierarchy
                String getResourceHierarchyQuery = ResourceUtils.getFile("getResourceHierarchyQuery.txt");
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
                            if (!listOfVisibleResourceIds.contains(topNode.getResource_id())) {
                                listOfVisibleResourceIds.add(topNode.getResource_id());
                                Integer depthLevel = topNode.getDepth_order();
                                i++;
                                while (i < getResourceHierarchy.size() && getResourceHierarchy.get(i).getDepth_order() > depthLevel) {
                                    if (listOfVisibleResourceIds.contains(getResourceHierarchy.get(i).getResource_id())) {
                                        break;
                                    } else {
                                        if (isTeam) {
                                            if (getResourceHierarchy.get(i).getDepth_order() > depthLevel + 1) {
                                                listOfVisibleResourceIds.add(getResourceHierarchy.get(i).getResource_id());
                                            }
                                        } else {
                                            if (getResourceHierarchy.get(i).getDepth_order().equals(depthLevel + 1)) {
                                                listOfVisibleResourceIds.add(getResourceHierarchy.get(i).getResource_id());
                                            }
                                        }
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                }

//            in case user cannot see his own record, please uncomment this
                List<ResourceMgmtFieldValue> resourceMgmtFieldValueList = resourceMgmtRepo.getResourceReportingManagerById(currentUser.getResourceId());
                if (isTeam || (resourceMgmtFieldValueList != null && !resourceMgmtFieldValueList.isEmpty()
                        && resourceMgmtFieldValueList.get(0).getFieldValue() != null && !resourceMgmtFieldValueList.get(0).getFieldValue().isEmpty()
                        && !resourceMgmtFieldValueList.get(0).getFieldValue().equals("0"))) {
                    listOfVisibleResourceIds.remove(currentUser.getResourceId());
                }
                listOfManagedResourceIds.remove(currentUser.getResourceId());
                listOfVisibleResourceIds.removeAll(listOfManagedResourceIds);
            }

            if (listOfVisibleResourceIds != null && !listOfVisibleResourceIds.isEmpty()) {
                return new ArrayList<>(listOfVisibleResourceIds);
            } else {
                return new ArrayList<>();
            }
        }
        else if(role.equals(ResourceUtils.USER_ROLE_RESOURCE_MANAGER)){
            List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.findAll();
            listOfVisibleResourceIds = resourceMgmtList.stream().map(ResourceMgmt::getResourceId).collect(Collectors.toSet());
            if (!listOfVisibleResourceIds.isEmpty()) {
                return new ArrayList<>(listOfVisibleResourceIds);
            } else {
                return new ArrayList<>();
            }
        }
        else{
            return new ArrayList<>();
        }

    }

    public GetRequestedResourcesDataMaster fetchResourceRequestDataByDepartmentIdForRM(String email, Boolean isTeam, String role) {

       GetRequestedResourcesDataMaster getRequestedResourcesDataMaster = null;

        Integer notifyCount = 0;

        List<Integer> visibleResourceIds = getVisibleResourceIdsForRMNotifications(email, isTeam, role);

//        get list of visible ids to this resource
        Set<Integer> getVisibleResourceIdsSet = new HashSet<>(visibleResourceIds);

//        get list of roles to determine which department they belong to
        List<ResourceMgmtRoles> resourceMgmtRolesList = resourceMgmtRolesRepo.findAll();
        Map<Integer, Integer> roleDepartmentMap = resourceMgmtRolesList.stream().collect(Collectors.toMap(ResourceMgmtRoles::getRoleId, ResourceMgmtRoles::getDepartmentId));

//        get current user details
        List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.getResourceByEmail(email);
        if(resourceMgmtList==null || resourceMgmtList.isEmpty()){
            return null;
        }
        ResourceMgmt currentUser = resourceMgmtList.get(0);

//        get current user department
        DepartmentStringIdName currentUserDepartment = resourceMgmtRepo.getResourceDepartmentId(currentUser.getResourceId());
        Integer currentUserDepartmentId = Integer.valueOf(currentUserDepartment.getDepartmentId());

//        get department hierarchy list
        String getDepartmentHierarchyQuery = ResourceUtils.getFile("getDepartmentHierarchyQuery.txt");
        Query query = entityManager.createNativeQuery(getDepartmentHierarchyQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(DepartmentHierarchy.class));
        List<DepartmentHierarchy> getDepartmentHierarchy = query.getResultList();

        List<Integer> currentUserChildDepartments = new ArrayList<>();

        if(getDepartmentHierarchy!=null && !getDepartmentHierarchy.isEmpty()){
            for (int i = 0; i < getDepartmentHierarchy.size(); i++) {
                if (getDepartmentHierarchy.get(i).getDepartment_id().equals(currentUserDepartmentId)) {
                    DepartmentHierarchy topNode = getDepartmentHierarchy.get(i);
                    if(!currentUserChildDepartments.contains(topNode.getDepartment_id())) {
                        Integer depthLevel = topNode.getDepth_order();
                        i++;
                        while (i < getDepartmentHierarchy.size() && getDepartmentHierarchy.get(i).getDepth_order() > depthLevel) {
                            if(currentUserChildDepartments.contains(getDepartmentHierarchy.get(i).getDepartment_id())){
                                break;
                            }
                            else {
                                currentUserChildDepartments.add(getDepartmentHierarchy.get(i).getDepartment_id());
                                i++;
                            }
                        }
                    }
                }
            }
        }


//        get list of visible department IDs
        List<DepartmentIdName> visibleDepartmentList = departmentDropdownOptions(email, role);
        Set<Integer> visibleDepartmentIdsSet = visibleDepartmentList.stream().map(DepartmentIdName::getDepartmentId).collect(Collectors.toSet());

//        get all named and unnamed resource requests
        String requestedResourcesDataSet = ResourceUtils.getFile("getNamedAndUnnamedRequestedResourcesForRM.txt");
        Query query1 = entityManager.createNativeQuery(requestedResourcesDataSet).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(GetRequestedResourcesDataForString.class));

        List<GetRequestedResourcesDataForString> getRequestedResourcesDataString = query1.getResultList();

        if(getRequestedResourcesDataString!=null && !getRequestedResourcesDataString.isEmpty()) {

            List<GetRequestedResourcesData> getRequestedResourcesData = new ArrayList<>();
            for(GetRequestedResourcesDataForString eachRequest: getRequestedResourcesDataString){
                getRequestedResourcesData.add(new GetRequestedResourcesData(eachRequest));
            }

            List<GetRequestedResourcesData> getRequestedResourcesDataVisible = new ArrayList<>();

            for(GetRequestedResourcesData eachRequest: getRequestedResourcesData){

                Boolean isNotificationVisible = false;

                if(eachRequest.getResource_id()!=null){
                    if(getVisibleResourceIdsSet.contains(eachRequest.getResource_id())){
                        isNotificationVisible = true;
                    }
                }
                else{
                    String notificationRole = eachRequest.getRole_id();
                    if(notificationRole!=null && !notificationRole.isEmpty()) {
                        Integer departmentId = roleDepartmentMap.get(Integer.valueOf(notificationRole));
//                        if request belongs to sub department
                        if(currentUserChildDepartments.contains(departmentId)){
//                            but it will only be visible if viewing my Team view
                            if(isTeam){
                                isNotificationVisible = true;
                            }
                        }
//                        if it is not sub-department, but still managed by user
                        else if(visibleDepartmentIdsSet.contains(departmentId)){
//                            it will only be visible if viewing self notifications
                            if(!isTeam){
                                isNotificationVisible = true;
                            }
                        }
                    }
                }

                if(isNotificationVisible) {

//                eachRequest.setCreated_date(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getCreated_date().getTime()).toLocalDate()));
//                eachRequest.setModified_date(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getModified_date().getTime()).toLocalDate()));

                    LocalDate startDate = new java.sql.Date(eachRequest.getAllocation_start_date().getTime()).toLocalDate();
                    LocalDate endDate = new java.sql.Date(eachRequest.getAllocation_end_date().getTime()).toLocalDate();

                    Integer workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);
                    Double avgAllocated = ResourceUtils.roundToTwoDecimalPlace(eachRequest.getAvg_allocated_fte().doubleValue() / workingDays);
                    Double avgRequested = ResourceUtils.roundToTwoDecimalPlace(eachRequest.getAvg_requested_fte().doubleValue() / workingDays);

                    eachRequest.setAvg_requested_fte(BigDecimal.valueOf(avgRequested));
                    eachRequest.setAvg_allocated_fte(BigDecimal.valueOf(avgAllocated));

                    eachRequest.setCreated_date(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getCreated_date().getTime()).toLocalDate()));
                    eachRequest.setModified_date(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getModified_date().getTime()).toLocalDate()));

                    Boolean isBoldTrue = false;
                    Set<String> viewedResourceIds = new HashSet<>(List.of(eachRequest.getViewed_by_ids().split(",")));
                    if(!viewedResourceIds.contains(String.valueOf(currentUser.getResourceId()))){
                        isBoldTrue = true;
                    }

                    if(eachRequest.getRequest_status().equals(ResourceUtils.REQUEST_RESOURCE_STATUS_COMPLETELY_FULFILLED)
                            || eachRequest.getRequest_status().equals(ResourceUtils.REQUEST_RESOURCE_STATUS_OVER_FULFILLED)
                            || eachRequest.getRequest_status().equals(ResourceUtils.REQUEST_RESOURCE_STATUS_RELEASED)){
                        LocalDate modifiedDate = new java.sql.Date(eachRequest.getModified_date().getTime()).toLocalDate();
                        LocalDate currentDate = LocalDate.now();
                        if((ChronoUnit.DAYS.between(modifiedDate, currentDate)+1)<8){
                            getRequestedResourcesDataVisible.add(eachRequest);
                        }
                    }
                    else{
                        getRequestedResourcesDataVisible.add(eachRequest);
                        if(isBoldTrue){
                            notifyCount++;
                            eachRequest.setShow_bold(true);
                        }
                    }
                }
            }

            if(!getRequestedResourcesDataVisible.isEmpty()) {

                getRequestedResourcesDataVisible.sort(Comparator.comparing(GetRequestedResourcesData::getRequest_id).reversed());

                getRequestedResourcesDataMaster = new GetRequestedResourcesDataMaster(notifyCount, getRequestedResourcesDataVisible);
            }
        }

        return getRequestedResourcesDataMaster;
    }

    private List<Integer> getVisibleProjectIds(String email, Boolean isTeam){

        Set<Integer> listOfVisibleResourceIds = null;

    //        get list of all managed department by this resource, if it exists
        List<ResourceMgmt> resourceMgmt = resourceMgmtRepo.getResourceByEmail(email);

        Set<Integer> visibleProjectIds = new HashSet<>();

    //        if resource exists
        if(resourceMgmt!=null && !resourceMgmt.isEmpty()){
            ResourceMgmt currentUser = resourceMgmt.get(0);

            Set<Integer> listOfManagedResourceIds = new HashSet<>();
            listOfManagedResourceIds.add(currentUser.getResourceId());

    //            get resource hierarchy
            String getResourceHierarchyQuery = ResourceUtils.getFile("getResourceHierarchyQuery.txt");
            Query query1 = entityManager.createNativeQuery(getResourceHierarchyQuery).unwrap(org.hibernate.query.Query.class);
            ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(AncestralPathOfResource.class));
            List<AncestralPathOfResource> getResourceHierarchy = query1.getResultList();

    //            add managed department list to visible list
            listOfVisibleResourceIds = new HashSet<>();

    //            for each managed department, find all the sub departments and add to visible list
            if(isTeam) {
                for (Integer eachResourceId : listOfManagedResourceIds) {
                    //                get the list of all sub departments from the department hierarchy
                    for (int i = 0; i < getResourceHierarchy.size(); i++) {
                        if (getResourceHierarchy.get(i).getResource_id().equals(eachResourceId)) {
                            AncestralPathOfResource topNode = getResourceHierarchy.get(i);
                            //                        check if current resource is already part of visibleResourceIds as those resource hierarchy have already been added
                            if (!listOfVisibleResourceIds.contains(topNode.getResource_id())) {
                                listOfVisibleResourceIds.add(topNode.getResource_id());
                                Integer depthLevel = topNode.getDepth_order();
                                i++;
                                while (i < getResourceHierarchy.size() && getResourceHierarchy.get(i).getDepth_order() > depthLevel) {
                                    if (listOfVisibleResourceIds.contains(getResourceHierarchy.get(i).getResource_id())) {
                                        break;
                                    } else {
                                        listOfVisibleResourceIds.add(getResourceHierarchy.get(i).getResource_id());
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                }
//                in case user cannot see his own record, please uncomment this
                listOfVisibleResourceIds.remove(currentUser.getResourceId());
            }
            else{
                listOfVisibleResourceIds.add(currentUser.getResourceId());
            }
        }

        Set<String> visibleResourceNames = new HashSet<>();
        if(listOfVisibleResourceIds!=null && !listOfVisibleResourceIds.isEmpty()) {
            for (ResourceMgmt eachResource : resourceMgmt) {
                if (listOfVisibleResourceIds.contains(eachResource.getResourceId())) {
                    String resourceName = ResourceUtils.removeAllExtraSpace(eachResource.getFirstName() + " " + eachResource.getLastName());
                    visibleResourceNames.add(resourceName.toLowerCase());
                }
            }
        }

        if(listOfVisibleResourceIds!=null && !listOfVisibleResourceIds.isEmpty()){
            List<ProjectDetailsForNotification> projectDetails = projectMgmtRepo.getProjectDetailsForNotification();
            for(ProjectDetailsForNotification eachProject: projectDetails){
                if(listOfVisibleResourceIds.contains(Integer.valueOf(eachProject.getResourceId()))){
                    visibleProjectIds.add(eachProject.getProjectId());
                }
                else if (visibleResourceNames.contains(eachProject.getCreatedBy().toLowerCase())){
                    visibleProjectIds.add(eachProject.getProjectId());
                }
            }
        }

        return new ArrayList<>(visibleProjectIds);
    }

    public GetRequestedResourcesDataMaster fetchResourceRequestDataByDepartmentIdForPM(String email, Boolean isTeam, String username) {

        GetRequestedResourcesDataMaster getRequestedResourcesDataMaster = null;
        Integer notifyCount = 0;

        List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.getResourceByEmail(email);
        if(resourceMgmtList==null || resourceMgmtList.isEmpty()){
            return null;
        }
        ResourceMgmt currentUser = resourceMgmtList.get(0);

        String requestedResourcesDataSet = ResourceUtils.getFile("getNamedAndUnnamedRequestedResourcesForPM.txt");
        Query query = entityManager.createNativeQuery(requestedResourcesDataSet).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetRequestedResourcesDataForString.class));

        List<GetRequestedResourcesDataForString> getRequestedResourcesDataString = query.getResultList();

        Set<Integer> listOfVisibleProjectIds = new HashSet<>(getVisibleProjectIds(email, isTeam));

        if(getRequestedResourcesDataString!=null && !getRequestedResourcesDataString.isEmpty()) {

            List<GetRequestedResourcesData> getRequestedResourcesData = new ArrayList<>();
            for(GetRequestedResourcesDataForString eachRequest: getRequestedResourcesDataString){
                getRequestedResourcesData.add(new GetRequestedResourcesData(eachRequest));
            }

            List<GetRequestedResourcesData> getRequestedResourcesDataVisible = new ArrayList<>();

            for(GetRequestedResourcesData eachRequest: getRequestedResourcesData){

                boolean isNotificationVisible = false;

                if(listOfVisibleProjectIds.contains(eachRequest.getProject_id())){
                    isNotificationVisible = true;
                }

                if(isNotificationVisible) {
                    LocalDate startDate = new java.sql.Date(eachRequest.getAllocation_start_date().getTime()).toLocalDate();
                    LocalDate endDate = new java.sql.Date(eachRequest.getAllocation_end_date().getTime()).toLocalDate();

                    Integer workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);
                    Double avgAllocated = ResourceUtils.roundToTwoDecimalPlace(eachRequest.getAvg_allocated_fte().doubleValue() / workingDays);
                    Double avgRequested = ResourceUtils.roundToTwoDecimalPlace(eachRequest.getAvg_requested_fte().doubleValue() / workingDays);

                    eachRequest.setAvg_requested_fte(BigDecimal.valueOf(avgRequested));
                    eachRequest.setAvg_allocated_fte(BigDecimal.valueOf(avgAllocated));

                    eachRequest.setCreated_date(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getCreated_date().getTime()).toLocalDate()));
                    eachRequest.setModified_date(java.sql.Date.valueOf(new java.sql.Date(eachRequest.getModified_date().getTime()).toLocalDate()));

                    LocalDate modifiedDate = new java.sql.Date(eachRequest.getModified_date().getTime()).toLocalDate();
                    LocalDate currentDate = LocalDate.now();
                    if ((ChronoUnit.DAYS.between(modifiedDate, currentDate) + 1) < 8) {
                        Boolean isBoldTrue = false;
                        Set<String> viewedResourceIds = new HashSet<>(List.of(eachRequest.getViewed_by_ids().split(",")));
                        if(!viewedResourceIds.contains(String.valueOf(currentUser.getResourceId()))){
                            isBoldTrue = true;
                        }
                        if (isBoldTrue) {
                            notifyCount++;
                            eachRequest.setShow_bold(true);
                        }
                        getRequestedResourcesDataVisible.add(eachRequest);
                    }
                }
            }

            getRequestedResourcesDataVisible.sort(Comparator.comparing(GetRequestedResourcesData::getRequest_id).reversed());

            getRequestedResourcesDataMaster = new GetRequestedResourcesDataMaster(notifyCount, getRequestedResourcesDataVisible);
        }

        return getRequestedResourcesDataMaster;
    }


//    Excel Download and upload functions Starts
    public Object downloadCreateResourceExcelFile() throws IOException{

        List<ResourceMgmtFieldMap> resourceMgmtFieldMapList = resourceMgmtFieldMapRepo.findAll();
        resourceMgmtFieldMapList.sort(Comparator.comparing(ResourceMgmtFieldMap::getFieldId));

        byte[] excelFile = null;
        try {
            excelFile = excelProcessingService.generateCreateResourceExcelFile(resourceMgmtFieldMapList);
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ResourceUtils.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        return excelFile;
    }

    public Object createResourceDetailsFromExcel(@RequestPart MultipartFile excelFile, String modifiedBy) throws IOException{

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        String dateFormat = generalSettingsList.get(0).getDateFormat();
        dateFormat = ResourceUtils.VALID_DATE_FORMAT.get(dateFormat);

//        Get all the field data, as well as all picklist data
        List<ResourceMgmtFieldMap> resourceMgmtFieldMapList = resourceMgmtFieldMapRepo.findAll();
        Map<String, ResourceMgmtFieldMap> resourceMgmtFieldMapListToMap = new HashMap<>();
        for(ResourceMgmtFieldMap data: resourceMgmtFieldMapList){
            resourceMgmtFieldMapListToMap.put(data.getFields().toLowerCase(), data);
        }

        List<ResourceMgmtEmploymentType> resourceMgmtEmploymentTypeList = resourceMgmtEmploymentTypeRepo.findAll();
        Map<String, ResourceMgmtEmploymentType> resourceMgmtEmploymentTypeListToMap = new HashMap<>();
        for(ResourceMgmtEmploymentType data: resourceMgmtEmploymentTypeList){
            resourceMgmtEmploymentTypeListToMap.put(data.getEmploymentType().toLowerCase(), data);
        }

        List<ResourceMgmtSkills> resourceMgmtSkillsList = resourceMgmtSkillsRepo.findAll();
        Map<String, ResourceMgmtSkills> resourceMgmtSkillsListToMap = new HashMap<>();
        for(ResourceMgmtSkills data: resourceMgmtSkillsList){
            resourceMgmtSkillsListToMap.put(data.getSkill().toLowerCase(), data);
        }

        List<ResourceMgmtRoles> resourceMgmtRolesList = resourceMgmtRolesRepo.findAll();
        Map<String, ResourceMgmtRoles> resourceMgmtRolesListToMap = new HashMap<>();
        for(ResourceMgmtRoles data: resourceMgmtRolesList){
            resourceMgmtRolesListToMap.put(data.getRole().toLowerCase(), data);
        }

        List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
        Map<String, ResourceMgmtDepartment> resourceMgmtDepartmentListToMap = new HashMap<>();
        for(ResourceMgmtDepartment data: resourceMgmtDepartmentList){
            resourceMgmtDepartmentListToMap.put(data.getDepartment().toLowerCase(), data);
        }

//        List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.findAll();
//        Map<String, ResourceMgmt> resourceMgmtListToMap
//                = resourceMgmtList.stream().collect(Collectors.toMap(ResourceMgmt::getFirstName, data->data));

        List<GeneralSettingsLocation> generalSettingsLocationList = generalSettingsLocationRepo.findAll();
        Map<String, GeneralSettingsLocation> generalSettingsLocationListToMap = new HashMap<>();
        for(GeneralSettingsLocation data: generalSettingsLocationList){
            generalSettingsLocationListToMap.put(data.getLocation().toLowerCase(), data);
        }

        List<ResourceCustomPicklist> resourceCustomPicklistList = resourceCustomPicklistRepo.findAll();
        Map<Integer, List<ResourceCustomPicklist>> resourceCustomPicklistListGroupByPicklistId
                = resourceCustomPicklistList.stream().collect(Collectors.groupingBy(ResourceCustomPicklist::getPicklistId));

        Map<Integer, Map<String, ResourceCustomPicklist>> resourceCustomPicklistListToMap = new HashMap<>();
        for(Map.Entry<Integer, List<ResourceCustomPicklist>> entry: resourceCustomPicklistListGroupByPicklistId.entrySet()){

            List<ResourceCustomPicklist> thisPicklistValues = entry.getValue();

            Map<String, ResourceCustomPicklist> groupByPicklistIdToMap = new HashMap<>();
            for(ResourceCustomPicklist data: thisPicklistValues){
                groupByPicklistIdToMap.put(data.getPicklistValue().toLowerCase(), data);
            }
            resourceCustomPicklistListToMap.put(entry.getKey(), groupByPicklistIdToMap);
        }

        List<ResourceIdEmailDetail> resourceIdEmailDetailList = resourceMgmtRepo.getResourceIdEmailList();
        Map<String, Integer> resourceIdEmailDetailListToMap
                = resourceIdEmailDetailList.stream().collect(Collectors.toMap(ResourceIdEmailDetail::getResourceEmail, ResourceIdEmailDetail::getResourceId));

//        get all the resource data converted to create resource format
        Map<String, Object> returnData = excelProcessingService.excelToResourceDetails(excelFile, resourceMgmtFieldMapListToMap, resourceMgmtEmploymentTypeListToMap,
                resourceMgmtSkillsListToMap, resourceMgmtRolesListToMap, resourceMgmtDepartmentListToMap, generalSettingsLocationListToMap, resourceCustomPicklistListToMap,
                resourceIdEmailDetailListToMap, dateFormat);

        if(!(boolean)returnData.get("result")){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ResourceUtils.INCORRECT_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        List<String> result = new ArrayList<>();
        int numberOfFields = (int)returnData.get("numberOfFields");
        List<PostResourceMgmt> listOfResources = (List<PostResourceMgmt>)returnData.get("resourceList");

        if(listOfResources!=null && !listOfResources.isEmpty()) {
            for (PostResourceMgmt newResource : listOfResources) {
                result.add(createResourceDetailsFromExcelExtended(newResource, modifiedBy, resourceMgmtFieldMapList));
            }
        }

        return excelProcessingService.generateUploadedCreateResourceExcelFileResponse(excelFile, result, numberOfFields);
    }

    public String createResourceDetailsFromExcelExtended(PostResourceMgmt postResourceMgmt, String modifiedBy, List<ResourceMgmtFieldMap> resourceMgmtFieldMaps) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating Resource..");
        }

        List<CreateResourceData> allResourceDataList = postResourceMgmt.getResourceData();

//        will check if all the compulsory fields are present,
//        as well as if the data inside all the coming fields is correct or not

        List<CreateResourceData> allValidResourceDataList = new ArrayList<>();
        Map<Integer, CreateResourceData> allResourceDataListToMap
                = allResourceDataList.stream().collect(Collectors.toMap(CreateResourceData::getFieldId, fieldData -> fieldData));

        for(ResourceMgmtFieldMap eachField: resourceMgmtFieldMaps){

//            compulsory data and field type validation
            if(eachField.getEnabled().equals("1") && eachField.getResourceCreationView().equals("1")){

                boolean isFieldCompulsory = false, isDataAddedToList = false;

                if(eachField.getIsEnabledFreeze().equals("1") && eachField.getIsEditAccessDdl().equals("1")){
                    isFieldCompulsory = true;
                }

                if(allResourceDataListToMap.containsKey(eachField.getFieldId())){

                    CreateResourceData currentFieldData = allResourceDataListToMap.get(eachField.getFieldId());

                    boolean isDataCorrect = true;
                    try {
                        if (currentFieldData.getValue()!=null && !currentFieldData.getValue().isEmpty()){
                            switch (eachField.getResourceType()) {
                                case ResourceUtils.FIELD_TYPE_PICKLIST:
                                    Set<Integer> allIds = new HashSet<>();
                                    String[] ids = currentFieldData.getValue().split(",");
                                    for (String eachId : ids) {
                                        allIds.add(Integer.valueOf(eachId));
                                    }
                                    if(allIds.size()>1){
                                        return ResourceUtils.PICKLIST_MORE_THAN_ONE_VALUE;
                                    }
                                    currentFieldData.setValue(String.valueOf(currentFieldData.getValue()));
                                    break;

                                case ResourceUtils.FIELD_TYPE_PICKLIST_MULTISELECT:
                                    currentFieldData.setValue(String.valueOf(currentFieldData.getValue()));
                                    break;

                                case ResourceUtils.FIELD_TYPE_DATE:
                                    LocalDate localDate = LocalDate.parse(currentFieldData.getValue());
                                    Date date = java.sql.Date.valueOf(localDate);
                                    currentFieldData.setValue(String.valueOf(date));
                                    break;

                                case ResourceUtils.FIELD_TYPE_NUMBER:
//                                    TODO:: can only proceed if we know what type of input to expect
                                    break;

                                case ResourceUtils.FIELD_TYPE_CURRENCY:
//                                    TODO:: ask for currency checks, like 2 decimal, negative currency, maximum limit, etc.
                                    break;

                                case ResourceUtils.FIELD_TYPE_EMAIL:
                                    String email = currentFieldData.getValue();
                                    String basicEmailFormat = "^([A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

                                    Pattern emailPattern = Pattern.compile(basicEmailFormat);
                                    boolean isEmailPatternCorrect =emailPattern.matcher(email).matches();
                                    if(!isEmailPatternCorrect){
                                        if(eachField.getSettingType().equals(ResourceUtils.FIELD_TYPE)){
                                            return ResourceUtils.ESSENTIAL_FIELDS_MISSING + ": " + ResourceUtils.INCORRECT_EMAIL_FORMAT;
                                        }
                                        LOGGER.warn(ResourceUtils.INCORRECT_EMAIL_FORMAT);
                                        isDataCorrect = false;
                                    }
                                    break;

                                case ResourceUtils.FIELD_TYPE_TEXT:
                                    if(currentFieldData.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION)){
                                        if(!currentFieldData.getValue().equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION_ONSITE)
                                                && !currentFieldData.getValue().equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION_OFFSHORE) ){
                                            isDataCorrect = false;
                                        }
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
                        allValidResourceDataList.add(currentFieldData);
                        isDataAddedToList = true;
                    }
                }
                if(isFieldCompulsory && !isDataAddedToList){
                    return ResourceUtils.ESSENTIAL_FIELDS_MISSING + ": " + eachField.getFields();
                }
            }
        }

        if(allValidResourceDataList.isEmpty()) {
            return ResourceUtils.ESSENTIAL_FIELDS_MISSING;
        }

        List<CreateResourceData> customResourceDataList = new ArrayList<>();

        ResourceMgmt resourceManagement = new ResourceMgmt();

        Date dateOfJoin = null, lastWorkingDate = null;

        for (CreateResourceData eachData : allValidResourceDataList) {
            switch (eachData.getSettingType()) {
                case ResourceUtils.HIDE_PICKLIST_TYPE:
                    /*
                    case 2 and 4 refer to same field type
                     */
                case ResourceUtils.FIELD_TYPE:
                    switch (eachData.getFieldName()) {
                        case ResourceUtils.FIELD_RESOURCE_FIRST_NAME:
                            resourceManagement.setFirstName(eachData.getValue());
                            break;

                        case ResourceUtils.FIELD_RESOURCE_LAST_NAME:
                            resourceManagement.setLastName(eachData.getValue());
                            break;

                        case ResourceUtils.FIELD_RESOURCE_RESOURCE_DATE_OF_JOIN:
                            LocalDate localDate = LocalDate.now();
                            Date startDate = java.sql.Date.valueOf(localDate);
                            try {
                                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(eachData.getValue());
                            } catch (java.text.ParseException e) {
                                LOGGER.warn("Parse Exception");
//                                e.printStackTrace();
                            }
                            resourceManagement.setDateOfJoin(startDate);
                            dateOfJoin = startDate;
                            break;

                        case ResourceUtils.FIELD_RESOURCE_RESOURCE_LAST_WORKING_DATE:
                            Date endDate = null;
                            try {
                                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(eachData.getValue());
                            } catch (java.text.ParseException e) {
                                LOGGER.warn("Parse Exception");
//                                e.printStackTrace();
                            }
                            resourceManagement.setLastWorkingDate(endDate);
                            lastWorkingDate = endDate;
                            break;

                        default:
                            if(eachData.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_EMAIL)){
                                Boolean checkIfExists = resourceMgmtFieldValueRepo.checkIfEmailIdExists(0, eachData.getFieldId(), String.valueOf(eachData.getValue()));
                                if(checkIfExists){
                                    return ResourceUtils.EMAIL_ALREADY_EXISTS;
                                }
                            }
                            customResourceDataList.add(eachData);
                    }
                    break;

                case ResourceUtils.CUSTOM_TYPE:
                    customResourceDataList.add(eachData);
                    break;

                default:
                    LOGGER.warn("Unrecognised setting_type: {}", eachData.getSettingType());
            }
        }

        if(lastWorkingDate!=null && dateOfJoin!=null){
            if(dateOfJoin.compareTo(lastWorkingDate)>0){
                return ResourceUtils.INCORRECT_RESOURCE_DATE_RANGE;
            }
        }

        resourceManagement.setCreatedBy(modifiedBy);
        resourceManagement.setCreatedDate(new Date());
        resourceManagement.setModifiedBy(modifiedBy);
        resourceManagement.setModifiedDate(new Date());

        ResourceMgmt resourceManagementWithId = resourceMgmtRepo.save(resourceManagement);

        List<ResourceMgmtFieldValue> createResourceCustomFields = new ArrayList<>();

        for (CreateResourceData eachCustomField : customResourceDataList) {
            ResourceMgmtFieldValue eachCustomData = new ResourceMgmtFieldValue(resourceManagementWithId, eachCustomField);
            eachCustomData.setCreatedBy(modifiedBy);
            eachCustomData.setCreatedDate(new Date());
            eachCustomData.setModifiedBy(modifiedBy);
            eachCustomData.setModifiedDate(new Date());
            createResourceCustomFields.add(eachCustomData);
        }

        resourceMgmtFieldValueRepo.saveAll(createResourceCustomFields);
        return ResourceUtils.RESOURCE_CREATED_SUCCESSFULLY;
    }

//    Excel Download and upload functions Ends

    private Object checkForValidPicklistIds (ResourceMgmtFieldMap fieldSetting, List<Integer> listOfIds, Map<Integer, ResourceCustomPicklist> resourceCustomPicklistToMap){

        if(fieldSetting==null){
            String message = ResourceUtils.FIELD_ID_MISMATCH;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

//        check if picklist type has more than one values
        if(fieldSetting.getResourceType().equals(ResourceUtils.FIELD_TYPE_PICKLIST)){
            if(listOfIds.size()>1){
                String message = ResourceUtils.PICKLIST_MORE_THAN_ONE_VALUE;
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
                ResourceCustomPicklist customPicklist = resourceCustomPicklistToMap.get(id);
                if(customPicklist!=null && customPicklist.getPicklistId().equals(fieldSetting.getFieldId())){
                    correctIdsList.add(id);
                }
            }
        }
        else{
            switch(fieldSetting.getFields()){
                case ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER:
                    Optional<ResourceMgmt> data = resourceMgmtRepo.findById(listOfIds.get(0));
                    data.ifPresent(resourceMgmt -> correctIdsList.add(resourceMgmt.getResourceId()));
                    break;

                case ResourceUtils.FIELD_RESOURCE_ROLE:
                    List<ResourceMgmtRoles> data2 = resourceMgmtRolesRepo.findAllById(listOfIds);
                    for(ResourceMgmtRoles eachData: data2){
                        correctIdsList.add(eachData.getRoleId());
                    }
                    break;

                case ResourceUtils.FIELD_RESOURCE_SKILL:
                    List<ResourceMgmtSkills> data3 = resourceMgmtSkillsRepo.findAllById(listOfIds);
                    for(ResourceMgmtSkills eachData: data3){
                        correctIdsList.add(eachData.getSkillId());
                    }
                    break;

                case ResourceUtils.FIELD_RESOURCE_DEPARTMENT:
                    Optional<ResourceMgmtDepartment> data4 = resourceMgmtDepartmentRepo.findById(listOfIds.get(0));
                    data4.ifPresent(resourceMgmtDepartment -> correctIdsList.add(resourceMgmtDepartment.getDepartmentId()));
                    break;

                case ResourceUtils.FIELD_RESOURCE_LOCATION:
                    Optional<GeneralSettingsLocation> data5 = generalSettingsLocationRepo.findById(listOfIds.get(0));
                    data5.ifPresent(generalSettingsLocation -> correctIdsList.add(generalSettingsLocation.getLocationId()));
                    break;

                case ResourceUtils.FIELD_RESOURCE_EMPLOYMENT_TYPE:
                    Optional<ResourceMgmtEmploymentType> data6 = resourceMgmtEmploymentTypeRepo.findById(listOfIds.get(0));
                    data6.ifPresent(resourceMgmtEmploymentType -> correctIdsList.add(resourceMgmtEmploymentType.getEmpTypeId()));
                    break;
            }
        }

        if(correctIdsList.isEmpty()){
            return "";
        }

        return correctIdsList.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public Object createResource(PostResourceMgmt postResourceMgmt, String modifiedBy) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating Resource..");
        }

        List<CreateResourceData> allResourceDataList = postResourceMgmt.getResourceData();
//        project custom picklist options
        List<ResourceCustomPicklist> resourceCustomPicklist =resourceCustomPicklistRepo.findAll();
        Map<Integer, ResourceCustomPicklist> resourceCustomPicklistToMap
                = resourceCustomPicklist.stream().collect(Collectors.toMap(ResourceCustomPicklist::getId, rcp -> rcp));

//        will check if all the compulsory fields are present,
//        as well as if the data inside all the coming fields is correct or not

        List<CreateResourceData> allValidResourceDataList = new ArrayList<>();
        List<ResourceMgmtFieldMap> resourceMgmtFieldMaps = resourceMgmtFieldMapRepo.findAll();
        Map<Integer, CreateResourceData> allResourceDataListToMap
                = allResourceDataList.stream().collect(Collectors.toMap(CreateResourceData::getFieldId, fieldData -> fieldData));

        for(ResourceMgmtFieldMap eachField: resourceMgmtFieldMaps){

//            compulsory data and field type validation
            if(eachField.getEnabled().equals("1") && eachField.getResourceCreationView().equals("1")){

                boolean isFieldCompulsory = false, isDataAddedToList = false;

                if(eachField.getIsEnabledFreeze().equals("1") && eachField.getIsEditAccessDdl().equals("1")){
                    isFieldCompulsory = true;
                }

                if(allResourceDataListToMap.containsKey(eachField.getFieldId())){

                    CreateResourceData currentFieldData = allResourceDataListToMap.get(eachField.getFieldId());

                    boolean isDataCorrect = true;
                    try {
                        if (currentFieldData.getValue()!=null && !currentFieldData.getValue().isEmpty()){
                            switch (eachField.getResourceType()) {
                                case ResourceUtils.FIELD_TYPE_PICKLIST:
                                case ResourceUtils.FIELD_TYPE_PICKLIST_MULTISELECT:
                                    Set<Integer> allIds = new HashSet<>();
                                    String[] ids = currentFieldData.getValue().split(",");
                                    for (String eachId : ids) {
                                        allIds.add(Integer.valueOf(eachId));
                                    }

                                    Object returnData = checkForValidPicklistIds(eachField, new ArrayList<>(allIds), resourceCustomPicklistToMap);

                                    if(returnData instanceof Error){
                                        return returnData;
                                    }

                                    currentFieldData.setValue(String.valueOf(returnData));

                                    break;
                                case ResourceUtils.FIELD_TYPE_DATE:
                                    LocalDate localDate = LocalDate.parse(currentFieldData.getValue());
                                    Date date = java.sql.Date.valueOf(localDate);
                                    currentFieldData.setValue(String.valueOf(date));

                                    break;
                                case ResourceUtils.FIELD_TYPE_NUMBER:
//                                    TODO:: can only proceed if we know what type of input to expect

                                    break;
                                case ResourceUtils.FIELD_TYPE_CURRENCY:
//                                    TODO:: ask for currency checks, like 2 decimal, negative currency, maximum limit, etc.

                                    break;
                                case ResourceUtils.FIELD_TYPE_EMAIL:
                                    String email = currentFieldData.getValue();
                                    String basicEmailFormat = "^([A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

                                    Pattern emailPattern = Pattern.compile(basicEmailFormat);
                                    boolean isEmailPatternCorrect =emailPattern.matcher(email).matches();
                                    if(!isEmailPatternCorrect){
                                        LOGGER.warn("Incorrect email format");
                                        isDataCorrect = false;
                                    }
                                    break;
                                case ResourceUtils.FIELD_TYPE_TEXT:

                                    if(currentFieldData.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION)){
                                        if(!currentFieldData.getValue().equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION_ONSITE)
                                                && !currentFieldData.getValue().equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION_OFFSHORE) ){
                                            isDataCorrect = false;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    catch(Exception e){
                        isDataCorrect = false;
                    }

                    if(isDataCorrect) {
                        allValidResourceDataList.add(currentFieldData);
                        isDataAddedToList = true;
                    }
                }
                if(isFieldCompulsory && !isDataAddedToList){
                    String message = ResourceUtils.ESSENTIAL_FIELDS_MISSING;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
            }
        }

        if(allValidResourceDataList.isEmpty()) {
            String message = ResourceUtils.ESSENTIAL_FIELDS_MISSING;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        List<CreateResourceData> customResourceDataList = new ArrayList<>();

        ResourceMgmt resourceManagement = new ResourceMgmt();

        Date dateOfJoin = null, lastWorkingDate = null;

        for (CreateResourceData eachData : allValidResourceDataList) {
            switch (eachData.getSettingType()) {
                case ResourceUtils.HIDE_PICKLIST_TYPE:
                    /*
                    case 2 and 4 refer to same field type
                     */
                case ResourceUtils.FIELD_TYPE:
                    switch (eachData.getFieldName()) {
                        case ResourceUtils.FIELD_RESOURCE_FIRST_NAME:
                            resourceManagement.setFirstName(eachData.getValue());
                            break;
                        case ResourceUtils.FIELD_RESOURCE_LAST_NAME:
                            resourceManagement.setLastName(eachData.getValue());
                            break;
//                        case "Email":
//                            resourceManagement.setEmail(eachData.getValue());
//                            break;
//                        case "Phone":
//                            resourceManagement.setPhone(eachData.getValue());
//                            break;
//                        case "Location":
//                            resourceManagement.setLocation(eachData.getValue());
//                            break;
                        case ResourceUtils.FIELD_RESOURCE_RESOURCE_DATE_OF_JOIN:
                            LocalDate localDate = LocalDate.now();
                            Date startDate = java.sql.Date.valueOf(localDate);
                            try {
                                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(eachData.getValue());
                            } catch (java.text.ParseException e) {
                                LOGGER.warn("Parse Exception");
//                                e.printStackTrace();
                            }
                            resourceManagement.setDateOfJoin(startDate);
                            dateOfJoin = startDate;
                            break;
                        case ResourceUtils.FIELD_RESOURCE_RESOURCE_LAST_WORKING_DATE:
                            Date endDate = null;
                            try {
                                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(eachData.getValue());
                            } catch (java.text.ParseException e) {
                                LOGGER.warn("Parse Exception");
//                                e.printStackTrace();
                            }
                            resourceManagement.setLastWorkingDate(endDate);
                            lastWorkingDate = endDate;
                            break;
//                        case "Department":
//                            resourceManagement.setDepartmentId(Integer.valueOf(eachData.getValue()));
//                            break;
//                        case "Reporting Manager":
//                            resourceManagement.setReportingMgr(Integer.valueOf(eachData.getValue()));
//                            break;
//                        case "Location Type":
//                            resourceManagement.setEmpLocation(eachData.getValue());
//                            break;
//                        case "Employment Type":
//                            resourceManagement.setEmpTypeId(Integer.valueOf(eachData.getValue()));
//                            break;
//                        case "Roles":
//                            resourceManagement.setRole(eachData.getValue());
//                            break;
//                        case "Skills":
//                            resourceManagement.setSkills(eachData.getValue());
//                            break;
//                        case "Cost per Hour":
//                            resourceManagement.setCostPerHour(Integer.valueOf(eachData.getValue()));
//                            break;
//                        case "FTE Rate":
//                            resourceManagement.setFteRate(new BigInteger(eachData.getValue()));
//                            break;
//                        case "Variable":
//                            resourceManagement.setVariable(new BigInteger(eachData.getValue()));
//                            break;
//                        case "Bonus":
//                            resourceManagement.setBonus(new BigInteger(eachData.getValue()));
//                            break;
                        default:
                            if(eachData.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_EMAIL)){
                                Boolean checkIfExists = resourceMgmtFieldValueRepo.checkIfEmailIdExists(0, eachData.getFieldId(), String.valueOf(eachData.getValue()));
                                if(checkIfExists){
                                    String message = ResourceUtils.EMAIL_ALREADY_EXISTS;
                                    Error error = new Error();
                                    error.setRequestAt(new Date());
                                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                                    error.setMessage(message);
                                    return error;
                                }
                            }
                            customResourceDataList.add(eachData);
                    }
                    break;

                case ResourceUtils.CUSTOM_TYPE:
                    customResourceDataList.add(eachData);
                    break;

                default:
                    LOGGER.warn("Unrecognised setting_type: {}", eachData.getSettingType());
            }
        }

        if(lastWorkingDate!=null && dateOfJoin!=null){
            if(dateOfJoin.compareTo(lastWorkingDate)>0){
                String message = ResourceUtils.INCORRECT_RESOURCE_DATE_RANGE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                error.setMessage(message);
                return error;
            }
        }

        resourceManagement.setCreatedBy(modifiedBy);
        resourceManagement.setCreatedDate(new Date());
        resourceManagement.setModifiedBy(modifiedBy);
        resourceManagement.setModifiedDate(new Date());

        ResourceMgmt resourceManagementWithId = resourceMgmtRepo.save(resourceManagement);

        List<ResourceMgmtFieldValue> createResourceCustomFields = new ArrayList<>();

        for (CreateResourceData eachCustomField : customResourceDataList) {
            ResourceMgmtFieldValue eachCustomData = new ResourceMgmtFieldValue(resourceManagementWithId, eachCustomField);
//            eachCustomData.setResourceId(resourceManagementWithId.getResourceId());
            eachCustomData.setCreatedBy(modifiedBy);
            eachCustomData.setCreatedDate(new Date());
            eachCustomData.setModifiedBy(modifiedBy);
            eachCustomData.setModifiedDate(new Date());
//            eachCustomData.setFieldId(eachCustomField.getFieldId());
//            eachCustomData.setFieldValue(eachCustomField.getValue());
            createResourceCustomFields.add(eachCustomData);
        }

        resourceMgmtFieldValueRepo.saveAll(createResourceCustomFields);
        return true;
    }

//    public List<GetResourceDetails> getResourceDetails(Integer resourceId){
////        get the resource details using resourceId
//        GetResourceDetailsById resourceDetail = resourceMgmtRepo.getResourceDetailsById(resourceId);
//        if (Objects.isNull(resourceDetail)) {
//            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
//        }
//
////        get the resource custom details using resourceId
//        List<ResourceMgmtFieldValue> resourceCustomDetails = resourceMgmtFieldValueRepo.getResourceCustomDetailsById(resourceId);
//
////        convert resource custom details to map of format <fieldId, custom details> for quick access
//        Map<Integer, ResourceMgmtFieldValue> resourceCustomDetailsMap = new HashMap<>();
//        for(ResourceMgmtFieldValue eachDetail : resourceCustomDetails){
//            resourceCustomDetailsMap.put(eachDetail.getFieldId(), eachDetail);
//        }
//
////        get all the settings which are enabled and resourceCardView is true,
////        means it is visible for resource card component
//        List<ResourceMgmtFieldMap> resourceCardSetting = resourceMgmtFieldMapRepo.getResourceCardSettings();
//
////        get all the custom picklist list, so that we can convert the picklist id
////        stored inside the resource custom details
//        List<ResourceCustomPicklist> resourceCustomPicklistList = resourceCustomPicklistRepo.findAll();
//
////        convert the custom picklist list to map of format <id, custom picklist list> for quick access
//        Map<Integer, ResourceCustomPicklist> resourceCustomPicklistMap = new HashMap<>();
//        for(ResourceCustomPicklist eachPicklist : resourceCustomPicklistList){
//            resourceCustomPicklistMap.put(eachPicklist.getId(), eachPicklist);
//        }
//
////        all visible settings and their values list
//        List<GetResourceDetails> getResourceDetails = new ArrayList<>();
//
////        traverse each visible setting
//        for(ResourceMgmtFieldMap eachSetting : resourceCardSetting){
//
////            set details of the field
//            GetResourceDetails fieldDetails = new GetResourceDetails();
//            fieldDetails.setFieldName(eachSetting.getFields());
//            fieldDetails.setFieldId(eachSetting.getFieldId());
//            fieldDetails.setFieldType(eachSetting.getResourceType());
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
//                        case "First Name":
//                            fieldDetails.setFieldValue(resourceDetail.getFirstName());
//                            break;
//                        case "Last Name":
//                            fieldDetails.setFieldValue(resourceDetail.getLastName());
//                            break;
//                        case "Email":
//                            fieldDetails.setFieldValue(resourceDetail.getEmail());
//                            break;
//                        case "Phone":
//                            fieldDetails.setFieldValue(resourceDetail.getPhone());
//                            break;
//                        case "Date Of Join":
//                            fieldDetails.setFieldValue(resourceDetail.getDateOfJoin());
//                            break;
//                        case "Location":
//                            fieldDetails.setFieldValue(resourceDetail.getLocation());
//                            break;
//                        case "Department":
//                            if(resourceDetail.getDepartmentId()!=null){
//                                fieldDetails.setFieldValue(resourceMgmtDepartmentRepo.getDepartmentById(resourceDetail.getDepartmentId()));
//                                fieldDetails.setPicklistId(String.valueOf(resourceDetail.getDepartmentId()));
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Role":
//                            if(resourceDetail.getRole()!=null && !resourceDetail.getRole().equals("")){
//                                String[] roleIdStringList = resourceDetail.getRole().split(",");
//                                List<Integer> roleIdIntegerList = new ArrayList<>();
//                                for(String id: roleIdStringList){
//                                    roleIdIntegerList.add(Integer.valueOf(id));
//                                }
//                                List<String> roleIdValueList = resourceMgmtRolesRepo.getRolesByIds(roleIdIntegerList);
//                                fieldDetails.setFieldValue(String.join(",", roleIdValueList));
//                                fieldDetails.setPicklistId(resourceDetail.getRole());
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Reporting Manager":
//                            if(resourceDetail.getReportingMgr()!=null){
//                                fieldDetails.setFieldValue(resourceMgmtRepo.getReportingManagerById(resourceDetail.getReportingMgr()));
//                                fieldDetails.setPicklistId(String.valueOf(resourceDetail.getReportingMgr()));
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Cost Per Hour":
//                            fieldDetails.setFieldValue(resourceDetail.getCostPerHour());
//                            break;
//                        case "FTE Rate":
//                            fieldDetails.setFieldValue(resourceDetail.getFteRate());
//                            break;
//                        case "Variable":
//                            fieldDetails.setFieldValue(resourceDetail.getVariable());
//                            break;
//                        case "Skills":
//                            if(resourceDetail.getSkills()!=null && !resourceDetail.getSkills().equals("")){
//                                String[] skillIdStringList = resourceDetail.getSkills().split(",");
//                                List<Integer> skillIdIntegerList = new ArrayList<>();
//                                for(String id: skillIdStringList){
//                                    skillIdIntegerList.add(Integer.valueOf(id));
//                                }
//                                List<String> skillIdValueList = resourceMgmtSkillsRepo.getSkillsByIds(skillIdIntegerList);
//                                fieldDetails.setFieldValue(String.join(",", skillIdValueList));
//                                fieldDetails.setPicklistId(resourceDetail.getSkills());
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Created By":
//                            fieldDetails.setFieldValue(resourceDetail.getCreatedBy());
//                            break;
//                        case "Created Date":
//                            fieldDetails.setFieldValue(resourceDetail.getCreatedDate());
//                            break;
//                        case "Modified By":
//                            fieldDetails.setFieldValue(resourceDetail.getModifiedBy());
//                            break;
//                        case "Modified Date":
//                            fieldDetails.setFieldValue(resourceDetail.getModifiedDate());
//                            break;
//                        case "Employment Type":
//                            if(resourceDetail.getEmpTypeId()!=null){
//                                fieldDetails.setFieldValue(resourceMgmtEmploymentTypeRepo.getEmploymentTypeById(resourceDetail.getEmpTypeId()));
//                                fieldDetails.setPicklistId(String.valueOf(resourceDetail.getEmpTypeId()));
//                            }
//                            else{
//                                fieldDetails.setFieldValue("");
//                                fieldDetails.setPicklistId("");
//                            }
//                            break;
//                        case "Employee Location":
//                            fieldDetails.setFieldValue(resourceDetail.getEmpLocation());
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
////                    obtain the custom resource data of the field based on field id
//                    ResourceMgmtFieldValue customFieldValue = resourceCustomDetailsMap.get(eachSetting.getFieldId());
//
////                    check for picklist
//                    if(eachSetting.getResourceType().contains("Picklist")){
//
//
////                        check if custom data for this field exists
//                        if(customFieldValue!=null){
////                            if the field value is not null
//
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
//                                    picklistIdValue.add(resourceCustomPicklistMap.get(Integer.valueOf(id)).getPicklistValue());
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
//                          1. this field was created after the resource creation, hence no record exists
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
//            getResourceDetails.add(fieldDetails);
//        }
//
//        return getResourceDetails;
//    }

    public GetResourceDetailsMaster getResourceDetails(GetDateRange getDateRange, Integer resourceId) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        //        handle the data stored in master table
        ResourceMgmt getResourceMasterData = resourceMgmtRepo.getResourceDetailsById(resourceId);

        if (Objects.isNull(getResourceMasterData)) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
        }

        String getUtilizationLevelQuery = ResourceUtils.getFile("getUtilizationLevelDataByResourceId.txt");

        Query query = entityManager.createNativeQuery(getUtilizationLevelQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourceFteDetails.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("resourceId", resourceId);

        List<GetResourceFteDetails> resourceFteDetails = query.getResultList();

        GetResourceFteDetails getResourceFteDetails = resourceFteDetails.get(0);

        Date startDate = getResourceFteDetails.getStart_date().compareTo(getDateRange.getStartDate())>0?getResourceFteDetails.getStart_date():getDateRange.getStartDate();
        Date endDate;
        if(getResourceFteDetails.getEnd_date()!=null) {
            endDate = getResourceFteDetails.getEnd_date().compareTo(getDateRange.getEndDate()) < 0 ? getResourceFteDetails.getEnd_date() : getDateRange.getEndDate();
        }
        else{
            endDate = getDateRange.getEndDate();
        }

        LocalDate localStartDate = new java.sql.Date(startDate.getTime()).toLocalDate();
        LocalDate localEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();

        Integer workingDays = ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);

        Double resourceAvgFte = ResourceUtils.roundToTwoDecimalPlace(resourceFteDetails.get(0).getSum_fte().doubleValue()/workingDays);

        //        all visible settings and their values list
        List<GetResourceDetails> getResourceDetails = new ArrayList<>();

//        List<ResourceMgmtFieldMap> resourceMgmtFieldMap = resourceMgmtFieldMapRepo.getAdminResourceManagementFields();
//
//        for (ResourceMgmtFieldMap eachSetting : resourceMgmtFieldMap) {
//            GetResourceDetails fieldDetails = new GetResourceDetails();
//            switch (eachSetting.getFields()) {
//                case "First Name":
//                    fieldDetails.setFieldId(eachSetting.getFieldId());
//                    fieldDetails.setFieldName(eachSetting.getFields());
//                    fieldDetails.setFieldValue(getResourceMasterData.getFirstName());
//                    fieldDetails.setFieldType(eachSetting.getResourceType());
//                    getResourceDetails.add(fieldDetails);
//                    break;
//                case "Last Name":
//                    fieldDetails.setFieldId(eachSetting.getFieldId());
//                    fieldDetails.setFieldName(eachSetting.getFields());
//                    fieldDetails.setFieldValue(getResourceMasterData.getLastName());
//                    fieldDetails.setFieldType(eachSetting.getResourceType());
//                    getResourceDetails.add(fieldDetails);
//                    break;
//                case "Date Of Join":
//                    fieldDetails.setFieldId(eachSetting.getFieldId());
//                    fieldDetails.setFieldName(eachSetting.getFields());
//                    fieldDetails.setFieldValue(getResourceMasterData.getDateOfJoin());
//                    fieldDetails.setFieldType(eachSetting.getResourceType());
//                    getResourceDetails.add(fieldDetails);
//                    break;
//                case "Last Working Date":
//                    fieldDetails.setFieldId(eachSetting.getFieldId());
//                    fieldDetails.setFieldName(eachSetting.getFields());
//                    fieldDetails.setFieldValue(getResourceMasterData.getLastWorkingDate());
//                    fieldDetails.setFieldType(eachSetting.getResourceType());
//                    getResourceDetails.add(fieldDetails);
//                    break;
//                default:
//            }
//        }

//        get the resource details using resourceId
        List<GetResourceDetailsById> resourceDetails = resourceMgmtFieldMapRepo.getResourceDetailsById(resourceId);

//        if (Objects.isNull(resourceDetails)) {
//            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
//        }

//        traverse each visible setting
        for (GetResourceDetailsById eachField : resourceDetails) {

//            set details of the field
            GetResourceDetails fieldDetails = new GetResourceDetails();
            fieldDetails.setFieldName(eachField.getFieldName());
            fieldDetails.setFieldId(eachField.getFieldId());
            fieldDetails.setFieldType(eachField.getFieldType());

            if (eachField.getFieldType().contains(ResourceUtils.FIELD_RESOURCE_PICKLIST)) {
                if (eachField.getFieldValue() != null && !eachField.getFieldValue().isEmpty()) {
                    fieldDetails.setPicklistId(eachField.getFieldValue());
                    switch (eachField.getSettingType()) {
                        case ResourceUtils.HIDE_PICKLIST_TYPE:
                            /*

                             */
                        case ResourceUtils.FIELD_TYPE:
                            switch (eachField.getFieldName()) {
                                case "Employment Type":
                                    fieldDetails.setFieldValue(resourceMgmtEmploymentTypeRepo.getEmploymentTypeById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
//                                case "Status":
//                                    fieldDetails.setFieldValue(projectMgmtPriorityRepo.getPriorityById(Integer.valueOf(eachField.getFieldValue())));
//                                    break;
                                case "Department":
                                    fieldDetails.setFieldValue(resourceMgmtDepartmentRepo.getDepartmentById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
                                case "Location":
                                    fieldDetails.setFieldValue(generalSettingsLocationRepo.getLocationById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
                                case "Role":
                                    String[] roleIdStringList = eachField.getFieldValue().split(",");

                                    List<Integer> roleIdIntegerList = new ArrayList<>();

                                    for (String id : roleIdStringList) {
                                        roleIdIntegerList.add(Integer.valueOf(id));
                                    }
                                    fieldDetails.setFieldValue(resourceMgmtRolesRepo.getRolesByIds(roleIdIntegerList));
                                    break;
                                case "Skills":
                                    String[] skillIdStringList = eachField.getFieldValue().split(",");

                                    List<Integer> skillIdIntegerList = new ArrayList<>();

                                    for (String id : skillIdStringList) {
                                        skillIdIntegerList.add(Integer.valueOf(id));
                                    }
                                    fieldDetails.setFieldValue(resourceMgmtSkillsRepo.getSkillsByIds(skillIdIntegerList));
                                    break;
                                case "Reporting Manager":
                                    fieldDetails.setFieldValue(resourceMgmtRepo.getReportingManagerById(Integer.valueOf(eachField.getFieldValue())));
                                    break;
                                default:
                                    LOGGER.warn("Unrecognised field_name: {}", eachField.getFieldName());
                            }
                            break;
                        case ResourceUtils.CUSTOM_TYPE:
                            String[] picklistIdList = eachField.getFieldValue().split(",");

                            List<Integer> picklistIdValue = new ArrayList<>();

                            for (String id : picklistIdList) {
                                picklistIdValue.add(Integer.valueOf(id));
                            }
                            fieldDetails.setFieldValue(resourceCustomPicklistRepo.getResourceCustomPicklistByPicklistId(picklistIdValue));
                            break;
                        default:
                            LOGGER.warn("Unrecognised field_id: {}", eachField.getFieldId());
                    }
                } else {
//                        field value of status is null as we do not store it in database, and it is of type picklist
                    if (eachField.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_STATUS)) {
                        List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
                        boolean ifAppropriateStatusDoesNotExist = true;

                        Double maxEnabledFteInAdmin = -1d;
                        String maxEnabledFteInAdminName = null;

                        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                                if (resourceAvgFte >= eachStatus.getStartValue() && resourceAvgFte < eachStatus.getEndValue()) {
                                    fieldDetails.setFieldValue(eachStatus.getStatus());
                                    fieldDetails.setPicklistId(String.valueOf(eachStatus.getStatusId()));
                                    ifAppropriateStatusDoesNotExist = false;

                                }
                                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                                    maxEnabledFteInAdminName = eachStatus.getStatus();
                                }
                            }
                        }
                        if (ifAppropriateStatusDoesNotExist) {
                            String resourceStatusIfAbsent = null;
                            if(maxEnabledFteInAdminName!=null && resourceAvgFte.equals(maxEnabledFteInAdmin)){
                                resourceStatusIfAbsent = maxEnabledFteInAdminName;
                            }
                            else {
                                resourceStatusIfAbsent = ResourceUtils.UNDEFINED_UTILIZATION_NAME;
                            }
                            fieldDetails.setPicklistId(String.valueOf(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID));
                            fieldDetails.setFieldValue(resourceStatusIfAbsent);
                        }
                    } else {
                        fieldDetails.setPicklistId("");
                        fieldDetails.setFieldValue("");
                    }
                }

            }
            else {
//                    Allocation(FTE) is not a picklist type, so handled here
                if (eachField.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_ALLOCATION_FTE)) {
                    fieldDetails.setFieldValue(String.valueOf(ResourceUtils.roundToTwoDecimalPlace(resourceAvgFte)));
                }
                else if (eachField.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_FIRST_NAME)) {
                    fieldDetails.setFieldValue(getResourceMasterData.getFirstName());
                }
                else if (eachField.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_LAST_NAME)) {
                    fieldDetails.setFieldValue(getResourceMasterData.getLastName());
                }
                else if (eachField.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_RESOURCE_DATE_OF_JOIN)) {
//                    String dateOfJoin = ResourceUtils.formatDateToStringDate(getResourceMasterData.getDateOfJoin(), timeZone, dateFormat);
//                    fieldDetails.setFieldValue(dateOfJoin);
                    fieldDetails.setFieldValue(String.valueOf(getResourceMasterData.getDateOfJoin()));
                }
                else if (eachField.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_RESOURCE_LAST_WORKING_DATE)) {
//                    String lastWorkingDate = ResourceUtils.formatDateToStringDate(getResourceMasterData.getLastWorkingDate(), timeZone, dateFormat);
//                    fieldDetails.setFieldValue(lastWorkingDate);
                    fieldDetails.setFieldValue(String.valueOf(getResourceMasterData.getLastWorkingDate()));
                }
                else if(eachField.getFieldType().equals(ResourceUtils.FIELD_TYPE_DATE)){
//                    String stringDate = ResourceUtils.formatStringDateToStringDate(eachField.getFieldValue(), timeZone, dateFormat);
//                    fieldDetails.setFieldValue(stringDate);
                    fieldDetails.setFieldValue(eachField.getFieldValue());
                }
                else if (eachField.getFieldValue() != null) {
                    fieldDetails.setFieldValue(eachField.getFieldValue());
                }
                else {
                    fieldDetails.setFieldValue("");
                }

            }

            getResourceDetails.add(fieldDetails);
        }

        GetResourceDetailsMaster resourceDetailsMaster = new GetResourceDetailsMaster();
        resourceDetailsMaster.setResourceData(getResourceDetails);
        resourceDetailsMaster.setCreatedBy(getResourceMasterData.getCreatedBy());
        resourceDetailsMaster.setCreatedDate(getResourceMasterData.getCreatedDate());
        resourceDetailsMaster.setModifiedBy(getResourceMasterData.getModifiedBy());
        resourceDetailsMaster.setModifiedDate(getResourceMasterData.getModifiedDate());
        return resourceDetailsMaster;
    }

    public Object updateResourceDetails(Integer resourceId, PostResourceDetailsMaster postResourceDetailsMaster, String modifiedBy) {

        List<PostResourceDetails> updateDetails = postResourceDetailsMaster.getResourceData();

//        get the stored project details using projectId
        ResourceMgmt resourceDetailsUpdate = resourceMgmtRepo.getResourceDetailsById(resourceId);

        if (Objects.isNull(resourceDetailsUpdate)) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
        }

        resourceDetailsUpdate.setModifiedBy(modifiedBy);
        resourceDetailsUpdate.setModifiedDate(new Date());

//        project custom picklist options
        List<ResourceCustomPicklist> resourceCustomPicklist = resourceCustomPicklistRepo.findAll();
        Map<Integer, ResourceCustomPicklist> resourceCustomPicklistToMap
                = resourceCustomPicklist.stream().collect(Collectors.toMap(ResourceCustomPicklist::getId, rcp -> rcp));

//        project field map admin settings
        List<ResourceMgmtFieldMap> resourceMgmtFieldMap = resourceMgmtFieldMapRepo.findAll();
        Map<Integer, ResourceMgmtFieldMap> resourceMgmtFieldMapToMap
                = resourceMgmtFieldMap.stream().collect(Collectors.toMap(ResourceMgmtFieldMap::getFieldId, rmf -> rmf));


//        array to store updated field values
        List<ResourceMgmtFieldValue> fieldValuesUpdate = new ArrayList<>();

//        get the stored project custom details using projectId
        List<ResourceMgmtFieldValue> resourceDetailStored = resourceMgmtFieldValueRepo.getResourceDetailsForUpdateById(resourceId);

//        convert the stored project custom field values to a map for quick access
        Map<Integer, ResourceMgmtFieldValue> resourceCustomFieldValueMap = new HashMap<>();
        for (ResourceMgmtFieldValue eachValue : resourceDetailStored) {
            resourceCustomFieldValueMap.put(eachValue.getFieldId(), eachValue);
        }

        Date startDate = null, endDate = null;

        for (PostResourceDetails eachFieldValue : updateDetails) {

            if (eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_FIRST_NAME)) {
                resourceDetailsUpdate.setFirstName(String.valueOf(eachFieldValue.getFieldValue()));
            }
            else if (eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_LAST_NAME)) {
                resourceDetailsUpdate.setLastName(String.valueOf(eachFieldValue.getFieldValue()));
            }
            else if (eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_RESOURCE_DATE_OF_JOIN)) {
                Date dateOfJoin = new Date();
                try {
                    dateOfJoin = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(eachFieldValue.getFieldValue()));
                    startDate = dateOfJoin;
                    resourceDetailsUpdate.setDateOfJoin(dateOfJoin);
                } catch (ParseException e) {
                    LOGGER.warn("Parse Exception");
//                                e.printStackTrace();
                }
            }
            else if (eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_RESOURCE_LAST_WORKING_DATE)) {

                if (!Objects.equals(String.valueOf(eachFieldValue.getFieldValue()), ResourceUtils.FIELD_RESOURCE_LWD_PLACEHOLDER)
                        && eachFieldValue.getFieldValue()!=null
                        && !String.valueOf(eachFieldValue.getFieldValue()).isEmpty()) {
                    Date lastWorkingDate = null;
                    try {
                        lastWorkingDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(eachFieldValue.getFieldValue()));
                        endDate = lastWorkingDate;
                        resourceDetailsUpdate.setLastWorkingDate(lastWorkingDate);
                    } catch (ParseException e) {
                        LOGGER.warn("Parse Exception");
//                                e.printStackTrace();
                    }
                }
                else{
                    resourceDetailsUpdate.setLastWorkingDate(null);
                }
            }
//            if the data for that custom field exists, update that
            else if (resourceCustomFieldValueMap.containsKey(eachFieldValue.getFieldId())) {

                ResourceMgmtFieldValue valueToUpdate = resourceCustomFieldValueMap.get(eachFieldValue.getFieldId());

//                    validations start from here

                if (eachFieldValue.getFieldType().contains(ResourceUtils.FIELD_TYPE_PICKLIST)) {
//                        Apply check if these picklist ids are valid or not for the given picklist
                    try {
                        if(eachFieldValue.getPicklistId()!=null && !eachFieldValue.getPicklistId().isEmpty()) {
                            Set<Integer> allIds = new HashSet<>();
                            String[] ids = eachFieldValue.getPicklistId().split(",");
                            for (String eachId : ids) {
                                allIds.add(Integer.valueOf(eachId));
                            }
                            Object returnData = checkForValidPicklistIds(
                                    resourceMgmtFieldMapToMap.get(eachFieldValue.getFieldId()),
                                    new ArrayList<>(allIds),
                                    resourceCustomPicklistToMap);

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
                                        case ResourceUtils.FIELD_TYPE_DATE:
                                            LocalDate localDate = LocalDate.parse(fieldValue);
                                            Date date = java.sql.Date.valueOf(localDate);
                                            eachFieldValue.setFieldValue(String.valueOf(date));
                                            break;
                                        case ResourceUtils.FIELD_TYPE_NUMBER:
//                                                TODO:: can only proceed if we know what type of input to expect

                                            break;
                                        case ResourceUtils.FIELD_TYPE_CURRENCY:
//                                                TODO:: ask for currency checks, like 2 decimal, negative currency, maximum limit, etc.

                                            break;
                                        case ResourceUtils.FIELD_TYPE_EMAIL:
                                            String email = fieldValue;
                                            String basicEmailFormat = "^([A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

                                            Pattern emailPattern = Pattern.compile(basicEmailFormat);
                                            boolean isEmailPatternCorrect =emailPattern.matcher(email).matches();
                                            if(!isEmailPatternCorrect){
                                                LOGGER.warn(ResourceUtils.INCORRECT_EMAIL_FORMAT);
                                                isDataCorrect = false;
                                            }
                                            break;
                                        case ResourceUtils.FIELD_TYPE_TEXT:

                                            if(eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION)){
                                                if(!fieldValue.equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION_ONSITE)
                                                        && !fieldValue.equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION_OFFSHORE) ){
                                                    isDataCorrect = false;
                                                }
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

                if(eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_EMAIL)){
                    Boolean checkIfExists = resourceMgmtFieldValueRepo.checkIfEmailIdExists(resourceId, eachFieldValue.getFieldId(), String.valueOf(eachFieldValue.getFieldValue()));
                    if(checkIfExists){
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        error.setMessage(ResourceUtils.EMAIL_ALREADY_EXISTS);
                        return error;
                    }
                }

                if(eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER)
                        && eachFieldValue.getPicklistId()!=null
                        && !eachFieldValue.getPicklistId().isEmpty()){

                    Object checkHierarchyCycle = checkForResourceHierarchyCycle(eachFieldValue, resourceId);
                    if(checkHierarchyCycle instanceof Error){
                        return checkHierarchyCycle;
                    }
                }

                valueToUpdate.setModifiedBy(modifiedBy);
                valueToUpdate.setModifiedDate(new Date());
                fieldValuesUpdate.add(valueToUpdate);

            }
//            if the data for that custom field does not exist, create a new instance with the new data
            else {
                if(eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_EMAIL)){
                    Boolean checkIfExists = resourceMgmtFieldValueRepo.checkIfEmailIdExists(resourceId, eachFieldValue.getFieldId(), String.valueOf(eachFieldValue.getFieldValue()));
                    if(checkIfExists){
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        error.setMessage(ResourceUtils.EMAIL_ALREADY_EXISTS);
//            Response error = new Response(new Date(), HttpStatus.BAD_REQUEST.value(), ResourceUtils.EMAIL_ALREADY_EXISTS);
                        return error;
                    }
                }

                if(eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER)
                        && eachFieldValue.getPicklistId()!=null
                        && !eachFieldValue.getPicklistId().isEmpty()){

                    Object checkHierarchyCycle = checkForResourceHierarchyCycle(eachFieldValue, resourceId);
                    if(checkHierarchyCycle instanceof Error){
                        return checkHierarchyCycle;
                    }
                }

                if (!eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_STATUS) && !eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_ALLOCATION_FTE)) {
                    ResourceMgmtFieldValue newValue = new ResourceMgmtFieldValue();
                    newValue.setResourceId(resourceId);
                    newValue.setFieldId(eachFieldValue.getFieldId());

                    newValue.setCreatedDate(new Date());
                    newValue.setCreatedBy(modifiedBy);
                    newValue.setModifiedDate(new Date());
                    newValue.setModifiedBy(modifiedBy);

                    if (eachFieldValue.getFieldType().contains(ResourceUtils.FIELD_TYPE_PICKLIST)) {
//                        Apply check if these picklist ids are valid or not for the given picklist
                        try {
                            if(eachFieldValue.getPicklistId()!=null && !eachFieldValue.getPicklistId().isEmpty()) {
                                Set<Integer> allIds = new HashSet<>();
                                String[] ids = eachFieldValue.getPicklistId().split(",");
                                for (String eachId : ids) {
                                    allIds.add(Integer.valueOf(eachId));
                                }

                                Object returnData = checkForValidPicklistIds(
                                        resourceMgmtFieldMapToMap.get(eachFieldValue.getFieldId()),
                                        new ArrayList<>(allIds),
                                        resourceCustomPicklistToMap);

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
                                        case ResourceUtils.FIELD_TYPE_DATE:
                                            LocalDate localDate = LocalDate.parse(fieldValue);
                                            Date date = java.sql.Date.valueOf(localDate);
                                            eachFieldValue.setFieldValue(String.valueOf(date));
                                            break;
                                        case ResourceUtils.FIELD_TYPE_NUMBER:
//                                                TODO:: can only proceed if we know what type of input to expect

                                            break;
                                        case ResourceUtils.FIELD_TYPE_CURRENCY:
//                                                TODO:: ask for currency checks, like 2 decimal, negative currency, maximum limit, etc.

                                            break;
                                        case ResourceUtils.FIELD_TYPE_EMAIL:
                                            String email = fieldValue;
                                            String basicEmailFormat = "^([A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";

                                            Pattern emailPattern = Pattern.compile(basicEmailFormat);
                                            boolean isEmailPatternCorrect =emailPattern.matcher(email).matches();
                                            if(!isEmailPatternCorrect){
                                                LOGGER.warn(ResourceUtils.INCORRECT_EMAIL_FORMAT);
                                                isDataCorrect = false;
                                            }
                                            break;
                                        case ResourceUtils.FIELD_TYPE_TEXT:

                                            if(eachFieldValue.getFieldName().equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION)){
                                                if(!fieldValue.equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION_ONSITE)
                                                        && !fieldValue.equals(ResourceUtils.FIELD_RESOURCE_EMPLOYEE_LOCATION_OFFSHORE) ){
                                                    isDataCorrect = false;
                                                }
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

        if(startDate!=null && endDate!=null) {

            if(startDate.compareTo(endDate)>0){
                String message = ResourceUtils.INCORRECT_RESOURCE_DATE_RANGE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }

            List<String> projectNames = projectResourceMappingRepo.getProjectListOutsideResourceDateRange(resourceId, startDate, endDate);
            if(projectNames!=null && !projectNames.isEmpty()){
                String message = ResourceUtils.outsideResourceDateRangeAllocation(projectNames);
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }

            List<DelegatedResourceHistory> delegatedResourceHistoryList = delegatedResourceHistoryRepo.getAllResourceActiveDelegations(resourceId, startDate, endDate);
            if(delegatedResourceHistoryList!=null && !delegatedResourceHistoryList.isEmpty()){
                String message = ResourceUtils.RESOLVE_RESOURCE_DELEGATIONS_BEFORE_DATE_CHANGE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }

        }
        else if (startDate!=null){
            List<String> projectNames = projectResourceMappingRepo.getProjectListBeforeResourceDateOfJoin(resourceId, startDate);
            if(projectNames!=null && !projectNames.isEmpty()){
                String message = ResourceUtils.outsideResourceDateRangeAllocation(projectNames);
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }

            List<DelegatedResourceHistory> delegatedResourceHistoryList = delegatedResourceHistoryRepo.getAllResourceActiveDelegations(resourceId, startDate);
            if(delegatedResourceHistoryList!=null && !delegatedResourceHistoryList.isEmpty()){
                String message = ResourceUtils.RESOLVE_RESOURCE_DELEGATIONS_BEFORE_DATE_CHANGE;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }
        else{
            String message = ResourceUtils.EMPTY_RESOURCE_DATE_OF_JOIN;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

//        update the project pre-defined field details and the custom field details
        resourceMgmtRepo.save(resourceDetailsUpdate);
        resourceMgmtFieldValueRepo.saveAll(fieldValuesUpdate);
        LOGGER.info("User " + modifiedBy + " updated resource id " + resourceDetailsUpdate.getResourceId() + " at " + new Date());
        return true;
    }

    private Object checkForResourceHierarchyCycle(PostResourceDetails eachFieldValue, Integer resourceId){

        String getAncestralPathByResourceIdQuery = ResourceUtils.getFile("getAncestralPathByResourceId.txt");

        Query query = entityManager.createNativeQuery(getAncestralPathByResourceIdQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(AncestralPathOfResource.class));

        Integer reportingManagerId = null;
        try{
            reportingManagerId = Integer.valueOf(eachFieldValue.getPicklistId());

            query.setParameter("reportingManager", reportingManagerId);

            List<AncestralPathOfResource> ancestralPathOfResourceList = query.getResultList();
            boolean doesCycleExists = false;
            String ancestralPathByName = null;
            for(AncestralPathOfResource eachLevel: ancestralPathOfResourceList){
                if (eachLevel.getResource_id().equals(resourceId)) {
                    doesCycleExists = true;
                    ancestralPathByName = eachLevel.getAncestral_path_by_name();
                    break;
                }
            }
            if(doesCycleExists){
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                error.setMessage(ResourceUtils.HIERARCHICAL_CYCLE_DETECTED + ": " + ancestralPathByName);
                return error;
            }
        }
        catch(Exception e){
            LOGGER.warn("incorrect Reporting Manager input data");
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Error Occurred");
            return error;
        }

        return true;
    }

    public Object deleteResourceDetails(Integer resourceId, String modifiedBy){

//        get the stored project details using projectId
        Optional<ResourceMgmt> resourceToDelete = resourceMgmtRepo.findById(resourceId);

        if (!resourceToDelete.isPresent()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
        }

        /*
        TODO:: who can delete a resource, ask
        apply checks to verify the person calling the api has access to delete the resource

        if yes
            delete all allocations
            TODO:: check if the resource is a project owner to some project, if yes, ask how to handle that
            check if this resource is a department head, if yes, remove it
            check for any delegations
            set email to something unique, like resource.{resourceId}@deleted.com
            check for all resource who are reporting to this person, ask how to deal with that
            check if any other data is to be set something specific
            set remaining resource data as deleted
            change date of join and last working date to some 1000 year or something
            change resource name to deleted resource

        if
            no access, return error message
         */

//        set the manager of all managed department to null
        List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
        if(resourceMgmtDepartmentList!=null && !resourceMgmtDepartmentList.isEmpty()){

            List<ResourceMgmtDepartment> resourceMgmtDepartmentListToUpdate = new ArrayList<>();

            for(ResourceMgmtDepartment eachDepartment: resourceMgmtDepartmentList){

                if(eachDepartment.getIsActive().equals("1") && eachDepartment.getResourceId().equals(resourceId)){
                    eachDepartment.setResourceId(null);
                    eachDepartment.setModifiedBy(modifiedBy);
                    eachDepartment.setModifiedDate(new Date());
                    resourceMgmtDepartmentListToUpdate.add(eachDepartment);
                }
            }
            resourceMgmtDepartmentRepo.saveAll(resourceMgmtDepartmentListToUpdate);
        }

//        delete all delegations
        List<DelegatedDepartmentHistory> delegatedDepartmentHistoryList = delegatedDepartmentHistoryRepo.findAll();
        if(delegatedDepartmentHistoryList!=null && !delegatedDepartmentHistoryList.isEmpty()){

            List<DelegatedDepartmentHistory> delegatedDepartmentHistoriesToUpdate = new ArrayList<>();

            for(DelegatedDepartmentHistory eachDelegatedDepartment: delegatedDepartmentHistoryList){

                if(eachDelegatedDepartment.getIsActive().equals("1") && eachDelegatedDepartment.getResourceId().equals(resourceId)){
                    eachDelegatedDepartment.setIsActive("0");
                    eachDelegatedDepartment.setModifiedBy(modifiedBy);
                    eachDelegatedDepartment.setModifiedDate(new Date());
                    delegatedDepartmentHistoriesToUpdate.add(eachDelegatedDepartment);
                }
            }
            delegatedDepartmentHistoryRepo.saveAll(delegatedDepartmentHistoriesToUpdate);
        }


//        delete all allocations
        List<ProjectResourceMapping> projectResourceMappingList = projectResourceMappingRepo.getAllProjectResourceMappingByResourceId(resourceId);
        if(projectResourceMappingList!=null && !projectResourceMappingList.isEmpty()) {
            List<ProjectResourceMapping> projectResourceMappingListToDelete = new ArrayList<>();
            for (ProjectResourceMapping eachAllocations : projectResourceMappingList) {
                eachAllocations.setIsDeleted("1");
                eachAllocations.setModifiedBy(modifiedBy);
                eachAllocations.setModifiedDate(new Date());
                projectResourceMappingListToDelete.add(eachAllocations);
            }
            projectResourceMappingRepo.saveAll(projectResourceMappingListToDelete);
        }

        List<ResourceMgmtFieldMap> resourceMgmtFieldMapList = resourceMgmtFieldMapRepo.findAll();

        Map<Integer, ResourceMgmtFieldMap> resourceMgmtFieldMapToMap
                = resourceMgmtFieldMapList.stream().collect(Collectors.toMap(ResourceMgmtFieldMap::getFieldId, data->data));

        String reportingManagerId = null;
        Integer reportingManagerFieldId = null;

//        update all resource details and set the reporting manager of all juniors to current resource's reporting manager
        List<ResourceMgmtFieldValue> resourceDetails = resourceMgmtFieldValueRepo.getAllResourceDetailsByResourceId(resourceId);
        if(resourceDetails!=null && !resourceDetails.isEmpty()){
            List<ResourceMgmtFieldValue> resourceFieldValueToUpdate = new ArrayList<>();
            for(ResourceMgmtFieldValue eachDetail: resourceDetails){
                if(resourceMgmtFieldMapToMap.get(eachDetail.getFieldId()).getFields().equals(ResourceUtils.FIELD_RESOURCE_EMAIL)){
                    eachDetail.setFieldValue("resource."+eachDetail.getResourceId()+"@deleted.com");
                }
                else{
                    if(resourceMgmtFieldMapToMap.get(eachDetail.getFieldId()).getFields().equals(ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER)){
                        reportingManagerId = eachDetail.getFieldValue();
                        reportingManagerFieldId = eachDetail.getFieldId();
                    }
                    eachDetail.setFieldValue("");
                }
                eachDetail.setModifiedBy(modifiedBy);
                eachDetail.setModifiedDate(new Date());
                resourceFieldValueToUpdate.add(eachDetail);
            }

//            set the reporting manager of all juniors to current resource's reporting manager
            List<ResourceMgmtFieldValue> reportingResources
                    = resourceMgmtFieldValueRepo.getAllReportingResourcesByResourceId(reportingManagerFieldId ,String.valueOf(resourceId));
            if(reportingResources!=null && !reportingResources.isEmpty()){
                for(ResourceMgmtFieldValue eachDetail: reportingResources){
                    eachDetail.setFieldValue(reportingManagerId);
                    eachDetail.setModifiedBy(modifiedBy);
                    eachDetail.setModifiedDate(new Date());

                    resourceFieldValueToUpdate.add(eachDetail);
                }
            }

            resourceMgmtFieldValueRepo.saveAll(resourceFieldValueToUpdate);
        }

//        update resource name and date of joining and last working date
        ResourceMgmt resourceMgmt = resourceToDelete.get();
        resourceMgmt.setFirstName("Deleted");
        resourceMgmt.setLastName("Resource");
        LocalDate localDate = LocalDate.parse("1900-01-01");
        resourceMgmt.setDateOfJoin(java.sql.Date.valueOf(localDate));
        resourceMgmt.setLastWorkingDate(java.sql.Date.valueOf(localDate));
        resourceMgmt.setModifiedBy(modifiedBy);
        resourceMgmt.setModifiedDate(new Date());

        resourceMgmtRepo.save(resourceMgmt);

        return true;
    }

    public Object getResourceHierarchy(){

        String getAncestralPathByResourceIdQuery = ResourceUtils.getFile("getResourceHierarchyDetails.txt");

        Query query = entityManager.createNativeQuery(getAncestralPathByResourceIdQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceHierarchyDetails.class));

        query.setParameter("currentDate", new Date());

        List<ResourceHierarchyDetails> resourceHierarchyDetailsList = query.getResultList();

        List<GetResourceHierarchy> getResourceHierarchyList = new ArrayList<>();

        if(resourceHierarchyDetailsList!=null && !resourceHierarchyDetailsList.isEmpty()) {
            for (ResourceHierarchyDetails eachResource : resourceHierarchyDetailsList) {
                getResourceHierarchyList.add(new GetResourceHierarchy(eachResource));
            }
        }

        if(getResourceHierarchyList.isEmpty()){
            getResourceHierarchyList = null;
        }

        return getResourceHierarchyList;
    }

    public Object getSpecificResourceHierarchy(String userEmail){

        String getAncestralPathByResourceIdQuery = ResourceUtils.getFile("getResourceHierarchyDetails.txt");

        Query query = entityManager.createNativeQuery(getAncestralPathByResourceIdQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceHierarchyDetails.class));

        query.setParameter("currentDate", new Date());

        List<ResourceHierarchyDetails> resourceHierarchyDetailsList = query.getResultList();

        List<GetResourceHierarchy> getResourceHierarchyList = new ArrayList<>();

        if(resourceHierarchyDetailsList!=null && !resourceHierarchyDetailsList.isEmpty()) {
            for (ResourceHierarchyDetails eachResource : resourceHierarchyDetailsList) {
                getResourceHierarchyList.add(new GetResourceHierarchy(eachResource));
            }
        }

        Integer resourceId = null;

        List<ResourceMgmt> loggedInUser = resourceMgmtRepo.getResourceByEmail(userEmail);

        if(loggedInUser!=null && !loggedInUser.isEmpty()){
            resourceId = loggedInUser.get(0).getResourceId();
        }

        if(getResourceHierarchyList.isEmpty()){
            getResourceHierarchyList = null;
        }

        Map<String, Object> returnData = new HashMap<>();
        returnData.put("parentNodeId", resourceId);
        returnData.put("data", getResourceHierarchyList);

        return returnData;
    }

    public List<GetResourceAnalyticsMaster> getResourceAnalyticsData(GetDateRange getDateRange, Integer departmentId, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);

        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        GeneralSettings generalSettings = generalSettingsList.get(0);
        String timeZone = generalSettings.getTimezone();
        String dateFormat = "yyyy-MM-dd";

        List<ResourceMgmtFieldMap> getAnalyticsVisibleField = resourceMgmtFieldMapRepo.getResourceAnalyticsSettings();
        List<ResourceMgmtFieldMap> getFieldsData = resourceMgmtFieldMapRepo.getFieldDataByFieldName(ResourceUtils.RESOURCE_FIELD_NAMES_DATA);
        Map<String, ResourceMgmtFieldMap> getFieldsDataMap = getFieldsData.stream().collect(Collectors.toMap(ResourceMgmtFieldMap::getFields, data -> data));

        List<GetResourceAnalyticsMaster> getResourceAnalyticsMasterList = null;
        String resourceAnalyticsQuery = ResourceUtils.getFile("getResourceAnalyticsData.txt");

        if(departmentId!=null && departmentId!=0){
            resourceAnalyticsQuery = ResourceUtils.getFile("getResourceAnalyticsDataByDepartmentId.txt");
        }

        Query query = entityManager.createNativeQuery(resourceAnalyticsQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourceAnalytics.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());

        if(departmentId!=null && departmentId!=0){
            query.setParameter("departmentId", departmentId);
        }
        else{
            query.setParameter("resourceIds", visibleResourceIds);
        }

//        get analytics data table
        List<GetResourceAnalytics> getResourceAnalyticsList = query.getResultList();

        if (getResourceAnalyticsList != null && !getResourceAnalyticsList.isEmpty()) {

//            get resource status data for status field inside the analytics table
            List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();

//            group all resource data by resourceId
            Map<Integer, List<GetResourceAnalytics>> groupByResource = getResourceAnalyticsList.stream().collect(Collectors.groupingBy(GetResourceAnalytics::getResource_id));
            getResourceAnalyticsMasterList = new ArrayList<>();

//            for each resource
            for (Map.Entry<Integer, List<GetResourceAnalytics>> entry : groupByResource.entrySet()) {

                GetResourceAnalyticsMaster getResourceAnalyticsMaster = new GetResourceAnalyticsMaster();

                if (!entry.getValue().isEmpty()) {
                    Map<Integer, Boolean> presentFields = new HashMap<>();

//                    convert first record to Map<String, Object> objToMap where string field and object is value
                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);

                    getResourceAnalyticsMaster.setResourceId(entry.getValue().get(0).getResource_id());
                    List<Map<String, Object>> fieldList = new ArrayList<>();

                    LocalDate startDate = LocalDate.parse(String.valueOf(entry.getValue().get(0).getStart_date()));
                    LocalDate endDate = LocalDate.parse(String.valueOf(entry.getValue().get(0).getEnd_date()));
                    Integer workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);
                    Double resourceAvgFte = Double.parseDouble(String.valueOf(entry.getValue().get(0).getAllocation()));
                    resourceAvgFte = ResourceUtils.roundToTwoDecimalPlace(resourceAvgFte/workingDays);

//                    traverse the objToMap to get all the data stored in resource_master table
                    for (Map.Entry<String, Object> getResourceAnalytics : objToMap.entrySet()) {
                        if (!getResourceAnalytics.getKey().equals("fields")
                                && !getResourceAnalytics.getKey().equals("fieldValue")
                                && !getResourceAnalytics.getKey().equals("resourceType")
                                && !getResourceAnalytics.getKey().equals("startDate")
                                && !getResourceAnalytics.getKey().equals("endDate")) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", getResourceAnalytics.getKey());

                            switch (getResourceAnalytics.getKey()) {
                                case ResourceUtils.FIELD_RESOURCE_ID:
                                    fields.put("fieldType", "Integer");
                                    fields.put("fieldValue", getResourceAnalytics.getValue());
                                    fields.put("fieldId", 0);
                                    break;
                                case ResourceUtils.FIELD_RESOURCE_FIRST_NAME:
//                                case ResourceUtils.FIELD_RESOURCE_CREATED_BY:
                                case ResourceUtils.FIELD_RESOURCE_LAST_NAME:
//                                case ResourceUtils.FIELD_RESOURCE_MODIFIED_BY:
                                    if(getFieldsDataMap.containsKey(getResourceAnalytics.getKey()) && getFieldsDataMap.get(getResourceAnalytics.getKey()).getEnabled().equals("1")
                                            && getFieldsDataMap.get(getResourceAnalytics.getKey()).getResourceAnalyticsView().equals("1")) {
                                        fields.put("fieldValue", getResourceAnalytics.getValue());
                                        fields.put("fieldType", "Text");
                                        fields.put("fieldId", getFieldsDataMap.get(getResourceAnalytics.getKey()).getFieldId());
                                        presentFields.put(getFieldsDataMap.get(getResourceAnalytics.getKey()).getFieldId(), true);
                                    }
                                    break;
                                case ResourceUtils.FIELD_RESOURCE_RESOURCE_DATE_OF_JOIN:
//                                case ResourceUtils.FIELD_RESOURCE_CREATED_DATE:
                                case ResourceUtils.FIELD_RESOURCE_RESOURCE_LAST_WORKING_DATE:
//                                case ResourceUtils.FIELD_RESOURCE_MODIFIED_DATE:
                                    if(getFieldsDataMap.containsKey(getResourceAnalytics.getKey()) && getFieldsDataMap.get(getResourceAnalytics.getKey()).getEnabled().equals("1")
                                            && getFieldsDataMap.get(getResourceAnalytics.getKey()).getResourceAnalyticsView().equals("1")) {
//                                        String stringDate = ResourceUtils.formatStringDateToStringDate(String.valueOf(getResourceAnalytics.getValue()), timeZone, dateFormat);
//                                        fields.put("fieldValue", stringDate);
                                        fields.put("fieldValue", String.valueOf(getResourceAnalytics.getValue()));
                                        fields.put("fieldType", "Date");
                                        fields.put("fieldId", getFieldsDataMap.get(getResourceAnalytics.getKey()).getFieldId());
                                        presentFields.put(getFieldsDataMap.get(getResourceAnalytics.getKey()).getFieldId(), true);
                                    }
                                    break;
                                case ResourceUtils.FIELD_RESOURCE_ALLOCATION_FTE:
                                    if(getFieldsDataMap.containsKey(getResourceAnalytics.getKey()) && getFieldsDataMap.get(getResourceAnalytics.getKey()).getEnabled().equals("1")
                                            && getFieldsDataMap.get(getResourceAnalytics.getKey()).getResourceAnalyticsView().equals("1")) {
                                        fields.put("fieldValue", String.valueOf(resourceAvgFte));
                                        fields.put("fieldType", "Number");
                                        fields.put("fieldId", getFieldsDataMap.get(getResourceAnalytics.getKey()).getFieldId());
                                        presentFields.put(getFieldsDataMap.get(getResourceAnalytics.getKey()).getFieldId(), true);
                                    }
                                    break;
                            }

//                            try {
//                                fields.put("resourceType", entry.getValue().get(0).getClass().getDeclaredField(getResourceAnalytics.getKey()).getType().getSimpleName());
//
//                            } catch (NoSuchFieldException e) {
//                                fields.put("resourceType", "");
//                            }
                            if (fields.containsKey("fieldType")) {
                                fieldList.add(fields);
                            }
                        }
                    }

                    if (getFieldsDataMap.containsKey(ResourceUtils.FIELD_RESOURCE_STATUS) && getFieldsDataMap.get(ResourceUtils.FIELD_RESOURCE_STATUS).getEnabled().equals("1")
                            && getFieldsDataMap.get(ResourceUtils.FIELD_RESOURCE_STATUS).getResourceAnalyticsView().equals("1")) {

//                        based on Allocation(FTE), assign a resource status to the resource
                        BigDecimal allocation = (BigDecimal) objToMap.get(ResourceUtils.FIELD_RESOURCE_ALLOCATION_FTE);

                        Map<String, Object> resourceStatus = new HashMap<>();
                        resourceStatus.put("fieldName", ResourceUtils.FIELD_RESOURCE_STATUS);
                        boolean ifAppropriateStatusDoesNotExist = true;

                        Double maxEnabledFteInAdmin = -1d;
                        String maxEnabledFteInAdminName = null;

                        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                                if (resourceAvgFte >= eachStatus.getStartValue() && resourceAvgFte < eachStatus.getEndValue()) {
                                    resourceStatus.put("fieldValue", eachStatus.getStatus());
                                    ifAppropriateStatusDoesNotExist = false;
                                    break;
                                }
                                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                                    maxEnabledFteInAdminName = eachStatus.getStatus();
                                }
                            }
                        }
                        if (ifAppropriateStatusDoesNotExist) {
                            String resourceStatusIfAbsent = null;
                            if(maxEnabledFteInAdminName!=null && resourceAvgFte.equals(maxEnabledFteInAdmin)){
                                resourceStatusIfAbsent = maxEnabledFteInAdminName;
                            }
                            else {
                                resourceStatusIfAbsent = ResourceUtils.UNDEFINED_UTILIZATION_NAME;
                            }
                            resourceStatus.put("fieldValue", resourceStatusIfAbsent);
                        }
                        resourceStatus.put("fieldType", "Text");
                        resourceStatus.put("fieldId", getFieldsDataMap.get(ResourceUtils.FIELD_RESOURCE_STATUS).getFieldId());
                        presentFields.put(getFieldsDataMap.get(ResourceUtils.FIELD_RESOURCE_STATUS).getFieldId(), true);
//                        enter the data to field list
                        fieldList.add(resourceStatus);
                    }

                    for (GetResourceAnalytics getResourceAnalytics : entry.getValue()) {
                        if (StringUtils.isNotBlank(getResourceAnalytics.getFields())) {
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", getResourceAnalytics.getFields());
//                            if(getResourceAnalytics.getResource_type().equals(ResourceUtils.FIELD_TYPE_DATE)){
////                                String stringDate = ResourceUtils.formatStringDateToStringDate(String.valueOf(getResourceAnalytics.getField_value()), timeZone, dateFormat);
////                                fields.put("fieldValue", stringDate);
//                                fields.put("fieldValue", String.valueOf(getResourceAnalytics.getField_value()));
//                            }
//                            else{
                                fields.put("fieldValue", getResourceAnalytics.getField_value());
//                            }
                            fields.put("fieldType", getResourceAnalytics.getResource_type());
                            fields.put("fieldId", getResourceAnalytics.getField_id());
                            presentFields.put(getResourceAnalytics.getField_id(), true);
                            fieldList.add(fields);
                        }
                    }
                    for(ResourceMgmtFieldMap eachField: getAnalyticsVisibleField){
                        if(!presentFields.containsKey(eachField.getFieldId())){
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("fieldName", eachField.getFields());
                            fields.put("fieldValue", "");
                            fields.put("fieldType", eachField.getResourceType());
                            fields.put("fieldId", eachField.getFieldId());
                            fieldList.add(fields);
                        }
                    }
                    fieldList.sort(Comparator.comparingInt(m->(int)m.get("fieldId")));
                    getResourceAnalyticsMaster.setFields(fieldList);
                }
                getResourceAnalyticsMasterList.add(getResourceAnalyticsMaster);
            }

        }

        if(getResourceAnalyticsMasterList==null || getResourceAnalyticsMasterList.isEmpty()){
            getResourceAnalyticsMasterList=null;
        }
        else{
            getResourceAnalyticsMasterList.sort(Comparator.comparing(GetResourceAnalyticsMaster::getResourceId).reversed());
        }

        return getResourceAnalyticsMasterList;
    }

    public Object downloadResourceAnalyticsTable(GetDateRange getDateRange, Integer departmentId, String email, String role){

        List<GetResourceAnalyticsMaster> getResourceAnalyticsMasterList = getResourceAnalyticsData(getDateRange, departmentId, email, role);
        List<ResourceMgmtFieldMap> resourceMgmtFieldMapList = resourceMgmtFieldMapRepo.findAll();
        resourceMgmtFieldMapList.sort(Comparator.comparingInt(ResourceMgmtFieldMap::getFieldId));

        byte[] excelFile = null;
        try {
            excelFile = excelProcessingService.generateExcelFileForResourceAnalyticsTable(getResourceAnalyticsMasterList, resourceMgmtFieldMapList);
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ResourceUtils.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        return excelFile;
    }

//    public List<GetResourceAllocationMatrix> getResourceAllocationMatrix(GetDateRange getDateRange) {
//
//        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
//            return null;
//        }
//
//        List<GetResourceAllocationMatrix> getResourceAnalyticsMasterList = new ArrayList<>();
//        String resourceProjectMapping = ResourceUtils.getFile("resourceAllProjectAllocation.txt");
//
//        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetProjectResourceAllocationData.class));
//
//        query.setParameter("startDate", getDateRange.getStartDate());
//        query.setParameter("endDate", getDateRange.getEndDate());
//
//        List<GetProjectResourceAllocationData> getResourceAnalyticsList = query.getResultList();
//
//        if (getResourceAnalyticsList != null && !getResourceAnalyticsList.isEmpty()) {
//
//            Map<Integer, List<GetProjectResourceAllocationData>> groupByResourceId = getResourceAnalyticsList.stream().collect(Collectors.groupingBy(GetProjectResourceAllocationData::getResource_id));
//            List<GetResourceAllocationMatrix> resourceAllocationMatrixList = new ArrayList<>();
//
//            for (Map.Entry<Integer, List<GetProjectResourceAllocationData>> entry : groupByResourceId.entrySet()) {
//                GetResourceAllocationMatrix getData = new GetResourceAllocationMatrix();
//                List<GetResourceAllProjectData> allProjectDataList = new ArrayList<>();
//
//                if (!entry.getValue().isEmpty()) {
//
//                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);
////                    getData.setResourceId(entry.getValue().get(0).getResource_id());
////                    getData.setResourceName(entry.getValue().get(0).getResource_name());
////                    getData.setRole(entry.getValue().get(0).getRole());
////                    getData.setEmpLocation(entry.getValue().get(0).getEmp_location());
////                    getData.setEmpType(entry.getValue().get(0).getEmployment_type());
//
//                    for (Map.Entry<String, Object> getResourceData : objToMap.entrySet()) {
//                        //if (!getResourceAnalytics.getKey().equals("fields") && !getResourceAnalytics.getKey().equals("fieldValue") && !getResourceAnalytics.getKey().equals("resourceType")) {
//                        switch (getResourceData.getKey()) {
//                            case "resourceId":
//                                getData.setResourceId(Integer.valueOf(String.valueOf(getResourceData.getValue())));
//                                break;
//                            case "resourceName":
//                                getData.setResourceName(String.valueOf(getResourceData.getValue()));
//                                break;
//                            case "role":
//                                if (getResourceData.getValue() != null) {
//                                    getData.setRole(String.valueOf(getResourceData.getValue()));
//                                } else {
//                                    getData.setRole("");
//                                }
//                                break;
//                            case "empLocation":
//                                if (getResourceData.getValue() != null) {
//                                    getData.setEmpLocation(String.valueOf(getResourceData.getValue()));
//                                } else {
//                                    getData.setEmpLocation("");
//                                }
//                                break;
//                            case "employmentType":
//                                if (getResourceData.getValue() != null) {
//                                    getData.setEmpType(String.valueOf(getResourceData.getValue()));
//                                } else {
//                                    getData.setEmpType("");
//                                }
//                                break;
//                        }
//                    }
//
//                    double totalFte = 0;
//
//                    for (GetProjectResourceAllocationData getProjectData : entry.getValue()) {
//                        GetResourceAllProjectData eachProjectData = new GetResourceAllProjectData();
//                        eachProjectData.setMapId(getProjectData.getMap_id());
//                        eachProjectData.setProjectName(getProjectData.getProject_name());
//                        eachProjectData.setAllocationStartDate(getProjectData.getAllocation_start_date());
//                        eachProjectData.setAllocationEndDate(getProjectData.getAllocation_end_date());
////                        eachProjectData.set(getProjectData.getFte());
//                        //Period dateDiff = Period.between(getProjectData.getAllocation_start_date(), getProjectData.getAllocation_end_date());
//                        //Long noOfDays = DAYS.between(getProjectData.getAllocation_start_date(), getProjectData.getAllocation_end_date());
//                        long diff = 1;
//                        try {
////                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
////                            LocalDate startDate = LocalDate.parse(String.valueOf(getProjectData.getAllocation_start_date()), dtf);
////                            LocalDate endDate = LocalDate.parse(String.valueOf(getProjectData.getAllocation_end_date()), dtf);
//                            LocalDate startDate, endDate;
//                            if (getProjectData.getAllocation_start_date().compareTo(getDateRange.getStartDate()) >= 0) {
//                                startDate = new java.sql.Date(getProjectData.getAllocation_start_date().getTime()).toLocalDate();
//                            } else {
//                                startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                            }
//
//                            if (getProjectData.getAllocation_end_date().compareTo(getDateRange.getEndDate()) >= 0) {
//                                endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                            } else {
//                                endDate = new java.sql.Date(getProjectData.getAllocation_end_date().getTime()).toLocalDate();
//                            }
//
//                            diff += ChronoUnit.DAYS.between(startDate, endDate);
////                            long date_diff = getProjectData.getAllocation_start_date().getTime() - getProjectData.getAllocation_end_date().getTime();
////                            //diff = (date_diff/(1000*60*60*24)) % 365;
////                            Period period = Period.between(getProjectData.getAllocation_start_date(), getProjectData.getAllocation_end_date());
////                            diff = period.getDays();
////                            System.out.println(ChronoUnit.DAYS.between(getProjectData.getAllocation_start_date().toInstant(), getProjectData.getAllocation_end_date().toInstant()));
////                            diff = ChronoUnit.DAYS.between(getProjectData.getAllocation_start_date().toInstant(), getProjectData.getAllocation_end_date().toInstant());
////                            System.out.println("diff--->" + diff);
//                        } catch (NullArgumentException e) {
//                            System.out.println("error---> " + e);
//                        }
////                        System.out.println("TILL HERE WORKING");
////                        if(Objects.equals(getProjectData.getAllocation_end_date().,getProjectData.getAllocation_start_date())){
////                            diff = 1;
////                        }
////                        else{
////                            diff = getProjectData.getAllocation_end_date().getTime() - getProjectData.getAllocation_start_date().getTime();
////                        }
////                        totalFte += (getProjectData.getFte().doubleValue() * diff);
//                        allProjectDataList.add(eachProjectData);
//                    }
//
//                    long getDateRangeDiff = 1;
//                    try {
////                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
////
////                        System.out.println("string date" + String.valueOf(getDateRange.getStartDate()));
////                        LocalDate startDate = LocalDate.parse(String.valueOf(getDateRange.getStartDate()), dtf);
////                        LocalDate endDate = LocalDate.parse(String.valueOf(getDateRange.getEndDate()), dtf);
//                        LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                        LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                        getDateRangeDiff += ChronoUnit.DAYS.between(startDate, endDate);
//                    } catch (NullArgumentException e) {
//                        System.out.println("error---> " + e);
//                    }
//                    getData.setAverageFte(ResourceUtils.roundToTwoDecimalPlace(totalFte / getDateRangeDiff));
//                }
//                getData.setAllocationData(allProjectDataList);
//                getResourceAnalyticsMasterList.add(getData);
//            }
//
//        }
//
//        if(getResourceAnalyticsMasterList.isEmpty()){
//            getResourceAnalyticsMasterList=null;
//        }
//
//        return getResourceAnalyticsMasterList;
//    }

    public List<GetFinancialResourceDetails> getInHouseResourceDetails(Integer financialYear) {

        String getInHouseResourceDetailsQuery = ResourceUtils.getFile("getFinancialResourceDetailsQuery.txt");

        Query query = entityManager.createNativeQuery(getInHouseResourceDetailsQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetInHouseResourceDetails.class));

        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month month = Month.valueOf(startMonth.toUpperCase());
        LocalDate customStartDate = LocalDate.of(financialYear, month, startDateOfMonth);
        LocalDate customEndDate = customStartDate.plusYears(1).minusDays(1);

        Date startDate = java.sql.Date.valueOf(customStartDate);
        Date endDate = java.sql.Date.valueOf(customEndDate);

        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("employmentType", ResourceUtils.FIELD_RESOURCE_EMPLOYMENT_TYPE_IN_HOUSE);

        List<GetInHouseResourceDetails> getDetails = query.getResultList();

        if(getDetails==null || getDetails.isEmpty()){
            return null;
        }

        List<FinanceMgmtFieldMap> financeMgmtFieldIds = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName(ResourceUtils.FINANCIAL_DIRECT_FIELD_NAMES, ResourceUtils.DIRECT_EMPLOYEE_DETAILS);

        Map<String, Integer> financeFieldNameFieldId = financeMgmtFieldIds.stream().collect(Collectors.toMap(FinanceMgmtFieldMap::getFields, FinanceMgmtFieldMap::getFieldId));

        List<GetFinancialResourceDetails> allFinancialResourceData = new ArrayList<>();

        for(GetInHouseResourceDetails eachResource: getDetails){
            GetFinancialResourceDetails financialResourceDataComplete = new GetFinancialResourceDetails();
            financialResourceDataComplete.setEmployeeId(eachResource.getResource_id());
            financialResourceDataComplete.setFirstName(eachResource.getFirst_name());
            financialResourceDataComplete.setLastName(eachResource.getLast_name());

            List<FinancialResourceData> financialResourceData = new ArrayList<>();

            for(FinanceMgmtFieldMap eachField: financeMgmtFieldIds){

                FinancialResourceData eachFieldData = new FinancialResourceData();

                eachFieldData.setFieldName(eachField.getFields());
                eachFieldData.setFieldType(eachField.getFieldType());
                eachFieldData.setFieldId(financeFieldNameFieldId.get(eachField.getFields()));

                switch(eachField.getFields()){
                    case ResourceUtils.FINANCE_EMPLOYEE_ID:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getResource_id()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_FIRST_NAME:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getFirst_name()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_LAST_NAME:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getLast_name()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_START_DATE:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getDate_of_join()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_END_DATE:
                        if(eachResource.getLast_working_date()==null){
                            eachFieldData.setFieldValue(null);
                        }
                        else{
                            eachFieldData.setFieldValue(String.valueOf(eachResource.getLast_working_date()));
                        }
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_WORK_LOCATION:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getEmp_location()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_DESIGNATION:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getRole()));
                        eachFieldData.setPicklistId(String.valueOf(eachResource.getRole_id()));
                        break;
                    case ResourceUtils.FINANCE_SALARY:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getSalary()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_LOCATION:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getLocation()));
                        eachFieldData.setPicklistId(String.valueOf(eachResource.getLocation_id()));
                        break;
                }

                financialResourceData.add(eachFieldData);
            }
            financialResourceDataComplete.setData(financialResourceData);
            allFinancialResourceData.add(financialResourceDataComplete);
        }

        return allFinancialResourceData;
    }

    public List<GetFinancialResourceDetails> getVendorResourceDetails(Integer financialYear) {

        String getInHouseResourceDetailsQuery = ResourceUtils.getFile("getFinancialResourceDetailsQuery.txt");

        Query query = entityManager.createNativeQuery(getInHouseResourceDetailsQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetVendorResourceDetails.class));

        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month month = Month.valueOf(startMonth.toUpperCase());
        LocalDate customStartDate = LocalDate.of(financialYear, month, startDateOfMonth);
        LocalDate customEndDate = customStartDate.plusYears(1).minusDays(1);

        Date startDate = java.sql.Date.valueOf(customStartDate);
        Date endDate = java.sql.Date.valueOf(customEndDate);

        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("employmentType", ResourceUtils.FIELD_RESOURCE_EMPLOYMENT_TYPE_VENDOR);

        List<GetVendorResourceDetails> getDetails = query.getResultList();

        if(getDetails==null || getDetails.isEmpty()){
            return null;
        }

        List<FinanceMgmtFieldMap> financeMgmtFieldIds = financeMgmtFieldMapRepo.getFinanceMgmtFieldIdByFieldName(ResourceUtils.FINANCIAL_VENDOR_FIELD_NAMES, ResourceUtils.VENDOR_EMPLOYEE_DETAILS);

        Map<String, Integer> financeFieldNameFieldId = financeMgmtFieldIds.stream().collect(Collectors.toMap(FinanceMgmtFieldMap::getFields, FinanceMgmtFieldMap::getFieldId));

        List<GetFinancialResourceDetails> allFinancialResourceData = new ArrayList<>();

        for(GetVendorResourceDetails eachResource: getDetails){
            GetFinancialResourceDetails financialResourceDataComplete = new GetFinancialResourceDetails();
            financialResourceDataComplete.setEmployeeId(eachResource.getResource_id());
            financialResourceDataComplete.setFirstName(eachResource.getFirst_name());
            financialResourceDataComplete.setLastName(eachResource.getLast_name());

            List<FinancialResourceData> financialResourceData = new ArrayList<>();

            for(FinanceMgmtFieldMap eachField: financeMgmtFieldIds){

                FinancialResourceData eachFieldData = new FinancialResourceData();

                eachFieldData.setFieldName(eachField.getFields());
                eachFieldData.setFieldType(eachField.getFieldType());
                eachFieldData.setFieldId(financeFieldNameFieldId.get(eachField.getFields()));

                switch(eachField.getFields()){
                    case ResourceUtils.FINANCE_EMPLOYEE_ID:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getResource_id()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_FIRST_NAME:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getFirst_name()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_LAST_NAME:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getLast_name()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_START_DATE:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getDate_of_join()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_END_DATE:
                        if(eachResource.getLast_working_date()==null){
                            eachFieldData.setFieldValue(null);
                        }
                        else{
                            eachFieldData.setFieldValue(String.valueOf(eachResource.getLast_working_date()));
                        }
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_WORK_LOCATION:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getEmp_location()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_DESIGNATION:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getRole()));
                        eachFieldData.setPicklistId(String.valueOf(eachResource.getRole_id()));
                        break;
                    case ResourceUtils.FINANCE_SALARY:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getSalary()));
                        eachFieldData.setPicklistId("");
                        break;
                    case ResourceUtils.FINANCE_LOCATION:
                        eachFieldData.setFieldValue(String.valueOf(eachResource.getLocation()));
                        eachFieldData.setPicklistId(String.valueOf(eachResource.getLocation_id()));
                        break;
                }

                financialResourceData.add(eachFieldData);
            }
            financialResourceDataComplete.setData(financialResourceData);
            allFinancialResourceData.add(financialResourceDataComplete);
        }

        return allFinancialResourceData;
    }

    private List<GetDateRange> createDateRangeList(GetDateRange getDateRange){

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
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

//    public ResourceMonthlyFteData getMaxPossibleFteByResourceId(Integer resourceId, GetDateRange getDateRange) {
//
//        ResourceMonthlyFteData resourceMaxPossibleFteData = new ResourceMonthlyFteData();
//
//        double maxPossibleFte = ResourceUtils.MIN_FTE;
//
//        List<GetDateRange> dateRange = createDateRangeList(getDateRange);
//
//        List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();
//
//        double maxFteByAdmin = ResourceUtils.MIN_FTE;
//
//        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
//            maxFteByAdmin = (maxFteByAdmin < eachStatus.getEndValue())?eachStatus.getEndValue():maxFteByAdmin;
//        }
//
////        check for total fte assignment in each month
//        String resourceMonthlyFteQuery = ResourceUtils.getFile("getMonthlyFteByResourceId.txt");
//        Query query1 = entityManager.createNativeQuery(resourceMonthlyFteQuery).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ResourceMonthlyFteData.class));
//
//        query1.setParameter("resourceId", resourceId);
//
//        for(GetDateRange monthRange: dateRange){
//            query1.setParameter("startDate", monthRange.getStartDate());
//            query1.setParameter("endDate", monthRange.getEndDate());
//
//            List<ResourceMonthlyFteData> resourceMonthlyFteList = query1.getResultList();
//
//            resourceMaxPossibleFteData.setResource_name(resourceMonthlyFteList.get(0).getResource_name());
//            resourceMaxPossibleFteData.setResource_id(resourceMonthlyFteList.get(0).getResource_id());
//
//            Date fteCalculateStartDate = monthRange.getStartDate().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():monthRange.getStartDate();
//            Date fteCalculateEndDate = monthRange.getEndDate().compareTo(getDateRange.getEndDate())<0?monthRange.getEndDate():getDateRange.getEndDate();
//
//            LocalDate date1, date2;
//            date1 = new java.sql.Date(monthRange.getStartDate().getTime()).toLocalDate();
//            date2 = new java.sql.Date(monthRange.getEndDate().getTime()).toLocalDate();
//            long monthDateDiff = ChronoUnit.DAYS.between(date1, date2) + 1;
//
//            date1 = new java.sql.Date(fteCalculateStartDate.getTime()).toLocalDate();
//            date2 = new java.sql.Date(fteCalculateEndDate.getTime()).toLocalDate();
//            long assignedDateDiff = ChronoUnit.DAYS.between(date1, date2) + 1;
//
//            double dataFromQuery = resourceMonthlyFteList.get(0).getFte().doubleValue();
//
//            double maxPossibleAllotment = ResourceUtils.roundToTwoDecimalPlace((monthDateDiff-dataFromQuery)/assignedDateDiff);
//
//            double validMaxPossibleAllotment = Math.max(ResourceUtils.MIN_FTE, maxPossibleAllotment);
//
//            maxPossibleFte = Math.min(validMaxPossibleAllotment, maxFteByAdmin);
//        }
//
//        resourceMaxPossibleFteData.setFte(BigDecimal.valueOf(maxPossibleFte));
//
//        return resourceMaxPossibleFteData;
//    }

    public ResourceMonthlyFteData getMaxPossibleFteByResourceId(Integer resourceId, GetDateRange getDateRange, Integer mapId) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        ResourceMonthlyFteData resourceMaxPossibleFteData = new ResourceMonthlyFteData();

        List<GetDateRange> dateRange = createDateRangeList(getDateRange);

        List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();

        double maxFteByAdmin = ResourceUtils.MIN_FTE;

        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
            if(eachStatus.getStartValue() !=null && eachStatus.getEndValue()!=null){
                maxFteByAdmin = (maxFteByAdmin < eachStatus.getEndValue())?eachStatus.getEndValue():maxFteByAdmin;
            }
        }

        double maxPossibleFte = maxFteByAdmin;

//        check for total fte assignment in each month
        String resourceMonthlyFteQuery = ResourceUtils.getFile("getResourceMonthlyAllocationById.txt");
        Query query1 = entityManager.createNativeQuery(resourceMonthlyFteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllocationData.class));

        query1.setParameter("resourceId", resourceId);
        query1.setParameter("mapId", mapId);

        for(GetDateRange monthRange: dateRange){
            query1.setParameter("startDate", monthRange.getStartDate());
            query1.setParameter("endDate", monthRange.getEndDate());

            List<ResourceAllocationData> resourceMonthlyFteList = query1.getResultList();

            Date fteCalculateStartDate = monthRange.getStartDate().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():monthRange.getStartDate();
            Date fteCalculateEndDate = monthRange.getEndDate().compareTo(getDateRange.getEndDate())<0?monthRange.getEndDate():getDateRange.getEndDate();

            LocalDate fteCalculateStartDateLocal, fteCalculateEndDateLocal, monthStartDate, monthEndDate;

//            in case the response date range starts from the middle of the month
            fteCalculateStartDateLocal = new java.sql.Date(fteCalculateStartDate.getTime()).toLocalDate();
            fteCalculateEndDateLocal = new java.sql.Date(fteCalculateEndDate.getTime()).toLocalDate();

            long monthDateDiff = ChronoUnit.DAYS.between(fteCalculateStartDateLocal, fteCalculateEndDateLocal) + 1;

//            month start and end date to calculate number of days in the month
            monthStartDate = new java.sql.Date(monthRange.getStartDate().getTime()).toLocalDate();
            monthEndDate = new java.sql.Date(monthRange.getEndDate().getTime()).toLocalDate();
            long numberOfDaysInMonth = ChronoUnit.DAYS.between(monthStartDate, monthEndDate) + 1;


            Map<LocalDate, Double> dateMap = new HashMap<>();

            double maxAllottedInADay = 0d;
            double totalMonthlyFteWorked = 0d;

            LocalDate currentDate, endDate;

            if(resourceMonthlyFteList!=null && !resourceMonthlyFteList.isEmpty()) {
                for (ResourceAllocationData eachRecord : resourceMonthlyFteList) {

                    currentDate = new java.sql.Date(eachRecord.getStart_date().getTime()).toLocalDate();
                    endDate = new java.sql.Date(eachRecord.getEnd_date().getTime()).toLocalDate();
                    while (!currentDate.isAfter(endDate)) {
                        dateMap.put(currentDate, dateMap.getOrDefault(currentDate, 0d) + eachRecord.getFte().doubleValue());
                        if((currentDate.isAfter(fteCalculateStartDateLocal) || currentDate.isEqual(fteCalculateStartDateLocal))
                                && (currentDate.isBefore(fteCalculateEndDateLocal) || currentDate.isEqual(fteCalculateEndDateLocal))) {
                            maxAllottedInADay = maxAllottedInADay < dateMap.get(currentDate) ? dateMap.get(currentDate) : maxAllottedInADay;
                        }
                        currentDate = currentDate.plusDays(1);
                    }
                    totalMonthlyFteWorked = totalMonthlyFteWorked + eachRecord.getFte_worked().doubleValue();
                }
            }

            double totalMonthlyFteLeft = numberOfDaysInMonth - totalMonthlyFteWorked;
            totalMonthlyFteLeft = ResourceUtils.roundToTwoDecimalPlace(totalMonthlyFteLeft/monthDateDiff);

            double maxPossibleAllotmentDayWise = ResourceUtils.roundToTwoDecimalPlace(maxFteByAdmin-maxAllottedInADay);
            double validMaxPossibleAllotmentForMonth = Math.max(ResourceUtils.MIN_FTE, maxPossibleAllotmentDayWise);
            validMaxPossibleAllotmentForMonth = Math.min(validMaxPossibleAllotmentForMonth, Math.min(maxFteByAdmin,totalMonthlyFteLeft));

            maxPossibleFte = Math.min(maxPossibleFte, validMaxPossibleAllotmentForMonth);

            if(resourceMonthlyFteList!=null && !resourceMonthlyFteList.isEmpty()){
                resourceMaxPossibleFteData.setResource_name(resourceMonthlyFteList.get(0).getResource_name());
                resourceMaxPossibleFteData.setResource_id(resourceMonthlyFteList.get(0).getResource_id());
            }
            else{
                String resourceName = resourceMgmtRepo.getReportingManagerById(resourceId);
                resourceMaxPossibleFteData.setResource_name(resourceName);
                resourceMaxPossibleFteData.setResource_id(resourceId);
            }
        }

        resourceMaxPossibleFteData.setFte(BigDecimal.valueOf(maxPossibleFte));

        return resourceMaxPossibleFteData;
    }

//    later, once new resource allocation implementation is complete
/*
    public GetUtilizationGraph getUtilizationGraph(Integer resourceId){

        GetUtilizationGraph getUtilizationGraph = new GetUtilizationGraph();

        Date currentDate = new Date();

        Date nextPreviousMonth, nextPreviousTwelveMonth;

        Calendar calendar = Calendar.getInstance();

//        create start end date of next twelve months
        calendar.setTime(currentDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        nextPreviousMonth = calendar.getTime();
        calendar.add(Calendar.MONTH,11);
        nextPreviousTwelveMonth = calendar.getTime();
        List<GetDateRange> nextTwelveMonthDateRange = createDateRangeList(new GetDateRange(nextPreviousMonth, nextPreviousTwelveMonth));


//        create start end date of previous twelve months
        calendar.setTime(currentDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH,-1);
        nextPreviousMonth = calendar.getTime();
        calendar.add(Calendar.MONTH,-11);
        nextPreviousTwelveMonth = calendar.getTime();
        List<GetDateRange> previousTwelveMonthDateRange = createDateRangeList(new GetDateRange(nextPreviousTwelveMonth, nextPreviousMonth));

        String resourceMonthlyFteQuery = ResourceUtils.getFile("getUtilizationLevelDataByResourceId.txt");
        Query query1 = entityManager.createNativeQuery(resourceMonthlyFteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(GetResourceFteDetails.class));

        query1.setParameter("resourceId", resourceId);

        List<MonthlyFteData> nextTwelveMonth = new ArrayList<>();
        List<MonthlyFteData> pastTwelveMonth = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");

        LocalDate startDate, endDate;
        Integer workingDays;
        Double resourceAvgFte;

        assert nextTwelveMonthDateRange != null;
        for(GetDateRange monthRange: nextTwelveMonthDateRange){
            query1.setParameter("startDate", monthRange.getStartDate());
            query1.setParameter("endDate", monthRange.getEndDate());

            List<GetResourceFteDetails> resourceMonthlyFteList = query1.getResultList();
            MonthlyFteData currentMonthData = new MonthlyFteData();

            currentMonthData.setMonth(dateFormat.format(monthRange.getStartDate()));
            calendar.setTime(monthRange.getStartDate());
            currentMonthData.setYear(calendar.get(Calendar.YEAR));

            if(resourceMonthlyFteList!=null && !resourceMonthlyFteList.isEmpty()){

                startDate = new java.sql.Date(monthRange.getStartDate().getTime()).toLocalDate();
                endDate = new java.sql.Date(monthRange.getEndDate().getTime()).toLocalDate();

                workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

                resourceAvgFte = ResourceUtils.roundToTwoDecimalPlace(resourceMonthlyFteList.get(0).getSum_fte().doubleValue()/workingDays);

                currentMonthData.setMonthlyFte(resourceAvgFte);
            }
            else{
                currentMonthData.setMonthlyFte(0d);
            }
            nextTwelveMonth.add(currentMonthData);
        }

        assert previousTwelveMonthDateRange != null;
        for(GetDateRange monthRange: previousTwelveMonthDateRange){
            query1.setParameter("startDate", monthRange.getStartDate());
            query1.setParameter("endDate", monthRange.getEndDate());

            List<GetResourceFteDetails> resourceMonthlyFteList = query1.getResultList();
            MonthlyFteData currentMonthData = new MonthlyFteData();

            currentMonthData.setMonth(dateFormat.format(monthRange.getStartDate()));
            calendar.setTime(monthRange.getStartDate());
            currentMonthData.setYear(calendar.get(Calendar.YEAR));

            if(resourceMonthlyFteList!=null && !resourceMonthlyFteList.isEmpty()){

                startDate = new java.sql.Date(monthRange.getStartDate().getTime()).toLocalDate();
                endDate = new java.sql.Date(monthRange.getEndDate().getTime()).toLocalDate();

                workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

                resourceAvgFte = ResourceUtils.roundToTwoDecimalPlace(resourceMonthlyFteList.get(0).getSum_fte().doubleValue()/workingDays);

                currentMonthData.setMonthlyFte(resourceAvgFte);
            }
            else{
                currentMonthData.setMonthlyFte(0d);
            }
            pastTwelveMonth.add(currentMonthData);
        }

        getUtilizationGraph.setNextTwelveMonth(nextTwelveMonth);
        getUtilizationGraph.setPastTwelveMonth(pastTwelveMonth);
        return getUtilizationGraph;
    }
*/

    public GetUtilizationGraph getUtilizationGraph(Integer resourceId){

        GetUtilizationGraph getUtilizationGraph = new GetUtilizationGraph();

        Date date = new Date();

        LocalDate currentMonthDate = new java.sql.Date(date.getTime()).toLocalDate();
        currentMonthDate = currentMonthDate.withDayOfMonth(1);

        LocalDate twelveMonthBeforeStartDate = currentMonthDate;
        twelveMonthBeforeStartDate = twelveMonthBeforeStartDate.minusMonths(12);

        LocalDate twelveMonthAfterEndDate = currentMonthDate;
        twelveMonthAfterEndDate = twelveMonthAfterEndDate.plusMonths(11);
        twelveMonthAfterEndDate = twelveMonthAfterEndDate.withDayOfMonth(twelveMonthAfterEndDate.lengthOfMonth());

        Date allocationStartDate = java.sql.Date.valueOf(twelveMonthBeforeStartDate);
        Date allocationEndDate = java.sql.Date.valueOf(twelveMonthAfterEndDate);

        String resourceMonthlyFteQuery = ResourceUtils.getFile("getUtilizationGraphDataByResourceId.txt");
        Query query = entityManager.createNativeQuery(resourceMonthlyFteQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllocationDataByDate.class));

        query.setParameter("resourceId", resourceId);
        query.setParameter("allocationStartDate", allocationStartDate);
        query.setParameter("allocationEndDate", allocationEndDate);

        List<MonthlyFteData> nextTwelveMonth = new ArrayList<>();
        List<MonthlyFteData> pastTwelveMonth = new ArrayList<>();

        List<ResourceAllocationDataByDate> resourceAllocationDataByDates = query.getResultList();
        Map<Date, Double> resourceAllocationDataByDatesMap = new HashMap<>();
        if(resourceAllocationDataByDates!=null && !resourceAllocationDataByDates.isEmpty()) {
            for (ResourceAllocationDataByDate eachDate : resourceAllocationDataByDates) {
                resourceAllocationDataByDatesMap.put(eachDate.getAllocation_date(), eachDate.getSum_fte().doubleValue());
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");

        LocalDate monthEndDate = twelveMonthBeforeStartDate.withDayOfMonth(twelveMonthBeforeStartDate.lengthOfMonth());
        LocalDate currentDate = twelveMonthBeforeStartDate;
        Integer workingDays = 0;
        Double monthTotalFte = 0d;
        while(!currentDate.isAfter(twelveMonthAfterEndDate)){

            if(currentDate.isAfter(monthEndDate)){
                workingDays = 0;
                monthTotalFte = 0d;
                monthEndDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
            }

            if(currentDate.getDayOfWeek()!=DayOfWeek.SATURDAY && currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY) {
                Date currentDateDate = java.sql.Date.valueOf(currentDate);

                if (resourceAllocationDataByDatesMap.containsKey(currentDateDate)){
                    monthTotalFte += resourceAllocationDataByDatesMap.get(currentDateDate);
                }
                workingDays++;
            }

            if(currentDate.isEqual(monthEndDate)){
                Date currentDateDate = java.sql.Date.valueOf(currentDate);
                Double resourceMonthlyFteAverage = ResourceUtils.roundToTwoDecimalPlace(monthTotalFte/workingDays);

                MonthlyFteData currentMonth = new MonthlyFteData(dateFormat.format(currentDateDate), currentDate.getYear(), resourceMonthlyFteAverage);

                if(currentDate.isAfter(currentMonthDate)){
                    nextTwelveMonth.add(currentMonth);
                }
                else{
                    pastTwelveMonth.add(currentMonth);
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        getUtilizationGraph.setNextTwelveMonth(nextTwelveMonth);
        getUtilizationGraph.setPastTwelveMonth(pastTwelveMonth);
        return getUtilizationGraph;
    }



//    public List<GetResourceAllocationMatrix> getResourceAllocationMatrixNew(GetDateRange getDateRange, Integer departmentId) {
//
////        if end date is before start date
//        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
//            return null;
//        }
//
//        List<GetResourceAllocationMatrix> getResourceAnalyticsMasterList = new ArrayList<>();
//        String resourceProjectMapping = ResourceUtils.getFile("resourceAllProjectAllocation.txt");
//        if(departmentId!=null && departmentId!=0){
//            resourceProjectMapping = ResourceUtils.getFile("resourceAllProjectAllocationByDepartmentId.txt");
//        }
//
//        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetProjectResourceAllocationData.class));
//
//        query.setParameter("startDate", getDateRange.getStartDate());
//        query.setParameter("endDate", getDateRange.getEndDate());
//        if(departmentId!=null && departmentId!=0){
//            query.setParameter("departmentId", departmentId);
//        }
//
////        bring all the resource and the project allocation data
//        List<GetProjectResourceAllocationData> getResourceAnalyticsList = query.getResultList();
//
//        if (getResourceAnalyticsList != null && !getResourceAnalyticsList.isEmpty()) {
//
////            bring all the date wise allocation data for all non-deleted allocations
//            List<AllResourceActiveAllocation> allResourceActiveData = projectResourceMappingRepo.getAllResourceActiveAllocation(getDateRange.getStartDate(), getDateRange.getEndDate());
////            group date wise allocations by resource id
//            Map<Integer, List<AllResourceActiveAllocation>> allResourceActiveDataToMap = null;
//            if(allResourceActiveData!=null && !allResourceActiveData.isEmpty()) {
//                allResourceActiveDataToMap = allResourceActiveData.stream().collect(Collectors.groupingBy(AllResourceActiveAllocation::getMapId));
//            }
////            group total allocations by resource id
//            Map<Integer, List<GetProjectResourceAllocationData>> groupByResourceId = getResourceAnalyticsList.stream().collect(Collectors.groupingBy(GetProjectResourceAllocationData::getResource_id));
//
//            for (Map.Entry<Integer, List<GetProjectResourceAllocationData>> entry : groupByResourceId.entrySet()) {
//                GetResourceAllocationMatrix getData = new GetResourceAllocationMatrix();
//                List<GetResourceAllProjectData> allProjectDataList = new ArrayList<>();
//
//                if (!entry.getValue().isEmpty()) {
//
////                    assign resource details
//                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);
//
//                    LocalDate dateOfJoin = null, lastWorkingDate = null;
//
//                    for (Map.Entry<String, Object> getResourceData : objToMap.entrySet()) {
//                        switch (getResourceData.getKey()) {
//                            case "resourceId":
//                                getData.setResourceId(Integer.valueOf(String.valueOf(getResourceData.getValue())));
//                                break;
//                            case "resourceName":
//                                getData.setResourceName(String.valueOf(getResourceData.getValue()));
//                                break;
//                            case "role":
//                                if (getResourceData.getValue() != null) {
//                                    getData.setRole(String.valueOf(getResourceData.getValue()));
//                                } else {
//                                    getData.setRole("");
//                                }
//                                break;
//                            case "empLocation":
//                                if (getResourceData.getValue() != null) {
//                                    getData.setEmpLocation(String.valueOf(getResourceData.getValue()));
//                                } else {
//                                    getData.setEmpLocation("");
//                                }
//                                break;
//                            case "employmentType":
//                                if (getResourceData.getValue() != null) {
//                                    getData.setEmpType(String.valueOf(getResourceData.getValue()));
//                                } else {
//                                    getData.setEmpType("");
//                                }
//                                break;
//                            case "dateOfJoin":
//                                if (getResourceData.getValue() != null) {
//                                    try{
//                                        dateOfJoin = LocalDate.parse(String.valueOf(getResourceData.getValue()));
//                                    }
//                                    catch(Exception e){
//                                        dateOfJoin = null;
//                                    }
//                                } else {
//                                    dateOfJoin = null;
//                                }
//                                break;
//                            case "lastWorkingDate":
//                                if (getResourceData.getValue() != null) {
//                                    try{
//                                        lastWorkingDate = LocalDate.parse(String.valueOf(getResourceData.getValue()));
//                                    }
//                                    catch(Exception e){
//                                        lastWorkingDate = null;
//                                    }
//                                } else {
//                                    lastWorkingDate = null;
//                                }
//                                break;
//                        }
//                    }
//
//                    LocalDate responseStartDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                    LocalDate responseEndDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                    responseStartDate = responseStartDate.withDayOfMonth(1);
//                    responseEndDate = responseEndDate.withDayOfMonth(responseEndDate.lengthOfMonth());
//                    if(dateOfJoin==null || responseStartDate.isAfter(dateOfJoin)){
//                        dateOfJoin = responseStartDate;
//                    }
//                    if(lastWorkingDate==null || lastWorkingDate.isAfter(responseEndDate)){
//                        lastWorkingDate = responseEndDate;
//                    }
//
//                    List<MonthAllocationLimit> monthlyLimit = new ArrayList<>();
//
//                    Month month = dateOfJoin.getMonth();
//                    Integer year = dateOfJoin.getYear();
//                    Integer workingDaysInTheMonth = 0;
//                    while(!dateOfJoin.isAfter(lastWorkingDate)){
//                        if(dateOfJoin.getMonth()!=month){
//                            monthlyLimit.add(new MonthAllocationLimit(month.toString(), year, null, null, null, workingDaysInTheMonth.doubleValue()));
//                            month = dateOfJoin.getMonth();
//                            year = dateOfJoin.getYear();
//                            workingDaysInTheMonth = 0;
//                        }
//                        if(dateOfJoin.getDayOfWeek()!=DayOfWeek.SATURDAY && dateOfJoin.getDayOfWeek()!=DayOfWeek.SUNDAY) {
//                            workingDaysInTheMonth++;
//                        }
//                        dateOfJoin = dateOfJoin.plusDays(1);
//                    }
//                    monthlyLimit.add(new MonthAllocationLimit(month.toString(), year, null, null, null, workingDaysInTheMonth.doubleValue()));
//                    getData.setMonthlyWorkingDays(monthlyLimit);
//
////                    a variable to calculate total fte allocation for this resource
//                    double totalFte = 0;
//                    if(entry.getValue().get(0).getMap_id()!=null) {
////                    for each allocation
//                        for (GetProjectResourceAllocationData getProjectData : entry.getValue()) {
//
//                            GetResourceAllProjectData eachProjectData = new GetResourceAllProjectData(getProjectData);
//
//
//                            List<AllResourceActiveAllocation> thisProjectAllocations = null;
//
////                        check if date wise data for this allocation exists, if yes, assign
//                            if (allResourceActiveDataToMap != null && !allResourceActiveDataToMap.isEmpty()) {
//                                thisProjectAllocations = allResourceActiveDataToMap.get(getProjectData.getMap_id());
//                            }
//
////                        if data exists, sort it by allocation date
//                            List<AllResourceActiveAllocation> insideDateRangeRecords = null;
////                        List<AllResourceActiveAllocationWeekly> insideDateRangeRecordsWeekly = null;
////                        List<AllResourceActiveAllocationMonthly> insideDateRangeRecordsMonthly = null;
//                            if (thisProjectAllocations != null && !thisProjectAllocations.isEmpty()) {
//
////                            sort records date wise
//                                thisProjectAllocations.sort(Comparator.comparing(AllResourceActiveAllocation::getAllocationDate));
//
////                            three lists for each view mode
//                                insideDateRangeRecords = new ArrayList<>();
////                            insideDateRangeRecordsWeekly = new ArrayList<>();
////                            insideDateRangeRecordsMonthly = new ArrayList<>();
//
////                            calculating weekly sum and monthly sum
////                            double weeklySum = 0d, monthlySum=0d;
//                                LocalDate currentDate;
////                            LocalDate currentWeekStartDate=null, currentMonthStartDate = null, weekStart=null, weekEnd=null, monthStart=null, monthEnd=null;
//
////                            traversing each record
//                                for (AllResourceActiveAllocation eachRecord : thisProjectAllocations) {
//                                    if ((eachRecord.getAllocationDate().compareTo(eachProjectData.getAllocationEndDate()) <= 0)
//                                            && (eachRecord.getAllocationDate().compareTo(eachProjectData.getAllocationStartDate()) >= 0)) {
//
//                                        currentDate = new java.sql.Date(eachRecord.getAllocationDate().getTime()).toLocalDate();
//
////                                    if(weekStart==null){
////                                        currentWeekStartDate = currentDate;
////                                        weekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
////                                        weekEnd = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
////                                    }
////                                    if(monthStart==null){
////                                        currentMonthStartDate = currentDate;
////                                        monthStart = currentDate.withDayOfMonth(1);
////                                        monthEnd = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
////                                    }
//
//                                        eachRecord.setAllocationDate(java.sql.Date.valueOf(currentDate));
//                                        insideDateRangeRecords.add(eachRecord);
//
////                                    weeklySum = weeklySum + eachRecord.getFte().doubleValue();
////                                    monthlySum = monthlySum + eachRecord.getFte().doubleValue();
////                                    if(currentDate.isEqual(weekEnd)){
////                                        AllResourceActiveAllocationWeekly weeklyRecord = new AllResourceActiveAllocationWeekly();
////                                        weeklyRecord.setWeekStartDate(java.sql.Date.valueOf(weekStart));
////                                        weeklyRecord.setWeekEndDate(java.sql.Date.valueOf(weekEnd));
////                                        weeklyRecord.setWeeklyAvgFte(weeklySum/5);
////                                        insideDateRangeRecordsWeekly.add(weeklyRecord);
////                                        weekStart = null;
////                                        weekEnd = null;
////                                        weeklySum = 0d;
////                                    }
//
////                                    if(currentDate.isEqual(monthEnd)){
////                                        AllResourceActiveAllocationMonthly monthlyRecord = new AllResourceActiveAllocationMonthly();
////                                        monthlyRecord.setMonthStartDate(java.sql.Date.valueOf(monthStart));
////                                        monthlyRecord.setMonthEndDate(java.sql.Date.valueOf(monthEnd));
////                                        //Integer workingDaysMonth
////                                        monthlyRecord.setMonthlyAvgFte(weeklySum/5);
////                                        insideDateRangeRecordsMonthly.add(monthlyRecord);
////                                        monthStart = null;
////                                        monthEnd  = null;
////                                        weeklySum = 0d;
////                                    }
//                                    }
//                                }
////                            insideDateRangeRecords.sort(Comparator.comparing(AllResourceActiveAllocation::getAllocationDate));
//                            }
//
//                            eachProjectData.setAllocations(insideDateRangeRecords);
//
////                        calculate working days in this allocation's date range
//                            Integer workingDays = 1;
//
//                            try {
//                                LocalDate startDate, endDate;
//                                if (getProjectData.getAllocation_start_date().compareTo(getDateRange.getStartDate()) >= 0) {
//                                    startDate = new java.sql.Date(getProjectData.getAllocation_start_date().getTime()).toLocalDate();
//                                } else {
//                                    startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                                }
//
//                                if (getProjectData.getAllocation_end_date().compareTo(getDateRange.getEndDate()) >= 0) {
//                                    endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                                } else {
//                                    endDate = new java.sql.Date(getProjectData.getAllocation_end_date().getTime()).toLocalDate();
//                                }
//
//                                workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);
//
//                            } catch (Exception e) {
//                                System.out.println("error---> ");
//                            }
//
//
////                        calculate avg fte for this allocation
//                            eachProjectData.setAvgFte(ResourceUtils.roundToTwoDecimalPlace(getProjectData.getSum_fte().doubleValue() / workingDays));
//
////                        get total fte allocation throughout the response date range
//                            totalFte += getProjectData.getSum_fte().doubleValue();
//                            allProjectDataList.add(eachProjectData);
//                        }
//
//                    }
//
////                    calculate the resource avg fte for working days in response date range
//                    long getDateRangeDiff = 1;
//                    try {
//                        LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                        LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                        getDateRangeDiff = ResourceUtils.numberOfWorkingDays(startDate, endDate);
//                    } catch (NullArgumentException e) {
//                        System.out.println("error---> " + e);
//                    }
//                    getData.setAverageFte(ResourceUtils.roundToTwoDecimalPlace(totalFte / getDateRangeDiff));
//                }
//                if(!allProjectDataList.isEmpty()) {
//                    allProjectDataList.sort(Comparator.comparing(GetResourceAllProjectData::getAllocationStartDate).thenComparing(GetResourceAllProjectData::getAllocationEndDate));
//                }
//                else{
//                    allProjectDataList = null;
//                }
//                getData.setAllocationData(allProjectDataList);
//                getResourceAnalyticsMasterList.add(getData);
//            }
//
//            getResourceAnalyticsMasterList.sort(Comparator.comparing(GetResourceAllocationMatrix::getAverageFte).thenComparing(GetResourceAllocationMatrix::getResourceName));
//        }
//
//        if(getResourceAnalyticsMasterList.isEmpty()){
//            getResourceAnalyticsMasterList=null;
//        }
//
//        return getResourceAnalyticsMasterList;
//    }

//    need 3 apis here
    public List<GetResourceAllocationMatrixForLevelOneMatrix> getAllResourceAllocationMatrixData(GetDateRange getDateRange, String email, String role) {

    //        if end date is before start date
        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);

        List<GetResourceAllocationMatrixForLevelOneMatrix> getResourceAnalyticsMasterList = new ArrayList<>();

        String resourceProjectMapping = ResourceUtils.getFile("resourceAllProjectAllocationForLevelOneMatrix.txt");

        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetProjectResourceAllocationDataForLevelOneMatrix.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("resourceIds", visibleResourceIds);

    //        bring all the resource and the project allocation data
        List<GetProjectResourceAllocationDataForLevelOneMatrix> getResourceAnalyticsList = query.getResultList();

        if (getResourceAnalyticsList != null && !getResourceAnalyticsList.isEmpty()) {

    //            group total allocations by resource id
            Map<Integer, List<GetProjectResourceAllocationDataForLevelOneMatrix>> groupByResourceId = getResourceAnalyticsList.stream().collect(Collectors.groupingBy(GetProjectResourceAllocationDataForLevelOneMatrix::getResource_id));

            for (Map.Entry<Integer, List<GetProjectResourceAllocationDataForLevelOneMatrix>> entry : groupByResourceId.entrySet()) {
                GetResourceAllocationMatrixForLevelOneMatrix getData = new GetResourceAllocationMatrixForLevelOneMatrix();
                List<GetResourceAllProjectData> allProjectDataList = new ArrayList<>();

                if (!entry.getValue().isEmpty()) {

                    LocalDate dateOfJoin = null, lastWorkingDate = null;

    //                    assign resource details
                    Map<String, Object> objToMap = objectMapper.convertValue(entry.getValue().get(0), Map.class);

    //                    LocalDate dateOfJoin = null, lastWorkingDate = null;

                    for (Map.Entry<String, Object> getResourceData : objToMap.entrySet()) {
                        switch (getResourceData.getKey()) {
                            case "resourceId":
                                getData.setResourceId(Integer.valueOf(String.valueOf(getResourceData.getValue())));
                                break;
                            case "resourceName":
                                getData.setResourceName(String.valueOf(getResourceData.getValue()));
                                break;
                            case "role":
                                if (getResourceData.getValue() != null) {
                                    getData.setRole(String.valueOf(getResourceData.getValue()));
                                } else {
                                    getData.setRole("");
                                }
                                break;
                            case "empLocation":
                                if (getResourceData.getValue() != null) {
                                    getData.setEmpLocation(String.valueOf(getResourceData.getValue()));
                                } else {
                                    getData.setEmpLocation("");
                                }
                                break;
                            case "employmentType":
                                if (getResourceData.getValue() != null) {
                                    getData.setEmpType(String.valueOf(getResourceData.getValue()));
                                } else {
                                    getData.setEmpType("");
                                }
                                break;
                            case "departmentId":
                                getData.setDepartmentId(Integer.valueOf(String.valueOf(getResourceData.getValue())));
                                break;
                            case "departmentName":
                                getData.setDepartmentName(String.valueOf(getResourceData.getValue()));
                                break;
                            case "dateOfJoin":
                                try{
                                    dateOfJoin = LocalDate.parse(String.valueOf(getResourceData.getValue()));
                                }
                                catch(Exception e){
                                    Instant instant = Instant.parse(String.valueOf(getResourceData.getValue()));
                                    dateOfJoin = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                                }
                                break;
                            case "lastWorkingDate":
                                if(getResourceData.getValue()!=null && !String.valueOf(getResourceData.getValue()).isEmpty() && !Objects.equals(String.valueOf(getResourceData.getValue()), "null")) {
                                    try {
                                        lastWorkingDate = LocalDate.parse(String.valueOf(getResourceData.getValue()));
                                    } catch (Exception e) {
                                        Instant instant = Instant.parse(String.valueOf(getResourceData.getValue()));
                                        lastWorkingDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                                    }
                                }
                                break;
                        }
                    }

    //                    a variable to calculate total fte allocation for this resource
                    double totalFte = 0;
                    if(entry.getValue().get(0).getMap_id()!=null) {
    //                    for each allocation
                        for (GetProjectResourceAllocationDataForLevelOneMatrix getProjectData : entry.getValue()) {

                            GetResourceAllProjectData eachProjectData = new GetResourceAllProjectData(getProjectData);

                            Integer workingDays = 1;
    //                            calculate working days in this allocation's date range

                            try {
                                LocalDate startDate, endDate;
//                                if (getProjectData.getAllocation_start_date().compareTo(getDateRange.getStartDate()) >= 0) {
                                    startDate = new java.sql.Date(getProjectData.getAllocation_start_date().getTime()).toLocalDate();
//                                } else {
//                                    startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                                }

//                                if (getProjectData.getAllocation_end_date().compareTo(getDateRange.getEndDate()) >= 0) {
//                                    endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                                } else {
                                    endDate = new java.sql.Date(getProjectData.getAllocation_end_date().getTime()).toLocalDate();
//                                }

                                workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

                            } catch (Exception e) {
                                System.out.println("error---> ");
                            }


    //                        calculate avg fte for this allocation
                            eachProjectData.setAvgFteForProject(ResourceUtils.roundToTwoDecimalPlace(getProjectData.getSum_fte().doubleValue() / workingDays));

    //                        get total fte allocation throughout the response date range
                            totalFte += getProjectData.getSum_fte().doubleValue();
                            allProjectDataList.add(eachProjectData);
                        }

                    }

    //                    calculate the resource avg fte for working days in response date range
                    long getDateRangeDiff = 1;
                    try {
                        LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
                        LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
                        if(startDate.isBefore(dateOfJoin)){
                            startDate = dateOfJoin;
                        }
                        if(lastWorkingDate!=null && endDate.isAfter(lastWorkingDate)){
                            endDate = lastWorkingDate;
                        }
                        getDateRangeDiff = ResourceUtils.numberOfWorkingDays(startDate, endDate);
                    } catch (NullArgumentException e) {
                        System.out.println("error---> " + e);
                    }
                    getData.setAverageFte(ResourceUtils.roundToTwoDecimalPlace(totalFte / getDateRangeDiff));
                }
                else{
                    allProjectDataList = null;
                }
                getData.setAllocationData(allProjectDataList);
                getResourceAnalyticsMasterList.add(getData);
            }
        }

        if(getResourceAnalyticsMasterList.isEmpty()){
            getResourceAnalyticsMasterList=null;
        }

        return getResourceAnalyticsMasterList;
    }

    public List<GetResourcesByDepartment> getResourcesByDepartment(GetDateRange getDateRange, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }
//        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
//            return null;
//        }
//
//        String resourcesByDepartment = ResourceUtils.getFile("getResourcesByDepartmentList.txt");
//
//        Query query = entityManager.createNativeQuery(resourcesByDepartment).unwrap(org.hibernate.query.Query.class);
//        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourcesByDepartment.class));
//
//        query.setParameter("startDate", getDateRange.getStartDate());
//        query.setParameter("endDate", getDateRange.getEndDate());
//
//        List<GetResourcesByDepartment> getResourcesByDepartmentList = query.getResultList();
//
//        if(getResourcesByDepartmentList==null || getResourcesByDepartmentList.isEmpty()){
//            getResourcesByDepartmentList = null;
//        }
//        return getResourcesByDepartmentList;
        List<GetResourcesByDepartment> getResourcesByDepartmentList = new ArrayList<>();

        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        List<ResourceMgmtStatus> resourceMgmtStatusList = new ArrayList<>();
        Double maxEnabledFteInAdmin = -1d;
        Integer maxEnabledFteInAdminId = null;
        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                resourceMgmtStatusList.add(eachStatus);
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                    maxEnabledFteInAdminId = eachStatus.getStatusId();
                }
            }
        }
//        if(!resourceMgmtStatusList.isEmpty()){
//            resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
//        }

        List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
        Map<Integer, String> isDepartmentActive
                = resourceMgmtDepartmentList.stream().collect(Collectors.toMap(ResourceMgmtDepartment::getDepartmentId, ResourceMgmtDepartment::getIsActive));

        List<GetResourceAllocationMatrixForLevelOneMatrix> getResourceAllAllocationData = getAllResourceAllocationMatrixData(getDateRange, email, role);
        Map<Integer, List<GetResourceAllocationMatrixForLevelOneMatrix>> groupByDepartment = new HashMap<>();

        if(getResourceAllAllocationData!=null && !getResourceAllAllocationData.isEmpty()) {
            groupByDepartment = getResourceAllAllocationData.stream().collect(Collectors.groupingBy(GetResourceAllocationMatrixForLevelOneMatrix::getDepartmentId));
        }

        boolean isUndefinedColumnNotNeeded = true;

        for(Map.Entry<Integer, List<GetResourceAllocationMatrixForLevelOneMatrix>> eachDepartment: groupByDepartment.entrySet()){
            if(isDepartmentActive.get(eachDepartment.getKey()).equals("1")) {
                Map<Integer, Integer> resourceCount = new HashMap<>();

                GetResourcesByDepartment getResourcesByDepartment = new GetResourcesByDepartment();
                getResourcesByDepartment.setDepartment_id(eachDepartment.getKey());
                getResourcesByDepartment.setDepartment_name(eachDepartment.getValue().get(0).getDepartmentName());
                getResourcesByDepartment.setResource_count(eachDepartment.getValue().size());
                if (!eachDepartment.getValue().isEmpty()) {

                    for (GetResourceAllocationMatrixForLevelOneMatrix eachResource : eachDepartment.getValue()) {

                        Double resourceFteAvg = eachResource.getAverageFte();

                        boolean ifAppropriateStatusDoesNotExist = true;

                        Integer statusId = null;

                        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                            if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                                if (resourceFteAvg >= eachStatus.getStartValue() && resourceFteAvg < eachStatus.getEndValue()) {
                                    statusId = eachStatus.getStatusId();
                                    ifAppropriateStatusDoesNotExist = false;
                                    break;
                                }
                            }
                        }
                        if (ifAppropriateStatusDoesNotExist) {
                            if (maxEnabledFteInAdminId != null && resourceFteAvg.equals(maxEnabledFteInAdmin)) {
                                statusId = maxEnabledFteInAdminId;
                            } else {
                                isUndefinedColumnNotNeeded = false;
                                statusId = ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID;
                            }
                        }
//                    resource utilization count
                        resourceCount.merge(statusId, 1, Integer::sum);
                    }

                    List<ResourceCountPerUtilizationLevel> resourceCountPerUtilizationLevels = new ArrayList<>();

                    for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                        if (resourceCount.containsKey(eachStatus.getStatusId())) {
                            ResourceCountPerUtilizationLevel eachLevel = new ResourceCountPerUtilizationLevel();
                            eachLevel.setStatusId(eachStatus.getStatusId());
                            eachLevel.setResourceCount(resourceCount.get(eachStatus.getStatusId()));
                            eachLevel.setStatusName(eachStatus.getStatus());
                            eachLevel.setStartValue(eachStatus.getStartValue());
                            eachLevel.setEndValue(eachStatus.getEndValue());
                            resourceCountPerUtilizationLevels.add(eachLevel);
                        } else {
                            ResourceCountPerUtilizationLevel eachLevel = new ResourceCountPerUtilizationLevel();
                            eachLevel.setStatusId(eachStatus.getStatusId());
                            eachLevel.setResourceCount(0);
                            eachLevel.setStatusName(eachStatus.getStatus());
                            eachLevel.setStartValue(eachStatus.getStartValue());
                            eachLevel.setEndValue(eachStatus.getEndValue());
                            resourceCountPerUtilizationLevels.add(eachLevel);
                        }
                    }
                    resourceCountPerUtilizationLevels.sort(Comparator.comparing(ResourceCountPerUtilizationLevel::getStartValue));

                    ResourceCountPerUtilizationLevel eachLevel = new ResourceCountPerUtilizationLevel();
                    eachLevel.setStatusId(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID);
                    eachLevel.setStatusName(ResourceUtils.UNDEFINED_UTILIZATION_NAME);
                    eachLevel.setStartValue(Double.MAX_VALUE);
                    eachLevel.setEndValue(Double.MAX_VALUE);
                    eachLevel.setResourceCount(resourceCount.getOrDefault(0, 0));
                    resourceCountPerUtilizationLevels.add(eachLevel);

                    getResourcesByDepartment.setResourceCountByUtilization(resourceCountPerUtilizationLevels);
                } else {
                    List<ResourceCountPerUtilizationLevel> resourceCountPerUtilizationLevels = new ArrayList<>();
                    for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                        ResourceCountPerUtilizationLevel eachLevel = new ResourceCountPerUtilizationLevel();
                        eachLevel.setStatusId(eachStatus.getStatusId());
                        eachLevel.setResourceCount(0);
                        eachLevel.setStatusName(eachStatus.getStatus());
                        eachLevel.setStartValue(eachStatus.getStartValue());
                        eachLevel.setEndValue(eachStatus.getEndValue());
                        resourceCountPerUtilizationLevels.add(eachLevel);
                    }
                    resourceCountPerUtilizationLevels.sort(Comparator.comparing(ResourceCountPerUtilizationLevel::getStartValue));
                    ResourceCountPerUtilizationLevel eachLevel = new ResourceCountPerUtilizationLevel();
                    eachLevel.setStatusId(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID);
                    eachLevel.setResourceCount(0);
                    eachLevel.setStatusName(ResourceUtils.UNDEFINED_UTILIZATION_NAME);
                    eachLevel.setStartValue(Double.MAX_VALUE);
                    eachLevel.setEndValue(Double.MAX_VALUE);
                    resourceCountPerUtilizationLevels.add(eachLevel);
                    getResourcesByDepartment.setResourceCountByUtilization(resourceCountPerUtilizationLevels);
                }
                getResourcesByDepartmentList.add(getResourcesByDepartment);
            }
        }

        if(!getResourcesByDepartmentList.isEmpty()){

            if(isUndefinedColumnNotNeeded) {

                List<GetResourcesByDepartment> getResourcesByDepartmentListUpdated = new ArrayList<>();

                for (GetResourcesByDepartment eachDepartment : getResourcesByDepartmentList) {
                    List<ResourceCountPerUtilizationLevel> utilizationLevel = eachDepartment.getResourceCountByUtilization();
                    utilizationLevel.remove(utilizationLevel.size()-1);
                    eachDepartment.setResourceCountByUtilization(utilizationLevel);
                    getResourcesByDepartmentListUpdated.add(eachDepartment);
                }
                getResourcesByDepartmentList = new ArrayList<>(getResourcesByDepartmentListUpdated);
            }

            getResourcesByDepartmentList.sort(Comparator.comparing(GetResourcesByDepartment::getDepartment_name, (s1, s2)->{
                String department1 = s1.toLowerCase();
                String department2 = s2.toLowerCase();
                return String.CASE_INSENSITIVE_ORDER.compare(department1, department2);
            }));
        }
        else{
            getResourcesByDepartmentList = null;
        }

        return getResourcesByDepartmentList;
    }

    public List<GetResourcesByProject> getResourcesByProject(GetDateRange getDateRange, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        /*if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        String resourcesByProject = ResourceUtils.getFile("getResourcesByProjectList.txt");

        Query query = entityManager.createNativeQuery(resourcesByProject).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourcesByProject.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());

        List<GetResourcesByProject> getResourcesByProjectList = query.getResultList();

        if(getResourcesByProjectList==null || getResourcesByProjectList.isEmpty()){
            getResourcesByProjectList = null;
        }
        return getResourcesByProjectList;*/

        List<GetResourcesByProject> getResourcesByProjectList = new ArrayList<>();

        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        List<ResourceMgmtStatus> resourceMgmtStatusList = new ArrayList<>();
        Double maxEnabledFteInAdmin = -1d;
        Integer maxEnabledFteInAdminId = null;

//        a template map with all valid status id and count zero for each project
        Map<Integer, Integer> resourceCountByStatus = new HashMap<>();
        Map<Integer, ResourceMgmtStatus> resourceMgmtStatusMap = new HashMap<>();
//        get all valid status from admin
        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                resourceMgmtStatusList.add(eachStatus);
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                    maxEnabledFteInAdminId = eachStatus.getStatusId();
                }
                resourceCountByStatus.put(eachStatus.getStatusId(), 0);
                resourceMgmtStatusMap.put(eachStatus.getStatusId(), eachStatus);
            }
        }
        resourceCountByStatus.put(0, 0);
        resourceMgmtStatusMap.put(0, new ResourceMgmtStatus(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID, ResourceUtils.UNDEFINED_UTILIZATION_NAME, ResourceUtils.UNDEFINED_UTILIZATION_COLOR,
                Double.MAX_VALUE, Double.MAX_VALUE, null, null, null, null, "1"));
//        if(!resourceMgmtStatusList.isEmpty()){
//            resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
//        }

        List<GetResourceAllocationMatrixForLevelOneMatrix> getResourceAllAllocationData = getAllResourceAllocationMatrixData(getDateRange, email, role);
        Map<Integer, Map<Integer, Integer>> resourceCountByProjectId = new HashMap<>();

        boolean isUndefinedColumnNotNeeded = true;

        Map<Integer, String> projectIdName = new HashMap<>();
        if(getResourceAllAllocationData!=null && !getResourceAllAllocationData.isEmpty()) {
            for (GetResourceAllocationMatrixForLevelOneMatrix eachResource : getResourceAllAllocationData) {
//            ignore all resource with no allocations
                if (eachResource.getAllocationData() != null && !eachResource.getAllocationData().isEmpty()) {
//                get the project allocation data
                    List<GetResourceAllProjectData> resourceAllProjectData = eachResource.getAllocationData();
                    for (GetResourceAllProjectData eachProjectAllocation : resourceAllProjectData) {

                        projectIdName.putIfAbsent(eachProjectAllocation.getProjectId(), eachProjectAllocation.getProjectName());

                        Double resourceFteAvg = eachProjectAllocation.getAvgFteForProject();

                        boolean ifAppropriateStatusDoesNotExist = true;

                        Integer statusId = 0;

//                    for every valid status
                        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                            if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                                if (resourceFteAvg >= eachStatus.getStartValue() && resourceFteAvg < eachStatus.getEndValue()) {
                                    statusId = eachStatus.getStatusId();
                                    ifAppropriateStatusDoesNotExist = false;
                                    break;
                                }
                            }
                        }
                        if (ifAppropriateStatusDoesNotExist) {
                            if (maxEnabledFteInAdminId != null && resourceFteAvg.equals(maxEnabledFteInAdmin)) {
                                statusId = maxEnabledFteInAdminId;
                            } else {
                                isUndefinedColumnNotNeeded = false;
                                statusId = ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID;
                            }
                        }

                        Map<Integer, Integer> resourceCountByStatusForEachProject = null;
//                    check if current project exists in the map?
                        if (resourceCountByProjectId.containsKey(eachProjectAllocation.getProjectId())) {
                            resourceCountByStatusForEachProject = resourceCountByProjectId.get(eachProjectAllocation.getProjectId());
                        } else {
                            resourceCountByStatusForEachProject = new HashMap<>(resourceCountByStatus);
                        }
                        resourceCountByStatusForEachProject.merge(statusId, 1, Integer::sum);
                        resourceCountByProjectId.put(eachProjectAllocation.getProjectId(), resourceCountByStatusForEachProject);
                    }
                }
            }
        }

//        for each project
        for(Map.Entry<Integer, Map<Integer, Integer>> eachProject: resourceCountByProjectId.entrySet()){
            GetResourcesByProject eachProjectData = new GetResourcesByProject();
            eachProjectData.setProject_id(eachProject.getKey());
            eachProjectData.setProject_name(projectIdName.get(eachProject.getKey()));

            List<ResourceCountPerUtilizationLevel> eachStatusList = new ArrayList<>();
            Integer resourceCount = 0;
//            for each resource status in the project
            for(Map.Entry<Integer, Integer> eachStatusResult: eachProject.getValue().entrySet()){
                ResourceCountPerUtilizationLevel eachStatus = new ResourceCountPerUtilizationLevel();

                ResourceMgmtStatus currentStatus = resourceMgmtStatusMap.get(eachStatusResult.getKey());

                eachStatus.setResourceCount(eachStatusResult.getValue());
                resourceCount+=eachStatus.getResourceCount();
                eachStatus.setStatusId(currentStatus.getStatusId());
                eachStatus.setStatusName(currentStatus.getStatus());
                eachStatus.setStartValue(currentStatus.getStartValue());
                eachStatus.setEndValue(currentStatus.getEndValue());
                eachStatusList.add(eachStatus);
            }
            eachStatusList.sort(Comparator.comparing(ResourceCountPerUtilizationLevel::getStartValue));
            eachProjectData.setResourceCountByUtilization(eachStatusList);
            eachProjectData.setResource_count(resourceCount);
            getResourcesByProjectList.add(eachProjectData);
        }

        if(getResourcesByProjectList.isEmpty()){
            getResourcesByProjectList = null;
        }
        else{

            if(isUndefinedColumnNotNeeded) {

                List<GetResourcesByProject> getResourcesByProjectListUpdated = new ArrayList<>();

                for (GetResourcesByProject eachProject : getResourcesByProjectList) {
                    List<ResourceCountPerUtilizationLevel> utilizationLevel = eachProject.getResourceCountByUtilization();
                    utilizationLevel.remove(utilizationLevel.size()-1);
                    eachProject.setResourceCountByUtilization(utilizationLevel);
                    getResourcesByProjectListUpdated.add(eachProject);
                }
                getResourcesByProjectList = new ArrayList<>(getResourcesByProjectListUpdated);
            }

            getResourcesByProjectList.sort(Comparator.comparing(GetResourcesByProject::getProject_name, (s1, s2)->{
                String project1 = s1.toLowerCase();
                String project2 = s2.toLowerCase();
                return String.CASE_INSENSITIVE_ORDER.compare(project1, project2);
            }));
        }

        return getResourcesByProjectList;
    }

    /*public List<GetResourcesByUtilization> getResourcesByUtilization(GetDateRange getDateRange) {
        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        String getUtilizationLevelQuery = ResourceUtils.getFile("getResourcesByUtilizationList.txt");

        Query query = entityManager.createNativeQuery(getUtilizationLevelQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetResourceFteDetails.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());

        List<GetResourceFteDetails> resourceFteDetails = query.getResultList();

        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        List<ResourceMgmtStatus> resourceMgmtStatusList = new ArrayList<>();
        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled() && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                resourceMgmtStatusList.add(eachStatus);
            }
        }
        if(!resourceMgmtStatusList.isEmpty()){
            resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
        }

        Map<Integer, Long> resourceCount = new HashMap<>();

        LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
        LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();

        Integer workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

        for (GetResourceFteDetails eachResource : resourceFteDetails) {

            Double resourceFteAvg;

            if((eachResource.getStart_date().compareTo(getDateRange.getStartDate())!=0) || (eachResource.getEnd_date().compareTo(getDateRange.getEndDate())!=0)){
                LocalDate localStartDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
                LocalDate localEndDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
                resourceFteAvg = eachResource.getSum_fte().doubleValue()/ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
            }
            else{
                resourceFteAvg = eachResource.getSum_fte().doubleValue()/workingDays;
            }

            boolean ifAppropriateStatusDoesNotExist = true;

            for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {

                if (eachStatus.getIsEnabled() && eachStatus.getStartValue() !=null && eachStatus.getEndValue()!=null){
                    if(resourceFteAvg >= eachStatus.getStartValue() && resourceFteAvg < eachStatus.getEndValue()) {
                        if (resourceCount.containsKey(eachStatus.getStatusId())) {
                            resourceCount.put(eachStatus.getStatusId(), resourceCount.get(eachStatus.getStatusId()) + 1);
                        } else {
                            resourceCount.put(eachStatus.getStatusId(), 1L);
                        }
                        ifAppropriateStatusDoesNotExist = false;
                        break;
                    }
                }
            }
            if (ifAppropriateStatusDoesNotExist) {
                Integer resourceStatusIfAbsent = 0;
                if (resourceCount.containsKey(resourceStatusIfAbsent)) {
                    resourceCount.put(resourceStatusIfAbsent, resourceCount.get(resourceStatusIfAbsent) + 1);
                } else {
                    resourceCount.put(resourceStatusIfAbsent, 1L);
                }
            }
        }

        List<GetResourcesByUtilization> getUtilizationLevelList = new ArrayList<>();

        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
            if(eachStatus.getIsEnabled() && eachStatus.getStartValue() !=null && eachStatus.getEndValue()!=null) {
                if (resourceCount.containsKey(eachStatus.getStatusId())) {
                    GetResourcesByUtilization eachLevel = new GetResourcesByUtilization();
                    eachLevel.setStatusId(eachStatus.getStatusId());
                    eachLevel.setResourceCount(resourceCount.get(eachStatus.getStatusId()));
                    eachLevel.setStatusName(eachStatus.getStatus());
                    eachLevel.setStartValue(eachStatus.getStartValue());
                    eachLevel.setEndValue(eachStatus.getEndValue());
                    getUtilizationLevelList.add(eachLevel);
                } else {
                    GetResourcesByUtilization eachLevel = new GetResourcesByUtilization();
                    eachLevel.setStatusId(eachStatus.getStatusId());
                    eachLevel.setResourceCount(0L);
                    eachLevel.setStatusName(eachStatus.getStatus());
                    eachLevel.setStartValue(eachStatus.getStartValue());
                    eachLevel.setEndValue(eachStatus.getEndValue());
                    getUtilizationLevelList.add(eachLevel);
                }
            }
        }
        if(resourceCount.containsKey(0)){
            GetResourcesByUtilization eachLevel = new GetResourcesByUtilization();
            eachLevel.setStatusId(0);
            eachLevel.setResourceCount(resourceCount.get(0));
            eachLevel.setStatusName(ResourceUtils.UNDEFINED_UTILIZATION_NAME);
            eachLevel.setStartValue(Double.MAX_VALUE);
            eachLevel.setEndValue(Double.MAX_VALUE);
            getUtilizationLevelList.add(eachLevel);
        }

        if(getUtilizationLevelList.isEmpty()){
            getUtilizationLevelList = null;
        }
        else{
            getUtilizationLevelList.sort(Comparator.comparing(GetResourcesByUtilization::getEndValue).reversed());
        }
        return getUtilizationLevelList;
    }*/

    public List<GetResourcesByUtilization> getResourcesByUtilization(GetDateRange getDateRange, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<GetResourcesByUtilization> getResourcesByUtilizationList = new ArrayList<>();

//        get all status from db
        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        List<ResourceMgmtStatus> resourceMgmtStatusList = new ArrayList<>();
        Double maxEnabledFteInAdmin = -1d;
        Integer maxEnabledFteInAdminId = null;
//        a template map for all the other maps
        Map<Integer, Integer> resourceCountByStatus = new HashMap<>();

//         a map for easier access to data
        Map<Integer, ResourceMgmtStatus> resourceMgmtStatusMap = new HashMap<>();

//        get all valid status from admin
        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                resourceMgmtStatusList.add(eachStatus);
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                    maxEnabledFteInAdminId = eachStatus.getStatusId();
                }
//                put status data to map for easier access
                resourceCountByStatus.put(eachStatus.getStatusId(), 0);
                resourceMgmtStatusMap.put(eachStatus.getStatusId(), eachStatus);
            }
        }

//        add undefined status to maps
        resourceCountByStatus.put(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID, 0);
        resourceMgmtStatusMap.put(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID, new ResourceMgmtStatus(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID,
                ResourceUtils.UNDEFINED_UTILIZATION_NAME, ResourceUtils.UNDEFINED_UTILIZATION_COLOR, Double.MAX_VALUE, Double.MAX_VALUE,
                null, null, null, null, "1"));

//        get all resource allocation data
        List<GetResourceAllocationMatrixForLevelOneMatrix> getResourceAllAllocationData = getAllResourceAllocationMatrixData(getDateRange, email, role);

        boolean isUndefinedColumnNotNeeded = true;

//        for resource utilization in each project count
        Map<Integer, Map<Integer, Integer>> resourceCountByUtilizationIdForEachProjectCount = new HashMap<>();

//        Add all the valid status into the map
        for(ResourceMgmtStatus eachStatus: resourceMgmtStatusList){
            resourceCountByUtilizationIdForEachProjectCount.put(eachStatus.getStatusId(), resourceCountByStatus);
        }
        resourceCountByUtilizationIdForEachProjectCount.put(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID, resourceCountByStatus);
//        for overall resource utilization count
        Map<Integer, Integer> resourceCountByUtilizationForEachResourceCount = new HashMap<>(resourceCountByStatus);

//        traverse for each resource
        if(getResourceAllAllocationData!=null && !getResourceAllAllocationData.isEmpty()) {
            for (GetResourceAllocationMatrixForLevelOneMatrix eachResource : getResourceAllAllocationData) {
//            ignore all resource with no allocations
                Double resourceFteAvg = eachResource.getAverageFte();

                boolean ifAppropriateStatusDoesNotExist = true;

                Integer statusId = null;

//            based on each resource avg fte, assign mail utilization status id
                for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                    if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                        if (resourceFteAvg >= eachStatus.getStartValue() && resourceFteAvg < eachStatus.getEndValue()) {
                            statusId = eachStatus.getStatusId();
                            ifAppropriateStatusDoesNotExist = false;
                            break;
                        }
                    }
                }
                if (ifAppropriateStatusDoesNotExist) {
                    if (maxEnabledFteInAdminId != null && resourceFteAvg.equals(maxEnabledFteInAdmin)) {
                        statusId = maxEnabledFteInAdminId;
                    } else {
                        statusId = ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID;
                    }
                }

//            resource utilization count
                resourceCountByUtilizationForEachResourceCount.merge(statusId, 1, Integer::sum);

//            created new because it was, which I believe referring to same address, so all the data was coming out to same
                Map<Integer, Integer> eachProjectUtilization = new HashMap<>(resourceCountByUtilizationIdForEachProjectCount.get(statusId));

//            for additional utilization status columns (refer to UI), we will assign data based on how is their
//            utilization ion each of the projects it was assigned to
                if (eachResource.getAllocationData() != null && !eachResource.getAllocationData().isEmpty()) {

                    for (GetResourceAllProjectData eachAllocationData : eachResource.getAllocationData()) {

//                    get avg fte of the project
                        Double projectAvgFte = eachAllocationData.getAvgFteForProject();
                        projectAvgFte = ResourceUtils.roundToTwoDecimalPlace(projectAvgFte);

                        boolean ifAppropriateStatusDoesNotExistForProjectAvgFte = true;

                        Integer statusIdForProjectAvgFte = null;

//                    assign status to the project wise utilization
                        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                            if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                                if (projectAvgFte >= eachStatus.getStartValue() && projectAvgFte < eachStatus.getEndValue()) {
                                    statusIdForProjectAvgFte = eachStatus.getStatusId();
                                    ifAppropriateStatusDoesNotExistForProjectAvgFte = false;
                                    break;
                                }
                            }
                        }
                        if (ifAppropriateStatusDoesNotExistForProjectAvgFte) {

                            if (maxEnabledFteInAdminId != null && projectAvgFte.equals(maxEnabledFteInAdmin)) {
                                statusIdForProjectAvgFte = maxEnabledFteInAdminId;
                            } else {
                                isUndefinedColumnNotNeeded = false;
                                statusIdForProjectAvgFte = ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID;
                            }
                        }
                        eachProjectUtilization.merge(statusIdForProjectAvgFte, 1, Integer::sum);
                    }
                }
//            System.out.println(eachProjectUtilization);
                resourceCountByUtilizationIdForEachProjectCount.put(statusId, eachProjectUtilization);
            }
        }

//        convert the map data collected from previous loop to required json format
        for(Map.Entry<Integer, Map<Integer, Integer>> eachStatus: resourceCountByUtilizationIdForEachProjectCount.entrySet()){
            GetResourcesByUtilization eachUtilizationData = new GetResourcesByUtilization();

//            for each utilization status in row
            Integer statusId = eachStatus.getKey();
            eachUtilizationData.setStatusId(statusId);
            eachUtilizationData.setStatusName(resourceMgmtStatusMap.get(statusId).getStatus());
            eachUtilizationData.setStartValue(resourceMgmtStatusMap.get(statusId).getStartValue());
            eachUtilizationData.setEndValue(resourceMgmtStatusMap.get(statusId).getEndValue());

            List<ResourceCountPerUtilizationLevel> eachStatusList = new ArrayList<>();
//            for each resource status in the project (column utilization level)
            for(Map.Entry<Integer, Integer> eachStatusResult: eachStatus.getValue().entrySet()){
                ResourceCountPerUtilizationLevel eachStatusForProject = new ResourceCountPerUtilizationLevel();

                ResourceMgmtStatus currentStatus = resourceMgmtStatusMap.get(eachStatusResult.getKey());

                eachStatusForProject.setResourceCount(eachStatusResult.getValue());
                eachStatusForProject.setStatusId(currentStatus.getStatusId());
                eachStatusForProject.setStatusName(currentStatus.getStatus());
                eachStatusForProject.setStartValue(currentStatus.getStartValue());
                eachStatusForProject.setEndValue(currentStatus.getEndValue());
                eachStatusList.add(eachStatusForProject);
            }
            eachStatusList.sort(Comparator.comparing(ResourceCountPerUtilizationLevel::getStartValue));
            eachUtilizationData.setResourceCountByUtilization(eachStatusList);
            eachUtilizationData.setResourceCount(resourceCountByUtilizationForEachResourceCount.get(statusId));
            getResourcesByUtilizationList.add(eachUtilizationData);
        }

        if(getResourcesByUtilizationList.isEmpty()){
            getResourcesByUtilizationList = null;
        }
        else{

            if(isUndefinedColumnNotNeeded) {

                List<GetResourcesByUtilization> getResourcesByUtilizationListUpdated = new ArrayList<>();

                for (GetResourcesByUtilization eachUtilization : getResourcesByUtilizationList) {
                    List<ResourceCountPerUtilizationLevel> utilizationLevel = eachUtilization.getResourceCountByUtilization();
                    utilizationLevel.remove(utilizationLevel.size()-1);
                    eachUtilization.setResourceCountByUtilization(utilizationLevel);
                    getResourcesByUtilizationListUpdated.add(eachUtilization);
                }
                getResourcesByUtilizationList = new ArrayList<>(getResourcesByUtilizationListUpdated);
            }

            getResourcesByUtilizationList.sort(Comparator.comparing(GetResourcesByUtilization::getStartValue).reversed());
            Collections.rotate(getResourcesByUtilizationList, -1);

//            if undefined row has zero resources, remove it
            if(getResourcesByUtilizationList.get(getResourcesByUtilizationList.size()-1).getResourceCount().equals(0)){
                getResourcesByUtilizationList.remove(getResourcesByUtilizationList.size()-1);
            }
        }

        return getResourcesByUtilizationList;
    }

    public List<GetResourcesByProjectOwner> getResourcesByProjectOwner(GetDateRange getDateRange, String email, String role) {

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<GetResourcesByProjectOwner> getResourcesByProjectOwnerList = new ArrayList<>();

//        get all status from db
        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        List<ResourceMgmtStatus> resourceMgmtStatusList = new ArrayList<>();
        Double maxEnabledFteInAdmin = -1d;
        Integer maxEnabledFteInAdminId = null;

//        a template map for all the other maps
        Map<Integer, Integer> resourceCountByStatus = new HashMap<>();

//         a map for easier access to data
        Map<Integer, ResourceMgmtStatus> resourceMgmtStatusMap = new HashMap<>();

//        get all valid status from admin
        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                resourceMgmtStatusList.add(eachStatus);
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                    maxEnabledFteInAdminId = eachStatus.getStatusId();
                }
//                put status data to map for easier access
                resourceCountByStatus.put(eachStatus.getStatusId(), 0);
                resourceMgmtStatusMap.put(eachStatus.getStatusId(), eachStatus);
            }
        }

//        add undefined status to maps
        resourceCountByStatus.put(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID, 0);
        resourceMgmtStatusMap.put(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID, new ResourceMgmtStatus(ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID,
                ResourceUtils.UNDEFINED_UTILIZATION_NAME, ResourceUtils.UNDEFINED_UTILIZATION_COLOR, Double.MAX_VALUE, Double.MAX_VALUE,
                null, null, null, null, "1"));

//        get all resource allocation data
        List<GetResourceAllocationMatrixForLevelOneMatrix> getResourceAllAllocationData = getAllResourceAllocationMatrixData(getDateRange, email, role);

        boolean isUndefinedColumnNotNeeded = true;

//        for resource utilization in each project count
        Map<Integer, Map<Integer, Integer>> resourceUtilizationByProjectOwner = new HashMap<>();

//        for overall resource count per project owner
        Map<Integer, Integer> resourceCountByProjectOwner = new HashMap<>();

//        to save each project owner id and its name
        Map<Integer, String> projectOwnerIdNameMap = new HashMap<>();

//        traverse for each resource
        if(getResourceAllAllocationData!=null && !getResourceAllAllocationData.isEmpty()) {
            for (GetResourceAllocationMatrixForLevelOneMatrix eachResource : getResourceAllAllocationData) {
                if (eachResource.getAllocationData() != null && !eachResource.getAllocationData().isEmpty()) {

//                since a project owner and a resource can be associated on multiple projects,
//                this ,map will keep track that this pair is only counted as one in resource count under that project owner
                    Map<Integer, Integer> projectOwnerResourceIdTracker = new HashMap<>();

                    List<GetResourceAllProjectData> eachResourceAllocationData = eachResource.getAllocationData();
                    for (GetResourceAllProjectData eachAllocationData : eachResourceAllocationData) {

                        projectOwnerIdNameMap.putIfAbsent(eachAllocationData.getProjectOwnerId(), eachAllocationData.getProjectOwnerName());

                        if (!projectOwnerResourceIdTracker.containsKey(eachAllocationData.getProjectOwnerId())) {
//                        if this pair is not already counted, then add it
                            resourceCountByProjectOwner.merge(eachAllocationData.getProjectOwnerId(), 1, Integer::sum);

//                        and mark this pair as counted
                            projectOwnerResourceIdTracker.put(eachAllocationData.getProjectOwnerId(), eachResource.getResourceId());
                        }

//                    check if data regarding this project owner exists in the map
                        Map<Integer, Integer> resourceCountByStatusForEachProjectOwner
                                = new HashMap<>(resourceUtilizationByProjectOwner.getOrDefault(eachAllocationData.getProjectOwnerId(), resourceCountByStatus));

                        Double resourceProjectFteUtilization = eachAllocationData.getAvgFteForProject();

                        boolean ifAppropriateStatusDoesNotExist = true;

                        Integer statusId = null;

//                    based on each resource avg fte, assign mail utilization status id
                        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                            if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() != null && eachStatus.getEndValue() != null) {
                                if (resourceProjectFteUtilization >= eachStatus.getStartValue() && resourceProjectFteUtilization < eachStatus.getEndValue()) {
                                    statusId = eachStatus.getStatusId();
                                    ifAppropriateStatusDoesNotExist = false;
                                    break;
                                }
                            }
                        }
                        if (ifAppropriateStatusDoesNotExist) {
                            if (maxEnabledFteInAdminId != null && resourceProjectFteUtilization.equals(maxEnabledFteInAdmin)) {
                                statusId = maxEnabledFteInAdminId;
                            } else {
                                isUndefinedColumnNotNeeded = false;
                                statusId = ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID;
                            }
                        }

//                    resource utilization count
                        resourceCountByStatusForEachProjectOwner.merge(statusId, 1, Integer::sum);

                        resourceUtilizationByProjectOwner.put(eachAllocationData.getProjectOwnerId(), resourceCountByStatusForEachProjectOwner);
                    }
                }
            }
        }

//        convert the map data collected from previous loop to required json format
        for(Map.Entry<Integer, Map<Integer, Integer>> eachProjectOwner: resourceUtilizationByProjectOwner.entrySet()){
            GetResourcesByProjectOwner eachProjectOwnerData = new GetResourcesByProjectOwner();

//            for each utilization status in row
            Integer projectOwnerId = eachProjectOwner.getKey();
            eachProjectOwnerData.setProjectOwnerId(projectOwnerId);
            eachProjectOwnerData.setProjectOwnerName(projectOwnerIdNameMap.get(projectOwnerId));
            eachProjectOwnerData.setResourceCount(resourceCountByProjectOwner.get(projectOwnerId));

            List<ResourceCountPerUtilizationLevel> eachStatusList = new ArrayList<>();
            for(Map.Entry<Integer, Integer> eachStatusResult: eachProjectOwner.getValue().entrySet()){
                ResourceCountPerUtilizationLevel eachResourceStatusForEveryAllocation = new ResourceCountPerUtilizationLevel();

                ResourceMgmtStatus currentStatus = resourceMgmtStatusMap.get(eachStatusResult.getKey());

                eachResourceStatusForEveryAllocation.setResourceCount(eachStatusResult.getValue());
                eachResourceStatusForEveryAllocation.setStatusId(currentStatus.getStatusId());
                eachResourceStatusForEveryAllocation.setStatusName(currentStatus.getStatus());
                eachResourceStatusForEveryAllocation.setStartValue(currentStatus.getStartValue());
                eachResourceStatusForEveryAllocation.setEndValue(currentStatus.getEndValue());
                eachStatusList.add(eachResourceStatusForEveryAllocation);
            }
            eachStatusList.sort(Comparator.comparing(ResourceCountPerUtilizationLevel::getStartValue));
            eachProjectOwnerData.setResourceCountByUtilization(eachStatusList);
            eachProjectOwnerData.setResourceCount(resourceCountByProjectOwner.get(projectOwnerId));
            getResourcesByProjectOwnerList.add(eachProjectOwnerData);
        }

        if(getResourcesByProjectOwnerList.isEmpty()){
            getResourcesByProjectOwnerList = null;
        }
        else{

            if(isUndefinedColumnNotNeeded) {

                List<GetResourcesByProjectOwner> getResourcesByProjectOwnersListUpdated = new ArrayList<>();

                for (GetResourcesByProjectOwner eachProjectOwner : getResourcesByProjectOwnerList) {
                    List<ResourceCountPerUtilizationLevel> utilizationLevel = eachProjectOwner.getResourceCountByUtilization();
                    utilizationLevel.remove(utilizationLevel.size()-1);
                    eachProjectOwner.setResourceCountByUtilization(utilizationLevel);
                    getResourcesByProjectOwnersListUpdated.add(eachProjectOwner);
                }
                getResourcesByProjectOwnerList = new ArrayList<>(getResourcesByProjectOwnersListUpdated);
            }

            getResourcesByProjectOwnerList.sort(Comparator.comparing(GetResourcesByProjectOwner::getProjectOwnerName));
        }

        return getResourcesByProjectOwnerList;
    }

//    new Implementation
    public List<GetResourceAllocationChart> getResourceAllocationMatrixForAllResources(String dropdown, Integer financialYear, Integer id, String email, String role){

        List<GetResourceAllocationChart> returnData = null;

        switch (dropdown) {
            case ResourceUtils.RESOURCE_ALLOCATION_BY_DEPARTMENT:
                returnData =  getAllResourceAllocationMatrixByDepartmentId(financialYear, id, email, role);
                break;
            case ResourceUtils.RESOURCE_ALLOCATION_BY_PROJECT:
                returnData = getAllResourceAllocationMatrixByProjectId(financialYear, id, email, role);
                break;
            case ResourceUtils.RESOURCE_ALLOCATION_BY_UTILIZATION:
                returnData = getAllResourceAllocationMatrixByUtilizationLevel(financialYear, id, email, role);
                break;
            case ResourceUtils.RESOURCE_ALLOCATION_BY_PROJECT_OWNER:
                returnData = getAllResourceAllocationMatrixByProjectOwnerId(financialYear, id, email, role);
                break;
            default:
                return null;
        }

        return returnData;
    }

    public List<GetResourceAllocationChart> getAllResourceAllocationMatrixByDepartmentId(Integer financialYear, Integer departmentId, String email, String role){

        Optional<ResourceMgmtDepartment> departmentDetail = resourceMgmtDepartmentRepo.findById(departmentId);
        if (!departmentDetail.isPresent()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Department Not Found");
        }

//        generate start date and end date from financial year
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month financialYearStartMonth = Month.valueOf(startMonth.toUpperCase());

        LocalDate responseLocalStartDate= null, responseLocalEndDate= null;
        responseLocalStartDate = LocalDate.of(financialYear, financialYearStartMonth, startDateOfMonth);
        responseLocalEndDate = responseLocalStartDate.plusYears(1).minusDays(1);


        Date responseStartDate = java.sql.Date.valueOf(responseLocalStartDate);
        Date responseEndDate = java.sql.Date.valueOf(responseLocalEndDate);

        GetDateRange getDateRange = new GetDateRange(responseStartDate, responseEndDate);

//        get maximum allowed FTE by admin
        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        Double maxEnabledFteInAdmin = ResourceUtils.MIN_FTE;

        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                }
            }
        }

//        find the department id of the resource id sent by frontend
//        DepartmentStringIdName departmentIdName = resourceMgmtRepo.getResourceDepartmentId(departmentId);


//        query database to bring records for all resource
        String resourceProjectMapping = ResourceUtils.getFile("getResourceDepartmentAllResourceProjectAllocationDataQuery.txt");

        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllProjectAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("departmentId", String.valueOf(departmentId));

        List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);
        query.setParameter("resourceIds", visibleResourceIds);

//        get all allocation data
        List<ResourceAllProjectAllocationData> resourceAllProjectAllocationData = query.getResultList();

//        group records by resource I'd
        Map<Integer, List<ResourceAllProjectAllocationData>> groupByResourceId
                = resourceAllProjectAllocationData.stream().collect(Collectors.groupingBy(ResourceAllProjectAllocationData::getResource_id));

        List<GetResourceAllocationChart> getResourceAllocationChartList = new ArrayList<>();

        List<ResourceMgmt> allResourceByDepartmentId = resourceMgmtRepo.getAllResourceByDepartmentId(String.valueOf(departmentId));

        Set<Integer> listOfVisibleResourceIdsSet = new HashSet<>(visibleResourceIds);

//        for each resource
        for(ResourceMgmt eachResource: allResourceByDepartmentId){

            if(listOfVisibleResourceIdsSet.contains(eachResource.getResourceId())) {
//            set resource date of join and last working date
                LocalDate dateOfJoin = null, lastWorkingDate = null;

                if (eachResource.getDateOfJoin() != null) {
                    dateOfJoin = new java.sql.Date(eachResource.getDateOfJoin().getTime()).toLocalDate();
                }
                if (eachResource.getLastWorkingDate() != null) {
                    lastWorkingDate = new java.sql.Date(eachResource.getLastWorkingDate().getTime()).toLocalDate();
                }

                String resourceName = eachResource.getFirstName() + " " + eachResource.getLastName();

                List<ResourceAllProjectAllocationData> currentResourceData = groupByResourceId.get(eachResource.getResourceId());

//            calculate data
                GetResourceAllocationChart eachResourceData
                        = getDepartmentAllResourceAllocationsChartHelper(eachResource.getResourceId(), resourceName, responseLocalStartDate, responseLocalEndDate, dateOfJoin, lastWorkingDate,
                        maxEnabledFteInAdmin, currentResourceData, getDateRange, departmentDetail.get().getDepartment());

//            if data is there, add to list
                if (eachResourceData != null) {
                    getResourceAllocationChartList.add(eachResourceData);
                }
            }
        }

        if(getResourceAllocationChartList.isEmpty()){
            getResourceAllocationChartList = null;
        }

        return getResourceAllocationChartList;
    }

    public List<GetResourceAllocationChart> getAllResourceAllocationMatrixByProjectId(Integer financialYear, Integer projectId, String email, String role){

        Optional<ProjectMgmt> projectDetails = projectMgmtRepo.findById(projectId);
        if (!projectDetails.isPresent()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Project Not Found");
        }

//        generate start date and end date from financial year
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month financialYearStartMonth = Month.valueOf(startMonth.toUpperCase());

        LocalDate responseLocalStartDate= null, responseLocalEndDate= null;
        responseLocalStartDate = LocalDate.of(financialYear, financialYearStartMonth, startDateOfMonth);
        responseLocalEndDate = responseLocalStartDate.plusYears(1).minusDays(1);


        Date responseStartDate = java.sql.Date.valueOf(responseLocalStartDate);
        Date responseEndDate = java.sql.Date.valueOf(responseLocalEndDate);

        GetDateRange getDateRange = new GetDateRange(responseStartDate, responseEndDate);

//        get maximum allowed FTE by admin
        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        Double maxEnabledFteInAdmin = ResourceUtils.MIN_FTE;

        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                }
            }
        }

//        find the department id of the resource id sent by frontend
//        DepartmentStringIdName departmentIdName = resourceMgmtRepo.getResourceDepartmentId(departmentId);
        List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);


//        query database to bring records for all resource
        String resourceProjectMapping = ResourceUtils.getFile("getResourceDepartmentAllResourceProjectAllocationDataByProjectIdQuery.txt");

        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllProjectAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("projectId", projectId);
        query.setParameter("resourceIds", visibleResourceIds);

//        get all allocation data
        List<ResourceAllProjectAllocationData> resourceAllProjectAllocationData = query.getResultList();

//        group records by resource I'd
        Map<Integer, List<ResourceAllProjectAllocationData>> groupByResourceId
                = resourceAllProjectAllocationData.stream().collect(Collectors.groupingBy(ResourceAllProjectAllocationData::getResource_id));

        List<GetResourceAllocationChart> getResourceAllocationChartList = new ArrayList<>();

//        for each resource
        for(Map.Entry<Integer, List<ResourceAllProjectAllocationData>> entry: groupByResourceId.entrySet()){

//            set resource date of join and last working date
            LocalDate dateOfJoin = null, lastWorkingDate = null;

            if(entry.getValue().get(0).getDate_of_join()!=null) {
                dateOfJoin = new java.sql.Date(entry.getValue().get(0).getDate_of_join().getTime()).toLocalDate();
            }
            if(entry.getValue().get(0).getLast_working_date()!=null) {
                lastWorkingDate = new java.sql.Date(entry.getValue().get(0).getLast_working_date().getTime()).toLocalDate();
            }

//            calculate data
            GetResourceAllocationChart eachResourceData
                    = getDepartmentAllResourceAllocationsChartHelper(entry.getKey(), entry.getValue().get(0).getResource_name(), responseLocalStartDate, responseLocalEndDate, dateOfJoin, lastWorkingDate,
                    maxEnabledFteInAdmin, entry.getValue(), getDateRange, null);

//            if data is there, add to list
            if(eachResourceData!=null){
                getResourceAllocationChartList.add(eachResourceData);
            }
        }

        if(getResourceAllocationChartList.isEmpty()){
            getResourceAllocationChartList = null;
        }

        return getResourceAllocationChartList;
    }

    public List<GetResourceAllocationChart> getAllResourceAllocationMatrixByUtilizationLevel(Integer financialYear, Integer statusId, String email, String role){

        Optional<ResourceMgmtStatus> utilizationLevel = resourceMgmtStatusRepo.findById(statusId);
        if (!utilizationLevel.isPresent()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Status id Not Found");
        }

//        generate start date and end date from financial year
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month financialYearStartMonth = Month.valueOf(startMonth.toUpperCase());

        LocalDate responseLocalStartDate= null, responseLocalEndDate= null;
        responseLocalStartDate = LocalDate.of(financialYear, financialYearStartMonth, startDateOfMonth);
        responseLocalEndDate = responseLocalStartDate.plusYears(1).minusDays(1);


        Date responseStartDate = java.sql.Date.valueOf(responseLocalStartDate);
        Date responseEndDate = java.sql.Date.valueOf(responseLocalEndDate);

        GetDateRange getDateRange = new GetDateRange(responseStartDate, responseEndDate);
//        get maximum allowed FTE by admin
        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        Double maxEnabledFteInAdmin = ResourceUtils.MIN_FTE;
        Double statusFteStartValue = 0d, statusFteEndValue = 0d;

        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                }
                if(eachStatus.getStatusId().equals(statusId)){
                    statusFteStartValue = eachStatus.getStartValue();
                    statusFteEndValue = eachStatus.getEndValue();
                }
            }
        }

//        find the department id of the resource id sent by frontend
//        DepartmentStringIdName departmentIdName = resourceMgmtRepo.getResourceDepartmentId(departmentId);
        List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);

//        calculate utilization level of all resources, and find the required resources
        List<Integer> listOfResourceIds = new ArrayList<>();

        String getResourceTotalAllocationSumQuery = ResourceUtils.getFile("getResourceTotalAllocationSumQuery.txt");

        Query query0 = entityManager.createNativeQuery(getResourceTotalAllocationSumQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query0).setResultTransformer(new AliasToBeanResultTransformer(ResourceTotalAllocationSum.class));

        query0.setParameter("startDate", getDateRange.getStartDate());
        query0.setParameter("endDate", getDateRange.getEndDate());
        query0.setParameter("resourceIds", visibleResourceIds);

        List<ResourceTotalAllocationSum> resourceTotalAllocationSumList = query0.getResultList();

        Map<Integer, ResourceTotalAllocationSum> resourceTotalAllocationSumListById
                = resourceTotalAllocationSumList.stream().collect(Collectors.toMap(ResourceTotalAllocationSum::getResource_id, data->data));

        if(resourceTotalAllocationSumList==null || resourceTotalAllocationSumList.isEmpty()){
            return null;
        }
        else{
            for(ResourceTotalAllocationSum eachResource: resourceTotalAllocationSumList) {
                Date startdate = getDateRange.getStartDate(), endDate = getDateRange.getEndDate();
                if (eachResource.getDate_of_join()!=null && startdate.compareTo(eachResource.getDate_of_join())<0) {
                    startdate = eachResource.getDate_of_join();
                }
                if (eachResource.getLast_working_date()!=null && endDate.compareTo(eachResource.getLast_working_date())<0) {
                    endDate = eachResource.getLast_working_date();
                }
                LocalDate localStartDate = new java.sql.Date(startdate.getTime()).toLocalDate();
                LocalDate localEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();
                Integer workingDays = ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
                Double avgAllocatedFte = ResourceUtils.roundToTwoDecimalPlace(eachResource.getSum_fte().doubleValue()/workingDays);
                if(avgAllocatedFte>=statusFteStartValue && avgAllocatedFte<=statusFteEndValue){
                    listOfResourceIds.add(eachResource.getResource_id());
                }
            }
        }

//        query database to bring records for all resource
        String resourceProjectMapping = ResourceUtils.getFile("getResourceDepartmentAllResourceProjectAllocationDataByStatusIdQuery.txt");

        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllProjectAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("resourceIds", listOfResourceIds);
//        query.setParameter("departmentIds", getVisibleDepartmentIdsSet);

//        get all allocation data
        List<ResourceAllProjectAllocationData> resourceAllProjectAllocationData = query.getResultList();

//        group records by resource I'd
        Map<Integer, List<ResourceAllProjectAllocationData>> groupByResourceId
                = resourceAllProjectAllocationData.stream().collect(Collectors.groupingBy(ResourceAllProjectAllocationData::getResource_id));

        List<GetResourceAllocationChart> getResourceAllocationChartList = new ArrayList<>();

//        for each resource
        for(Integer eachResource: listOfResourceIds){

//            set resource date of join and last working date
            LocalDate dateOfJoin = null, lastWorkingDate = null;

            String resourceName = resourceTotalAllocationSumListById.get(eachResource).getResource_name();
            Date resourceDateOfJoin = resourceTotalAllocationSumListById.get(eachResource).getDate_of_join();
            Date resourceLastWorkingDate = resourceTotalAllocationSumListById.get(eachResource).getLast_working_date();

            if(resourceDateOfJoin!=null) {
                dateOfJoin = new java.sql.Date(resourceDateOfJoin.getTime()).toLocalDate();
            }
            if(resourceLastWorkingDate!=null) {
                lastWorkingDate = new java.sql.Date(resourceLastWorkingDate.getTime()).toLocalDate();
            }

//            calculate data
            GetResourceAllocationChart eachResourceData
                    = getDepartmentAllResourceAllocationsChartHelper(eachResource, resourceName, responseLocalStartDate, responseLocalEndDate, dateOfJoin, lastWorkingDate,
                    maxEnabledFteInAdmin, groupByResourceId.get(eachResource), getDateRange, null);

//            if data is there, add to list
            if(eachResourceData!=null){
                getResourceAllocationChartList.add(eachResourceData);
            }
        }

        if(getResourceAllocationChartList.isEmpty()){
            getResourceAllocationChartList = null;
        }

        return getResourceAllocationChartList;
    }

    public List<GetResourceAllocationChart> getAllResourceAllocationMatrixByProjectOwnerId(Integer financialYear, Integer projectOwnerId, String email, String role){

        Optional<ResourceMgmt> resourceDetails = resourceMgmtRepo.findById(projectOwnerId);
        if (!resourceDetails.isPresent()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
        }

//        generate start date and end date from financial year
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month financialYearStartMonth = Month.valueOf(startMonth.toUpperCase());

        LocalDate responseLocalStartDate= null, responseLocalEndDate= null;
        responseLocalStartDate = LocalDate.of(financialYear, financialYearStartMonth, startDateOfMonth);
        responseLocalEndDate = responseLocalStartDate.plusYears(1).minusDays(1);


        Date responseStartDate = java.sql.Date.valueOf(responseLocalStartDate);
        Date responseEndDate = java.sql.Date.valueOf(responseLocalEndDate);

        GetDateRange getDateRange = new GetDateRange(responseStartDate, responseEndDate);

//        get maximum allowed FTE by admin
        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        Double maxEnabledFteInAdmin = ResourceUtils.MIN_FTE;

        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                }
            }
        }

//        find the department id of the resource id sent by frontend
//        DepartmentStringIdName departmentIdName = resourceMgmtRepo.getResourceDepartmentId(departmentId);
        List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);


//        query database to bring records for all resource
        String resourceProjectMapping = ResourceUtils.getFile("getResourceDepartmentAllResourceProjectAllocationDataByProjectOwnerIdQuery.txt");

        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllProjectAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("projectOwnerId", String.valueOf(projectOwnerId));
        query.setParameter("resourceIds", visibleResourceIds);

//        get all allocation data
        List<ResourceAllProjectAllocationData> resourceAllProjectAllocationData = query.getResultList();

//        group records by resource I'd
        Map<Integer, List<ResourceAllProjectAllocationData>> groupByResourceId
                = resourceAllProjectAllocationData.stream().collect(Collectors.groupingBy(ResourceAllProjectAllocationData::getResource_id));

        List<GetResourceAllocationChart> getResourceAllocationChartList = new ArrayList<>();

//        for each resource
        for(Map.Entry<Integer, List<ResourceAllProjectAllocationData>> entry: groupByResourceId.entrySet()){

//            set resource date of join and last working date
            LocalDate dateOfJoin = null, lastWorkingDate = null;

            if(entry.getValue().get(0).getDate_of_join()!=null) {
                dateOfJoin = new java.sql.Date(entry.getValue().get(0).getDate_of_join().getTime()).toLocalDate();
            }
            if(entry.getValue().get(0).getLast_working_date()!=null) {
                lastWorkingDate = new java.sql.Date(entry.getValue().get(0).getLast_working_date().getTime()).toLocalDate();
            }

//            calculate data
            GetResourceAllocationChart eachResourceData
                    = getDepartmentAllResourceAllocationsChartHelper(entry.getKey(), entry.getValue().get(0).getResource_name(), responseLocalStartDate, responseLocalEndDate, dateOfJoin, lastWorkingDate,
                    maxEnabledFteInAdmin, entry.getValue(), getDateRange, null);

//            if data is there, add to list
            if(eachResourceData!=null){
                getResourceAllocationChartList.add(eachResourceData);
            }
        }

        if(getResourceAllocationChartList.isEmpty()){
            getResourceAllocationChartList = null;
        }

        return getResourceAllocationChartList;
    }

/*
    //    old implementation, to be changed soon
    public List<GetResourceAllocationMatrix> getResourceAllocationMatrix(String dropdown, Integer id, GetDateRange getDateRange, String email){

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<GetResourceAllocationMatrix> returnData = null;

        switch (dropdown) {
            case ResourceUtils.RESOURCE_ALLOCATION_BY_DEPARTMENT:
                returnData =  getResourceAllocationMatrixByDepartmentId(getDateRange, id);
                break;
            case ResourceUtils.RESOURCE_ALLOCATION_BY_PROJECT:
                returnData = getResourceAllocationMatrixByProjectId(getDateRange, id, email);
                break;
            case ResourceUtils.RESOURCE_ALLOCATION_BY_UTILIZATION:
                returnData = getResourceAllocationMatrixByUtilizationLevel(getDateRange, id, email);
                break;
            case ResourceUtils.RESOURCE_ALLOCATION_BY_PROJECT_OWNER:
                returnData = getResourceAllocationMatrixByProjectOwnerId(getDateRange, id, email);
                break;
            default:
                return null;
        }

        if(returnData!=null && !returnData.isEmpty()) {
            returnData.sort(Comparator.comparing(GetResourceAllocationMatrix::getResourceName, (s1, s2)->{
                        String resourceName1 = s1.toLowerCase();
                        String resourceName2 = s2.toLowerCase();
                        return String.CASE_INSENSITIVE_ORDER.compare(resourceName1, resourceName2);
                    })
                    .thenComparing(GetResourceAllocationMatrix::getResourceId)
                    .thenComparing(GetResourceAllocationMatrix::getProjectName, (s1, s2)->{
                        String projectName1 = s1.toLowerCase();
                        String projectName2 = s2.toLowerCase();
                        return String.CASE_INSENSITIVE_ORDER.compare(projectName1, projectName2);
                    })
                    .thenComparing(GetResourceAllocationMatrix::getAllocationStartDate));
        }

        return returnData;
    }

    public List<GetResourceAllocationMatrix> getResourceAllocationMatrixByDepartmentId(GetDateRange getDateRange, Integer departmentId) {

//        if end date is before start date
        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<GetResourceAllocationMatrix> getResourceAnalyticsMasterList = new ArrayList<>();
        String resourceProjectMapping = ResourceUtils.getFile("resourceAllProjectAllocationByDepartmentId.txt");


        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetProjectResourceAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("departmentId", departmentId);


//        bring all the resource and the project allocation data
        List<GetProjectResourceAllocationData> getResourceAnalyticsList = query.getResultList();

        Map<Integer, Double> totalResourceFte = new HashMap<>();
        Map<Integer, Date> resourceStartDate = new HashMap<>();
        Map<Integer, Date> resourceEndDate = new HashMap<>();
        Map<Integer, Double> resourceAverageFte = new HashMap<>();

        String resourceTotalFte = ResourceUtils.getFile("resourceTotalFteForDateRange.txt");

        Query query1 = entityManager.createNativeQuery(resourceTotalFte).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ResourceTotalFteForDateRange.class));

        query1.setParameter("startDate", getDateRange.getStartDate());
        query1.setParameter("endDate", getDateRange.getEndDate());

//        bring all the resource and the project allocation data
        List<ResourceTotalFteForDateRange> resourceTotalFteList = query1.getResultList();

        for(ResourceTotalFteForDateRange eachResource: resourceTotalFteList){
            totalResourceFte.put(eachResource.getResource_id(), eachResource.getSum_fte().doubleValue());
        }

        if (getResourceAnalyticsList != null && !getResourceAnalyticsList.isEmpty()) {

            for (GetProjectResourceAllocationData eachRecord : getResourceAnalyticsList) {

                GetResourceAllocationMatrix getData = new GetResourceAllocationMatrix(eachRecord);

//                collect resource start date
                if(!resourceStartDate.containsKey(eachRecord.getResource_id())){
                    Date startDate = eachRecord.getDate_of_join().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():eachRecord.getDate_of_join();
                    resourceStartDate.put(eachRecord.getResource_id(), startDate);
                }

//                collect resource end date
                if(!resourceEndDate.containsKey(eachRecord.getResource_id())){
                    Date endDate;
                    if(eachRecord.getLast_working_date()!=null) {
                        endDate = eachRecord.getLast_working_date().compareTo(getDateRange.getEndDate()) < 0 ?eachRecord.getLast_working_date(): getDateRange.getEndDate();
                    }
                    else{
                        endDate = getDateRange.getEndDate();
                    }
                    resourceEndDate.put(eachRecord.getResource_id(), endDate);
                }

                if(getData.getMapId()!=null) {
                        Integer workingDays = 1;
//                            calculate working days in this allocation's date range

                        try {
                            LocalDate startDate, endDate;
//                            if (getData.getAllocationStartDate().compareTo(getDateRange.getStartDate()) >= 0) {
                                startDate = new java.sql.Date(getData.getAllocationStartDate().getTime()).toLocalDate();
//                            } else {
//                                startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                            }

//                            if (getData.getAllocationEndDate().compareTo(getDateRange.getEndDate()) >= 0) {
//                                endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                            } else {
                                endDate = new java.sql.Date(getData.getAllocationEndDate().getTime()).toLocalDate();
//                            }

                            workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

                        } catch (Exception e) {
                            System.out.println("error---> ");
                        }

//                        calculate avg fte for this allocation
                        getData.setAvgFteForProject(ResourceUtils.roundToTwoDecimalPlace(eachRecord.getSum_fte().doubleValue() / workingDays));

                }

                getResourceAnalyticsMasterList.add(getData);
            }
        }

        List<GetResourceAllocationMatrix> getResourceAllocationDataList = new ArrayList<>();

        if(!getResourceAnalyticsMasterList.isEmpty()){
            for(GetResourceAllocationMatrix eachResource: getResourceAnalyticsMasterList){

//                assign avg overall fte here
//                if avg fte already calculated
                if(resourceAverageFte.containsKey(eachResource.getResourceId())){
                    eachResource.setAverageFte(resourceAverageFte.get(eachResource.getResourceId()));
                }
                else {
//                    calculate avg fte of the resource
                    LocalDate localStartDate = new java.sql.Date(resourceStartDate.get(eachResource.getResourceId()).getTime()).toLocalDate();
                    LocalDate localEndDate = new java.sql.Date(resourceEndDate.get(eachResource.getResourceId()).getTime()).toLocalDate();
                    Integer workingDays = ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
                    Double averageFte = totalResourceFte.get(eachResource.getResourceId())/workingDays;
                    averageFte = ResourceUtils.roundToTwoDecimalPlace(averageFte);
                    eachResource.setAverageFte(averageFte);
                    resourceAverageFte.put(eachResource.getResourceId(), averageFte);
                }
                getResourceAllocationDataList.add(eachResource);
            }
        }
        else{
            getResourceAllocationDataList = null;
        }

        return getResourceAllocationDataList;
    }

    public List<GetResourceAllocationMatrix> getResourceAllocationMatrixByProjectId(GetDateRange getDateRange, Integer projectId, String email) {

//        if end date is before start date
        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<Integer> getVisibleDepartmentIdsList = getVisibleDepartmentIds(email);
        List<String> getVisibleDepartmentIdsSet = new ArrayList<>();
        for(Integer i:getVisibleDepartmentIdsList){
            getVisibleDepartmentIdsSet.add(String.valueOf(i));
        }

        List<GetResourceAllocationMatrix> getResourceAnalyticsMasterList = new ArrayList<>();

        String resourceProjectMapping = ResourceUtils.getFile("resourceAllProjectAllocationByProjectId.txt");


        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetProjectResourceAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("projectId", projectId);
        query.setParameter("departmentIds", getVisibleDepartmentIdsSet);

//        bring all the resource and the project allocation data
        List<GetProjectResourceAllocationData> getResourceAnalyticsList = query.getResultList();

        Map<Integer, Double> totalResourceFte = new HashMap<>();
        Map<Integer, Date> resourceStartDate = new HashMap<>();
        Map<Integer, Date> resourceEndDate = new HashMap<>();
        Map<Integer, Double> resourceAverageFte = new HashMap<>();

        String resourceTotalFte = ResourceUtils.getFile("resourceTotalFteForDateRange.txt");

        Query query1 = entityManager.createNativeQuery(resourceTotalFte).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ResourceTotalFteForDateRange.class));

        query1.setParameter("startDate", getDateRange.getStartDate());
        query1.setParameter("endDate", getDateRange.getEndDate());

//        bring all the resource and the project allocation data
        List<ResourceTotalFteForDateRange> resourceTotalFteList = query1.getResultList();

        for(ResourceTotalFteForDateRange eachResource: resourceTotalFteList){
            totalResourceFte.put(eachResource.getResource_id(), eachResource.getSum_fte().doubleValue());
        }

        if (getResourceAnalyticsList != null && !getResourceAnalyticsList.isEmpty()) {

            for (GetProjectResourceAllocationData eachRecord : getResourceAnalyticsList) {

                GetResourceAllocationMatrix getData = new GetResourceAllocationMatrix(eachRecord);

//                collect resource start date
                if(!resourceStartDate.containsKey(eachRecord.getResource_id())){
                    Date startDate = eachRecord.getDate_of_join().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():eachRecord.getDate_of_join();
                    resourceStartDate.put(eachRecord.getResource_id(), startDate);
                }

//                collect resource end date
                if(!resourceEndDate.containsKey(eachRecord.getResource_id())){
                    Date endDate;
                    if(eachRecord.getLast_working_date()!=null) {
                        endDate = eachRecord.getLast_working_date().compareTo(getDateRange.getEndDate()) < 0 ?eachRecord.getLast_working_date(): getDateRange.getEndDate();
                    }
                    else{
                        endDate = getDateRange.getEndDate();
                    }
                    resourceEndDate.put(eachRecord.getResource_id(), endDate);
                }

                if(getData.getMapId()!=null) {
                    Integer workingDays = 1;
//                            calculate working days in this allocation's date range

                    try {
                        LocalDate startDate, endDate;
//                        if (getData.getAllocationStartDate().compareTo(getDateRange.getStartDate()) >= 0) {
                            startDate = new java.sql.Date(getData.getAllocationStartDate().getTime()).toLocalDate();
//                        } else {
//                            startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                        }

//                        if (getData.getAllocationEndDate().compareTo(getDateRange.getEndDate()) >= 0) {
//                            endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                        } else {
                            endDate = new java.sql.Date(getData.getAllocationEndDate().getTime()).toLocalDate();
//                        }

                        workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

                    } catch (Exception e) {
                        System.out.println("error---> ");
                    }

//                        calculate avg fte for this allocation
                    getData.setAvgFteForProject(ResourceUtils.roundToTwoDecimalPlace(eachRecord.getSum_fte().doubleValue() / workingDays));

                }

                getResourceAnalyticsMasterList.add(getData);
            }
        }

        List<GetResourceAllocationMatrix> getResourceAllocationDataList = new ArrayList<>();

        if(!getResourceAnalyticsMasterList.isEmpty()){
            for(GetResourceAllocationMatrix eachResource: getResourceAnalyticsMasterList){

//                assign avg overall fte here
//                if avg fte already calculated
                if(resourceAverageFte.containsKey(eachResource.getResourceId())){
                    eachResource.setAverageFte(resourceAverageFte.get(eachResource.getResourceId()));
                }
                else {
//                    calculate avg fte of the resource
                    LocalDate localStartDate = new java.sql.Date(resourceStartDate.get(eachResource.getResourceId()).getTime()).toLocalDate();
                    LocalDate localEndDate = new java.sql.Date(resourceEndDate.get(eachResource.getResourceId()).getTime()).toLocalDate();
                    Integer workingDays = ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
                    Double averageFte = totalResourceFte.get(eachResource.getResourceId())/workingDays;
                    averageFte = ResourceUtils.roundToTwoDecimalPlace(averageFte);
                    eachResource.setAverageFte(averageFte);
                    resourceAverageFte.put(eachResource.getResourceId(), averageFte);
                }
                getResourceAllocationDataList.add(eachResource);
            }
        }
        else{
            getResourceAllocationDataList = null;
        }

        return getResourceAllocationDataList;
    }


    public List<GetResourceAllocationMatrix> getResourceAllocationMatrixByUtilizationLevel(GetDateRange getDateRange, Integer statusId, String email){

//        if end date is before start date
        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<Integer> getVisibleDepartmentIdsList = getVisibleDepartmentIds(email);
        List<String> getVisibleDepartmentIdsSet = new ArrayList<>();
        for(Integer i:getVisibleDepartmentIdsList){
            getVisibleDepartmentIdsSet.add(String.valueOf(i));
        }

        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();

        boolean isStatusIdInvalid = true;

        List<ResourceMgmtStatus> resourceMgmtStatusList = new ArrayList<>();
        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled() && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                if(eachStatus.getStatusId().equals(statusId)){
                    isStatusIdInvalid = false;
                }
                resourceMgmtStatusList.add(eachStatus);
            }
        }
        if(isStatusIdInvalid && statusId!=0){
            return null;
        }
        if(!resourceMgmtStatusList.isEmpty()){
            resourceMgmtStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
        }

        List<GetResourceAllocationMatrix> getResourceAnalyticsMasterList = new ArrayList<>();

        String resourceProjectMapping = ResourceUtils.getFile("resourceAllProjectAllocation.txt");

        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetProjectResourceAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("departmentIds", getVisibleDepartmentIdsSet);

//        bring all the resource and the project allocation data
        List<GetProjectResourceAllocationData> getProjectResourceAllocationDataList = query.getResultList();

        Map<Integer, Double> totalResourceFte = new HashMap<>();
        Map<Integer, Date> resourceStartDate = new HashMap<>();
        Map<Integer, Date> resourceEndDate = new HashMap<>();
        Map<Integer, Double> resourceAverageFte = new HashMap<>();

        String resourceTotalFte = ResourceUtils.getFile("resourceTotalFteForDateRange.txt");

        Query query1 = entityManager.createNativeQuery(resourceTotalFte).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ResourceTotalFteForDateRange.class));

        query1.setParameter("startDate", getDateRange.getStartDate());
        query1.setParameter("endDate", getDateRange.getEndDate());

//        bring all the resource and the project allocation data
        List<ResourceTotalFteForDateRange> resourceTotalFteList = query1.getResultList();

        for(ResourceTotalFteForDateRange eachResource: resourceTotalFteList){
            totalResourceFte.put(eachResource.getResource_id(), eachResource.getSum_fte().doubleValue());
        }

        if (getProjectResourceAllocationDataList != null && !getProjectResourceAllocationDataList.isEmpty()) {

            for (GetProjectResourceAllocationData eachRecord : getProjectResourceAllocationDataList) {

                GetResourceAllocationMatrix getData = new GetResourceAllocationMatrix(eachRecord);

//                collect resource start date
                if(!resourceStartDate.containsKey(eachRecord.getResource_id())){
                    Date startDate = eachRecord.getDate_of_join().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():eachRecord.getDate_of_join();
                    resourceStartDate.put(eachRecord.getResource_id(), startDate);
                }

//                collect resource end date
                if(!resourceEndDate.containsKey(eachRecord.getResource_id())){
                    Date endDate;
                    if(eachRecord.getLast_working_date()!=null) {
                        endDate = eachRecord.getLast_working_date().compareTo(getDateRange.getEndDate()) < 0 ?eachRecord.getLast_working_date(): getDateRange.getEndDate();
                    }
                    else{
                        endDate = getDateRange.getEndDate();
                    }
                    resourceEndDate.put(eachRecord.getResource_id(), endDate);
                }

                if(getData.getMapId()!=null) {
                    Integer workingDays = 1;
//                            calculate working days in this allocation's date range

                    try {
                        LocalDate startDate, endDate;
//                        if (getData.getAllocationStartDate().compareTo(getDateRange.getStartDate()) >= 0) {
                            startDate = new java.sql.Date(getData.getAllocationStartDate().getTime()).toLocalDate();
//                        } else {
//                            startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                        }

//                        if (getData.getAllocationEndDate().compareTo(getDateRange.getEndDate()) >= 0) {
//                            endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                        } else {
                            endDate = new java.sql.Date(getData.getAllocationEndDate().getTime()).toLocalDate();
//                        }

                        workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

                    } catch (Exception e) {
                        System.out.println("error---> ");
                    }

//                        calculate avg fte for this allocation
                    getData.setAvgFteForProject(ResourceUtils.roundToTwoDecimalPlace(eachRecord.getSum_fte().doubleValue() / workingDays));

                }

                getResourceAnalyticsMasterList.add(getData);
            }
        }

        List<GetResourceAllocationMatrix> getResourceAllocationDataList = new ArrayList<>();

        if(!getResourceAnalyticsMasterList.isEmpty()){
            Map<Integer, Long> resourceCount = new HashMap<>();
            for(GetResourceAllocationMatrix eachResource: getResourceAnalyticsMasterList){

//                assign avg overall fte here

//                if avg fte already calculated
                if(resourceAverageFte.containsKey(eachResource.getResourceId())){
                    eachResource.setAverageFte(resourceAverageFte.get(eachResource.getResourceId()));
                }
                else {
//                    calculate avg fte of the resource
                    LocalDate localStartDate = new java.sql.Date(resourceStartDate.get(eachResource.getResourceId()).getTime()).toLocalDate();
                    LocalDate localEndDate = new java.sql.Date(resourceEndDate.get(eachResource.getResourceId()).getTime()).toLocalDate();
                    Integer workingDays = ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
                    Double averageFte = totalResourceFte.get(eachResource.getResourceId())/workingDays;
                    averageFte = ResourceUtils.roundToTwoDecimalPlace(averageFte);
                    eachResource.setAverageFte(averageFte);
                    resourceAverageFte.put(eachResource.getResourceId(), averageFte);
                }

                boolean ifAppropriateStatusDoesNotExist = true;
                Double maxEnabledFteInAdmin = -1d;
                Integer maxEnabledFteInAdminId = null;

                Integer statusIdAssigned = null;

                for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
                    if (eachStatus.getIsEnabled() && eachStatus.getStartValue() !=null && eachStatus.getEndValue()!=null){
                        if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                            maxEnabledFteInAdmin = eachStatus.getEndValue();
                            maxEnabledFteInAdminId = eachStatus.getStatusId();
                        }
                        if(eachResource.getAverageFte() >= eachStatus.getStartValue() && eachResource.getAverageFte() < eachStatus.getEndValue()) {
                            statusIdAssigned = eachStatus.getStatusId();
                            ifAppropriateStatusDoesNotExist = false;
                            break;
                        }
                    }
                }
                if (ifAppropriateStatusDoesNotExist) {
                    if(maxEnabledFteInAdminId!=null && eachResource.getAverageFte()==maxEnabledFteInAdmin){
                        statusIdAssigned = maxEnabledFteInAdminId;
                    }
                    else {
                        statusIdAssigned = ResourceUtils.UNDEFINED_UTILIZATION_STATUS_ID;
                    }
                }
                if(statusIdAssigned.equals(statusId)){
                    getResourceAllocationDataList.add(eachResource);
                }
                resourceCount.merge(statusId, 1L, Long::sum);
            }
//            System.out.println("checking");
        }
        else{
            return null;
        }

        if(getResourceAllocationDataList.isEmpty()){
            getResourceAllocationDataList = null;
        }

        return getResourceAllocationDataList;
    }

    public List<GetResourceAllocationMatrix> getResourceAllocationMatrixByProjectOwnerId(GetDateRange getDateRange, Integer projectOwnerId, String email) {

//        if end date is before start date
        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        List<Integer> getVisibleDepartmentIdsList = getVisibleDepartmentIds(email);
        List<String> getVisibleDepartmentIdsSet = new ArrayList<>();
        for(Integer i:getVisibleDepartmentIdsList){
            getVisibleDepartmentIdsSet.add(String.valueOf(i));
        }

        List<GetResourceAllocationMatrix> getResourceAnalyticsMasterList = new ArrayList<>();

        String resourceProjectMapping = ResourceUtils.getFile("resourceAllProjectAllocationByProjectOwnerId.txt");


        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(GetProjectResourceAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("projectOwnerId", projectOwnerId);
        query.setParameter("departmentIds", getVisibleDepartmentIdsSet);

//        bring all the resource and the project allocation data
        List<GetProjectResourceAllocationData> getResourceAnalyticsList = query.getResultList();

        Map<Integer, Double> totalResourceFte = new HashMap<>();
        Map<Integer, Date> resourceStartDate = new HashMap<>();
        Map<Integer, Date> resourceEndDate = new HashMap<>();
        Map<Integer, Double> resourceAverageFte = new HashMap<>();


        String resourceTotalFte = ResourceUtils.getFile("resourceTotalFteForDateRange.txt");

        Query query1 = entityManager.createNativeQuery(resourceTotalFte).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query1).setResultTransformer(new AliasToBeanResultTransformer(ResourceTotalFteForDateRange.class));

        query1.setParameter("startDate", getDateRange.getStartDate());
        query1.setParameter("endDate", getDateRange.getEndDate());

//        bring all the resource and the project allocation data
        List<ResourceTotalFteForDateRange> resourceTotalFteList = query1.getResultList();

        for(ResourceTotalFteForDateRange eachResource: resourceTotalFteList){
            totalResourceFte.put(eachResource.getResource_id(), eachResource.getSum_fte().doubleValue());
        }

        if (getResourceAnalyticsList != null && !getResourceAnalyticsList.isEmpty()) {

            for (GetProjectResourceAllocationData eachRecord : getResourceAnalyticsList) {

                GetResourceAllocationMatrix getData = new GetResourceAllocationMatrix(eachRecord);

//                collect resource start date
                if(!resourceStartDate.containsKey(eachRecord.getResource_id())){
                    Date startDate = eachRecord.getDate_of_join().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():eachRecord.getDate_of_join();
                    resourceStartDate.put(eachRecord.getResource_id(), startDate);
                }

//                collect resource end date
                if(!resourceEndDate.containsKey(eachRecord.getResource_id())){
                    Date endDate;
                    if(eachRecord.getLast_working_date()!=null) {
                        endDate = eachRecord.getLast_working_date().compareTo(getDateRange.getEndDate()) < 0 ?eachRecord.getLast_working_date(): getDateRange.getEndDate();
                    }
                    else{
                        endDate = getDateRange.getEndDate();
                    }
                    resourceEndDate.put(eachRecord.getResource_id(), endDate);
                }

                if(getData.getMapId()!=null) {
                    Integer workingDays = 1;
//                            calculate working days in this allocation's date range

                    try {
                        LocalDate startDate, endDate;
//                        if (getData.getAllocationStartDate().compareTo(getDateRange.getStartDate()) >= 0) {
                            startDate = new java.sql.Date(getData.getAllocationStartDate().getTime()).toLocalDate();
//                        } else {
//                            startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
//                        }

//                        if (getData.getAllocationEndDate().compareTo(getDateRange.getEndDate()) >= 0) {
//                            endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
//                        } else {
                            endDate = new java.sql.Date(getData.getAllocationEndDate().getTime()).toLocalDate();
//                        }

                        workingDays = ResourceUtils.numberOfWorkingDays(startDate, endDate);

                    } catch (Exception e) {
                        System.out.println("error---> ");
                    }

//                        calculate avg fte for this allocation
                    getData.setAvgFteForProject(ResourceUtils.roundToTwoDecimalPlace(eachRecord.getSum_fte().doubleValue() / workingDays));

                }

                getResourceAnalyticsMasterList.add(getData);
            }
        }

        List<GetResourceAllocationMatrix> getResourceAllocationDataList = new ArrayList<>();

        if(!getResourceAnalyticsMasterList.isEmpty()){
            for(GetResourceAllocationMatrix eachResource: getResourceAnalyticsMasterList){

//                assign avg overall fte here
//                if avg fte already calculated
                if(resourceAverageFte.containsKey(eachResource.getResourceId())){
                    eachResource.setAverageFte(resourceAverageFte.get(eachResource.getResourceId()));
                }
                else {
//                    calculate avg fte of the resource
                    LocalDate localStartDate = new java.sql.Date(resourceStartDate.get(eachResource.getResourceId()).getTime()).toLocalDate();
                    LocalDate localEndDate = new java.sql.Date(resourceEndDate.get(eachResource.getResourceId()).getTime()).toLocalDate();
                    Integer workingDays = ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
                    Double averageFte = totalResourceFte.get(eachResource.getResourceId())/workingDays;
                    averageFte = ResourceUtils.roundToTwoDecimalPlace(averageFte);
                    eachResource.setAverageFte(averageFte);
                    resourceAverageFte.put(eachResource.getResourceId(), averageFte);
                }
                getResourceAllocationDataList.add(eachResource);
            }
        }
        else{
            getResourceAllocationDataList = null;
        }

        return getResourceAllocationDataList;
    }

    */

    /*public List<AllResourceActiveAllocation> getAllocationDataByMapId(Integer mapId){
        List<AllResourceActiveAllocation> allocationData = projectResourceMappingRepo.getAllocationDataByMapId(mapId);
        List<AllResourceActiveAllocation> allocationDataWithDateOnly = new ArrayList<>();
        if(allocationData!=null && !allocationData.isEmpty()){
            for(AllResourceActiveAllocation eachAllocation: allocationData){
                AllResourceActiveAllocation newAllocationWithDateOnlyData = new AllResourceActiveAllocation();
                newAllocationWithDateOnlyData.setId(eachAllocation.getId());
                newAllocationWithDateOnlyData.setMapId(eachAllocation.getMapId());
                newAllocationWithDateOnlyData.setFte(eachAllocation.getFte());

                Date oldDate = eachAllocation.getAllocationDate();
                LocalDate localDate = new java.sql.Date(oldDate.getTime()).toLocalDate();

                newAllocationWithDateOnlyData.setAllocationDate(java.sql.Date.valueOf(localDate));

                allocationDataWithDateOnly.add(newAllocationWithDateOnlyData);
            }
            allocationDataWithDateOnly.sort(Comparator.comparing(AllResourceActiveAllocation::getAllocationDate));
        }
        else{
            allocationDataWithDateOnly = null;
        }
        return allocationDataWithDateOnly;
    }*/

    public GetAllocationDataByMapId getAllocationDataByMapId(Integer mapId){
        Optional<ProjectResourceMapping> projectResourceMapping = projectResourceMappingRepo.findById(mapId);

        if(!projectResourceMapping.isPresent()){
            return null;
        }

        List<AllResourceActiveAllocation> allocationData = projectResourceMappingRepo.getAllocationDataByMapId(mapId);

        Map<LocalDate, AllResourceActiveAllocation> allocationDateMap = new HashMap<>();

        if(allocationData!=null && !allocationData.isEmpty()){
            for(AllResourceActiveAllocation eachAllocation: allocationData){
                AllResourceActiveAllocation newAllocationWithDateOnlyData = new AllResourceActiveAllocation();
                newAllocationWithDateOnlyData.setId(eachAllocation.getId());
                newAllocationWithDateOnlyData.setMapId(eachAllocation.getMapId());
                newAllocationWithDateOnlyData.setFte(eachAllocation.getFte());

                Date oldDate = eachAllocation.getAllocationDate();
                LocalDate localDate = new java.sql.Date(oldDate.getTime()).toLocalDate();

                newAllocationWithDateOnlyData.setAllocationDate(java.sql.Date.valueOf(localDate));

                allocationDateMap.put(localDate, newAllocationWithDateOnlyData);
            }
        }
        else{
            return null;
        }

        Date startDate = projectResourceMapping.get().getAllocationStartDate();
        Date endDate = projectResourceMapping.get().getAllocationEndDate();
        LocalDate localStartDate = new java.sql.Date(startDate.getTime()).toLocalDate();
        LocalDate localEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();

        List<AllResourceActiveAllocation> allocationDailyData = new ArrayList<>();
        List<AllocationDataByWeek> allocationDataByWeek = new ArrayList<>();
        List<AllocationDataByMonth> allocationDataByMonths = new ArrayList<>();

        List<AllResourceActiveAllocation> allocationWeeklyData = new ArrayList<>();
        List<AllResourceActiveAllocation> allocationMonthlyData = new ArrayList<>();

        LocalDate monthStartDate = localStartDate.withDayOfMonth(1);
        LocalDate monthEndDate = localStartDate.withDayOfMonth(localStartDate.lengthOfMonth());
        LocalDate weekStartDate = localStartDate.with(DayOfWeek.MONDAY);
        LocalDate weekEndDate = localStartDate.with(DayOfWeek.SUNDAY);

        Double weekSum = 0d, monthSum = 0d;

        Integer noOfWeekDays = 0, noOfMonthDays = 0;

        while(!localStartDate.isAfter(localEndDate)){

            if(localStartDate.getDayOfWeek()!=DayOfWeek.SATURDAY && localStartDate.getDayOfWeek()!=DayOfWeek.SUNDAY) {
                if(localStartDate.isAfter(weekEndDate)){
                    AllocationDataByWeek thisWeekData = new AllocationDataByWeek();
                    thisWeekData.setWeek(java.sql.Date.valueOf(weekStartDate));
                    thisWeekData.setYear(weekStartDate.getYear());
                    if(noOfWeekDays==0d){
                        thisWeekData.setAverageToShow(0d);
                    }
                    else{
                        thisWeekData.setAverageToShow(ResourceUtils.roundToTwoDecimalPlace(weekSum/noOfWeekDays));
                    }
                    thisWeekData.setThisWeekAllocationData(allocationWeeklyData);
                    allocationDataByWeek.add(thisWeekData);

    //                reset values
                    weekSum = 0d;
                    noOfWeekDays = 0;
                    weekStartDate = localStartDate;
                    weekEndDate = localStartDate.with(DayOfWeek.SUNDAY);
                    allocationWeeklyData = new ArrayList<>();
                }

                if(localStartDate.isAfter(monthEndDate)){
                    AllocationDataByMonth thisMonthData = new AllocationDataByMonth();
                    thisMonthData.setMonth(monthStartDate.getMonth().toString());
                    thisMonthData.setYear(monthStartDate.getYear());
                    if(noOfMonthDays==0d){
                        thisMonthData.setAverageToShow(0d);
                    }
                    else{
                        thisMonthData.setAverageToShow(ResourceUtils.roundToTwoDecimalPlace(monthSum/noOfMonthDays));
                    }
                    thisMonthData.setThisMonthAllocationData(allocationMonthlyData);
                    allocationDataByMonths.add(thisMonthData);

    //                reset values
                    monthSum = 0d;
                    noOfMonthDays = 0;
                    monthStartDate = localStartDate;
                    monthEndDate = localStartDate.withDayOfMonth(localStartDate.lengthOfMonth());
                    allocationMonthlyData = new ArrayList<>();
                }

                AllResourceActiveAllocation currentDateData = null;
                if(allocationDateMap.containsKey(localStartDate)){
                    currentDateData = allocationDateMap.get(localStartDate);
                }
                else{
                    Date currentDate = java.sql.Date.valueOf(localStartDate);
                    currentDateData = new AllResourceActiveAllocation(null, mapId, currentDate, BigDecimal.valueOf(0d), BigDecimal.valueOf(0d));
                }
//                daily assignments
                allocationDailyData.add(currentDateData);

//                weekly assignments
                weekSum = weekSum + currentDateData.getFte().doubleValue();
                allocationWeeklyData.add(currentDateData);

//                monthly assignments
                monthSum = monthSum + currentDateData.getFte().doubleValue();
                allocationMonthlyData.add(currentDateData);
                noOfWeekDays = noOfWeekDays + 1;
                noOfMonthDays = noOfMonthDays + 1;
            }

            localStartDate = localStartDate.plusDays(1);
        }


        if (noOfMonthDays.equals(0)) {
            allocationDataByMonths.add(new AllocationDataByMonth(monthStartDate.getMonth().toString(), monthStartDate.getYear(), 0d, 0d, allocationMonthlyData));
        }
        else{
            Double averageFte = ResourceUtils.roundToTwoDecimalPlace(monthSum/noOfMonthDays);
            allocationDataByMonths.add(new AllocationDataByMonth(monthStartDate.getMonth().toString(), monthStartDate.getYear(), averageFte, 0d, allocationMonthlyData));
        }

        if (noOfWeekDays.equals(0)) {
            Date weekDate = java.sql.Date.valueOf(weekStartDate);
            allocationDataByWeek.add(new AllocationDataByWeek(weekDate, weekStartDate.getYear(), 0d, 0d, allocationWeeklyData));
        }
        else{
            Double averageFte = ResourceUtils.roundToTwoDecimalPlace(weekSum/noOfWeekDays);
            Date weekDate = java.sql.Date.valueOf(weekStartDate);
            allocationDataByWeek.add(new AllocationDataByWeek(weekDate, weekStartDate.getYear(), averageFte, 0d, allocationWeeklyData));
        }

        return new GetAllocationDataByMapId(allocationDailyData, allocationDataByWeek, allocationDataByMonths);
    }

    public GetAllocationLimitsForResourceId getResourceAllocationDataLimitsByMapId(Integer resourceId, Integer mapId, GetDateRange getDateRange){

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }

        Optional<ResourceMgmt> resourceMgmt = resourceMgmtRepo.findById(resourceId);
        if(!resourceMgmt.isPresent()){
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
        }

//        bring status list to get max possible fte in a day
        List<ResourceMgmtStatus> resourceMgmtStatusList = resourceMgmtStatusRepo.findAll();

        double maxFteAllowed = ResourceUtils.MIN_FTE;

        for (ResourceMgmtStatus eachStatus : resourceMgmtStatusList) {
            if (eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue() !=null && eachStatus.getEndValue()!=null) {
                maxFteAllowed = (maxFteAllowed < eachStatus.getEndValue())?eachStatus.getEndValue():maxFteAllowed;
            }
        }

        if(maxFteAllowed <= ResourceUtils.MIN_FTE){
            maxFteAllowed = 1.5;
        }

//        bring all records for the resource
        List<AllResourceActiveAllocation> resourceAllActiveAllocations =
                projectResourceMappingRepo.getResourceByIdActiveAllocation(resourceId, getDateRange.getStartDate(), getDateRange.getEndDate());

//        go through all records and calculate daily allocation for those which lie in date range,
//        exclude the record for which limit is being calculated
        Map<Date, Double> dailyAllocation = new HashMap<>();
        for(AllResourceActiveAllocation eachAllocation : resourceAllActiveAllocations){

            LocalDate currentDate = new java.sql.Date(eachAllocation.getAllocationDate().getTime()).toLocalDate();
            LocalDate startDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
            LocalDate endDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();

            if((!Objects.equals(eachAllocation.getMapId(), mapId)) && !(currentDate.isAfter(endDate)) && !(currentDate.isBefore(startDate))){
//                checks if key is present, if no, adds it, if yes, updates it
                dailyAllocation.compute(eachAllocation.getAllocationDate(), (k, v) -> v == null ? eachAllocation.getFte().doubleValue() : v + eachAllocation.getFte().doubleValue());
            }
        }
        List<MonthAllocationLimit> monthlyLimit = new ArrayList<>();
        List<WeeklyAllocationLimit> weeklyLimit = new ArrayList<>();
        List<DailyAllocationLimit> dailyLimit = new ArrayList<>();

        List<GetDateRange> dateRangeList = createDateRangeList(getDateRange);
        assert dateRangeList != null;

        Date dateOfJoin = resourceMgmt.get().getDateOfJoin();
        Date lastWorkingDate = resourceMgmt.get().getLastWorkingDate();

        String resourceProjectMapping = ResourceUtils.getFile("MonthlyMaxFteCount.txt");
        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(MonthlyMaxFteCount.class));

        query.setParameter("resourceId", resourceId);
        query.setParameter("mapId", mapId);

        for(GetDateRange eachRange : dateRangeList){

            Date startDate = eachRange.getStartDate().compareTo(getDateRange.getStartDate())<0?getDateRange.getStartDate():eachRange.getStartDate();
            Date endDate = eachRange.getEndDate().compareTo(getDateRange.getEndDate())>0?getDateRange.getEndDate():eachRange.getEndDate();

            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<MonthlyMaxFteCount> monthlyAllocatedTillNow = query.getResultList();

            MonthlyMaxFteCount data = monthlyAllocatedTillNow.get(0);
            boolean isFteNull = false;
            if(data.getMax_fte()==null){
                isFteNull = true;
                data.setMax_fte(BigDecimal.valueOf(0d));
            }
//            Date startDate = eachRange.getStartDate();
//            Date endDate = eachRange.getEndDate();

            if(dateOfJoin.compareTo(startDate)>0){
                startDate = dateOfJoin;
            }
            if(lastWorkingDate!=null && lastWorkingDate.compareTo(endDate)<0){
                endDate = lastWorkingDate;
            }

            LocalDate localStartDate = new java.sql.Date(startDate.getTime()).toLocalDate();
            LocalDate localEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();
            Integer workingDays = ResourceUtils.numberOfWorkingDays(localStartDate, localEndDate);
            Double monthlyMaxAllowed = maxFteAllowed - data.getMax_fte().doubleValue();
            monthlyMaxAllowed = ResourceUtils.roundToTwoDecimalPlace(monthlyMaxAllowed>0?monthlyMaxAllowed:0);
            if(isFteNull) {
                monthlyLimit.add(new MonthAllocationLimit(localStartDate.getMonth().toString(), localStartDate.getYear(),
                        monthlyMaxAllowed, data.getMax_fte().doubleValue(), Integer.valueOf(workingDays), workingDays.doubleValue()));
            }
            else{
                monthlyLimit.add(new MonthAllocationLimit(localStartDate.getMonth().toString(), localStartDate.getYear(),
                        monthlyMaxAllowed, data.getMax_fte().doubleValue(), data.getCount_of_max_fte(), workingDays.doubleValue()));
            }
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

        LocalDate weekStartDate = responseStartDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        Double maxFteAllocated = 0d;
        Integer numberOfDays = 0;
        while(!responseStartDate.isAfter(responseEndDate)){

            Date currentDate = java.sql.Date.valueOf(responseStartDate);
            Double currentAllocationPossible = dailyAllocation.get(currentDate);
            if(currentAllocationPossible==null){
                currentAllocationPossible = 0d;
            }
            if(responseStartDate.getDayOfWeek()!= DayOfWeek.SATURDAY && responseStartDate.getDayOfWeek()!= DayOfWeek.SUNDAY){
                if(responseStartDate.getDayOfWeek() == DayOfWeek.MONDAY){
                    weekStartDate = responseStartDate;
                }
                dailyLimit.add(new DailyAllocationLimit(currentDate, ResourceUtils.roundToTwoDecimalPlace(maxFteAllowed - currentAllocationPossible)));
                if(currentAllocationPossible>maxFteAllocated){
                    maxFteAllocated = currentAllocationPossible;
                    numberOfDays=1;
                }
                else if(currentAllocationPossible.equals(maxFteAllocated)){
                    numberOfDays++;
                }
            }
            else if(responseStartDate.getDayOfWeek() == DayOfWeek.SATURDAY){
                Date weekStart = java.sql.Date.valueOf(weekStartDate);
                Double weekAllocationLimit = maxFteAllowed - maxFteAllocated;
                weekAllocationLimit = ResourceUtils.roundToTwoDecimalPlace(weekAllocationLimit);
                weeklyLimit.add(new WeeklyAllocationLimit(weekStart, responseStartDate.getYear(), weekAllocationLimit, maxFteAllocated, numberOfDays));
                maxFteAllocated = 0d;
                numberOfDays = 0;
            }
            responseStartDate = responseStartDate.plusDays(1);
        }
        if(responseEndDate.getDayOfWeek()!= DayOfWeek.SATURDAY && responseEndDate.getDayOfWeek()!= DayOfWeek.SUNDAY){
            Date weekStart = java.sql.Date.valueOf(weekStartDate);
            Double weekAllocationLimit = maxFteAllowed - maxFteAllocated;
            weekAllocationLimit = ResourceUtils.roundToTwoDecimalPlace(weekAllocationLimit);
            weeklyLimit.add(new WeeklyAllocationLimit(weekStart, responseStartDate.getYear(), weekAllocationLimit, maxFteAllocated, numberOfDays));
        }

        return new GetAllocationLimitsForResourceId(monthlyLimit, weeklyLimit, dailyLimit);
    }

    /*public void createUpdateProjectResourceAllocation(PostResourceAllocationMatrix postResourceAllocationMatrix){

//        check if data is present, if yes, then is resource id is present
        if (postResourceAllocationMatrix != null && postResourceAllocationMatrix.getResourceId()!=null) {


            Optional<ResourceMgmt> resourceData = resourceMgmtRepo.findById(postResourceAllocationMatrix.getResourceId());

            List<PostResourceAllProjectData> allocationData = postResourceAllocationMatrix.getAllocationData();

//             is resource id valid & if allocation data is present?
            if(resourceData.isPresent() && allocationData!=null && !allocationData.isEmpty()){

//                traverse each allocation data
                for(PostResourceAllProjectData eachAllocation : allocationData){


                    Optional<ProjectMgmt> projectData = projectMgmtRepo.findById(eachAllocation.getProjectId());

//                    check if project id exists and if allocation start date is after allocation end date
                    if(projectData.isPresent() && !ResourceUtils.isEndDateBeforeStartDate(eachAllocation.getAllocationStartDate(), eachAllocation.getAllocationEndDate())) {

//                        if no map id (new record)
                        if (eachAllocation.getMapId() == null) {
                            eachAllocation.setMapId(0);
                        }

//                        check if map Id exists
                        Optional<ProjectResourceMapping> storedAllocation = projectResourceMappingRepo.findById(eachAllocation.getMapId());

                        ProjectResourceMapping newAllocationData = new ProjectResourceMapping();
                        if (!storedAllocation.isPresent()) {
                            eachAllocation.setMapId(null);
                            newAllocationData.setRoleId("");
                            newAllocationData.setDescription("");
                            newAllocationData.setCreatedBy(postResourceAllocationMatrix.getCreatedBy());
                            newAllocationData.setCreatedDate(new Date());
                            try {
                                newAllocationData.setRequestedFte(BigDecimal.valueOf(eachAllocation.getFteRequested()));
                            }
                            catch(Exception e){
                                newAllocationData.setRequestedFte(BigDecimal.valueOf(0d));
                            }
                        }
                        else{
                            newAllocationData.setRequestedFte(storedAllocation.get().getRequestedFte());
                            newAllocationData.setRoleId(storedAllocation.get().getRoleId());
                            newAllocationData.setDescription(storedAllocation.get().getDescription());
                            newAllocationData.setCreatedBy(storedAllocation.get().getCreatedBy());
                            newAllocationData.setCreatedDate(storedAllocation.get().getCreatedDate());
                        }

//                        add data
                        newAllocationData.setMapId(eachAllocation.getMapId());
                        newAllocationData.setResourceId(postResourceAllocationMatrix.getResourceId());
                        newAllocationData.setProjectId(eachAllocation.getProjectId());
                        newAllocationData.setAllocationStartDate(eachAllocation.getAllocationStartDate());
                        newAllocationData.setAllocationEndDate(eachAllocation.getAllocationEndDate());
                        newAllocationData.setModifiedBy(postResourceAllocationMatrix.getModifiedBy());
                        newAllocationData.setModifiedDate(postResourceAllocationMatrix.getModifiedDate());

                        newAllocationData.setIsDeleted(false);

//                        save data, in case new allocation, we need map id for after this
                        newAllocationData = projectResourceMappingRepo.save(newAllocationData);

                        List<AllResourceActiveAllocation> thisAllocationDailyData = eachAllocation.getAllocations();

                        List<ProjectResourceMappingExtended> allocationsToSave = new ArrayList<>();

                        boolean isInsideDateRangeRecordNotPresent = true;
//                        traverse allocation data
                        for(AllResourceActiveAllocation eachDateAllocationData: thisAllocationDailyData){

                            LocalDate currentDate = new java.sql.Date(eachDateAllocationData.getAllocationDate().getTime()).toLocalDate();

//                            check if date is weekend, if yes, don't save
                            if((currentDate.getDayOfWeek()!=DayOfWeek.SATURDAY) && (currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY)) {

//                                if the record date lies outside the allocation date range, make it zero
                                if ((eachDateAllocationData.getAllocationDate().compareTo(newAllocationData.getAllocationEndDate()) <= 0)
                                        && (eachDateAllocationData.getAllocationDate().compareTo(newAllocationData.getAllocationStartDate()) >= 0)) {
                                    if(eachDateAllocationData.getMapId()==null){
                                        eachDateAllocationData.setMapId(newAllocationData.getMapId());
                                    }
                                    if(Objects.equals(eachDateAllocationData.getMapId(), newAllocationData.getMapId())){
                                        if(isInsideDateRangeRecordNotPresent) {
                                            isInsideDateRangeRecordNotPresent = false;
                                        }
                                        allocationsToSave.add(new ProjectResourceMappingExtended(eachDateAllocationData));
                                    }
                                }
                                else{
                                    if(eachDateAllocationData.getId()!=null && Objects.equals(eachDateAllocationData.getMapId(), newAllocationData.getMapId())){
                                        eachDateAllocationData.setFte(BigDecimal.valueOf(0d));
                                        allocationsToSave.add(new ProjectResourceMappingExtended(eachDateAllocationData));
                                    }
                                }
                            }
                        }

                        if(isInsideDateRangeRecordNotPresent){
                            LocalDate localStartDate = new java.sql.Date(newAllocationData.getAllocationStartDate().getTime()).toLocalDate();
                            LocalDate localEndDate = new java.sql.Date(newAllocationData.getAllocationEndDate().getTime()).toLocalDate();
                            while(!localStartDate.isAfter(localEndDate)){
                                if(localStartDate.getDayOfWeek()!= DayOfWeek.SATURDAY && localStartDate.getDayOfWeek()!= DayOfWeek.SUNDAY) {
                                    Date currentDate = java.sql.Date.valueOf(localStartDate);
                                    ProjectResourceMappingExtended newAllocation = new ProjectResourceMappingExtended(null, newAllocationData.getMapId(), currentDate, BigDecimal.valueOf(0d));
                                    allocationsToSave.add(newAllocation);
                                }
                                localStartDate = localStartDate.plusDays(1);
                            }
                        }

                        projectResourceMappingExtendedRepo.saveAll(allocationsToSave);
                    }
                }
            }
        }
    }*/

    public Object createUpdateProjectResourceAllocation(PostAllocationDataByMapId postAllocationDataByMapId, Integer mapId, String allocationFrequency, String modifiedBy){

        Optional<ProjectResourceMapping> projectResourceMapping = projectResourceMappingRepo.findById(mapId);
        if(!projectResourceMapping.isPresent()){
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Allocation Not Found");
        }

        List<AllResourceActiveAllocation> thisAllocationDailyData = new ArrayList<>();
//        extract the value which was updated by user in frontend
        if(allocationFrequency.equals(ResourceUtils.ALLOCATION_FREQUENCY_DAILY)){
            thisAllocationDailyData.addAll(postAllocationDataByMapId.getDailyData());
        }
        else if(allocationFrequency.equals(ResourceUtils.ALLOCATION_FREQUENCY_WEEKLY)){

            List<AllocationDataByWeek> weeklyData = postAllocationDataByMapId.getWeeklyAllocationData();

            for(AllocationDataByWeek eachWeekData : weeklyData){
                thisAllocationDailyData.addAll(eachWeekData.getThisWeekAllocationData());
            }
        }
        else if(allocationFrequency.equals(ResourceUtils.ALLOCATION_FREQUENCY_MONTHLY)){

            List<AllocationDataByMonth> monthlyData = postAllocationDataByMapId.getMonthlyAllocationData();

            for(AllocationDataByMonth eachMonthData : monthlyData){
                thisAllocationDailyData.addAll(eachMonthData.getThisMonthAllocationData());
            }
        }
        else{
            String message = ResourceUtils.ALLOCATION_FREQUENCY_INVALID;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

//             is resource id valid & if allocation data is present?
        if(!thisAllocationDailyData.isEmpty()){

            ProjectResourceMapping currentAllocationRecord  = projectResourceMapping.get();
            currentAllocationRecord.setModifiedBy(modifiedBy);
            currentAllocationRecord.setModifiedDate(new Date());
            currentAllocationRecord = projectResourceMappingRepo.save(currentAllocationRecord);

//            convert the allocation dates to remove time zone
            Date allocationStartDate = currentAllocationRecord.getAllocationStartDate();
            Date allocationEndDate = currentAllocationRecord.getAllocationEndDate();

            LocalDate localAllocationStartDate = new java.sql.Date(allocationStartDate.getTime()).toLocalDate();
            LocalDate localAllocationEndDate = new java.sql.Date(allocationEndDate.getTime()).toLocalDate();

            allocationStartDate = java.sql.Date.valueOf(localAllocationStartDate);
            allocationEndDate = java.sql.Date.valueOf(localAllocationEndDate);

//            list to store data to be saved
            List<ProjectResourceMappingExtended> allocationsToSave = new ArrayList<>();

            boolean isInsideDateRangeRecordNotPresent = true;

//            traverse allocation data
            for(AllResourceActiveAllocation eachDateAllocationData: thisAllocationDailyData){

                LocalDate localCurrentDate = new java.sql.Date(eachDateAllocationData.getAllocationDate().getTime()).toLocalDate();
                Date currentDate = java.sql.Date.valueOf(localCurrentDate);

//                            check if date is weekend, if yes, don't save
                if((localCurrentDate.getDayOfWeek()!=DayOfWeek.SATURDAY) && (localCurrentDate.getDayOfWeek()!=DayOfWeek.SUNDAY)) {

//                                if the record date lies outside the allocation date range, make it zero
                    if ((currentDate.compareTo(allocationEndDate) <= 0) && (currentDate.compareTo(allocationStartDate) >= 0)) {
                        if(eachDateAllocationData.getMapId()==null){
                            eachDateAllocationData.setMapId(currentAllocationRecord.getMapId());
                        }
                        if(Objects.equals(eachDateAllocationData.getMapId(), currentAllocationRecord.getMapId())){
                            if(isInsideDateRangeRecordNotPresent) {
                                isInsideDateRangeRecordNotPresent = false;
                            }
                            allocationsToSave.add(new ProjectResourceMappingExtended(eachDateAllocationData));
                        }
                    }
                    else{
                        if(eachDateAllocationData.getId()!=null && Objects.equals(eachDateAllocationData.getMapId(), currentAllocationRecord.getMapId())){
                            eachDateAllocationData.setFte(BigDecimal.valueOf(0d));
                            allocationsToSave.add(new ProjectResourceMappingExtended(eachDateAllocationData));
                        }
                    }
                }
            }

            if(isInsideDateRangeRecordNotPresent){
                while(!localAllocationStartDate.isAfter(localAllocationEndDate)){
                    if(localAllocationStartDate.getDayOfWeek()!= DayOfWeek.SATURDAY && localAllocationStartDate.getDayOfWeek()!= DayOfWeek.SUNDAY) {
                        Date currentDate = java.sql.Date.valueOf(localAllocationStartDate);
                        ProjectResourceMappingExtended newAllocation = new ProjectResourceMappingExtended(null, currentAllocationRecord.getMapId(), currentDate, BigDecimal.valueOf(0d), currentAllocationRecord.getAllocatedFteAvg());
                        allocationsToSave.add(newAllocation);
                    }
                    localAllocationStartDate = localAllocationStartDate.plusDays(1);
                }
            }

            projectResourceMappingExtendedRepo.saveAll(allocationsToSave);

            return true;
        }
        else{
            return false;
        }
    }

    public List<GetResourceAllocationChart> getDepartmentAllResourceAllocationsChart(Integer resourceId, Integer financialYear, String email, String role){

        Optional<ResourceMgmt> resourceDetail = resourceMgmtRepo.findById(resourceId);
        if (!resourceDetail.isPresent()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
        }

//        generate start date and end date from financial year
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month financialYearStartMonth = Month.valueOf(startMonth.toUpperCase());

        LocalDate responseLocalStartDate= null, responseLocalEndDate= null;
        responseLocalStartDate = LocalDate.of(financialYear, financialYearStartMonth, startDateOfMonth);
        responseLocalEndDate = responseLocalStartDate.plusYears(1).minusDays(1);


        Date responseStartDate = java.sql.Date.valueOf(responseLocalStartDate);
        Date responseEndDate = java.sql.Date.valueOf(responseLocalEndDate);

        GetDateRange getDateRange = new GetDateRange(responseStartDate, responseEndDate);

//        get maximum allowed FTE by admin
        List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
        Double maxEnabledFteInAdmin = ResourceUtils.MIN_FTE;

        for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
            if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                    maxEnabledFteInAdmin = eachStatus.getEndValue();
                }
            }
        }

//        find the department id of the resource id sent by frontend
        DepartmentStringIdName departmentIdName = resourceMgmtRepo.getResourceDepartmentId(resourceId);


//        query database to bring records for all resource
        String resourceProjectMapping = ResourceUtils.getFile("getResourceDepartmentAllResourceProjectAllocationDataQuery.txt");

        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllProjectAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("departmentId", departmentIdName.getDepartmentId());

        List<Integer> visibleResourceIds = getVisibleResourceIds(email, role);
        query.setParameter("resourceIds", visibleResourceIds);

//        get all allocation data
        List<ResourceAllProjectAllocationData> resourceAllProjectAllocationData = query.getResultList();

//        group records by resource I'd
        Map<Integer, List<ResourceAllProjectAllocationData>> groupByResourceId
                = resourceAllProjectAllocationData.stream().collect(Collectors.groupingBy(ResourceAllProjectAllocationData::getResource_id));

        List<GetResourceAllocationChart> getResourceAllocationChartList = new ArrayList<>();

//        for each resource
        for(Map.Entry<Integer, List<ResourceAllProjectAllocationData>> entry: groupByResourceId.entrySet()){

//            set resource date of join and last working date
            LocalDate dateOfJoin = null, lastWorkingDate = null;

            if(entry.getValue().get(0).getDate_of_join()!=null) {
                dateOfJoin = new java.sql.Date(entry.getValue().get(0).getDate_of_join().getTime()).toLocalDate();
            }
            if(entry.getValue().get(0).getLast_working_date()!=null) {
                lastWorkingDate = new java.sql.Date(entry.getValue().get(0).getLast_working_date().getTime()).toLocalDate();
            }

//            calculate data
            GetResourceAllocationChart eachResourceData
                    = getDepartmentAllResourceAllocationsChartHelper(entry.getKey(), entry.getValue().get(0).getResource_name(), responseLocalStartDate, responseLocalEndDate, dateOfJoin, lastWorkingDate,
                    maxEnabledFteInAdmin, entry.getValue(), getDateRange, departmentIdName.getDepartmentName());

//            if data is there, add to list
            if(eachResourceData!=null){
                getResourceAllocationChartList.add(eachResourceData);
            }
        }

        if(getResourceAllocationChartList.isEmpty()){
            getResourceAllocationChartList = null;
        }

        return getResourceAllocationChartList;
    }

    private GetResourceAllocationChart getDepartmentAllResourceAllocationsChartHelper(Integer resourceId, String resourceName, LocalDate responseLocalStartDate, LocalDate responseLocalEndDate,
                                                                                      LocalDate dateOfJoin, LocalDate lastWorkingDate, Double maxEnabledFteInAdmin,
                                                                                      List<ResourceAllProjectAllocationData> resourceAllProjectAllocationData,
                                                                                      GetDateRange getDateRange, String departmentName){

        Date requestStartDate = getDateRange.getStartDate();
        Date requestEndDate = getDateRange.getEndDate();

        GetResourceAllocationChart chartData = null;

        List<GanttChartData> ganttChartData = new ArrayList<>();

        if(resourceAllProjectAllocationData!=null && !resourceAllProjectAllocationData.isEmpty()) {
//        group them by project id
            Map<Integer, List<ResourceAllProjectAllocationData>> groupByProject
                    = resourceAllProjectAllocationData.stream().collect(Collectors.groupingBy(ResourceAllProjectAllocationData::getProject_id));

//        for each project
            for (Map.Entry<Integer, List<ResourceAllProjectAllocationData>> entry : groupByProject.entrySet()) {

                List<ResourceAllProjectAllocationData> thisProjectAllocations = entry.getValue();
//            if allocations are not empty
                if (thisProjectAllocations != null && !thisProjectAllocations.isEmpty()) {

//                CRITICAL PART STARTS
//                the part from here is to ensure that the allocations in the list are complete,
//                that is, no such case is present where a record of a date, which lie in its allocation date range, is not present
                    Map<Integer, ResourceAllProjectAllocationData> allUniqueMapIds = new HashMap<>();
                    HashSet<LocalDate> localDateHashSet = new HashSet<>();

//                keep track of all the dates present, as well as every allocation date range
                    for (ResourceAllProjectAllocationData eachDailyAllocation : thisProjectAllocations) {
                        LocalDate allocationDate;

                        allocationDate = new java.sql.Date(eachDailyAllocation.getAllocation_date().getTime()).toLocalDate();
                        allUniqueMapIds.putIfAbsent(eachDailyAllocation.getMap_id(), eachDailyAllocation);
                        localDateHashSet.add(allocationDate);
                    }

//                for every allocation date range for resource on this project
                    for (Map.Entry<Integer, ResourceAllProjectAllocationData> allocationDates : allUniqueMapIds.entrySet()) {
                        LocalDate allocationStartDate, allocationEndDate, currentDate;

                        allocationStartDate = new java.sql.Date(allocationDates.getValue().getAllocation_start_dt().getTime()).toLocalDate();
                        allocationEndDate = new java.sql.Date(allocationDates.getValue().getAllocation_end_dt().getTime()).toLocalDate();
                        currentDate = allocationStartDate;

//                    check if all the dates in this date range are present? if not, create them, save them, and add the saved record to the list
                        while (!currentDate.isAfter(allocationEndDate)) {

                            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                                if (!localDateHashSet.contains(currentDate)) {

                                    ProjectResourceMappingExtended missingRecordToSave = new ProjectResourceMappingExtended();
                                    missingRecordToSave.setMapId(allocationDates.getValue().getMap_id());
                                    missingRecordToSave.setAllocationDate(java.sql.Date.valueOf(currentDate));
                                    missingRecordToSave.setFte(BigDecimal.valueOf(0d));
                                    missingRecordToSave.setRequestedFte(BigDecimal.valueOf(0d));
                                    missingRecordToSave = projectResourceMappingExtendedRepo.save(missingRecordToSave);

                                    ResourceAllProjectAllocationData newRecordCreated = new ResourceAllProjectAllocationData(allocationDates.getValue());
                                    newRecordCreated.setId(missingRecordToSave.getId());
                                    newRecordCreated.setAllocation_date(missingRecordToSave.getAllocationDate());
                                    newRecordCreated.setFte(missingRecordToSave.getFte());
                                    newRecordCreated.setRequested_fte(missingRecordToSave.getRequestedFte());

                                    thisProjectAllocations.add(newRecordCreated);
                                }
                            }

                            currentDate = currentDate.plusDays(1);
                        }
                    }

//                CRITICAL PART ENDS

                    GanttChartData thisProjectData = new GanttChartData();
                    Map<LocalDate, LocalDate> allAllocatedDateRanges = new HashMap<>();
//                Map<LocalDate, Double> allRequestedFte = new HashMap<>();

                    Map<LocalDate, Double> weekAllocatedFteSumTracker = new HashMap<>();
                    Map<LocalDate, Double> weekRequestedFteSumTracker = new HashMap<>();
                    Map<LocalDate, List<AllResourceActiveAllocation>> dailyRecordsByWeekStartDate = new HashMap<>();

                    Map<LocalDate, Double> monthAllocatedFteSumTracker = new HashMap<>();
                    Map<LocalDate, Double> monthRequestedFteSumTracker = new HashMap<>();
                    Map<LocalDate, List<AllResourceActiveAllocation>> dailyRecordsByMonthStartDate = new HashMap<>();

//                sort
                    thisProjectAllocations.sort(Comparator.comparing(ResourceAllProjectAllocationData::getAllocation_date));

                    thisProjectData.setProjectId(thisProjectAllocations.get(0).getProject_id());
                    thisProjectData.setProjectName(thisProjectAllocations.get(0).getProject_name());

                    Date startDate = thisProjectAllocations.get(0).getAllocation_start_dt();
                    Date endDate = thisProjectAllocations.get(0).getAllocation_end_dt();

//                set each allocation
                    List<AllResourceActiveAllocation> allResourceActiveAllocations = new ArrayList<>();

//                to collect list of dates which lie in weeks which are not part of current financial year
//                but are part of financial year first week or last week
                    List<LocalDate> missingDatesInWeek = new ArrayList<>();

//                remove all the records which is beyond what is asked for by frontend
                    for (ResourceAllProjectAllocationData eachDailyAllocation : thisProjectAllocations) {

                        LocalDate allocStart, allocEnd;
                        allocStart = new java.sql.Date(eachDailyAllocation.getAllocation_start_dt().getTime()).toLocalDate();
                        allocEnd = new java.sql.Date(eachDailyAllocation.getAllocation_end_dt().getTime()).toLocalDate();

//                    take note of all the allocation date range the requested fte for those allocations
                        if (!allocStart.isAfter(responseLocalEndDate) && !allocEnd.isBefore(responseLocalStartDate)) {
                            allAllocatedDateRanges.putIfAbsent(allocStart, allocEnd);
//                        allRequestedFte.putIfAbsent(allocStart, eachDailyAllocation.getRequested_fte().doubleValue());
                        }

                        AllResourceActiveAllocation eachAllocationData = new AllResourceActiveAllocation(eachDailyAllocation);

//                    if current record is part of financial year
                        boolean inResponseDateRange = eachAllocationData.getAllocationDate().compareTo(getDateRange.getStartDate()) >= 0
                                && eachAllocationData.getAllocationDate().compareTo(getDateRange.getEndDate()) <= 0;

//                    in cases where financial year start or end is in middle of the week,
//                    we need the remaining week data to show correct Average Allocation FTE data for weekly allocation,
//                    hence, we are retrieving the first week and last week missing data, if any

                        Boolean isRecordPartOfFirstWeek = false, isRecordPartOfLastWeek = false;

//                    check if the financial start date lies in middle of week?
                        if (responseLocalStartDate.getDayOfWeek() != DayOfWeek.MONDAY && responseLocalStartDate.getDayOfWeek() != DayOfWeek.SATURDAY && responseLocalStartDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
//                        get dates which are part of the first financial week but not financial year
                            Date missingFirstWeekStartDate = java.sql.Date.valueOf(responseLocalStartDate.with(DayOfWeek.MONDAY));
                            Date missingFirstWeekEndDate = java.sql.Date.valueOf(responseLocalStartDate.minusDays(1));

//                        if current record lies in above dates
                            if (eachAllocationData.getAllocationDate().compareTo(missingFirstWeekStartDate) >= 0
                                    && eachAllocationData.getAllocationDate().compareTo(missingFirstWeekEndDate) <= 0) {
                                isRecordPartOfFirstWeek = true;
                            }
                        }
                        if (responseLocalEndDate.getDayOfWeek() != DayOfWeek.FRIDAY && responseLocalEndDate.getDayOfWeek() != DayOfWeek.SATURDAY && responseLocalEndDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
//                        get dates which are part of the last financial week but not financial year
                            Date missingLastWeekStartDate = java.sql.Date.valueOf(responseLocalEndDate.plusDays(1));
                            Date missingLastWeekEndDate = java.sql.Date.valueOf(responseLocalEndDate.with(DayOfWeek.FRIDAY));

//                        if current record lies in above dates
                            if (eachAllocationData.getAllocationDate().compareTo(missingLastWeekStartDate) >= 0
                                    && eachAllocationData.getAllocationDate().compareTo(missingLastWeekEndDate) <= 0) {
                                isRecordPartOfLastWeek = true;
                            }
                        }

                        if (inResponseDateRange) {
                            allResourceActiveAllocations.add(eachAllocationData);

                            List<AllResourceActiveAllocation> dailyRecordByDate;

//                        week data
                            LocalDate currentWeekStartDate = new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate();
                            currentWeekStartDate = currentWeekStartDate.with(DayOfWeek.MONDAY);
                            weekAllocatedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getFte().doubleValue(), Double::sum);
                            weekRequestedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getRequestedFte().doubleValue(), Double::sum);

                            if (dailyRecordsByWeekStartDate.containsKey(currentWeekStartDate)) {
                                dailyRecordByDate = dailyRecordsByWeekStartDate.get(currentWeekStartDate);
                            } else {
                                dailyRecordByDate = new ArrayList<>();
                            }
                            dailyRecordByDate.add(eachAllocationData);
                            dailyRecordsByWeekStartDate.put(currentWeekStartDate, dailyRecordByDate);


//                        month data
                            LocalDate currentMonthStartDate = new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate();
                            currentMonthStartDate = currentMonthStartDate.withDayOfMonth(1);
                            monthAllocatedFteSumTracker.merge(currentMonthStartDate, eachAllocationData.getFte().doubleValue(), Double::sum);
                            monthRequestedFteSumTracker.merge(currentMonthStartDate, eachAllocationData.getRequestedFte().doubleValue(), Double::sum);

                            if (dailyRecordsByMonthStartDate.containsKey(currentMonthStartDate)) {
                                dailyRecordByDate = dailyRecordsByMonthStartDate.get(currentMonthStartDate);
                            } else {
                                dailyRecordByDate = new ArrayList<>();
                            }
                            dailyRecordByDate.add(eachAllocationData);
                            dailyRecordsByMonthStartDate.put(currentMonthStartDate, dailyRecordByDate);

                        }
                        if (isRecordPartOfFirstWeek) {
                            List<AllResourceActiveAllocation> dailyRecordByDate;
//                        week data
                            LocalDate currentWeekStartDate = new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate();
                            currentWeekStartDate = currentWeekStartDate.with(DayOfWeek.MONDAY);
                            weekAllocatedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getFte().doubleValue(), Double::sum);
                            weekRequestedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getRequestedFte().doubleValue(), Double::sum);

                            if (dailyRecordsByWeekStartDate.containsKey(currentWeekStartDate)) {
                                dailyRecordByDate = dailyRecordsByWeekStartDate.get(currentWeekStartDate);
                            } else {
                                dailyRecordByDate = new ArrayList<>();
                            }
                            dailyRecordByDate.add(eachAllocationData);
                            dailyRecordsByWeekStartDate.put(currentWeekStartDate, dailyRecordByDate);
                        }
                        if (isRecordPartOfLastWeek) {
                            List<AllResourceActiveAllocation> dailyRecordByDate;
//                        week data
                            LocalDate currentWeekStartDate = new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate();
                            currentWeekStartDate = currentWeekStartDate.with(DayOfWeek.MONDAY);
                            weekAllocatedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getFte().doubleValue(), Double::sum);
                            weekRequestedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getRequestedFte().doubleValue(), Double::sum);

                            if (dailyRecordsByWeekStartDate.containsKey(currentWeekStartDate)) {
                                dailyRecordByDate = dailyRecordsByWeekStartDate.get(currentWeekStartDate);
                            } else {
                                dailyRecordByDate = new ArrayList<>();
                            }
                            dailyRecordByDate.add(eachAllocationData);
                            dailyRecordsByWeekStartDate.put(currentWeekStartDate, dailyRecordByDate);
                        }
                        if (isRecordPartOfFirstWeek || isRecordPartOfLastWeek) {
                            missingDatesInWeek.add(new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate());
                        }


//                    check for this resource's earliest allocation on this project
                        if (startDate.compareTo(eachDailyAllocation.getAllocation_start_dt()) > 0) {
                            startDate = eachDailyAllocation.getAllocation_start_dt();
                        }

//                    check for this resource's last allocation date on this project
                        if (endDate.compareTo(eachDailyAllocation.getAllocation_end_dt()) < 0) {
                            endDate = eachDailyAllocation.getAllocation_end_dt();
                        }

                    }

                    LocalDate localStartDate, localEndDate;
                    localStartDate = new java.sql.Date(startDate.getTime()).toLocalDate();
                    localEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();

                    thisProjectData.setAllocationStartDate(java.sql.Date.valueOf(localStartDate));
                    thisProjectData.setAllocationEndDate(java.sql.Date.valueOf(localEndDate));
                    thisProjectData.setAllocationData(allResourceActiveAllocations);

                    Map<LocalDate, Integer> weekWorkingDays = new HashMap<>();
                    Map<LocalDate, Integer> monthWorkingDays = new HashMap<>();

//                weekly and monthly working days' calculation, for every allocation of resource on this project
                    for (Map.Entry<LocalDate, LocalDate> dateRange : allAllocatedDateRanges.entrySet()) {
//                    LocalDate rangeStartDate = dateRange.getKey();
//                    LocalDate rangeEndDate = dateRange.getValue();

                        LocalDate rangeStartDate = dateRange.getKey().isAfter(responseLocalStartDate) ? dateRange.getKey() : responseLocalStartDate;
                        LocalDate rangeEndDate = dateRange.getValue().isBefore(responseLocalEndDate) ? dateRange.getValue() : responseLocalEndDate;

                        while (!rangeStartDate.isAfter(rangeEndDate)) {
                            if (rangeStartDate.getDayOfWeek() != DayOfWeek.SATURDAY && rangeStartDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                                LocalDate currentWeek = rangeStartDate.with(DayOfWeek.MONDAY);
                                LocalDate currentMonth = rangeStartDate.withDayOfMonth(1);

                                weekWorkingDays.merge(currentWeek, 1, Integer::sum);
                                monthWorkingDays.merge(currentMonth, 1, Integer::sum);
                            }

                            rangeStartDate = rangeStartDate.plusDays(1);
                        }
                    }
                    for (LocalDate date : missingDatesInWeek) {
                        LocalDate weekStartDate = date.with(DayOfWeek.MONDAY);
                        weekWorkingDays.merge(weekStartDate, 1, Integer::sum);
                    }

                    List<AllocationDataByWeek> weeklyAllocations = new ArrayList<>();
                    List<AllocationDataByMonth> monthlyAllocations = new ArrayList<>();

                    for (Map.Entry<LocalDate, Integer> eachWeek : weekWorkingDays.entrySet()) {

                        Double weekAllocatedFte, weekRequestedFte;
                        LocalDate weekStart = eachWeek.getKey();

                        if (weekAllocatedFteSumTracker.containsKey(weekStart)) {
                            weekAllocatedFte = weekAllocatedFteSumTracker.get(weekStart);
                            weekAllocatedFte = ResourceUtils.roundToTwoDecimalPlace(weekAllocatedFte / eachWeek.getValue());

                            weekRequestedFte = weekRequestedFteSumTracker.get(weekStart);
                            weekRequestedFte = ResourceUtils.roundToTwoDecimalPlace(weekRequestedFte / eachWeek.getValue());
                        } else {
                            weekAllocatedFte = 0d;
                            weekRequestedFte = 0d;
                        }

                        weeklyAllocations.add(new AllocationDataByWeek(java.sql.Date.valueOf(weekStart), weekStart.getYear(), weekAllocatedFte, weekRequestedFte, dailyRecordsByWeekStartDate.get(weekStart)));

                    }

                    for (Map.Entry<LocalDate, Integer> eachMonth : monthWorkingDays.entrySet()) {

                        Double monthAllocatedFte, monthRequestedFte;
                        LocalDate month = eachMonth.getKey();

                        if (monthAllocatedFteSumTracker.containsKey(eachMonth.getKey())) {
                            monthAllocatedFte = monthAllocatedFteSumTracker.get(eachMonth.getKey());
                            monthAllocatedFte = ResourceUtils.roundToTwoDecimalPlace(monthAllocatedFte / eachMonth.getValue());

                            monthRequestedFte = monthRequestedFteSumTracker.get(eachMonth.getKey());
                            monthRequestedFte = ResourceUtils.roundToTwoDecimalPlace(monthRequestedFte / eachMonth.getValue());
                        } else {
                            monthAllocatedFte = 0d;
                            monthRequestedFte = 0d;
                        }

                        monthlyAllocations.add(new AllocationDataByMonth(month.getMonth().toString(), month.getYear(), monthAllocatedFte, monthRequestedFte, dailyRecordsByMonthStartDate.get(month)));
                    }

                    weeklyAllocations.sort(Comparator.comparing(AllocationDataByWeek::getWeek));
                    monthlyAllocations.sort(Comparator.comparing(AllocationDataByMonth::getYear).thenComparing((a, b) -> {
                        Month first = Month.valueOf(a.getMonth());
                        Month second = Month.valueOf(b.getMonth());

                        return first.compareTo(second);
                    }));

                    thisProjectData.setWeeklyAllocations(weeklyAllocations);
                    thisProjectData.setMonthlyAllocations(monthlyAllocations);
                    thisProjectData.setIsUpdated(false);

//                add data to list
                    if (!thisProjectData.getAllocationData().isEmpty()) {
                        ganttChartData.add(thisProjectData);
                        if (getDateRange.getEndDate() == null && (requestEndDate == null || requestEndDate.compareTo(thisProjectData.getAllocationEndDate()) < 0)) {
                            requestEndDate = thisProjectData.getAllocationEndDate();
                        }
                    }
                }

            }

        }

        if(!ganttChartData.isEmpty()){
            ganttChartData.sort(Comparator.comparing(GanttChartData::getProjectName, (s1, s2)->{
                String project1 = s1.toLowerCase();
                String project2 = s2.toLowerCase();
                return String.CASE_INSENSITIVE_ORDER.compare(project1, project2);
            }).thenComparing(GanttChartData::getAllocationStartDate).thenComparing(GanttChartData::getAllocationEndDate));


            List<WorkingDaysInWeek> workingDaysInWeekList = new ArrayList<>();
            List<WorkingDaysInMonth> workingDaysInMonthList = new ArrayList<>();

            LocalDate weekStart = null, monthStart = null, allocationStartDate=null, allocationEndDate=null, currentDate=null;
            allocationStartDate = new java.sql.Date(requestStartDate.getTime()).toLocalDate();
            allocationEndDate = new java.sql.Date(requestEndDate.getTime()).toLocalDate();
            currentDate = allocationStartDate.isAfter(dateOfJoin)?allocationStartDate:dateOfJoin;
            weekStart = currentDate.with(DayOfWeek.MONDAY);
            monthStart = currentDate.withDayOfMonth(1);
            Integer weekWorkingDays = 0, monthWorkingDays = 0;

            LocalDate startDate = weekStart.isAfter(dateOfJoin)?weekStart:dateOfJoin;
            weekWorkingDays = weekWorkingDays + ResourceUtils.numberOfWorkingDays(startDate, currentDate.minusDays(1));

            while(!currentDate.isAfter(allocationEndDate)){

                if(currentDate.getDayOfWeek()!=DayOfWeek.SATURDAY && currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY) {
                    weekWorkingDays++;
                    monthWorkingDays++;
                }
                currentDate = currentDate.plusDays(1);

                if(!weekStart.isEqual(currentDate.with(DayOfWeek.MONDAY))){
                    workingDaysInWeekList.add(new WorkingDaysInWeek(java.sql.Date.valueOf(weekStart) ,weekStart.getYear(), weekWorkingDays));
                    weekWorkingDays=0;
                    weekStart = currentDate.with(DayOfWeek.MONDAY);
                }
                if(!monthStart.isEqual(currentDate.withDayOfMonth(1))){
                    workingDaysInMonthList.add(new WorkingDaysInMonth(monthStart.getMonth().toString() ,monthStart.getYear(), monthWorkingDays));
                    monthWorkingDays=0;
                    monthStart = currentDate.withDayOfMonth(1);
                }

            }

            if(currentDate.isAfter(allocationEndDate)){

                LocalDate endDate = null;

                if(weekWorkingDays!=0){
                    endDate = currentDate.with(DayOfWeek.SUNDAY);
                    if(lastWorkingDate!=null){
                        if(endDate.isAfter(lastWorkingDate)){
                            endDate = lastWorkingDate;
                        }
                    }
                    weekWorkingDays = weekWorkingDays + ResourceUtils.numberOfWorkingDays(currentDate, endDate);
                    workingDaysInWeekList.add(new WorkingDaysInWeek(java.sql.Date.valueOf(weekStart) ,weekStart.getYear(), weekWorkingDays));
                }

                if(monthWorkingDays!=0){
                    endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
                    if(lastWorkingDate!=null){
                        if(endDate.isAfter(lastWorkingDate)){
                            endDate = lastWorkingDate;
                        }
                    }
                    monthWorkingDays = monthWorkingDays + ResourceUtils.numberOfWorkingDays(currentDate, endDate);
                    workingDaysInMonthList.add(new WorkingDaysInMonth(monthStart.getMonth().toString() ,monthStart.getYear(), monthWorkingDays));
                }
            }


            chartData = new GetResourceAllocationChart(resourceId, resourceName, departmentName,
                    requestStartDate, requestEndDate, workingDaysInWeekList, workingDaysInMonthList, maxEnabledFteInAdmin, ganttChartData);
        }
        else{
            List<WorkingDaysInWeek> workingDaysInWeekList = new ArrayList<>();
            List<WorkingDaysInMonth> workingDaysInMonthList = new ArrayList<>();

            LocalDate weekStart = null, monthStart = null, allocationStartDate=null, allocationEndDate=null, currentDate=null;
            allocationStartDate = new java.sql.Date(requestStartDate.getTime()).toLocalDate();
            allocationEndDate = new java.sql.Date(requestEndDate.getTime()).toLocalDate();
            currentDate = allocationStartDate.isAfter(dateOfJoin)?allocationStartDate:dateOfJoin;
            weekStart = currentDate.with(DayOfWeek.MONDAY);
            monthStart = currentDate.withDayOfMonth(1);
            Integer weekWorkingDays = 0, monthWorkingDays = 0;

            LocalDate startDate = weekStart.isAfter(dateOfJoin)?weekStart:dateOfJoin;
            weekWorkingDays = weekWorkingDays + ResourceUtils.numberOfWorkingDays(startDate, currentDate.minusDays(1));

            while(!currentDate.isAfter(allocationEndDate)){

                if(currentDate.getDayOfWeek()!=DayOfWeek.SATURDAY && currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY) {
                    weekWorkingDays++;
                    monthWorkingDays++;
                }
                currentDate = currentDate.plusDays(1);

                if(!weekStart.isEqual(currentDate.with(DayOfWeek.MONDAY))){
                    workingDaysInWeekList.add(new WorkingDaysInWeek(java.sql.Date.valueOf(weekStart) ,weekStart.getYear(), weekWorkingDays));
                    weekWorkingDays=0;
                    weekStart = currentDate.with(DayOfWeek.MONDAY);
                }
                if(!monthStart.isEqual(currentDate.withDayOfMonth(1))){
                    workingDaysInMonthList.add(new WorkingDaysInMonth(monthStart.getMonth().toString() ,monthStart.getYear(), monthWorkingDays));
                    monthWorkingDays=0;
                    monthStart = currentDate.withDayOfMonth(1);
                }

            }

            if(currentDate.isAfter(allocationEndDate)){

                LocalDate endDate = null;

                if(weekWorkingDays!=0){
                    endDate = currentDate.with(DayOfWeek.SUNDAY);
                    if(lastWorkingDate!=null){
                        if(endDate.isAfter(lastWorkingDate)){
                            endDate = lastWorkingDate;
                        }
                    }
                    weekWorkingDays = weekWorkingDays + ResourceUtils.numberOfWorkingDays(currentDate, endDate);
                    workingDaysInWeekList.add(new WorkingDaysInWeek(java.sql.Date.valueOf(weekStart) ,weekStart.getYear(), weekWorkingDays));
                }

                if(monthWorkingDays!=0){
                    endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
                    if(lastWorkingDate!=null){
                        if(endDate.isAfter(lastWorkingDate)){
                            endDate = lastWorkingDate;
                        }
                    }
                    monthWorkingDays = monthWorkingDays + ResourceUtils.numberOfWorkingDays(currentDate, endDate);
                    workingDaysInMonthList.add(new WorkingDaysInMonth(monthStart.getMonth().toString() ,monthStart.getYear(), monthWorkingDays));
                }
            }


            chartData = new GetResourceAllocationChart(resourceId, resourceName, departmentName,
                    requestStartDate, requestEndDate, workingDaysInWeekList, workingDaysInMonthList, maxEnabledFteInAdmin, new ArrayList<>());
        }

//        TODO:: add resources which does not have any allocations

        return chartData;
    }


    public GetResourceAllocationChart getResourceAllAllocationsChart(Integer resourceId, Integer financialYear){

        Optional<ResourceMgmt> resourceDetail = resourceMgmtRepo.findById(resourceId);
        if (!resourceDetail.isPresent()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND.value(), "Resource Not Found");
        }

//        get financial year start month to build the start date and end date for which record is to be retrieved
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month financialYearStartMonth = Month.valueOf(startMonth.toUpperCase());

        LocalDate responseLocalStartDate= null, responseLocalEndDate= null;
        responseLocalStartDate = LocalDate.of(financialYear, financialYearStartMonth, startDateOfMonth);
        responseLocalEndDate = responseLocalStartDate.plusYears(1).minusDays(1);

        Date responseStartDate = null, responseEndDate = null;
        Date requestStartDate= null, requestEndDate = null;

        responseStartDate = java.sql.Date.valueOf(responseLocalStartDate);
        responseEndDate = java.sql.Date.valueOf(responseLocalEndDate);

        GetDateRange getDateRange = new GetDateRange(responseStartDate, responseEndDate);
        requestStartDate = getDateRange.getStartDate();
        requestEndDate = getDateRange.getEndDate();

        if(ResourceUtils.isEndDateBeforeStartDate(getDateRange.getStartDate(), getDateRange.getEndDate())){
            return null;
        }


        String resourceProjectMapping = ResourceUtils.getFile("getResourceAllProjectAllocationDataQuery.txt");

        Query query = entityManager.createNativeQuery(resourceProjectMapping).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ResourceAllProjectAllocationData.class));

        query.setParameter("startDate", getDateRange.getStartDate());
        query.setParameter("endDate", getDateRange.getEndDate());
        query.setParameter("resourceId", resourceId);

//        get all allocation data
        List<ResourceAllProjectAllocationData> resourceAllProjectAllocationData = query.getResultList();

//        group them by project id
        Map<Integer, List<ResourceAllProjectAllocationData>> groupByProject
                = resourceAllProjectAllocationData.stream().collect(Collectors.groupingBy(ResourceAllProjectAllocationData::getProject_id));

        List<GanttChartData> ganttChartData = new ArrayList<>();

        String resourceName = null;

//        for each project
        for(Map.Entry<Integer, List<ResourceAllProjectAllocationData>> entry : groupByProject.entrySet()){

            List<ResourceAllProjectAllocationData> thisProjectAllocations = entry.getValue();
//            if allocations are not empty
            if(thisProjectAllocations!=null && !thisProjectAllocations.isEmpty()){

//                CRITICAL PART STARTS
//                the part from here is to ensure that the allocations in the list are complete,
//                that is, no such case is present where a record of a date, which lie in its allocation date range, is not present
                Map<Integer, ResourceAllProjectAllocationData> allUniqueMapIds = new HashMap<>();
                HashSet<LocalDate> localDateHashSet = new HashSet<>();

//                keep track of all the dates present, as well as every allocation date range
                for(ResourceAllProjectAllocationData eachDailyAllocation : thisProjectAllocations){
                    LocalDate allocationDate;

                    allocationDate = new java.sql.Date(eachDailyAllocation.getAllocation_date().getTime()).toLocalDate();
                    allUniqueMapIds.putIfAbsent(eachDailyAllocation.getMap_id(), eachDailyAllocation);
                    localDateHashSet.add(allocationDate);
                }

//                for every allocation date range for resource on this project
                for(Map.Entry<Integer, ResourceAllProjectAllocationData> allocationDates: allUniqueMapIds.entrySet()){
                    LocalDate allocationStartDate, allocationEndDate, currentDate;

                    allocationStartDate = new java.sql.Date(allocationDates.getValue().getAllocation_start_dt().getTime()).toLocalDate();
                    allocationEndDate = new java.sql.Date(allocationDates.getValue().getAllocation_end_dt().getTime()).toLocalDate();
                    currentDate = allocationStartDate;

//                    check if all the dates in this date range are present? if not, create them, save them, and add the saved record to the list
                    while(!currentDate.isAfter(allocationEndDate)){

                        if(currentDate.getDayOfWeek()!=DayOfWeek.SATURDAY && currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
                            if(!localDateHashSet.contains(currentDate)){

                                ProjectResourceMappingExtended missingRecordToSave = new ProjectResourceMappingExtended();
                                missingRecordToSave.setMapId(allocationDates.getValue().getMap_id());
                                missingRecordToSave.setAllocationDate(java.sql.Date.valueOf(currentDate));
                                missingRecordToSave.setFte(BigDecimal.valueOf(0d));
                                missingRecordToSave.setRequestedFte(BigDecimal.valueOf(0d));
                                missingRecordToSave = projectResourceMappingExtendedRepo.save(missingRecordToSave);

                                ResourceAllProjectAllocationData newRecordCreated = new ResourceAllProjectAllocationData(allocationDates.getValue());
                                newRecordCreated.setId(missingRecordToSave.getId());
                                newRecordCreated.setAllocation_date(missingRecordToSave.getAllocationDate());
                                newRecordCreated.setFte(missingRecordToSave.getFte());
                                newRecordCreated.setRequested_fte(missingRecordToSave.getRequestedFte());

                                thisProjectAllocations.add(newRecordCreated);
                            }
                        }

                        currentDate = currentDate.plusDays(1);
                    }
                }

//                CRITICAL PART ENDS

                GanttChartData thisProjectData = new GanttChartData();
                Map<LocalDate, LocalDate> allAllocatedDateRanges = new HashMap<>();
//                Map<LocalDate, Double> allRequestedFte = new HashMap<>();

                Map<LocalDate, Double> weekAllocatedFteSumTracker = new HashMap<>();
                Map<LocalDate, Double> weekRequestedFteSumTracker = new HashMap<>();
                Map<LocalDate, List<AllResourceActiveAllocation>> dailyRecordsByWeekStartDate = new HashMap<>();

                Map<LocalDate, Double> monthAllocatedFteSumTracker = new HashMap<>();
                Map<LocalDate, Double> monthRequestedFteSumTracker = new HashMap<>();
                Map<LocalDate, List<AllResourceActiveAllocation>> dailyRecordsByMonthStartDate = new HashMap<>();

//                sort
                thisProjectAllocations.sort(Comparator.comparing(ResourceAllProjectAllocationData::getAllocation_date));

                thisProjectData.setProjectId(thisProjectAllocations.get(0).getProject_id());
                thisProjectData.setProjectName(thisProjectAllocations.get(0).getProject_name());
                resourceName = thisProjectAllocations.get(0).getResource_name();

                Date startDate = thisProjectAllocations.get(0).getAllocation_start_dt();
                Date endDate = thisProjectAllocations.get(0).getAllocation_end_dt();

//                set each allocation
                List<AllResourceActiveAllocation> allResourceActiveAllocations = new ArrayList<>();

//                to collect list of dates which lie in weeks which are not part of current financial year
//                but are part of financial year first week or last week
                List<LocalDate> missingDatesInWeek = new ArrayList<>();

//                remove all the records which is beyond what is asked for by frontend
                for(ResourceAllProjectAllocationData eachDailyAllocation : thisProjectAllocations){

                    LocalDate allocStart, allocEnd;
                    allocStart = new java.sql.Date(eachDailyAllocation.getAllocation_start_dt().getTime()).toLocalDate();
                    allocEnd = new java.sql.Date(eachDailyAllocation.getAllocation_end_dt().getTime()).toLocalDate();

//                    take note of all the allocation date range the requested fte for those allocations
                    if(!allocStart.isAfter(responseLocalEndDate) && !allocEnd.isBefore(responseLocalStartDate)) {
                        allAllocatedDateRanges.putIfAbsent(allocStart, allocEnd);
//                        allRequestedFte.putIfAbsent(allocStart, eachDailyAllocation.getRequested_fte().doubleValue());
                    }

                    AllResourceActiveAllocation eachAllocationData = new AllResourceActiveAllocation(eachDailyAllocation);

//                    if current record is part of financial year
                    boolean inResponseDateRange = eachAllocationData.getAllocationDate().compareTo(getDateRange.getStartDate()) >= 0
                            && eachAllocationData.getAllocationDate().compareTo(getDateRange.getEndDate()) <= 0;

//                    in cases where financial year start or end is in middle of the week,
//                    we need the remaining week data to show correct Average Allocation FTE data for weekly allocation,
//                    hence, we are retrieving the first week and last week missing data, if any

                    Boolean isRecordPartOfFirstWeek = false, isRecordPartOfLastWeek = false;

//                    check if the financial start date lies in middle of week?
                    if(responseLocalStartDate.getDayOfWeek()!=DayOfWeek.MONDAY && responseLocalStartDate.getDayOfWeek()!=DayOfWeek.SATURDAY && responseLocalStartDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
//                        get dates which are part of the first financial week but not financial year
                        Date missingFirstWeekStartDate = java.sql.Date.valueOf(responseLocalStartDate.with(DayOfWeek.MONDAY));
                        Date missingFirstWeekEndDate = java.sql.Date.valueOf(responseLocalStartDate.minusDays(1));

//                        if current record lies in above dates
                        if(eachAllocationData.getAllocationDate().compareTo(missingFirstWeekStartDate) >= 0
                                && eachAllocationData.getAllocationDate().compareTo(missingFirstWeekEndDate) <= 0){
                            isRecordPartOfFirstWeek = true;
                        }
                    }
                    if(responseLocalEndDate.getDayOfWeek()!=DayOfWeek.FRIDAY && responseLocalEndDate.getDayOfWeek()!=DayOfWeek.SATURDAY && responseLocalEndDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
//                        get dates which are part of the last financial week but not financial year
                        Date missingLastWeekStartDate = java.sql.Date.valueOf(responseLocalEndDate.plusDays(1));
                        Date missingLastWeekEndDate = java.sql.Date.valueOf(responseLocalEndDate.with(DayOfWeek.FRIDAY));

//                        if current record lies in above dates
                        if(eachAllocationData.getAllocationDate().compareTo(missingLastWeekStartDate) >= 0
                                && eachAllocationData.getAllocationDate().compareTo(missingLastWeekEndDate) <= 0){
                            isRecordPartOfLastWeek = true;
                        }
                    }

                    if(inResponseDateRange){
                        allResourceActiveAllocations.add(eachAllocationData);

                        List<AllResourceActiveAllocation> dailyRecordByDate;

//                        week data
                        LocalDate currentWeekStartDate = new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate();
                        currentWeekStartDate = currentWeekStartDate.with(DayOfWeek.MONDAY);
                        weekAllocatedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getFte().doubleValue(), Double::sum);
                        weekRequestedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getRequestedFte().doubleValue(), Double::sum);

                        if(dailyRecordsByWeekStartDate.containsKey(currentWeekStartDate)){
                            dailyRecordByDate = dailyRecordsByWeekStartDate.get(currentWeekStartDate);
                        }
                        else{
                            dailyRecordByDate = new ArrayList<>();
                        }
                        dailyRecordByDate.add(eachAllocationData);
                        dailyRecordsByWeekStartDate.put(currentWeekStartDate, dailyRecordByDate);


//                        month data
                        LocalDate currentMonthStartDate = new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate();
                        currentMonthStartDate = currentMonthStartDate.withDayOfMonth(1);
                        monthAllocatedFteSumTracker.merge(currentMonthStartDate, eachAllocationData.getFte().doubleValue(), Double::sum);
                        monthRequestedFteSumTracker.merge(currentMonthStartDate, eachAllocationData.getRequestedFte().doubleValue(), Double::sum);

                        if(dailyRecordsByMonthStartDate.containsKey(currentMonthStartDate)){
                            dailyRecordByDate = dailyRecordsByMonthStartDate.get(currentMonthStartDate);
                        }
                        else{
                            dailyRecordByDate = new ArrayList<>();
                        }
                        dailyRecordByDate.add(eachAllocationData);
                        dailyRecordsByMonthStartDate.put(currentMonthStartDate, dailyRecordByDate);

                    }
                    if(isRecordPartOfFirstWeek){
                        List<AllResourceActiveAllocation> dailyRecordByDate;
//                        week data
                        LocalDate currentWeekStartDate = new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate();
                        currentWeekStartDate = currentWeekStartDate.with(DayOfWeek.MONDAY);
                        weekAllocatedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getFte().doubleValue(), Double::sum);
                        weekRequestedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getRequestedFte().doubleValue(), Double::sum);

                        if(dailyRecordsByWeekStartDate.containsKey(currentWeekStartDate)){
                            dailyRecordByDate = dailyRecordsByWeekStartDate.get(currentWeekStartDate);
                        }
                        else{
                            dailyRecordByDate = new ArrayList<>();
                        }
                        dailyRecordByDate.add(eachAllocationData);
                        dailyRecordsByWeekStartDate.put(currentWeekStartDate, dailyRecordByDate);
                    }
                    if(isRecordPartOfLastWeek){
                        List<AllResourceActiveAllocation> dailyRecordByDate;
//                        week data
                        LocalDate currentWeekStartDate = new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate();
                        currentWeekStartDate = currentWeekStartDate.with(DayOfWeek.MONDAY);
                        weekAllocatedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getFte().doubleValue(), Double::sum);
                        weekRequestedFteSumTracker.merge(currentWeekStartDate, eachAllocationData.getRequestedFte().doubleValue(), Double::sum);

                        if(dailyRecordsByWeekStartDate.containsKey(currentWeekStartDate)){
                            dailyRecordByDate = dailyRecordsByWeekStartDate.get(currentWeekStartDate);
                        }
                        else{
                            dailyRecordByDate = new ArrayList<>();
                        }
                        dailyRecordByDate.add(eachAllocationData);
                        dailyRecordsByWeekStartDate.put(currentWeekStartDate, dailyRecordByDate);
                    }
                    if(isRecordPartOfFirstWeek||isRecordPartOfLastWeek){
                        missingDatesInWeek.add(new java.sql.Date(eachAllocationData.getAllocationDate().getTime()).toLocalDate());
                    }



//                    check for this resource's earliest allocation on this project
                    if(startDate.compareTo(eachDailyAllocation.getAllocation_start_dt())>0){
                        startDate = eachDailyAllocation.getAllocation_start_dt();
                    }

//                    check for this resource's last allocation date on this project
                    if(endDate.compareTo(eachDailyAllocation.getAllocation_end_dt())<0){
                        endDate = eachDailyAllocation.getAllocation_end_dt();
                    }

                }

                LocalDate localStartDate, localEndDate;
                localStartDate = new java.sql.Date(startDate.getTime()).toLocalDate();
                localEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();

                thisProjectData.setAllocationStartDate(java.sql.Date.valueOf(localStartDate));
                thisProjectData.setAllocationEndDate(java.sql.Date.valueOf(localEndDate));
                thisProjectData.setAllocationData(allResourceActiveAllocations);

                Map<LocalDate, Integer> weekWorkingDays = new HashMap<>();
                Map<LocalDate, Integer> monthWorkingDays = new HashMap<>();

//                weekly and monthly working days' calculation, for every allocation of resource on this project
                for(Map.Entry<LocalDate, LocalDate> dateRange: allAllocatedDateRanges.entrySet()){
//                    LocalDate rangeStartDate = dateRange.getKey();
//                    LocalDate rangeEndDate = dateRange.getValue();

                    LocalDate rangeStartDate = dateRange.getKey().isAfter(responseLocalStartDate)?dateRange.getKey():responseLocalStartDate;
                    LocalDate rangeEndDate = dateRange.getValue().isBefore(responseLocalEndDate) ? dateRange.getValue() : responseLocalEndDate;

                    while(!rangeStartDate.isAfter(rangeEndDate)){
                        if(rangeStartDate.getDayOfWeek()!=DayOfWeek.SATURDAY && rangeStartDate.getDayOfWeek()!=DayOfWeek.SUNDAY) {
                            LocalDate currentWeek = rangeStartDate.with(DayOfWeek.MONDAY);
                            LocalDate currentMonth = rangeStartDate.withDayOfMonth(1);

                            weekWorkingDays.merge(currentWeek, 1, Integer::sum);
                            monthWorkingDays.merge(currentMonth, 1, Integer::sum);
                        }

                        rangeStartDate = rangeStartDate.plusDays(1);
                    }
                }
                for(LocalDate date: missingDatesInWeek){
                    LocalDate weekStartDate = date.with(DayOfWeek.MONDAY);
                    weekWorkingDays.merge(weekStartDate, 1, Integer::sum);
                }

                List<AllocationDataByWeek> weeklyAllocations = new ArrayList<>();
                List<AllocationDataByMonth> monthlyAllocations = new ArrayList<>();

                for(Map.Entry<LocalDate, Integer> eachWeek: weekWorkingDays.entrySet()){

                    Double weekAllocatedFte, weekRequestedFte;
                    LocalDate weekStart = eachWeek.getKey();

                    if(weekAllocatedFteSumTracker.containsKey(weekStart)){
                        weekAllocatedFte = weekAllocatedFteSumTracker.get(weekStart);
                        weekAllocatedFte = ResourceUtils.roundToTwoDecimalPlace(weekAllocatedFte/eachWeek.getValue());

                        weekRequestedFte = weekRequestedFteSumTracker.get(weekStart);
                        weekRequestedFte = ResourceUtils.roundToTwoDecimalPlace(weekRequestedFte/eachWeek.getValue());
                    }
                    else{
                        weekAllocatedFte = 0d;
                        weekRequestedFte = 0d;
                    }

                    weeklyAllocations.add(new AllocationDataByWeek(java.sql.Date.valueOf(weekStart), weekStart.getYear(), weekAllocatedFte, weekRequestedFte, dailyRecordsByWeekStartDate.get(weekStart)));

                }

                for(Map.Entry<LocalDate, Integer> eachMonth: monthWorkingDays.entrySet()){

                    Double monthAllocatedFte, monthRequestedFte;
                    LocalDate month = eachMonth.getKey();

                    if(monthAllocatedFteSumTracker.containsKey(eachMonth.getKey())){
                        monthAllocatedFte = monthAllocatedFteSumTracker.get(eachMonth.getKey());
                        monthAllocatedFte = ResourceUtils.roundToTwoDecimalPlace(monthAllocatedFte/eachMonth.getValue());

                        monthRequestedFte = monthRequestedFteSumTracker.get(eachMonth.getKey());
                        monthRequestedFte = ResourceUtils.roundToTwoDecimalPlace(monthRequestedFte/eachMonth.getValue());
                    }
                    else{
                        monthAllocatedFte = 0d;
                        monthRequestedFte = 0d;
                    }

                    monthlyAllocations.add(new AllocationDataByMonth(month.getMonth().toString(), month.getYear(), monthAllocatedFte, monthRequestedFte, dailyRecordsByMonthStartDate.get(month)));
                }

                weeklyAllocations.sort(Comparator.comparing(AllocationDataByWeek::getWeek));
                monthlyAllocations.sort(Comparator.comparing(AllocationDataByMonth::getYear).thenComparing((a,b)->{
                    Month first = Month.valueOf(a.getMonth());
                    Month second = Month.valueOf(b.getMonth());

                    return first.compareTo(second);
                }));

////                make list of requested fte for every allocation of this resource on current project
//                List<RequestedFteForEachAllocation> requestedFteForEachAllocations = new ArrayList<>();
//                for(Map.Entry<LocalDate, LocalDate> dateRange: allAllocatedDateRanges.entrySet()){
//                    LocalDate rangeStartDate = dateRange.getKey();
//                    LocalDate rangeEndDate = dateRange.getValue();
//                    Double requestedFte = allRequestedFte.get(rangeStartDate);
//                    Date allocStartDate = java.sql.Date.valueOf(rangeStartDate);
//                    Date allocEndDate = java.sql.Date.valueOf(rangeEndDate);
//
//                    requestedFteForEachAllocations.add(new RequestedFteForEachAllocation(allocStartDate, allocEndDate, requestedFte));
//                }

//                requestedFteForEachAllocations.sort(Comparator.comparing(RequestedFteForEachAllocation::getAllocationStartDate));

//                thisProjectData.setRequested_fte(requestedFteForEachAllocations);
                thisProjectData.setWeeklyAllocations(weeklyAllocations);
                thisProjectData.setMonthlyAllocations(monthlyAllocations);
                thisProjectData.setIsUpdated(false);

//                add data to list
                if(!thisProjectData.getAllocationData().isEmpty()){
                    ganttChartData.add(thisProjectData);
                    if(getDateRange.getEndDate()==null && (requestEndDate==null || requestEndDate.compareTo(thisProjectData.getAllocationEndDate())<0)){
                        requestEndDate = thisProjectData.getAllocationEndDate();
                    }
                }
            }

        }

        GetResourceAllocationChart chartData = null;

        if(!ganttChartData.isEmpty()){
            ganttChartData.sort(Comparator.comparing(GanttChartData::getProjectName, (s1, s2)->{
                String project1 = s1.toLowerCase();
                String project2 = s2.toLowerCase();
                return String.CASE_INSENSITIVE_ORDER.compare(project1, project2);
            }).thenComparing(GanttChartData::getAllocationStartDate).thenComparing(GanttChartData::getAllocationEndDate));

            List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
            Double maxEnabledFteInAdmin = ResourceUtils.MIN_FTE;

            for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
                if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                    if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                        maxEnabledFteInAdmin = eachStatus.getEndValue();
                    }
                }
            }


            LocalDate dateOfJoin = null, lastWorkingDate = null;
            dateOfJoin = new java.sql.Date(resourceDetail.get().getDateOfJoin().getTime()).toLocalDate();
            if (resourceDetail.get().getLastWorkingDate() != null) {
                lastWorkingDate = new java.sql.Date(resourceDetail.get().getLastWorkingDate().getTime()).toLocalDate();
            }


            List<WorkingDaysInWeek> workingDaysInWeekList = new ArrayList<>();
            List<WorkingDaysInMonth> workingDaysInMonthList = new ArrayList<>();

            LocalDate weekStart = null, monthStart = null, allocationStartDate=null, allocationEndDate=null, currentDate=null;
            allocationStartDate = new java.sql.Date(requestStartDate.getTime()).toLocalDate();
            allocationEndDate = new java.sql.Date(requestEndDate.getTime()).toLocalDate();
            currentDate = allocationStartDate.isAfter(dateOfJoin)?allocationStartDate:dateOfJoin;
            weekStart = currentDate.with(DayOfWeek.MONDAY);
            monthStart = currentDate.withDayOfMonth(1);
            Integer weekWorkingDays = 0, monthWorkingDays = 0;

            LocalDate startDate = weekStart.isAfter(dateOfJoin)?weekStart:dateOfJoin;
            weekWorkingDays = weekWorkingDays + ResourceUtils.numberOfWorkingDays(startDate, currentDate.minusDays(1));

            while(!currentDate.isAfter(allocationEndDate)){

                if(currentDate.getDayOfWeek()!=DayOfWeek.SATURDAY && currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY) {
                    weekWorkingDays++;
                    monthWorkingDays++;
                }
                currentDate = currentDate.plusDays(1);

                if(!weekStart.isEqual(currentDate.with(DayOfWeek.MONDAY))){
                    workingDaysInWeekList.add(new WorkingDaysInWeek(java.sql.Date.valueOf(weekStart) ,weekStart.getYear(), weekWorkingDays));
                    weekWorkingDays=0;
                    weekStart = currentDate.with(DayOfWeek.MONDAY);
                }
                if(!monthStart.isEqual(currentDate.withDayOfMonth(1))){
                    workingDaysInMonthList.add(new WorkingDaysInMonth(monthStart.getMonth().toString() ,monthStart.getYear(), monthWorkingDays));
                    monthWorkingDays=0;
                    monthStart = currentDate.withDayOfMonth(1);
                }

            }

            if(currentDate.isAfter(allocationEndDate)){

                LocalDate endDate = null;

                if(weekWorkingDays!=0){
                    endDate = currentDate.with(DayOfWeek.SUNDAY);
                    if(lastWorkingDate!=null){
                        if(endDate.isAfter(lastWorkingDate)){
                            endDate = lastWorkingDate;
                        }
                    }
                    weekWorkingDays = weekWorkingDays + ResourceUtils.numberOfWorkingDays(currentDate, endDate);
                    workingDaysInWeekList.add(new WorkingDaysInWeek(java.sql.Date.valueOf(weekStart) ,weekStart.getYear(), weekWorkingDays));
                }

                if(monthWorkingDays!=0){
                    endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
                    if(lastWorkingDate!=null){
                        if(endDate.isAfter(lastWorkingDate)){
                            endDate = lastWorkingDate;
                        }
                    }
                    monthWorkingDays = monthWorkingDays + ResourceUtils.numberOfWorkingDays(currentDate, endDate);
                    workingDaysInMonthList.add(new WorkingDaysInMonth(monthStart.getMonth().toString() ,monthStart.getYear(), monthWorkingDays));
                }
            }


            chartData = new GetResourceAllocationChart(resourceId, resourceName, null, requestStartDate, requestEndDate, workingDaysInWeekList, workingDaysInMonthList, maxEnabledFteInAdmin, ganttChartData);
        }

        return chartData;
    }


    public Object updateResourceAllAllocationsChart(String modifiedBy, String allocationFrequency, PostResourceAllocationChart postResourceAllocationChart) {

//        check if allocationFrequency is correct or not
        if(!Objects.equals(allocationFrequency, ResourceUtils.ALLOCATION_FREQUENCY_DAILY)
                && !Objects.equals(allocationFrequency, ResourceUtils.ALLOCATION_FREQUENCY_WEEKLY)
                && !Objects.equals(allocationFrequency, ResourceUtils.ALLOCATION_FREQUENCY_MONTHLY)){
            String message = ResourceUtils.ALLOCATION_FREQUENCY_INVALID;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

//        to store allocations for which isUpdated = true
        List<AllResourceActiveAllocation> modifiedAllocations = new ArrayList<>();
        HashMap<Integer, AllResourceActiveAllocation> modifiedAllocationsToMap = new HashMap<>();

//        updated allocations ids
        HashSet<Integer> updatedAllocationIds = new HashSet<>();

        List<AllResourceActiveAllocation> allAllocations = new ArrayList<>();

        List<GanttChartData> ganttChartData = postResourceAllocationChart.getGanttChartData();

//        separate the updated data from the response
        for(GanttChartData eachProject: ganttChartData){
//            collect records from projects for which allocations are updated
            if(eachProject.getIsUpdated()){
                switch (allocationFrequency) {
                    case ResourceUtils.ALLOCATION_FREQUENCY_DAILY:
                        for(AllResourceActiveAllocation eachData: eachProject.getAllocationData()){
                            LocalDate localDate = new java.sql.Date(eachData.getAllocationDate().getTime()).toLocalDate();
                            updatedAllocationIds.add(eachData.getId());

                            eachData.setAllocationDate(java.sql.Date.valueOf(localDate));
                            modifiedAllocationsToMap.put(eachData.getId(), eachData);
                        }
                        break;
                    case ResourceUtils.ALLOCATION_FREQUENCY_WEEKLY:

                        List<AllocationDataByWeek> weeklyData = eachProject.getWeeklyAllocations();

                        for (AllocationDataByWeek eachWeekData : weeklyData) {
                            for(AllResourceActiveAllocation eachData: eachWeekData.getThisWeekAllocationData()){
                                LocalDate localDate = new java.sql.Date(eachData.getAllocationDate().getTime()).toLocalDate();
                                updatedAllocationIds.add(eachData.getId());

                                eachData.setAllocationDate(java.sql.Date.valueOf(localDate));
                                modifiedAllocationsToMap.put(eachData.getId(), eachData);
                            }
                        }
                        break;
                    case ResourceUtils.ALLOCATION_FREQUENCY_MONTHLY:

                        List<AllocationDataByMonth> monthlyData = eachProject.getMonthlyAllocations();

                        for (AllocationDataByMonth eachMonthData : monthlyData) {
                            for(AllResourceActiveAllocation eachData: eachMonthData.getThisMonthAllocationData()){
                                LocalDate localDate = new java.sql.Date(eachData.getAllocationDate().getTime()).toLocalDate();
                                updatedAllocationIds.add(eachData.getId());

                                eachData.setAllocationDate(java.sql.Date.valueOf(localDate));
                                modifiedAllocationsToMap.put(eachData.getId(), eachData);
                            }
                        }
                        break;
                }
            }
//            collect records for all projects to check for daily limit
            switch (allocationFrequency) {
                case ResourceUtils.ALLOCATION_FREQUENCY_DAILY:
                    allAllocations.addAll(eachProject.getAllocationData());
                    break;
                case ResourceUtils.ALLOCATION_FREQUENCY_WEEKLY:

                    List<AllocationDataByWeek> weeklyData = eachProject.getWeeklyAllocations();

                    for (AllocationDataByWeek eachWeekData : weeklyData) {
                        allAllocations.addAll(eachWeekData.getThisWeekAllocationData());
                    }
                    break;
                case ResourceUtils.ALLOCATION_FREQUENCY_MONTHLY:

                    List<AllocationDataByMonth> monthlyData = eachProject.getMonthlyAllocations();

                    for (AllocationDataByMonth eachMonthData : monthlyData) {
                        allAllocations.addAll(eachMonthData.getThisMonthAllocationData());
                    }
                    break;
            }
        }

//        to check if the allocations are not assigned beyond the updated limit
        if(!allAllocations.isEmpty()) {

//            get total allocations for each day for this resource
            Map<LocalDate, Double> totalAllocations = new HashMap<>();
            for (AllResourceActiveAllocation eachAllocation : allAllocations) {
                LocalDate localDate = new java.sql.Date(eachAllocation.getAllocationDate().getTime()).toLocalDate();
                totalAllocations.merge(localDate, eachAllocation.getFte().doubleValue(), Double::sum);
            }

//            get the stored data for the records where isUpdated is true
            List<ProjectResourceMappingExtended> storedData = projectResourceMappingExtendedRepo.findAllById(updatedAllocationIds);
            HashSet<LocalDate> updatedAllocationDates =new HashSet<>();
            for(ProjectResourceMappingExtended eachRecord: storedData){
//                for each record, check if it was updated?
//                if yes, then store those dates
                Double storedFte = eachRecord.getFte().doubleValue();
                Double newFte = modifiedAllocationsToMap.get(eachRecord.getId()).getFte().doubleValue();
                if(!storedFte.equals(newFte)){
                    updatedAllocationDates.add(new java.sql.Date(eachRecord.getAllocationDate().getTime()).toLocalDate());
                    modifiedAllocations.add(modifiedAllocationsToMap.get(eachRecord.getId()));
                }
            }

//            get maximum enabled FTE from admin settings
            List<ResourceMgmtStatus> resourceMgmtStatus = resourceMgmtStatusRepo.findAll();
            Double maxEnabledFteInAdmin = ResourceUtils.MIN_FTE;

            for(ResourceMgmtStatus eachStatus: resourceMgmtStatus){
                if(eachStatus.getIsEnabled().equals("1") && eachStatus.getStartValue()!=null && eachStatus.getEndValue()!=null){
                    if(maxEnabledFteInAdmin < eachStatus.getEndValue()){
                        maxEnabledFteInAdmin = eachStatus.getEndValue();
                    }
                }
            }

//            if allocation for any date is beyond the limit, return the error
            for(LocalDate date: updatedAllocationDates){
                if(totalAllocations.get(date)>maxEnabledFteInAdmin){
                    String message = ResourceUtils.ALLOCATION_BEYOND_PERMISSIBLE_LIMIT;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
            }

        }

//        else update the modified records
        if(!modifiedAllocations.isEmpty()){
            Set<Integer> allMapIds = modifiedAllocations.stream().map(AllResourceActiveAllocation::getMapId).collect(Collectors.toSet());

            List<ProjectResourceMapping> modifiedProjectResourceMappings = projectResourceMappingRepo.findAllById(allMapIds);

            Set<Integer> allValidMapIds = modifiedProjectResourceMappings.stream().map(ProjectResourceMapping::getMapId).collect(Collectors.toSet());

            if(!allValidMapIds.isEmpty()){

                List<ProjectResourceMappingExtended> dailyAllocationsToUpdate = new ArrayList<>();
                for(AllResourceActiveAllocation eachDailyAllocations : modifiedAllocations){
                    if(allValidMapIds.contains(eachDailyAllocations.getMapId())){
                        dailyAllocationsToUpdate.add(new ProjectResourceMappingExtended(eachDailyAllocations));
                    }
                }
                projectResourceMappingExtendedRepo.saveAll(dailyAllocationsToUpdate);

                List<AllocatedFteAverageByMapId> allocatedFteAverageByMapIdList = projectResourceMappingRepo.getTotalAllocatedFteForMapIds(new ArrayList<>(allMapIds));
                Map<Integer, BigDecimal> allocatedFteAverageByMapIdToMap
                        = allocatedFteAverageByMapIdList.stream().collect(Collectors.toMap(AllocatedFteAverageByMapId::getMapId, AllocatedFteAverageByMapId::getTotalAllocated));

                List<RequestedResources> requestedResourcesList = requestedResourcesRepo.getRequestedResourceListByMapIds(new ArrayList<>(allMapIds));
                Map<Integer, RequestedResources> requestedResourcesListToMap
                        = requestedResourcesList.stream().collect(Collectors.toMap(RequestedResources::getRequestId, data->data));

                List<ProjectResourceMapping> projectResourceMappingsToUpdate = new ArrayList<>();
                List<RequestedResources> requestedResourceToUpdate = new ArrayList<>();
                for(ProjectResourceMapping eachAllocation: modifiedProjectResourceMappings){
                    eachAllocation.setModifiedDate(new Date());
                    eachAllocation.setModifiedBy(modifiedBy);

                    LocalDate allocationStartDate = new java.sql.Date(eachAllocation.getAllocationStartDate().getTime()).toLocalDate();
                    LocalDate allocationEndDate = new java.sql.Date(eachAllocation.getAllocationEndDate().getTime()).toLocalDate();
                    Integer workingDays = ResourceUtils.numberOfWorkingDays(allocationStartDate, allocationEndDate);

                    Double oldAllocatedFteAvg = eachAllocation.getAllocatedFteAvg().doubleValue();

                    Double newAllocatedAvg = allocatedFteAverageByMapIdToMap.get(eachAllocation.getMapId()).doubleValue();
                    newAllocatedAvg = ResourceUtils.roundToTwoDecimalPlace(newAllocatedAvg/workingDays);

                    String requestStatus = "";
                    if(newAllocatedAvg>0d && newAllocatedAvg.equals(eachAllocation.getRequestedFteAvg())){
                        requestStatus = ResourceUtils.REQUEST_RESOURCE_STATUS_COMPLETELY_FULFILLED;
                    }
                    else if(newAllocatedAvg>0d && newAllocatedAvg>eachAllocation.getRequestedFteAvg()){
                        requestStatus = ResourceUtils.REQUEST_RESOURCE_STATUS_OVER_FULFILLED;
                    }
                    else if(newAllocatedAvg>0d && newAllocatedAvg<eachAllocation.getRequestedFteAvg()){
                        requestStatus = ResourceUtils.REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED;
                    }
                    else{
                        requestStatus = ResourceUtils.REQUEST_RESOURCE_STATUS_OPEN;
                    }

                    String rmLastAction = "";
                    if(oldAllocatedFteAvg>newAllocatedAvg){
                        rmLastAction = ResourceUtils.REQUEST_RESOURCE_AVG_APPROVED_FTE_DECREASED + ResourceUtils.roundToTwoDecimalPlace(oldAllocatedFteAvg - newAllocatedAvg);
                    }
                    else{
                        rmLastAction = ResourceUtils.REQUEST_RESOURCE_AVG_APPROVED_FTE_INCREASED + ResourceUtils.roundToTwoDecimalPlace(newAllocatedAvg - oldAllocatedFteAvg);
                    }

                    eachAllocation.setAllocatedFteAvg(BigDecimal.valueOf(newAllocatedAvg));
                    projectResourceMappingsToUpdate.add(eachAllocation);

                    RequestedResources requestedResources = requestedResourcesListToMap.get(eachAllocation.getRequestId());
                    requestedResources.setModifiedDate(eachAllocation.getModifiedDate());
                    requestedResources.setModifiedBy(eachAllocation.getModifiedBy());
                    requestedResources.setRequestStatus(requestStatus);
                    requestedResources.setRmLastAction(rmLastAction);
                    requestedResources.setNotify("0");
                    requestedResources.setNotifyPm("1");
                    requestedResources.setViewedByPMIds("");
                    requestedResourceToUpdate.add(requestedResources);
                }

                projectResourceMappingRepo.saveAll(projectResourceMappingsToUpdate);
                requestedResourcesRepo.saveAll(requestedResourceToUpdate);
            }
        }

        LOGGER.info("User " + modifiedBy + " UpdateResourseMgmtSettings skills at " + new Date());
        return true;
    }

    public Object getResourceAllocationChartForAllResources(List<Integer> resourceIds, Integer financialYear){

//        get financial year month start from general settings
        String startMonth = generalSettingsRepo.getFinancialYearStartMonth();
        int startDateOfMonth = 1;

        Month financialYearStartMonth = Month.valueOf(startMonth.toUpperCase());

        LocalDate responseLocalStartDate= null, responseLocalEndDate= null;
        responseLocalStartDate = LocalDate.of(financialYear, financialYearStartMonth, startDateOfMonth);
        responseLocalEndDate = responseLocalStartDate.plusYears(1).minusDays(1);

        Date responseStartDate = null, responseEndDate = null, recordRetrieveStartDate = null, recordRetrieveEndDate = null;

//        create the start date and end date of the range for which record is to be retrieved (including complete weeks which share financial year)
        recordRetrieveStartDate = java.sql.Date.valueOf(responseLocalStartDate.with(DayOfWeek.MONDAY));
        recordRetrieveEndDate = java.sql.Date.valueOf(responseLocalEndDate.with(DayOfWeek.FRIDAY));

//        this is response date
        responseStartDate = java.sql.Date.valueOf(responseLocalStartDate);
        responseEndDate = java.sql.Date.valueOf(responseLocalEndDate);

//        GetDateRange getDateRange = new GetDateRange(responseStartDate, responseEndDate);

//        retrieve all records
        List<AllResourceAllAllocationForGanttChart> allResourceData
                = resourceMgmtRepo.getAllResourceActiveAllocationForGanttChart(resourceIds, recordRetrieveStartDate, recordRetrieveEndDate);

//        group by resource id
        Map<Integer, List<AllResourceAllAllocationForGanttChart>> allResourceDataToMap
                = allResourceData.stream().collect(Collectors.groupingBy(AllResourceAllAllocationForGanttChart::getResourceId));

//        get all active project allocation done for each of these resource
        List<ResourceRequestedProjectDetails> resourceRequestedProjectDetails
                = projectResourceMappingRepo.getAllocatedProjectsForResourceIds(resourceIds, recordRetrieveStartDate, recordRetrieveEndDate);

//        remove time-stamp from each of these records
        for(int i=0; i<resourceRequestedProjectDetails.size(); i++){
            ResourceRequestedProjectDetails eachRecord = resourceRequestedProjectDetails.get(i);

            LocalDate localStartDate = new java.sql.Date(eachRecord.getAllocationStartDate().getTime()).toLocalDate();
            eachRecord.setAllocationStartDate(java.sql.Date.valueOf(localStartDate));

            LocalDate localEndDate = new java.sql.Date(eachRecord.getAllocationEndDate().getTime()).toLocalDate();
            eachRecord.setAllocationEndDate(java.sql.Date.valueOf(localEndDate));

            resourceRequestedProjectDetails.set(i, eachRecord);
        }

        Map<Integer, List<ResourceRequestedProjectDetails>> resourceRequestedProjectDetailsToMap;
//        if(resourceRequestedProjectDetails!=null && !resourceRequestedProjectDetails.isEmpty()) {
            resourceRequestedProjectDetailsToMap = resourceRequestedProjectDetails.stream().collect(Collectors.groupingBy(ResourceRequestedProjectDetails::getResourceId));
//        }

//        get all resource details list, as well as check if resource ids received are valid or not
        List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.findAllById(resourceIds);

        List<GanttChartDataForAllResource> chartDataForAllResources = new ArrayList<>();

//        for each resource
        for(ResourceMgmt eachResource: resourceMgmtList){

            List<AllResourceAllAllocationForGanttChart> resourceAllocationData = allResourceDataToMap.get(eachResource.getResourceId());

            Integer resourceId = eachResource.getResourceId();
            String resourceName = eachResource.getFirstName() + " " + eachResource.getLastName();
            Date dateOfJoin = eachResource.getDateOfJoin();
            Date lastWorkingDate = eachResource.getLastWorkingDate();

            Map<LocalDate, Double> dailySum = new HashMap<>();
            Map<LocalDate, Double> weeklySum = new HashMap<>();
            Map<LocalDate, Double> monthlySum = new HashMap<>();

//                calculate each fte sum
            if(resourceAllocationData!=null && !resourceAllocationData.isEmpty()) {
                for (AllResourceAllAllocationForGanttChart eachDate : resourceAllocationData) {
                    LocalDate currentDate = new java.sql.Date(eachDate.getAllocationDate().getTime()).toLocalDate();
                    weeklySum.merge(currentDate.with(DayOfWeek.MONDAY), eachDate.getFte().doubleValue(), Double::sum);

//                    since week which share financial year have records which we don't need for daily and monthly, we don't include them
                    if (!currentDate.isBefore(responseLocalStartDate) && !currentDate.isAfter(responseLocalEndDate)) {
                        dailySum.merge(currentDate, eachDate.getFte().doubleValue(), Double::sum);
                        monthlySum.merge(currentDate.withDayOfMonth(1), eachDate.getFte().doubleValue(), Double::sum);
                    }
                }
            }

//                in weekly, we need working days for the entire week, if the week is shared between financial years,
//                hence we use recordRetrieveStartDate and recordRetrieveEndDate
            Date startDate = recordRetrieveStartDate.compareTo(dateOfJoin)>0?recordRetrieveStartDate:dateOfJoin;
            Date endDate = recordRetrieveEndDate;
            if(lastWorkingDate!=null){
                endDate = recordRetrieveEndDate.compareTo(lastWorkingDate)<0?recordRetrieveEndDate:lastWorkingDate;
            }

//                calculate each week and months working days
            LocalDate localStartDate, localEndDate;
            localStartDate = new java.sql.Date(startDate.getTime()).toLocalDate();
            localEndDate = new java.sql.Date(endDate.getTime()).toLocalDate();
            LocalDate currentDate = localStartDate;

            Map<LocalDate, Integer> dailyWorkingDays = new HashMap<>();
            Map<LocalDate, Integer> weekWorkingDays = new HashMap<>();
            Map<LocalDate, Integer> monthWorkingDays = new HashMap<>();

            while(!currentDate.isAfter(localEndDate)){
                if(currentDate.getDayOfWeek()!=DayOfWeek.SATURDAY && currentDate.getDayOfWeek()!=DayOfWeek.SUNDAY){
                    weekWorkingDays.merge(currentDate.with(DayOfWeek.MONDAY), 1, Integer::sum);

//                        we only want working days for financial year in case for monthly
                    if(!currentDate.isBefore(responseLocalStartDate) && !currentDate.isAfter(responseLocalEndDate)) {
                        dailyWorkingDays.put(currentDate, 1);
                        monthWorkingDays.merge(currentDate.withDayOfMonth(1), 1, Integer::sum);
                    }
                }
                currentDate = currentDate.plusDays(1);
            }

//            resource start date and end date
            startDate = responseStartDate.compareTo(dateOfJoin)>0?responseStartDate:dateOfJoin;
            endDate = responseEndDate;
            if(lastWorkingDate!=null){
                endDate = responseEndDate.compareTo(lastWorkingDate)<0?responseEndDate:lastWorkingDate;
            }

            List<AllocationDataByDaily> dailyAllocation = new ArrayList<>();
            List<AllocationDataByWeek> weeklyAllocation = new ArrayList<>();
            List<AllocationDataByMonth> monthlyAllocation = new ArrayList<>();

//                create data for each allocation frequency
//                daily
            for(Map.Entry<LocalDate, Integer> eachDate: dailyWorkingDays.entrySet()){
                Integer workingDays = eachDate.getValue();
                Double fteSum = 0d;
                if(dailySum.containsKey(eachDate.getKey())){
                    fteSum = dailySum.get(eachDate.getKey());
                }

                Date date = java.sql.Date.valueOf(eachDate.getKey());
                Double dailyAvg = ResourceUtils.roundToTwoDecimalPlace(fteSum/workingDays);

                dailyAllocation.add(new AllocationDataByDaily(date, dailyAvg));
            }
            dailyAllocation.sort(Comparator.comparing(AllocationDataByDaily::getDate));

//                weekly, divide fte sum by working days in week
            for(Map.Entry<LocalDate, Integer> eachDate: weekWorkingDays.entrySet()){
                Integer workingDays = eachDate.getValue();
                Double fteSum = 0d;
                if(weeklySum.containsKey(eachDate.getKey())){
                    fteSum = weeklySum.get(eachDate.getKey());
                }
                Date date = java.sql.Date.valueOf(eachDate.getKey());
                Double weekAvg = ResourceUtils.roundToTwoDecimalPlace(fteSum/workingDays);

                weeklyAllocation.add(new AllocationDataByWeek(date, eachDate.getKey().getYear(), weekAvg, null, null));
            }
            weeklyAllocation.sort(Comparator.comparing(AllocationDataByWeek::getWeek));

//                monthly, divide fte sum by working days in month
            for(Map.Entry<LocalDate, Integer> eachDate: monthWorkingDays.entrySet()){

                Integer workingDays = eachDate.getValue();
                Double fteSum = 0d;
                if(monthlySum.containsKey(eachDate.getKey())){
                    fteSum = monthlySum.get(eachDate.getKey());
                }

                Double monthAvg = ResourceUtils.roundToTwoDecimalPlace(fteSum/workingDays);

                monthlyAllocation.add(new AllocationDataByMonth(eachDate.getKey().getMonth().toString(), eachDate.getKey().getYear(), monthAvg, null, null));
            }
            monthlyAllocation.sort(Comparator.comparing(AllocationDataByMonth::getYear).thenComparing((a,b)->{
                Month first = Month.valueOf(a.getMonth());
                Month second = Month.valueOf(b.getMonth());

                return first.compareTo(second);
            }));

//            remove time stamp from these dates
            startDate = java.sql.Date.valueOf(new java.sql.Date(startDate.getTime()).toLocalDate());
            endDate = java.sql.Date.valueOf(new java.sql.Date(endDate.getTime()).toLocalDate());

//                all data for each resource to return list
            chartDataForAllResources.add(new GanttChartDataForAllResource(resourceId, resourceName, startDate, endDate, resourceRequestedProjectDetailsToMap.get(resourceId),
                    dailyAllocation, weeklyAllocation, monthlyAllocation));

        }

        if(chartDataForAllResources.isEmpty()){
            chartDataForAllResources=null;
        }

//        create response data
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("startDate", responseStartDate);
        returnData.put("endDate", responseEndDate);
        returnData.put("ganttChartData", chartDataForAllResources);

        return returnData;
    }

    public Object sendResourceListEligibleForSwapping(Integer resourceId, Integer projectId){

        DepartmentStringIdName departmentIdName = resourceMgmtRepo.getResourceDepartmentId(resourceId);
        List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.getAllResourceByDepartmentId(departmentIdName.getDepartmentId());

        String projectAnalysisQuery = ResourceUtils.getFile("getProjectStartEndDates.txt");
        Query query = entityManager.createNativeQuery(projectAnalysisQuery).unwrap(org.hibernate.query.Query.class);
        ((NativeQueryImpl) query).setResultTransformer(new AliasToBeanResultTransformer(ProjectStartEndDate.class));

        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(projectId);
        query.setParameter("projectIds", projectIds);

        List<ProjectStartEndDate> projectStartEndDateList = query.getResultList();
        ProjectStartEndDate currentProject = null;
        if(projectStartEndDateList!=null && !projectStartEndDateList.isEmpty()){
            currentProject = projectStartEndDateList.get(0);
        }

        List<ResourceIdNameDetail> resourceIdNameDetailList = null;

        List<ProjectResourceMapping> allAllocationsOnProject = projectResourceMappingRepo.getAllocationResourcesOnProject(projectId);
        Set<Integer> resourceNotEligibleForSwapping = new HashSet<>();
        for(ProjectResourceMapping eachResource: allAllocationsOnProject){
            if(eachResource.getResourceId()!=null){
                resourceNotEligibleForSwapping.add(eachResource.getResourceId());
            }
        }

        if(currentProject!=null) {
            if (resourceMgmtList != null && !resourceMgmtList.isEmpty()) {
                resourceIdNameDetailList = new ArrayList<>();
                for (ResourceMgmt eachResource : resourceMgmtList) {
                    if (!resourceNotEligibleForSwapping.contains(eachResource.getResourceId())
                            && eachResource.getDateOfJoin().compareTo(currentProject.getProject_end_date())<0
                            && ((eachResource.getLastWorkingDate()==null)||(eachResource.getLastWorkingDate().compareTo(currentProject.getProject_start_date())>0 ))) {
                        String resourceName = eachResource.getFirstName() + " " + eachResource.getLastName();
                        resourceIdNameDetailList.add(new ResourceIdNameDetail(eachResource.getResourceId(), resourceName));
                    }
                }
            }
        }

        if(resourceIdNameDetailList==null || resourceIdNameDetailList.isEmpty()){
            resourceIdNameDetailList = null;
        }

        return resourceIdNameDetailList;
    }

    public Object swapResourceForRequestedResource(Integer mapId, Integer resourceId, String modifiedBy){

        Optional<ProjectResourceMapping> projectResourceMapping = projectResourceMappingRepo.findById(mapId);
        Optional<ResourceMgmt> resourceMgmt = resourceMgmtRepo.findById(resourceId);
        if(projectResourceMapping.isPresent() && resourceMgmt.isPresent()){

            ProjectResourceMapping currentRequest = projectResourceMapping.get();
            ResourceMgmt swappedResource = resourceMgmt.get();

            String firstResourceName = swappedResource.getFirstName() + " " + swappedResource.getLastName();
            String secondResourceName = "Unnamed";

            if(currentRequest.getResourceId()!=null) {
                Optional<ResourceMgmt> resourceMgmt1 = resourceMgmtRepo.findById(currentRequest.getResourceId());
                if (resourceMgmt1.isPresent()){
                    secondResourceName = resourceMgmt1.get().getFirstName() + " " + resourceMgmt1.get().getLastName();
                }
            }

            Date allocationStartDate = currentRequest.getAllocationStartDate();
            Date allocationEndDate = currentRequest.getAllocationEndDate();
            if(swappedResource.getDateOfJoin()!=null && allocationStartDate.compareTo(swappedResource.getDateOfJoin())<0){
                allocationStartDate = swappedResource.getDateOfJoin();
            }
            if(swappedResource.getLastWorkingDate()!=null && allocationEndDate.compareTo(swappedResource.getLastWorkingDate())>0){
                allocationEndDate = swappedResource.getLastWorkingDate();
            }

            List<AllResourceActiveAllocation> currentRequestAllocations = projectResourceMappingRepo.getAllocationDataByMapId(currentRequest.getMapId());

            List<ProjectResourceMappingExtended> allocationsToUpdate = null;
            if(currentRequestAllocations!=null && !currentRequestAllocations.isEmpty()){
                allocationsToUpdate = new ArrayList<>();

                LocalDate localAllocationStartDate = new java.sql.Date(allocationStartDate.getTime()).toLocalDate();
                LocalDate localAllocationEndDate = new java.sql.Date(allocationEndDate.getTime()).toLocalDate();
                Integer workingDays = ResourceUtils.numberOfWorkingDays(localAllocationStartDate, localAllocationEndDate);

                Double totalAllocation = 0d, totalRequested = 0d;

                for(AllResourceActiveAllocation eachAllocation: currentRequestAllocations){

                    ProjectResourceMappingExtended currentAllocation = new ProjectResourceMappingExtended(eachAllocation);

                    LocalDate currentDate = new java.sql.Date(eachAllocation.getAllocationDate().getTime()).toLocalDate();

                    if(currentDate.isBefore(localAllocationStartDate) || currentDate.isAfter(localAllocationEndDate)){
                        currentAllocation.setRequestedFte(BigDecimal.valueOf(0d));
                        currentAllocation.setFte(BigDecimal.valueOf(0d));
                    }

                    totalAllocation += currentAllocation.getFte().doubleValue();
                    totalRequested += currentAllocation.getRequestedFte().doubleValue();

                    allocationsToUpdate.add(currentAllocation);
                }
                projectResourceMappingExtendedRepo.saveAll(allocationsToUpdate);

                if(workingDays!=0) {
                    totalAllocation = ResourceUtils.roundToTwoDecimalPlace(totalAllocation / workingDays);
                    totalRequested = ResourceUtils.roundToTwoDecimalPlace(totalRequested / workingDays);
                }

                currentRequest.setAllocatedFteAvg(BigDecimal.valueOf(totalAllocation));
                currentRequest.setRequestedFteAvg(totalRequested);
            }
            currentRequest.setAllocationStartDate(allocationStartDate);
            currentRequest.setAllocationEndDate(allocationEndDate);
            currentRequest.setResourceId(resourceId);
            currentRequest.setModifiedDate(new Date());
            currentRequest.setModifiedBy(modifiedBy);
            currentRequest = projectResourceMappingRepo.save(currentRequest);

            Optional<RequestedResources> requestedResources = requestedResourcesRepo.findById(currentRequest.getRequestId());
            if(requestedResources.isPresent()){
                RequestedResources currentRequestedResource = requestedResources.get();

                currentRequestedResource.setModifiedDate(new Date());
                currentRequestedResource.setModifiedBy(modifiedBy);
                currentRequestedResource.setAllocationStartDate(allocationStartDate);
                currentRequestedResource.setAllocationEndDate(allocationEndDate);
                currentRequestedResource.setRequestedResourceId(resourceId);
                currentRequestedResource.setNotify("0");
                currentRequestedResource.setNotifyPm("1");
                currentRequestedResource.setViewedByPMIds("");
                currentRequestedResource.setRmLastAction("Resource Swapped from " + secondResourceName + " to " + firstResourceName);

                if(currentRequest.getAllocatedFteAvg().doubleValue()>0d && currentRequest.getAllocatedFteAvg().doubleValue()<currentRequest.getRequestedFteAvg()){
                    currentRequestedResource.setRequestStatus(ResourceUtils.REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED);
                }
                else if(currentRequest.getAllocatedFteAvg().doubleValue()>currentRequest.getRequestedFteAvg()){
                    currentRequestedResource.setRequestStatus(ResourceUtils.REQUEST_RESOURCE_STATUS_OVER_FULFILLED);
                }
                else if(currentRequest.getAllocatedFteAvg().doubleValue()>0d && currentRequest.getAllocatedFteAvg().doubleValue()==currentRequest.getRequestedFteAvg()){
                    currentRequestedResource.setRequestStatus(ResourceUtils.REQUEST_RESOURCE_STATUS_COMPLETELY_FULFILLED);
                }
                else{
                    currentRequestedResource.setRequestStatus(ResourceUtils.REQUEST_RESOURCE_STATUS_OPEN);
                }

                requestedResourcesRepo.save(currentRequestedResource);
            }

        }

        return true;
    }

    public Object getSearchFiltersForUnnamedRequest(Integer requestId){

        Map<String, Object> returnData = null;

        Optional<ProjectResourceMapping> projectResourceMapping = projectResourceMappingRepo.findByRequestId(requestId);
        if(projectResourceMapping.isPresent()){
            ProjectResourceMapping currentMapping = projectResourceMapping.get();
            currentMapping.setAllocationStartDate(java.sql.Date.valueOf(new java.sql.Date(currentMapping.getAllocationStartDate().getTime()).toLocalDate()));
            currentMapping.setAllocationEndDate(java.sql.Date.valueOf(new java.sql.Date(currentMapping.getAllocationEndDate().getTime()).toLocalDate()));

            GanttChartData ganttChartData = null;
            RequestResourceFilters searchFilters = new RequestResourceFilters(currentMapping);

            String projectName = projectMgmtRepo.getProjectName(currentMapping.getProjectId());

//            get all allocations for map id
            List<AllResourceActiveAllocation> allocations = projectResourceMappingRepo.getAllocationDataByMapId(currentMapping.getMapId());

            List<AllResourceActiveAllocation> dailyAllocations = new ArrayList<>();
            List<AllocationDataByWeek> weeklyAllocations = new ArrayList<>();
            List<AllocationDataByMonth> monthlyAllocations = new ArrayList<>();

//            maps to keep track of weekly and monthly allocations
            Map<LocalDate, Double> weekAllocationTracker = new HashMap<>();
            Map<LocalDate, Double> monthAllocationTracker = new HashMap<>();


            for(AllResourceActiveAllocation eachAllocation: allocations){
                LocalDate localDate = new java.sql.Date(eachAllocation.getAllocationDate().getTime()).toLocalDate();

//                remove time data from daily allocations
                eachAllocation.setAllocationDate(java.sql.Date.valueOf(localDate));
                dailyAllocations.add(eachAllocation);

                weekAllocationTracker.merge(localDate.with(DayOfWeek.MONDAY), eachAllocation.getRequestedFte().doubleValue(), Double::sum);
                monthAllocationTracker.merge(localDate.withDayOfMonth(1), eachAllocation.getRequestedFte().doubleValue(), Double::sum);
            }

            LocalDate allocationStartDate = new java.sql.Date(currentMapping.getAllocationStartDate().getTime()).toLocalDate();
            LocalDate allocationEndDate = new java.sql.Date(currentMapping.getAllocationEndDate().getTime()).toLocalDate();

//            calculate weekly allocation average and add them to list
            for(Map.Entry<LocalDate, Double> entry: weekAllocationTracker.entrySet()){
                LocalDate weekStartDate = entry.getKey();
                LocalDate weekEndDate = weekStartDate.with(DayOfWeek.SUNDAY);
                if(weekStartDate.isBefore(allocationStartDate)){
                    weekStartDate = allocationStartDate;
                }
                if(weekEndDate.isAfter(allocationEndDate)){
                    weekEndDate = allocationEndDate;
                }
                Integer workingDays = ResourceUtils.numberOfWorkingDays(weekStartDate, weekEndDate);
                if(workingDays.equals(0)){
                    workingDays = 1;
                }
                Double weekAvgFte = ResourceUtils.roundToTwoDecimalPlace(entry.getValue()/workingDays);

                Date week = java.sql.Date.valueOf(entry.getKey());
                weeklyAllocations.add(new AllocationDataByWeek(week, entry.getKey().getYear(), null, weekAvgFte, null));
            }

//            calculate monthly allocation average and add them to list
            for(Map.Entry<LocalDate, Double> entry: monthAllocationTracker.entrySet()){
                LocalDate monthStartDate = entry.getKey();
                LocalDate monthEndDate = monthStartDate.withDayOfMonth(entry.getKey().lengthOfMonth());
                if(monthStartDate.isBefore(allocationStartDate)){
                    monthStartDate = allocationStartDate;
                }
                if(monthEndDate.isAfter(allocationEndDate)){
                    monthEndDate = allocationEndDate;
                }
                Integer workingDays = ResourceUtils.numberOfWorkingDays(monthStartDate, monthEndDate);
                Double monthAvgFte = ResourceUtils.roundToTwoDecimalPlace(entry.getValue()/workingDays);

//                Date month = java.sql.Date.valueOf(entry.getKey());
                monthlyAllocations.add(new AllocationDataByMonth(entry.getKey().getMonth().toString(), entry.getKey().getYear(), null, monthAvgFte, null));
            }

//            sort all allocations
            dailyAllocations.sort(Comparator.comparing(AllResourceActiveAllocation::getAllocationDate));
            weeklyAllocations.sort(Comparator.comparing(AllocationDataByWeek::getWeek));
            monthlyAllocations.sort(Comparator.comparing(AllocationDataByMonth::getYear).thenComparing((a,b)->{
                Month first = Month.valueOf(a.getMonth());
                Month second = Month.valueOf(b.getMonth());
                return first.compareTo(second);
            }));


            ganttChartData = new GanttChartData(currentMapping.getProjectId(), projectName, currentMapping.getAllocationStartDate(),
                    currentMapping.getAllocationEndDate(), false, dailyAllocations, weeklyAllocations, monthlyAllocations);

//            build return data
            returnData = new HashMap<>();
            returnData.put("searchFilters", searchFilters);
            returnData.put("ganttChartData", ganttChartData);
        }

        return returnData;
    }

    public Object assignResourceToUnnamedRequest(Integer requestId, Integer resourceId, String modifiedBy){
        Optional<ProjectResourceMapping> projectResourceMapping = projectResourceMappingRepo.findByRequestId(requestId);
        Object returnData = true;
        if(projectResourceMapping.isPresent()){
            returnData = swapResourceForRequestedResource(projectResourceMapping.get().getMapId(), resourceId, modifiedBy);
        }
        return returnData;
    }

    public Object getUserProfileDetails(String email){
        List<ResourceMgmt> resourceMgmtList = resourceMgmtRepo.getResourceByEmail(email);
        if(resourceMgmtList==null || resourceMgmtList.isEmpty()){
            return null;
        }

        Integer resourceId = resourceMgmtList.get(0).getResourceId();

        return getResourceDetails(new GetDateRange(resourceMgmtList.get(0).getDateOfJoin(), resourceMgmtList.get(0).getDateOfJoin()), resourceId);
    }
}
