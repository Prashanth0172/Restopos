/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.rest.pojo;



import com.hyva.restopos.rest.entities.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
@Data
public class PageLoadData extends AbstractResponse{

    private UserAccessRightsDTO userAccessRights;
    private List<CustomerPojo> customers = new ArrayList<>();
    private List<CategoryPojo> itemCategorys = new ArrayList<>();
    private String companyLogoPath;
    private String fullUserName;
    private String password;
    private String companyName;
    private List<TableConfigPojo> tableConfigDetails= new ArrayList<>();
    private List<EmployeePojo> employeeList=new ArrayList<>();
    private List<AgentPojo> agentList = new ArrayList<>();
    private String menuTypeGold;
    private String menuTypeResturant;
    private String menuTypePharma;
    private String menuTypeMobile;
    private Long countryId;
    private Long cmpyCurrencyId;
    private String userLoginId;
    private boolean buildTypeCloud;
    private String defaultEmployee;
    private String LocationRights;
    private String InventorylocationPhoneNumber;
}
