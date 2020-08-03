package com.hyva.restopos.rest.Hiposservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyva.restopos.rest.Mapper.*;
import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.entities.Currency;
import com.hyva.restopos.rest.pojo.*;
import com.hyva.restopos.rest.repository.*;
import com.hyva.restopos.util.*;
import org.apache.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.codehaus.jackson.node.ObjectNode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import com.hyva.restopos.service.BasePosPrinterService;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class HiposService {

    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    CountryRepository countryRepository;
    @Autowired
    PaymentVoucherRepository voucherRepository;
    @Autowired
    PusherRepository pusherRepository;
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    AccountSetupRepository accountSetupRepository;
    @Autowired
    TableConfigRepository tableConfigRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StaffGroupRepository staffGroupRepository;
    @Autowired
    ItemRespository itemRespository;
    @Autowired
    TablesPosRepository tablesPosRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    StateRepository stateRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    UserAccountSetupRepository userAccountSetupRepository;
    @Autowired
    UserAccessRightsRepository userAccessRightsRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    SalesInvoiceDetailsRepository salesInvoiceDetailsRepository;
    @Autowired
    CompanyInfoRepository companyInfoRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    CompanyAccessRightsRepository companyAccessRightsRepository;
    @Autowired
    FormSetupRepository formSetupRepository;
    @Autowired
    SalesInvoiceRepository salesInvoiceRepository;
    @Autowired
    PosPaymentTypesRepository posPaymentTypesRepository;
    @Autowired
    TableReservationRepository tableReservationRepository;
    @Autowired
    SchedulerService schedulerService;
    @Autowired
    SmsServerRepository smsServerRepository;
    @Autowired
    CustomerNotificationRepository customerNotificationRepository;
    @Autowired
    DeliveryAreaRepository deliveryAreaRepository;
    @Autowired
    KitchenRepository kitchenRepository;
    @Value("${hisaas_domainame}")
    String hisaas_domainame;
    @Value("${buildType}")
    String buildType;
    int paginatedConstants = 10;

    @Autowired
    RestaurantTempDataRepository restaurantTempDataRepository;
    @Autowired
    RestaurantTokenRecordRepository restaurantTokenRecordRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    AccountTypeRepository accountTypeRepository;
    @Autowired
    AccountGroupRepository accountGroupRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    AccountMasterRepository accountMasterRepository;
    @Autowired
    GLTransactionRepository glTransactionRepository;
    @Autowired
    AirPayDetailsRepository airPayDetailsRepository;
    @Autowired
    @Qualifier("EPSONTMT82")
    BasePosPrinterService epsonPosPrinterService;

    @Autowired
    @Qualifier("GENERIC58MM")
    BasePosPrinterService generic58PosPrinterService;

    @Autowired
    @Qualifier("TVSRP3150")
    BasePosPrinterService tvsRP3150PosPrinterService;
    Logger logger = Logger.getLogger(HiposService.class);
    @Value("${printerType}")
    String printerType;

    public List<UserAccountSetUpDTO> retrieveUserAccountSetupListByNameOrCode(String searchText, String type) {
        List<UserAccountSetup> userAccountSetupList = new ArrayList<>();
        List<UserAccountSetUpDTO> userAccountSetupsdtoList = new ArrayList<>();
        if (StringUtils.isEmpty(type)) {
            userAccountSetupList = userAccountSetupRepository.findBy(searchText, searchText, searchText);
        } else {
            userAccountSetupList = userAccountSetupRepository.findByStatus(searchText, searchText, searchText, type);
        }
        userAccountSetupsdtoList = UserMapper.MapEntityToPojo(userAccountSetupList);
        for (UserAccountSetUpDTO userAccountSetUpDTO : userAccountSetupsdtoList) {
            if (userAccountSetUpDTO.isEmployeeflag() == true) {
                Employee employee = employeeRepository.findByEmployeeName(userAccountSetUpDTO.getFull_name());
//                userAccountSetUpDTO.setWaiterFlag(employee.isWaiterFlag());
//                userAccountSetUpDTO.setDeliveryFlag(employee.isDeliveryFlag());
            }
        }
        return userAccountSetupsdtoList;
    }

    @Transactional
    public UserAccountSetUpDTO createSaveUserAccountSetupDetails(UserAccountSetUpDTO saveUserAccountSetupDetails) throws Exception {
        saveUserAccountSetupDetails = saveOrUpDateUserAccountSetup(saveUserAccountSetupDetails);
        return saveUserAccountSetupDetails;
    }

    public UserAccessRights saveOrUpDateUserAccessRights(UserAccountSetUpDTO userAccountSetUpDTO) {
        UserAccessRights userAccessRights = new UserAccessRights();
        try {
            UserAccessRights saveUserAccountSetupDetails = userAccessRightsRepository.findOne(1L);
            if(saveUserAccountSetupDetails==null){
                saveUserAccountSetupDetails=addUserAccessRights();
                UserAccountSetup userAccountSetup=userAccountSetupRepository.findOne(1);
                userAccountSetup.setUserAccessRights(saveUserAccountSetupDetails);
                userAccountSetupRepository.save(userAccountSetup);
            }
            UserAccessRightsDTO userAccessRightdto=ObjectMapperUtils.map(saveUserAccountSetupDetails,UserAccessRightsDTO.class);
            userAccessRights=ObjectMapperUtils.map(userAccessRightdto,UserAccessRights.class);
            userAccessRights.setUserAccountSetupID(userAccountSetUpDTO.getUser_loginId());
            userAccessRightsRepository.save(userAccessRights);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return userAccessRights;
    }

    public UserAccountSetUpDTO saveOrUpDateUserAccountSetup(UserAccountSetUpDTO saveUserAccountSetupDetails) {
        UserAccountSetup userAccountSetup = null;
        try {
            if (saveUserAccountSetupDetails.getUseraccount_id() != 0) {
                UserAccountSetup userAccount=userAccountSetupRepository.findByUserloginIdAndUseraccountIdNotIn(saveUserAccountSetupDetails.getUser_loginId(),saveUserAccountSetupDetails.getUseraccount_id());
                if (userAccount!=null) {
                    saveUserAccountSetupDetails = null;
                    return saveUserAccountSetupDetails;
                }
                userAccountSetup = userAccountSetupRepository.findOne(saveUserAccountSetupDetails.getUseraccount_id());
            } else {
                UserAccountSetup userAccount = userAccountSetupRepository.findByUserloginId(saveUserAccountSetupDetails.getUser_loginId());
                if (userAccount != null) {
                    saveUserAccountSetupDetails = null;
                    return saveUserAccountSetupDetails;
                }
                userAccountSetup = new UserAccountSetup();
                userAccountSetup.setPasswordUser(saveUserAccountSetupDetails.getPasswordUser());
            }
            UserAccessRights accessRights = saveOrUpDateUserAccessRights(saveUserAccountSetupDetails);
            userAccountSetup.setUserloginId(saveUserAccountSetupDetails.getUser_loginId());
            userAccountSetup.setFullName(saveUserAccountSetupDetails.getFull_name());
            userAccountSetup.setSecurityQuestion(saveUserAccountSetupDetails.getSecurityQuestion());
            userAccountSetup.setSecurityAnswer(saveUserAccountSetupDetails.getSecurityAnswer());
            userAccountSetup.setPhone(saveUserAccountSetupDetails.getPhone());
            userAccountSetup.setEmail(saveUserAccountSetupDetails.getEmail());
            userAccountSetup.setEmployeeflag(saveUserAccountSetupDetails.isEmployeeflag());
            userAccountSetup.setKitchenflag(saveUserAccountSetupDetails.isKitchenflag());
            userAccountSetup.setKitchenId(saveUserAccountSetupDetails.getKitchenId());
            userAccountSetup.setStatus(saveUserAccountSetupDetails.getStatus());
            userAccountSetup.setUserAccessRights(accessRights);
            if(!org.apache.commons.lang3.StringUtils.isEmpty(saveUserAccountSetupDetails.getAccessLocations())&& org.apache.commons.lang3.StringUtils.equalsIgnoreCase(saveUserAccountSetupDetails.getAccessLocations(),"yes")){
                userAccountSetup.setAllLocations("true");
            } else {
                userAccountSetup.setAllLocations("false");
            }
            userAccountSetupRepository.save(userAccountSetup);
//            if (saveUserAccountSetupDetails.isEmployeeflag() == true) {
//                EmployeePojo employeeDTO = new EmployeePojo();
//                if (saveUserAccountSetupDetails.getUseraccount_id() != 0) {
//                    Employee employee = employeeRepository.findByEmployeeName(saveUserAccountSetupDetails.getFull_name());
//                    if (employee != null) {
//                        employeeDTO.se(employee.getEmployeeId());
//                    }
//                }
//                employeeDTO.setEmployeeName(saveUserAccountSetupDetails.getFull_name());
//                employeeDTO.setWaiterFlag(saveUserAccountSetupDetails.isWaiterFlag());
//                employeeDTO.setDeliveryFlag(saveUserAccountSetupDetails.isDeliveryFlag());
//                employeeDTO.setStatus(saveUserAccountSetupDetails.getStatus());
//                employeeDTO.setEmployeePhone(saveUserAccountSetupDetails.getPhone());
//                saveEmployee(employeeDTO);
//            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return saveUserAccountSetupDetails;
    }

    public UserAccountSetup deleteUserAccountSetupDetails(UserAccountSetup deleteDetails) {
        try {
            userAccountSetupRepository.delete(deleteDetails);
        } catch (Exception e) {
            deleteDetails = null;
            e.printStackTrace();
        }
        return deleteDetails;
    }

    public UserAccessRightsDTO createSaveUserAccessRights(UserAccessRightsDTO saveAccessRights) {
        UserAccessRightsDTO userAccessRights = saveUserAccessRights(saveAccessRights);
        return userAccessRights;
    }

    public UserAccessRightsDTO saveUserAccessRights(UserAccessRightsDTO saveUserAccountSetupDetails) {
        try {
//            UserAccountSetup userAccountSetup = userAccountSetupRepository.findOne(Integer.parseInt(saveUserAccountSetupDetails.getUserAccountSetupID()));
            UserAccessRights userAccessRights = userAccessRightsRepository.findOne(saveUserAccountSetupDetails.getUserid());
            userAccessRights.setRestDashboard(saveUserAccountSetupDetails.isRestDashboard());
            userAccessRights.setCountry(saveUserAccountSetupDetails.isCountry());
            userAccessRights.setState(saveUserAccountSetupDetails.isState());
            userAccessRights.setCurrency(saveUserAccountSetupDetails.isCurrency());
            userAccessRights.setTableZone(saveUserAccountSetupDetails.isTableZone());
            userAccessRights.setTablePos(saveUserAccountSetupDetails.isTablePos());
            userAccessRights.setAgent(saveUserAccountSetupDetails.isAgent());
            userAccessRights.setEmployee(saveUserAccountSetupDetails.isEmployee());
            userAccessRights.setCustomer(saveUserAccountSetupDetails.isCustomer());
            userAccessRights.setCategory(saveUserAccountSetupDetails.isCategory());
            userAccessRights.setItem(saveUserAccountSetupDetails.isItem());
            userAccessRights.setPaymentMethod(saveUserAccountSetupDetails.isPaymentMethod());
            userAccessRights.setUserAccountSetUp(saveUserAccountSetupDetails.isUserAccountSetUp());
            userAccessRights.setAccountType(saveUserAccountSetupDetails.isAccountType());
            userAccessRights.setAccountGroup(saveUserAccountSetupDetails.isAccountGroup());
            userAccessRights.setChartOfAccounts(saveUserAccountSetupDetails.isChartOfAccounts());
            userAccessRights.setContact(saveUserAccountSetupDetails.isContact());
            userAccessRights.setConfiguration(saveUserAccountSetupDetails.isConfiguration());
            userAccessRights.setShift(saveUserAccountSetupDetails.isShift());
            userAccessRights.setPaymentVoucher(saveUserAccountSetupDetails.isPaymentVoucher());
            userAccessRights.setSmsService(saveUserAccountSetupDetails.isSmsService());
            userAccessRights.setRestaurantInvReg(saveUserAccountSetupDetails.isRestaurantInvReg());
            userAccessRights.setMonthEndReport(saveUserAccountSetupDetails.isMonthEndReport());
            userAccessRights.setItemSalesReport(saveUserAccountSetupDetails.isItemSalesReport());
            userAccessRights.setShiftReport(saveUserAccountSetupDetails.isShiftReport());
            userAccessRights.setCancelledItemReg(saveUserAccountSetupDetails.isCancelledItemReg());
            userAccessRights.setFreeMealReport(saveUserAccountSetupDetails.isFreeMealReport());
            userAccessRights.setDiscountReport(saveUserAccountSetupDetails.isDiscountReport());
            userAccessRights.setTableWiseReport(saveUserAccountSetupDetails.isTableWiseReport());
            userAccessRights.setAgentReport(saveUserAccountSetupDetails.isAgentReport());
            userAccessRights.setCancelledInv(saveUserAccountSetupDetails.isCancelledInv());
            userAccessRights.setDayEndReport(saveUserAccountSetupDetails.isDayEndReport());
            userAccessRights.setOnlineInvReport(saveUserAccountSetupDetails.isOnlineInvReport());
            userAccessRights.setWaiterReport(saveUserAccountSetupDetails.isWaiterReport());
            userAccessRights.setReceiptAddCnct(saveUserAccountSetupDetails.isReceiptAddCnct());
            userAccessRights.setReceiptSelectAcnt(saveUserAccountSetupDetails.isReceiptSelectAcnt());
            userAccessRights.setReceiptRemoveAcnt(saveUserAccountSetupDetails.isReceiptRemoveAcnt());
            userAccessRights.setReceiptClearAll(saveUserAccountSetupDetails.isReceiptClearAll());
            userAccessRights.setReceiptSave(saveUserAccountSetupDetails.isReceiptSave());
            userAccessRights.setReceiptPrintlist(saveUserAccountSetupDetails.isReceiptPrintlist());
            userAccessRights.setExpenseAddCnct(saveUserAccountSetupDetails.isExpenseAddCnct());
            userAccessRights.setExpenseSelectAcnt(saveUserAccountSetupDetails.isExpenseSelectAcnt());
            userAccessRights.setExpenseRemoveAcnt(saveUserAccountSetupDetails.isExpenseRemoveAcnt());
            userAccessRights.setExpenseClearAll(saveUserAccountSetupDetails.isExpenseClearAll());
            userAccessRights.setExpenseSave(saveUserAccountSetupDetails.isExpenseSave());
            userAccessRights.setExpensePrintlist(saveUserAccountSetupDetails.isExpensePrintlist());
            userAccessRights.setJESelectAcnt(saveUserAccountSetupDetails.isJESelectAcnt());
            userAccessRights.setJERemoveAcnt(saveUserAccountSetupDetails.isJERemoveAcnt());
            userAccessRights.setJECancel(saveUserAccountSetupDetails.isJECancel());
            userAccessRights.setJEDraft(saveUserAccountSetupDetails.isJEDraft());
            userAccessRights.setJEPrintlist(saveUserAccountSetupDetails.isJEPrintlist());
            userAccessRights.setJESave(saveUserAccountSetupDetails.isJESave());

            userAccessRights.setRestDineIn(saveUserAccountSetupDetails.isRestDineIn());
            userAccessRights.setRestDelivery(saveUserAccountSetupDetails.isRestDelivery());
            userAccessRights.setRestTakeaway(saveUserAccountSetupDetails.isRestTakeaway());
            userAccessRights.setRestTableReservation(saveUserAccountSetupDetails.isRestTableReservation());
            userAccessRights.setRestDigiOrders(saveUserAccountSetupDetails.isRestDigiOrders());
            userAccessRights.setRestOnlineDelivery(saveUserAccountSetupDetails.isRestOnlineDelivery());
            userAccessRights.setRestTable(saveUserAccountSetupDetails.isRestTable());
            userAccessRights.setRestWaiter(saveUserAccountSetupDetails.isRestWaiter());
            userAccessRights.setRestItemSearch(saveUserAccountSetupDetails.isRestItemSearch());
            userAccessRights.setRestItemAdd(saveUserAccountSetupDetails.isRestItemAdd());
            userAccessRights.setRestAgent(saveUserAccountSetupDetails.isRestAgent());
            userAccessRights.setRestCustomer(saveUserAccountSetupDetails.isRestCustomer());
            userAccessRights.setRestCustomerAdd(saveUserAccountSetupDetails.isRestCustomerAdd());
            userAccessRights.setRestPax(saveUserAccountSetupDetails.isRestPax());
            userAccessRights.setRestSave(saveUserAccountSetupDetails.isRestSave());
            userAccessRights.setRestSavePrint(saveUserAccountSetupDetails.isRestSavePrint());
            userAccessRights.setRestSaveSms(saveUserAccountSetupDetails.isRestSaveSms());
            userAccessRights.setRestCustomerBill(saveUserAccountSetupDetails.isRestCustomerBill());
            userAccessRights.setRestSplitbill(saveUserAccountSetupDetails.isRestSplitbill());
            userAccessRights.setRestMergeTable(saveUserAccountSetupDetails.isRestMergeTable());
            userAccessRights.setRestPrintList(saveUserAccountSetupDetails.isRestPrintList());
            userAccessRights.setRestDailyReport(saveUserAccountSetupDetails.isRestDailyReport());
            userAccessRights.setRestVoucher(saveUserAccountSetupDetails.isRestVoucher());
            userAccessRights.setRestOffers(saveUserAccountSetupDetails.isRestOffers());

            userAccessRights.setKitchenTokens(saveUserAccountSetupDetails.isKitchenTokens());
            userAccessRights.setWaiterTokens(saveUserAccountSetupDetails.isWaiterTokens());
            userAccessRights.setRestDashboard(saveUserAccountSetupDetails.isRestDashboard());
            userAccessRights.setRestaurant(saveUserAccountSetupDetails.isRestaurant());
            userAccessRights.setTokens(saveUserAccountSetupDetails.isTokens());
            userAccessRights.setMasters(saveUserAccountSetupDetails.isMasters());
            userAccessRights.setFinance(saveUserAccountSetupDetails.isFinance());
            userAccessRights.setCompanyInfo(saveUserAccountSetupDetails.isCompanyInfo());
            userAccessRights.setReports(saveUserAccountSetupDetails.isReports());
            userAccessRights.setRestaurantReports(saveUserAccountSetupDetails.isRestaurantReports());
            userAccessRights.setExpense(saveUserAccountSetupDetails.isExpense());
            userAccessRights.setReceipt(saveUserAccountSetupDetails.isReceipt());
            userAccessRights.setJournalEntry(saveUserAccountSetupDetails.isJournalEntry());
            userAccessRights.setPandlreport(saveUserAccountSetupDetails.isPandlreport());

            userAccessRightsRepository.save(userAccessRights);

        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return saveUserAccountSetupDetails;
    }

    public UserAccountSetup getUserAccountSetupObj(int userId) {
        UserAccountSetup accountSetup = userAccountSetupRepository.findOne(userId);
        return accountSetup;
    }
    @Transactional
    public List<PaymentVoucher> getCouponList(){
        List<PaymentVoucher> paymentVoucherList = voucherRepository.findAllByStatusAndTypeAndDateaddedLessThanEqualAndFixeddateGreaterThanEqual("Active","true",new Date(),new Date());
        return paymentVoucherList;
    }

    @Transactional
    public List<PaymentVoucher> validateCoupon(String coupon){
        List<PaymentVoucher> paymentVoucherList = voucherRepository.findAllByCodeAndDateaddedLessThanEqualAndFixeddateGreaterThanEqual(coupon,new Date(),new Date());
        return paymentVoucherList;
    }
    public UserAccessRightsDTO retrieveEditUserAccessRightsByNameOrCode(UserAccountSetup userAccObj) {
        UserAccessRights userAccessRights = userAccessRightsRepository.findOne(userAccObj.getUserAccessRights().getId());
        UserAccessRightsDTO accessRightsDTO=ObjectMapperUtils.map(userAccessRights,UserAccessRightsDTO.class);
        accessRightsDTO.setUserid(userAccessRights.getId());
        return accessRightsDTO;
    }

    public CompanyAccessRightsDTO getCompanyAccessRights() {
        CompanyAccessRightsDTO companyAccessRightsDTO = new CompanyAccessRightsDTO();
        List<String> projectModuleIdList = new ArrayList<>();
        if (projectModuleIdList.size() > 0) {
            List<CompanyAccessRights> company = companyAccessRightsRepository.findAll();
            for (CompanyAccessRights cmp : company) {
                if (cmp.isCountry() == true) {
                    companyAccessRightsDTO.setCountry(true);
                }
                if (cmp.isState() == true) {
                    companyAccessRightsDTO.setState(true);
                }
                if (cmp.isCurrency() == true) {
                    companyAccessRightsDTO.setCurrency(true);
                }
                if (cmp.isTableZone() == true) {
                    companyAccessRightsDTO.setTableZone(true);
                }
                if (cmp.isTablePos() == true) {
                    companyAccessRightsDTO.setTablePos(true);
                }
                if (cmp.isAgent() == true) {
                    companyAccessRightsDTO.setAgent(true);
                }
                if (cmp.isEmployee() == true) {
                    companyAccessRightsDTO.setEmployee(true);
                }
                if (cmp.isCustomer() == true) {
                    companyAccessRightsDTO.setCustomer(true);
                }
                if (cmp.isCategory() == true) {
                    companyAccessRightsDTO.setCategory(true);
                }
                if (cmp.isItem() == true) {
                    companyAccessRightsDTO.setItem(true);
                }
                if (cmp.isPaymentMethod() == true) {
                    companyAccessRightsDTO.setPaymentMethod(true);
                }
                if (cmp.isUserAccountSetUp() == true) {
                    companyAccessRightsDTO.setUserAccountSetUp(true);
                }
                if (cmp.isAccountType() == true) {
                    companyAccessRightsDTO.setAccountType(true);
                }
                if (cmp.isAccountGroup() == true) {
                    companyAccessRightsDTO.setAccountGroup(true);
                }
                if (cmp.isChartOfAccounts() == true) {
                    companyAccessRightsDTO.setChartOfAccounts(true);
                }
                if (cmp.isContact() == true) {
                    companyAccessRightsDTO.setContact(true);
                }
                if (cmp.isConfiguration() == true) {
                    companyAccessRightsDTO.setConfiguration(true);
                }
                if (cmp.isShift() == true) {
                    companyAccessRightsDTO.setShift(true);
                }
                if (cmp.isPaymentVoucher() == true) {
                    companyAccessRightsDTO.setPaymentVoucher(true);
                }
                if (cmp.isSmsService() == true) {
                    companyAccessRightsDTO.setSmsService(true);
                }
                if (cmp.isRestaurantInvReg() == true) {
                    companyAccessRightsDTO.setRestaurantInvReg(true);
                }
                if (cmp.isMonthEndReport() == true) {
                    companyAccessRightsDTO.setMonthEndReport(true);
                }
                if (cmp.isItemSalesReport() == true) {
                    companyAccessRightsDTO.setItemSalesReport(true);
                }
                if (cmp.isShiftReport() == true) {
                    companyAccessRightsDTO.setShiftReport(true);
                }
                if (cmp.isCancelledItemReg() == true) {
                    companyAccessRightsDTO.setCancelledItemReg(true);
                }
                if (cmp.isFreeMealReport() == true) {
                    companyAccessRightsDTO.setFreeMealReport(true);
                }
                if (cmp.isDiscountReport() == true) {
                    companyAccessRightsDTO.setDiscountReport(true);
                }
                if (cmp.isTableWiseReport() == true) {
                    companyAccessRightsDTO.setTableWiseReport(true);
                }
                if (cmp.isAgentReport() == true) {
                    companyAccessRightsDTO.setAgentReport(true);
                }
                if (cmp.isCancelledInv() == true) {
                    companyAccessRightsDTO.setCancelledInv(true);
                }
                if (cmp.isDayEndReport() == true) {
                    companyAccessRightsDTO.setDayEndReport(true);
                }
                if (cmp.isOnlineInvReport() == true) {
                    companyAccessRightsDTO.setOnlineInvReport(true);
                }
                if (cmp.isWaiterReport() == true) {
                    companyAccessRightsDTO.setWaiterReport(true);
                }
                if (cmp.isReceiptAddCnct() == true) {
                    companyAccessRightsDTO.setReceiptAddCnct(true);
                }
                if (cmp.isReceiptSelectAcnt() == true) {
                    companyAccessRightsDTO.setReceiptSelectAcnt(true);
                }
                if (cmp.isReceiptRemoveAcnt() == true) {
                    companyAccessRightsDTO.setReceiptRemoveAcnt(true);
                }
                if (cmp.isReceiptSave() == true) {
                    companyAccessRightsDTO.setReceiptSave(true);
                }
                if (cmp.isReceiptClearAll() == true) {
                    companyAccessRightsDTO.setReceiptClearAll(true);
                }
                if (cmp.isReceiptPrintlist() == true) {
                    companyAccessRightsDTO.setReceiptClearAll(true);
                }
                if (cmp.isExpenseAddCnct() == true) {
                    companyAccessRightsDTO.setExpenseAddCnct(true);
                }
                if (cmp.isExpenseSelectAcnt() == true) {
                    companyAccessRightsDTO.setExpenseSelectAcnt(true);
                }
                if (cmp.isExpenseRemoveAcnt() == true) {
                    companyAccessRightsDTO.setExpenseRemoveAcnt(true);
                }
                if (cmp.isExpenseSave() == true) {
                    companyAccessRightsDTO.setExpenseSave(true);
                }
                if (cmp.isExpenseClearAll() == true) {
                    companyAccessRightsDTO.setExpenseClearAll(true);
                }
                if (cmp.isExpensePrintlist() == true) {
                    companyAccessRightsDTO.setExpensePrintlist(true);
                }
                if (cmp.isJESelectAcnt() == true) {
                    companyAccessRightsDTO.setJESelectAcnt(true);
                }
                if (cmp.isJESave() == true) {
                    companyAccessRightsDTO.setJESave(true);
                }
                if (cmp.isJECancel() == true) {
                    companyAccessRightsDTO.setJECancel(true);
                }
                if (cmp.isJEDraft() == true) {
                    companyAccessRightsDTO.setJEDraft(true);
                }
                if (cmp.isJEPrintlist() == true) {
                    companyAccessRightsDTO.setJEPrintlist(true);
                }
                if (cmp.isJERemoveAcnt() == true) {
                    companyAccessRightsDTO.setJERemoveAcnt(true);
                }
                if (cmp.isRestDineIn() == true) {
                    companyAccessRightsDTO.setRestDineIn(true);
                }
                if (cmp.isRestDigiOrders() == true) {
                    companyAccessRightsDTO.setRestDigiOrders(true);
                }
                if (cmp.isRestDelivery() == true) {
                    companyAccessRightsDTO.setRestDelivery(true);
                }
                if (cmp.isRestOnlineDelivery() == true) {
                    companyAccessRightsDTO.setRestOnlineDelivery(true);
                }
                if (cmp.isRestTableReservation() == true) {
                    companyAccessRightsDTO.setRestTableReservation(true);
                }
                if (cmp.isRestTakeaway() == true) {
                    companyAccessRightsDTO.setRestTakeaway(true);
                }
                if (cmp.isRestTable() == true) {
                    companyAccessRightsDTO.setRestTable(true);
                }
                if (cmp.isRestAgent() == true) {
                    companyAccessRightsDTO.setRestAgent(true);
                }
                if (cmp.isRestWaiter() == true) {
                    companyAccessRightsDTO.setRestWaiter(true);
                }
                if (cmp.isRestCustomer() == true) {
                    companyAccessRightsDTO.setRestCustomer(true);
                }
                if (cmp.isRestCustomerAdd() == true) {
                    companyAccessRightsDTO.setRestCustomerAdd(true);
                }
                if (cmp.isRestPax() == true) {
                    companyAccessRightsDTO.setRestPax(true);
                }
                if (cmp.isRestSave() == true) {
                    companyAccessRightsDTO.setRestSave(true);
                }
                if (cmp.isRestSavePrint() == true) {
                    companyAccessRightsDTO.setRestSavePrint(true);
                }
                if (cmp.isRestSaveSms() == true) {
                    companyAccessRightsDTO.setRestSaveSms(true);
                }
                if (cmp.isRestCustomerBill() == true) {
                    companyAccessRightsDTO.setRestCustomerBill(true);
                }
                if (cmp.isRestClearall() == true) {
                    companyAccessRightsDTO.setRestClearall(true);
                }
                if (cmp.isRestSplitbill() == true) {
                    companyAccessRightsDTO.setRestSplitbill(true);
                }
                if (cmp.isRestMergeTable() == true) {
                    companyAccessRightsDTO.setRestMergeTable(true);
                }
                if (cmp.isRestChangeTable() == true) {
                    companyAccessRightsDTO.setRestChangeTable(true);
                }
                if (cmp.isRestPrintList() == true) {
                    companyAccessRightsDTO.setRestPrintList(true);
                }
                if (cmp.isRestVoucher() == true) {
                    companyAccessRightsDTO.setRestVoucher(true);
                }
                if (cmp.isRestOffers() == true) {
                    companyAccessRightsDTO.setRestOffers(true);
                }
                if (cmp.isRestDailyReport() == true) {
                    companyAccessRightsDTO.setRestDailyReport(true);
                }
                if (cmp.isRestItemAdd() == true) {
                    companyAccessRightsDTO.setRestItemAdd(true);
                }
                if (cmp.isRestItemSearch() == true) {
                    companyAccessRightsDTO.setRestItemSearch(true);
                }


                if (cmp.isKitchenTokens() == true) {
                    companyAccessRightsDTO.setKitchenTokens(true);
                }
                if (cmp.isWaiterTokens() == true) {
                    companyAccessRightsDTO.setWaiterTokens(true);
                }
                if (cmp.isRestDashboard() == true) {
                    companyAccessRightsDTO.setRestDashboard(true);
                }
                if (cmp.isRestaurant() == true) {
                    companyAccessRightsDTO.setRestaurant(true);
                }
                if (cmp.isTokens() == true) {
                    companyAccessRightsDTO.setTokens(true);
                }
                if (cmp.isMasters() == true) {
                    companyAccessRightsDTO.setMasters(true);
                }
                if (cmp.isFinance() == true) {
                    companyAccessRightsDTO.setFinance(true);
                }
                if (cmp.isCompanyInfo() == true) {
                    companyAccessRightsDTO.setCompanyInfo(true);
                }
                if (cmp.isReports() == true) {
                    companyAccessRightsDTO.setReports(true);
                }
                if (cmp.isRestaurantReports() == true) {
                    companyAccessRightsDTO.setRestaurantReports(true);
                }
                if (cmp.isReceipt() == true) {
                    companyAccessRightsDTO.setReceipt(true);
                }
                if (cmp.isExpense() == true) {
                    companyAccessRightsDTO.setExpense(true);
                }
                if (cmp.isJournalEntry() == true) {
                    companyAccessRightsDTO.setJournalEntry(true);
                }
                if (cmp.isPandlreport() == true) {
                    companyAccessRightsDTO.setPandlreport(true);
                }

            }
        }
        return companyAccessRightsDTO;
    }

    public  List<String> getTablesPosMessages(){
        List<TablesPos> tablesPosList = tablesPosRepository.findAllByMessageNotNull();
        List<String> messageList = tablesPosList.stream().distinct().map(TablesPos::getMessage).collect(Collectors.toList());
        return messageList;
    }

    @Transactional
    public List<CustomerNotificationsPojo> getAllRestaurantNotifications(String status){
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "custNotiId"));
        List<CustomerNotifications> customerNotifications = new ArrayList<>();
        if(!StringUtils.isEmpty(status)) {
            List<String> list=new ArrayList<>();
            if(StringUtils.pathEquals(status,"Acknowledged")){
                list.add("Acknowledged");
                list.add("Food Ready");
            }else {
                list.add(status);
            }
            customerNotifications = customerNotificationRepository.findAllByStatusInAndTableNameIsNull(list, sort);
        }else {
            customerNotifications  = customerNotificationRepository.findAllByStatusNotInAndTableNameIsNull("pending", sort);
        }
        List<CustomerNotificationsPojo> customerNotificationsPojos=ObjectMapperUtils.mapAll(customerNotifications,CustomerNotificationsPojo.class);
        return customerNotificationsPojos;
    }
    @Transactional
    public List<CustomerNotificationsPojo> getAllDigiOrders(String status){
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "custNotiId"));
        List<CustomerNotifications> customerNotifications = new ArrayList<>();
        if(!StringUtils.isEmpty(status)) {
            List<String> list=new ArrayList<>();
            if(StringUtils.pathEquals(status,"Acknowledged")){
                list.add("Acknowledged");
                list.add("Food Ready");
            }else {
                list.add(status);
            }
            customerNotifications = customerNotificationRepository.findAllByStatusInAndTableNameNotNull(list,sort);
        }else {
            customerNotifications = customerNotificationRepository.findAllByStatusNotInAndTableNameNotNull("pending",sort);
        }
        List<CustomerNotificationsPojo> customerNotificationsPojos=ObjectMapperUtils.mapAll(customerNotifications,CustomerNotificationsPojo.class);
        return customerNotificationsPojos;
    }

    public String  getStatusChange(String status,String item,String order) throws Exception{
        Company company = companyRepository.findAllByStatus("Active");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("connect_id", company.getConnectNo());
        jsonObject.put("order_id", order);
        jsonObject.put("status", status);
        jsonObject.put("itemname", item);
        String url = readDomainNameRestoOrder()+"/services/Account/kdsStatuschange";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String jsonString = responseEntity.getBody();
        return jsonString;

    }

    public List<TableReservationPojo> getTableReservationList(){
        List<TableReservation> tableReservationList = tableReservationRepository.findAll();
            List<TableReservationPojo> list = new ArrayList<>();
        for (TableReservation tableReservation : tableReservationList) {
            TableReservationPojo tableReservationPojo = new TableReservationPojo();
            tableReservationPojo.setId(tableReservation.getId());
            tableReservationPojo.setGuestName(tableReservation.getGuestName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = simpleDateFormat.format(tableReservation.getDate());
            tableReservationPojo.setDate(date);
            tableReservationPojo.setTime(tableReservation.getTime());
            tableReservationPojo.setNoOfPersons(tableReservation.getNoOfPersons());
            tableReservationPojo.setStatus(tableReservation.getStatus());
            tableReservationPojo.setTableName(tableReservation.getTableName());
            list.add(tableReservationPojo);
        }
        return list;
    }
    public TableReservation saveTableReservation(TableReservationPojo tableReservationPojo) throws Exception{
        TableReservation tableReservation = new TableReservation();
        tableReservation.setId(tableReservationPojo.getId());
        tableReservation.setGuestName(tableReservationPojo.getGuestName());
        tableReservation.setNoOfPersons(tableReservationPojo.getNoOfPersons());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(tableReservationPojo.getDate());
        tableReservation.setDate(date);
        tableReservation.setTime(tableReservationPojo.getTime());
        tableReservation.setStatus(tableReservationPojo.getStatus());
        tableReservation.setSpecialRequests(tableReservationPojo.getSpecialRequests());
        tableReservationRepository.save(tableReservation);
        return tableReservation;
    }
    public TableReservation saveSelectedTableName(Long tableId,Long id){
        TableReservation tableReservation = tableReservationRepository.findAllById(id);
        tableReservation.setTableName(String.valueOf(tableId));
        tableReservationRepository.save(tableReservation);
        MailSchedulerData mailSchedulerData = new MailSchedulerData();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(tableReservation.getDate());
        mailSchedulerData.setScheduleDate(date);
        String[] times=null;
        times = tableReservation.getTime().split(":");
        Integer startTime = Integer.parseInt(times[0]);
        Integer endTime =Integer.parseInt(times[1]);
        if(endTime==30){
            endTime = 00;
        }else if(endTime==00){
            startTime = startTime-1;
            endTime = 30;
        }
        mailSchedulerData.setScheduleTime(String.valueOf(startTime+""+':'+""+endTime));
        mailSchedulerData.setReservationId(tableReservation.getId().toString());
        mailSchedulerData.setTableName(String.valueOf(tableId));
        mailSchedulerData.setScheduleType("Yearly");
        mailSchedulerData.setReportName("Table Reservation");
        mailSchedulerData.setDbKeyword(TenantContext.getCurrentTenant());
        schedulerService.schedule(mailSchedulerData);
        return tableReservation;

    }

    public List<TablesPosPojo> getTablesListForReserved( Long id) {
        TableReservation tableReservation = tableReservationRepository.findAllById(id);
        List<TablesPosPojo> tableList = getTableList(null,"Active");
        Calendar cal = Calendar.getInstance(); // get calendar instance
        for (TablesPosPojo tablesPos : tableList) {
            List<String> stringList = new ArrayList<>();
            String[] times = null;
            times = tableReservation.getTime().split(":");
            List<java.sql.Time> intervals = new ArrayList<>(25);
            java.sql.Time startTime = new java.sql.Time(Integer.parseInt(times[0]) - 1, Integer.parseInt(times[1]), 0);
            intervals.add(startTime);
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]) - 1); // set hour to midnight
            cal.set(Calendar.MINUTE, Integer.parseInt(times[1])); // set minute in hour
            cal.set(Calendar.SECOND, 0); // set second in minute
            cal.set(Calendar.MILLISECOND, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]) + 1); // set hour to midnight
            calendar.set(Calendar.MINUTE, Integer.parseInt(times[1])); // set minute in hour
            calendar.set(Calendar.SECOND, 0); // set second in minute
            calendar.set(Calendar.MILLISECOND, 0);
            while (cal.getTime().before(calendar.getTime())) {
                cal.add(Calendar.MINUTE, 30);
                intervals.add(new java.sql.Time(cal.getTimeInMillis()));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            for (java.sql.Time time : intervals) {
                stringList.add(sdf.format(time));
            }
            List<TableReservation> tableReservations= tableReservationRepository.findAllByTableNameAndDateAndTimeIn(String.valueOf(tablesPos.getTable_name()),tableReservation.getDate(),stringList);

            if (tableReservations.size() > 0) {
                tablesPos.setTable_status("Reserved");
            }

        }
        return tableList;
    }

    public String saveCountry(CountryPojo countryPojo)throws Exception{
        if(StringUtils.pathEquals(buildType,"desktop")) {
            Country country = new Country();
            if (countryPojo.getCountry_id() != null) {
                country = countryRepository.findAllByCountryNameAndCountryIdNotIn(countryPojo.getCountry_name(), countryPojo.getCountry_id());
            } else {
                country = countryRepository.findAllByCountryName(countryPojo.getCountry_name());

            }
            JSONObject jsonObject = new JSONObject();
            if (country == null) {
                country = CountryMapper.MapPojoToEntity(countryPojo);
                countryRepository.save(country);
                jsonObject.put("status","success");
                jsonObject.put("message","Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status","fail");
                jsonObject.put("message","Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("country_id",countryPojo.getCountry_id());
            jsonObject.put("countryName",countryPojo.getCountry_name());
//            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(countryPojo.getStatus(), "Active")) {
//                countryPojo.setStatus("1");
//            } else {
//                countryPojo.setStatus("0");
//            }
            jsonObject.put("status",countryPojo.getStatus());
            String url = readDomainName() + "/services/Litecall/saveCountry";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

    public String saveEmployee(EmployeePojo employeePojo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            Employee employee = new Employee();
            if (employeePojo.getStaff_id() != null) {
                employee = employeeRepository.findAllByEmployeeNameAndEmployeeIdNotIn(employeePojo.getStaff_name(), employeePojo.getStaff_id());
            } else {
                employee = employeeRepository.findAllByEmployeeName(employeePojo.getStaff_name());

            }
            JSONObject jsonObject = new JSONObject();
            if (employee == null) {
                employee = EmployeeMapper.MapEmployeePojoToEntity(employeePojo);
                employeeRepository.save(employee);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("staff_name",employeePojo.getStaff_name());
            jsonObject.put("staff_group_id","1");
            jsonObject.put("staff_email",employeePojo.getStaff_email());
            jsonObject.put("username",employeePojo.getUsername());
            jsonObject.put("password",employeePojo.getPassword());
            jsonObject.put("incentives",employeePojo.getIncentives());
            jsonObject.put("permission",employeePojo.getPermission());
            jsonObject.put("staff_id",employeePojo.getStaff_id());
            jsonObject.put("staff_status",employeePojo.getStaff_status());
            Gson gson = new Gson();
            String url = readDomainName() + "/services/Litecall/saveStaff";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

    public String savePaymentmethod(PaymentMethodDTO paymentMethodDTO)throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            PaymentMethod paymentMethod = new PaymentMethod();
            if (paymentMethodDTO.getPaymentmethodId() != null) {
                paymentMethod = paymentMethodRepository.findAllByPaymentmethodNameAndPaymentmethodIdNotIn(paymentMethodDTO.getPaymentmethodName(), paymentMethodDTO.getPaymentmethodId());
            } else {
                paymentMethod = paymentMethodRepository.findAllByPaymentmethodName(paymentMethodDTO.getPaymentmethodName());

            }
            JSONObject jsonObject = new JSONObject();
            if (paymentMethod == null) {
                paymentMethod = PaymentMethodMapper.MapPaymentmethodPojoToEntity(paymentMethodDTO);
                paymentMethodRepository.save(paymentMethod);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("paymentmethodId",paymentMethodDTO.getPaymentmethodId());
            jsonObject.put("paymentmethodName",paymentMethodDTO.getPaymentmethodName());
            jsonObject.put("paymentmethodDescription",paymentMethodDTO.getPaymentmethodDescription());
            jsonObject.put("paymentmethodType",paymentMethodDTO.getPaymentmethodType());
            jsonObject.put("defaultType","");
            jsonObject.put("merchantId","");
            jsonObject.put("secretKey","");
            jsonObject.put("uniqueId","");
            jsonObject.put("status",paymentMethodDTO.getStatus());
            Gson gson = new Gson();
            String url = readDomainName() + "/services/Litecall/savepaymentMethod";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

    public String saveDiscountmethod(DiscountPojo discountPojo)  throws Exception{
        if(StringUtils.pathEquals(buildType,"desktop")) {
            Discount discount = new Discount();
            if (discountPojo.getId() != null) {
                discount = discountRepository.findAllByMenuidAndIdNotIn(discountPojo.getMenu_id(), discountPojo.getId());
            } else {
                discount = discountRepository.findAllByMenuid(discountPojo.getMenu_id());

            }
            JSONObject jsonObject = new JSONObject();
            if (discount == null) {
                discount = DiscountMapper.MapDiscountPojoToEntity(discountPojo);
                discountRepository.save(discount);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(discountPojo.getStatus(), "Active")) {
            jsonObject.put("status","1");
            } else {
                jsonObject.put("status","0");
            }
            String url = readDomainName() + "/services/Litecall/saveitemwiseDiscount";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(jsonObject), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }



    public String saveState(StatePojo statePojo) throws Exception{
        if(StringUtils.pathEquals(buildType,"desktop")) {
            State state = new State();
            if (statePojo.getId() != null) {
                state = stateRepository.findAllByStateNameAndStateIdNotIn(statePojo.getName(), statePojo.getId());
            } else {
                state = stateRepository.findAllByStateName(statePojo.getName());

            }
            JSONObject jsonObject=new JSONObject();
            if (state == null) {
                state = EmployeeMapper.MapStateEntityToPojo(statePojo);
//                if (!StringUtils.isEmpty(statePojo.getCountryName())) {
//                    Country country = countryRepository.findAllByCountryName(statePojo.getCountryName());
//                    state.setCountryId(country.getCountryId());
//                } else {
//                    state.setCountryId(statePojo.getCountryId());
//                }
                stateRepository.save(state);
                jsonObject.put("status","success");
                jsonObject.put("message","Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status","fail");
                jsonObject.put("message","Duplicate Entry");
                return jsonObject.toString();
            }
        }else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("name",statePojo.getName());
            jsonObject.put("country_id",statePojo.getCountry_id());
            jsonObject.put("id",statePojo.getId());
            jsonObject.put("status",statePojo.getStatus());
            String url = readDomainName() + "/services/Litecall/SaveState";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

    public String saveCity(CityPojo cityPojo) throws Exception{
        if(StringUtils.pathEquals(buildType,"desktop")) {
            City city = new City();
            if (cityPojo.getId() != null) {
                city = cityRepository.findAllByNameAndIdNotIn(cityPojo.getName(), cityPojo.getId());
            } else {
                city = cityRepository.findAllByName(cityPojo.getName());

            }
            JSONObject jsonObject = new JSONObject();
            if (city == null) {
                city = CityMapper.MapPojoToEntity(cityPojo);
                cityRepository.save(city);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("id",cityPojo.getId());
            jsonObject.put("name",cityPojo.getName());
            jsonObject.put("state_id",cityPojo.getState_id());
            jsonObject.put("country_id",cityPojo.getCountry_id());
            jsonObject.put("status",cityPojo.getStatus());
            String url = readDomainName() + "/services/Litecall/SaveCity";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

    public List<EmployeePojo> getEmployeeList() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList = employeeRepository.findAllByStatus("Active");
        List<EmployeePojo> employeePojoList = ObjectMapperUtils.mapAll(employeeList, EmployeePojo.class);
        return employeePojoList;
    }

    public List<PaymentMethodDTO> getPaymentMethodList(){
        List<PaymentMethod> paymentMethodList = new ArrayList<>();
        paymentMethodList = paymentMethodRepository.findAllByStatus("Active");
        List<PaymentMethodDTO> paymentMethodDTOList = ObjectMapperUtils.mapAll(paymentMethodList,PaymentMethodDTO.class);
        return paymentMethodDTOList;
    }

    public List<DiscountPojo> getDiscountMethodList(){
        List<Discount> discounts = new ArrayList<>();
        discounts = discountRepository.findAllByStatus("Active");
        List<DiscountPojo> discountPojoList = ObjectMapperUtils.mapAll(discounts,DiscountPojo.class);
        return discountPojoList;
    }


    public String getCountryList()throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Country> countryList = new ArrayList<>();
            countryList = countryRepository.findAllByStatus("1");
            List<CountryPojo> countryPojoList = CountryMapper.MapcountryEntityToPojo(countryList);
            Map m = new HashMap();
            m.put("data", countryPojoList);
            m.put("status", "success");
            Gson json = new Gson();
            return json.toJson(m);

        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/AllCountryList";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }

    }

    public String getAllStaffList()throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<StaffGroup> staffGroupList = new ArrayList<>();
            staffGroupList = staffGroupRepository.findAllByStatus("1");
            List<StaffGroupPojo> pojoList = StaffGroupMapper.mapStaffEntityToPojo(staffGroupList);
            Map m = new HashMap();
            m.put("data", pojoList);
            m.put("status", "success");
            Gson json = new Gson();
            return json.toJson(m);

        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/AllStaffList";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }

    }

    public String getAllStateList()throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<State> stateList = new ArrayList<>();
            stateList = stateRepository.findAllByStatus("1");
            List<StatePojo> pojoList = StateMapper.mapStateEntityToPojo(stateList);
            Map m = new HashMap();
            m.put("data", pojoList);
            m.put("status", "success");
            Gson json = new Gson();
            return json.toJson(m);

        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/AllstateList";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }

    }


    public String getAllCityList()throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<City> cityList = new ArrayList<>();
            cityList = cityRepository.findAllByStatus("1");
            List<CityPojo> pojoList = CityMapper.MapCityEntityToPojo(cityList);
            Map m = new HashMap();
            m.put("data", pojoList);
            m.put("status", "success");
            Gson json = new Gson();
            return json.toJson(m);

        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/AllcityList";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }

    }

    public String getAllZoneList() throws Exception {
        if (StringUtils.pathEquals(buildType, "desktop")) {
            List<TableConfig> cityList = new ArrayList<>();
            cityList = tableConfigRepository.findAllByStatus("1");
            List<TableConfigPojo> pojoList = TableConfigMapper.mapEntityToPojo(cityList);
            Map m = new HashMap();
            m.put("data", pojoList);
            m.put("status", "success");
            Gson json = new Gson();
            return json.toJson(m);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/AllzoneList";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }

    public String getAllCategoryList() throws Exception {
        if (StringUtils.pathEquals(buildType, "desktop")) {
            List<Category> list = new ArrayList<>();
            list = categoryRepository.findAllByStatus("1");
            List<CategoryPojo> pojoList = CategoryMapper.mapEntityToPojo(list);
            Map m = new HashMap();
            m.put("data", pojoList);
            m.put("status", "success");
            Gson json = new Gson();
            return json.toJson(m);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/Allcategorylist";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }
    public String getAllShiftList() throws Exception {
        if (StringUtils.pathEquals(buildType, "desktop")) {
            List<Shift> list = new ArrayList<>();
            list = shiftRepository.findAllByStatus("1");
            List<ShiftPojo> pojoList = ShiftMapper.mapShiftEntityToPojo(list);
            Map m = new HashMap();
            m.put("data", pojoList);
            m.put("status", "success");
            Gson json = new Gson();
            return json.toJson(m);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/AllshiftList";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }
    public List<KitchenMasterPOJO> getKitchenList() {
        List<KitchenMaster> kitchenMasterList = new ArrayList<>();
        kitchenMasterList = kitchenRepository.findAllByStatus("Active");
        List<KitchenMasterPOJO> kitchenMasterPOJOList = ObjectMapperUtils.mapAll(kitchenMasterList, KitchenMasterPOJO.class);
        return kitchenMasterPOJOList;
    }
    public List<UserAccountSetUpDTO> getUserAccountSetupList(){
        List<UserAccountSetup> list = new ArrayList<>();
        list = userAccountSetupRepository.findAll();
        List<UserAccountSetUpDTO> countryPojoList = UserMapper.MapEntityToPojo(list);
        return countryPojoList;
    }

    public List<TableConfigPojo> getTableConfigList() {
        List<TableConfig> tableConfigList = new ArrayList<>();
        tableConfigList = tableConfigRepository.findAllByStatus("Active");
        List<TableConfigPojo> countryPojoList = ObjectMapperUtils.mapAll(tableConfigList, TableConfigPojo.class);
        return countryPojoList;
    }

    public List<TablesPosPojo> getTableNmList() {
        List<TablesPos> tablesPos = new ArrayList<>();
        tablesPos = tablesPosRepository.findAllByStatus("Active");
        List<TablesPosPojo> tablesPosPojoList = ObjectMapperUtils.mapAll(tablesPos, TablesPosPojo.class);
        return tablesPosPojoList;
    }

    public List<AgentPojo> getAgentList() {
        List<Agent> agentPojos = new ArrayList<>();
        agentPojos = agentRepository.findAllByStatus("Active");
        List<AgentPojo> agentPojoList = ObjectMapperUtils.mapAll(agentPojos, AgentPojo.class);
        return agentPojoList;
    }


    public String getTableConfigList(String configurationName) {
        List<Integer> gridLocationHList = null;
        List<Integer> gridLocationVList = null;
        String bothJson = null;
        try {
            gridLocationHList = new ArrayList<>();
            gridLocationVList = new ArrayList<>();
            TableConfig tableConfigobj = tableConfigRepository.findAllByConfigurationname(configurationName);
            for (int i = 0; i < tableConfigobj.getRowtableconfig(); i++) {
                gridLocationHList.add(i);
            }
            for (int j = 1; j <= tableConfigobj.getColumntableconfig(); j++) {
                gridLocationVList.add(j);
            }
            String json1 = new Gson().toJson(gridLocationHList);
            String json2 = new Gson().toJson(gridLocationVList);
            bothJson = "[" + json1 + "," + json2 + "]";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bothJson;
    }

    public List<TablesPosPojo> getTableListOnConfig(String configName, String tableSearchText) {
        List<TablesPos> tableList = new ArrayList<>();
        if (StringUtils.isEmpty(tableSearchText)) {
            tableList = tablesPosRepository.findAllByConfigurationnameAndStatus(configName, "Active");
        } else {
            tableList = tablesPosRepository.findAllByConfigurationnameAndStatusAndTableNameContaining(configName, "Active", tableSearchText);
        }
        List<TablesPosPojo> tablesPosPojoList = ObjectMapperUtils.mapAll(tableList, TablesPosPojo.class);
        for (TablesPosPojo tablesPosDTO : tablesPosPojoList) {
            TableConfig tableConfiguration = tableConfigRepository.findAllByConfigurationname(tablesPosDTO.getZone_id().toString());
//            tablesPosDTO.set(tableConfiguration.getTableconfigid().toString());
        }
        return tablesPosPojoList;
    }

    public TableConfig tableConfigurationByTableName(String tableConfigName) {
        TableConfig tableConfig = tableConfigRepository.findAllByConfigurationname(tableConfigName);
        return tableConfig;
    }

    public PageLoadData getIntialData(int userId) {
        PageLoadData pageLoadData = new PageLoadData();
        UserAccountSetup userAccountSetup = userAccountSetupRepository.findOne(1);
        if (userAccountSetup != null) {
            pageLoadData.setLocationRights(userAccountSetup.getAllLocations());
            if (userAccountSetup.isEmployeeflag() == true) {
                Employee employee = employeeRepository.findByEmployeeName(userAccountSetup.getFullName());
                if (employee.getEmployeeName() != null) {
                    pageLoadData.setDefaultEmployee(employee.getEmployeeName());
                }
            }
            pageLoadData.setFullUserName(userAccountSetup.getFullName());
            pageLoadData.setPassword(userAccountSetup.getPasswordUser());
            pageLoadData.setUserLoginId(userAccountSetup.getUserloginId());
        }
        pageLoadData.setCustomers(ObjectMapperUtils.mapAll(customerRepository.findAll(), CustomerPojo.class));
        pageLoadData.setItemCategorys(CategoryMapper.mapEntityToPojo(categoryRepository.findAllByStatus("Active")));
        List<EmployeePojo> employeeList = ObjectMapperUtils.mapAll(employeeRepository.findAllByStatus("Active"), EmployeePojo.class);
        pageLoadData.setEmployeeList(employeeList);
        List<AgentPojo> agentDTOList = AgentMapper.mapAgentEntityToPojo(agentRepository.findAllByStatus("Active"));
        pageLoadData.setAgentList(agentDTOList);
        return pageLoadData;
    }

    public List<TablesPosPojo> getTableList(String searchText, String status) {
        List<TablesPos> tablesPos = new ArrayList<>();
        if (StringUtils.isEmpty(status)) {
            status = "Active";
        }
        if (StringUtils.isEmpty(searchText)) {
            tablesPos = tablesPosRepository.findAllByStatus(status);
        } else {
            tablesPos = tablesPosRepository.findAllByStatusAndTableNameContaining(status, searchText);
        }
        List<TablesPosPojo> tablesPosPojoList = ObjectMapperUtils.mapAll(tablesPos, TablesPosPojo.class);
        return tablesPosPojoList;
    }

    public List<ShiftPojo> getShiftList() {
        List<Shift> shiftList = new ArrayList<>();
        shiftList = shiftRepository.findAllByStatus("Active");
        List<ShiftPojo> shiftPojoList = ObjectMapperUtils.mapAll(shiftList, ShiftPojo.class);
        return shiftPojoList;
    }

    public List<StatePojo> getStateList() {
        List<State> stateList = new ArrayList<>();
        stateList = stateRepository.findAllByStatus("Active");
        List<StatePojo> statePojos = ObjectMapperUtils.mapAll(stateList, StatePojo.class);
        for (StatePojo statePojo : statePojos) {
            Country country = countryRepository.findAllByCountryId(statePojo.getCountry_id());
            if (country != null) {
                statePojo.setCountry_name(country.getCountryName());
            }
        }
        return statePojos;
    }
    public List<CurrencyPojo> getCurrencyList(){
        List<Currency> countryList = new ArrayList<>();
        countryList = currencyRepository.findAllByStatus("Active");
        List<CurrencyPojo> countryPojoList = ObjectMapperUtils.mapAll(countryList,CurrencyPojo.class);
        return countryPojoList;
    }

    public List<CategoryPojo> getItemCategoryList(){
        List<Category> categoryList = new ArrayList<>();
        categoryList = categoryRepository.findAllByStatus("Active");
        List<CategoryPojo> categoryPojos = CategoryMapper.mapEntityToPojo(categoryList);
        return categoryPojos;
    }  public List<AddNewItemDTO> getAllItems(){
        List<Item> itemList = new ArrayList<>();
        itemList = itemRespository.findAllByItemStatus("Active");
        List<AddNewItemDTO> itemPojos = EmployeeMapper.mapItemEntityToPojo(itemList);
        return itemPojos;
    }

    public List<CustomerPojo> getCustomerDtoObjectOrList(){
        List<Customer> customerList = new ArrayList<>();
        customerList = customerRepository.findAllByStatus("Active");
        List<CustomerPojo> customerPojos = ObjectMapperUtils.mapAll(customerList, CustomerPojo.class);
        return customerPojos;
    }

//    public List<ItemPojo> getItemDtoObjectOrList(String searchText, Long itemCategoryId) {
//        List<Item> itemList = new ArrayList<>();
//        Category category = categoryRepository.findOne(itemCategoryId);
//        if (category == null) {
//            itemList = itemRespository.findAll();
//        } else {
//            itemList = itemRespository.findByIdItemCategory(category);
//        }
//        List<ItemPojo> itemPojos = HiposMapper.mapEntitytoPojo(itemList);
//        return itemPojos;
//    }

//    public List<ItemPojo> retrieveItemListOnCategory(Long itemCategoryId, String searchText) {
//        List<ItemPojo> itemList = getItemDtoObjectOrList(searchText, itemCategoryId);
//        for (ItemPojo itemDTO : itemList) {
////            itemDTO.setUnitPrice(itemDTO.getItemPrice());
////            itemDTO.setUnitPriceIn(itemDTO.getItemPrice());
//        }
//        return itemList;
//    }
    @Transactional
    public RetailPrintDTO retrieveSIDetailsA4(String invoiceNo){
        double discountAmount = 0.0;
        RetailPrintDTO printDTO = new RetailPrintDTO();
        RetailDTO retailDTO = new RetailDTO();
        SalesInvoice si = salesInvoiceRepository.findAllBySINo(invoiceNo);
        retailDTO.setMemo(si.getMemo());
        if (si != null) {
            List<SelectedItem> selectedItems = getSIDetailsPrint(si);
            retailDTO = mapSalesInvoiceEntityToRetailDTO(selectedItems, si);
            for (SelectedItem item : selectedItems) {
                retailDTO.setDiscountAmount(discountAmount += item.getDiscountAmt());
            }
            retailDTO.setDiscountAmtInPercentage((float)si.getDiscountConfig());
            retailDTO.setDiscType(si.getDiscountType());
        }
        Company company = companyRepository.findAllByStatus("Active");
        printDTO.setCompanyData(company);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        printDTO.setDate(sdf.format(si.getSIDate()));
        if(si.getCustomerId()!=null) {
//            printDTO.setBillToGST(si.getCustomerId().getGstCode());

            //Customer address(Billed To)
            retailDTO.setCustomerAddress(si.getCustomerId().getAddress1());
            retailDTO.setCustomerNo(si.getCustomerId().getTelephone());
//            if (si.getCustomerId().getStateId() == null) {
//                retailDTO.setCustomerState("");
////        } else {
////            retailDTO.setCustomerState(si.getCustomerId().getStateId().getStateName());
//            }
//            retailDTO.setCustomerGst(si.getCustomerId().getGstCode());
            retailDTO.setCutomerName(si.getCustomerId().getFirstName());
            retailDTO.setPhoneNumber(si.getCustomerId().getTelephone());
//            retailDTO.setPersonIncharge(si.getCustomerId().getPersonIncharge());
            retailDTO.setEmail(si.getCustomerId().getEmail());
            retailDTO.setCustomerPincode(si.getCustomerId().getPostcode());
        }
        printDTO.setMemo(si.getMemo());

        retailDTO.setTotalCheckOutamt(si.getTotalAmount());
        if(si.getAgentId()!=null) {
            retailDTO.setAgentIdOfInvoice(si.getAgentId().getAgentName());
            retailDTO.setAgentName(si.getAgentId().getAgentName());
        }
        retailDTO.setDiscountAmount(si.getTotalDiscountAmount());
        retailDTO.setHiPosServiceCharge((float) si.getServiceChargePer());
        retailDTO.setHiposServiceChargeAmt((float) si.getServiceChargeAmt());
        retailDTO.setReferenceNo(si.getReferenceno());
        retailDTO.setShippingReferenceNo(si.getShippingReferenceNo());
        retailDTO.setShippingDate(si.getShippingDate());

        //Next 2 lines Getting the formsetup obj to display the footer in print
        FormSetUp formterms = formSetupRepository.findAllByTypename("DirectSalesInvoice");
        printDTO.setFooter(formterms.getTermsDesc());
        printDTO.setTotalIncludingTax(si.getTotalAmount());
        printDTO.setTaxAmt(si.getSalesTotalTaxAmt());
        printDTO.setCessTaxAmt(si.getCessTaxAmt());
        List<SelectedItem> item = retailDTO.getSelectedItemsList();
        double excludingTax = 0d;
        for (SelectedItem itemList : item) {
            excludingTax = excludingTax + itemList.getAmtexclusivetax();
            printDTO.setTotalExcludingTax(excludingTax);
        }
        printDTO.setTotalPaid(si.getTotalReceived());
        printDTO.setBalance(si.getARBalance());
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy @hh:mm a");
        DateFormat d = new SimpleDateFormat("dd-MMM-yyyy @hh:mm a");
        String dateFormat = df.format(si.getSIDate());
        printDTO.setDate(dateFormat);
        if(si.getDateWithTime()!=null) {
            dateFormat = df.format(si.getDateWithTime());
        }
        printDTO.setRestaurantDate(dateFormat);
        retailDTO.setDateOfInvoice(d.format(si.getSIDate()));
        retailDTO.setSiNo(si.getSINo());
        retailDTO.setSrlnNo(si.getSerialNumber());
        retailDTO.setItemCount(retailDTO.getSelectedItemsList().size());
        retailDTO.setDiscountAmount(si.getTotalDiscountAmount());
        retailDTO.setCessTotalTaxAmt(si.getCessTaxAmt());
        retailDTO.setTotalTaxAmt(si.getSalesTotalTaxAmt());
        printDTO.setExchangeRateValue(Double.toString(si.getExchangeRateValue()));
//        Currency currency = new Currency();
//        currency.setCurrencyCode(si.getCurrencyId().getCurrencyCode());
//        currency.setCurrencyName(si.getCurrencyId().getCurrencyName());
        //pos print data
        PosPaymentTypes payments = retrive(invoiceNo);
        if (payments != null) {
            VoucherPayment v = new VoucherPayment();
            CreditCardPayment cp = new CreditCardPayment();
            CashPayment cashPayment = new CashPayment();
            Gson json1 = new Gson();
            BankPayment bp = new BankPayment();
            Type bankType = new TypeToken<ArrayList<MultiBankPayment>>() {
            }.getType();
            Type type1 = new TypeToken<ArrayList<MultiCardPayment>>() {
            }.getType();
            Type type = new TypeToken<ArrayList<MultiVoucherPayment>>() {
            }.getType();
            Type typeCash = new TypeToken<ArrayList<MultiCashPayment>>() {
            }.getType();
            List<MultiVoucherPayment> multiVoucherPayments = new ArrayList<>();
            List<MultiCashPayment> multiCashPayments = new ArrayList<>();
            multiVoucherPayments = json1.fromJson(payments.getVoucherPayment(), type);
            multiCashPayments = json1.fromJson(payments.getCashPayment(), typeCash);
            v.setMultiVoucherPayments(multiVoucherPayments);
            retailDTO.setVoucherPayment(v);
            List<MultiCardPayment> multiCardPayments = new ArrayList<>();
            multiCardPayments = json1.fromJson(payments.getCardPayment(), type1);
            cp.setCardPaymentList(multiCardPayments);
            retailDTO.setCreditPayment(cp);
            List<MultiBankPayment> multiBankPayment = json1.fromJson(payments.getBankPayment(), bankType);
            bp.setMultiBankPaymentList(multiBankPayment);
            retailDTO.setBankPayment(bp);
            cashPayment.setTotalCPAmountTendered(payments.getTotalCashPayment());
            cashPayment.setMultiCashPaymentList(multiCashPayments);
            retailDTO.setCashPayment(cashPayment);
            AccountSetup accountSet = accountSetupRepository.findOne(1L);
            retailDTO.setPrintType(accountSet.getPrintType());
            retailDTO.setRoundingAdj(payments.getRoundingAdjustment());
            retailDTO.setRoundingOffValue(String.valueOf(payments.getRoundingAdjustment()));
            retailDTO.setAmountReturned(payments.getTotalAmount());
        }

        retailDTO.setAmtToBePaid(Math.round(retailDTO.getTotalCheckOutamt()));
        retailDTO.setAmountReturned(Math.round(retailDTO.getAmountReturned()));
        printDTO.setSiData(retailDTO);
        double totalVoucherAmt = 0;
        double totalCardAmt = 0;
        double totalBankAmt = 0;
        double totalCashAmt = 0;
        if (printDTO.getSiData().getVoucherPayment() != null) {
            for (MultiVoucherPayment voucherPayment : printDTO.getSiData().getVoucherPayment().getMultiVoucherPayments()) {
                PaymentMethod paymentMethod=paymentMethodRepository.findOne(voucherPayment.getPaymentType());
                totalVoucherAmt = totalVoucherAmt+voucherPayment.getVoucherAmt();
                retailDTO.getVoucherPayment().setTotalVPAmountTendered(totalVoucherAmt);
                retailDTO.getVoucherPayment().setPaymentType(paymentMethod.getPaymentmethodName());
                voucherPayment.setPaymentName(paymentMethod.getPaymentmethodName());
            }
        }
        if (printDTO.getSiData().getCashPayment() != null) {
            if (printDTO.getSiData().getCashPayment().getMultiCashPaymentList() != null)
                for (MultiCashPayment cashPayment : printDTO.getSiData().getCashPayment().getMultiCashPaymentList()) {
                    PaymentMethod paymentMethod=paymentMethodRepository.findOne(cashPayment.getPaymentType());
                    totalCashAmt = totalCashAmt+cashPayment.getCashAmt();
                    cashPayment.setPaymentName(paymentMethod.getPaymentmethodName());
                    retailDTO.getCashPayment().setTotalCPAmountTendered(totalCashAmt);
                    retailDTO.getCashPayment().setPaymentType(paymentMethod.getPaymentmethodName());
                }
        }
        if(printDTO.getSiData().getBankPayment()!=null) {
            for (MultiBankPayment bankPayment : printDTO.getSiData().getBankPayment().getMultiBankPaymentList()) {
                PaymentMethod paymentMethod=paymentMethodRepository.findOne(Long.parseLong(bankPayment.getPaymentType()));
                totalBankAmt = totalBankAmt+bankPayment.getAmount();
                bankPayment.setPaymentName(paymentMethod.getPaymentmethodName());
                retailDTO.getBankPayment().setTotalBPAmountTendered(totalBankAmt);
                retailDTO.getBankPayment().setPaymentType(paymentMethod.getPaymentmethodName());
            }
        }
        if(printDTO.getSiData().getCreditPayment()!=null) {
            for (MultiCardPayment cardPayment : printDTO.getSiData().getCreditPayment().getCardPaymentList()) {
                totalCardAmt = totalCardAmt+cardPayment.getCardAmt();
                PaymentMethod paymentMethod=paymentMethodRepository.findOne(cardPayment.getPaymentType());
                cardPayment.setPaymentName(paymentMethod.getPaymentmethodName());
                retailDTO.getCreditPayment().setTotalCCPAmountTendered(totalCardAmt);
                retailDTO.getCreditPayment().setPaymentType(paymentMethod.getPaymentmethodName());
            }
        }
        List<TaxSummary> taxPerList = new ArrayList<>();
        AccountSetup accountSetup = getAccountSetup();
        TaxSummary taxSummary = new TaxSummary();
        taxSummary.setTaxName("");
        taxSummary.setTaxableAmt(retailDTO.getTotalCheckOutamt()-retailDTO.getTotalTaxAmt());
        taxSummary.setTaxAmount(retailDTO.getTotalTaxAmt());
        taxSummary.setTaxPercent(Double.parseDouble(accountSetup.getTaxId()));
        taxSummary.setCessAmt(0);
        taxSummary.setCessPercent(0);
        taxPerList.add(taxSummary);
        retailDTO.setTaxWiseSummaryList(taxPerList);
        String tokenRecord = "";
        String tableName = "";
        String waiterName = "";
        List<RestaurantTokenRecord> result = restaurantTokenRecordRepository.findBySiNo(retailDTO.getSiNo());
        for(RestaurantTokenRecord restaurantTokenRecord:result){
            tokenRecord += restaurantTokenRecord.getToken()+",";
            tableName = restaurantTokenRecord.getTableName();
            waiterName = restaurantTokenRecord.getWaiterName();
            retailDTO.setOrderNo(restaurantTokenRecord.getOrderNo());
            retailDTO.setPax(restaurantTokenRecord.getPax());
        }
        retailDTO.setEmail(tokenRecord.replaceAll(",$", ""));
        retailDTO.setTable(tableName);
        retailDTO.setMainTable(tableName);
        retailDTO.setEmployeeName(waiterName);
        printDTO.setSiData(retailDTO);
        printDTO.setHiposServiceChargeAmt(si.getServiceChargeAmt());
        printDTO.setHiPosServiceCharge(si.getServiceChargePer());
        return printDTO;
    }
    public RetailDTO mapSalesInvoiceEntityToRetailDTO(List<SelectedItem> selectedItems, SalesInvoice salesInvoice) {
        RetailDTO retailDTO = new RetailDTO();
        for(SelectedItem selectedItem:selectedItems){
            double taxAmt=(selectedItem.getTaxamt());
            selectedItem.setAmtexclusivetax(selectedItem.getAmtexclusivetax());
            selectedItem.setAmtinclusivetax(selectedItem.getAmtinclusivetax());
            selectedItem.setTaxamt(taxAmt);
            retailDTO.setTotalCheckOutamt(salesInvoice.getTotalReceivable());
        }
        retailDTO.setSelectedItemsList(selectedItems);
        retailDTO.setSiid(salesInvoice.getSIId());
        retailDTO.setSrlnNo(salesInvoice.getSerialNumber());
        if(salesInvoice.getCustomerId()!=null){
            retailDTO.setCutomerName(salesInvoice.getCustomerId().getFirstName());
            retailDTO.setCustomerId(salesInvoice.getCustomerId().getCustomerId());
        }
        if(salesInvoice.getUserId()!=null) {
            retailDTO.setEmployeeId(salesInvoice.getUserId().getEmployeeId().toString());
        }
        if(salesInvoice.getAgentId()!=null) {
            retailDTO.setAgentIdOfInvoice(salesInvoice.getAgentId().getAgentId().toString());
        }
        retailDTO.setOtherContactId(salesInvoice.getOtherContactId());
        retailDTO.setTaxType(salesInvoice.getTaxInvoice());
        return retailDTO;
    }
    @Transactional
    public  List<SelectedItem> getSIDetailsPrint(SalesInvoice SIId) {
        List<SelectedItem> list = new ArrayList<>();
        List<SalesInvoiceDetails> salesInvoiceDetails=salesInvoiceDetailsRepository.findBySIId(SIId);
        for(SalesInvoiceDetails salesInvoiceDetails1:salesInvoiceDetails) {
            SelectedItem selectedItem=new SelectedItem();
            Item item = itemRespository.findAllByItemId(salesInvoiceDetails1.getItemId().getItemId());
            selectedItem.setItemId(item.getItemId());
            selectedItem.setItemName(item.getItemName());
            selectedItem.setItemCode(item.getItemCode());
            selectedItem.setItemDescription(salesInvoiceDetails1.getItemDesc());
            selectedItem.setUnitPrice(salesInvoiceDetails1.getPrice());
            selectedItem.setQty(salesInvoiceDetails1.getQtyRemain());
            selectedItem.setRemainingQty(salesInvoiceDetails1.getQtyRemain());
            selectedItem.setAmtexclusivetax(salesInvoiceDetails1.getItemAmountExcTax());
            selectedItem.setMakingCharge(salesInvoiceDetails1.getMakingCharge());
            selectedItem.setActualWeight(salesInvoiceDetails1.getActualWeight());
            selectedItem.setTaxamt(salesInvoiceDetails1.getItemTax());
            selectedItem.setAmtinclusivetax(salesInvoiceDetails1.getItemAmountIncTax());
            selectedItem.setDiscountAmt(salesInvoiceDetails1.getDiscountAmount());
            selectedItem.setInclusiveJSON(item.getInclusiveJSON());
            if (!StringUtils.isEmpty(selectedItem.getTablesId())) {
                TablesPos tablesPos = tablesPosRepository.findOne(Long.parseLong(selectedItem.getTablesId()));
                selectedItem.setTablesId(tablesPos.getConfigurationname());
            }
            list.add(selectedItem);
        }
        return list;
    }
    public PosPaymentTypes retrive(String invoiceNo) {
        SalesInvoice invoice = salesInvoiceRepository.findAllBySINo(invoiceNo);
        PosPaymentTypes vd = null;
        if (invoice != null) {
            List<PosPaymentTypes> posPaymentTypes=posPaymentTypesRepository.findBySalesInvoice(invoice);
            if(posPaymentTypes.size()>0)
            vd = posPaymentTypes.get(0);
        }
        return vd;
    }
    public List<SalesInvoiceDTO> retrievePOSInvoiceist(String itemSearchText, String type) {
        List<SalesInvoice> salesInvoices = new ArrayList<>();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "sIId"));
        if(!StringUtils.isEmpty(itemSearchText)){
            salesInvoices = salesInvoiceRepository.findAllBySINoContaining(itemSearchText,sort);
        }else {
            salesInvoices = salesInvoiceRepository.findFirst20BySIStatus("Prepared",sort);
        }
        List<SalesInvoiceDTO> salesInvoiceDTOList = new ArrayList<>();
        for (SalesInvoice salesInvoice : salesInvoices) {
            SalesInvoiceDTO salesInvoiceDTO = new SalesInvoiceDTO();
            if (salesInvoice.getCustomerId() != null) {
                salesInvoiceDTO.setCustomerName(salesInvoice.getCustomerId().getFirstName());
                salesInvoiceDTO.setCustomerPhNo(salesInvoice.getCustomerId().getTelephone());
                salesInvoiceDTO.setCustomerEmail(salesInvoice.getCustomerId().getEmail());
            }
            salesInvoiceDTO.setFormNo(salesInvoice.getSINo());
            salesInvoiceDTO.setAmount(salesInvoice.getTotalReceivable());
            salesInvoiceDTO.setId(salesInvoice.getSIId());
            salesInvoiceDTO.setPiStatus(salesInvoice.getSIStatus());
            List<RestaurantTokenRecord> restaurantTokenRecords=restaurantTokenRecordRepository.findBySiId(salesInvoice);
            if(restaurantTokenRecords.size()>0) {
                RestaurantTokenRecord restaurantTokenRecord = restaurantTokenRecords.get(0);
                if (restaurantTokenRecord != null) {
                    salesInvoiceDTO.setTableName(restaurantTokenRecord.getTableName());
                }
            }
            salesInvoiceDTOList.add(salesInvoiceDTO);
        }
        return salesInvoiceDTOList;
    }

    public int getSalesInvoiceSize(String invoiceNo) {
        List<SalesInvoice> salesInvoiceList = salesInvoiceRepository.findAllBySINoStartingWith(invoiceNo);
        List<SalesInvoice> data = salesInvoiceList;
        return data.size();
    }

    public SalesInvoice cancelInvoice(String salesInvoiceId) {
        SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySIId(Long.parseLong(salesInvoiceId));
        int size = getSalesInvoiceSize(salesInvoice.getSINo());
        salesInvoice.setSINo(salesInvoice.getSINo() + "c" + size);
        salesInvoice.setSIStatus("Cancelled Invoice");
        salesInvoiceRepository.save(salesInvoice);
        return salesInvoice;
    }

    public SalesInvoice getSalesInvoice(Long invoiceNo) {
        SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySIId(invoiceNo);
        return salesInvoice;
    }

    public List<ItemDTO> retrieveSimplifiedItemList() {
        List<ItemDTO> itemList = ObjectMapperUtils.mapAll(itemRespository.findAll(), ItemDTO.class);
        for (ItemDTO itemDTO : itemList) {
            itemDTO.setUnitPrice(itemDTO.getSalesPrice());
            itemDTO.setUnitPriceIn(itemDTO.getSalesPrice());
        }
        return itemList;
    }

    public AccountSetupPojo getConfigureDetails() {
        AccountSetupPojo configureDetails = null;
        AccountSetup accountSetup = accountSetupRepository.findOne(1L);
        if (accountSetup != null) {
            configureDetails = ObjectMapperUtils.map(accountSetup, AccountSetupPojo.class);
            configureDetails.setBuildTypeCloud(printerType.equalsIgnoreCase("cloud"));
        }
        return configureDetails;
    }

    public RestaurantTempData getTempDataBasedOnId(String tableName, String tableId) {
        RestaurantTempData restaurantTempData = new RestaurantTempData();
        restaurantTempData = restaurantTempDataRepository.findAllByTableNameAndTableId(tableName, tableId);
        return restaurantTempData;
    }

    public TablesPos getTablePosObj(String tableId) {
        TablesPos tablesPos = tablesPosRepository.findAllByTableid(Long.parseLong(tableId));
        return tablesPos;
    }

    public RestaurantTempDataPojo getTempData(String currTableName, String currTableId) {
        RestaurantTempData restaurantTempData = getTempDataBasedOnId(currTableName, currTableId);
        RestaurantTempDataPojo restaurantTempDataPojo = new RestaurantTempDataPojo();
        if(!StringUtils.isEmpty(currTableId)) {
            TablesPos tablesPos = tablesPosRepository.findAllByTableid(Long.parseLong(currTableId));
            if(tablesPos!=null) {
                tablesPos.setMessage(null);
                tablesPosRepository.save(tablesPos);
            }
            if (!StringUtils.pathEquals(tablesPos.getTableStatus(), "Empty") && restaurantTempData != null) {
                RestaurantTokenRecord restaurantTokenRecord = restaurantTokenRecordRepository.findByTableIdAndTableNameOrderByRestaurantTokenIdDesc(currTableId, currTableName).get(0);
                restaurantTempDataPojo.setId(restaurantTempData.getId());
                restaurantTempDataPojo.setTableName(restaurantTempData.getTableName());
                restaurantTempDataPojo.setTableId(restaurantTempData.getTableId());
                restaurantTempDataPojo.setSelectedItemsList(restaurantTempData.getSelectedItemsList());
                restaurantTempDataPojo.setCustomerId(restaurantTempData.getCustomerId());
                restaurantTempDataPojo.setLocationId(restaurantTempData.getLocationId());
                restaurantTempDataPojo.setOrderNo(restaurantTempData.getOrderNo());
                restaurantTempDataPojo.setAgentId(restaurantTempData.getAgentId());
                restaurantTempDataPojo.setUseraccount_id(restaurantTempData.getUseraccount_id());
                restaurantTempDataPojo.setRemovedItemsList(restaurantTempData.getRemovedItemsList());
                restaurantTempDataPojo.setCustomerBill(restaurantTempData.isCustomerBill());
                Customer customer = customerRepository.findAllByCustomerId(restaurantTempData.getCustomerId());
                if (customer != null) {
                    restaurantTempDataPojo.setCustomerNo(customer.getTelephone());
                    restaurantTempDataPojo.setCustomerName(customer.getFirstName());
                }
                if (restaurantTokenRecord != null) {
                    restaurantTempDataPojo.setPax(restaurantTokenRecord.getPax());
                }
            }
        }
        return restaurantTempDataPojo;

    }

    public String userValidate(UserAccountSetUpDTO userAccountSetUpDTO) throws Exception{
        List<UserAccountSetup> userAccountSetupList = userAccountSetupRepository.findAll();
        Company company1 = companyRepository.findAllByStatus("Active");
        UserAccountSetup userAccountSetup = new UserAccountSetup();
        if(userAccountSetupList.size()==0||company1==null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", userAccountSetUpDTO.getOrderId());
            jsonObject.put("email", userAccountSetUpDTO.getEmail());
            jsonObject.put("full_name", userAccountSetUpDTO.getFull_name());
            jsonObject.put("passwordUser",userAccountSetUpDTO.getPasswordUser());
            String url =readDomainNameIdm()+"/sass/validateOrderForRestopos";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            Gson gson = new Gson();
            Map<String,String> map = new HashMap<>();
            Type type = new TypeToken<Map<String,String>>(){}.getType();
            map = gson.fromJson(jsonString,type);
            if(map!=null) {
                userAccountSetup = userAccountSetupRepository.findAllByFullName(map.get("username"));
               if(userAccountSetup==null){
                   userAccountSetup = new UserAccountSetup();
               }
                userAccountSetup.setFullName(map.get("username"));
                userAccountSetup.setPasswordUser(map.get("password"));
                userAccountSetup.setPhone(map.get("companyPhone"));
                userAccountSetup.setEmail(map.get("customerEmail"));
                userAccountSetup.setUserloginId(map.get("username"));
                userAccountSetup.setStatus("Active");
                UserAccessRights userAccessRights=addUserAccessRights();
                userAccountSetup.setUserAccessRights(userAccessRights);
                userAccountSetupRepository.save(userAccountSetup);
                Company company = companyRepository.findAllByCompanyName(map.get("companyName"));
                if(company==null){
                    company = new Company();
                }
                company.setCompanyName(map.get("companyName"));
                company.setOrderId(String.valueOf(map.get("orderValue")));
                company.setCompanyNo("");
                company.setCreatedDate(map.get("createdDate"));
                company.setExpiryDate(map.get("expiryDate"));
                company.setStatus("Active");
                company.setPanNumber(map.get("pan"));
                company.setAddress(map.get("companyAddress"));
                company.setPhone(map.get("companyPhone"));
                company.setFax(map.get("faxno"));
                company.setLang(map.get("language"));
                company.setEmail(map.get("customerEmail"));
                company.setConnectNo(map.get("orderValue"));
                companyRepository.save(company);
                return "Success";
            }else {
                return "Not Registered";
            }
        }else {
            userAccountSetup = userAccountSetupRepository.findAllByEmailAndFullNameAndPasswordUser(userAccountSetUpDTO.getEmail(), userAccountSetUpDTO.getFull_name(),userAccountSetUpDTO.getPasswordUser());
          if(userAccountSetup!=null) {
              return "Success";
          }else {
              return "Invalid";
          }
        }
    }
    public UserAccessRights addUserAccessRights(){
        UserAccessRights userAccessRights=new UserAccessRights();
        userAccessRights.setRestDineIn(true);
        userAccessRights.setRestDelivery(true);
        userAccessRights.setRestTakeaway(true);
        userAccessRights.setRestTableReservation(true);
        userAccessRights.setRestDigiOrders(true);
        userAccessRights.setRestOnlineDelivery(true);
        userAccessRights.setRestTable(true);
        userAccessRights.setRestWaiter(true);
        userAccessRights.setRestAgent(true);
        userAccessRights.setRestItemSearch(true);
        userAccessRights.setRestItemAdd(true);
        userAccessRights.setRestCustomer(true);
        userAccessRights.setRestCustomerAdd(true);
        userAccessRights.setRestPax(true);
        userAccessRights.setRestSave(true);
        userAccessRights.setRestSavePrint(true);
        userAccessRights.setRestSaveSms(true);
        userAccessRights.setRestCustomerBill(true);
        userAccessRights.setRestClearall(true);
        userAccessRights.setRestSplitbill(true);
        userAccessRights.setRestChangeTable(true);
        userAccessRights.setRestMergeTable(true);
        userAccessRights.setRestPrintList(true);
        userAccessRights.setRestDailyReport(true);
        userAccessRights.setRestVoucher(true);
        userAccessRights.setRestOffers(true);

        userAccessRights.setKitchenTokens(true);
        userAccessRights.setWaiterTokens(true);

        userAccessRights.setTokens(true);
        userAccessRights.setRestDashboard(true);
        userAccessRights.setRestaurant(true);
        userAccessRights.setMasters(true);
        userAccessRights.setFinance(true);
        userAccessRights.setCompanyInfo(true);
        userAccessRights.setReports(true);
        userAccessRights.setRestaurantReports(true);
        userAccessRights.setReceipt(true);
        userAccessRights.setExpense(true);
        userAccessRights.setJournalEntry(true);
        userAccessRights.setPandlreport(true);
        userAccessRights.setCountry(true);
        userAccessRights.setState(true);
        userAccessRights.setCurrency(true);
        userAccessRights.setTableZone(true);
        userAccessRights.setTablePos(true);
        userAccessRights.setAgent(true);
        userAccessRights.setEmployee(true);
        userAccessRights.setCustomer(true);
        userAccessRights.setCategory(true);
        userAccessRights.setItem(true);
        userAccessRights.setPaymentMethod(true);
        userAccessRights.setUserAccountSetUp(true);
        userAccessRights.setAccountType(true);
        userAccessRights.setAccountGroup(true);
        userAccessRights.setChartOfAccounts(true);
        userAccessRights.setContact(true);
        userAccessRights.setConfiguration(true);
        userAccessRights.setShift(true);
        userAccessRights.setPaymentVoucher(true);
        userAccessRights.setSmsService(true);
        userAccessRights.setRestaurantInvReg(true);
        userAccessRights.setMonthEndReport(true);
        userAccessRights.setItemSalesReport(true);
        userAccessRights.setShiftReport(true);
        userAccessRights.setCancelledItemReg(true);
        userAccessRights.setFreeMealReport(true);
        userAccessRights.setDiscountReport(true);
        userAccessRights.setTableWiseReport(true);
        userAccessRights.setAgentReport(true);
        userAccessRights.setCancelledInv(true);
        userAccessRights.setDayEndReport(true);
        userAccessRights.setOnlineInvReport(true);
        userAccessRights.setWaiterReport(true);
        userAccessRights.setReceiptAddCnct(true);
        userAccessRights.setReceiptSelectAcnt(true);
        userAccessRights.setReceiptRemoveAcnt(true);
        userAccessRights.setReceiptClearAll(true);
        userAccessRights.setReceiptSave(true);
        userAccessRights.setReceiptPrintlist(true);
        userAccessRights.setExpenseAddCnct(true);
        userAccessRights.setExpenseSelectAcnt(true);
        userAccessRights.setExpenseRemoveAcnt(true);
        userAccessRights.setExpenseClearAll(true);
        userAccessRights.setExpenseSave(true);
        userAccessRights.setExpensePrintlist(true);
        userAccessRights.setJESave(true);
        userAccessRights.setJEPrintlist(true);
        userAccessRights.setJEDraft(true);
        userAccessRights.setJECancel(true);
        userAccessRights.setJESelectAcnt(true);
        userAccessRights.setJERemoveAcnt(true);

        userAccessRightsRepository.save(userAccessRights);
        return  userAccessRights;
    }

    public String getDeactivatelicense(String orderId) throws Exception{
        if(!StringUtils.isEmpty(orderId)) {
            String url = readDomainNameIdm() + "/sass/expiryLicense?orderId="+orderId;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            if(!StringUtils.isEmpty(jsonString)) {
                Company company = companyRepository.findAllByOrderIdAndStatus(orderId,"Active");
                company.setStatus("InActive");
                companyRepository.save(company);
                return "Success";
            }else {
                return "False";
            }
        }else {
            return "Invalid";

        }
    }

    public UserAccountSetUpDTO getUserObject(String name){
        UserAccountSetup userAccountSetup = userAccountSetupRepository.findByFullName(name);
        UserAccountSetUpDTO userAccountSetUpDTO=new UserAccountSetUpDTO();
        if(userAccountSetup!=null) {
            userAccountSetUpDTO.setFull_name(userAccountSetup.getFullName());
            userAccountSetUpDTO.setUserAccessRights(userAccountSetup.getUserAccessRights());
        }
        return userAccountSetUpDTO;
    }
    @Transactional
    public ZomatoPojo getRestaurantConfirmNotification(ZomatoPojo zomatoPojo)throws Exception{
        Company company = companyRepository.findAllByStatus("Active");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order_id", zomatoPojo.getOrderId());
        jsonObject.put("status", zomatoPojo.getStatus());
        jsonObject.put("message", zomatoPojo.getMessage());
        jsonObject.put("store_id",company.getConnectNo());
        String url =readDomainNameRestoOrder()+"/services/Account/orderStatusChange";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String jsonString = responseEntity.getBody();
        CustomerNotifications customerNotifications = customerNotificationRepository.findOne(zomatoPojo.getCustNotiId());
        customerNotifications.setStatus(zomatoPojo.getStatus());
        customerNotificationRepository.save(customerNotifications);
        return zomatoPojo;
    }

    @Transactional
    public RestaurantTempData changeTempData(String fromTable, String toTable, String fromTableId, String toTableId) {
        RestaurantTempData restaurantTempData = restaurantTempDataRepository.findAllByTableNameAndTableId(fromTable, fromTableId);
        restaurantTempData.setTableName(toTable);
        restaurantTempData.setTableId(toTableId);
        TablesPos tablesPos = tablesPosRepository.findAllByTableid(Long.parseLong(toTableId));
        tablesPos.setTableStatus("Occupied");
        TablesPos fromTablePos = tablesPosRepository.findAllByTableid(Long.parseLong(fromTableId));
        Gson gson=new Gson();
        List<Long> longList = new ArrayList<>();
        Type type = new TypeToken<List<Long>>() {
        }.getType();
        if(!StringUtils.isEmpty(fromTablePos.getMergeTable())) {
            longList = gson.fromJson(fromTablePos.getMergeTable(), type);
            List<Long> tableList = new ArrayList<>();
            if (longList.contains(Long.parseLong(fromTableId))) {
                longList.remove(Long.parseLong(fromTableId));
                longList.add(Long.parseLong(toTableId));
            }
            tableList = longList;
            for (Long tableId : longList) {
                TablesPos totable = tablesPosRepository.findAllByTableid(tableId);
                totable.setMergeTable(gson.toJson(tableList));
                tablesPosRepository.save(totable);
            }
            tablesPos.setMergeTable(gson.toJson(tableList));
        }
        tablesPosRepository.save(tablesPos);
        fromTablePos.setTableStatus("Empty");
        fromTablePos.setMergeTable("");
        tablesPosRepository.save(fromTablePos);
        restaurantTempDataRepository.save(restaurantTempData);
        RestaurantTokenRecord restaurantTokenRecord = restaurantTokenRecordRepository.findByTableIdAndTableNameOrderByRestaurantTokenIdDesc(fromTableId, fromTable).get(0);
        restaurantTokenRecord.setTableName(toTable);
        restaurantTokenRecord.setTableId(toTableId);
        restaurantTokenRecordRepository.save(restaurantTokenRecord);
        return restaurantTempData;
    }

    @Transactional
    public List<TablesPosPojo> getOccupiedTables(String type) {
        List<String> tablesList = restaurantTempDataRepository.findBy();
        List<TablesPos> tablesPos=new ArrayList<>();
        if(!StringUtils.pathEquals(type,"occupied")){
            tablesPos=tablesPosRepository.findByStatusAndTableNameNotIn("Active",tablesList);
        }else {
            tablesPos=tablesPosRepository.findByTableNameIn(tablesList);
        }
        List<TablesPosPojo> tableList = TablesPosMapper.mapEntityToPojo(tablesPos);
        for (TablesPosPojo tablesPosDTO : tableList) {
            TableConfig tableConfiguration = tableConfigRepository.findAllByConfigurationname(tablesPosDTO.getZone_id().toString());
//            tablesPosDTO.setFloorId(tableConfiguration.getTableconfigid().toString());
        }
        return tableList;
    }
    @Transactional
    public TablesPos saveMergeTable(String fromTableId, String toTable) {
        TablesPos fromTablePos = tablesPosRepository.findAllByTableid(Long.parseLong(fromTableId));
        Gson gson = new Gson();
        if (!org.apache.commons.lang3.StringUtils.isEmpty(toTable)) {
            List<Long> longList = new ArrayList<>();
            Type type = new TypeToken<List<Long>>() {
            }.getType();
            longList = gson.fromJson(toTable, type);
            List<Long> tableList = new ArrayList<>();
            tableList = longList;
            if (!tableList.contains(Long.parseLong(fromTableId))) {
                tableList.add(Long.parseLong(fromTableId));
            }
            for (Long tableId : longList) {
                TablesPos totable = tablesPosRepository.findAllByTableid(tableId);
                totable.setMergeTable(gson.toJson(tableList));
                tablesPosRepository.save(totable);
            }
            fromTablePos.setMergeTable(gson.toJson(tableList));
            tablesPosRepository.save(fromTablePos);
        }
        return fromTablePos;
    }

    public String incrementToken(String number) {
        char[] cars = number.toUpperCase().toCharArray();
        for (int i = cars.length - 1; i >= 0; i--) {
            if (cars[i] == 'Z') {
                cars[i] = 'A';
            } else if (cars[i] == '9') {
                cars[i] = '0';
            } else {
                cars[i]++;
                break;
            }
        }
        return String.valueOf(cars);
    }

    //Method Overloaded
    public RestaurantTokenRecord createRestaurantTokenRecord(String instructions, String pax, List<POSKOTItemOrderDTO> kitchenOrders, String tableName, String waiterName, Long restaurantTempDataId, String tableId, String orderNo) {
        //holds the reference of current token[AlphaNumeric]
        String runningToken = "";
        RestaurantTokenRecord restaurantTokenRecord = new RestaurantTokenRecord();
        //holds the reference of current token
        List<RestaurantTokenRecord> list = restaurantTokenRecordRepository.findByOrderByRestaurantTokenIdDesc();
        RestaurantTokenRecord tokenRecord = null;
        if (list.size() > 0) {
            tokenRecord = list.get(0);
        }
        List<RestaurantTokenRecordDto> currentOrder = new ArrayList<>();
        //First record
        if (tokenRecord == null) {
            //else get from value from form setup
            runningToken = "A1";
        } else {
            if (tokenRecord.getToken() == null) {
                //Don't stop continue
                runningToken = "A1";
            } else {
                runningToken = incrementToken(tokenRecord.getToken());
            }
        }
        restaurantTokenRecord.setDate(new Date());
        restaurantTokenRecord.setTime(new SimpleDateFormat("HH:mm")
                .format(new Date()));
        restaurantTokenRecord.setStatus("Ordered");
        restaurantTokenRecord.setProductStatus("Ordered");
        restaurantTokenRecord.setTableName(tableName);
        restaurantTokenRecord.setOrderNo(orderNo);
        restaurantTokenRecord.setTableId(tableId);
        restaurantTokenRecord.setOrderRemarks(instructions);
        restaurantTokenRecord.setWaiterName(waiterName);
        restaurantTokenRecord.setKitchenTokenStart(new Date());
        //set restaurant temp data id for sales invoice
        restaurantTokenRecord.setSiNo(String.valueOf(restaurantTempDataId));
        restaurantTokenRecord.setPax(pax);
        JSONObject jo = null;
        JSONArray kitchenToken = new JSONArray();
        Gson gson = new Gson();
        try {
            for (POSKOTItemOrderDTO item : kitchenOrders) {
                RestaurantTokenRecordDto restaurantTokenRecordDto = new RestaurantTokenRecordDto();
                restaurantTokenRecordDto.setItemName(item.getItemName());
                restaurantTokenRecordDto.setQty(String.valueOf(item.getItemQty()));
                restaurantTokenRecordDto.setStatus(item.getType());
                currentOrder.add(restaurantTokenRecordDto);
                jo = new JSONObject();
                jo.put("itemName", item.getItemName());
                jo.put("qtyOrdered", item.getItemQty());
                jo.put("qtyCompleted", 0);
                jo.put("qtyDelivered", 0);
                jo.put("status", item.getType());
                kitchenToken.put(jo);
            }
        } catch (Exception e) {
        }
        //parse to JSON
        String itemDetails = gson.toJson(currentOrder);
        String removedItemsList = kitchenToken.toString();
        restaurantTokenRecord.setItemDeteails(itemDetails);
        restaurantTokenRecord.setRemovedItemsList(removedItemsList);
        restaurantTokenRecord.setToken(runningToken);
        restaurantTokenRecord.setDayEndStatus("false");
        restaurantTokenRecordRepository.save(restaurantTokenRecord);

        return restaurantTokenRecord;
    }
    public static String readDomainName() throws IOException {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = HiposService.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(in);
            in.close();
        } catch (Exception e) {
        } finally {
            in.close();
        }
        return prop.getProperty("rlite_web");
    }

    public String saveStaffGroup(StaffGroupPojo staffGroupPojo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            JSONObject jsonObject = new JSONObject();
            StaffGroup staffGroup = new StaffGroup();
            if (staffGroupPojo.getStaff_group_id() != null) {
                staffGroup = staffGroupRepository.findAllByStaffgroupnameAndStaffgroupidNotIn(staffGroupPojo.getStaff_group_name(), staffGroupPojo.getStaff_group_id());
            } else {
                staffGroup = staffGroupRepository.findAllByStaffgroupname(staffGroupPojo.getStaff_group_name());
            }
            if (staffGroup == null) {
                staffGroup = StaffGroupMapper.MapStaffPojoToEntity(staffGroupPojo);
                staffGroupRepository.save(staffGroup);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("staff_group_name",staffGroupPojo.getStaff_group_name());
            jsonObject.put("permission",staffGroupPojo.getPermission());
            jsonObject.put("staff_group_id",staffGroupPojo.getStaff_group_id());
            jsonObject.put("status",staffGroupPojo.getStatus());
            Gson gson = new Gson();
            String url = readDomainName() + "/services/Litecall/saveStaffGroup";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(jsonObject), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }



    public String saveNewItemCategory(CategoryPojo categoryPojo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            JSONObject jsonObject = new JSONObject();
            Category category = new Category();
            if (categoryPojo.getCategory_id() != null) {
                category = categoryRepository.findAllByItemCategoryNameAndItemCategoryIdNotIn(categoryPojo.getName(), categoryPojo.getCategory_id());
            } else {
                category = categoryRepository.findAllByItemCategoryName(categoryPojo.getName());
            }
            if (category == null) {
                category = CategoryMapper.mapPojoToEntity(categoryPojo);
                categoryRepository.save(category);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("description",categoryPojo.getDescription());
            jsonObject.put("name",categoryPojo.getName());
            jsonObject.put("code",categoryPojo.getCode());
            jsonObject.put("category_id",categoryPojo.getCategory_id());
            if (org.apache.commons.lang3.StringUtils.isEmpty(categoryPojo.getImage())) {
                jsonObject.put("image","");
            }
            jsonObject.put("status",categoryPojo.getStatus());
            String url = readDomainName() + "/services/Litecall/saveCategory";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }
    @Transactional
    public RestaurantTempData saveTempTableData(String currTableName, String currTableId,
                                                String employeeName, Long customerId, String jsonBody, String orderNo, String agentId) {

        RestaurantTempData tempData = new RestaurantTempData();
        if (currTableId != null) {
            tempData = getTempDataBasedOnId(currTableName, currTableId);
            if (tempData == null) tempData = new RestaurantTempData();
            tempData.setSelectedItemsList(jsonBody);
            tempData.setTableId(currTableId);
            tempData.setTableName(currTableName);
            tempData.setUseraccount_id(employeeName);
            tempData.setCustomerId(customerId);
            tempData.setAgentId(agentId);
            if (!StringUtils.isEmpty(orderNo))
                tempData.setOrderNo(orderNo);
            restaurantTempDataRepository.save(tempData);
            if (!StringUtils.isEmpty(currTableId)) {
                TablesPos tablesPos = getTablePosObj(currTableId);
                tablesPos.setTableStatus("Occupied");
                tablesPosRepository.save(tablesPos);
            }
        }
        RestaurantTempData restaurantTempData = getTempDataBasedOnId(currTableName, currTableId);
        Gson json = new Gson();
        Type type = new TypeToken<ArrayList<SelectedItem>>() {
        }.getType();
        List<SelectedItem> itemList = json.fromJson(restaurantTempData.getSelectedItemsList(), type);
        if (itemList.size() == 0) {
            restaurantTempDataRepository.delete(restaurantTempData);
            TablesPos tablesPos = getTablePosObj(restaurantTempData.getTableId());
            tablesPos.setTableStatus("Empty");
            tablesPosRepository.save(tablesPos);
            return null;
        }
        return restaurantTempData;
    }

    public List<TablesPosPojo> completeTableList(String type) {
        List<TablesPos> tablesPosList = new ArrayList<>();
        tablesPosList = tablesPosRepository.findAllByStatus(type);
        List<TablesPosPojo> tablesPosPojoList = ObjectMapperUtils.mapAll(tablesPosList, TablesPosPojo.class);
        return tablesPosPojoList;
    }
    public TableReservation getTablesListSpecialRequests(Long id){
        TableReservation tableReservation = tableReservationRepository.findAllById(id);
        return tableReservation;
    };
    public String saveShift(ShiftPojo shiftPojo)throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            Shift shift = new Shift();
            if (shiftPojo.getMealtime_id() != null) {
                shift = shiftRepository.findAllByShiftNameAndShiftIdNotIn(shiftPojo.getMealtime_name(), shiftPojo.getMealtime_id());
            } else {
                shift = shiftRepository.findAllByShiftName(shiftPojo.getMealtime_name());

            }
            JSONObject jsonObject = new JSONObject();
            if (shift == null) {
                shift = ShiftMapper.MapPojoToEntity(shiftPojo);
                shiftRepository.save(shift);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("mealtime_id",shiftPojo.getMealtime_id());
            jsonObject.put("mealtime_name",shiftPojo.getMealtime_name());
            jsonObject.put("start_time",shiftPojo.getStart_time());
            jsonObject.put("end_time",shiftPojo.getEnd_time());
            jsonObject.put("mealtime_status",shiftPojo.getMealtime_status());
            String url = readDomainName() + "/services/Litecall/saveShift";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

    public List<Item> retrieveItemListForInventoryModule(String searchText) {
        List<Item> itemList = itemRespository.findAll();
        return itemList;
    }
    @Transactional
    public List<TablesPosPojo> getMergeOccupiedTables(String type, String searchText) {
        List<TablesPos> tablesPosList=new ArrayList<>();
        if(StringUtils.isEmpty(searchText)) {
             tablesPosList = tablesPosRepository.findAllByTableStatusAndStatus(type,"Active");
        }else {
            tablesPosList=tablesPosRepository.findAllByTableStatusAndTableNameContainingAndStatus(type,searchText,"Active");
        }
        List<TablesPosPojo> list=ObjectMapperUtils.mapAll(tablesPosList,TablesPosPojo.class);
        return list;
    }

//    public ItemPojo getItem(String itemName) {
//        Item item = itemRespository.findAllByItemName(itemName);
//        ItemPojo itemPojo=new ItemPojo();
//        if(item!=null) {
//            List<Item> items = new ArrayList<>();
//            items.add(item);
//            itemPojo = HiposMapper.mapEntitytoPojo(items).get(0);
//        }else {
//            return null;
//        }
//        return itemPojo;
//    }

    public TablesPosPojo getTable(String tablename){
        TablesPosPojo tablesPosPojo = new TablesPosPojo();
        TablesPos tablesPos = tablesPosRepository.findAllByTableName(tablename);
        tablesPosPojo.setTable_id(tablesPos.getTableid());
        tablesPosPojo.setTable_name(tablesPos.getTableName());
        tablesPos.setTableStatus("Reserved");
        tablesPosRepository.save(tablesPos);
        return tablesPosPojo;
    }
    public AccountSetup getAccountSetup() {
        AccountSetup accountSetup = accountSetupRepository.findOne(1L);
        return accountSetup;
    }

    public Customer getCustomerObject(Long id) {
        Customer customer = customerRepository.findAllByCustomerId(id);
        return customer;
    }

    public Agent getAgentObjectbyName(String name) {
        Agent agent = agentRepository.findByAgentName(name);
        return agent;
    }

    public PaymentMethod getPaymentmethodObject(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findAllByPaymentmethodId(id);
        return paymentMethod;
    }


    public Employee getEmployeeNameObject(String name) {
        Employee employee = employeeRepository.findByEmployeeName(name);
        return employee;
    }

    public RestaurantTokenRecord getRestaurentObjbyTableName(String name, String id) {
        RestaurantTokenRecord restaurantTokenRecord = null;
        try {
            restaurantTokenRecord = restaurantTokenRecordRepository.findByTableIdAndTableNameOrderByRestaurantTokenIdDesc(id, name).get(0);
        } catch (IndexOutOfBoundsException e) {
        }
        return restaurantTokenRecord;
    }

    public List<RestaurantTokenRecord> getRestaurantTokenRecord() {
        List<RestaurantTokenRecord> restaurantTokenRecordList = restaurantTokenRecordRepository.findAll();
        return restaurantTokenRecordList;
    }

    @Transactional
    public FormSetUp getFormSetUp(String formSetupType) {
        FormSetUp formSetUp = new FormSetUp();
        try {
            if (!StringUtils.pathEquals(formSetupType, "SerialNumber")) {
                formSetUp = formSetupRepository.findAllByTypename(formSetupType);
            } else {
                formSetUp = formSetupRepository.findAllByTypename(formSetupType);
            }
            int incValue = Integer.parseInt(formSetUp.getNextref());
            synchronized (formSetUp) {
                formSetUp.setNextref(String.format("%08d", ++incValue));
                formSetupRepository.save(formSetUp);
            }
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return formSetUp;
    }

    public String getAvailableFormSetup(String type) {
        FormSetUp formSetUp = null;
        String no = "";
        while (no.isEmpty()) {
            if (StringUtils.pathEquals(type, "DirectSalesInvoice")) {
                formSetUp = getFormSetUp("DirectSalesInvoice");
                no = formSetUp.getTypeprefix() + formSetUp.getNextref();
                SalesInvoice salesInvoice = getSIBySINOReturn(no);
                if (salesInvoice != null) {
                    no = "";
                }
            }
        }
        return no;
    }

    public SalesInvoice getSIBySINOReturn(String siNo) {
        SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySINo(siNo);
        return salesInvoice;
    }


    public SalesInvoice createPOSSI(RetailDTO retailDTO) {
        SalesInvoice salesInvoice = new SalesInvoice();
        salesInvoice.setSINo(getAvailableFormSetup("DirectSalesInvoice"));
        Employee employee = getEmployeeNameObject(retailDTO.getEmployeeName());
        salesInvoice.setUserId(employee);
        salesInvoice.setDateWithTime(new Date());
        salesInvoice.setSIDate(HiposUtil.parseDate(retailDTO.getDateOfInvoice()));
        Customer customer = getCustomerObject(retailDTO.getCustomerId());
        salesInvoice.setCustomerId(customer);
        salesInvoice.setGstflag(true);
        if (!HiposUtil.isStringNullrEmpty(retailDTO.getMemo())) {
            salesInvoice.setMemo(retailDTO.getMemo());
        }
        if (!HiposUtil.isStringNullrEmpty(retailDTO.getCustomerPo())) {
            salesInvoice.setCustPONo(retailDTO.getCustomerPo());
        }
        salesInvoice.setCompanyLocation(retailDTO.getCmpyLocation());
        salesInvoice.setCustomerLocation(retailDTO.getCustLocation());
        if (retailDTO.getAdvancepayment() == "true") {
            salesInvoice.setAdvancePayment("true");
            salesInvoice.setSIStatus("AdvanceDraft");
            if (StringUtils.pathEquals(retailDTO.getSiStatus(), "Draft")) {
                salesInvoice.setPosting("No");
            } else {
                salesInvoice.setSIStatus("AdvancePrepared");
                salesInvoice.setPosting("Yes");
            }
        } else if (StringUtils.pathEquals(retailDTO.getSiStatus(), "Draft")) {
            salesInvoice.setAdvancePayment("false");
            salesInvoice.setSIStatus("Draft");
            salesInvoice.setPosting("No");
        } else {
            salesInvoice.setAdvancePayment("false");
            salesInvoice.setSIStatus("Prepared");
            salesInvoice.setPosting("Yes");
        }
        salesInvoice.setFlag("POS Sales Invoice");
        salesInvoice.setBadDeptRelief("No");
        salesInvoice.setShowReport("No");
        salesInvoice.setTotalamountsalesretun(0.00);
        salesInvoice.setSalesTotalTaxAmt(retailDTO.getTotalTaxAmt());
        salesInvoice.setCessTaxAmt(retailDTO.getCessTotalTaxAmt());
        if (retailDTO.getAmountReturned() == 0 && retailDTO.getTotalTenderedAmount() == 0) {
            salesInvoice.setARBalance(BigDecimal.valueOf(retailDTO.getTotalCheckOutamt()));
        } else {
            if (retailDTO.getAmountReturned() < 0) {
                salesInvoice.setARBalance(BigDecimal.valueOf(retailDTO.getAmountReturned()).negate());
                salesInvoice.setTotalReceived(retailDTO.getTotalTenderedAmount());
            } else {
                salesInvoice.setARBalance(BigDecimal.valueOf(0));
                salesInvoice.setTotalReceived(retailDTO.getTotalTenderedAmount() - retailDTO.getAmountReturned());
            }
        }
        Agent agent=agentRepository.findByAgentName(retailDTO.getAgentName());
        salesInvoice.setAgentId(agent);
        salesInvoice.setTaxInvoice(retailDTO.getTaxType());
        salesInvoice.setTotalAmount(retailDTO.getTotalCheckOutamt());
        salesInvoice.setTotalReceivable(retailDTO.getTotalCheckOutamt());
        salesInvoice.setTotalDiscountAmount(retailDTO.getDiscountAmount());
        salesInvoice.setServiceChargeAmt(retailDTO.getHiposServiceChargeAmt());
        salesInvoice.setServiceChargePer(retailDTO.getHiPosServiceCharge());
        switch (retailDTO.getPaymentType()) {
            case "cashPayment":
                double amtPaid = retailDTO.getTotalCheckOutamt() - retailDTO.getCashPayment().getTotalCPDiscount();
                salesInvoice.setTotalAmount(amtPaid);
                salesInvoice.setTotalReceived(amtPaid);
                salesInvoice.setTotalReceivable(amtPaid);
                salesInvoice.setARBalance(BigDecimal.valueOf(amtPaid));
                salesInvoice.setTotalDiscountAmount(retailDTO.getCashPayment().getTotalCPDiscount());
                break;
            case "creditPayment":
                amtPaid = retailDTO.getTotalCheckOutamt() - retailDTO.getCreditPayment().getTotalCCPDiscount();
                salesInvoice.setTotalAmount(amtPaid);
                salesInvoice.setTotalReceived(amtPaid);
                salesInvoice.setTotalReceivable(amtPaid);
                salesInvoice.setARBalance(BigDecimal.valueOf(amtPaid));
                salesInvoice.setTotalDiscountAmount(retailDTO.getCreditPayment().getTotalCCPDiscount());
                break;
            case "voucherPayment":
                amtPaid = retailDTO.getVoucherPayment().getTotalVPBeforDiscount() - retailDTO.getVoucherPayment().getTotalVPDiscount();
                salesInvoice.setTotalAmount(amtPaid);
                salesInvoice.setTotalReceived(amtPaid);
                salesInvoice.setTotalReceivable(amtPaid);
                salesInvoice.setARBalance(BigDecimal.valueOf(amtPaid));
                salesInvoice.setTotalDiscountAmount(retailDTO.getVoucherPayment().getTotalVPDiscount());
                break;
        }
        SalesInvoice salesInvoice1 = getSIBySINOReturn(salesInvoice.getSINo());
        if (salesInvoice1 != null && salesInvoice.getSIId() == null) {
            salesInvoice.setSINo(getAvailableFormSetup("DirectSalesInvoice"));
        }
        salesInvoiceRepository.save(salesInvoice);
        return salesInvoice;
    }


    public List<SalesInvoiceDetails> createPOSSIDetails(RetailDTO retailDTO, SalesInvoice salesInvoice) {
        List<SalesInvoiceDetails> sidList = new ArrayList<>();
        for (SelectedItem selectedItem : retailDTO.getSelectedItemsList()) {
            SalesInvoiceDetails sid = new SalesInvoiceDetails();
            // TODO
            sid.setSIId(salesInvoice);
            sid.setSINo(salesInvoice.getSINo());
            Item item = itemRespository.findAllByItemName(selectedItem.getItemName());
            sid.setItemId(item);
            sid.setItemDesc(selectedItem.getItemDescription());
            sid.setItemCode(item.getItemCode());
            sid.setPrice(selectedItem.getUnitPrice());
            sid.setQtyOrdered(selectedItem.getQty());
            sid.setQtySent(0);
            sid.setConcateItemStockId(selectedItem.getSerializableItemId());
            sid.setQtyRemain(selectedItem.getQty());
            sid.setDiscPercent(0.00);
            sid.setItemAmountExcTax(selectedItem.getAmtexclusivetax());
            sid.setItemTax(selectedItem.getTaxamt());
            sid.setItemAmountIncTax(selectedItem.getAmtinclusivetax());
            sid.setRecStatus("Prepared");
            sid.setDiscountconfigamount(selectedItem.getDiscountConfigAmt());
            sid.setDiscountAmount(selectedItem.getDiscountAmt());
            sid.setCessPer(selectedItem.getCess());
            Double cessTaxAmt = ((selectedItem.getAmtexclusivetax()) * (selectedItem.getCess())) / 100;
            DecimalFormat df = new DecimalFormat("#.##");
            sid.setCessTaxAmt(Double.parseDouble((df.format(cessTaxAmt))));
            salesInvoiceDetailsRepository.save(sid);
            sidList.add(sid);
        }
        return sidList;
    }

    private List<RestaurantTokenRecord> updateRestaurantTokenRecord(Long restTempRecId, SalesInvoice salesInvoice) {
        List<RestaurantTokenRecord> restaurantTokenRecords = restaurantTokenRecordRepository.findBySiNo(restTempRecId.toString());
        for (RestaurantTokenRecord restaurantTokenRecord : restaurantTokenRecords) {
            restaurantTokenRecord.setSiNo(salesInvoice.getSINo());
            restaurantTokenRecord.setSiId(salesInvoice);
            restaurantTokenRecord.setStatus("Prepared");
            restaurantTokenRecordRepository.save(restaurantTokenRecord);
        }
        return restaurantTokenRecords;
    }

    public RestaurantTempData deleteTempData(String tableName, String tableId) {
        RestaurantTempData restaurantTempData = restaurantTempDataRepository.findByTableNameAndTableId(tableName, tableId);
        if(restaurantTempData!=null)
        restaurantTempDataRepository.delete(restaurantTempData);
        return restaurantTempData;
    }

    public void persistTempTableData(RestaurantTempData restaurantTempData) {
        try {
            restaurantTempDataRepository.save(restaurantTempData);
        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }

    @Transactional
    public RetailPrintDTO createInoviceNPayment(RetailDTO retailDTO, String tableNo, String tableName, String waiterName, RetailPrintDTO printDTO) {
        SalesInvoice salesInvoice = createPOSSI(retailDTO);
        salesInvoice.setInvoiceType(retailDTO.getOrderType());
        salesInvoice.setMemo(retailDTO.getMemo());
        salesInvoice.setDiscountType(retailDTO.getDiscType());
        salesInvoice.setDiscountConfig(retailDTO.getDiscountAmtInPercentage());
        if (!StringUtils.isEmpty(retailDTO.getDiscountCode())) {
            salesInvoice.setDiscountCode(retailDTO.getDiscountCode());
        }
        List<SalesInvoiceDetails> sidList = createPOSSIDetails(retailDTO, salesInvoice);
        int i = 0;
        for (SelectedItem selectedItem : retailDTO.getSelectedItemsList()) {
            selectedItem.setItemCategoryName(sidList.get(i).getItemId().getIdItemCategory().getItemCategoryName());
            selectedItem.setItemCategoryId(sidList.get(i).getItemId().getIdItemCategory().getItemCategoryId());
            selectedItem.setTaxpercent(sidList.get(i).getItemTaxPer());
            Item item = itemRespository.findAllByItemName(selectedItem.getItemName());
            if (!StringUtils.isEmpty(item.getProductionName()))
                if (item.getProductionName().equalsIgnoreCase("productionName")) {
                }
            i++;
        }
        //get restaurant temp data
        RestaurantTempData resturantTempData = getRestTempDataOnId(tableName, tableNo);
        if(resturantTempData!=null) {
            //update restaurant token record with sales invoice
            List<RestaurantTokenRecord> restaurantTokenRecords = updateRestaurantTokenRecord(resturantTempData.getId(), salesInvoice);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(tableNo)) {
                TablesPos tablesPos = getTablePosObj(tableNo);
                if (!org.apache.commons.lang3.StringUtils.isEmpty(tablesPos.getMergeTable())) {
                    List<Long> longList = new ArrayList<>();
                    Gson json = new Gson();
                    Type type = new TypeToken<List<Long>>() {
                    }.getType();
                    longList = json.fromJson(tablesPos.getMergeTable(), type);
                    for (Long tablesPos1 : longList) {
                        TablesPos tablesPos2 = getTablePosObj(tablesPos1.toString());
                        RestaurantTempData resturantTempData1 = getRestTempDataOnId(tablesPos2.getTableName(), tablesPos2.getTableid().toString());
                        updateRestaurantTokenRecord(resturantTempData1.getId(), salesInvoice);
                    }
                }
            }
            if (resturantTempData != null) {
                Gson json = new Gson();
                Type selectedItems = new TypeToken<ArrayList<SelectedItem>>() {
                }.getType();
                List<SelectedItem> selectedItemList = json.fromJson(resturantTempData.getSelectedItemsList(), selectedItems);
                if (selectedItemList != null) {
                    for (SelectedItem selectedItem : retailDTO.getSelectedItemsList()) {
                        for (SelectedItem selectedItem1 : selectedItemList) {
                            if (StringUtils.pathEquals(selectedItem1.getItemName(), selectedItem.getItemName())) {
                                if (selectedItem1.getQty() == selectedItem.getQty()) {
                                    selectedItem1.setFlag(true);
                                } else {
                                    if (selectedItem1.getQty() > selectedItem.getQty()) {
                                        selectedItem1.setQty(selectedItem1.getQty() - selectedItem.getQty());
                                    } else {
                                        selectedItem1.setQty(0);
                                    }
                                    selectedItem1.setFlag(false);
                                }
                            }
                        }

                    }
                    List<SelectedItem> finalList = new ArrayList<>();
                    for (SelectedItem selectedItem : selectedItemList) {
                        if (selectedItem.isFlag() == false) {
                            finalList.add(selectedItem);
                        }
                    }

                    if (finalList.isEmpty()) {
                        deleteTempData(tableName, tableNo);
                        if (!StringUtils.isEmpty(tableNo)) {
                            TablesPos tablesPos = getTablePosObj(tableNo);
                            tablesPos.setTableStatus("Empty");
                            tablesPosRepository.save(tablesPos);
                            Gson gson = new Gson();
                            if (!org.apache.commons.lang3.StringUtils.isEmpty(tablesPos.getMergeTable())) {
                                List<Long> longList = new ArrayList<>();
                                Type type = new TypeToken<List<Long>>() {
                                }.getType();
                                longList = gson.fromJson(tablesPos.getMergeTable(), type);
                                for (Long restaurantTempData1 : longList) {
                                    TablesPos tablesPos1 = getTablePosObj(Long.toString(restaurantTempData1));
                                    tablesPos1.setTableStatus("Empty");
                                    tablesPosRepository.save(tablesPos1);
                                    deleteTempData(tablesPos1.getTableName(), tablesPos1.getTableid().toString());
                                }
                            }
                        }
                    } else {
                        Gson gson = new Gson();
                        String jsonSelectedList = gson.toJson(finalList);
                        resturantTempData.setSelectedItemsList(jsonSelectedList);
                        TablesPos tablesPos = getTablePosObj(resturantTempData.getTableId());
                        tablesPos.setTableStatus("Occupied");
                        tablesPosRepository.save(tablesPos);
                        persistTempTableData(resturantTempData);
                        if (!org.apache.commons.lang3.StringUtils.isEmpty(tableNo)) {
                            TablesPos tablesPos1 = getTablePosObj(tableNo);
                            if (!org.apache.commons.lang3.StringUtils.isEmpty(tablesPos1.getMergeTable())) {
                                List<Long> longList = new ArrayList<>();
                                Type type = new TypeToken<List<Long>>() {
                                }.getType();
                                longList = json.fromJson(tablesPos1.getMergeTable(), type);
                                for (Long tables : longList) {
                                    TablesPos tab = getTablePosObj(tables.toString());
                                    RestaurantTempData resturantTempData1 = getRestTempDataOnId(tab.getTableName(), tab.getTableid().toString());
                                    resturantTempData1.setSelectedItemsList(jsonSelectedList);
                                    tab.setTableStatus("Occupied");
                                    tablesPosRepository.save(tab);
                                    RestaurantTokenRecord restaurantTokenRecord123=getRestaurentObjbyTableName(tab.getTableName(),tab.getTableid().toString());
                                    if(restaurantTokenRecord123!=null) {
                                        RestaurantTokenRecord restaurantTokenRecord = new RestaurantTokenRecord();
                                        restaurantTokenRecord.setSiNo(resturantTempData1.getId().toString());
                                        restaurantTokenRecord.setPax(restaurantTokenRecord123.getPax());
                                        restaurantTokenRecord.setDate(new Date());
                                        restaurantTokenRecord.setTime(new SimpleDateFormat("HH:mm")
                                                .format(new Date()));
                                        restaurantTokenRecord.setDayEndStatus("false");
                                        restaurantTokenRecord.setStatus("Prepared");
                                        restaurantTokenRecord.setTableName(tab.getTableName());
                                        restaurantTokenRecord.setTableId(tab.getTableid().toString());
                                        restaurantTokenRecord.setWaiterName(waiterName);
                                        restaurantTokenRecord.setSiId(salesInvoice);
                                        restaurantTokenRecord.setToken(restaurantTokenRecord123.getToken());
                                        restaurantTokenRecord.setInventoryLocation(restaurantTokenRecord123.getInventoryLocation());
                                        restaurantTokenRecordRepository.save(restaurantTokenRecord);
                                    }
                                    persistTempTableData(resturantTempData1);
                                }
                            } else if(restaurantTokenRecords.size()>0) {
                                RestaurantTokenRecord restaurantTokenRecord = new RestaurantTokenRecord();
                                restaurantTokenRecord.setSiNo(resturantTempData.getId().toString());
                                restaurantTokenRecord.setPax(restaurantTokenRecords.get(0).getPax());
                                restaurantTokenRecord.setDate(new Date());
                                restaurantTokenRecord.setTime(new SimpleDateFormat("HH:mm")
                                        .format(new Date()));
                                restaurantTokenRecord.setDayEndStatus("false");
                                restaurantTokenRecord.setStatus("Prepared");
                                restaurantTokenRecord.setTableName(tableName);
                                restaurantTokenRecord.setTableId(tableNo);
                                restaurantTokenRecord.setWaiterName(waiterName);
                                restaurantTokenRecord.setSiId(salesInvoice);
                                restaurantTokenRecord.setToken(restaurantTokenRecords.get(0).getToken());
                                restaurantTokenRecord.setInventoryLocation(restaurantTokenRecords.get(0).getInventoryLocation());
                                restaurantTokenRecordRepository.save(restaurantTokenRecord);
                            }
                        }
                    }
                }
            }
            RestaurantTokenRecord restaurantToken = getRestaurentObjbyTableName(tableName, tableNo);
            if (restaurantTokenRecords.size() == 0) {
                List<RestaurantTokenRecord> result = restaurantTokenRecordRepository.findBySiNo(salesInvoice.getSINo());
                for (RestaurantTokenRecord restaurantTokenRec : result) {
                    RestaurantTokenRecord restaurantTokenRecord = new RestaurantTokenRecord();
                    restaurantTokenRecord.setSiNo(salesInvoice.getSINo());
                    restaurantTokenRecord.setPax(restaurantTokenRec.getPax());
                    restaurantTokenRecord.setDate(new Date());
                    restaurantTokenRecord.setTime(new SimpleDateFormat("HH:mm")
                            .format(new Date()));
                    restaurantTokenRecord.setDayEndStatus("false");
                    restaurantTokenRecord.setStatus("Prepared");
                    restaurantTokenRecord.setTableName(tableName);
                    restaurantTokenRecord.setTableId(tableNo);
                    restaurantTokenRecord.setWaiterName(waiterName);
                    restaurantTokenRecord.setSiId(salesInvoice);
                    restaurantTokenRecord.setToken(restaurantTokenRec.getToken());
                    restaurantTokenRecord.setInventoryLocation(restaurantTokenRec.getInventoryLocation());
                    restaurantTokenRecordRepository.save(restaurantTokenRecord);
                }
            }
            String tokenRecord = getTokensOnSI(salesInvoice);
            if (StringUtils.isEmpty(tokenRecord)) {
                tokenRecord = restaurantTokenRecords.stream()
                        .map(RestaurantTokenRecord::getToken)
                        .collect(Collectors.joining(", "));
            }
            //field is used to store token record
            retailDTO.setEmail(tokenRecord);
            if (restaurantToken != null) {
                restaurantToken.setPax(retailDTO.getPax());
                restaurantTokenRecordRepository.save(restaurantToken);
            }
        }
        DateFormat d = new SimpleDateFormat("dd-MMM-yyyy @hh:mm a");
        retailDTO.setDateOfInvoice(d.format(salesInvoice.getSIDate()));
        PosPaymentTypes posPaymentTypes = null;
        if ("multiPayment".equalsIgnoreCase(retailDTO.getPaymentType())) {
            createDuplicatePrintReciept(retailDTO, salesInvoice);
            posPaymentTypes = posPaymentTypesRepository.findAllBySalesInvoice(salesInvoice);
        }
        if(posPaymentTypes!=null) {
            posPaymentTypes.setOnlineInvoiceId(retailDTO.getOlInvPayId());
            posPaymentTypes.setAirPaydetailsId(retailDTO.getAirPayId());
            printDTO.setPaymentId(posPaymentTypes.getPaymenetId().toString());
        }
        printDTO = getPrintDetails(retailDTO, salesInvoice, printDTO);
        printDTO.setRestaurantDate(d.format(new Date()));
        return printDTO;
    }


    public RetailDTO createDuplicatePrintReciept(RetailDTO retailDTO, SalesInvoice salesInvoice) {
        PosPaymentTypes posPaymentTypes = null;
        if (StringUtils.isEmpty(retailDTO.getPosPaymentId())) {
            posPaymentTypes = new PosPaymentTypes();
        } else {
            posPaymentTypes = posPaymentTypesRepository.findOne(retailDTO.getPosPaymentId());
        }
        List<MultiVoucherPayment> multiVoucherPaymentsList = new ArrayList<>();
        List<MultiCardPayment> multiCardPaymentsList = new ArrayList<>();
        List<MultiBankPayment> multiBankPayments = new ArrayList<>();
        List<MultiCashPayment> multiCashPayments = new ArrayList<>();
        List<MultiAirPayPayment> swipePayment = new ArrayList<>();
        List<InvoicePayment> onlineInvPayment = new ArrayList<>();
        double totalVoucherAmount = 0.00;
        double totalCreditCardAmount = 0.00;
        double totalbankAmount = 0.00;
        double totalcashAmount = 0.00;
        double totalonlineAmount = 0.00;
        if (retailDTO.getVoucherPayment() != null) {
            if (retailDTO.getVoucherPayment().getMultiVoucherPayments().size() != 0) {
                for (MultiVoucherPayment dto : retailDTO.getVoucherPayment().getMultiVoucherPayments()) {
                    MultiVoucherPayment m = new MultiVoucherPayment();
                    m.setVoucherNo(dto.getVoucherNo());
                    m.setVoucherAmt(dto.getVoucherAmt());
                    m.setPaymentType(dto.getPaymentType());
                    totalVoucherAmount += dto.getVoucherAmt();
                    multiVoucherPaymentsList.add(m);
//                    PaymentVoucher paymentVoucher = hiposDAO.checkVoucherNo(dto.getVoucherNo());
//                    if (paymentVoucher != null) {
//                        paymentVoucher.setUsedCount(String.valueOf(Long.parseLong(paymentVoucher.getUsedCount())+1));
//                        if(StringUtils.pathEquals(paymentVoucher.getUsedCount(),paymentVoucher.getNoOfCoupans())){
//                            paymentVoucher.setStatus("Used");
//                        }
//                        hiposDAO.saveOrUpdate(paymentVoucher);
//                    }
                }
            }
        }
        if (retailDTO.getCashPayment() != null) {
            if (retailDTO.getCashPayment().getMultiCashPaymentList().size() != 0) {
                for (MultiCashPayment dto : retailDTO.getCashPayment().getMultiCashPaymentList()) {
                    MultiCashPayment m = new MultiCashPayment();
                    m.setCashAmt(dto.getCashAmt());
                    m.setPaymentType(dto.getPaymentType());
                    totalcashAmount += dto.getCashAmt();
                    multiCashPayments.add(m);
                }
            }
        }
        if (retailDTO.getAirPayments() != null) {
            if (retailDTO.getAirPayments().getAirPayPaymentList().size() != 0) {
                for (MultiAirPayPayment dto : retailDTO.getAirPayments().getAirPayPaymentList()) {
                    MultiAirPayPayment m = new MultiAirPayPayment();
                    m.setAirPayAmt(dto.getAirPayAmt());
                    m.setPaymentType(dto.getPaymentType());
                    totalonlineAmount += dto.getAirPayAmt();
                    swipePayment.add(m);
                }
            }
        }

            if (retailDTO.getInvoicePaymentList().size() != 0) {
                for (InvoicePayment dto : retailDTO.getInvoicePaymentList()) {
                    InvoicePayment i = new InvoicePayment();
                    i.setInvoicePayAmt(dto.getInvoicePayAmt());
                    i.setPaymentType(dto.getPaymentType());
                    totalonlineAmount += dto.getInvoicePayAmt();
                    onlineInvPayment.add(i);
                }
            }

        if (retailDTO.getBankPayment() != null) {
            if (retailDTO.getBankPayment().getMultiBankPaymentList().size() != 0) {
                for (MultiBankPayment dto : retailDTO.getBankPayment().getMultiBankPaymentList()) {
                    MultiBankPayment b = new MultiBankPayment();
                    b.setBankAccountId(dto.getBankAccountId());
                    b.setBankAccount(dto.getBankAccount());
                    b.setAmount(dto.getAmount());
                    b.setInvoiceNo(dto.getInvoiceNo());
                    Date date = new Date();
                    if (dto.getDate() == null) {
                        dto.setDate(new Date());
                        b.setDate(date);
                    } else {
                        b.setDate(dto.getDate());
                    }
                    b.setBankName(dto.getBankName());
                    b.setPaymentType(dto.getPaymentType());
                    totalbankAmount += dto.getAmount();
                    multiBankPayments.add(b);
                }
            }
        }
        if (retailDTO.getCreditPayment() != null) {
            if (retailDTO.getCreditPayment().getCardPaymentList().size() != 0) {
                for (MultiCardPayment cardPayment : retailDTO.getCreditPayment().getCardPaymentList()) {
                    MultiCardPayment multiCardPayment = new MultiCardPayment();
                    multiCardPayment.setCardAmt(cardPayment.getCardAmt());
                    multiCardPayment.setCardNo(cardPayment.getCardNo());
                    multiCardPayment.setPaymentType(cardPayment.getPaymentType());
                    multiCardPayment.setCardBankName(cardPayment.getCardBankName());
                    if (cardPayment.getCardDate() != null) {
                        multiCardPayment.setCardDate(cardPayment.getCardDate());
                    } else {
                        multiCardPayment.setCardDate(new Date().toString());
                    }
                    totalCreditCardAmount += cardPayment.getCardAmt();
                    multiCardPaymentsList.add(multiCardPayment);
                }
            }
        }
        double totalExAmt = 0;
        double totalInAmt = 0;
        for (SelectedItem selectedItem : retailDTO.getSelectedItemsList()) {
            totalExAmt = selectedItem.getAmtexclusivetax() + totalExAmt;
            totalInAmt = selectedItem.getAmtinclusivetax() + totalInAmt;
        }
        posPaymentTypes.setSalesInvoice(salesInvoice);
        if (!StringUtils.isEmpty(retailDTO.getRoundingOffValue()))
            posPaymentTypes.setRoundingAdjustment(Double.parseDouble(retailDTO.getRoundingOffValue()));
        posPaymentTypes.setTotalVoucherPayment(totalVoucherAmount);
        posPaymentTypes.setTotalCardPayment(totalCreditCardAmount);
        posPaymentTypes.setTotalBankAmt(totalbankAmount);
        posPaymentTypes.setTotalCashPayment(totalcashAmount);
        posPaymentTypes.setOlPayAmount(totalonlineAmount);
        String json1 = new Gson().toJson(multiVoucherPaymentsList);
        String jsoncardpayment = new Gson().toJson(multiCardPaymentsList);
        String jsonbankpayment = new Gson().toJson(multiBankPayments);
        String jsoncashpayment = new Gson().toJson(multiCashPayments);
        String jsonSwipePayment = new Gson().toJson(swipePayment);
        String jsonOlInvPayment = new Gson().toJson(onlineInvPayment);
        posPaymentTypes.setVoucherPayment(json1);
        posPaymentTypes.setCardPayment(jsoncardpayment);
        posPaymentTypes.setBankPayment(jsonbankpayment);
        if(!StringUtils.isEmpty(jsonSwipePayment)){
            posPaymentTypes.setOnlinePayment(jsonSwipePayment);
        }else if(!StringUtils.isEmpty(jsonOlInvPayment)){
            posPaymentTypes.setOnlinePayment(jsonOlInvPayment);
        }
        else{
            posPaymentTypes.setOnlinePayment("[]");
        }
        posPaymentTypes.setBankPayment(jsonbankpayment);
        posPaymentTypes.setCustPaymentNo(retailDTO.getCustPaymentNo());
        posPaymentTypes.setCashPayment(jsoncashpayment);
        posPaymentTypes.setCustomer(salesInvoice.getCustomerId());
        posPaymentTypes.setTotalAmount(retailDTO.getAmountReturned());
        posPaymentTypes.setCount(retailDTO.getItemCount());
        posPaymentTypesRepository.save(posPaymentTypes);
        retailDTO.setRoundingAdj(retailDTO.getTotalCheckOutamt() - totalInAmt);
        retailDTO.setAmtToBePaid(Math.round(retailDTO.getTotalCheckOutamt()));
        return retailDTO;
    }

    public RetailPrintDTO getPrintDetails(RetailDTO retailDTO, SalesInvoice si, RetailPrintDTO printDTO) {
        retailDTO.setSiNo(si.getSINo());
        retailDTO.setSrlnNo(si.getSerialNumber());
        if(printDTO==null){
            printDTO=new RetailPrintDTO();
        }
        printDTO.setSiData(retailDTO);
        printDTO.setHiPosServiceCharge(retailDTO.getHiPosServiceCharge());
        printDTO.setHiposServiceChargeAmt(retailDTO.getHiposServiceChargeAmt());
        if (retailDTO.getServiceCharge() != null) {
            printDTO.getSiData().setTotalServiceCharge(Double.parseDouble(retailDTO.getServiceCharge()));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy @ hh:mm a");
        printDTO.setDate(sdf.format(new Date()).toString());
        return printDTO;
    }


    public String getTokensOnSI(SalesInvoice salesInvoice) {
        List<String> result = restaurantTokenRecordRepository.findBy(salesInvoice.getSINo());
        Gson gson = new Gson();
        return (gson.toJson(result).replaceAll("\\[|\\]|\"", ""));
    }

    public String getTokensOn(Long id) {
        List<String> result = restaurantTokenRecordRepository.findBy(String.valueOf(id));
        Gson gson = new Gson();
        return (gson.toJson(result).replaceAll("\\[|\\]|\"", ""));
    }

    @Transactional
    public RetailPrintDTO getAllDetails(RetailDTO retailDTO, String tableNo, String tableName, String waiterName) {
        RetailPrintDTO printDTO = new RetailPrintDTO();
        printDTO.setSiData(retailDTO);
        double totalVoucherAmt = 0;
        double totalCardAmt = 0;
        double totalBankAmt = 0;
        double totalCashAmt = 0;
        if (printDTO.getSiData().getVoucherPayment() != null) {
            for (MultiVoucherPayment voucherPayment : printDTO.getSiData().getVoucherPayment().getMultiVoucherPayments()) {
                PaymentMethod paymentMethod = getPaymentmethodObject(voucherPayment.getPaymentType());
                totalVoucherAmt = totalVoucherAmt + voucherPayment.getVoucherAmt();
                retailDTO.getVoucherPayment().setTotalVPAmountTendered(totalVoucherAmt);
                retailDTO.getVoucherPayment().setPaymentType(paymentMethod.getPaymentmethodName());
                voucherPayment.setPaymentName(paymentMethod.getPaymentmethodName());
            }
        }
        if (printDTO.getSiData().getCashPayment() != null) {
            for (MultiCashPayment cashPayment : printDTO.getSiData().getCashPayment().getMultiCashPaymentList()) {
                PaymentMethod paymentMethod = getPaymentmethodObject(cashPayment.getPaymentType());
                totalCashAmt = totalCashAmt + cashPayment.getCashAmt();
                cashPayment.setPaymentName(paymentMethod.getPaymentmethodName());
                retailDTO.getCashPayment().setTotalCPAmountTendered(totalCashAmt);
                retailDTO.getCashPayment().setPaymentType(getPaymentmethodObject(cashPayment.getPaymentType()).getPaymentmethodName());
            }
        }
        if (printDTO.getSiData().getBankPayment() != null) {
            for (MultiBankPayment bankPayment : printDTO.getSiData().getBankPayment().getMultiBankPaymentList()) {
                PaymentMethod paymentMethod = getPaymentmethodObject(Long.parseLong(bankPayment.getPaymentType()));
                totalBankAmt = totalBankAmt + bankPayment.getAmount();
                bankPayment.setPaymentName(paymentMethod.getPaymentmethodName());
                retailDTO.getBankPayment().setTotalBPAmountTendered(totalBankAmt);
                retailDTO.getBankPayment().setPaymentType(getPaymentmethodObject(Long.parseLong(bankPayment.getPaymentType())).getPaymentmethodName());
            }
        }
        if (printDTO.getSiData().getCreditPayment() != null) {
            for (MultiCardPayment cardPayment : printDTO.getSiData().getCreditPayment().getCardPaymentList()) {
                totalCardAmt = totalCardAmt + cardPayment.getCardAmt();
                PaymentMethod paymentMethod = getPaymentmethodObject(cardPayment.getPaymentType());
                cardPayment.setPaymentName(paymentMethod.getPaymentmethodName());
                retailDTO.getCreditPayment().setTotalCCPAmountTendered(totalCardAmt);
                retailDTO.getCreditPayment().setPaymentType(getPaymentmethodObject(cardPayment.getPaymentType()).getPaymentmethodName());
            }
        }
        retailDTO.setTable(tableName);
        retailDTO.setSiStatus("Prepared");
        if (!StringUtils.isEmpty(retailDTO.getTotalRemaininBalance()))
            if (Double.parseDouble(retailDTO.getTotalRemaininBalance()) > 0) {
                retailDTO.setTotalTenderedAmount(retailDTO.getTotalTenderedAmount() - Double.parseDouble(retailDTO.getTotalRemaininBalance()));
                if (retailDTO.getCashPayment() != null)
                    retailDTO.getCashPayment().getMultiCashPaymentList().get(0).setCashAmt(retailDTO.getCashPayment().getMultiCashPaymentList().get(0).getCashAmt() - Double.parseDouble(retailDTO.getTotalRemaininBalance()));
            }
        Employee employee = getEmployeeNameObject(waiterName);
        if (employee != null)
            retailDTO.setEmployeeId(employee.getEmployeeId().toString());
        DateFormat d = new SimpleDateFormat("dd-MMM-yyyy @hh:mm a");
        printDTO.setRestaurantDate(d.format(new Date()));
        if (!StringUtils.isEmpty(retailDTO.getAgentIdOfInvoice())) {
            Agent agent = getAgentObjectbyName(retailDTO.getAgentIdOfInvoice());
            if (agent != null) {
                retailDTO.setAgentIdOfInvoice(agent.getAgentId().toString());
                retailDTO.setAgentName(agent.getAgentName());
            }
        }
        List<TaxSummary> taxPerList = new ArrayList<>();
        AccountSetup accountSetup = getAccountSetup();
        TaxSummary taxSummary = new TaxSummary();
        taxSummary.setTaxName("");
        taxSummary.setTaxableAmt(retailDTO.getTotalCheckOutamt()-retailDTO.getTotalTaxAmt());
        taxSummary.setTaxAmount(retailDTO.getTotalTaxAmt());
        taxSummary.setTaxPercent(Double.parseDouble(accountSetup.getTaxId()));
        taxSummary.setCessAmt(0);
        taxSummary.setCessPercent(0);
        taxPerList.add(taxSummary);
        retailDTO.setTaxWiseSummaryList(taxPerList);
        RestaurantTokenRecord restaurantToken = getRestaurentObjbyTableName(tableName, tableNo);
        if (restaurantToken != null) {
            retailDTO.setPax(restaurantToken.getPax());
        }
        retailDTO.setRoundingAdj(Double.parseDouble(retailDTO.getRoundingOffValue()));
        Customer customer = getCustomerObject(retailDTO.getCustomerId());
//        printDTO.setCompanyLogoPath(printDTO.getCompanyData().getLogo());
        //set waiter name to employee
        printDTO.getSiData().setEmployeeName(waiterName);
        printDTO.getSiData().setCutomerName(retailDTO.getCutomerName());
        if (customer != null) {
            printDTO.getSiData().setCustomerNo(customer.getTelephone());
            printDTO.getSiData().setCustomerAddress(customer.getAddress1());
            printDTO.getSiData().setCustomerPincode(customer.getPostcode());
        }
        RestaurantTempData resturantTempData = getRestTempDataOnId(tableName, tableNo);
        String tokenRecord = getTokensOn(resturantTempData.getId());
        if (StringUtils.isEmpty(tokenRecord)) {
            List<RestaurantTokenRecord> result = getRestaurantTokenRecord();
            tokenRecord = result.stream()
                    .map(RestaurantTokenRecord::getToken)
                    .collect(Collectors.joining(", "));
        }
        if (StringUtils.isEmpty(retailDTO.getOrderType())) {
            retailDTO.setOrderType("DineIn");
        }
        //field is used to store token record
        retailDTO.setEmail(tokenRecord);
        //set table name
        retailDTO.setTable(tableName);
        retailDTO.setRestaurantDiscount(retailDTO.getDiscountAmount());
        printDTO.setHiPosServiceCharge(retailDTO.getHiPosServiceCharge());
        printDTO.setHiposServiceChargeAmt(retailDTO.getHiposServiceChargeAmt());
        if (retailDTO.getServiceCharge() != null) {
            printDTO.getSiData().setTotalServiceCharge(Double.parseDouble(retailDTO.getServiceCharge()));
        }
//        retailDTO.setCompanyData(companyInfoDto);
        printDTO.setSiData(retailDTO);
        return printDTO;
    }


    public RestaurantTempData getRestTempDataOnId(String tableName, String tableId) {
        return getTempDataBasedOnId(tableName, tableId);
    }

    public String saveTableConfig(TableConfigPojo tableConfigPojo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            TableConfig tableConfig = new TableConfig();
            if (tableConfigPojo.getId() != null) {
                tableConfig = tableConfigRepository.findByConfigurationnameAndRowtableconfigAndColumntableconfigAndTableconfigidNotIn(tableConfigPojo.getZone_name(), tableConfigPojo.getNo_of_rows(), tableConfigPojo.getNo_of_col(), tableConfigPojo.getId());
            } else {
                tableConfig = tableConfigRepository.findByConfigurationnameAndRowtableconfigAndColumntableconfig(tableConfigPojo.getZone_name(), tableConfigPojo.getNo_of_rows(), tableConfigPojo.getNo_of_col());
            }
            JSONObject jsonObject = new JSONObject();
            if (tableConfig == null) {
                tableConfig = TableConfigMapper.MapTableConfigPojoToEntity(tableConfigPojo);
                tableConfigRepository.save(tableConfig);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }else {
            JSONObject jsonObject  = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("no_of_rows","2");
            jsonObject.put("no_of_col","4");
            jsonObject.put("id",tableConfigPojo.getId());
            jsonObject.put("zone_name",tableConfigPojo.getZone_name());
            jsonObject.put("discription",tableConfigPojo.getDiscription());
            jsonObject.put("status",tableConfigPojo.getStatus());
            String url = readDomainName() + "/services/Litecall/saveZone";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    };

    @Transactional
    public Map reportForPeriodList(String dayEndStatus, String type, String location) {
        Map result = new HashMap();
        List<Category> itemCategoryList = categoryRepository.findAll();
        List<String> invoiceList = restaurantTokenRecordRepository.findByDayEndStatus();
        if (invoiceList.isEmpty())
            return result;

        List<PosPaymentTypes> posPaymentTypes = posPaymentTypesRepository.findBySalesInvoice(invoiceList);
        double totalAmt = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getTotalReceivable()).sum();
        double cashAmt = posPaymentTypes.stream().mapToDouble(o -> o.getTotalCashPayment()).sum();
        double roundingOffAmt = posPaymentTypes.stream().mapToDouble(o -> o.getRoundingAdjustment()).sum();
        double serviceCharge = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getServiceChargeAmt()).sum();
        double discountAmt = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getTotalDiscountAmount()).sum();
        List<MultiBankPayment> multiBankPaymentList = new ArrayList<>();
        List<MultiCardPayment> multiCardPaymentList = new ArrayList<>();
        List<MultiVoucherPayment> multiVoucherPaymentList = new ArrayList<>();
        List<MultiCashPayment> multiCashPaymentList = new ArrayList<>();
        Gson json = new Gson();
        Type voucherPayment = new TypeToken<ArrayList<MultiVoucherPayment>>() {
        }.getType();
        Type cardPayment = new TypeToken<ArrayList<MultiCardPayment>>() {
        }.getType();
        Type bankPayment = new TypeToken<ArrayList<MultiBankPayment>>() {
        }.getType();
        Type cashPayment = new TypeToken<ArrayList<MultiCashPayment>>() {
        }.getType();
        for (PosPaymentTypes posPaymentTypes1 : posPaymentTypes) {
            multiBankPaymentList.addAll(json.fromJson(posPaymentTypes1.getBankPayment(), bankPayment));
            multiCardPaymentList.addAll(json.fromJson(posPaymentTypes1.getCardPayment(), cardPayment));
            multiVoucherPaymentList.addAll(json.fromJson(posPaymentTypes1.getVoucherPayment(), voucherPayment));
            multiCashPaymentList.addAll(json.fromJson(posPaymentTypes1.getCardPayment(), cashPayment));
        }
        for (MultiVoucherPayment multiVoucherPayment : multiVoucherPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiVoucherPayment.getVoucherAmt());
            multiBankPayment.setPaymentType(multiVoucherPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiCardPayment multiCardPayment : multiCardPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiCardPayment.getCardAmt());
            multiBankPayment.setPaymentType(multiCardPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiCashPayment multiCashPayment : multiCashPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiCashPayment.getCashAmt());
            multiBankPayment.setPaymentType(multiCashPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiBankPayment multiBankPayment : multiBankPaymentList) {
            if (!StringUtils.isEmpty(multiBankPayment.getPaymentType())) {
                PaymentMethod paymentMethod = paymentMethodRepository.findOne(Long.parseLong(multiBankPayment.getPaymentType()));
                multiBankPayment.setPaymentType(paymentMethod.getPaymentmethodName());
            }
        }
        Map<String, Double> resultMap = multiBankPaymentList.stream().filter(c -> c.getPaymentType() != null).collect(
                Collectors.groupingBy(MultiBankPayment::getPaymentType, Collectors.summingDouble(MultiBankPayment::getAmount)));
        resultMap.put("Cash", cashAmt);
        for (Category itemCategory : itemCategoryList) {
            List<Map> categoryItemList = salesInvoiceDetailsRepository.findBy(invoiceList, itemCategory);
            //holds the reference of category total
            float total = 0;
            for (Map<String, Double> element : categoryItemList) {
                total += element.get("totalAmtReceived");
            }
            Map newVar = new HashMap<>();
            {
                newVar.put("itemId", "");
                newVar.put("itemName", "");
                //newVar.put("totalAmtReceived", "");
                newVar.put("QtySold", "");
                newVar.put("total", String.valueOf(total));
            }

            if (!categoryItemList.isEmpty() && categoryItemList.get(0).get("itemId") != null) {
                categoryItemList.add(newVar);
                result.put(itemCategory.getItemCategoryName(), categoryItemList);
            }
        }
        result.put("Discount", discountAmt);
        if (resultMap.get("Discount Voucher") == null) {
            resultMap.put("Discount Voucher", 0D);
        }
        double amt = resultMap.get("Discount Voucher").doubleValue();
        resultMap.put("Discount Voucher", amt);
        result.put("Rounding Off", roundingOffAmt);
        result.put("Service Charge", serviceCharge);
        result.put("Total Amount", totalAmt);
        result.put("Payment", resultMap);
        //finally reset token counter
        if (dayEndStatus != null && dayEndStatus.equalsIgnoreCase("true")) {
            updateDayEndStatus();
        }
        return result;
    }

    public RestaurantTokenRecord updateDayEndStatus() {
        RestaurantTokenRecord restaurantTokenRecord = null;
        try {
            List<RestaurantTokenRecord> restaurantTokenRecords = restaurantTokenRecordRepository.findBy();
            for (RestaurantTokenRecord tokenRecord : restaurantTokenRecords) {
                tokenRecord.setDayEndStatus("true");
                tokenRecord.setDayEndDate(new Date());
                restaurantTokenRecordRepository.save(tokenRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurantTokenRecord;
    }

    public String saveTablesPos(TablesPosPojo tablesPosPojo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            TablesPos tablesPos = new TablesPos();
            if (tablesPosPojo.getTable_id() != null) {
                tablesPos = tablesPosRepository.findAllByTableNameAndTableidNotIn(tablesPosPojo.getTable_name(), tablesPosPojo.getTable_id());
            } else {
                tablesPos = tablesPosRepository.findAllByTableName(tablesPosPojo.getTable_name());

            }
            JSONObject jsonObject = new JSONObject();
            if (tablesPos == null) {
                tablesPos = TablesPosMapper.MapTablesPosPojoToEntity(tablesPosPojo);
                tablesPosRepository.save(tablesPos);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("min_capacity",tablesPosPojo.getMin_capacity());
            jsonObject.put("max_capacity",tablesPosPojo.getMax_capacity());
            jsonObject.put("zone_id",tablesPosPojo.getZone_id());
            jsonObject.put("table_id",tablesPosPojo.getTable_id());
            jsonObject.put("table_name",tablesPosPojo.getTable_name());
            jsonObject.put("table_status",tablesPosPojo.getTable_status());
            String url = readDomainName() + "/services/Litecall/saveTable";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

    public String saveAgent(AgentPojo agentPojo)throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            Agent agent = new Agent();
            if (agentPojo.getId() != null) {
                agent = agentRepository.findAllByAgentNameAndAgentIdNotIn(agentPojo.getAgentName(), agentPojo.getId());
            } else {
                agent = agentRepository.findByAgentName(agentPojo.getAgentName());
            }
            JSONObject jsonObject = new JSONObject();
            if (agent == null) {
                agent = AgentMapper.MapAgentPojoToEntity(agentPojo);
                agentRepository.save(agent);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("id",agentPojo.getId());
            jsonObject.put("agentName",agentPojo.getAgentName());
            jsonObject.put("effectiveDate",agentPojo.getEffectiveDate());
            jsonObject.put("email",agentPojo.getEmail());
            jsonObject.put("mobile",agentPojo.getMobile());
            jsonObject.put("address",agentPojo.getAddress());
            jsonObject.put("commission",agentPojo.getCommission());
            jsonObject.put("gstinNo",agentPojo.getGstinNo());
            jsonObject.put("ecommerce",agentPojo.getEcommerce());
            jsonObject.put("status",agentPojo.getStatus());
            String url = readDomainName() + "/services/Litecall/saveAgent";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;

        }
    }

    public UserAccountSetup changeCredentails(UserAccountSetUpDTO userAccountSetUpDTO){
        UserAccountSetup userAccountSetup = userAccountSetupRepository.findAllByFullName(userAccountSetUpDTO.getFull_name());
        userAccountSetup.setReTypePassword(userAccountSetUpDTO.getReTypePassword());
        userAccountSetup.setPasswordUser(userAccountSetUpDTO.getReTypePassword());
        userAccountSetupRepository.save(userAccountSetup);
        return userAccountSetup;
    }

    public String saveCustomer(CustomerPojo customerPojo)throws Exception{
        if(StringUtils.pathEquals(buildType,"desktop")) {
            Customer customer = new Customer();
            if (customerPojo.getCustomer_id() != null) {
                customer = customerRepository.findAllByFirstNameAndCustomerIdNotIn(customerPojo.getFirst_name(), customerPojo.getCustomer_id());
            } else {
                customer = customerRepository.findByFirstName(customerPojo.getFirst_name());

            }
            JSONObject jsonObject = new JSONObject();
            if (customer == null) {
                customer = CustomerMapper.MapCustomerPojoToEntity(customerPojo);
                customerRepository.save(customer);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else{

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("customer_id",customerPojo.getCustomer_id());
            jsonObject.put("first_name",customerPojo.getFirst_name());
            jsonObject.put("last_name",customerPojo.getLast_name());
            jsonObject.put("email",customerPojo.getEmail());
            jsonObject.put("password",customerPojo.getPassword());
            jsonObject.put("telephone",customerPojo.getTelephone());
            jsonObject.put("address_1",customerPojo.getAddress_1());
            jsonObject.put("city",customerPojo.getCity());
            jsonObject.put("state",customerPojo.getState());
            jsonObject.put("postcode",customerPojo.getPostcode());
            jsonObject.put("country_id",customerPojo.getCountry_id());
            if(org.apache.commons.lang3.StringUtils.isEmpty(customerPojo.getLast_name())){
                customerPojo.setLast_name("");
            }
            if(org.apache.commons.lang3.StringUtils.isEmpty(customerPojo.getAddress_1())){
                customerPojo.setAddress_1("");
            }
            if(org.apache.commons.lang3.StringUtils.isEmpty(customerPojo.getEmail())){
                customerPojo.setEmail("");
            }
            if(org.apache.commons.lang3.StringUtils.isEmpty(customerPojo.getPostcode())){
                customerPojo.setPostcode("");
            }
            if(org.apache.commons.lang3.StringUtils.isEmpty(customerPojo.getTelephone())){
                customerPojo.setTelephone("");
            }
            if(org.apache.commons.lang3.StringUtils.isEmpty(customerPojo.getCountry_id())){
                customerPojo.setCountry_id("");
            }
            if(org.apache.commons.lang3.StringUtils.isEmpty(customerPojo.getState())){
                customerPojo.setState("");
            }
            if(org.apache.commons.lang3.StringUtils.isEmpty(customerPojo.getCity())){
                customerPojo.setCity("");
            }
            jsonObject.put("status",customerPojo.getStatus());
            Gson gson = new Gson();
            String url = readDomainName() + "/services/Litecall/saveCustomer";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;

        }
    }

    public List<PaymentMethodDTO> getPaymentTypes(String type) {
        List<PaymentMethodDTO> paymentTypes = getPaymentMethodDtoObjectOrList(type);
        return paymentTypes;
    }

    public List<PaymentMethodDTO> getPaymentMethodDtoObjectOrList(String type) {
        List<PaymentMethodDTO> paymentMethodDTOList = new ArrayList<>();
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAllByStatus("Active");
        paymentMethodDTOList.addAll(ObjectMapperUtils.mapAll(paymentMethodList, PaymentMethodDTO.class));
        return paymentMethodDTOList;
    }

    public String saveCurrency(CurrencyPojo currencyPojo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            Currency currency = new Currency();
            if (currencyPojo.getCurrency_id() != null) {
                currency = currencyRepository.findAllByCurrencyNameAndCurrencyIdNotIn(currencyPojo.getCurrency_name(), currencyPojo.getCurrency_id());
            } else {
                currency = currencyRepository.findAllByCurrencyName(currencyPojo.getCurrency_name());

            }
            JSONObject jsonObject = new JSONObject();
            if (currency == null) {
                currency = CurrecncyMapper.mapPojoToEntity(currencyPojo);
                currencyRepository.save(currency);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("currency_id",currencyPojo.getCurrency_id());
            jsonObject.put("currency_name",currencyPojo.getCurrency_name());
            jsonObject.put("currency_code",currencyPojo.getCurrency_code());
            jsonObject.put("currency_symbol",currencyPojo.getCurrency_symbol());
            jsonObject.put("currency_status",currencyPojo.getCurrency_status());
            jsonObject.put("country_id",currencyPojo.getCountry_id());
            String url = readDomainName() + "/services/Litecall/saveCurrency";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }
    public String saveDeliveryArea(DeliveryAreaPojo deliveryAreaPojo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            DeliveryArea deliveryArea = new DeliveryArea();
            if (deliveryAreaPojo.getId() != null) {
                deliveryArea = deliveryAreaRepository.findAllByNameAndIdNotIn(deliveryAreaPojo.getName(), deliveryAreaPojo.getId());
            } else {
                deliveryArea = deliveryAreaRepository.findAllByName(deliveryAreaPojo.getName());

            }
            JSONObject jsonObject = new JSONObject();
            if (deliveryArea == null) {
                deliveryArea = DeliveryMapper.mapPojoToEntity(deliveryAreaPojo);
                deliveryAreaRepository.save(deliveryArea);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("area_id",deliveryAreaPojo.getId());
            jsonObject.put("name",deliveryAreaPojo.getName());
            jsonObject.put("type",deliveryAreaPojo.getType());
//            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(deliveryAreaPojo.getStatus(), "Active")) {
//                deliveryAreaPojo.setStatus("1");
//            } else {
//                deliveryAreaPojo.setStatus("0");
//            }
            jsonObject.put("status",deliveryAreaPojo.getStatus());
            String url = readDomainName() + "/services/Litecall/SaveArea";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }
    public String saveVoucher(PaymentVoucherPojo paymentVoucherPojo)throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            PaymentVoucher paymentVoucher = new PaymentVoucher();
            if (paymentVoucherPojo.getCoupon_id() != null) {
                paymentVoucher = voucherRepository.findAllByCodeAndCouponidNotIn(paymentVoucherPojo.getCode(), paymentVoucherPojo.getCoupon_id());
            } else {
                paymentVoucher = voucherRepository.findAllByCode(paymentVoucherPojo.getCode());

            }
            JSONObject jsonObject = new JSONObject();
            if (paymentVoucher == null) {
                paymentVoucher = VoucherMapper.MapPojoToEntity(paymentVoucherPojo);
                voucherRepository.save(paymentVoucher);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("coupon_id",paymentVoucherPojo.getCoupon_id());
            jsonObject.put("name",paymentVoucherPojo.getName());
            jsonObject.put("code",paymentVoucherPojo.getCode());
            jsonObject.put("type",paymentVoucherPojo.getType());
            jsonObject.put("discount",paymentVoucherPojo.getDiscount());
            jsonObject.put("min_total",paymentVoucherPojo.getMin_total());
            jsonObject.put("redemptions",paymentVoucherPojo.getRedemptions());
            jsonObject.put("customer_redemptions",paymentVoucherPojo.getCustomer_redemptions());
            jsonObject.put("order_restriction",paymentVoucherPojo.getOrder_restriction());
            jsonObject.put("validity",paymentVoucherPojo.getValidity());
            jsonObject.put("description",paymentVoucherPojo.getDescription());

            jsonObject.put("date_added",paymentVoucherPojo.getDate_added());
            jsonObject.put("fixed_date",paymentVoucherPojo.getFixed_date());
            jsonObject.put("fixed_from_time",paymentVoucherPojo.getFixed_from_time());
            jsonObject.put("fixed_to_time",paymentVoucherPojo.getFixed_to_time());
            jsonObject.put("period_start_date",paymentVoucherPojo.getPeriod_start_date());
            jsonObject.put("period_end_date",paymentVoucherPojo.getPeriod_end_date());
            jsonObject.put("recurring_every",paymentVoucherPojo.getRecurring_every());
            jsonObject.put("recurring_from_time",paymentVoucherPojo.getRecurring_from_time());
            jsonObject.put("recurring_to_time",paymentVoucherPojo.getRecurring_to_time());
            jsonObject.put("status",paymentVoucherPojo.getStatus());
            String url = readDomainName() + "/services/Litecall/Savepaymentvoucher";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

//    public AddNewItemDTO createSaveNewItemDetails(AddNewItemDTO saveNewItemDetails) throws JSONException {
//        AddNewItemDTO itemDetails = null;
//        byte byteArray[];
//        String fileName = FileSystemOperations.getImagesDirItem() + File.separator + saveNewItemDetails.getItemCode() + ".png";
//        //read item image
//        if (!StringUtils.isEmpty(saveNewItemDetails.getItemImage())) {
//            try {
//                FileOutputStream fos = new FileOutputStream(fileName);
//                byteArray = org.apache.commons.codec.binary.Base64.decodeBase64(saveNewItemDetails.getItemImage().split(",")[1]);
//                //write to file
//                fos.write(byteArray);
//                fos.flush();
//                fos.close();
//                saveNewItemDetails.setItemImage(fileName);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (saveNewItemDetails.getItemId() != null && saveNewItemDetails.getItemId() != 0) {
//            Item item = itemRespository.findByItemNameOrItemCodeAndItemIdNotIn(saveNewItemDetails.getItemName(), saveNewItemDetails.getItemCode(), saveNewItemDetails.getItemId());
//            if (item != null) {
//                itemDetails = new AddNewItemDTO();
//                itemDetails.setMessage("Duplicate");
//                return itemDetails;
//            }
//        } else {
//            Item item = itemRespository.findByItemNameOrItemCode(saveNewItemDetails.getItemName(), saveNewItemDetails.getItemCode());
//            if (item != null) {
//                itemDetails = new AddNewItemDTO();
//                itemDetails.setMessage("Duplicate");
//                return itemDetails;
//            }
//        }
//        Category itemCategory = categoryRepository.findOne(saveNewItemDetails.getItemCategory().getCategoryId());
//        Item terms = new Item();
//        try {
//            terms.setItemCode(saveNewItemDetails.getItemCode());
//            if(saveNewItemDetails.getItemId()!=null) {
//                terms.setItemId(saveNewItemDetails.getItemId());
//            }
//            terms.setItemName(saveNewItemDetails.getItemName());
//            terms.setItemStatus(saveNewItemDetails.getItemStatus());
//            terms.setItemtype(saveNewItemDetails.getItemType());
//            terms.setHsnCode(saveNewItemDetails.getHsnCode());
//            terms.setFoodtype(saveNewItemDetails.getFoodtype());
//            terms.setReorderlevel(saveNewItemDetails.getReOrderLevel());
//            terms.setItemPrice(saveNewItemDetails.getSalesPrice());
//            terms.setItemStatus(saveNewItemDetails.getItemStatus());
//            terms.setItemDesc(saveNewItemDetails.getItemDesc());
//            terms.setImageFile(saveNewItemDetails.getItemImage());
//            terms.setIdItemCategory(itemCategory);
//            terms.setInclusiveJSON(saveNewItemDetails.getInclusiveJSON());
//            if (saveNewItemDetails.getProductionItem() == "true") {
//                terms.setProductionName("productionName");
//            }
//            itemRespository.save(terms);
//
//        } catch (HibernateException he) {
//            he.printStackTrace();
//        }
//        return itemDetails;
//    }
    public String createSaveNewItemDetails(ItemPojo itemPojo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            Item item = new Item();
            if (itemPojo.getMenu_id() != null) {
                item = itemRespository.findAllByItemNameAndItemIdNotIn(itemPojo.getMenu_name(), itemPojo.getMenu_id());
            } else {
                item = itemRespository.findAllByItemName(itemPojo.getMenu_name());

            }
            JSONObject jsonObject = new JSONObject();
            if (item == null) {
                item = ItemMapper.mapPojoToEntity(itemPojo);
                itemRespository.save(item);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("menu_name",itemPojo.getMenu_name());
            jsonObject.put("menu_price",itemPojo.getMenu_price());
            jsonObject.put("menu_category_id",itemPojo.getMenu_category_id());
            jsonObject.put("stock_qty","1");
            jsonObject.put("subtract_stock","1");
            jsonObject.put("mealtime_id",itemPojo.getMealtime_id());
            jsonObject.put("menu_description",itemPojo.getMenu_description());
            jsonObject.put("menu_photo","");
            jsonObject.put("menu_id",itemPojo.getMenu_id());
            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(itemPojo.getTaxtype(), "true")) {
                itemPojo.setTaxtype("Inclusive");
            } else {
                itemPojo.setTaxtype("Exclusive");
            }
            jsonObject.put("taxtype",itemPojo.getTaxtype());
            jsonObject.put("itemType",itemPojo.getItemType());
            jsonObject.put("taxpercent",itemPojo.getTaxpercent());
            jsonObject.put("image","");
//            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(itemPojo.getMenu_status(), "Active")) {
//                itemPojo.setMenu_status("1");
//            } else {
//                itemPojo.setMenu_status("0");
//            }
            jsonObject.put("menu_status",itemPojo.getMenu_status());
            String url = readDomainName() + "/services/Litecall/saveitem";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;
        }
    }

    public AccountSetup saveAccountSetup(AccountSetupPojo accountSetupPojo) {
        AccountSetup accountSetup = new AccountSetup();
        if (accountSetup == null) {
            accountSetup = AccountSetupMapper.MapPojoToEntity(accountSetupPojo);
            accountSetupRepository.save(accountSetup);
            return accountSetup;
        } else {
            return null;
        }
    }

    public BasePojo calculatePagination(BasePojo basePojo, int size) {
        if (basePojo.isLastPage() == true) {
            basePojo.setFirstPage(false);
            basePojo.setNextPage(true);
            basePojo.setPrevPage(false);
        } else if (basePojo.isFirstPage() == true) {
            basePojo.setLastPage(false);
            basePojo.setNextPage(false);
            basePojo.setPrevPage(true);
            if (basePojo.isStatus() == true) {
                basePojo.setLastPage(true);
                basePojo.setNextPage(true);
            }
        } else if (basePojo.isNextPage() == true) {
            basePojo.setLastPage(false);
            basePojo.setFirstPage(false);
            basePojo.setPrevPage(false);
            basePojo.setNextPage(false);
            if (basePojo.isStatus() == true) {
                basePojo.setLastPage(true);
                basePojo.setNextPage(true);
            }
        } else if (basePojo.isPrevPage() == true) {
            basePojo.setLastPage(false);
            basePojo.setFirstPage(false);
            basePojo.setNextPage(false);
            basePojo.setPrevPage(false);
            if (basePojo.isStatus() == true) {
                basePojo.setPrevPage(true);
                basePojo.setFirstPage(true);
            }
        }
        if (size == 0) {
            basePojo.setLastPage(true);
            basePojo.setFirstPage(true);
            basePojo.setNextPage(true);
            basePojo.setPrevPage(true);
        }
        return basePojo;
    }

    public BasePojo getPaginatedUserAccountSetupList(String searchText, String status, BasePojo basePojo) {
        List<UserAccountSetup> list = new ArrayList<>();
        basePojo.setPageSize(paginatedConstants);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "useraccountId"));
        if (basePojo.isLastPage() == true) {
            List<UserAccountSetup> list1 = new ArrayList<>();
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list1 = userAccountSetupRepository.findAllByUserloginIdContainingAndStatus(searchText, status);
            } else {
                list1 = userAccountSetupRepository.findAllByStatus(status);
            }
            int size = list1.size() % paginatedConstants;
            int pageNo = list1.size() / paginatedConstants;
            if (size != 0) {
                basePojo.setPageNo(pageNo);
            } else {
                basePojo.setPageNo(pageNo - 1);
            }
            sort = new Sort(new Sort.Order(Sort.Direction.DESC, "useraccountId"));
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
            status = "Active";
        }
        UserAccountSetup qualification = new UserAccountSetup();
        Pageable pageable = new PageRequest(basePojo.getPageNo(), basePojo.getPageSize(), sort);
        if (basePojo.isNextPage() == true || basePojo.isFirstPage() == true) {
            sort = new Sort(new Sort.Order(Sort.Direction.ASC, "useraccountId"));
        }
        if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
            qualification = userAccountSetupRepository.findFirstByUserloginIdContainingAndStatus(searchText, status, sort);
            list = userAccountSetupRepository.findAllByUserloginIdContainingAndStatus(searchText, status, pageable);
        } else {
            qualification = userAccountSetupRepository.findFirstByStatus(status, sort);
            list = userAccountSetupRepository.findAllByStatus(status, pageable);
        }
        if (list.contains(qualification)) {
            basePojo.setStatus(true);
        } else {
            basePojo.setStatus(false);
        }
        List<UserAccountSetUpDTO> PojoList = UserMapper.MapEntityToPojo(list);
        for (UserAccountSetUpDTO userAccountSetUpDTO : PojoList) {
            if (userAccountSetUpDTO.isEmployeeflag() == true) {
                Employee employee = employeeRepository.findByEmployeeName(userAccountSetUpDTO.getFull_name());
//                userAccountSetUpDTO.setWaiterFlag(employee.isWaiterFlag());
//                userAccountSetUpDTO.setDeliveryFlag(employee.isDeliveryFlag());
            }
        }
        basePojo = calculatePagination(basePojo, PojoList.size());
        basePojo.setList(PojoList);
        return basePojo;
    }

    public String getcountrypagelist(String status, int pageNo, String searchText)throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Country> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "countryId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "Active";
            }
            Pageable pageable = new PageRequest(pageNo - 1, paginatedConstants, sort);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = countryRepository.findAllByCountryNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = countryRepository.findAllByStatus(status, pageable);
            }
//            List<CountryPojo> PojoList = CountryMapper.mapcountryEntityToPojo(list);
            List<Country> list1 = countryRepository.findAllByCountryNameContainingAndStatus(searchText, status);
            int size = list1.size() / paginatedConstants;
            int number = list1.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", list1);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }
        else{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "GDatk3");
            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(status, "Active")) {
                status = "1";
            } else {
                status = "0";
            }
            String url = readDomainName() + "/services/Litecall/CountryList?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }
    public String getPaginatedShiftList(String status, int pageNo, String searchText)throws Exception {
        if (StringUtils.pathEquals(buildType, "desktop")) {
            List<Shift> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "shiftId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo - 1, paginatedConstants, sort);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = shiftRepository.findAllByShiftNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = shiftRepository.findAllByStatus(status, pageable);
            }
            List<ShiftPojo> PojoList = ShiftMapper.mapShiftEntityToPojo(list);
            List<Shift> list1 = shiftRepository.findAllByShiftNameContainingAndStatus(searchText, status);
            int size = list1.size() / paginatedConstants;
            int number = list1.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            String url = readDomainName() + "/services/Litecall/shiftList?page=" + pageNo + "&status=" + status + "&searchText=" + searchText;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }
//
//    public BasePojo getPaginatedShiftList(String status, BasePojo basePojo, String searchText) {
//        List<Shift> list = new ArrayList<>();
//        basePojo.setPageSize(paginatedConstants);
//        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "shiftId"));
//        if (basePojo.isLastPage() == true) {
//            List<Shift> list1 = new ArrayList<>();
//            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
//                list1 = shiftRepository.findAllByShiftNameContainingAndStatus(searchText, status);
//            } else {
//                list1 = shiftRepository.findAllByStatus(status);
//            }
//            int size = list1.size() % paginatedConstants;
//            int pageNo = list1.size() / paginatedConstants;
//            if (size != 0) {
//                basePojo.setPageNo(pageNo);
//            } else {
//                basePojo.setPageNo(pageNo - 1);
//            }
//            sort = new Sort(new Sort.Order(Sort.Direction.DESC, "shiftId"));
//        }
//        if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
//            status = "Active";
//        }
//        Shift qualification = new Shift();
//        Pageable pageable = new PageRequest(basePojo.getPageNo(), basePojo.getPageSize(), sort);
//        if (basePojo.isNextPage() == true || basePojo.isFirstPage() == true) {
//            sort = new Sort(new Sort.Order(Sort.Direction.ASC, "shiftId"));
//        }
//        if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
//            qualification = shiftRepository.findFirstByShiftNameContainingAndStatus(searchText, status, sort);
//            list = shiftRepository.findAllByShiftNameContainingAndStatus(searchText, status, pageable);
//        } else {
//            qualification = shiftRepository.findFirstByStatus(status, sort);
//            list = shiftRepository.findAllByStatus(status, pageable);
//        }
//        if (list.contains(qualification)) {
//            basePojo.setStatus(true);
//        } else {
//            basePojo.setStatus(false);
//        }
//        List<ShiftPojo> PojoList = ShiftMapper.mapShiftEntityToPojo(list);
//        basePojo = calculatePagination(basePojo, PojoList.size());
//        basePojo.setList(PojoList);
//        return basePojo;
//    }

    public String getPaginationCustomerList(String status, int pageNo, String searchText)throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Customer> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "customerId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo - 1, paginatedConstants, sort);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = customerRepository.findAllByFirstNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = customerRepository.findAllByStatus(status, pageable);
            }
            List<CustomerPojo> PojoList = CustomerMapper.mapcustomerEntityToPojo(list);
            List<Customer> list1 = customerRepository.findAllByFirstNameContainingAndStatus(searchText, status);
            int size = list1.size() / paginatedConstants;
            int number = list1.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }
        else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Restaurant_id", "4XqbGS");
                jsonObject.put("location_connect_id", "4XqbGS49");
                String url = readDomainName() + "/services/Litecall/CustomerList?page=" + pageNo + "&status="+status + "&searchText="+searchText;
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                String jsonString = responseEntity.getBody();
                return jsonString;
            }
    }

    public String getPaginatedTableconfigList(String status,String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<TableConfig> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "tableconfigid"));

            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "Active";
            }
            TableConfig qualification = new TableConfig();
            Pageable pageable = new PageRequest(pageNo - 1, paginatedConstants, sort);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = tableConfigRepository.findAllByConfigurationnameContainingAndStatus(searchText, status, pageable);
            } else {
                list = tableConfigRepository.findAllByStatus(status, pageable);
            }
            List<TableConfigPojo> PojoList = TableConfigMapper.mapEntityToPojo(list);
            List<TableConfig> tableConfigList = tableConfigRepository.findAllByConfigurationnameContainingAndStatus(searchText, status);
            int size = tableConfigList.size() / paginatedConstants;
            int number = tableConfigList.size() % paginatedConstants;
            if(number>=1){
                size = size+1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson gson = new Gson();
            return gson.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            String url = readDomainName() + "/services/Litecall/zoneList?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }

    public String getpaginatedtable(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<TablesPos> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "tableid"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            TablesPos qualification = new TablesPos();
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = tablesPosRepository.findAllByTableNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = tablesPosRepository.findAllByStatus(status, pageable);
            }
            List<TablesPosPojo> PojoList = TablesPosMapper.mapEntityToPojo(list);
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", pageNo);
            Gson gson = new Gson();
            return gson.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            String url = readDomainName() + "/services/Litecall/tableList?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }

    public Boolean deleteCountry(CountryPojo details) {
        countryRepository.delete(details.getCountry_id());
        return true;
    }

    public Boolean deletePayment(PaymentMethodDTO details) {
        paymentMethodRepository.delete(details.getPaymentmethodId());
        return true;
    }

    public Boolean deleteEmployee(EmployeePojo details) {
//        employeeRepository.delete(details.getEmployeeId());
        return true;
    }

    public void deleteState(Long id) {
        stateRepository.delete(stateRepository.findAllByStateId(id));
    }
    public void deleteShift(Long id) {
        shiftRepository.delete(shiftRepository.findAllByShiftId(id));
    }
  public void DeleteVocher(Long id) {
        voucherRepository.delete(voucherRepository.findAllByCouponid(id));
    }

  public void deletecustomer(Long id) {
        customerRepository.delete(customerRepository.findAllByCustomerId(id));
    }

    public Boolean deleteTableConfig(TableConfigPojo details) {
        tableConfigRepository.delete(details.getId());
        return true;
    }

    public void deleteItem(Long id) {
        itemRespository.delete(itemRespository.findAllByItemId(id));
    }

    public void deleteAgent(Long id) {
        agentRepository.delete(agentRepository.findAllByAgentId(id));
    }

    public void deleteCategory(Long id) {
        categoryRepository.delete(categoryRepository.findAllByItemCategoryId(id));
    }

    public void deleteCurrency(Long id) {
        currencyRepository.delete(currencyRepository.findAllByCurrencyId(id));
    }
    @Transactional
    public String deleteTempTableData(String currTableName,String currTableId){
        RestaurantTempData restaurantTempData=restaurantTempDataRepository.findAllByTableNameAndTableId(currTableName,currTableId);
        if(restaurantTempData!=null){
            restaurantTempDataRepository.delete(restaurantTempData);
        }
        TablesPos tablesPos=tablesPosRepository.findAllByTableid(Long.parseLong(currTableId));
        tablesPos.setTableStatus("Empty");
        tablesPosRepository.save(tablesPos);
        return "success";
    }
    public Boolean deleteTable(TablesPosPojo details) {
        tablesPosRepository.delete(details.getTable_id());
        return true;
    }


//    public String getPaginatedState(String status, String searchText, int pageNo) throws Exception {
//        if (StringUtils.pathEquals(buildType, "desktop")) {
//            List<State> list = new ArrayList<>();
//            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "stateId"));
//            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
//                status = "Active";
//            }
//            State qualification = new State();
//            Pageable pageable = new PageRequest(pageNo - 1, paginatedConstants, sort);
//            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
//                qualification = stateRepository.findFirstByStateNameContainingAndStatus(searchText, status, sort);
//                list = stateRepository.findAllByStateNameContainingAndStatus(searchText, status, pageable);
//            } else {
//                qualification = stateRepository.findFirstByStatus(status, sort);
//                list = stateRepository.findAllByStatus(status, pageable);
//            }
//            List<StatePojo> PojoList = EmployeeMapper.mapEntityToPojo(list);
////            for (StatePojo statePojo : PojoList) {
////                Country country = countryRepository.findAllByCountryId(statePojo.getCountry_id());
////                statePojo.setCountryName(country.getCountryName());
////            }
//            List<State> stateList = stateRepository.findAll();
//            int size = stateList.size() / paginatedConstants;
//            int number = stateList.size() % paginatedConstants;
//            if (number >= 1) {
//                size = size + 1;
//            }
//            Map m = new HashMap<>();
//            m.put("data", PojoList);
//            m.put("no_Of_Paginations", size);
//            Gson gson = new Gson();
//            return gson.toJson(m);
//        } else {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("Restaurant_id", "GDatk3");
//            System.out.println(jsonObject);
//            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(status, "Active")) {
//                status = "1";
//            } else {
//                status = "0";
//            }
//            String url = readDomainName() + "/services/Litecall/stateList?page=" + pageNo + "&searchText=" + searchText + "&status=" + status;
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Type", "application/json");
//            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
//            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//            String jsonString = responseEntity.getBody();
//            return jsonString;
//        }
//    }
    public ResponseEntity getPrinterDeviceList() {
        final String uri = "http://localhost:9001/hiAccounts/getLocalPrinterList";
        if (buildType.equals("cloud")) {
            log.info("Request came from cloud");
            RestTemplate restTemplate = new RestTemplate();
            List<String> printerList = restTemplate.getForObject("http://localhost:9001/hiAccounts/getLocalPrinterList", List.class);
            return ResponseEntity.ok(printerList);
        } else { //Desktop
            log.info("Request came from desktop");
            List<String> printerNames = new ArrayList<>();
            printerNames.add("None");
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            for (PrintService ps : printServices) {
                printerNames.add(ps.getName());
            }
            return ResponseEntity.ok(printerNames);
        }
    }
    public String getPaginatedItemList(String status,  String searchText,int PageNo) throws Exception {

        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Item> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "itemId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(PageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = itemRespository.findAllByItemNameContainingAndItemStatus(searchText, status, pageable);
            } else {
                list = itemRespository.findAllByItemStatus(status, pageable);
            }

            List<ItemPojo> PojoList = ItemMapper.mapItemEntityToPojo(list);
            List<Item> itemList = itemRespository.findAllByItemNameContainingAndItemStatus(searchText,status);
            int size = itemList.size()/paginatedConstants;
            int number = itemList.size() % paginatedConstants;
            if(number>=1){
                size = size+1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }
       else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
//            if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(status,"Active")){
//                status ="1";
//            }else {
//                status="0";
//            }
            String url = readDomainName() + "/services/Litecall/Itemlist?page=" + PageNo+"&status="+status +"&searchText="+searchText;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }


    public String getPaginatedEmployeeList(String status, String searchText,int pageNo) throws Exception{
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Employee> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "employeeId"));

            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Employee qualification = new Employee();
            Pageable pageable = new PageRequest(pageNo - 1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = employeeRepository.findAllByEmployeeNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = employeeRepository.findAllByStatus(status, pageable);
            }
            List<EmployeePojo> PojoList = EmployeeMapper.mapEmpEntityToPojo(list);
            List<Employee> employeeList = employeeRepository.findAllByEmployeeNameContainingAndStatus(searchText, status);
            int size = employeeList.size() / paginatedConstants;
            int number = employeeList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson gson = new Gson();
            return gson.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/staffList?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }

    public String getPaginationStaffGroupList(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<StaffGroup> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "staffgroupid"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = staffGroupRepository.findAllByStaffgroupnameContainingAndStatus(searchText, status, pageable);
            } else {
                list = staffGroupRepository.findAllByStatus(status, pageable);
            }

            List<StaffGroupPojo> PojoList = StaffGroupMapper.mapStaffEntityToPojo(list);
            List<StaffGroup> staffGroupList = staffGroupRepository.findAllByStaffgroupnameContainingAndStatus(searchText, status);
            int size = staffGroupList.size() / paginatedConstants;
            int number = staffGroupList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            String url = readDomainName() + "/services/Litecall/staffGrouplist?page=" + pageNo + "&status="+status + "&searchText="+searchText;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }

        public String getPaginatedCategoryList(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Category> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "itemCategoryId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = categoryRepository.findAllByItemCategoryNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = categoryRepository.findAllByStatus(status, pageable);
            }

            List<CategoryPojo> PojoList = CategoryMapper.mapEntityToPojo(list);
            List<Category> categoryList = categoryRepository.findAllByItemCategoryNameContainingAndStatus(searchText, status);
            int size = categoryList.size() / paginatedConstants;
            int number = categoryList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            String url = readDomainName() + "/services/Litecall/categorylist?page=" + pageNo + "&status="+status + "&searchText="+searchText;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }
    public String getPaginatedDiscountList(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Discount> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "Active";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = discountRepository.findAllByMenuidContainingAndStatus(searchText, status, pageable);
            } else {
                list = discountRepository.findAllByStatus(status, pageable);
            }

            List<DiscountPojo> PojoList = DiscountMapper.mapdiscountEntityToPojo(list);
            List<Discount> discountList = discountRepository.findAllByMenuidContainingAndStatus(searchText, status);
            int size = discountList.size() / paginatedConstants;
            int number = discountList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "GDatk3");
            jsonObject.put("location_connect_id", "GDatk3");
            System.out.println(jsonObject);
            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(status, "Active")) {
                status="1";
            } else {
                status="0";
            }
            String url = readDomainName() + "/services/Litecall/itemwisediscountlist?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }

    public String getPaginatedState(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<State> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "stateId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = stateRepository.findAllByStateNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = stateRepository.findAllByStatus(status, pageable);
            }

            List<StatePojo> PojoList = StateMapper.mapStateEntityToPojo(list);
            for (StatePojo statePojo : PojoList) {
                if(statePojo.getCountry_id()!=null) {
                    Country country = countryRepository.findAllByCountryId(statePojo.getCountry_id());
                    statePojo.setCountry_name(country.getCountryName());
                }
            }
            List<State> stateList = stateRepository.findAllByStateNameContainingAndStatus(searchText, status);
            int size = stateList.size() / paginatedConstants;
            int number = stateList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/statelist?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }

    public String getPaginatedVoucherList(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<PaymentVoucher> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "couponid"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = voucherRepository.findAllByCodeContainingAndStatus(searchText, status, pageable);
            } else {
                list = voucherRepository.findAllByStatus(status, pageable);
            }

            List<PaymentVoucherPojo> PojoList = VoucherMapper.mapEntityToPojo(list);
            List<PaymentVoucher> paymentVoucherList = voucherRepository.findAllByCodeContainingAndStatus(searchText, status);
            int size = paymentVoucherList.size() / paginatedConstants;
            int number = paymentVoucherList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/paymentvoucherlist?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }



    public String getPaginatedCityList(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<City> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "Active";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = cityRepository.findAllByNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = cityRepository.findAllByStatus(status, pageable);
            }

            List<CityPojo> PojoList = CityMapper.MapCityEntityToPojo(list);
            for (CityPojo cityPojo : PojoList) {
                Country country = countryRepository.findAllByCountryId(cityPojo.getCountry_id());
                cityPojo.setCountry_name(country.getCountryName());
                State state = stateRepository.findAllByStateId(cityPojo.getState_id());
                cityPojo.setState_name(state.getStateName());
            }
            List<City> cityList = cityRepository.findAllByNameContainingAndStatus(searchText, status);
            int size = cityList.size() / paginatedConstants;
            int number = cityList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/citylist?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }



    public String getPaginatedAgentList(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Agent> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "agentId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "Active";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = agentRepository.findAllByAgentNameContainingAndStatus(searchText, status, pageable);
            }
            else {
                list = agentRepository.findAllByStatus(status, pageable);
            }

            List<AgentPojo> PojoList = AgentMapper.mapAgentEntityToPojo(list);
            List<Agent> agentList = agentRepository.findAllByAgentNameContainingAndStatus(searchText, status);
            int size = agentList.size() / paginatedConstants;
            int number = agentList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/agentlist?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }


    public String getcountrypagelist(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Country> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "countryId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = countryRepository.findAllByCountryNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = countryRepository.findAllByStatus(status, pageable);
            }

            List<CountryPojo> PojoList = CountryMapper.MapcountryEntityToPojo(list);
            List<Country> countryList = countryRepository.findAllByCountryNameContainingAndStatus(searchText, status);
            int size = countryList.size() / paginatedConstants;
            int number = countryList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
//            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(status, "Active")) {
//                status="1";
//            } else {
//                status="0";
//            }
            String url = readDomainName() + "/services/Litecall/countrylist?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }


    public String getPaginatedDeliveryAreaList(String status, String searchText,int pageNo) throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<DeliveryArea> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants, sort);

            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = deliveryAreaRepository.findAllByNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = deliveryAreaRepository.findAllByStatus(status, pageable);
            }

            List<DeliveryAreaPojo> PojoList = DeliveryMapper.mapEntityToPojo(list);
            List<DeliveryArea> deliveryAreaList = deliveryAreaRepository.findByNameContainingAndStatus(searchText, status);
            int size = deliveryAreaList.size() / paginatedConstants;
            int number = deliveryAreaList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/AreaList?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }
//    public BasePojo getpaginatedOnlineOrders(BasePojo basePojo) {
//        List<CustomerNotifications> list = new ArrayList<>();
//        basePojo.setPageSize(paginatedConstants);
//        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "custNotiId"));
//        if (basePojo.isLastPage() == true) {
//            List<CustomerNotifications> list1 = new ArrayList<>();
//            list1 = customerNotificationRepository.findAllByStatus("placed");
//            int size = list1.size() % paginatedConstants;
//            int pageNo = list1.size() / paginatedConstants;
//            if (size != 0) {
//                basePojo.setPageNo(pageNo);
//            } else {
//                basePojo.setPageNo(pageNo - 1);
//            }
//            sort = new Sort(new Sort.Order(Sort.Direction.DESC, "custNotiId"));
//        }
//
//        CustomerNotifications customerNotifications = new CustomerNotifications();
//        Pageable pageable = new PageRequest(basePojo.getPageNo(), basePojo.getPageSize(), sort);
//        if (basePojo.isNextPage() == true || basePojo.isFirstPage() == true) {
//            sort = new Sort(new Sort.Order(Sort.Direction.ASC, "custNotiId"));
//        }
//        customerNotifications = customerNotificationRepository.findFirstByStatus("placed", sort);
//            list = customerNotificationRepository.findAllByStatus("placed", pageable);
//
//        if (list.contains(customerNotifications)) {
//            basePojo.setStatus(true);
//        } else {
//            basePojo.setStatus(false);
//        }
//        List<CustomerNotificationsPojo> PojoList = CustomerNotificationMapper.mapEntityToPojo(list);
//        basePojo = calculatePagination(basePojo, PojoList.size());
//        basePojo.setList(PojoList);
//        return basePojo;
//    }

    public String getPaginatedCurrencyList(String status, String searchText,int pageNo)throws Exception {
        if(StringUtils.pathEquals(buildType,"desktop")) {
            List<Currency> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "currencyId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "Active";
            }
            Currency qualification = new Currency();
            Pageable pageable = new PageRequest(pageNo-1, paginatedConstants,sort);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = currencyRepository.findAllByCurrencyNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = currencyRepository.findAllByStatus(status, pageable);
            }
            List<CurrencyPojo> PojoList = CurrecncyMapper.mapEntityToPojo(list);
            for (CurrencyPojo currencyPojo : PojoList) {
                Country country = countryRepository.findAllByCountryId(currencyPojo.getCountry_id());
                currencyPojo.setCountry_name(country.getCountryName());
            }
            List<Currency> currencyList = currencyRepository.findAllByCurrencyNameContainingAndStatus(searchText, status);
            int size = currencyList.size() / paginatedConstants;
            int number = currencyList.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap<>();
            m.put("data", PojoList);
            m.put("no_Of_Paginations", size);
            Gson gson = new Gson();
            return gson.toJson(m);
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            System.out.println(jsonObject);
            String url = readDomainName() + "/services/Litecall/currenciesList?page=" + pageNo+"&searchText=" +searchText+"&status="+status;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }

    public static String readDomainNameRestoOrder() throws IOException {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = HiposService.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(in);
            in.close();
        } catch (Exception e) {
        } finally {
            in.close();
        }
        return prop.getProperty("resto_order_domain");
    }
    public static String readDomainNameConsumerId() throws IOException {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = HiposService.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(in);
            in.close();
        } catch (Exception e) {
        } finally {
            in.close();
        }
        return prop.getProperty("spring.kafka.consumer.group-id");
    }
    public static String readDomainNameKafkaUrl() throws IOException {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = HiposService.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(in);
            in.close();
        } catch (Exception e) {
        } finally {
            in.close();
        }
        return prop.getProperty("spring.kafka.bootstrap-servers");
    }

    public List<TablesPosPojo> synchronizeTable(List<TablesPosPojo> tablesPosDTOList) throws Exception {
        List<Map<String, String>> mapList = new ArrayList<>();
        Company company = companyRepository.findAllByStatus("Active");
        JSONObject jsonObject = new JSONObject();
        for (TablesPosPojo tablesPosDTO : tablesPosDTOList){
            Map<String, String> map = new HashMap<>();
            map.put("table_name", tablesPosDTO.getTable_name());
//            map.put("min_capacity", tablesPosDTO.get());
//            map.put("max_capacity", tablesPosDTO.getMaxCapacity());
            map.put("ref_id", String.valueOf(tablesPosDTO.getTable_id()));
            mapList.add(map);
        }
        jsonObject.put("store_id", company.getConnectNo());
        jsonObject.put("tablelist", mapList);
        System.out.println(jsonObject);
        String url = readDomainNameRestoOrder() + "/services/Account/tableSync";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String jsonString = responseEntity.getBody();
        return tablesPosDTOList;
    }

    @Transactional
    public Item getItemBasedOnItemCode(Long itemCode) {
        Item item = null;
        try {
            item = itemRespository.findAllByItemCode(itemCode);

        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return item;
    }

    public String updateItemsinLocation(List<String> itemIds, List<String> locations, String status, String locationId) throws Exception {
        ResponseEntity<String> responseEntity = null;
        String result = null;
        Gson gson = new Gson();
        Company company = companyRepository.findAllByStatus("Active");
//        cartMaster cartMaster = hiposDAO.getCartMasterByName("UrbanPiper",locationId);
        org.codehaus.jackson.map.ObjectMapper zomatoMapper = new org.codehaus.jackson.map.ObjectMapper();
        String url = readDomainNameDashboard() + "/dashdoard/updateItemsinLocations";
        ObjectNode node = zomatoMapper.createObjectNode();
        node.put("location_ref_id", company.getConnectNo());
        node.put("action", status);
        node.putPOJO("platforms", gson.toJson(locations));
        node.putPOJO("item_ref_ids", gson.toJson(itemIds));
        try {
            RestTemplate zomatoRestTemplate = new RestTemplate();
            HttpHeaders zomatoHeaders = new HttpHeaders();
            HttpEntity<String> zomatoEntity = new HttpEntity<String>(node.toString(), zomatoHeaders);
            responseEntity = zomatoRestTemplate.exchange(url, HttpMethod.POST, zomatoEntity, String.class);
            result = responseEntity.getStatusCode().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (String itemCode : itemIds) {
//            Item item = HiposService.getItemBasedOnItemCode(itemCode);
//            if (item != null) {
//                java.lang.reflect.Type type = new TypeToken<ArrayList<AddCartMaster>>() {
//                }.getType();
//                if (!org.apache.commons.lang3.StringUtils.isEmpty(item.getCartValue())) {
//                    List<AddCartMaster> cartMasters = gson.fromJson(item.getCartValue(), type);
//                    for (AddCartMaster addCartMaster : cartMasters) {
//                        if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(addCartMaster.getCartName(), locations.get(0))) {
//                            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(status, "enable")) {
//                                addCartMaster.setStatus("true");
//                            } else {
//                                addCartMaster.setStatus("false");
//                            }
//                        }
//                    }
//                    item.setCartValue(gson.toJson(cartMasters));
//                    HiposService.saveOrUpdate(item);
//                }
//            }
//
//        }
        return result;
    }

    public static String readDomainNameDashboard() throws IOException {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = HiposService.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(in);
            in.close();
        } catch (Exception e) {
        } finally {
            in.close();
        }
        return prop.getProperty("pkt_domain");
    }
    public static String readDomainNameIdm() throws IOException {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = HiposService.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(in);
            in.close();
        } catch (Exception e) {
        } finally {
            in.close();
        }
        return prop.getProperty("hisaas_domainame");
    }

    public String addNewItem(List<UrbanPiperItemPojo> itemPojos, List<ItemPojo> items) throws Exception {
        Gson gson = new Gson();
        Company company = companyRepository.findAllByStatus("Active");
        ResponseEntity<String> responseEntity = null;
        String res = null;
        String zomatoUrl = readDomainNameDashboard() + "/dashdoard/updateItems";
        org.codehaus.jackson.map.ObjectMapper zomatoMapper = new org.codehaus.jackson.map.ObjectMapper();
        ObjectNode zomatoObjectNode = zomatoMapper.createObjectNode();
        ObjectNode result = zomatoMapper.createObjectNode();
        zomatoObjectNode.putPOJO("items", gson.toJson(itemPojos));
        zomatoObjectNode.put("callback_url", "");
//        result.putPOJO("zomatoObjectNode", zomatoObjectNode);
        zomatoObjectNode.put("restaurantId", company.getConnectNo());
        try {
            RestTemplate zomatoRestTemplate = new RestTemplate();
            HttpHeaders zomatoHeaders = new HttpHeaders();
            HttpEntity<String> zomatoEntity = new HttpEntity<String>(zomatoObjectNode.toString(), zomatoHeaders);
            responseEntity = zomatoRestTemplate.exchange(zomatoUrl, HttpMethod.POST, zomatoEntity, String.class);
            res = responseEntity.getStatusCode().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Transactional
//    public List<ItemPojo> synchronizeItem(List<ItemPojo> itemPojoList) throws Exception {
//        List<UrbanPiperItemPojo> itemPojos = new ArrayList<>();
//        List<ItemPojo> items = new ArrayList<>();
//        for (ItemPojo itemObj : itemPojoList) {
//            List<String> stringList = new ArrayList<>();
//            UrbanPiperItemPojo urbanPiperItemPojo = new UrbanPiperItemPojo();
//            urbanPiperItemPojo.setRef_id(itemObj.getItemCode());
//            urbanPiperItemPojo.setTitle(itemObj.getItemName());
//            urbanPiperItemPojo.setSold_at_store(true);
//            urbanPiperItemPojo.setAvailable(true);
//            Item item=itemRespository.findAllByItemName(itemObj.getItemName());
//            urbanPiperItemPojo.setDescription(itemObj.getItemDesc());
//            urbanPiperItemPojo.setCurrent_stock(-1);
//            urbanPiperItemPojo.setFood_type(itemObj.getFoodtype());
//            stringList.add(itemObj.getItemCategoryId().toString());
//            urbanPiperItemPojo.setCategory_ref_ids(stringList);
//            urbanPiperItemPojo.setSort_order(1L);
//            urbanPiperItemPojo.setRecommended(true);
//            urbanPiperItemPojo.setPrice((float)item.getItemPrice());
//            urbanPiperItemPojo.setImg_url("");
//            itemPojos.add(urbanPiperItemPojo);
//            items.add(itemObj);
//        }
//        addNewItem(itemPojos, items);
//        return itemPojoList;
//    }

    public String addCategoryUrbanPiper(List<UrbanPiperCategoryPojo> urbanPiperCategoryPojos) throws Exception {
        Gson gson = new Gson();
        Company company = companyRepository.findAllByStatus("Active");
        ResponseEntity<String> responseEntity = null;
        String res = null;
        String zomatoUrl = readDomainNameDashboard() + "/dashdoard/updateCategories";
        org.codehaus.jackson.map.ObjectMapper zomatoMapper = new org.codehaus.jackson.map.ObjectMapper();
        ObjectNode result = zomatoMapper.createObjectNode();
        result.putPOJO("categories", gson.toJson(urbanPiperCategoryPojos));
        result.put("restaurantId", company.getConnectNo());
        try {
            RestTemplate zomatoRestTemplate = new RestTemplate();
            HttpHeaders zomatoHeaders = new HttpHeaders();
            HttpEntity<String> zomatoEntity = new HttpEntity<String>(result.toString(), zomatoHeaders);
            responseEntity = zomatoRestTemplate.exchange(zomatoUrl, HttpMethod.POST, zomatoEntity, String.class);
            res = responseEntity.getStatusCode().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<CategoryPojo> SynchronizeCategory(List<CategoryPojo> categoryPojoList) throws Exception {
        List<UrbanPiperCategoryPojo> urbanPiperCategoryPojoList = new ArrayList<>();
        for (CategoryPojo itemCategoryDTOS : categoryPojoList) {
            UrbanPiperCategoryPojo urbanPiperCategoryPojo = new UrbanPiperCategoryPojo();
            urbanPiperCategoryPojo.setRef_id(itemCategoryDTOS.getCategory_id().toString());
            urbanPiperCategoryPojo.setName(itemCategoryDTOS.getName());
            if(!StringUtils.isEmpty(itemCategoryDTOS.getDescription())) {
                urbanPiperCategoryPojo.setDescription(itemCategoryDTOS.getDescription());
            }else {
                urbanPiperCategoryPojo.setDescription("");
            }
            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(itemCategoryDTOS.getStatus(), "Active")) {
                urbanPiperCategoryPojo.setActive(true);
            } else {
                urbanPiperCategoryPojo.setActive(false);
            }
            urbanPiperCategoryPojo.setSort_order(1L);
            urbanPiperCategoryPojo.setImg_url("");
            urbanPiperCategoryPojoList.add(urbanPiperCategoryPojo);
        }
        addCategoryUrbanPiper(urbanPiperCategoryPojoList);
        return categoryPojoList;
    }

    @javax.transaction.Transactional
    public List<Map> gettop5Items(Date fromDate, Date toDate) {
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(fromDate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
        cal.set(Calendar.MINUTE, 0); // set minute in hour
        cal.set(Calendar.SECOND, 0); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroedDate = cal.getTime();
        cal.setTime(toDate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 23); // set hour to midnight
        cal.set(Calendar.MINUTE, 59); // set minute in hour
        cal.set(Calendar.SECOND, 59); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroToDate = cal.getTime();
        List<Map> responsePojos = new ArrayList<>();
        responsePojos = salesInvoiceDetailsRepository.findForItems(zeroedDate, zeroToDate);
        return responsePojos;

    }

    public DashBoardDto getRunningSales() {
        List<RestaurantTempData> restaurantTempDataList = new ArrayList<>();
        restaurantTempDataList = restaurantTempDataRepository.findAll();
        double totalAmt = 0, taxAmt = 0, discountAmt = 0;
        DashBoardDto dashBoardDto = new DashBoardDto();
        for (RestaurantTempData restaurantTempData : restaurantTempDataList) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<List<SelectedItem>>() {
            }.getType();
            List<SelectedItem> selectedItems = gson.fromJson(restaurantTempData.getSelectedItemsList(), type);
            if (selectedItems != null)
                for (SelectedItem selectedItem : selectedItems) {
                    totalAmt = totalAmt + selectedItem.getAmtexclusivetax();
                    taxAmt = taxAmt + selectedItem.getTaxamt();
                    discountAmt = discountAmt + selectedItem.getDiscountAmt();
                }
        }
        dashBoardDto.setAmount(totalAmt);
        dashBoardDto.setDiscount(discountAmt);
        dashBoardDto.setTaxamount(taxAmt);
        return dashBoardDto;

    }

    @javax.transaction.Transactional
    public Map<String, Double> shiftSales(Date startDate, Date toDate) {
        List<Shift> shiftList = new ArrayList<>();
        Map<String, Double> result = new HashMap<>();
        shiftList = shiftRepository.findAllByStatus("Active");
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(startDate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
        cal.set(Calendar.MINUTE, 0); // set minute in hour
        cal.set(Calendar.SECOND, 0); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroedDate = cal.getTime();
        cal.setTime(toDate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 23); // set hour to midnight
        cal.set(Calendar.MINUTE, 59); // set minute in hour
        cal.set(Calendar.SECOND, 59); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroToDate = cal.getTime();
        for (Shift shift : shiftList) {
            List<String> stringList = new ArrayList<>();
            String[] times = null;
            if (StringUtils.pathEquals(shift.getFromTime(), "00:00")) {
                times = "24:00".split(":");
            } else {
                times = shift.getFromTime().split(":");
            }
            String[] times1 = null;
            if (StringUtils.pathEquals(shift.getToTime(), "00:00")) {
                times1 = "24:00".split(":");
            } else {
                times1 = shift.getToTime().split(":");
            }
            List<java.sql.Time> intervals = new ArrayList<>(25);
            java.sql.Time startTime = new java.sql.Time(Integer.parseInt(times[0]), Integer.parseInt(times[1]), 0);
            java.sql.Time endTime = new java.sql.Time(Integer.parseInt(times1[0]), Integer.parseInt(times1[1]), 0);
            intervals.add(startTime);
            cal.setTime(startTime);
            while (cal.getTime().before(endTime)) {
                cal.add(Calendar.MINUTE, 1);
                intervals.add(new java.sql.Time(cal.getTimeInMillis()));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            for (java.sql.Time time : intervals) {
                stringList.add(sdf.format(time));
            }
            List<Map> restaurantTokenRecords = new ArrayList<>();
            restaurantTokenRecords = restaurantTokenRecordRepository.findByOrderBy(stringList, zeroedDate, zeroToDate);
            double amt = restaurantTokenRecords.stream().mapToDouble(o -> Double.parseDouble(o.get("totalAmt").toString())).sum();
            result.put(shift.getShiftName(), amt);
        }
        return result;
    }

    public List<Map> getOrderType(Date fromdate, Date todate) {
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(fromdate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
        cal.set(Calendar.MINUTE, 0); // set minute in hour
        cal.set(Calendar.SECOND, 0); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroedDate = cal.getTime();
        cal.setTime(todate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 23); // set hour to midnight
        cal.set(Calendar.MINUTE, 59); // set minute in hour
        cal.set(Calendar.SECOND, 59); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroToDate = cal.getTime();
        List<Map> restaurantTokenRecordDtos = salesInvoiceRepository.findByinvoice(zeroedDate, zeroToDate);
        return restaurantTokenRecordDtos;

    }

    @javax.transaction.Transactional
    public Map getPaymentTypeTotal(Date fromDate, Date toDate) {
        Map result = new HashMap();
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(fromDate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
        cal.set(Calendar.MINUTE, 0); // set minute in hour
        cal.set(Calendar.SECOND, 0); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroedDate = cal.getTime();
        cal.setTime(toDate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 23); // set hour to midnight
        cal.set(Calendar.MINUTE, 59); // set minute in hour
        cal.set(Calendar.SECOND, 59); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroToDate = cal.getTime();
        List<String> invoiceList = restaurantTokenRecordRepository.findByDate(zeroedDate, zeroToDate);
        if (invoiceList.isEmpty())
            return result;
        List<PosPaymentTypes> posPaymentTypes = posPaymentTypesRepository.findByInvoice(invoiceList);
        double cashAmt = posPaymentTypes.stream().mapToDouble(o -> o.getTotalCashPayment()).sum();
        double discountAmt = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getTotalDiscountAmount()).sum();
        List<MultiBankPayment> multiBankPaymentList = new ArrayList<>();
        List<MultiCardPayment> multiCardPaymentList = new ArrayList<>();
        List<MultiVoucherPayment> multiVoucherPaymentList = new ArrayList<>();
        Gson json = new Gson();
        java.lang.reflect.Type voucherPayment = new TypeToken<ArrayList<MultiVoucherPayment>>() {
        }.getType();
        java.lang.reflect.Type cardPayment = new TypeToken<ArrayList<MultiCardPayment>>() {
        }.getType();
        java.lang.reflect.Type bankPayment = new TypeToken<ArrayList<MultiBankPayment>>() {
        }.getType();
        for (PosPaymentTypes posPaymentTypes1 : posPaymentTypes) {
            multiBankPaymentList.addAll(json.fromJson(posPaymentTypes1.getBankPayment(), bankPayment));
            multiCardPaymentList.addAll(json.fromJson(posPaymentTypes1.getCardPayment(), cardPayment));
            multiVoucherPaymentList.addAll(json.fromJson(posPaymentTypes1.getVoucherPayment(), voucherPayment));
        }
        for (MultiVoucherPayment multiVoucherPayment : multiVoucherPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiVoucherPayment.getVoucherAmt());
            multiBankPayment.setPaymentType(multiVoucherPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiCardPayment multiCardPayment : multiCardPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiCardPayment.getCardAmt());
            multiBankPayment.setPaymentType(multiCardPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiBankPayment multiBankPayment : multiBankPaymentList) {
            if (!org.springframework.util.StringUtils.isEmpty(multiBankPayment.getPaymentType())) {
                PaymentMethod paymentMethod = paymentMethodRepository.findOne(Long.parseLong(multiBankPayment.getPaymentType()));
                multiBankPayment.setPaymentType(paymentMethod.getPaymentmethodName());
            }
        }
        Map<String, Double> resultMap = multiBankPaymentList.stream().filter(c -> c.getPaymentType() != null).collect(
                Collectors.groupingBy(MultiBankPayment::getPaymentType, Collectors.summingDouble(MultiBankPayment::getAmount)));
        resultMap.put("Cash", cashAmt);
        result.put("Discount", discountAmt);
        if (resultMap.get("Discount Voucher") == null) {
            resultMap.put("Discount Voucher", 0D);
        }
        return resultMap;

    }

    public DashBoardDto restdashboard(Date fromdate, Date todate)throws Exception {
        DashBoardDto dashBoardDto = new DashBoardDto();
        List<Map> salesInvoices = salesInvoiceRepository.findBy(fromdate, todate);
        double taxamount = salesInvoices.stream().mapToDouble(o -> Double.parseDouble(o.get("salesTotalTaxAmt").toString())).sum();
        double amount = salesInvoices.stream().mapToDouble(o -> Double.parseDouble(o.get("totalAmount").toString())).sum();
        double discount = salesInvoices.stream().mapToDouble(o -> Double.parseDouble(o.get("totalDiscountAmount").toString())).sum();
        dashBoardDto.setTaxamount(taxamount);
        dashBoardDto.setAmount(amount - taxamount);
        dashBoardDto.setDiscount(discount);
        return dashBoardDto;
    }


    public Company SaveDetails(CompanyDto companyDto){
        Company company = new Company();
        byte byteArray[];
        String fileName = FileSystemOperations.getImagesDirItem() + File.separator + companyDto.getCompanyName() + ".png";
        List<Company> list = companyRepository.findAll();
        if (list.size() > 0) {
            companyDto.setCompanyId(list.get(0).getCompanyId());
        }
        if (!org.apache.commons.lang3.StringUtils.isEmpty(companyDto.getLogo()) && companyDto.getLogo().contains("data:image")) {
            try {
                FileOutputStream fos = new FileOutputStream(fileName);
                byteArray = org.apache.commons.codec.binary.Base64.decodeBase64(companyDto.getLogo().split(",")[1]);
                fos.write(byteArray);
                fos.flush();
                fos.close();
                companyDto.setLogo(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (companyDto.getCompanyId() != null) {
            Company settings = companyRepository.findByCompanyId(companyDto.getCompanyId());
            companyDto.setLogo(settings.getLogo());
        }
        company= CountryMapper.MapCompanyPojoToEntity(companyDto);
        company.setLogo(companyDto.getLogo());
        companyRepository.save(company);
            return company;
    }

    public Company getCompanyList() {
        Company general = null;
        general = companyRepository.findAllByStatus("Active");
        if (general != null) {
            if (!org.apache.commons.lang3.StringUtils.isEmpty(general.getLogo())) {
                if (!general.getLogo().equalsIgnoreCase("")) {
                    String imageLocation = FileSystemOperations.getImagesDirItem() + File.separator + general.getCompanyName() + ".png";

                    String fileDirectory = File.separator;
                    if (fileDirectory.equals("\\"))//Windows OS
                        imageLocation = imageLocation.substring(imageLocation.indexOf("\\image")).replaceAll("\\\\", "/");
                    else//Linux or MAC
                        imageLocation = imageLocation.substring(imageLocation.indexOf("/image"));
                    general.setLogo(imageLocation);
                }
            }
        }
        return general;
    }

    public void syncOrders()throws Exception{
        ResponseEntity<String> responseEntity = null;
        Company company = companyRepository.findAllByStatus("Active");
        String URL=readDomainNameRestoOrder()+"/services/Account/getOrders?connect_id="+company.getConnectNo();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>( headers);
        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        String jsonstring = responseEntity.getBody();
        Gson gson=new Gson();
        JSONObject jsonObject = new JSONObject(jsonstring);
        Type type=new TypeToken<List<OrderSync>>(){}.getType();
        List<OrderSync> ordersList=new ArrayList<>();
        if(StringUtils.pathEquals(jsonObject.get("status").toString(),"success")) {
            ordersList = gson.fromJson(jsonObject.get("data").toString(), type);
            for (OrderSync m : ordersList) {
                RetailDTO retailDTO = new RetailDTO();
                Customer customer = customerRepository.findByFirstName(m.getFirst_name());
                System.out.println(m.getFirst_name());
                retailDTO.setCutomerName(customer.getFirstName());
                retailDTO.setCustomerId(customer.getCustomerId());
                retailDTO.setOrderType(m.getOrder_type());
                retailDTO.setTotalCheckOutamt(m.getOrder_total());
                retailDTO.setTotalTenderedAmount(m.getOrder_total());
                retailDTO.setCustomerEmail(m.getEmail());
                retailDTO.setSiStatus("Prepared");
                retailDTO.setPaymentType("multiPayment");
                CashPayment cashPayment = new CashPayment();
                MultiCashPayment multiCashPayment = new MultiCashPayment();
                List<MultiCashPayment> multiCashPayments = new ArrayList<>();
                multiCashPayment.setCashAmt(m.getOrder_total());
                multiCashPayment.setPaymentType(1L);
                multiCashPayments.add(multiCashPayment);
                cashPayment.setMultiCashPaymentList(multiCashPayments);
                retailDTO.setCashPayment(cashPayment);
//            TablesPos tablesPos = tablesPosRepository.findAllByTableid(Long.parseLong(m.get("table_id")));
//            retailDTO.setTable(tablesPos.getTableName());
                List<SelectedItem> selectedItems = new ArrayList<>();
                for (SelectedItemsSync m1 : m.getSelectedItem()) {
                    SelectedItem selectedItem = new SelectedItem();
                    Item item = itemRespository.findAllByItemName(m1.getName());
                    System.out.println(m1.getName());
                    selectedItem.setItemName(m1.getName());
                    selectedItem.setUnitPrice(m1.getPrice());
                    selectedItem.setUnitPriceIn(m1.getPrice());
                    selectedItem.setAmtexclusivetax(m1.getSubtotal());
                    selectedItem.setAmtinclusivetax(m1.getSubtotal());
                    selectedItem.setItemId(item.getItemId());
                    selectedItem.setItemCode(item.getItemCode());
                    selectedItem.setQty(m1.getQuantity());
                    selectedItems.add(selectedItem);
                    retailDTO.setSelectedItemsList(selectedItems);
                }
                createInoviceNPayment(retailDTO, null, "", null, null);
            }
        }
    }

//    public void syncTables() throws Exception {
//        ResponseEntity<String> responseEntity = null;
//        Company company = companyRepository.findAllByStatus("Active");
//        String URL = readDomainNameRestoOrder() + "/services/Account/tableDetails?connect_id=" + company.getConnectNo();
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        HttpEntity<String> entity = new HttpEntity<String>(headers);
//        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
//        String jsonString = responseEntity.getBody();
//        Gson gson = new Gson();
//        JSONObject jsonObject = new JSONObject(jsonString);
//        Type type1 = new TypeToken<List<Map<String, String>>>() {
//        }.getType();
//        if (StringUtils.pathEquals(jsonObject.get("status").toString(), "success")) {
//            List<Map<String, String>> tableslist = new ArrayList<>();
//            tableslist = gson.fromJson(jsonObject.get("data").toString(), type1);
//            for (Map<String, String> m : tableslist) {
//                TablesPos tablesPos = tablesPosRepository.findAllByTableName(m.get("table_name"));
//                TablesPosPojo tablesPosPojo = new TablesPosPojo();
//                if (tablesPos != null) {
//                    tablesPosPojo.setTable_id(tablesPos.getTableid());
//                }
//                tablesPosPojo.setTable_name(m.get("table_name"));
////                tablesPosPojo.set(m.get("min_capacity"));
////                tablesPosPojo.setMaxCapacity(m.get("max_capacity"));
//                tablesPosPojo.setZone_id(m.get("zone"));
//                tablesPosPojo.setTable_status("Active");
//                saveTablesPos(tablesPosPojo);
//
//            }
//        }
//    }

    public void syncEmployee() throws Exception {
        ResponseEntity<String> responseEntity = null;
        Company company = companyRepository.findAllByStatus("Active");
        String URL = readDomainNameRestoOrder() + "/services/Account/getStaffDetails?connect_id=" + company.getConnectNo();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        String jsonString = responseEntity.getBody();
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(jsonString);
        Type type1 = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        if (StringUtils.pathEquals(jsonObject.get("status").toString(), "success")) {
            List<Map<String, String>> employeeList = new ArrayList<>();
            employeeList = gson.fromJson(jsonObject.get("data").toString(), type1);
            for (Map<String, String> m : employeeList) {
                Employee employee = employeeRepository.findByEmployeeName(m.get("staff_name"));
                EmployeePojo employeePojo = new EmployeePojo();
                if (employee != null) {
//                    employeePojo.setEmployeeId(employee.getEmployeeId());
                }
//                employeePojo.setEmployeeName(m.get("staff_name"));
//                employeePojo.setStatus("Active");
//                employeePojo.setWaiterFlag(true);
                saveEmployee(employeePojo);

            }
        }
    }
    public void syncCustomers() throws Exception {
        ResponseEntity<String> responseEntity = null;
        Company company = companyRepository.findAllByStatus("Active");
        String URL = readDomainNameRestoOrder() + "/services/Account/getcustomer?connect_id=" + company.getConnectNo();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        String jsonString = responseEntity.getBody();
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(jsonString);
        Type type1 = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        if (StringUtils.pathEquals(jsonObject.get("status").toString(), "success")) {
            List<Map<String, String>> customerList = new ArrayList<>();
            customerList = gson.fromJson(jsonObject.get("data").toString(), type1);
            for (Map<String, String> m : customerList) {
                Customer customer = customerRepository.findAllByCustomerId(Long.parseLong(m.get("customer_id")));
                CustomerPojo customerPojo = new CustomerPojo();
                if (customer != null) {
                    customerPojo.setCustomer_id(customer.getCustomerId());
                }
                customerPojo.setFirst_name(m.get("first_name"));
                customerPojo.setEmail(m.get("email"));
                customerPojo.setAddress_1(m.get("address_1"));
                customerPojo.setStatus("Active");
                saveCustomer(customerPojo);
            }
        }
    }
    public void syncTableConfig() throws Exception {
        ResponseEntity<String> responseEntity = null;
        Company company = companyRepository.findAllByStatus("Active");
        String URL = readDomainNameRestoOrder() + "/services/Account/getzone?connect_id=" + company.getConnectNo();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        String jsonString = responseEntity.getBody();
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(jsonString);
        Type type1 = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        if (StringUtils.pathEquals(jsonObject.get("status").toString(), "success")) {
            List<Map<String, String>> zoneList = new ArrayList<>();
            zoneList = gson.fromJson(jsonObject.get("data").toString(), type1);
            for (Map<String, String> m : zoneList) {
                TableConfig tableConfig = tableConfigRepository.findAllByTableconfigid(Long.parseLong(m.get("id")));
                TableConfigPojo tableConfigPojo = new TableConfigPojo();
                if (tableConfig != null) {
                    tableConfigPojo.setId(tableConfig.getTableconfigid());
                }
                tableConfigPojo.setZone_name(m.get("zone_name"));
                tableConfigPojo.setNo_of_rows(Integer.parseInt(m.get("no_of_rows")));
                tableConfigPojo.setNo_of_col(Integer.parseInt(m.get("no_of_col")));
                tableConfigPojo.setStatus("Active");
                saveTableConfig(tableConfigPojo);
            }
        }
    }
    public void syncPaymentVoucherDetails() throws Exception {
        ResponseEntity<String> responseEntity = null;
        Company company = companyRepository.findAllByStatus("Active");
        String URL = readDomainNameRestoOrder() + "/services/Account/getPaymentvoucherDetails?connect_id=" + company.getConnectNo();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        String jsonString = responseEntity.getBody();
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(jsonString);
        Type type1 = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        if (StringUtils.pathEquals(jsonObject.get("status").toString(), "success")) {
            List<Map<String, String>> paymentvoucherList = new ArrayList<>();
            paymentvoucherList = gson.fromJson(jsonObject.get("data").toString(), type1);
            for (Map<String, String> m : paymentvoucherList) {
                PaymentVoucher paymentVoucher = voucherRepository.findAllByCouponid(Long.parseLong(m.get("coupon_id")));
                PaymentVoucherPojo paymentVoucherPojo = new PaymentVoucherPojo();
                if (paymentVoucher != null) {
                    paymentVoucherPojo.setCoupon_id(paymentVoucher.getCouponid());
                }
//                paymentVoucherPojo.setVocherCode(m.get("code"));
//                paymentVoucherPojo.setDiscountAmount(m.get("discount"));
//                paymentVoucherPojo.setMinBill(m.get("min_total"));
//                paymentVoucherPojo.setDiscountType(m.get("type"));
//                paymentVoucherPojo.setFromDate(m.get("date_added"));
//                paymentVoucherPojo.setToDate(m.get("date_added"));
//                paymentVoucherPojo.setStatus("Active");
//                saveVoucher(paymentVoucherPojo);
            }
        }
    }
    public void syncPaymentmethod() throws Exception {
        ResponseEntity<String> responseEntity = null;
        Company company = companyRepository.findAllByStatus("Active");
        String URL = readDomainNameRestoOrder() + "/services/Account/getpaymentmethod?connect_id=" + company.getConnectNo();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        String jsonString = responseEntity.getBody();
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(jsonString);
        Type type1 = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        if (StringUtils.pathEquals(jsonObject.get("status").toString(), "success")) {
            List<Map<String, String>> paymentMethodList = new ArrayList<>();
            paymentMethodList = gson.fromJson(jsonObject.get("data").toString(), type1);
            for (Map<String, String> m : paymentMethodList) {
                PaymentMethod paymentMethod = paymentMethodRepository.findAllByPaymentmethodName(m.get("name"));
                PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
                if (paymentMethod != null) {
                    paymentMethodDTO.setPaymentmethodId(paymentMethod.getPaymentmethodId());
                }
                paymentMethodDTO.setPaymentmethodName(m.get("name"));
                paymentMethodDTO.setPaymentmethodDescription(m.get("name"));
                paymentMethodDTO.setStatus("Active");
                savePaymentmethod(paymentMethodDTO);
            }
        }
    }

//    public void syncCategoryAndItems()throws Exception{
//        ResponseEntity<String> responseEntity = null;
//        Company company=companyRepository.findAllByStatus("Active");
//        String URL=readDomainNameRestoOrder()+"/services/Account/getCategoryAndItem?connect_id="+company.getConnectNo();
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        HttpEntity<String> entity = new HttpEntity<String>(headers);
//        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
//        String jsonString = responseEntity.getBody();
//        Gson gson=new Gson();
//        JSONObject jsonObject = new JSONObject(jsonString);
//        Type type1=new TypeToken<List<Map<String,String>>>(){}.getType();
//        if(StringUtils.pathEquals(jsonObject.get("status").toString(),"success")) {
//            JSONObject jsonObjects = new JSONObject(jsonObject.get("data").toString());
//            List<Map<String, String>> categoryList = new ArrayList<>();
//            categoryList = gson.fromJson(jsonObjects.get("categoryList").toString(), type1);
//            for (Map<String, String> m : categoryList) {
//                Category category = categoryRepository.findAllByName(m.get("name"));
//                CategoryPojo categoryPojo = new CategoryPojo();
//                if (category != null) {
//                    categoryPojo.setCategory_id(category.getCategoryId());
//                }
//                categoryPojo.setName(m.get("name"));
//                categoryPojo.setDescription(m.get("description"));
//                categoryPojo.setStatus("Active");
//                saveNewItemCategory(categoryPojo);
//            }
//            JSONObject itemlist = new JSONObject(jsonObjects.get("itemList").toString());
//            JSONArray key = itemlist.names();
//            for (int i = 0; i < key.length(); ++i) {
//                String keys = key.getString(i);
//                String value = itemlist.getString(keys);
//                List<Map<String, String>> itemsList = new ArrayList<>();
//                itemsList = gson.fromJson(value, type1);
//                for (Map<String, String> item : itemsList) {
//                    Item item1 = itemRespository.findAllByItemName(item.get("menu_name"));
//                    AddNewItemDTO addNewItemDTO = new AddNewItemDTO();
//                    addNewItemDTO.setItemStatus("Active");
//                    if (item1 != null)
//                        addNewItemDTO.setItemId(item1.getItemId());
//                    addNewItemDTO.setItemName(item.get("menu_name"));
//                    addNewItemDTO.setItemCode(item.get("menu_id"));
//                    addNewItemDTO.setItemDesc(item.get("menu_description"));
//                    addNewItemDTO.setSalesPrice(Double.parseDouble(item.get("menu_price")));
//                    addNewItemDTO.setItemName(item.get("menu_name"));
//                    Category category = categoryRepository.findAllByName(keys);
//                    addNewItemDTO.setItemCategory(category);
//                    createSaveNewItemDetails(addNewItemDTO);
//                }
//            }
//        }
//    }
    public List<CustomerPojo> synchronizeCustomer(List<CustomerPojo> customerPojoList) throws Exception {
        // Service Call For PHP
        Company company = companyRepository.findAllByStatus("Active");
        List<CustomerPojo> customerPojos = new ArrayList<>();
//        List<CustomerPojo> customerAccountMasterDTOList = new ArrayList<>();
        for (CustomerPojo customerPojo : customerPojoList) {
//            Country country = hiposDAO.getCountryObj(customerAccountMasterDTO.getCountryId());
//            State state = hiposDAO.getStateObj(customerAccountMasterDTO.getState());
//            Currency currency = hiposDAO.getCurrencyObj(customerAccountMasterDTO.getCurrencyId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("hiconnectNo",company.getConnectNo());
            jsonObject.put("customerName", customerPojo.getFirst_name());
            jsonObject.put("customerEmail", customerPojo.getEmail());
            jsonObject.put("customerContact", customerPojo.getTelephone());
            jsonObject.put("customerAddress",customerPojo.getAddress_1());
//            jsonObject.put("pincode",customerPojo.getPanNo());
//            jsonObject.put("companyRegNo",customerPojo.getCompanyNumber());
            jsonObject.put("notificationFlag",customerPojo.getStatus());
//            jsonObject.put("to_Reg_Comp",customerPojo.getCurrencyCode());
//            jsonObject.put("from_Reg_Comp",customerPojo.getFrom_Reg_Comp());
//            jsonObject.put("gstIn",customerPojo.getGstCode());
//            jsonObject.put("state",customerPojo.getStateId());
//            jsonObject.put("personIncharge",customerPojo.getPersonIncharge());
//            jsonObject.put("country",customerPojo.getCountry());
//            jsonObject.put("currency",customerPojo.getCurrencyWord());
            jsonObject.put("custStatus",customerPojo.getStatus());
//            jsonObject.put("bankName",customerPojo.getBankName());
//            jsonObject.put("discountType",customerPojo.getDiscountType());
//            jsonObject.put("accountNo",customerPojo.getAccountNo());
//            jsonObject.put("branchName",customerPojo.getBranchName());
//            jsonObject.put("website",customerPojo.getWebsite());
//            jsonObject.put("panNumber",customerPojo.getPanNo());
//            jsonObject.put("terms",customerPojo.getTerms());
//            jsonObject.put("creditedLimit",customerPojo.getCreditedLimit());
            System.out.println(jsonObject);
            ResponseEntity<String> responseEntity = null;
            String URL=readDomainNameRestoOrder()+"/services/Account/addCustomer";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
        }


        return customerPojoList;
    }

    public List<CustomerPojo> getCustomerList(String search) {
        List<Customer> list = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isEmpty(search)) {
            list = customerRepository.findAll();
        } else {
            list = customerRepository.findAllByFirstName(search);
        }
        List<CustomerPojo> typePojos = CustomerMapper.mapcustomerEntityToPojo(list);
        return typePojos;
    }


    public List<SalesInvoice> getAllOrderTypes(){
        List<SalesInvoice> salesinvoice=salesInvoiceRepository.findAllByInvoiceType();
        return salesinvoice;
    }
    public AccountType saveAccountType(AccountTypePojo accountTypePojo) {
        AccountType accountType = new AccountType();
        if (accountTypePojo.getAccountId() != null) {
            accountType = accountTypeRepository.findByAccountNameAndAccountIdNotIn(accountTypePojo.getAccountName(), accountTypePojo.getAccountId());
        } else {
            accountType = accountTypeRepository.findByAccountName(accountTypePojo.getAccountName());
        }
        if (accountType == null) {
            AccountType accountType1 = AccountTypeMapper.mapAccountTypePojoToEntity(accountTypePojo);
            accountTypeRepository.save(accountType1);
            return accountType1;
        } else {
            return null;
        }

    }

    public AccountGroup saveAccountGroup(AccountGroupPojo accountGroupPojo) {
        AccountGroup accountGroup = new AccountGroup();
        if (accountGroupPojo.getAccountId() != null) {
            accountGroup = accountGroupRepository.findByAccountNameAndAccountIdNotIn(accountGroupPojo.getAccountName(), accountGroupPojo.getAccountId());
        } else {
            accountGroup = accountGroupRepository.findByAccountName(accountGroupPojo.getAccountName());
        }
        if (accountGroup == null) {
            AccountGroup accountGroup1 = AccountGroupMapper.mapAccountGroupPojoToEntity(accountGroupPojo);
            accountGroupRepository.save(accountGroup1);
            return accountGroup1;
        } else {
            return null;
        }

    }
    public BasePojo getPaginatedAcctGrpList(String status, BasePojo basePojo, String searchText) {
        List<AccountGroup> list = new ArrayList<>();
        basePojo.setPageSize(paginatedConstants);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "accountId"));
        if (basePojo.isLastPage() == true) {
            List<AccountGroup> list1 = new ArrayList<>();
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list1 = accountGroupRepository.findAllByAccountNameContainingAndStatus(searchText, status);
            } else {
                list1 = accountGroupRepository.findAllByStatus(status);
            }
            int size = list1.size() % paginatedConstants;
            int pageNo = list1.size() / paginatedConstants;
            if (size != 0) {
                basePojo.setPageNo(pageNo);
            } else {
                basePojo.setPageNo(pageNo - 1);
            }
            sort = new Sort(new Sort.Order(Sort.Direction.DESC, "accountId"));
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
            status = "Active";
        }
        AccountGroup accountGroup = new AccountGroup();
        Pageable pageable = new PageRequest(basePojo.getPageNo(), basePojo.getPageSize(), sort);
        if (basePojo.isNextPage() == true || basePojo.isFirstPage() == true) {
            sort = new Sort(new Sort.Order(Sort.Direction.ASC, "accountId"));
        }
        if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
            accountGroup = accountGroupRepository.findFirstByAccountNameContainingAndStatus(searchText, status, sort);
            list = accountGroupRepository.findAllByAccountNameContainingAndStatus(searchText, status, pageable);
        } else {
            accountGroup = accountGroupRepository.findFirstByStatus(status, sort);
            list = accountGroupRepository.findAllByStatus(status, pageable);
        }
        if (list.contains(accountGroup)) {
            basePojo.setStatus(true);
        } else {
            basePojo.setStatus(false);
        }
        List<AccountGroupPojo> accountGroupPojos = AccountGroupMapper.mapAccountGroupEntityToPojo(list);
        basePojo = calculatePagination(basePojo, accountGroupPojos.size());
        basePojo.setList(accountGroupPojos);
        return basePojo;
    }


    public BasePojo getPaginatedAcctTypeList(String status, BasePojo basePojo, String searchText) {
        List<AccountType> list = new ArrayList<>();
        basePojo.setPageSize(paginatedConstants);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "accountId"));
        if (basePojo.isLastPage() == true) {
            List<AccountType> list1 = new ArrayList<>();
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list1 = accountTypeRepository.findAllByAccountNameContainingAndStatus(searchText, status);
            } else {
                list1 = accountTypeRepository.findAllByStatus(status);
            }
            int size = list1.size() % paginatedConstants;
            int pageNo = list1.size() / paginatedConstants;
            if (size != 0) {
                basePojo.setPageNo(pageNo);
            } else {
                basePojo.setPageNo(pageNo - 1);
            }
            sort = new Sort(new Sort.Order(Sort.Direction.DESC, "accountId"));
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
            status = "Active";
        }
        AccountType accountType = new AccountType();
        Pageable pageable = new PageRequest(basePojo.getPageNo(), basePojo.getPageSize(), sort);
        if (basePojo.isNextPage() == true || basePojo.isFirstPage() == true) {
            sort = new Sort(new Sort.Order(Sort.Direction.ASC, "accountId"));
        }
        if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
            accountType = accountTypeRepository.findFirstByAccountNameContainingAndStatus(searchText, status, sort);
            list = accountTypeRepository.findAllByAccountNameContainingAndStatus(searchText, status, pageable);
        } else {
            accountType = accountTypeRepository.findFirstByStatus(status, sort);
            list = accountTypeRepository.findAllByStatus(status, pageable);
        }
        if (list.contains(accountType)) {
            basePojo.setStatus(true);
        } else {
            basePojo.setStatus(false);
        }
        List<AccountTypePojo> accountTypePojos = AccountTypeMapper.mapAccountTypeEntityToPojo(list);
        basePojo = calculatePagination(basePojo, accountTypePojos.size());
        basePojo.setList(accountTypePojos);
        return basePojo;
    }
    public Boolean deleteAccountType(AccountTypePojo details) {
        accountTypeRepository.delete(details.getAccountId());
        return true;
    }

    public Boolean deleteAccountGroup(AccountGroupPojo details) {
        accountGroupRepository.delete(details.getAccountId());
        return true;
    }

    public List<AccountTypePojo> getAccountTypeList(String search) {
        List<AccountType> list = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isEmpty(search)) {
            list = accountTypeRepository.findAll();
        } else {
            list = accountTypeRepository.findAllByAccountName(search);
        }
        List<AccountTypePojo> typePojos = AccountTypeMapper.mapAccountTypeEntityToPojo(list);
        return typePojos;
    }

    public List<AccountGroupPojo> getAccountGroupList(String search) {
        List<AccountGroup> list = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isEmpty(search)) {
            list = accountGroupRepository.findAll();
        } else {
            list = accountGroupRepository.findAllByAccountName(search);
        }
        List<AccountGroupPojo> typePojos = AccountGroupMapper.mapAccountGroupEntityToPojo(list);
        return typePojos;
    }

    public OtherContacts saveContact(OtherContactsDTO otherContactsDTO){
        OtherContacts otherContacts = new OtherContacts();
        if (otherContactsDTO.getOtherContactId() != null) {
            otherContacts = contactRepository.findByFullNameAndOtherContactIdNotIn(otherContactsDTO.getFullName(), otherContactsDTO.getOtherContactId());
        } else {
            otherContacts = contactRepository.findByFullName(otherContactsDTO.getFullName());

        }
        if (otherContacts == null) {
            otherContacts = OtherContactMapper.mapContactPojoToEntity(otherContactsDTO);
            contactRepository.save(otherContacts);
            return otherContacts;
        } else {
            return null;
        }
    }
    public Boolean deleteContact(OtherContactsDTO details) {
        contactRepository.delete(details.getOtherContactId());
        return true;
    }
    public List<OtherContactsDTO> getContactsList(String search) {
        List<OtherContacts> list = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isEmpty(search)) {
            list = contactRepository.findAll();
        } else {
            list = contactRepository.findAllByFullName(search);
        }
        List<OtherContactsDTO> otherContactsDTOS = OtherContactMapper.mapContactEntityToPojo(list);
        return otherContactsDTOS;
    }
    public BasePojo getContactList(String status, BasePojo basePojo, String searchText) {
        List<OtherContacts> list = new ArrayList<>();
        basePojo.setPageSize(paginatedConstants);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "otherContactId"));
        if (basePojo.isLastPage() == true) {
            List<OtherContacts> list1 = new ArrayList<>();
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list1 = contactRepository.findAllByFullNameContainingAndStatus(searchText, status);
            } else {
                list1 = contactRepository.findAllByStatus(status);
            }
            int size = list1.size() % paginatedConstants;
            int pageNo = list1.size() / paginatedConstants;
            if (size != 0) {
                basePojo.setPageNo(pageNo);
            } else {
                basePojo.setPageNo(pageNo - 1);
            }
            sort = new Sort(new Sort.Order(Sort.Direction.DESC, "otherContactId"));
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
            status = "Active";
        }
        OtherContacts otherContacts = new OtherContacts();
        Pageable pageable = new PageRequest(basePojo.getPageNo(), basePojo.getPageSize(), sort);
        if (basePojo.isNextPage() == true || basePojo.isFirstPage() == true) {
            sort = new Sort(new Sort.Order(Sort.Direction.ASC, "otherContactId"));
        }
        if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
            otherContacts = contactRepository.findFirstByFullNameContainingAndStatus(searchText, status, sort);
            list = contactRepository.findAllByFullNameContainingAndStatus(searchText, status, pageable);
        } else {
            otherContacts = contactRepository.findFirstByStatus(status, sort);
            list = contactRepository.findAllByStatus(status, pageable);
        }
        if (list.contains(otherContacts)) {
            basePojo.setStatus(true);
        } else {
            basePojo.setStatus(false);
        }
        List<OtherContactsDTO> otherContactsDTOList = OtherContactMapper.mapContactEntityToPojo(list);
        basePojo = calculatePagination(basePojo, otherContactsDTOList.size());
        basePojo.setList(otherContactsDTOList);
        return basePojo;
    }

    public List<AccountMaster> editAccountMAsterObj(Long accountId) {
        List<AccountMaster> createchartofacc = accountMasterRepository.findAllByAccountid(accountId);
        return createchartofacc;
    }

    @Transactional
    public AccountMaster editChartOfAccounts(PostChartOfAccountDto accountDto) {
        AccountMaster accountMaster = null;
        if (accountDto.getAccountid() != null) {
            accountMaster = accountMasterRepository.findOne(accountDto.getAccountid());
            accountMaster.setAccountname(accountDto.getAccountName());
            accountMaster.setAparcode(accountDto.getAccountType());
        }
        accountMasterRepository.save(accountMaster);
        return accountMaster;
    }
    @Transactional
    public List<ListChatOfAccountDto> saveCreateChartAcc(PostChartOfAccountDto accountDto) {
        List<ListChatOfAccountDto> list = null;
        AccountMaster accountMaster = null;
        AccountMaster parentAccountMaster = null;
        Integer autoGenerateSecoundLevelAccCode = 0;
        String substringOfAccCode;
        AccountMaster master = returnAccountMaster(accountDto);
        if (accountDto.getSecoundLevelStringAccCode() != null && accountDto.getFirstLevelStringAccCode() != null && !accountDto.getSecoundLevelStringAccCode().isEmpty() && !accountDto.getFirstLevelStringAccCode().isEmpty()) {
            substringOfAccCode = accountDto.getSecoundLevelStringAccCode().substring(0, accountDto.getSecoundLevelStringAccCode().lastIndexOf("/") + 1);
            accountMaster = maxAccountCodeValue(substringOfAccCode, "ThirdLevel");
            parentAccountMaster = getTopLevelAccountMaster(accountDto.getSecoundLevelAccountId());
            if (accountMaster != null) {
                autoGenerateSecoundLevelAccCode = 1 + Integer.parseInt(accountMaster.getAccountcode());
            } else {
                autoGenerateSecoundLevelAccCode = 0001;
            }
            master.setStringAccountCode(substringOfAccCode + String.format("%04d", autoGenerateSecoundLevelAccCode));
            master.setAmaccountid(parentAccountMaster);
            master.setAccountcode(String.valueOf(String.format("%04d", autoGenerateSecoundLevelAccCode)));
        } else if (accountDto.getFirstLevelStringAccCode() != null && !accountDto.getFirstLevelStringAccCode().isEmpty()) {
            substringOfAccCode = accountDto.getFirstLevelStringAccCode().substring(0, accountDto.getFirstLevelStringAccCode().indexOf("/") + 1);
            accountMaster = maxAccountCodeValue(substringOfAccCode, "SecoundLevel");
            parentAccountMaster = getTopLevelAccountMaster(accountDto.getFirstLevelAccountId());
            if (accountMaster != null) {
                autoGenerateSecoundLevelAccCode = 1 + Integer.parseInt(accountMaster.getAccountcode());
            } else {
                autoGenerateSecoundLevelAccCode = 001;
            }
            master.setStringAccountCode(substringOfAccCode + String.format("%03d", autoGenerateSecoundLevelAccCode) + "/0000");
            master.setAmaccountid(parentAccountMaster);
            master.setAccountcode(String.valueOf(String.format("%03d", autoGenerateSecoundLevelAccCode)));
        } else if (org.apache.commons.lang3.StringUtils.isEmpty(accountDto.getFirstLevelStringAccCode())) {
            AccountGroup accountGroup = accountGroupRepository.findOne(Long.parseLong(accountDto.getAccountGroup()));
            master.setStringAccountCode(accountGroup.getAccountCode() + "/000" + "/0000");
            master.setAmaccountid(parentAccountMaster);
            master.setAccountcode(accountGroup.getAccountCode());
        }
        AccountType accountType = accountTypeRepository.findOne(accountDto.getAccountTypeId());
        master.setReportvalue(accountDto.getReportvalue());
        master.setAccountTypeId(accountType);
        list = saveOrUpDateChartOfAccount(master);
        return list;

    }

    public List<ListChatOfAccountDto> saveOrUpDateChartOfAccount(AccountMaster accountMaster) {
        accountMasterRepository.save(accountMaster);
        return null;
    }

    public AccountMaster getTopLevelAccountMaster(String accountMasterId) {
        AccountMaster accountMaster = accountMasterRepository.findOne(Long.parseLong(accountMasterId));
        return accountMaster;
    }

    public AccountGroup getAccountGroup(Long accountGroupId) {
        AccountGroup accountGroup = accountGroupRepository.findOne(accountGroupId);
        return accountGroup;
    }

    public AccountMaster maxAccountCodeValue(String accountCode, String accountLevel) {
        List<AccountMaster> amList = new ArrayList<>();
        AccountMaster amObj = null;
        Query qry = null;
        try {
            switch (accountLevel) {
                case "ThirdLevel":
                    accountLevel.equals("ThirdLevel");
                    List<AccountMaster> list = accountMasterRepository.findAllBy(accountCode);
                    if (list.size() > 0) {
                        amObj = list.get(0);
                    }
                    break;
                case "SecoundLevel":
                    accountLevel.equals("SecoundLevel");
                    list = accountMasterRepository.findAllBy1(accountCode);
                    if (list.size() > 0) {
                        amObj = list.get(0);
                    }
                    break;
                case "FirstLevel":
                    accountLevel.equals("FirstLevel");
                    list = accountMasterRepository.findAllBy2(accountCode);
                    if (list.size() > 0) {
                        amObj = list.get(0);
                    }
                    break;
            }
//            amObj = accountMasterRepository.findAllBy(accountCode).get(0);
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return amObj;
    }
    public AccountMaster returnAccountMaster(PostChartOfAccountDto accountDto) {
        AccountMaster master = new AccountMaster();
        master.setAccountname(accountDto.getAccountName());
        master.setFlag(true);
        master.setStatus("Active");
        master.setAparcode(accountDto.getAccountType());
        master.setAgid(getAccountGroup(Long.parseLong(accountDto.getAccountGroup())));
        return master;
    }
    public List<ListChatOfAccountDto> getAccountMasterList(String search) {
        List<AccountMaster> list = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isEmpty(search)) {
            list = accountMasterRepository.findAll();
        } else {
            list = accountMasterRepository.findAllByAccountname(search);
        }
        List<ListChatOfAccountDto> typePojos = AccountMasterMapper.mapAccountMasterEntityToPojo(list);
        return typePojos;
    }
    public List<ListChatOfAccountDto> getAccountMasterListBasedOnReport(String search) {
        List<AccountMaster> list = new ArrayList<>();
        list = accountMasterRepository.findAllByReportvalue(search);
        List<ListChatOfAccountDto> typePojos = AccountMasterMapper.mapAccountMasterEntityToPojo(list);
        for (ListChatOfAccountDto accountMaster : typePojos) {
            double totalAmount = 0.0;
            AccountMaster accountMaster1 = accountMasterRepository.findOne(accountMaster.getAccountid());
            List<AccountMaster> accountMaster2 = accountMasterRepository.findAllByAmaccountid(accountMaster1);
            List<SecondChatOfAccountDto> secondLevelList = AccountMasterMapper.mapSecondAccountMasterEntityToPojo(accountMaster2);
            accountMaster.setSecondChatOfAccountDtoList(secondLevelList);
            for (SecondChatOfAccountDto secondChatOfAccountDto : secondLevelList) {
                double firstTotalAmt = 0.0;
                List<GLTransactions> glTransactions = glTransactionRepository.findAllByAccount(accountMasterRepository.findOne(secondChatOfAccountDto.getAccountid()));
                secondChatOfAccountDto.setGldetails(glTransactions);
                for (GLTransactions glTransactions1 : secondChatOfAccountDto.getGldetails()) {
                    firstTotalAmt = glTransactions1.getAmount().doubleValue() + firstTotalAmt;
                    secondChatOfAccountDto.setTotalAmt(firstTotalAmt);
                }
                List<AccountMaster> secondaccountMaster = accountMasterRepository.findAllByAmaccountid(accountMaster1);
                List<AccountMaster> secondaccountMasterList = accountMasterRepository.findAllByAmaccountid(secondaccountMaster.get(0));
                List<ThirdChatOfAccountDto> thirdLevelList = AccountMasterMapper.mapThirdAccountMasterEntityToPojo(secondaccountMasterList);
                accountMaster.setThirdChatOfAccountDtoList(thirdLevelList);
                for (ThirdChatOfAccountDto thirdChatOfAccountDto : thirdLevelList) {
                    double secondTotalAmt = 0.0;
                    List<GLTransactions> thirdGlTransactions = glTransactionRepository.findAllByAccount(accountMasterRepository.findOne(thirdChatOfAccountDto.getAccountid()));
                    thirdChatOfAccountDto.setGldetails(thirdGlTransactions);
                    for (GLTransactions glTransactions1 : thirdChatOfAccountDto.getGldetails()) {
                        secondTotalAmt = glTransactions1.getAmount().doubleValue() + secondTotalAmt;
                        thirdChatOfAccountDto.setTotalAmt(secondTotalAmt);
                    }
                }
            }
            List<GLTransactions> glTransactions = glTransactionRepository.findAllByAccount(accountMasterRepository.findOne(accountMaster.getAccountid()));
            accountMaster.setGldetails(glTransactions);
            for (SecondChatOfAccountDto glTransactions1 : accountMaster.getSecondChatOfAccountDtoList()) {
                totalAmount = glTransactions1.getTotalAmt() + totalAmount;
                accountMaster.setTotalAmt(totalAmount);
            }
            if (accountMaster.getThirdChatOfAccountDtoList() != null) {
                for (ThirdChatOfAccountDto thirdChatOfAccountDto : accountMaster.getThirdChatOfAccountDtoList()) {
                    totalAmount = thirdChatOfAccountDto.getTotalAmt() + totalAmount;
                    accountMaster.setTotalAmt(totalAmount);
                }
            }
            for (GLTransactions glTransactions1 : accountMaster.getGldetails()) {
                totalAmount = glTransactions1.getAmount().doubleValue() + totalAmount;
                accountMaster.setTotalAmt(totalAmount);
            }
        }
        return typePojos;
    }
    public List<ListChatOfAccountDto> getFirstLevelAccountMaster(Long id) {
        AccountMaster accountMaster = accountMasterRepository.findOne(id);
        AccountGroup accountGroup = accountGroupRepository.findOne(accountMaster.getAccountid());
        List<AccountMaster> accountMasters = accountMasterRepository.findByAgidAndAmaccountidIsNull(accountGroup);
        List<ListChatOfAccountDto> list = AccountMasterMapper.mapChartOfAccEntityToPojo(accountMasters);
        return list;
    }

    public List<ListChatOfAccountDto> secoundLevelChartOfAccount(Long firstLevelId) {
        AccountMaster accountMaster = accountMasterRepository.findOne(firstLevelId);
        List<AccountMaster> accountMaster1 = accountMasterRepository.findAllByAmaccountid(accountMaster);
        List<ListChatOfAccountDto> list = AccountMasterMapper.mapChartOfAccEntityToPojo(accountMaster1);
        return list;
    }
    public List<AccountMasterDTO> retrieveAccountMasterList(String accountCode) {
        List<AccountMaster> accountMaster = accountMasterRepository.findAllByAccountid(Long.parseLong(accountCode));
        List<AccountMasterDTO> accountMasterDTOList = AccountMasterMapper.mapAccMasterEntityToPojo(accountMaster);
        return accountMasterDTOList;
    }

    public AirPayDetails getPosTransDetails(String merchantId)throws Exception{
        AirPayDetails airPayDetails = null;
        ResponseEntity<String> responseEntity = null;
        String URL ="http://pay.restopos.in/dashdoard/getSwipePayDetails";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(merchantId, headers);
        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        if(responseEntity.getBody()!=null){
            Gson gson = new Gson();
            airPayDetails= gson.fromJson(responseEntity.getBody(),AirPayDetails.class);
            String delUrl = "http://pay.restopos.in/dashdoard/inactiveSwipePay";
            restTemplate = new RestTemplate();
            headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entities = new HttpEntity<String>(airPayDetails.getAirPayId().toString(), headers);
            responseEntity = restTemplate.exchange(delUrl, HttpMethod.POST, entities, String.class);
            airPayDetails.setAirPayId(null);
            //airPayDetails.setPaymentId(paymentId);
            airPayDetailsRepository.save(airPayDetails);
        }
        System.out.println(merchantId);
        System.out.println(airPayDetails+"service");
        return airPayDetails;

    }


    public AccountSetupPojo createSaveNewPosConfigDetails(AccountSetupPojo accountSetupPojo){
        AccountSetup account = new AccountSetup();
        AccountSetupPojo accounts = new AccountSetupPojo();
            account = AccountSetupMapper.MapPojoToEntity(accountSetupPojo);
            accountSetupRepository.save(account);
            accounts = AccountSetupMapper.MapEntityToPojo(account);
            return accounts;

    }
    public AccountSetupPojo getConfigurationList() {
        AccountSetup accountSetup = accountSetupRepository.findOne(1L);
        if(accountSetup!=null) {
            AccountSetupPojo accountSetupPojo = AccountSetupMapper.MapEntityToPojo(accountSetup);
            accountSetupPojo.setBuildTypeCloud(printerType.equalsIgnoreCase("cloud"));
            return accountSetupPojo;
        }else{
            return null;
        }
    }
    public List<AccountSetupPojo> getConfiguration() {
        AccountSetup accountSetup = accountSetupRepository.findOne(1L);
        if(accountSetup!=null) {
            List<AccountSetupPojo> pojo = new ArrayList<>();
            AccountSetupPojo accountSetupPojo = AccountSetupMapper.MapEntityToPojo(accountSetup);
            pojo.add(accountSetupPojo);
            return pojo;
        }else{
            return null;
        }
    }
    public List<StatePojo> getCountryState(Long countryName) {
        List<State> states = stateRepository.findAllByCountryIdAndStatus(countryName, "Active");
        List<StatePojo> stateList = StateMapper.mapStateEntityToPojo(states);
        return stateList;
    }
    public BasePosPrinterService getMobilePrinterService() {
        BasePosPrinterService posPrinterService = null;
        try {
            AccountSetup accountSetup = accountSetupRepository.findOne(1L);
            //holds the reference of stored Printer Layout Display
            String val = accountSetup.getPrinterModelMobile();
            //holds the reference of parsed string
            if(!StringUtils.isEmpty(val)) {
                String result = val.replaceAll("[\\[\\]{}]", "");
                //holds the reference of parsed string Printer Layout JSON Array to JSON Object
                String printerModelList = "{" + result + "}";
                //holds the reference of kot printer <-> print model
                JSONObject printerModelMap = new JSONObject(printerModelList);
                if (!StringUtils.isEmpty(accountSetup.getMobileBillPrinter())) {
                    String printerClass = printerModelMap.getString(accountSetup.getMobileBillPrinter());
                    posPrinterService = getBasePrinterServiceObj(printerClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posPrinterService;
    }
    public BasePosPrinterService getBasePrinterServiceObj(String printerClass){
        //TVSRP3150,EPSONTMT82,GENERIC58MM
        switch (printerClass){
            case "TVSRP3150": return tvsRP3150PosPrinterService;
            case "EPSONTMT82": return epsonPosPrinterService;
            case "GENERIC58MM": return generic58PosPrinterService;
        }
        return null;
    }
    public void printHTML(String htmlString, String cssStyle, String printerType) {
        try {
            if (buildType.equals("cloud")) {
                final String uri = "http://localhost:9001/hiAccounts/sendDatatoLocalPrinter";
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("printerType", printerType);
                objectNode.put("htmlString", htmlString);
                objectNode.put("cssStyle", "");
                HttpEntity entity = new HttpEntity(objectNode.toString(), headers);
                restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            } else {
                Reader stringReader = new StringReader(htmlString);
                HTMLEditorKit htmlKit = new HTMLEditorKit();
                StyleSheet styleSheet = htmlKit.getStyleSheet();
                styleSheet.addRule(cssStyle);
                HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
                htmlKit.read(stringReader, htmlDoc, 0);
                DocumentRenderer doc = new DocumentRenderer();
                //get all printers installed
                PrintService[] printServiceList = PrintServiceLookup.lookupPrintServices(null, null);
                for (PrintService printService : printServiceList) {
                    if (printService.getName().equals(printerType)) {
                        doc.setPrintService(printService);
                        doc.print(htmlDoc);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("~~~");
            System.out.println("Printer Not Connected");
        }
    }
    public BasePosPrinterService getReportPrinterService(String tableNo) {
        BasePosPrinterService posPrinterService = null;
        try {
            AccountSetup accountSetup = accountSetupRepository.findOne(1L);
            //holds the reference of stored Printer Layout Display
            String val = accountSetup.getPrinterModel();
            //holds the reference of parsed string
            if(!StringUtils.isEmpty(val)) {
                String result = val.replaceAll("[\\[\\]{}]", "");
                //holds the reference of parsed string Printer Layout JSON Array to JSON Object
                String printerModelList = "{" + result + "}";
                //holds the reference of kot printer <-> print model
                UserAccountSetup userAccountSetup=userAccountSetupRepository.findOne(1);
                JSONObject printerModelMap = new JSONObject(printerModelList);
                TablesPos tablesPos=null;
                TableConfig tableConfig=null;
                Gson gson = new Gson();
                List<PosPrinterDto> posPrinterDtos = new ArrayList<>();
                Type type = new TypeToken<List<PosPrinterDto>>() {
                }.getType();
                if (!StringUtils.isEmpty(accountSetup.getReportPrinter())) {
                    posPrinterDtos = gson.fromJson(accountSetup.getReportPrinter(), type);
                    String printerName=null;
                    if(!StringUtils.isEmpty(tableNo)) {
                        tablesPos = getTablePosObj(tableNo);
                        tableConfig = tableConfigRepository.findAllByConfigurationname(tablesPos.getConfigurationname());
                        for(PosPrinterDto posPrinterDto:posPrinterDtos){
                            if(tableConfig!=null)
                                if(StringUtils.pathEquals(posPrinterDto.getTableConfig(),tableConfig.getTableconfigid().toString())&&StringUtils.pathEquals(posPrinterDto.getUser(),userAccountSetup.getFullName())){
                                    printerName=posPrinterDto.getPosPrinter();
                                }
                        }
                    }
                    if(StringUtils.isEmpty(printerName)){
                        printerName=posPrinterDtos.get(0).getPosPrinter();
                    }
                    String printerClass = printerModelMap.getString(printerName);
                    posPrinterService = getBasePrinterServiceObj(printerClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posPrinterService;
    }
    public Map  mapKitchenOrderToPrintersAndPrint(List<POSKOTItemOrderDTO> kitchenAllOrders,String tableName,String waiterName,String tokenNo,String customerName,String instructions,String orderNo,String pax,String agentName){
        Map<String,List<String>> cloudData = new HashMap();
        try
        {
            /***
             *
             * 	key:
             ---
             ---BAR		<--------------- BAR WHISKEY		<--------- Child Category
             |	CHINESE					CHINESE Chicken
             |	INDIAN					INDIAN palav
             |	TANDOOR					TANDOOR Chicken
             |
             |
             |
             |	key:
             |	---
             |-->BAR				-->		RP3150 STAR(E)                   --->
             TANDOOR					    EPSON TM-T82 Receipt             	|
             undefined				    POS-58-Series		    	   	    |
             |
             key:															|
             ---																|
             |
             ------------------------------------------------------------------ |
             |
             |		POS-58-Series			GENERIC58M
             --->	RP3150 STAR(E)			TVSRP3150   --------> Printer Display Model Class
             EPSON TM-T82 Receipt	EPSONTMT82
             *
             *
             *
             *
             */


            //holds the reference of kitchen orders based on category
            Map<String, List<POSKOTItemOrderDTO>> groupedKitchenOrder =
                    kitchenAllOrders.parallelStream().collect(Collectors.groupingBy(POSKOTItemOrderDTO::getItemCategoryName));

            //holds the reference of account setup
            AccountSetup accountSetup = accountSetupRepository.findOne(1L);
            //holds the reference of stored KOT Printer Configuration
            String val = accountSetup.getKotPrinter();
            //holds the reference of parsed string
            String result = val.replaceAll("[\\[\\]{}]", "");
            //holds the reference of parsed string KOT Printer JSON Array to JSON Object
            String kotPrinterList = "{" + result + "}";
            //holds the reference of stored Printer Layout Display
            val = accountSetup.getPrinterModel();
            //holds the reference of parsed string
            result = val.replaceAll("[\\[\\]{}]", "");
            //holds the reference of parsed string Printer Layout JSON Array to JSON Object
            String printerModelList = "{" + result + "}";

            //holds the reference of category <-> kot printer
            JSONObject kotPrinterMap = new JSONObject(kotPrinterList);
            //holds the reference of kot printer <-> print model
            JSONObject printerModelMap = new JSONObject(printerModelList);
            //holds the reference of parent n child category mapping
            JSONObject parentChildConfiguration = generic58PosPrinterService.getParentChildConfiguration();
            //holds the reference of kitchen orders based on category
            List<POSKOTItemOrderDTO> kitchenOrderGroup = new ArrayList<>();
            //holds the reference of 'print model' <-> category based group order
            Map<String, List<POSKOTItemOrderDTO>> printConfCategoryMap = new HashMap<>();
            //holds reference of parent child configuration key
            Iterator configIterator;
            //holds reference to keys of kitchen orders based on category
            Set<String> categoryList = groupedKitchenOrder.keySet();
            //Iterate category list (parent/child)
            for (String category : categoryList) {
                configIterator = parentChildConfiguration.keys();
                String printerClass = "";
                if (configIterator.hasNext()) {
                    while (configIterator.hasNext()) {
                        String key = String.valueOf(configIterator.next());
                        String childCategories = parentChildConfiguration.getJSONArray(key).toString();
                        String compareA = category.replaceAll("\\s+","").toLowerCase();
                        String compareB = childCategories.replaceAll("\\s+","").toLowerCase();
                        if (compareB.contains(compareA)) {
                            if (!kotPrinterMap.has(key)) {
                                //if no printer defined then print to 'undefined'
                                key = "undefined";
                            }
                            printerClass = printerModelMap.getString(kotPrinterMap.getString(key));
                            kitchenOrderGroup = printConfCategoryMap.get(printerClass);
                            if (kitchenOrderGroup == null) {
                                kitchenOrderGroup = new ArrayList<>();
                            }
                            kitchenOrderGroup.addAll(groupedKitchenOrder.get(category));
                            printConfCategoryMap.put(printerClass, kitchenOrderGroup);
                            logger.info("Category: " + category + " Printer Class: " + printerClass);
                            break;
                        }
                    }
                } else {
                    String key = category;
                    if (!kotPrinterMap.has(key)) {
                        //if no printer defined then print to 'undefined'
                        key = "undefined";
                    }
                    printerClass = printerModelMap.getString(kotPrinterMap.getString(key));
                    kitchenOrderGroup = printConfCategoryMap.get(printerClass);
                    if (kitchenOrderGroup == null) {
                        kitchenOrderGroup = new ArrayList<>();
                    }
                    kitchenOrderGroup.addAll(groupedKitchenOrder.get(category));
                    printConfCategoryMap.put(printerClass, kitchenOrderGroup);
                    logger.info("Category: " + category + " Printer Class: " + printerClass);
                }
            }
            Set<String> printerClassSet = printConfCategoryMap.keySet();
            for (String printerClass : printerClassSet) {
                logger.info("Printer Class: "+printerClass);
                BasePosPrinterService posPrinterService = getBasePrinterServiceObj(printerClass);
                kitchenOrderGroup = printConfCategoryMap.get(printerClass);
                List<POSKOTItemOrderDTO> kitchenOrders = kitchenOrderGroup.stream().filter(kitchenOrder -> kitchenOrder.getType().equals("Order")).collect(Collectors.toList());
                List<POSKOTItemOrderDTO> cancelOrders = kitchenOrderGroup.stream().filter(kitchenOrder -> kitchenOrder.getType().equals("Cancel")).collect(Collectors.toList());
                if (kitchenOrders.size() > 0) {
                    Map<String, List<String>> result1 = posPrinterService.placeOrdersToKOT(kitchenOrders, tableName, waiterName, tokenNo, "Order", customerName,  instructions, orderNo, pax,agentName);
                    for (String key : result1.keySet()) {
                        List<String> values = cloudData.get(key);
                        if (values == null) {
                            values = result1.get(key);
                            cloudData.put(key, values);
                        } else {
                            values = result1.get(key);
                        }
                    }
                }
                if (cancelOrders.size() > 0) {
                    Map<String, List<String>> result2 = posPrinterService.placeOrdersToKOT(cancelOrders, tableName, waiterName, tokenNo, "Cancel", customerName,  instructions, orderNo, pax,agentName);
                    for (String key : result2.keySet()) {
                        List<String> values = cloudData.get(key);
                        if (values == null) {
                            values = result2.get(key);
                            cloudData.put(key, values);
                        } else {
                            values = result2.get(key);
                        }
                    }
                }

            }


        }catch(Exception e){
            e.printStackTrace();
        }
        return cloudData;
    }


    public SMSServer savesms(SmsServerDto dto) {

        SMSServer smsServer = new SMSServer();
        smsServer = SmsServerMapper.MapsmsserverPojoToEntity(dto);
        smsServerRepository.save(smsServer);
        return smsServer;
    }

    public List<SmsServerDto> getsmslist(String searchText) {

        List<SMSServer> smsServer = new ArrayList<>();
        SMSServer smsServers = new SMSServer();
        if (!StringUtils.isEmpty(searchText)) {

            smsServer = smsServerRepository.findAllByApiKey(searchText);
        } else {
            smsServers = smsServerRepository.findOne(1L);
            smsServer.add(smsServers);
        }
        if (smsServers != null) {
            List<SmsServerDto> list = ObjectMapperUtils.mapAll(smsServer, SmsServerDto.class);
            return list;
        }
        return null;
    }
    public String saveKitchen(KitchenMasterPOJO kitchenMasterPOJO)throws Exception{
        if(StringUtils.pathEquals(buildType,"desktop")) {
            KitchenMaster kitchenMaster = new KitchenMaster();
            if (kitchenMasterPOJO.getKitchenId() != null) {
                kitchenMaster = kitchenRepository.findAllByKitchenNameAndKitchenIdNotIn(kitchenMasterPOJO.getKitchenName(), kitchenMasterPOJO.getKitchenId());
            } else {
                kitchenMaster = kitchenRepository.findAllByKitchenName(kitchenMasterPOJO.getKitchenName());

            }
            JSONObject jsonObject = new JSONObject();
            if (kitchenMaster == null) {
                kitchenMaster = KitchenMapper.MapPojoToEntity(kitchenMasterPOJO);
                kitchenRepository.save(kitchenMaster);
                jsonObject.put("status", "success");
                jsonObject.put("message", "Saved Successfully");
                return jsonObject.toString();
            } else {
                jsonObject.put("status", "fail");
                jsonObject.put("message", "Duplicate Entry");
                return jsonObject.toString();
            }
        }
        else{

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id","4XqbGS");
            jsonObject.put("location_connect_id","4XqbGS49");
            jsonObject.put("kitchenId",kitchenMasterPOJO.getKitchenId());
            jsonObject.put("kitchenName",kitchenMasterPOJO.getKitchenName());
            jsonObject.put("description",kitchenMasterPOJO.getDescription());
            jsonObject.put("status",kitchenMasterPOJO.getStatus());
            String url = readDomainName() + "/services/Litecall/saveKitchen";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Gson gson = new Gson();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString1 = responseEntity.getBody();
            return jsonString1;

        }
    }

    public String getPaginatedPaymentMethodList(String status, String searchText, int pageNo)throws Exception {
        if (StringUtils.pathEquals(buildType, "desktop")) {
        List<PaymentMethod> list = new ArrayList<>();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "paymentmethodId"));
        if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "1";
            }
            Pageable pageable = new PageRequest(pageNo - 1, paginatedConstants, sort);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = paymentMethodRepository.findAllByPaymentmethodNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = paymentMethodRepository.findAllByStatus(status, pageable);
            }
            List<PaymentMethodDTO> PojoList = PaymentMethodMapper.mapPaymentMethodEntityToPojo(list);
            List<PaymentMethod> list1 = paymentMethodRepository.findAllByPaymentmethodNameContainingAndStatus(searchText, status);
            int size = list1.size() / paginatedConstants;
            int number = list1.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            String url = readDomainName() + "/services/Litecall/PaymentMethodList?page=" + pageNo +"&status="+pageNo + "&searchText="+searchText;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }





    public String getKitchenpagelist(String status, String searchText, int pageNo)throws Exception {
        if (StringUtils.pathEquals(buildType, "desktop")) {
            List<KitchenMaster> list = new ArrayList<>();
            Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "kitchenId"));
            if (org.apache.commons.lang3.StringUtils.isEmpty(status)) {
                status = "Active";
            }
            Pageable pageable = new PageRequest(pageNo - 1, paginatedConstants, sort);
            if (!org.apache.commons.lang3.StringUtils.isEmpty(searchText)) {
                list = kitchenRepository.findAllByKitchenNameContainingAndStatus(searchText, status, pageable);
            } else {
                list = kitchenRepository.findAllByStatus(status, pageable);
            }
            List<KitchenMasterPOJO> PojoList = KitchenMapper.mapEntityToPojo(list);
            List<KitchenMaster> list1 = kitchenRepository.findAllByKitchenNameContainingAndStatus(searchText, status);
            int size = list1.size() / paginatedConstants;
            int number = list1.size() % paginatedConstants;
            if (number >= 1) {
                size = size + 1;
            }
            Map m = new HashMap();
            m.put("data", PojoList);
            m.put("no_of_paginations", size);
            Gson json = new Gson();
            return json.toJson(m);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Restaurant_id", "4XqbGS");
            jsonObject.put("location_connect_id", "4XqbGS49");
            String url = readDomainName() + "/services/Litecall/KitchenList?page=" + pageNo +"&status="+status + "&searchText="+searchText;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String jsonString = responseEntity.getBody();
            return jsonString;
        }
    }


}
