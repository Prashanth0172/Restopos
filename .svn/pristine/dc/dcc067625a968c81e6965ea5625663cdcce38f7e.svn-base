package com.hyva.restopos.rest.Hiposservice;

import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class BasicDataService {
    @Autowired
    TablesPosRepository tablesPosRepository;
    @Autowired
    TableConfigRepository tableConfigRepository;
    @Autowired
    HiposService hiposService;
    @Autowired
    FormSetupRepository formSetupRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    UserAccountSetupRepository userAccountSetupRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public void pushData() {
        List<PaymentMethod> paymentMethods=paymentMethodRepository.findAll();
        if(paymentMethods.size()==0) {
            paymentMethodadd("Cash", "Cash", "Cash");
            paymentMethodadd("Bank", "Bank", "Bank");
            paymentMethodadd("Card", "Card", "Card");
            paymentMethodadd("Discount Voucher", "Voucher", "Voucher");
        }
//        List<UserAccountSetup> userAccountSetupList=userAccountSetupRepository.findAll();
//        if(userAccountSetupList.size()==0) {
//            UserAccountSetupAdd("admin@restopos.in", "admin", "21232f297a57a5a743894a0e4a801fc3","Active");
//        }
        List<Category> categoryList=categoryRepository.findAll();
        if(categoryList.size()==0) {
            CategoryAdd(1L, "001", "Manufacturing","Manufacturing","Active","true");
        }
        List<Employee> employeeList=employeeRepository.findAll();
        if(employeeList.size()==0){
            employeeAdd("Digiwaiter","001","Active");
        }
        if(categoryList.size()==0) {
            CategoryAdd(1L, "001", "Manufacturing","Manufacturing","Active","true");
        }
        List<Customer> customerList=customerRepository.findAll();
        if(customerList.size()==0) {
            CustomerAdd(1L, "Cash Customer", "customer@gmail.com","Active");
        }
        List<FormSetUp> formSetUp=formSetupRepository.findAll();
        if(formSetUp.size()==0) {
            insertFormSetUp("DirectSalesInvoice", "DSINV", "00000000", true, "AR");
            insertFormSetUp("JournalEntry", "JE", "00000000", true, "AR");
            insertFormSetUp("OtherPurchase", "OPN", "00000000", true, "AR");
            insertFormSetUp("OtherReceipt", "ORN", "00000000", true, "AR");
        }
        List<TableConfig> tableConfigList = tableConfigRepository.findAll();
        if (tableConfigList.size()==0) {
            getTableConfigData("DineIn", 1, 1, "Active");
            getTableConfigData("TakeAWay", 1, 1, "OrderType");
            getTableConfigData("Delivery", 1, 1, "OrderType");
        }
        List<TablesPos> tablesPosList = tablesPosRepository.findAll();
        if (tablesPosList.isEmpty()) {
            getTableData("DineIn", 1, 1, "WalkIn", "Active");
            getTableData("TakeAWay", 1, 1, "TAKEAWAY", "OrderType");
            getTableData("Delivery", 1, 1, "Delivery", "OrderType");
        }
    }
    private void paymentMethodadd(String name, String description, String type) {
        PaymentMethod p = new PaymentMethod();
        p.setPaymentmethodName(name);
        p.setPaymentmethodDescription(description);
        p.setPaymentmethodType(type);
        p.setStatus("Active");
        p.setDefaultType("false");
        paymentMethodRepository.save(p);
    }
//    private void UserAccountSetupAdd(String email, String fullName, String passwordUser,String status) {
//        UserAccountSetup userAccountSetup = new UserAccountSetup();
//        userAccountSetup.setEmail(email);
//        userAccountSetup.setFullName(fullName);
//        userAccountSetup.setPasswordUser(passwordUser);
//        userAccountSetup.setStatus("Active");
//        userAccountSetupRepository.save(userAccountSetup);
//    }
    private void CategoryAdd(Long itemCategoryId, String itemCategoryCode, String itemCategoryDesc ,String itemCategoryName,String status,String defaultType) {
        Category category = new Category();
        category.setItemCategoryId(itemCategoryId);
        category.setCode(itemCategoryCode);
        category.setItemCategoryName(itemCategoryName);
        category.setItemCategoryDesc(itemCategoryDesc);
        category.setStatus(status);
        categoryRepository.save(category);
    }
    private void employeeAdd(String employeeName,String employeeCode,String status){
        Employee employee = new Employee();
        employee.setEmployeeName(employeeName);
//        employee.setEmployeeCode(employeeCode);
        employee.setStatus(status);
        employeeRepository.save(employee);
    }
    private void CustomerAdd(Long customerId, String customerName, String email,String status) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFirstName(customerName);
        customer.setEmail(email);
        customer.setStatus(status);
        customerRepository.save(customer);
    }
    public void insertFormSetUp(String typename, String typeprefix, String nextref, boolean include_date, String transactionType) {
        FormSetUp c = new FormSetUp();
        c.setTypename(typename);
        c.setTypeprefix(typeprefix);
        c.setNextref(nextref);
        c.setTransactionType(transactionType);
        c.setInclude_date(include_date);
        formSetupRepository.save(c);
    }

    public void getTableConfigData(String tableConfigName, int noOfRows, int noOfColumns, String status) {
        TableConfig tableConfiguration = new TableConfig();
        tableConfiguration.setConfigurationname(tableConfigName);
        tableConfiguration.setRowtableconfig(noOfRows);
        tableConfiguration.setColumntableconfig(noOfColumns);
        tableConfiguration.setStatus(status);
        tableConfigRepository.save(tableConfiguration);
    }

    public void getTableData(String tableConfigName, int noOfRows, int noOfColumns, String tableName, String status) {
        TablesPos tablesPos = new TablesPos();
        tablesPos.setTableName(tableName);
        tablesPos.setGridLocationH(noOfRows);
        tablesPos.setGridLocationV(noOfColumns);
        tablesPos.setConfigurationname(tableConfigName);
        TableConfig tableConfig = hiposService.tableConfigurationByTableName(tableConfigName);
//        tablesPos.setTableConfig(tableConfig);
        tablesPos.setStatus(status);
        tablesPos.setTableStatus("Empty");
        tablesPosRepository.save(tablesPos);
    }

}


















